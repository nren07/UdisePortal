import React from "react";
import { Link } from "react-router-dom";
import ClientAdd from "../components/client/ClientAdd";
import Sidebar from "../components/Sidebar/Sidebar";
import ClientList from "../components/client/ClientList";

const Client = () => {
  return (
    <div
      className="d-flex mt-3 mb-5"
      style={{
        display: "flex",
        minHeight: "100vh",
        backgroundColor: "#f3f6f9",
      }}
    >
      <Sidebar />

      <div
        className="page-content flex-grow-1 p-2 mt-5"
        // style={{ backgroundColor: "white" }}
      >
        <div className="row">
          <div className="col-12">
            <div className="page-title-box d-sm-flex align-items-center justify-content-between">
              <h4 className="mb-sm-0">Clients</h4>
              <div className="page-title-right">
                <ol className="breadcrumb m-0">
                  <li className="breadcrumb-item">
                    <a href="javascript: void(0);">Client</a>
                  </li>
                  <li className="breadcrumb-item active">Clients</li>
                </ol>
              </div>
            </div>
          </div>
        </div>
        <div className="container-fluid">
          <div className="row">
            <div className="col-lg-12">
              <div id="ClientList" className="card">
                <div className="card-header border-bottom-dashed">
                  <div className="row g-1 align-items-center">
                    <div className="col-sm">
                      <h5 className="card-title mb-0">Client List</h5>
                    </div>
                    <div className="col-sm-auto">
                      <div className="d-flex flex-wrap align-items-start gap-2">
                        <Link to="/ClientAdd">
                          <button
                            type="button"
                            id="create-btn"
                            className="btn btn-success add-btn"
                            data-bs-target="#addClient"
                          >
                            <i className="ri-add-line align-bottom me-1"></i>{" "}
                            Add Client
                          </button>
                        </Link>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="card-body border-bottom-dashed border-bottom">
                  <form className="ng-untouched ng-pristine ng-valid">
                    <div className="row g-3">
                      <div className="col-xl-6">
                        <div className="search-box ml-5  mb-5 ">
                          <input
                            type="text"
                            placeholder="Search for Client, email, phone, status or something..."
                            className="form-control search"
                          />
                          <i className="ri-search-line search-icon "></i>
                        </div>
                      </div>
                    </div>
                  </form>
                </div>
              </div>
              <ClientList />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Client;
