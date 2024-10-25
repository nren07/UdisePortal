import React from "react";
import "bootstrap/dist/css/bootstrap.min.css"; // Import Bootstrap CSS
import "./Footer.css";
import Sidebar from "../Sidebar/Sidebar";
const Footer = () => {
  return (
    <footer className="footer d-flex flex-grow-1">
      <Sidebar />
      <div className="container-fluid">
        <div className="row">
          {/* Left section: company name */}
          <div className="col-sm-6 footer-text">© Pytosoft.</div>

          {/* Right section: designer info */}
          <div className="col-sm-6 footer-texts">
            <div className="text-sm-end d-none d-sm-block">
              Design &amp; Develop by Pytosoft IT Solution Private Limited
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
