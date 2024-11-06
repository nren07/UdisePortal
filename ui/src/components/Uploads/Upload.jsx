import React, { useState, useCallback, useEffect } from "react";
import './Upload.css'
import { useSelector, useDispatch } from "react-redux";
import { selectUserId, selectToken, selectTokenExpiration } from "../../store/useSelectors";
import { clearUser, setUserId, setRole, setToken } from "../../store/userSlice";

function Upload() {
  const [submitData, setSubmitData] = useState({ jobType: "", file: null, jobTitle: "" });
  const [errors, setErrors] = useState({ TitleError: "", optionError: "", fileError: "" });
  const [isSubmitting, setIsSubmitting] = useState(false);

  const userId = useSelector(selectUserId);
  const token = useSelector(selectToken);

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
          const response = await fetch(`http://13.233.228.245/v1/fileupload/${userId}/upload`, {
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
    setErrors({ TitleError: "", optionError: "", fileError: "" });
  }, [submitData]);

  return (
    <div className="container my-5">
      <div className="row justify-content-center">
        <div className="col-lg-6 col-md-8 col-sm-12">
          <form onSubmit={handleSubmit} className="p-4 shadow rounded bg-white">
            <div className="mb-3">
              <label htmlFor="jobTitle" className="form-label">Job Title</label>
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
                <option value="UDISE">UDISE</option>
                <option value="DUMMY">DUMMY</option>
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

            <button type="submit" className="btn btn-success w-100" disabled={isSubmitting}>
              {isSubmitting ? "Uploading..." : "Submit"}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Upload;
