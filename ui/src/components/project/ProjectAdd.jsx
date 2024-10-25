import React, { useState } from "react";
import Sidebar from "../Sidebar/Sidebar";
import "./ProjectAdd.css";

function ProjectAdd() {
  const [step, setStep] = useState(1);
  const [formValues, setFormValues] = useState({
    projectName: "",
    startDate: "",
    endDate: "",
    tat: "",
    cpl: "",
    totalLeadsToBeDeliverd: "",
    priceCostFrom: "",
    projectType: "",
    propertyTypes: [],
    landingPageURL: "",
    state: "",
    city: "",
    description: "",
  });
  const [error, setError] = useState("");

  const handleInputChange = (e) => {
    const { name, value, type, multiple } = e.target;
    if (type === "select-multiple") {
      const options = e.target.options;
      const selectedValues = Array.from(options)
        .filter((option) => option.selected)
        .map((option) => option.value);
      setFormValues({ ...formValues, [name]: selectedValues });
    } else {
      setFormValues({ ...formValues, [name]: value });
    }
  };

  const handleNextStep = (nextStep) => {
    setStep(nextStep);
  };

  const handleSubmitButton = () => {
    setError(""); // For now, no actual submission, just a placeholder
    console.log("Form Values:", formValues);
    alert("Form submitted! (This is just a placeholder)");
  };

  const progressWidth = {
    width: step === 1 ? "33%" : step === 2 ? "66%" : "100%",
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
        className="page-conten mt-5"
        style={{ flexGrow: 1, paddingLeft: "20px" }}
      >
        <div className="row">
          <div className="col-12">
            <div className="page-title-box d-sm-flex align-items-center justify-content-between">
              <h4 className="mb-sm-0">Projects</h4>
              <div className="page-title-right">
                <ol className="breadcrumb m-0">
                  <li className="breadcrumb-item">
                    <a href="javascript:void(0);">Projects</a>
                  </li>
                  <li className="breadcrumb-item">
                    <a href="/app/projects">Project List</a>
                  </li>
                  <li className="breadcrumb-item active">Create Project</li>
                </ol>
              </div>
            </div>
          </div>
        </div>
        <div className="container-fluid mt-4">
          <div className="row">
            <div className="col-xl-12">
              <div className="card">
                <div className="card-header">
                  <h4 className="card-title mb-0">Create New Project</h4>
                </div>
                <div className="card-body">
                  <form className="form-steps" autoComplete="off">
                    <div
                      id="custom-progress-bar"
                      className="progress-nav mb-4 mt-4"
                    >
                      <div className="progress">
                        <div
                          className="progress-bar"
                          role="progressbar"
                          style={progressWidth}
                          aria-valuenow={step}
                          aria-valuemin="0"
                          aria-valuemax="100"
                        ></div>
                      </div>
                      <ul
                        className="nav nav-pills progress-bar-tab custom-nav"
                        role="tablist"
                      >
                        {[1, 2, 3].map((num) => (
                          <li
                            key={num}
                            className="nav-item"
                            role="presentation"
                          >
                            <button
                              className={`nav-link rounded-pill ${
                                step === num ? "active" : ""
                              }`}
                              onClick={() => handleNextStep(num)}
                            >
                              {num}
                            </button>
                          </li>
                        ))}
                      </ul>
                    </div>
                    <div className="tab-content">
                      {/* Step 1 Content */}
                      <div
                        className={`tab-pane fade ${
                          step === 1 ? "show active" : ""
                        }`}
                      >
                        <div className="mb-4 mx-2">
                          <h5 className="mb-1 ">General Information</h5>
                          <p className="text-muted">
                            Fill all Information as below
                          </p>
                        </div>
                        <div className="row mb-3 col-lg-12">
                          <div className="col-lg-4">
                            <label className="form-label mandatory ps-2">
                              Project Name
                            </label>
                            <input
                              type="text"
                              className="form-control"
                              name="projectName"
                              value={formValues.projectName}
                              onChange={handleInputChange}
                              placeholder="Project Name"
                              minLength="3"
                              maxLength="30"
                            />
                          </div>
                          <div className="col-lg-4">
                            <label className="form-label mandatory ps-2">
                              Start Date
                            </label>
                            <input
                              type="date"
                              className="form-control"
                              name="startDate"
                              value={formValues.startDate}
                              onChange={handleInputChange}
                            />
                          </div>
                          <div className="col-lg-4">
                            <label className="form-label mandatory ps-2">
                              End Date
                            </label>
                            <input
                              type="date"
                              className="form-control"
                              name="endDate"
                              value={formValues.endDate}
                              onChange={handleInputChange}
                            />
                          </div>
                        </div>
                        <div className="row mt-4 col-lg-12">
                          <div className="col-lg-4">
                            <label className="form-label mandatory ps-2">
                              Turn Around Time (/Hour)
                            </label>
                            <input
                              type="number"
                              className="form-control"
                              name="tat"
                              value={formValues.tat}
                              onChange={handleInputChange}
                              placeholder="Turn around time"
                            />
                          </div>
                          <div className="col-lg-4">
                            <label className="form-label mandatory ps-2">
                              Cost Per Lead (Excluding GST)
                            </label>
                            <input
                              type="number"
                              className="form-control"
                              name="cpl"
                              value={formValues.cpl}
                              onChange={handleInputChange}
                              placeholder="Cost per lead"
                            />
                          </div>
                          <div className="col-lg-4">
                            <label className="form-label mandatory ps-2">
                              Total Leads To be Delivered
                            </label>
                            <input
                              type="number"
                              className="form-control"
                              name="totalLeadsToBeDeliverd"
                              value={formValues.totalLeadsToBeDeliverd}
                              onChange={handleInputChange}
                              placeholder="Total leads to be delivered"
                            />
                          </div>
                        </div>
                        <div className="d-flex align-items-start gap-3 mt-2">
                          <button
                            type="button"
                            className="btn btn-success btn-label right ms-auto"
                            onClick={() => handleNextStep(2)}
                          >
                            Go to more info
                          </button>
                        </div>
                      </div>
                      {/* Step 2 Content */}
                      <div
                        className={`tab-pane fade ${
                          step === 2 ? "show active" : ""
                        }`}
                      >
                        <div className="row mt-4 col-lg-12">
                          <div className="col-lg-4">
                            <label className="form-label mandatory ps-2">
                              Price Start From
                            </label>
                            <input
                              type="number"
                              className="form-control"
                              name="priceCostFrom"
                              value={formValues.priceCostFrom}
                              onChange={handleInputChange}
                              placeholder="Price From"
                            />
                          </div>
                          <div className="col-lg-4">
                            <label className="form-label mandatory ps-2">
                              Project Type
                            </label>
                            <select
                              className="form-control"
                              name="projectType"
                              value={formValues.projectType}
                              onChange={handleInputChange}
                            >
                              <option value="">Select Type</option>
                              <option value="type1">Type 1</option>
                              <option value="type2">Type 2</option>
                            </select>
                          </div>
                          <div className="col-lg-4">
                            <label className="form-label mandatory ps-2">
                              Select Products
                            </label>
                            <select
                              className="form-control"
                              name="propertyTypes"
                              onChange={handleInputChange}
                              multiple
                            >
                              <option value="product1">Product 1</option>
                              <option value="product2">Product 2</option>
                            </select>
                          </div>
                        </div>
                        <div className="row mt-4 col-lg-12">
                          <div className="col-lg-4">
                            <label className="form-label mandatory ps-2">
                              Landing Page URL
                            </label>
                            <input
                              type="text"
                              className="form-control"
                              name="landingPageURL"
                              value={formValues.landingPageURL}
                              onChange={handleInputChange}
                              placeholder="Landing Page URL"
                            />
                          </div>
                          <div className="col-lg-4">
                            <label className="form-label mandatory ps-2">
                              State
                            </label>
                            <select
                              className="form-control"
                              name="state"
                              value={formValues.state}
                              onChange={handleInputChange}
                            >
                              <option value="">Select State</option>
                              <option value="state1">State 1</option>
                              <option value="state2">State 2</option>
                            </select>
                          </div>
                          <div className="col-lg-4">
                            <label className="form-label mandatory ps-2">
                              City
                            </label>
                            <select
                              className="form-control"
                              name="city"
                              value={formValues.city}
                              onChange={handleInputChange}
                            >
                              <option value="">Select City</option>
                              <option value="city1">City 1</option>
                              <option value="city2">City 2</option>
                            </select>
                          </div>
                        </div>
                        <div className="d-flex align-items-start gap-3 mt-4">
                          <button
                            type="button"
                            className="btn btn-light btn-label right ms-auto"
                            onClick={() => handleNextStep(1)}
                          >
                            <i className="ri-arrow-left-line label-icon align-middle fs-16 ms-2"></i>
                            Previous
                          </button>
                          <button
                            type="button"
                            className="btn btn-success btn-label right"
                            onClick={() => handleNextStep(3)}
                          >
                            Go to more info
                          </button>
                        </div>
                      </div>
                      {/* Step 3 Content */}
                      <div
                        className={`tab-pane fade ${
                          step === 3 ? "show active" : ""
                        }`}
                      >
                        <div className="row mt-4">
                          <div className="col">
                            <label className="form-label mandatory ps-2">
                              Description
                            </label>
                            <textarea
                              rows="4"
                              className="form-control"
                              name="description"
                              value={formValues.description}
                              onChange={handleInputChange}
                            ></textarea>
                          </div>
                        </div>
                        <div className="d-flex align-items-start gap-3 mt-4">
                          <button
                            type="button"
                            className="btn btn-light btn-label right ms-auto"
                            onClick={() => handleNextStep(2)}
                          >
                            <i className="ri-arrow-left-line label-icon align-middle fs-16 ms-2"></i>
                            Previous
                          </button>
                          <button
                            type="button"
                            className="btn btn-success btn-label right"
                            onClick={handleSubmitButton}
                          >
                            Submit
                          </button>
                        </div>
                      </div>
                    </div>
                  </form>
                  {error && <p className="text-danger mt-3">{error}</p>}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProjectAdd;
