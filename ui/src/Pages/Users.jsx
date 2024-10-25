import React, { useState } from "react";
import Sidebar from "../components/Sidebar/Sidebar";
import "remixicon/fonts/remixicon.css";
import UserList from "../components/user/UserList";
// import UserList from "../UserList";
function User() {
  // State for form data
  const [userForm, setUserForm] = useState({
    firstName: "",
    lastName: "",
    contactNumber: "",
    email: "",
    role: "",
  });

  // State to determine if we are in edit mode
  const [editMode, setEditMode] = useState(false);

  // Sample user data
  const [users, setUsers] = useState([]);

  // Handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserForm((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  // Add new user
  const addUser = () => {
    setUsers([...users, { ...userForm, _id: new Date().toISOString() }]);
    setUserForm({
      firstName: "",
      lastName: "",
      contactNumber: "",
      email: "",
      role: "",
    });
  };

  // Update existing user
  const updateUser = () => {
    setUsers(
      users.map((user) => (user._id === userForm._id ? userForm : user))
    );
    setUserForm({
      firstName: "",
      lastName: "",
      contactNumber: "",
      email: "",
      role: "",
    });
    setEditMode(false);
  };

  // Delete user
  const deleteUser = (id) => {
    setUsers(users.filter((user) => user._id !== id));
  };

  // Set form for editing user
  const editUser = (user) => {
    setUserForm(user);
    setEditMode(true);
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

        <div className="page-content mt-4 flex-grow-1 p-3">
          <div className="container-fluid">
            <div className="row mt-1">
              <div className="col-12">
                <div className="page-title-box d-sm-flex align-items-center justify-content-between">
                  <h4 className="mb-sm-0">Users</h4>
                  <div className="page-title-right">
                    <ol className="breadcrumb m-0">
                      <li className="breadcrumb-item">
                        <a href="javascript:void(0);">Users</a>
                      </li>
                      <li className="breadcrumb-item active">User List</li>
                    </ol>
                  </div>
                </div>
              </div>
            </div>
            <div className="card">
              <div className="card-header border-0 rounded">
                <div className="row g-2">
                  <div className="col-xl-4">
                    <div className="search-box">
                      <input
                        type="text"
                        className="form-control search"
                        placeholder="Search for user name..."
                      />
                      <i className="ri-search-line search-icon" />
                    </div>
                  </div>
                  <div className="col-xl-8 d-flex flex-row-reverse">
                    <div className="hstack gap-2">
                      <button
                        className="btn btn-success"
                        data-bs-toggle="modal"
                        data-bs-target="#addSeller"
                        onClick={() => setEditMode(false)}
                      >
                        <i className="ri-add-fill me-1 align-bottom"></i> Add
                        User
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div className="row mt-4">
              <UserList />
              {users.map((user) => (
                <div key={user._id} className="col-xl-3 col-lg-6">
                  <div className="card ribbon-box right overflow-hidden">
                    <div className="card-body text-center p-4">
                      <div
                        className={`ribbon ${
                          user.active ? "ribbon-success" : "ribbon-danger"
                        } ribbon-shape trending-ribbon`}
                      >
                        <i className="ri-flashlight-fill text-white align-bottom"></i>
                        <span className="trending-ribbon-text">
                          {user.active ? "Active" : "Inactive"}
                        </span>
                      </div>
                      <h5 className="mb-1 mt-4">
                        <a className="link-primary">
                          {user.firstName} {user.lastName || ""}
                        </a>
                      </h5>
                      <p className="text-muted">{user.email}</p>
                      <p className="text-muted">{user.contactNumber}</p>
                      <div className="row mt-4">
                        <div className="col">
                          <button
                            className="btn btn-danger btn-sm"
                            onClick={() => deleteUser(user._id)}
                          >
                            <i className="ri-delete-bin-2-line"></i>
                          </button>
                        </div>
                        <div className="col">
                          <button
                            className="btn btn-success btn-sm"
                            data-bs-toggle="modal"
                            data-bs-target="#addSeller"
                            onClick={() => editUser(user)}
                          >
                            <i className="ri-edit-line"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
            <d className="row g-0 text-center text-sm-start align-items-center mb-3">
              <div className="col-sm-6">
                <div>
                  <p className="mb-sm-0">Showing 1 to 8 of 12 entries</p>
                </div>
              </div>
              <div className="col-sm-6">
                <ul className="pagination pagination-separated justify-content-center justify-content-sm-end mb-sm-0">
                  <li className="page-item disabled">
                    <a href="#" className="page-link">
                      <i className="mdi mdi-chevron-left"></i>
                    </a>
                  </li>
                  <li className="page-item active">
                    <a href="#" className="page-link">
                      1
                    </a>
                  </li>
                  <li className="page-item">
                    <a href="#" className="page-link">
                      2
                    </a>
                  </li>
                  <li className="page-item">
                    <a href="#" className="page-link">
                      3
                    </a>
                  </li>
                  <li className="page-item">
                    <a href="#" className="page-link">
                      4
                    </a>
                  </li>
                  <li className="page-item">
                    <a href="#" className="page-link">
                      5
                    </a>
                  </li>
                  <li className="page-item">
                    <a href="#" className="page-link">
                      6
                    </a>
                  </li>
                  <li className="page-item">
                    <a href="#" className="page-link">
                      7
                    </a>
                  </li>
                  <li className="page-item">
                    <a href="#" className="page-link">
                      <i className="mdi mdi-chevron-right"></i>
                    </a>
                  </li>
                </ul>
              </div>
            </d>
            <div
              className="modal fade zoomIn"
              id="addSeller"
              tabIndex="-1"
              aria-labelledby="addSellerLabel"
              aria-hidden="true"
            >
              <div className="modal-dialog modal-dialog-centered modal-lg">
                <div className="modal-content">
                  <div className="modal-header">
                    <h5 className="modal-title" id="addSellerLabel">
                      {editMode ? "Edit User" : "Add User"}
                    </h5>
                    <button
                      type="button"
                      className="btn-close"
                      data-bs-dismiss="modal"
                      aria-label="Close"
                    ></button>
                  </div>
                  <div className="modal-content border-0 mt-3">
                    <ul
                      className="nav nav-tabs nav-tabs-custom nav-success p-2 pb-0 bg-light"
                      role="tablist"
                    >
                      <li className="nav-item">
                        <a
                          className="nav-link active"
                          data-bs-toggle="tab"
                          href="#personalDetails"
                          role="tab"
                          aria-selected="true"
                        >
                          Personal Details
                        </a>
                      </li>
                    </ul>
                  </div>
                  <div className="modal-body">
                    <div className="tab-content">
                      <div
                        className="tab-pane active"
                        id="personalDetails"
                        role="tabpanel"
                      >
                        <form>
                          <div className="row justify-content-center">
                            <div className="col-lg-6">
                              <div className="mb-3">
                                <label
                                  htmlFor="firstnameInput"
                                  className="form-label"
                                >
                                  First Name
                                </label>
                                <input
                                  type="text"
                                  name="firstName"
                                  value={userForm.firstName}
                                  onChange={handleInputChange}
                                  className="form-control"
                                  id="firstnameInput"
                                  placeholder="Enter your firstname"
                                />
                              </div>
                            </div>
                            <div className="col-lg-6">
                              <div className="mb-3">
                                <label
                                  htmlFor="lastnameInput"
                                  className="form-label"
                                >
                                  Last Name
                                </label>
                                <input
                                  type="text"
                                  name="lastName"
                                  value={userForm.lastName}
                                  onChange={handleInputChange}
                                  className="form-control"
                                  id="lastnameInput"
                                  placeholder="Enter your lastname"
                                />
                              </div>
                            </div>
                            <div className="col-lg-6">
                              <div className="mb-3">
                                <label
                                  htmlFor="contactnumberInput"
                                  className="form-label"
                                >
                                  Contact Number
                                </label>
                                <input
                                  type="text"
                                  name="contactNumber"
                                  value={userForm.contactNumber}
                                  onChange={handleInputChange}
                                  className="form-control"
                                  id="contactnumberInput"
                                  placeholder="Enter your number"
                                />
                              </div>
                            </div>

                            <div className="col-lg-6">
                              <div className="mb-3">
                                <label
                                  htmlFor="emailidInput"
                                  className="form-label"
                                >
                                  Email
                                </label>
                                <input
                                  type="email"
                                  name="email"
                                  value={userForm.email}
                                  onChange={handleInputChange}
                                  className="form-control"
                                  id="emailidInput"
                                  placeholder="Enter your email"
                                />
                              </div>
                            </div>
                            <div className="col-lg-6">
                              <div className="mb-3">
                                <label
                                  htmlFor="roleSelect"
                                  className="form-label"
                                >
                                  Select Role
                                </label>
                                <select
                                  name="role"
                                  value={userForm.role}
                                  onChange={handleInputChange}
                                  className="form-select mb-3"
                                  id="roleSelect"
                                >
                                  <option value="">Choose...</option>
                                  {/* Example roles */}
                                  <option value="admin">Admin</option>
                                  <option value="user">User</option>
                                </select>
                              </div>
                            </div>

                            <div className="col-lg-12">
                              <div className="hstack gap-2 justify-content-end">
                                <button
                                  type="button"
                                  className="btn btn-link link-success text-decoration-none fw-medium"
                                  data-bs-dismiss="modal"
                                >
                                  <i className="ri-close-line me-1 align-middle"></i>{" "}
                                  Close
                                </button>
                                <button
                                  type="button"
                                  className="btn btn-primary"
                                  onClick={editMode ? updateUser : addUser}
                                >
                                  <i className="ri-save-3-line align-bottom me-1"></i>{" "}
                                  {editMode ? "Update" : "Save"}
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
    </>
  );
}

export default User;
