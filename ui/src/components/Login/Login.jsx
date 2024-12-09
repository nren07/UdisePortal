

import React, { useState, useEffect,useRef } from "react";
import "./Login.css";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import logo from "../../assets/logo.png";
import "remixicon/fonts/remixicon.css";
import "bootstrap/dist/js/bootstrap.bundle.min";
import { useDispatch, useSelector } from "react-redux";
import {
  selectUserId,
  selectToken,
  selectTokenExpiration,
} from "../../store/useSelectors";
import {
  clearUser,
  setUserId,
  setRole,
  setToken,
  setUsername,
} from "../../store/userSlice";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

function Login() {
  const initialValues = { userName: "", password: "" };
  const [formValues, setFormValues] = useState(initialValues);
  const [formErrors, setFormErrors] = useState({});
  const [isSubmit, setIsSubmit] = useState(false);
  const [loading, setLoading] = useState(false);
  const [apiError, setApiError] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const stompClientRef = useRef(null);

  const navigate = useNavigate();
  const dispatch = useDispatch();



  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormValues({ ...formValues, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const errors = validate(formValues);
    setFormErrors(errors);
    setIsSubmit(true);
  };

  const sendMessage = () => {
    if (stompClient) {
      stompClient.send('/app/message', {}, input);
      setInput('');
    } 
  }

  const initializeWebSocket = (userId) => {
    const socket = Stomp.over(function(){
      return new WebSocket('ws://13.233.228.245/ws')
    });
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame) => {
      console.log("Connected to WebSocket:", frame);
      stompClient.subscribe(`/user/${userId}/queue/messages`, (message) => {
        const parsedMessage = JSON.parse(message.body);
        console.log(parsedMessage);
        // Handle incoming messages here (e.g., update state or notify the user)
      });
    }, (error) => {
      console.error("WebSocket Connection Error:", error);
    });

    stompClientRef.current = stompClient; // Save to ref
  };

  useEffect(() => {
    const submitData = async () => {
      if (Object.keys(formErrors).length === 0 && isSubmit) {
        setLoading(true);
        try {
          const response = await fetch("http://udise.pytosoft.com/v1/login/user", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(formValues),
          });

          if (!response.ok) {
            const errorData = await response.json();
            throw new Error(`HTTP error! ${response.status}: ${errorData.message}`);
          }

          const data = await response.json();
          dispatch(setUserId(data.id));
          dispatch(setToken(data.authtoken));
          const role = data.role === "User" ? "USER" : data.role.toUpperCase();
          dispatch(setRole(role));
          dispatch(setUsername(data.name));
          initializeWebSocket(data.id); // Initialize WebSocket after successful login

          navigate("/Dashboard");
        } catch (error) {
          console.error("Login error:", error);
          setApiError("Login failed. Please check your credentials.");
        } finally {
          setLoading(false);
          setIsSubmit(false);
        }
      }
    };

    submitData();

    return () => {
      if (stompClientRef.current) {
        stompClientRef.current.disconnect(); // Cleanup WebSocket on unmount
      }
    };
  }, [formErrors, isSubmit, formValues, navigate, dispatch]);

  const validate = (values) => {
    const errors = {};
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i;
    if (!values.userName) {
      errors.userName = "Username is required";
    } else if (!regex.test(values.userName)) {
      errors.userName = "Invalid username";
    }
    if (!values.password) {
      errors.password = "Password is required";
    }
    return errors;
  };

  const togglePasswordVisibility = () => {
    setShowPassword((prev) => !prev);
  };

  return (
    <div className="container-fluid container">
      <div className="auth-page-wrapper auth-bg-cover py-5 d-flex">
        <div className="auth-page-content overflow-hidden pt-sm-5">
          <div className="container">
            <div className="row d-flex">
              <div className="col-sm-12">
                <div className="card overflow-hidden">
                  <div className="rows row g-3">
                    <div className="col-sm-6">
                      <div className="p-sm-5 p-4 auth-one-bg h-100">
                        <div className="bg-overlay"></div>
                        <div className="position-relative h-100 d-flex flex-column">
                          <div className="mb-4 text-center">
                            <img src={logo} alt="Logo" height="100" />
                          </div>
                          <div className="mt-auto">
                            <div className="mb-3">
                              <i className="ri-double-quotes-l display-4 text-success"></i>
                            </div>
                            <div
                              id="qoutescarouselIndicators"
                              className="carousel slide"
                              data-bs-ride="carousel"
                            >
                              <div className="carousel-indicators">
                                <button
                                  type="button"
                                  data-bs-target="#qoutescarouselIndicators"
                                  data-bs-slide-to="0"
                                  className="active"
                                  aria-current="true"
                                  aria-label="Slide 1"
                                ></button>
                                <button
                                  type="button"
                                  data-bs-target="#qoutescarouselIndicators"
                                  data-bs-slide-to="1"
                                  aria-label="Slide 2"
                                ></button>
                                <button
                                  type="button"
                                  data-bs-target="#qoutescarouselIndicators"
                                  data-bs-slide-to="2"
                                  aria-label="Slide 3"
                                ></button>
                              </div>
                              <div className="carousel-inner text-center text-white pb-5">
                                <div className="carousel-item active">
                                  <p className="fs-5 fst-italic">
                                    "Empowering Tomorrow, Today: Innovate,
                                    Integrate, Illuminate with Pytosoft IT
                                    Solution Pvt. Ltd"
                                  </p>
                                </div>
                                <div className="carousel-item">
                                  <p className="fs-5 fst-italic">
                                    "Lead Flow Analysis Data Analysis Mastery"
                                  </p>
                                </div>
                                <div className="carousel-item">
                                  <p className="fs-5 fst-italic">
                                    "Lead Flow Analysis AI Machine Learning
                                    Mastery"
                                  </p>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="p-sm-5 p-4">
                        <h4 className="text-primary">
                          Hello! Let's get started
                        </h4>
                        <p className="text-muted">Sign in to continue.</p>
                        <form onSubmit={handleSubmit}>
                          <div className="mb-3">
                            <label htmlFor="userName" className="form-label">
                              Username
                            </label>
                            <input
                              type="email"
                              name="userName"
                              className={`form-control ${
                                formErrors.userName ? "is-invalid" : ""
                              }`}
                              id="userName"
                              placeholder="Enter username"
                              value={formValues.userName}
                              onChange={handleChange}
                            />
                            {formErrors.userName && (
                              <div className="invalid-feedback">
                                {formErrors.userName}
                              </div>
                            )}
                          </div>
                          <div className="mb-3">
                            <label className="form-label" htmlFor="password">
                              Password
                            </label>
                            <div className="position-relative auth-pass-inputgroup mb-3">
                              <input
                                type={showPassword ? "text" : "password"}
                                name="password"
                                className={`form-control pe-5 password-input ${
                                  formErrors.password ? "is-invalid" : ""
                                }`}
                                placeholder="Enter password"
                                id="password"
                                value={formValues.password}
                                onChange={handleChange}
                              />
                              <button
                                type="button"
                                className="btn btn-link position-absolute end-0 top-0 text-muted"
                                onClick={togglePasswordVisibility}
                              >
                                <i
                                  className={`ri-${
                                    showPassword ? "eye-off" : "eye"
                                  }-fill`}
                                ></i>
                              </button>
                              {formErrors.password && (
                                <div className="invalid-feedback">
                                  {formErrors.password}
                                </div>
                              )}
                            </div>
                          </div>
                          <button
                            className="btn btn-success w-100"
                            type="submit"
                            disabled={loading}
                          >
                            {loading ? "Signing In..." : "Sign In"}
                          </button>
                          {apiError && (
                            <div className="alert alert-danger mt-3">
                              {apiError}
                            </div>
                          )}
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
    </div>
  );
}

export default Login;


