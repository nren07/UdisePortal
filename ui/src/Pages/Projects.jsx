import React, { useState,useEffect } from "react";
import { Link,useNavigate } from "react-router-dom";
import Sidebar from "../components/Sidebar/Sidebar";
import "remixicon/fonts/remixicon.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Project.css";
import ProjectList from "../components/project/ProjectList";
import { useDispatch, useSelector } from "react-redux";
import { selectUserId,selectToken,selectTokenExpiration } from "../store/useSelectors";
import { clearUser } from "../store/userSlice";
import Upload from "../components/Uploads/Upload";

const Projects = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [showModal, setShowModal] = useState(false);
  const role = "portal_admin"; // Consider using this role in your logic if necessary.

  const handleSearchChange = (e) => setSearchTerm(e.target.value);
  const userId = useSelector(selectUserId);
  const token = useSelector(selectToken);
  const expirationTime = useSelector(selectTokenExpiration);
  const navigate=useNavigate();
  const dispatch=useDispatch();

  useEffect(() => {
    if (Date.now() >= expirationTime || !token || !userId) {
      dispatch(clearUser());
      navigate("/");
    }
  }, [dispatch, navigate, expirationTime, token, userId]);

  const handleOpenModal = () => {
    setShowModal(true); // Open modal when button is clicked
  };

  const handleCloseModal = () => {
    setShowModal(false); // Close modal when user clicks close button or outside
  };


  return (
    <div className="d-flex mb-4" style={{ backgroundColor: "#f3f6f9", minHeight: "100vh" }}>
      <Sidebar />
      <div className="flex-grow-1 p-4 mt-4">
        <div className="page-content">
          <div className="container-fluid">
            <Header />
            <SearchBar searchTerm={searchTerm} onSearchChange={handleSearchChange} handleOpenModal={handleOpenModal} />
            {showModal && <Upload handleClose={handleCloseModal} />}
            <ProjectList />
          </div>
        </div>
      </div>
    </div>
  );

};

// Header Component
const Header = () => (
  <div className="row mt-2 ml-0 p-3">
    <div className="col-12">
      <div className="page-title-box d-sm-flex align-items-center justify-content-between">
        <h4 className="mb-sm-0">Projects</h4>
        <Breadcrumb />
      </div>
    </div>
  </div>
);

// Breadcrumb Component
const Breadcrumb = () => (
  <div className="page-title-right">
    <ol className="breadcrumb m-0">
      <li className="breadcrumb-item">
        <Link to="#">Projects</Link>
      </li>
      <li className="breadcrumb-item">
        <Link to="/app/projects">Project List</Link>
      </li>
    </ol>
  </div>
);

// SearchBar Component
const SearchBar = ({ searchTerm, onSearchChange,handleOpenModal }) => (
  <div>
    <div className="card mt-3">
      <div className="card-body" style={{ backgroundColor: "white" }}>
        <div className="row g-3">
          <div className="col-md-3">
            <div className="search-box">
              <input
                type="text"
                className="form-control search"
                placeholder="Search for project..."
                value={searchTerm}
                onChange={onSearchChange}
              />
              <i className="ri-search-line search-icon"></i>
            </div>
          </div>
          <div className="col-md-auto ms-auto">
            <button onClick={handleOpenModal} className="btn btn-success mt-1 py-1">
              <i className="ri-add-fill align-bottom me-1"></i> Add Project
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
);

export default Projects;
