// // import React, { useEffect, useState } from "react";

// // const ClientList = () => {
// //   const [client, setClient] = useState([]);

// //   const fetchClientList = async () => {
// //     try {
// //       const token =
// //         "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySW5mbyI6eyJlbWFpbCI6InBvcnRhbGFkbWluQGxtcy5jb20iLCJ1c2VySWQiOiI2NjY2Yjc3NjU3ZTM0YWIyYjAwZTRiN2UiLCJyb2xlIjoiUE9SVEFMX0FETUlOIn0sImV4cGlyZXNJbiI6IjFoIiwiaWF0IjoxNzI2MDM5MjU1fQ.J_S9Nl2Cyq2_Ql7kke53ZqfAHFUSEwKUF3F2sLbr7xQ";

// //         // const token = sessionStorage.getItem("token");
// //       if (token) {
// //         const payload = {
// //           limit: 10,
// //           page: 1,
// //         };
// //         const response = await axios.post(
// //           "https://lmsapi.propdoors.com:3000/client/list",
// //           payload,
// //           {
// //             headers: {
// //               Authorization: `Bearer ${token}`,
// //               "Content-Type": "application/json",
// //             },
// //           }
// //         );
// //         const data = response.data.clients;
// //         console.log("this is my Client data:", data);
// //         data.forEach((client) => {
// //           console.log("my projects data", client.projects.projectName);
// //         });
// //         setClient(data);
// //       }
// //     } catch (error) {
// //       console.error("Something went wrong", error);
// //     }
// //   };
// //   useEffect(() => {
// //     fetchClientList();
// //   }, []);

// //   return (
// //     <>
// //       <div className="card-body mb-5">
// //         <div className="table-responsive table-card mb-1">
// //           <table id="ClientTable" class="table align-middle">
// //             <thead className="table-light text-muted">
// //               <tr>
// //                 <th>S.No</th>
// //                 <th>Client</th>
// //                 <th>Email</th>
// //                 <th>Phone</th>
// //                 <th>Projects</th>
// //                 <th>Status</th>
// //                 <th>Action</th>
// //               </tr>
// //             </thead>
// //             <tbody className="list form-check-all">
// //               {client.map((item, i) => {
// //                 return (
// //                   <tr key={i}>
// //                     <td>{i + 1}</td>
// //                     <td>{item.name}</td>
// //                     <td>{item.alternativeEmail}</td>
// //                     <td>{item.contactNumber}</td>
// //                     <td>
// //                       {item.projects.map((project, index) => (
// //                         <span key={index} className="badge badge-soft-primary">
// //                           {project.projectName}
// //                         </span>
// //                       ))}
// //                     </td>
// //                     <td>{item.staus}</td>
// //                     <td>{item.action}</td>
// //                   </tr>
// //                 );
// //               })}
// //             </tbody>
// //           </table>
// //         </div>
// //       </div>
// //     </>
// //   );
// // };

// // export default ClientList;

// import axios from "axios";
// import React, { useEffect, useState } from "react";

// const ClientList = () => {
//   const [clients, setClients] = useState([]);

//   const fetchClientList = async () => {
//     try {
//       const token =
//         "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySW5mbyI6eyJlbWFpbCI6InBvcnRhbGFkbWluQGxtcy5jb20iLCJ1c2VySWQiOiI2NjY2Yjc3NjU3ZTM0YWIyYjAwZTRiN2UiLCJyb2xlIjoiUE9SVEFMX0FETUlOIn0sImV4cGlyZXNJbiI6IjFoIiwiaWF0IjoxNzI2MDM5MjU1fQ.J_S9Nl2Cyq2_Ql7kke53ZqfAHFUSEwKUF3F2sLbr7xQ";

//       // const token = sessionStorage.getItem("token");
//       if (token) {
//         const payload = {
//           limit: 10,
//           page: 1,
//         };
//         const response = await axios.post(
//           "https://lmsapi.propdoors.com:3000/client/list",
//           payload,
//           {
//             headers: {
//               Authorization: `Bearer ${token}`,
//               "Content-Type": "application/json",
//             },
//           }
//         );
//         const data = response.data.clients;
//         console.log("This is my client data:", data);
//         setClients(data);
//       }
//     } catch (error) {
//       console.error("Something went wrong", error);
//     }
//   };

//   useEffect(() => {
//     fetchClientList();
//   }, []);

//   return (
//     <>
//       <div className="card-body mb-5">
//         <div className="table-responsive table-card mb-1">
//           <table id="ClientTable" className="table align-middle">
//             <thead className="table-light text-muted">
//               <tr>
//                 <th>S.No</th>
//                 <th>Client</th>
//                 <th>Email</th>
//                 <th>Phone</th>
//                 <th>Projects</th>
//                 <th>Status</th>
//                 <th>Action</th>
//               </tr>
//             </thead>
//             <tbody className="list form-check-all">
//               {clients.map((client, index) => (
//                 <tr key={index}>
//                   <td>{index + 1}</td>
//                   <td>{client.name}</td>
//                   <td>{client.alternativeEmail}</td>
//                   <td>{client.contactNumber}</td>
//                   <td>
//                     {client.projects && client.projects.length > 0 ? (
//                       client.projects.map((project, projectIndex) => (
//                         <span
//                           key={projectIndex}
//                           className="badge badge-soft-primary me-1"
//                         >
//                           {project.projectName}
//                         </span>
//                       ))
//                     ) : (
//                       <span>No Projects</span>
//                     )}
//                   </td>
//                   <td>{client.status || "N/A"}</td>
//                   <td>{/* Actions can be added here */}</td>
//                 </tr>
//               ))}
//             </tbody>
//           </table>
//         </div>
//       </div>
//     </>
//   );
// };

