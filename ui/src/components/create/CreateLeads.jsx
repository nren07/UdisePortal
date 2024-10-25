import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Sidebar from "../Sidebar/Sidebar";

import axios from "axios";

function CreateLead() {
  const navigate = useNavigate();
  const [leadForm, setLeadForm] = useState({
    name: "",
    email: "",
    contactNumber: "",
    budget: "",
    location: "",
    leadType: "",
    projects: [],
  });
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const [projects, setProjects] = useState([
    { _id: "1", projectName: "Project A" },
    { _id: "2", projectName: "Project B" },
  ]);
  const [tellerCallers, setTellerCallers] = useState([
    { _id: "1", firstName: "Teller 1" },
    { _id: "2", firstName: "Teller 2" },
  ]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setLeadForm({ ...leadForm, [name]: value });
  };

  const handleSelectChange = (e) => {
    const { name, value } = e.target;
    setLeadForm({ ...leadForm, [name]: value });
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleSaveLead = async () => {
    setLoading(true);
    setError("");

    const token =
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySW5mbyI6eyJlbWFpbCI6ImNzcmF0aG9yZTYxN0BnbWFpbC5jb20iLCJ1c2VySWQiOiI2NmM5NzA4ODU5OWNlNTU1MTM0Y2M1ZTAifSwiZXhwaXJlc0luIjoiMWgiLCJpYXQiOjE3MjUwMTE0Mjl9.LcZbW2VE8EoKLIav4Z3mZjwLgAa93KERAbrQBORvRv8";

    try {
      const response = await axios.post(
        "https://lmsapi.propdoors.com:3000/lead",
        leadForm,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      console.log("Lead created successfully:", response.data);
      alert("Lead created successfully!");
      navigate("/leads");
    } catch (error) {
      console.error("Error creating lead:", error);

      if (
        error.response &&
        error.response.data &&
        error.response.data.message
      ) {
        setError(error.response.data.message);
      } else {
        setError(
          "An error occurred while creating the lead. Please try again later."
        );
      }
    } finally {
      setLoading(false);
    }
  };

  const handleUploadFile = async () => {
    if (!file) {
      alert("Please select a file to upload");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    setLoading(true);

    try {
      console.log("Uploading file with data:", formData);

      const response = await axios.post(
        "https://qa.propdoors.com:3000/batch-upload",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      console.log("File uploaded successfully:", response.data);
      alert("File uploaded successfully!");
    } catch (error) {
      console.error("Error uploading file:", error);
      alert("Error uploading file.");
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setLeadForm({
      projects: [],
      name: "",
      contactNumber: "",
      email: "",
      budget: "",
      assignTo: "",
    });
  };

  return (
    <div
      style={{
        display: "flex",
        minHeight: "100vh",
        backgroundColor: "#f0f0f0",
      }}
    >
      {/* Sidebar */}
      <Sidebar />
      <div
        className="page-content mt-5"
        style={{ flexGrow: 1, paddingLeft: "20px" }}
      >
        <div className="row">
          <div className="col-12">
            <div className="page-title-box d-sm-flex align-items-center justify-content-between">
              <h4 className="mb-sm-0">Leads</h4>
              <div className="page-title-right">
                <ol className="breadcrumb m-0">
                  <li className="breadcrumb-item">
                    <a href="javascript: void(0);">Leads</a>
                  </li>
                  <li className="breadcrumb-item">Leads List</li>
                  <li className="breadcrumb-item active">Create Lead</li>
                </ol>
              </div>
            </div>
          </div>
        </div>
        <div className="container-fluid">
          <div className="row">
            <div className="col-xxl-12">
              <div className="card mt-xxl-n5">
                <div className="card-header">
                  <ul
                    className="nav nav-tabs-custom rounded card-header-tabs border-bottom-0"
                    role="tablist"
                  >
                    <li className="nav-item">
                      <a
                        className="nav-link active"
                        data-bs-toggle="tab"
                        href="#personalDetails"
                        role="tab"
                      >
                        <i className="fas fa-home"></i> Create New Lead
                      </a>
                    </li>
                    <li className="nav-item">
                      <a
                        className="nav-link"
                        data-bs-toggle="tab"
                        href="#batchUpload"
                        role="tab"
                      >
                        <i className="far fa-user"></i> Batch Upload
                      </a>
                    </li>
                  </ul>
                </div>
                <div className="card-body p-4">
                  <div className="tab-content">
                    <div
                      className="tab-pane active"
                      id="personalDetails"
                      role="tabpanel"
                    >
                      <form>
                        <div className="row g-2">
                          <div className="col-lg-4">
                            <label htmlFor="projects" className="form-label">
                              Select Projects *
                            </label>
                            <select
                              id="projects"
                              name="projects"
                              className="form-control form-control-sm"
                              multiple
                              value={leadForm.projects}
                              onChange={(e) =>
                                setLeadForm({
                                  ...leadForm,
                                  projects: Array.from(
                                    e.target.selectedOptions,
                                    (option) => option.value
                                  ),
                                })
                              }
                            >
                              {projects.map((project) => (
                                <option key={project._id} value={project._id}>
                                  {project.projectName}
                                </option>
                              ))}
                            </select>
                          </div>

                          <div className="col-lg-4">
                            <label htmlFor="name" className="form-label">
                              Name *
                            </label>
                            <input
                              type="text"
                              id="name"
                              name="name"
                              className="form-control"
                              placeholder="Enter name"
                              value={leadForm.name}
                              onChange={handleInputChange}
                            />
                          </div>

                          <div className="col-lg-4">
                            <label htmlFor="email" className="form-label">
                              Email
                            </label>
                            <input
                              type="email"
                              id="email"
                              name="email"
                              className="form-control"
                              placeholder="Enter email"
                              value={leadForm.email}
                              onChange={handleInputChange}
                            />
                          </div>

                          <div className="col-lg-4">
                            <label
                              htmlFor="contactNumber"
                              className="form-label"
                            >
                              Contact Number *
                            </label>
                            <input
                              type="text"
                              id="contactNumber"
                              name="contactNumber"
                              className="form-control"
                              placeholder="Enter contact number"
                              value={leadForm.contactNumber}
                              onChange={handleInputChange}
                            />
                          </div>

                          <div className="col-lg-4">
                            <label htmlFor="budget" className="form-label">
                              Budget
                            </label>
                            <input
                              type="text"
                              id="budget"
                              name="budget"
                              className="form-control"
                              placeholder="Enter budget"
                              value={leadForm.budget}
                              onChange={handleInputChange}
                            />
                          </div>

                          <div className="col-lg-4">
                            <label htmlFor="assignTo" className="form-label">
                              Assign To *
                            </label>
                            <select
                              id="assignTo"
                              name="assignTo"
                              className="form-control form-control-sm"
                              value={leadForm.assignTo}
                              onChange={handleSelectChange}
                            >
                              {tellerCallers.map((teller) => (
                                <option key={teller._id} value={teller._id}>
                                  {teller.firstName}
                                </option>
                              ))}
                            </select>
                          </div>

                          <div className="col-lg-12">
                            <div className="d-flex justify-content-end">
                              <button
                                type="button"
                                className="btn btn-light"
                                onClick={handleReset}
                              >
                                Cancel
                              </button>
                              <button
                                type="button"
                                className="btn btn-primary ms-2"
                                onClick={handleSaveLead}
                                disabled={loading}
                              >
                                {loading ? "Saving..." : "Save"}
                              </button>
                            </div>
                            {error && (
                              <div className="text-danger mt-2">{error}</div>
                            )}
                          </div>
                        </div>
                      </form>
                    </div>

                    <div className="tab-pane" id="batchUpload" role="tabpanel">
                      <form>
                        <div className="row g-2">
                          <div className="col-lg-4">
                            <label htmlFor="file" className="form-label">
                              Upload File *
                            </label>
                            <input
                              type="file"
                              id="file"
                              className="form-control"
                              onChange={handleFileChange}
                            />
                          </div>

                          <div className="col-lg-12">
                            <div className="d-flex justify-content-end">
                              <button
                                type="button"
                                className="btn btn-primary"
                                onClick={handleUploadFile}
                                disabled={loading}
                              >
                                {loading ? "Uploading..." : "Upload"}
                              </button>
                            </div>
                          </div>
                        </div>
                      </form>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default CreateLead;
