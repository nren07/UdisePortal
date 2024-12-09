import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import { Link } from "react-router-dom";
import "./Dashboard.css";
import axios from "axios";
import Sidebar from "../components/Sidebar/Sidebar";
import { useDispatch, useSelector } from "react-redux";
import { selectUserId,selectToken,selectTokenExpiration,selectUsername } from "../store/useSelectors";
import { clearUser} from "../store/userSlice";

const Dashboard = () => {
  const [selectedTimeframe, setSelectedTimeframe] = useState("All");
  const navigate=useNavigate();
  const userName = useSelector(selectUsername);
  const userId = useSelector(selectUserId);
  const token = useSelector(selectToken);
  const expirationTime = useSelector(selectTokenExpiration);
  const dispatch=useDispatch();

  useEffect(() => {
    if (Date.now() >= expirationTime || !token || !userId) {
      dispatch(clearUser());
      navigate("/");
    }
  }, [dispatch, navigate, expirationTime, token, userId]);

  const handleButtonClick = (timeframe) => {
    setSelectedTimeframe(timeframe);
  };

  const currenthour = new Date().getHours();
  let hour = "";
  if (currenthour >= 3 && currenthour < 12) {
    hour = "  Good Morning,!";
  } else if (currenthour >= 11 && currenthour < 15) {
    hour = " Good Aternoon,";
  } else if (currenthour >= 15 && currenthour < 22) {
    hour = " Good Evening,";
  } else {
    hour = " Good Night,";
  }
  const [dashboardData, setDashboardData] = useState([]);
  const [leadSatus, setleadSataus] = useState([]);
  const [recentLeads, setRecentLeads] = useState([]);
  const fetchData = async () => {
    try {
      const token =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySW5mbyI6eyJlbWFpbCI6InBvcnRhbGFkbWluQGxtcy5jb20iLCJ1c2VySWQiOiI2NjY2Yjc3NjU3ZTM0YWIyYjAwZTRiN2UiLCJyb2xlIjoiUE9SVEFMX0FETUlOIn0sImV4cGlyZXNJbiI6IjFoIiwiaWF0IjoxNzI2MzIzNDIwfQ.2RXRFtZBjLNMSvG2hbpmybaTzhn4jB_JwHblBaTVFcI";
      //console.log(token);
      if (token) {
        const payload = {
          parameter1: "value1",
          parameter2: "value2",
        };
        const response = await axios.post(
          `https://lmsapi.propdoors.com:3000/dashboard/admin`,
          payload,
          {
            headers: {
              Authorization: ` Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
        );
        const data = response.data;
        //console.log(data);
        //console.log(data.leadsStatus);
        //console.log(data.recentLeads[0].name);
        const recentLeadsData = data.recentLeads.map((lead) => ({
          name: lead.name,
          contactNumber: lead.contactNumber,
          verifiedOn: lead.verifiedOn,
        }));
        //console.log(recentLeadsData);
        setDashboardData(data);
        setleadSataus(data.leadsStatus[0]);
        setRecentLeads(recentLeadsData);
      }
    } catch (error) {
      console.error(error);
    }
  };

  const fetchProjectList = async () => {
    try {
      const response = await fetch(`http://udise.pytosoft.com/v1/job/${userId}/getJobs`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      // setProjectList(data);
    } catch (error) {
      console.error("Something went wrong", error);
    }
  };
 
  return (
    <div
      className="mt-4"
      style={{
        display: "flex",
        minHeight: "100vh",
        backgroundColor: "#f3f6f9",
      }}
    >
      <Sidebar />
      <div className="page-content mt-5 flex-grow-1 p-3 ">
        <div className="container-fluid">
          <div className="row">
            <div className="col">
              <div className="h-100">
                <div className="row mb-3 pb-1">
                  <div className="col-12">
                    <div className="d-flex align-items-lg-center flex-lg-row flex-column">
                      <div className="flex-grow-1">
                        <h4>{hour} {userName}! </h4>
                        <h4 className="fs-16 mb-1"></h4>
                        <p className="text-muted mb-0">
                          Here's what's happening with your {selectedTimeframe}{" "}
                          leads
                        </p>
                      </div>
                      <div className="mt-3 mt-lg-0">
                        <form onSubmit={(e) => e.preventDefault()}>
                          <div className="row g-4 mb-0 align-items-center">
                            <div
                              className="btn-group"
                              role="group"
                              aria-label="Basic radio toggle button group"
                            >
                              <label
                                className="btn btn-outline-secondary shadow-none"
                                htmlFor="btnradio1"
                                style={{
                                  backgroundColor:
                                    selectedTimeframe === "All"
                                      ? "#865ce2"
                                      : "",
                                  color:
                                    selectedTimeframe === "All" ? "white" : "",
                                }}
                                onClick={() => handleButtonClick("All")}
                              >
                                All
                              </label>

                              <label
                                className="btn btn-outline-secondary shadow-none"
                                htmlFor="btnradio0"
                                style={{
                                  backgroundColor:
                                    selectedTimeframe === "Today"
                                      ? "#865ce2"
                                      : "",
                                  color:
                                    selectedTimeframe === "Today"
                                      ? "white"
                                      : "",
                                }}
                                onClick={() => handleButtonClick("Today")}
                              >
                                Today
                              </label>

                              <label
                                className="btn btn-outline-secondary shadow-none"
                                htmlFor="btnradio2"
                                style={{
                                  backgroundColor:
                                    selectedTimeframe === "This Week"
                                      ? "#865ce2"
                                      : "",
                                  color:
                                    selectedTimeframe === "This Week"
                                      ? "white"
                                      : "#865ce2",
                                }}
                                onClick={() => handleButtonClick("This Week")}
                              >
                                This Week
                              </label>

                              <label
                                className="btn btn-outline-secondary shadow-none"
                                htmlFor="btnradio3"
                                style={{
                                  backgroundColor:
                                    selectedTimeframe === "This Month"
                                      ? "#865ce2"
                                      : "",
                                  color:
                                    selectedTimeframe === "This Month"
                                      ? "white"
                                      : "#865ce2",
                                }}
                                onClick={() => handleButtonClick("This Month")}
                              >
                                This Month
                              </label>
                            </div>
                          </div>
                        </form>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="row">
                  <div className="col-xl-12">
                    <div className="card crm-widget">
                      <div className="card-body p-0">
                        <div className="row row-cols-md-3 row-cols-1">
                          <div className="col col-lg border-end cursor">
                            <Link
                              to="/Projects"
                              className="text-decoration-none text-dark"
                            >
                              <div className="py-4 px-3">
                                <h5 className="text-muted text-uppercase fs-13">
                                  Projects
                                </h5>
                                <div className="d-flex align-items-center">
                                  <div className="flex-shrink-0">
                                    <i className="ri-building-4-line display-6 icon-color"></i>
                                  </div>
                                  <div className="flex-grow-1 ms-3">
                                    <h2 className="mb-0">
                                      <span
                                        className="counter-value"
                                        data-target="197"
                                      ></span>
                                    </h2>
                                    <h2>
                                      <span
                                        className="counter-value"
                                        data-target="489.4"
                                      >
                                        {dashboardData.totalProjects}
                                      </span>
                                    </h2>
                                  </div>
                                </div>
                              </div>
                            </Link>
                          </div>

                          <div className="col col-lg border-end">
                            <Link
                              to="/leads"
                              className="text-decoration-none text-dark"
                            >
                              <div className="mt-3 mt-md-0 py-4 px-3 cursor">
                                <h5 className="text-muted text-uppercase fs-13">
                                  Pending Leads
                                </h5>
                                <div className="d-flex align-items-center">
                                  <div className="flex-shrink-0">
                                    <i className="ri-pulse-line display-6 icon-color"></i>
                                  </div>
                                  <div className="flex-grow-1 ms-3">
                                    <h2>
                                      <span
                                        className="counter-value"
                                        data-target="489.4"
                                      ></span>
                                    </h2>
                                    <h2>
                                      <span
                                        className="counter-value"
                                        data-target="489.4"
                                      >
                                        {leadSatus.pendingLeads}
                                      </span>
                                    </h2>
                                  </div>
                                </div>
                              </div>
                            </Link>
                          </div>

                          <div className="col col-lg border-end">
                            <Link
                              to="/app/leads/filter/Accepted"
                              className="mt-3 mt-md-0 py-4 px-3 cursor text-decoration-none text-dark"
                            >
                              <h5 className="text-muted text-uppercase fs-13">
                                Lead Accepted
                              </h5>
                              <div className="d-flex align-items-center">
                                <div className="flex-shrink-0">
                                  <i className="ri-pulse-line display-6 icon-color"></i>
                                </div>
                                <div className="flex-grow-1 ms-3">
                                  <h2 className="mb-0">
                                    <span
                                      className="counter-value"
                                      data-target="32.89"
                                    ></span>
                                  </h2>
                                  <h2>
                                    <span
                                      className="counter-value"
                                      data-target="489.4"
                                    >
                                      {dashboardData.accepted}
                                    </span>
                                  </h2>
                                </div>
                              </div>
                            </Link>
                          </div>

                          <div className="col col-lg border-end">
                            <Link
                              to="/app/leads/filter/Site Vist"
                              className="mt-3 mt-lg-0 py-4 px-3 cursor text-decoration-none text-dark"
                            >
                              <h5 className="text-muted text-uppercase fs-13">
                                Site Visit
                              </h5>
                              <div className="d-flex align-items-center">
                                <div className="flex-shrink-0">
                                  <i className="ri-trophy-line display-6 icon-color"></i>
                                </div>
                                <div className="flex-grow-1 ms-3">
                                  <h2 className="mb-0">
                                    <span
                                      className="counter-value"
                                      data-target="1596.5"
                                    ></span>
                                  </h2>
                                  <h2>
                                    <span
                                      className="counter-value"
                                      data-target="489.4"
                                    >
                                      {dashboardData.siteVist}
                                    </span>
                                  </h2>
                                </div>
                              </div>
                            </Link>
                          </div>

                          <div className="col col-lg border-end">
                            <Link
                              to="/app/leads/filter/Location Issue"
                              className="mt-3 mt-lg-0 py-4 px-3 cursor text-decoration-none text-dark"
                            >
                              <h5 className="text-muted text-uppercase fs-13">
                                Location Issue
                              </h5>
                              <div className="d-flex align-items-center">
                                <div className="flex-shrink-0">
                                  <i className="ri-user-location-line display-6 icon-color"></i>
                                </div>
                                <div className="flex-grow-1 ms-3">
                                  <h2 className="mb-0">
                                    <span
                                      className="counter-value"
                                      data-target="1596.5"
                                    ></span>
                                  </h2>
                                  <h2>
                                    <span
                                      className="counter-value"
                                      data-target="489.4"
                                    >
                                      {dashboardData.locationIssue}
                                    </span>
                                  </h2>
                                </div>
                              </div>
                            </Link>
                          </div>
                        </div>

                        <div className="row row-cols-md-3 row-cols-1">
                          <div className="col col-lg border-end">
                            <Link
                              to="/app/leads/filter/Lost To Competitor"
                              className="mt-3 mt-lg-0 py-4 px-3 cursor text-decoration-none text-dark"
                            >
                              <h5 className="text-muted text-uppercase fs-13">
                                Lost to Competitor
                              </h5>
                              <div className="d-flex align-items-center">
                                <div className="flex-shrink-0">
                                  <i className="ri-funds-box-line display-6 icon-color"></i>
                                </div>
                                <div className="flex-grow-1 ms-3">
                                  <h2 className="mb-0">
                                    <span
                                      className="counter-value"
                                      data-target="1596.5"
                                    ></span>
                                  </h2>
                                  <h2>
                                    <span
                                      className="counter-value"
                                      data-target="489.4"
                                    >
                                      {dashboardData.lostToCompetitor}
                                    </span>
                                  </h2>
                                </div>
                              </div>
                            </Link>
                          </div>

                          <div className="col col-lg border-end">
                            <Link
                              to="/app/leads/filter/Follow Up"
                              className="mt-3 mt-lg-0 py-4 px-3 cursor text-decoration-none text-dark"
                            >
                              <h5 className="text-muted text-uppercase fs-13">
                                Follow Up
                              </h5>
                              <div className="d-flex align-items-center">
                                <div className="flex-shrink-0">
                                  <i className="ri-customer-service-2-line display-6 icon-color"></i>
                                </div>
                                <div className="flex-grow-1 ms-3">
                                  <h2 className="mb-0">
                                    <span
                                      className="counter-value"
                                      data-target="2659"
                                    ></span>
                                  </h2>
                                  <h2>
                                    <span
                                      className="counter-value"
                                      data-target="489.4"
                                    >
                                      {dashboardData.followUp}
                                    </span>
                                  </h2>
                                </div>
                              </div>
                            </Link>
                          </div>

                          <div className="col col-lg border-end">
                            <Link
                              to="/app/leads/filter/Not Connected"
                              className="mt-3 mt-lg-0 py-4 px-3 cursor text-decoration-none text-dark"
                            >
                              <h5 className="text-muted text-uppercase fs-13">
                                Not Connected
                              </h5>
                              <div className="d-flex align-items-center">
                                <div className="flex-shrink-0">
                                  <i className="ri-notification-off-line display-6 icon-color"></i>
                                </div>
                                <div className="flex-grow-1 ms-3">
                                  <h2 className="mb-0">
                                    <span
                                      className="counter-value"
                                      data-target="2659"
                                    ></span>
                                  </h2>
                                  <h2>
                                    <span
                                      className="counter-value"
                                      data-target="489.4"
                                    >
                                      {dashboardData.notConnected}
                                    </span>
                                  </h2>
                                </div>
                              </div>
                            </Link>
                          </div>

                          <div className="col col-lg border-end">
                            <Link
                              to="/app/leads/filter/Budget Issue"
                              className="mt-3 mt-lg-0 py-4 px-3 cursor text-decoration-none text-dark"
                            >
                              <h5 className="text-muted text-uppercase fs-13">
                                Budget Issue
                              </h5>
                              <div className="d-flex align-items-center">
                                <div className="flex-shrink-0">
                                  <i className="ri-money-dollar-box-fill display-6 icon-color"></i>
                                </div>
                                <div className="flex-grow-1 ms-3">
                                  <h2 className="mb-0">
                                    <span
                                      className="counter-value"
                                      data-target="2659"
                                    ></span>
                                  </h2>
                                  <h2>
                                    <span
                                      className="counter-value"
                                      data-target="489.4"
                                    >
                                      {dashboardData.budgetIssue}
                                    </span>
                                  </h2>
                                </div>
                              </div>
                            </Link>
                          </div>

                          <div className="col col-lg border-end">
                            <Link
                              to="/app/leads/filter/Follow Up"
                              className="mt-3 mt-lg-0 py-4 px-3 cursor text-decoration-none text-dark"
                            >
                              <h5 className="text-muted text-uppercase fs-13">
                                Follow Up
                              </h5>
                              <div className="d-flex align-items-center">
                                <div className="flex-shrink-0">
                                  <i className="ri-customer-service-2-line display-6 icon-color"></i>
                                </div>
                                <div className="flex-grow-1 ms-3">
                                  <h2 className="mb-0">
                                    <span
                                      className="counter-value"
                                      data-target="2659"
                                    ></span>
                                  </h2>
                                  <h2>
                                    <span
                                      className="counter-value"
                                      data-target="489.4"
                                    >
                                      {dashboardData.followUp}
                                    </span>
                                  </h2>
                                </div>
                              </div>
                            </Link>
                          </div>
                        </div>
                      </div>
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
};

export default Dashboard;
