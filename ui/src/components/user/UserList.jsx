import axios from "axios";
import React, { useEffect, useState } from "react";
import { useSelector,useDispatch } from "react-redux";
import "remixicon/fonts/remixicon.css";
import "./UserList.css";

export default function UserList() {
  const [userList, setUserList] = useState([]);
  const id = useSelector((state) => state.user.userId);
  const token=useSelector((state) => state.user.token);
  const jobId=useSelector((state) => state.user.jobId);
  const dispatch=useDispatch();
  
  const fetchUserList = async () => {
      const url=`https://udise.pytosoft.com/v1/user/${id}/getUsers`;
        try {
          const response = await fetch(
            url,
            {
              method: "GET",
              headers: {
                Authorization: `Bearer ${token}`,  // Use token from Redux store for authorization
              },
            }
          );
  
          if (!response.ok) {
            throw new Error("Failed to upload");
          }
          const data= await response.json();
          console.log("this is my User data:", data);
          setUserList(data);
      }
      catch (error) {
        console.error("Something went wrong", error);
      }
  };

  useEffect(() => {
    fetchUserList();
  }, []);

  const deleteUser = (id) => {
    console.log("Delete user with ID:", id);
  };

  const editUser = (user) => {
    console.log("Edit user:", user);
  };

  return (
    <div className="row mb-5">
      {userList.map((user) => (
        <div key={user.id} className="col-lg-6 col-md-6 col-sm-12 mb-4">
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
                  {user.fullName}
                </a>
              </h5>
              <p className="text-muted">{user.email}</p>
              <p className="text-muted">{user.mobile}</p>
              <div className="row mt-4">
                <div className="col">
                  <button
                    className="btn btn-danger btn-sm"
                    onClick={() => deleteUser(user.id)}
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
  );
}
