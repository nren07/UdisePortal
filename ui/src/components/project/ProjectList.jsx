import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector,useDispatch } from "react-redux";
import { selectUserId, selectRole, selectToken,selectTokenExpiration } from "../../store/useSelectors";
import "./ProjectList.css";

export default function ProjectList() {
  const [projectList, setProjectList] = useState([]);
  const role=useSelector(selectRole);
  const userId = useSelector(selectUserId);
  const token = useSelector(selectToken);
  const expirationTime = useSelector(selectTokenExpiration);
  const navigate=useNavigate();
  const dispatch=useDispatch();

  useEffect(() => {
    if (Date.now() >= expirationTime || !token || !userId) navigate("/");
  }, [expirationTime, navigate]);
  useEffect(() => {
     userId??navigate("/");
  }, [userId, navigate]);

  // Fetch project list
  const fetchProjectList = async () => {
    try {
      const response = await fetch(`http://localhost:8080/v1/job/${userId}/getJobs`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      setProjectList(data);
    } catch (error) {
      console.error("Something went wrong", error);
    }
  };

  useEffect(() => {
    fetchProjectList();
  }, [userId, token]); // Adding dependencies to ensure updates if userId or token changes

  return (
    <div className="row mb-5">
      {projectList.map((item) => (
        <ProjectCard key={item.id} item={item} navigate={navigate} />
        
      ))}
    </div>
  );
}

// ProjectCard Component
const ProjectCard = ({ item, navigate }) => (
  <div className="col-xxl-3 col-sm-6 project-card">
    <div
      className="card"
      style={{ cursor: "pointer" }}
      tabIndex="0"
      onClick={() => navigate(`/ProjectTitle/${item.id}`, { state: { item } })}
    >
      <div className="card-body">
        <div className="p-3 bg-soft-project rounded-top">
          <div className="d-flex align-items-center">
            <div className="flex-grow-1">
              <h5 className="mb-0 fs-14">
                <a href="#" className="text-white">
                  {item.jobTitle}
                </a>
              </h5>
            </div>
            <div className="flex-shrink-0">
              <ActionButtons item={item} />
            </div>
          </div>
        </div>
        <ProjectDetails item={item} />
      </div>
    </div>
  </div>
);

// ActionButtons Component
const ActionButtons = ({ item }) => (
  <div className="d-flex gap-1 align-items-center my-n2">
    <button
      type="button"
      className="btn avatar-xs p-0 favourite-btn shadow-none active"
    >
      <span className="avatar-title bg-transparent fs-15">
        <i className="ri-star-fill"></i>
      </span>
    </button>
    <div className="dropdown">
      <button
        className="btn btn-link text-muted p-1 mt-n1 py-0 text-decoration-none fs-15 shadow-none"
        data-bs-toggle="dropdown"
        aria-haspopup="true"
        aria-expanded="true"
      >
        <i className="icon-sm ri-list-check" style={{ color: "white" }}></i>
      </button>
      <div className="dropdown-menu dropdown-menu-end">
        <a className="dropdown-item" href={`/Projects/${item.id}`}>
          <i className="ri-eye-fill align-bottom me-2 text-muted"></i>
          View
        </a>
      </div>
    </div>
  </div>
);

// ProjectDetails Component
const ProjectDetails = ({ item }) => (
  <div className="py-3">
    <div className="row gy-3">
      <DetailItem label="Status" value={item.jobStatus} />
      <DetailItem label="Uploaded On" value={new Date(item.uploadedOn).toLocaleDateString()} />
      <DetailItem label="File Name" value={item.fileName} />
      <DetailItem label="Job Type" value={item.jobType} />
    </div>
  </div>
);

// DetailItem Component
const DetailItem = ({ label, value }) => (
  <div className="col-6">
    <div>
      <p className="text-muted mb-1">{label}</p>
      <h5 className="fs-14">{value}</h5>
    </div>
  </div>
);