// export default ClientList;

import React, { useState, useEffect } from "react";
import axios from "axios";

const ClientList = () => {
  const [client, setClient] = useState([]);
  const [clientToDelete, setClientToDelete] = useState(null); // Store the client to be deleted

  const fetchClientList = async () => {
    try {
      const token =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySW5mbyI6eyJlbWFpbCI6InBvcnRhbGFkbWluQGxtcy5jb20iLCJ1c2VySWQiOiI2NjY2Yjc3NjU3ZTM0YWIyYjAwZTRiN2UiLCJyb2xlIjoiUE9SVEFMX0FETUlOIn0sImV4cGlyZXNJbiI6IjFoIiwiaWF0IjoxNzI2MDM5MjU1fQ.J_S9Nl2Cyq2_Ql7kke53ZqfAHFUSEwKUF3F2sLbr7xQ";

      const payload = { limit: 10 };
      const response = await axios.post(
        "https://lmsapi.propdoors.com:3000/client/list",
        payload,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      const { clients } = response.data;
      setClient(clients);
    } catch (error) {
      console.error("Error fetching client data", error);
    }
  };

  useEffect(() => {
    fetchClientList();
  }, []);

  // Function to handle deleting a client
  const deleteClient = async () => {
    try {
      const token =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySW5mbyI6eyJlbWFpbCI6InBvcnRhbGFkbWluQGxtcy5jb20iLCJ1c2VySWQiOiI2NjY2Yjc3NjU3ZTM0YWIyYjAwZTRiN2UiLCJyb2xlIjoiUE9SVEFMX0FETUlOIn0sImV4cGlyZXNJbiI6IjFoIiwiaWF0IjoxNzI2MDM5MjU1fQ.J_S9Nl2Cyq2_Ql7kke53ZqfAHFUSEwKUF3F2sLbr7xQ";

      await axios.delete(
        `https://lmsapi.propdoors.com:3000/client/${clientToDelete._id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      // Remove the deleted client from the state
      setClient(client.filter((item) => item._id !== clientToDelete._id));

      // Reset the clientToDelete state and close the modal
      setClientToDelete(null);
      document.getElementById("closeModalBtn").click();
    } catch (error) {
      console.error("Error deleting client", error);
    }
  };

  // Set the client to delete and open the modal
  const handleDeleteClick = (client) => {
    setClientToDelete(client);
  };

  return (
    <div className="page-content">
      <div className="container-fluid">
        <div className="row">
          <div className="col-lg-12">
            <div className="card" id="ClientList">
              <div className="card-body">
                <div className="table-responsive table-card mb-1">
                  <table className="table align-middle" id="ClientTable">
                    <thead className="table-light text-muted">
                      <tr>
                        <th>S.No</th>
                        <th>Client</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Projects</th>
                        <th>Status</th>
                        <th>Action</th>
                      </tr>
                    </thead>
                    <tbody className="list form-check-all">
                      {client.length > 0 ? (
                        client.map((item, index) => (
                          <tr key={index}>
                            <td>{index + 1}</td>
                            <td>{item.name}</td>
                            <td>{item.alternativeEmail || item.email}</td>
                            <td>{item.contactNumber}</td>
                            <td>
                              {item.projects.map((project, i) => (
                                <span
                                  key={i}
                                  className="badge badge-soft-primary text-black"
                                >
                                  {project.projectName}
                                </span>
                              ))}
                            </td>
                            <td>
                              {item.clientStatus ? (
                                <span className="text-uppercase">
                                  Active
                                </span>
                              ) : (
                                <span className="text-uppercase text-yellow">
                                  Disabled
                                </span>
                              )}
                            </td>
                            <td>
                              <ul className="list-inline hstack gap-2 mb-0">
                                <li
                                  className="list-inline-item edit"
                                  data-bs-toggle="tooltip"
                                  data-bs-trigger="hover"
                                  data-bs-placement="top"
                                  title="Edit"
                                >
                                  <a href="#showModal" className="text-primary">
                                    <i className="ri-pencil-fill fs-16"></i>
                                  </a>
                                </li>
                                <li
                                  className="list-inline-item"
                                  data-bs-toggle="tooltip"
                                  data-bs-trigger="hover"
                                  data-bs-placement="top"
                                  title="Remove"
                                  onClick={() => handleDeleteClick(item)}
                                >
                                  <a
                                    href="#deleteRecordModal"
                                    className="text-danger"
                                    data-bs-toggle="modal"
                                  >
                                    <i className="ri-delete-bin-5-fill fs-16"></i>
                                  </a>
                                </li>
                              </ul>
                            </td>
                          </tr>
                        ))
                      ) : (
                        <tr>
                          <td colSpan="7">No clients found.</td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>

                {/* Delete Modal */}
                <div
                  className="modal fade zoomIn"
                  id="deleteRecordModal"
                  tabIndex="-1"
                  aria-hidden="true"
                >
                  <div className="modal-dialog modal-dialog-centered">
                    <div className="modal-content">
                      <div className="modal-header">
                        <button
                          type="button"
                          className="btn-close"
                          data-bs-dismiss="modal"
                          aria-label="Close"
                          id="closeModalBtn"
                        ></button>
                      </div>
                      <div className="modal-body text-center">
                        <h4>Are you sure?</h4>
                        <p>Are you sure you want to remove this record?</p>
                        <div className="d-flex gap-2 justify-content-center mt-4 mb-2">
                          <button
                            type="button"
                            className="btn btn-light"
                            data-bs-dismiss="modal"
                          >
                            Close
                          </button>
                          <button
                            type="button"
                            className="btn btn-danger"
                            onClick={deleteClient}
                          >
                            Yes, Delete It!
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                {/* End Modal */}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ClientList;
