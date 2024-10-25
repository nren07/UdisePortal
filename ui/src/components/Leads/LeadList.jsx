import axios from "axios";
import React, { useEffect, useState } from "react";
import "./LeadList.css";

export default function LeadList() {
  const [leadList, setLeadList] = useState([]);
  const [totalDocuments, setTotalDocuments] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [pageRange, setPageRange] = useState([1, 10]); // Range for visible pages

  const indexOfLastItem = currentPage * rowsPerPage;
  const indexOfFirstItem = indexOfLastItem - rowsPerPage;
  const totalPages = Math.ceil(totalDocuments / rowsPerPage);

  // Fetch leads from the API
  const fetchLeadList = async () => {
    try {
      const token =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySW5mbyI6eyJlbWFpbCI6InBvcnRhbGFkbWluQGxtcy5jb20iLCJ1c2VySWQiOiI2NjY2Yjc3NjU3ZTM0YWIyYjAwZTRiN2UiLCJyb2xlIjoiUE9SVEFMX0FETUlOIn0sImV4cGlyZXNJbiI6IjFoIiwiaWF0IjoxNzI3MTYwMTkyfQ.CP33z5fE1zhby72ylk6ti9ltkDuRYa2vU0B_lRUftdE";
      // const token = sessionStorage.getItem("token");
      if (token) {
        const payload = {
          limit: rowsPerPage,
          page: currentPage,
        };
        const response = await axios.post(
          "https://lmsapi.propdoors.com:3000/lead/list",
          payload,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
        );
        const data = response.data;
        setLeadList(data.leads);
        setTotalDocuments(data.totalDocuments);
      }
    } catch (error) {
      console.error("Something went wrong", error);
    }
  };

  useEffect(() => {
    fetchLeadList();
  }, []);

  // Handle page change and adjust page range if needed
  const getPageData = (pageNumber) => {
    if (pageNumber >= 1 && pageNumber <= totalPages) {
      setCurrentPage(pageNumber);

      // Adjust the visible page range when moving forward or backward
      if (pageNumber > pageRange[1]) {
        setPageRange([pageRange[0] + 10, pageRange[1] + 10]);
      } else if (pageNumber < pageRange[0]) {
        setPageRange([pageRange[0] - 10, pageRange[1] - 10]);
      }
    }
  };

  // Slice the leadList for displaying the current page items
  const currentItems = leadList.slice(indexOfFirstItem, indexOfLastItem);

  return (
    <div className="container-fluid mb-5">
      <div className="row">
        {currentItems?.map((item, index) => (
          <div className="col-md-4 col-sm-12 mt-n2" key={index}>
            <div className="card mb-0 ribbon-box ribbon-fill ribbon-sm">
              <div className="ribbon ribbon-info">0</div>
              <div className="card-body">
                <a
                  data-bs-toggle="collapse"
                  role="button"
                  aria-expanded="false"
                  aria-controls={`contactInitiated${index}`}
                  className="d-flex align-items-center"
                  href={`#contactInitiated${index}`}
                >
                  <div className="flex-grow-1 ms-5">
                    <h6 className="fs-14 mb-1">
                      {item.name}
                      <div
                        className="status"
                        style={{ textAlign: "end", marginTop: "-18px" }}
                      >
                        <span className="badge badge-soft-success text-uppercase mt-3">
                          {item.status}
                        </span>
                      </div>
                    </h6>
                    <p className="text-muted mb-0">
                      {item.contactNumber}
                      <i className="ri-eye-off-line me-4 align-bottom text-muted eye"></i>
                    </p>
                  </div>
                </a>
              </div>
              <div
                className="border-top border-top-dashed collapse"
                id={`contactInitiated${index}`}
              >
                <div className="card-body">
                  <form noValidate>
                    <ul className="list-unstyled vstack gap-2 mb-0">
                      <li>
                        <div>
                          <label
                            htmlFor={`basiInput${index}`}
                            className="form-label"
                          >
                            Change Status
                          </label>
                          <select className="form-select mb-3">
                            <option defaultValue>Choose...</option>
                            <option value="Verified">Verified</option>
                            <option value="Accepted">Accepted</option>
                            <option value="Site Visit">Site Visit</option>
                            <option value="Location Issue">
                              Location Issue
                            </option>
                            <option value="Budget Issue">Budget Issue</option>
                            <option value="Lost To Competitor">
                              Lost To Competitor
                            </option>
                            <option value="Not Connected">Not Connected</option>
                            <option value="Follow Up">Follow Up</option>
                            <option value="Other">Other</option>
                          </select>
                        </div>
                      </li>
                      <li>
                        <div>
                          <label
                            htmlFor={`exampleFormControlTextarea${index}`}
                            className="form-label"
                          >
                            Remarks
                          </label>
                          <textarea
                            id={`exampleFormControlTextarea${index}`}
                            rows="3"
                            className="form-control"
                          ></textarea>
                        </div>
                      </li>
                    </ul>
                  </form>
                </div>
                <div className="card-footer hstack gap-2">
                  <button className="btn btn-info btn-sm w-100">Cancel</button>
                  <button className="btn btn-success btn-sm w-100">Save</button>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
      {/* Pagination Component */}
      <div className="row g-0 text-center text-sm-start align-items-center mb-4">
        <div className="col-sm-6">
          <p className="mb-sm-0 text-muted">
            Showing{" "}
            <span className="fw-semibold">{(currentPage - 1) * 10 + 1}</span> to{" "}
            <span className="fw-semibold">
              {Math.min(currentPage * 10, totalDocuments)}
            </span>{" "}
            of{" "}
            <span className="fw-semibold text-decoration-underline">
              {totalDocuments}
            </span>{" "}
            entries
          </p>
        </div>
        <div className="col-sm-6">
          <ul className="pagination pagination-separated justify-content-center justify-content-sm-end mb-sm-0">
            <li className={`page-item ${currentPage === 1 ? "disabled" : ""}`}>
              <button
                onClick={() => getPageData(currentPage - 1)}
                className="page-link"
              >
                Previous
              </button>
            </li>
            {Array.from(
              { length: Math.min(totalPages, pageRange[1] - pageRange[0] + 1) },
              (_, i) => {
                const pageNumber = pageRange[0] + i;
                if (pageNumber > totalPages) return null;
                return (
                  <li
                    key={pageNumber}
                    className={`page-item ${
                      pageNumber === currentPage ? "active" : ""
                    }`}
                  >
                    <button
                      className="page-link"
                      onClick={() => getPageData(pageNumber)}
                    >
                      {pageNumber}
                    </button>
                  </li>
                );
              }
            )}
            <li
              className={`page-item ${
                currentPage === totalPages ? "disabled" : ""
              }`}
            >
              <button
                onClick={() => getPageData(currentPage + 1)}
                className="page-link"
              >
                Next
              </button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
}
