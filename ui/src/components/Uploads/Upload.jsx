// UploadModal.js
import React, { useState, useCallback, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { selectUserId, selectToken, selectTokenExpiration } from "../../store/useSelectors";
import { clearUser } from "../../store/userSlice";
import './Upload.css'; // Your CSS file for modal styling



const Upload = ({ handleClose }) => {
  const [submitData, setSubmitData] = useState({ jobType: "", file: null, jobTitle: "" });
  const [errors, setErrors] = useState({ TitleError: "", optionError: "", fileError: "" });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const dispatch = useDispatch();

  const userId = useSelector(selectUserId);
  const token = useSelector(selectToken);
  const expirationTime = useSelector(selectTokenExpiration);

  const validateFile = (file) => {
    const allowedTypes = ["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"];
    const maxSize = 5 * 1024 * 1024; // 5MB

    if (!allowedTypes.includes(file.type)) return "Invalid file type. Only .xlsx files are allowed.";
    if (file.size > maxSize) return "File size exceeds 5MB.";
    return "";
  };

  const validate = () => submitData.file && submitData.jobTitle && submitData.jobType;

  const uploadData = useCallback(
    async (signal) => {
      if (validate()) {
        setIsSubmitting(true);
        const formData = new FormData();
        formData.append("jobType", submitData.jobType);
        formData.append("file", submitData.file);
        formData.append("jobTitle", submitData.jobTitle);

        try {
          const response = await fetch(`http://udise.pytosoft.com/v1/fileupload/${userId}/upload`, {
            method: "POST",
            body: formData,
            headers: { Authorization: `Bearer ${token}` },
            signal,
          });

          if (!response.ok) throw new Error("Failed to upload");

          setSubmitData({ jobType: "", file: null, jobTitle: "" });
          console.log(await response.json());
        } catch (error) {
          if (error.name === "AbortError") {
            console.log("Request aborted");
          } else {
            console.error("Error uploading file:", error);
            alert("An error occurred while uploading the file");
          }
        } finally {
          setIsSubmitting(false);
        }
      }
    },
    [submitData, userId, token]
  );

  const handleSubmit = (event) => {
    event.preventDefault();
    const newErrors = { TitleError: "", optionError: "", fileError: "" };
    let hasError = false;

    if (!submitData.jobTitle) {
      newErrors.TitleError = "Please provide a job title";
      hasError = true;
    }
    if (!submitData.jobType) {
      newErrors.optionError = "Please select a job type";
      hasError = true;
    }
    if (!submitData.file) {
      newErrors.fileError = "Please choose a file";
      hasError = true;
    } else {
      const fileValidationError = validateFile(submitData.file);
      if (fileValidationError) {
        newErrors.fileError = fileValidationError;
        hasError = true;
      }
    }

    setErrors(newErrors);

    if (!hasError) {
      const controller = new AbortController();
      uploadData(controller.signal);
      return () => controller.abort();
    }
  };

  useEffect(() => {
    if (Date.now() >= expirationTime || !token || !userId) {
      dispatch(clearUser());
    }
  }, [dispatch, expirationTime, token, userId]);

  useEffect(() => {
    setErrors({ TitleError: "", optionError: "", fileError: "" });
  }, [submitData]);

  return (
    <div className="modal-overlay" onClick={handleClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <form onSubmit={handleSubmit} className="p-4 shadow rounded bg-white">
          <div className="mb-3">
            <label htmlFor="jobTitle" className="form-label">Job Title</label>
            <button className="close-btn" onClick={handleClose}>X</button> {/* Close button */}
            <input
              id="jobTitle"
              type="text"
              className={`form-control ${errors.TitleError && "is-invalid"}`}
              value={submitData.jobTitle}
              onChange={(e) => setSubmitData({ ...submitData, jobTitle: e.target.value })}
            />
            {errors.TitleError && <div className="invalid-feedback">{errors.TitleError}</div>}
          </div>

          <div className="mb-3">
            <label htmlFor="dropdown" className="form-label">Job Type</label>
            <select
              id="dropdown"
              className={`form-select ${errors.optionError && "is-invalid"}`}
              value={submitData.jobType}
              onChange={(e) => setSubmitData({ ...submitData, jobType: e.target.value })}
            >
              <option value="" disabled>Select an option</option>
              <option value="PROGRESSION_ACTIVITY">PROGRESSION_ACTIVITY</option>
              <option value="ADD_NEW_STUDENTS">ADD_NEW_STUDENTS</option>
              <option value="UPDATE_STUDENTS">UPDATE_STUDENTS</option>
            </select>
            {errors.optionError && <div className="invalid-feedback">{errors.optionError}</div>}
          </div>

          <div className="mb-3">
            <label htmlFor="fileInput" className="form-label">Choose File</label>
            <input
              id="fileInput"
              type="file"
              className={`form-control ${errors.fileError && "is-invalid"}`}
              onChange={(e) => setSubmitData({ ...submitData, file: e.target.files[0] })}
            />
            {errors.fileError && <div className="invalid-feedback">{errors.fileError}</div>}
          </div>

          {/* Download Sample File button */}
          <a href="samplefile.xlsx" download="sample_file.xlsx">
            <button
              type="button"
              className="btn btn-secondary w-100 mb-3"
            >
              Download Sample File
            </button>
          </a>

          <button type="submit" className="btn btn-success w-100" disabled={isSubmitting}>
            {isSubmitting ? "Uploading..." : "Submit"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default Upload;
