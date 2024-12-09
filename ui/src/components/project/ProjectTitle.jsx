import React, { useEffect, useState, CSSProperties } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "./ProjectTitle.css";
import Sidebar from "../Sidebar/Sidebar";
import { useSelector, useDispatch } from "react-redux";
import {
  selectRole,
  selectToken,
  selectTokenExpiration,
  selectUserId,
} from "../../store/useSelectors"; // Import setJobId
import { clearUser } from "../../store/userSlice";

import { styles } from "./styles";
import { useSocket } from "./../context/WebSocketProvider";
import ClipLoader from "react-spinners/ClipLoader";

function ProjectTitle() {
  const location = useLocation();
  const { item } = location.state;
  // State to manage the job status
  const [isJobStarted, setIsJobStarted] = useState(false);
  const [loader, setLoader] = useState(false);
  const [jobRecordData, setJobRecordData] = useState([]);
  const [showIframe, setShowIframe] = useState(false); // Control iframe visibility
  const [vncPort, setVncPort] = useState(null);
  const [counter, setCounter] = useState(60);
  const token = useSelector(selectToken);
  const userId = useSelector(selectUserId);
  const role = useSelector(selectRole);
  const expirationTime = useSelector(selectTokenExpiration);
  const navigate = useNavigate();
  const { isConnected, eventType, messages } = useSocket();
  const dispatch=useDispatch();

  // console.log(vncPort);
  // console.log(showIframe);

  useEffect(() => {
    if (counter > 0 && showIframe) {
      // const timer = setInterval(() => {
      //   setCounter((prevCounter) => prevCounter - 1);
      // }, 1000);
      // // Clear the interval when component unmounts or counter reaches 0
      // return () => clearInterval(timer);
    } else if (counter == 0) {
      setShowIframe(false);
    }
  }, [counter, showIframe]);

  useEffect(() => {
    if (Date.now() >= expirationTime || !token || !userId) {
      dispatch(clearUser());
      navigate("/");
    }
  }, [dispatch, navigate, expirationTime, token, userId]);

  // Function to handle job start
  const handleStartJob = () => {
    StartJobFetchApi();
  };

  // Function to fetch job records
  const fetchData = async () => {
    try {
      const url = `http://udise.pytosoft.com/v1/job_record/${item.id}/get_job_records`;
      const response = await fetch(url, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const data = await response.json();
      setJobRecordData(Array.isArray(data) ? data : []); // Set to an empty array if not an array
    } catch (error) {
      console.error("Something went wrong", error);
    }
  };

  // Function to fetch job records
  const StartJobFetchApi = async () => {
    setIsJobStarted(true);
    try {
      const url = `http://udise.pytosoft.com/v1/job/${item.id}/start`;
      const response = await fetch(url, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.ok) {
      }
      const data = await response.json();
      // setVncPort(data.vncPort); // Set to an empty array if not an array

      setVncPort(data.vncPort);
    } catch (error) {
      console.error("Something went wrong", error);
      setIsJobStarted(false);
    }
  };

  const startIframe = () => setShowIframe(true);
  const closeIframe = () => setShowIframe(false); // Close iframe

  useEffect(() => {
    console.log(item);
    if (item.jobStatus == "PENDING") {
      setIsJobStarted(false);
    } else if (item.jobStatus == "IN_PROGRESS") {
      setLoader(true);
      setIsJobStarted(true);
    } else {
    }
  }, [item]);

  // Listen for WebSocket events to load iframe dynamically
  useEffect(() => {
    if (isConnected) {
      console.log(eventType);
      if (eventType == "JOB_STARTED") {
        startIframe(); // Show iframe when the job starts
      } else if (eventType == "JOB_ENDED") {
        closeIframe();
      }
    }
  }, [messages, isConnected]);

  // Fetch job records when item.id changes
  useEffect(() => {
    fetchData();
  }, [item.id, token]); // Also include token in dependency to refetch if it changes

  return (
    <div
      style={{
        display: "flex",
        minHeight: "100vh",
        backgroundColor: "#f0f0f0",
      }}
    >
      <Sidebar />
      {!showIframe && (
        <div
          className="page-content mt-4"
          style={{ flexGrow: 1, paddingLeft: "20px" }}
        >
          <div className="container-fluid">
            <div className="row mt-4 p-2">
              <div className="col-12">
                <div className="page-title-box d-sm-flex align-items-center justify-content-between">
                  <h4 className="mb-sm-0">Projects</h4>
                  <div className="page-title-right">
                    <ol className="breadcrumb m-0">
                      <li className="breadcrumb-item">
                        <a
                          href="#"
                          onClick={(e) => {
                            e.preventDefault();
                            // Handle navigation or function here if needed
                          }}
                        >
                          Projects
                        </a>
                      </li>
                      <li className="breadcrumb-item">Project List</li>
                      <li className="breadcrumb-item active">
                        Project Details
                      </li>
                    </ol>
                  </div>
                </div>
              </div>
            </div>

            <div className="row">
              <div className="col-lg-12">
                <div className="card mt-n4 mx-n4">
                  <div className="bg-soft-warning">
                    <div className="card-body pb-0 px-4">
                      <div className="row mb-3">
                        <div className="col-md-12">
                          <div className="d-flex align-items-center">
                            <div className="avatar-md">
                              <div className="avatar-title bg-white rounded-circle">
                                SEL001
                              </div>
                            </div>
                            <div className="ms-3">
                              <div className="d-flex justify-content-between">
                                <h4 className="fw-bold">{item.jobTitle}</h4>
                                {!loader && (
                                  <button
                                    id="startJobButton"
                                    className={`btn btn-primary ${
                                      isJobStarted ? "disabled" : ""
                                    }`}
                                    onClick={handleStartJob}
                                    disabled={isJobStarted}
                                  >
                                    {isJobStarted ? (
                                      <i className="fas fa-spinner fa-spin"></i>
                                    ) : (
                                      <i className="fas fa-play"></i>
                                    )}
                                    {showIframe
                                      ? "Job Started"
                                      : isJobStarted
                                      ? "Wait..."
                                      : "Start Job"}
                                  </button>
                                )}
                                {<ClipLoader loading={loader}
                                    size={30}
                                    aria-label="Loading Spinner"
                                    data-testid="loader"
                                  />
                                }
                              </div>

                              <div className="hstack gap-3 flex-wrap">
                                <div>
                                  Start Date:{" "}
                                  <span className="fw-medium">Sep 2, 2024</span>
                                </div>
                                <div className="vr"></div>
                                <div>
                                  Due Date:{" "}
                                  <span className="fw-medium">
                                    Sep 23, 2024
                                  </span>
                                </div>
                                <div className="vr"></div>
                                <div className="badge rounded-pill bg-danger fs-12">
                                  TAT: 12 hours
                                </div>
                              </div>
                            </div>
                            <div className="ms-auto">
                              <div className="hstack gap-1 flex-wrap">
                                <button className="btn py-0 fs-16 favourite-btn active shadow-none">
                                  <i className="ri-star-fill"></i>
                                </button>
                                <button className="btn py-0 fs-16 text-body shadow-none">
                                  <i className="ri-share-line"></i>
                                </button>
                                <button className="btn py-0 fs-16 text-body shadow-none">
                                  <i className="ri-flag-line"></i>
                                </button>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                      <ul className="nav nav-tabs border-bottom-0">
                        <li className="nav-item">
                          <a
                            className="nav-link fw-semibold active"
                            id="project-overview-tab"
                            data-bs-toggle="tab"
                            href="#project-overview"
                            role="tab"
                            aria-controls="project-overview"
                            aria-selected="true"
                          >
                            Overview
                          </a>
                        </li>
                        <li className="nav-item">
                          <a
                            className="nav-link fw-semibold"
                            id="project-document-tab"
                            data-bs-toggle="tab"
                            href="#project-document"
                            role="tab"
                            aria-controls="project-document"
                            aria-selected="false"
                          >
                            Job Records
                          </a>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            {<div className="row">
              {" "}
              <div className="col-lg-12">
                <div className="tab-content text-muted">
                  <div
                    id="project-overview"
                    className="tab-pane fade show active"
                    role="tabpanel"
                    aria-labelledby="project-overview-tab"
                  >
                    <div className="row">
                      <div className="col-xl-12 col-lg-12">
                        <div className="card">
                          <div className="card-body">
                            <h6 className="mb-3 fw-semibold text-uppercase">
                              Summary
                            </h6>
                            <div className="pt-3 border-top border-top-dashed mt-4">
                              <div className="row">{/* Details */}</div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div
                    id="project-document"
                    className="tab-pane fade"
                    role="tabpanel"
                    aria-labelledby="project-document-tab"
                  >
                    <div className="card">
                      <div className="card-body">
                        <h5 className="card-title">Job Records</h5>
                        <div className="table-responsive">
                          <table className="table table-borderless align-middle mb-0 styled-table">
                            <thead className="table-light">
                              <tr>
                                <th>S.No.</th>
                                <th>Student Name</th>
                                <th>Class</th>
                                <th>Section</th>
                                <th>Student Pen</th>
                                <th>Student Attendance</th>
                                <th>Student Percentage</th>
                                <th>Status</th>
                              </tr>
                            </thead>
                            <tbody>
                              {jobRecordData.length === 0 && (
                                <tr>
                                  <td colSpan="8" className="no-result-cell">
                                    <div className="noresult">
                                      <h4>No Record Found</h4>
                                    </div>
                                  </td>
                                </tr>
                              )}

                              {jobRecordData.map((record, index) => (
                                <tr
                                  key={`${record.id}-${index}`}
                                  className="table-row"
                                >
                                  <td>{index + 1}</td>
                                  <td>{record.studentName}</td>
                                  <td>{record.className}</td>
                                  <td>{record.section}</td>
                                  <td>{record.studentPen}</td>
                                  <td>{record.attendence}</td>
                                  <td>{record.percentange}</td>
                                  <td>{record.jobStatus}</td>
                                </tr>
                              ))}
                            </tbody>
                          </table>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>}
          </div>
        </div>
      )}
      {showIframe && (
        <div style={styles.iframeOverlay}>
          <div style={styles.iframeContainer}>
            <div className="d-flex justify-content-between">
              <p style={styles.counter}>login before {counter} sec</p>;
              <button style={styles.closeButton} onClick={closeIframe}>
                &times;
              </button>
            </div>
            <iframe
              src={`http://udise.pytosoft.com:${vncPort}/?autoconnect=1&resize=scale&password=secret`}
              // src={`http://udise.pytosoft.com/vnc`}
              style={styles.iframe}
              title="Job Iframe"
            ></iframe>
          </div>
        </div>
      )}
    </div>
  );
}

export default ProjectTitle;

// import React, { useEffect, useState } from "react";
// import { useLocation, useNavigate } from "react-router-dom";
// import "bootstrap/dist/css/bootstrap.min.css";
// import "bootstrap/dist/js/bootstrap.bundle.min.js";
// import "./ProjectTitle.css";
// import Sidebar from "../Sidebar/Sidebar";
// import { useSelector } from "react-redux";
// import {
//   selectRole,
//   selectToken,
//   selectTokenExpiration,
//   selectUserId,
// } from "../../store/useSelectors";

// function ProjectTitle() {
//   const location = useLocation();
//   const { item } = location.state;
//   const [isJobStarted, setIsJobStarted] = useState(item.jobStatus === "PENDING" ? false : true);
//   const [jobRecordData, setJobRecordData] = useState([]);
//   const [showIframe, setShowIframe] = useState(false); // Control iframe visibility
//   const [vncPort, setVncPort] = useState(null); // Store VNC port
//   const token = useSelector(selectToken);
//   const userId = useSelector(selectUserId);
//   const role = useSelector(selectRole);
//   const expirationTime = useSelector(selectTokenExpiration);
//   const navigate = useNavigate();

//   useEffect(() => {
//     if (Date.now() >= expirationTime || !token || !userId) navigate("/");
//   }, [expirationTime, navigate]);

//   const handleStartJob = () => {
//     StartJobFetchApi();
//   };

//   const fetchData = async () => {
//     try {
//       const url = `http://udise.pytosoft.com/v1/job_record/${item.id}/get_job_records`;
//       const response = await fetch(url, {
//         method: "GET",
//         headers: {
//           Authorization: `Bearer ${token}`,
//         },
//       });
//       const data = await response.json();
//       console.log(data);
//       setJobRecordData(Array.isArray(data) ? data : []);
//     } catch (error) {
//       console.log("Something went wrong", error);
//     }
//   };

//   const StartJobFetchApi = async () => {
//     try {
//       const url = `http://udise.pytosoft.com/v1/job/${item.id}/start`;
//       const response = await fetch(url, {
//         method: "GET",
//         headers: {
//           Authorization: `Bearer ${token}`,
//         },
//       });
//       const data = await response.json();
//       setIsJobStarted(true);
//       setVncPort(data.vncPort);
//       setShowIframe(true); // Show iframe when job starts
//     } catch (error) {
//       console.log("Something went wrong", error);
//     }
//   };

//   useEffect(() => {
//     fetchData();
//   }, [item.id, token]);

//   const closeIframe = () => setShowIframe(false); // Close iframe

//   return (
//     <div style={{ display: "flex", minHeight: "100vh", backgroundColor: "#f0f0f0" }}>
//       <Sidebar />
//       <div className="page-content mt-4" style={{ flexGrow: 1, paddingLeft: "20px" }}>
//         <div className="container-fluid">
//           <div className="row mt-4 p-2">
//             <div className="col-12">
//               <div className="page-title-box d-sm-flex align-items-center justify-content-between">
//                 <h4 className="mb-sm-0">Projects</h4>
//                 <div className="page-title-right">
//                   <ol className="breadcrumb m-0">
//                     <li className="breadcrumb-item">
//                       <a href="#" onClick={(e) => e.preventDefault()}>
//                         Projects
//                       </a>
//                     </li>
//                     <li className="breadcrumb-item">Project List</li>
//                     <li className="breadcrumb-item active">Project Details</li>
//                   </ol>
//                 </div>
//               </div>
//             </div>
//           </div>

//           <div className="row">
//             <div className="col-lg-12">
//               <div className="card mt-n4 mx-n4">
//                 <div className="bg-soft-warning">
//                   <div className="card-body pb-0 px-4">
//                     <div className="row mb-3">
//                       <div className="col-md-12">
//                         <div className="d-flex align-items-center">
//                           <div className="avatar-md">
//                             <div className="avatar-title bg-white rounded-circle">SEL001</div>
//                           </div>
//                           <div className="ms-3">
//                             <div className="d-flex justify-content-between">
//                               <h4 className="fw-bold">{item.jobTitle}</h4>
//                               <button
//                                 className={`btn btn-primary ${isJobStarted ? "disabled" : ""}`}
//                                 onClick={handleStartJob}
//                                 disabled={isJobStarted}
//                               >
//                                 {isJobStarted ? (
//                                   <i className="fas fa-spinner fa-spin"></i>
//                                 ) : (
//                                   <i className="fas fa-play"></i>
//                                 )}
//                                 {isJobStarted ? "Job Started" : "Start Job"}
//                               </button>
//                             </div>
//                           </div>
//                           <div className="ms-auto">
//                             <button className="btn py-0 fs-16 favourite-btn active shadow-none">
//                               <i className="ri-star-fill"></i>
//                             </button>
//                           </div>
//                         </div>
//                       </div>
//                     </div>
//                     <ul className="nav nav-tabs border-bottom-0">
//                       <li className="nav-item">
//                         <a
//                           className="nav-link fw-semibold active"
//                           data-bs-toggle="tab"
//                           href="#project-overview"
//                         >
//                           Overview
//                         </a>
//                       </li>
//                       <li className="nav-item">
//                         <a
//                           className="nav-link fw-semibold"
//                           data-bs-toggle="tab"
//                           href="#project-document"
//                         >
//                           Job Records
//                         </a>
//                       </li>
//                     </ul>
//                   </div>
//                 </div>
//               </div>
//             </div>
//           </div>

//           {showIframe && (
//             <div className="iframe-container">
//               <button className="btn btn-danger mb-2" onClick={closeIframe}>
//                 Close
//               </button>
//               <iframe
//                 src={`http://udise.pytosoft.com:${vncPort}/?autoconnect=1&resize=scale&password=secret`}
//                 style={{ width: "100%", height: "500px", border: "none" }}
//                 title="Job Iframe"
//               ></iframe>
//             </div>
//           )}
//         </div>
//       </div>
//     </div>
//   );
// }

// export default ProjectTitle;
