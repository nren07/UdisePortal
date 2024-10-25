import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Sidebar from "../components/Sidebar/Sidebar";
// import LeadList from '../LeadList';
import LeadList from "../components/Leads/LeadList.jsx";

function Leads() {
  const navigate = useNavigate();

  const [leads, setLeads] = useState([]);
  const [projects, setProjects] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalDocuments, setTotalDocuments] = useState(0);
  const [statusList, setStatusList] = useState([]);
  const [filterForm, setFilterForm] = useState({
    verifiedOn: "",
    clientStatus: [],
    projects: [],
  });

  const getPageData = (page) => {};

  const updateStatus = (id) => {};

  const applyFilter = () => {
    const filteredLeads = originalLeads.filter((lead) => {
      return (
        (filterForm.verifiedOn
          ? lead.verifiedOn === filterForm.verifiedOn
          : true) &&
        (filterForm.clientStatus.length
          ? filterForm.clientStatus.includes(lead.clientStatus)
          : true) &&
        (filterForm.projects.length
          ? filterForm.projects.includes(lead.project)
          : true)
      );
    });
    setLeads(filteredLeads);
  };

  const clearFilters = () => {
    setFilterForm({
      verifiedOn: "",
      clientStatus: [],
      projects: [],
    });
  };

  const handleCreateLead = () => {
    // navigate('/app/leads/new');
    navigate("/CreateLeads");
  };

  return (
    <>
      <div
        className="mt-4 mb-5"
        style={{
          display: "flex",
          minHeight: "100vh",
          backgroundColor: "#f3f6f9",
        }}
      >
        {/* Sidebar */}
        <Sidebar />
        <div className="page-content mt-4 flex-grow-1 p-4">
          <Sidebar />
          <div className="row ml-0">
            <div className="col-12">
              <div className="page-title-box d-sm-flex align-items-center justify-content-between">
                <h4 className="mb-sm-0">Leads</h4>
                <div className="page-title-right">
                  <ol className="breadcrumb m-0">
                    <li className="breadcrumb-item">
                      <a href="#" onClick={(e) => e.preventDefault()}>
                        Leads
                      </a>
                    </li>
                    <li className="breadcrumb-item active">Leads List</li>
                  </ol>
                </div>
              </div>
            </div>
          </div>
          <div className="container-fluid mt-4 ">
            <div className="card">
              <div className="card-body">
                <div className="row g-3">
                  <div className="col-md-3">
                    <div className="search-box">
                      <input
                        type="text"
                        className="form-control search"
                        placeholder="Search for lead..."
                      />
                      <i className="ri-search-line search-icon"></i>
                    </div>
                  </div>
                  <div className="col-md-auto ms-auto">
                    <div className="d-flex hastck gap-2 flex-wrap me-4 mb-2">
                      <button
                        type="button"
                        className="btn btn-success"
                        onClick={handleCreateLead}
                      >
                        <i className="ri-add-line"></i> Create Lead
                      </button>
                      <button
                        type="button"
                        className="btn btn-info"
                        data-bs-toggle="offcanvas"
                        href="#offcanvasExample"
                      >
                        <i className="ri-filter-3-line align-bottom me-1"></i>{" "}
                        Filters
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className="row">
              <LeadList />
              {leads.map((lead, i) => (
                <div key={lead.id} className="col-md-4 col-sm-12">
                  <div className="card mb-1 ribbon-box ribbon-fill ribbon-sm">
                    <div className="ribbon ribbon-info">{lead.viewCount}</div>
                    <div className="card-body">
                      <a
                        className="d-flex align-items-center"
                        data-bs-toggle="collapse"
                        href={`#contactInitiated${i}`}
                        role="button"
                        aria-expanded="false"
                        aria-controls={`contactInitiated${i}`}
                      >
                        <div className="flex-grow-1 ms-3">
                          <h6 className="fs-14 mb-1">
                            {lead.name}
                            <div
                              className="status"
                              style={{ textAlign: "end", marginTop: "-18px" }}
                            >
                              <span className="badge badge-soft-success text-uppercase">
                                {lead.clientStatus || lead.status}
                              </span>
                            </div>
                          </h6>
                          <p className="text-muted mb-0">
                            {lead.contactNumber}{" "}
                            <i
                              className={`ri-eye-off-line me-2 align-bottom text-muted eye ${
                                lead.show ? "d-none" : ""
                              }`}
                              onClick={() => viewLead(lead._id)}
                            ></i>
                          </p>
                          <p className="text-muted mb-0">{lead.email}</p>
                        </div>
                      </a>
                    </div>
                    <div
                      className="collapse border-top border-top-dashed"
                      id={`contactInitiated${i}`}
                    >
                      <div className="card-body">
                        {lead.clientActivities.map((activity, index) => (
                          <div key={index}>
                            <h6 className="fs-14 mb-1">
                              {activity.name}{" "}
                              <small className="badge badge-soft-danger">
                                {new Date(activity.date).toLocaleDateString()}
                              </small>
                            </h6>
                            <p className="text-muted">
                              {activity.activity} <br />
                              <small>{activity.remark}</small>
                            </p>
                          </div>
                        ))}
                        <form>
                          <ul className="list-unstyled vstack gap-2 mb-0">
                            <li>
                              <div>
                                <label
                                  htmlFor="basiInput"
                                  className="form-label"
                                >
                                  Change Status
                                </label>
                                <select
                                  className="form-select mb-3"
                                  value={lead.clientStatus}
                                  onChange={(e) => {}}
                                >
                                  <option value="">Choose...</option>
                                  <option
                                    value="Verified"
                                    disabled={
                                      lead.clientStatus === "Accepted" ||
                                      lead.clientStatus === "Site Vist"
                                    }
                                  >
                                    Verified
                                  </option>
                                  <option
                                    value="Accepted"
                                    disabled={lead.clientStatus === "Site Vist"}
                                  >
                                    Accepted
                                  </option>
                                  <option
                                    value="Site Vist"
                                    disabled={lead.clientStatus === "Accepted"}
                                  >
                                    Site Vist
                                  </option>
                                  <option
                                    value="Location Issue"
                                    disabled={
                                      lead.clientStatus === "Accepted" ||
                                      lead.clientStatus === "Site Vist"
                                    }
                                  >
                                    Location Issue
                                  </option>
                                  <option
                                    value="Budget Issue"
                                    disabled={
                                      lead.clientStatus === "Accepted" ||
                                      lead.clientStatus === "Site Vist"
                                    }
                                  >
                                    Budget Issue
                                  </option>
                                  <option
                                    value="Lost To Competitor"
                                    disabled={
                                      lead.clientStatus === "Accepted" ||
                                      lead.clientStatus === "Site Vist"
                                    }
                                  >
                                    Lost To Competitor
                                  </option>
                                  <option
                                    value="Not Connected"
                                    disabled={
                                      lead.clientStatus === "Accepted" ||
                                      lead.clientStatus === "Site Vist"
                                    }
                                  >
                                    Not Connected
                                  </option>
                                  <option
                                    value="Follow Up"
                                    disabled={
                                      lead.clientStatus === "Accepted" ||
                                      lead.clientStatus === "Site Vist"
                                    }
                                  >
                                    Follow Up
                                  </option>
                                  <option
                                    value="Other"
                                    disabled={
                                      lead.clientStatus === "Accepted" ||
                                      lead.clientStatus === "Site Vist"
                                    }
                                  >
                                    Other
                                  </option>
                                </select>
                              </div>
                            </li>
                            <li>
                              <div>
                                <label
                                  htmlFor="exampleFormControlTextarea5"
                                  className="form-label"
                                >
                                  Remarks
                                </label>
                                <textarea
                                  className="form-control"
                                  rows="3"
                                ></textarea>
                              </div>
                            </li>
                          </ul>
                        </form>
                      </div>
                      <div className="card-footer hstack gap-2">
                        <button className="btn btn-info btn-sm w-100">
                          Cancel
                        </button>
                        <button
                          className="btn btn-success btn-sm w-100"
                          onClick={() => updateStatus(lead._id)}
                        >
                          Save
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
            
          </div>
        </div>
        <div
          className="offcanvas offcanvas-end"
          tabIndex="-1"
          id="offcanvasExample"
          aria-labelledby="offcanvasExampleLabel"
        >
          <div className="offcanvas-header">
            <h5 id="offcanvasExampleLabel">Filters</h5>
            <button
              type="button"
              className="btn-close"
              data-bs-dismiss="offcanvas"
              aria-label="Close"
            ></button>
          </div>
          <div className="offcanvas-body">
            <form>
              <div className="mb-3">
                <label htmlFor="verifiedOn" className="form-label">
                  Verified On
                </label>
                <input
                  type="date"
                  id="verifiedOn"
                  className="form-control"
                  value={filterForm.verifiedOn}
                  onChange={(e) =>
                    setFilterForm({ ...filterForm, verifiedOn: e.target.value })
                  }
                />
              </div>
              <div className="mb-3">
                <label htmlFor="clientStatus" className="form-label">
                  Client Status
                </label>
                <select
                  id="clientStatus"
                  className="form-select"
                  multiple
                  value={filterForm.clientStatus}
                  onChange={(e) =>
                    setFilterForm({
                      ...filterForm,
                      clientStatus: Array.from(
                        e.target.selectedOptions,
                        (option) => option.value
                      ),
                    })
                  }
                >
                  <option value="Verified">Verified</option>
                  <option value="Accepted">Accepted</option>
                  <option value="Site Vist">Site Vist</option>
                  <option value="Location Issue">Location Issue</option>
                  <option value="Budget Issue">Budget Issue</option>
                  <option value="Lost To Competitor">Lost To Competitor</option>
                  <option value="Not Connected">Not Connected</option>
                  <option value="Follow Up">Follow Up</option>
                  <option value="Other">Other</option>
                </select>
              </div>
              <div className="mb-3">
                <label htmlFor="projects" className="form-label">
                  Projects
                </label>
                <select
                  id="projects"
                  className="form-select"
                  multiple
                  value={filterForm.projects}
                  onChange={(e) =>
                    setFilterForm({
                      ...filterForm,
                      projects: Array.from(
                        e.target.selectedOptions,
                        (option) => option.value
                      ),
                    })
                  }
                ></select>
              </div>
              <button
                type="button"
                className="btn btn-primary"
                onClick={applyFilter}
              >
                Apply Filters
              </button>
              <button
                type="button"
                className="btn btn-secondary ms-2"
                onClick={clearFilters}
              >
                Clear Filters
              </button>
            </form>
          </div>
        </div>
      </div>
    </>
  );
}

export default Leads;
