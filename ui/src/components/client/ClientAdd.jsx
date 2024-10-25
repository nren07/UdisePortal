// import React, { useState } from 'react';

// const ClientAdd = () => {
//   const [step, setStep] = useState(1);
//   const [formData, setFormData] = useState({
//     clientName: '',
//     email: '',
//     alternativeEmail: '',
//     phoneNumber: '',
//     contactPerson: '',
//   });

//   const createClient = (e) => {
//     e.preventDefault();
//     // Handle form submission
//     console.log(formData);
//   };

//   const handleChange = (e) => {
//     setFormData({
//       ...formData,
//       [e.target.name]: e.target.value,
//     });
//   };

//   return (
//     <>
//       <div className="page-content">
//         <div className="row">
//           <div className="col-12">
//             <div className="page-title-box d-sm-flex align-items-center justify-content-between">
//               <h4 className="mb-sm-0">Clients</h4>

//               <div className="page-title-right">
//                 <ol className="breadcrumb m-0">
//                   <li className="breadcrumb-item"><a href="javascript: void(0);">Clients</a></li>
//                   <li className="breadcrumb-item"><a href="/app/Clients">Client List</a></li>
//                   <li className="breadcrumb-item active">Create Client</li>
//                 </ol>
//               </div>
//             </div>
//           </div>
//         </div>

//         <div className="container-fluid">
//           <div className="row">
//             <div className="col-xl-12">
//               <div className="card">
//                 <div className="card-header">
//                   <h4 className="card-title mb-0">Create New Client</h4>
//                 </div>
//                 <div className="card-body">
//                   <form onSubmit={createClient} className="form-steps" autoComplete="off">
//                     <div id="custom-progress-bar" className="progress-nav mb-4">
//                       <div className="progress" style={{ height: '1px' }}>
//                         <div className="progress-bar" role="progressbar" style={{ width: `${(step - 1) * 50}%` }} aria-valuenow={step - 1} aria-valuemin="0" aria-valuemax="100"></div>
//                       </div>

//                       <ul className="nav nav-pills progress-bar-tab custom-nav" role="tablist">
//                         <li className="nav-item" role="presentation">
//                           <button className={`nav-link rounded-pill ${step === 1 ? 'active' : ''}`} type="button" onClick={() => setStep(1)}>1</button>
//                         </li>
//                         <li className="nav-item" role="presentation">
//                           <button className={`nav-link rounded-pill ${step === 2 ? 'active' : ''}`} type="button" onClick={() => setStep(2)}>2</button>
//                         </li>
//                         <li className="nav-item" role="presentation">
//                           <button className={`nav-link rounded-pill ${step === 3 ? 'active' : ''}`} type="button" onClick={() => setStep(3)}>3</button>
//                         </li>
//                       </ul>
//                     </div>

//                     <div className="tab-content">
//                       <div className={`tab-pane fade ${step === 1 ? 'show active' : ''}`}>
//                         <div>
//                           <h5 className="mb-1">Personal Details</h5>
//                           <p className="text-muted">Fill all Information as below</p>

//                           <div className="row">
//                             <div className="col-lg-6">
//                               <div className="mb-3">
//                                 <label htmlFor="clientName" className="form-label">Client Name</label>
//                                 <input
//                                   type="text"
//                                   className="form-control"
//                                   id="clientName"
//                                   name="clientName"
//                                   placeholder="Enter your client name"
//                                   value={formData.clientName}
//                                   onChange={handleChange}
//                                 />
//                               </div>
//                             </div>
//                             <div className="col-lg-6">
//                               <div className="mb-3">
//                                 <label htmlFor="email" className="form-label">Email</label>
//                                 <input
//                                   type="email"
//                                   className="form-control"
//                                   id="email"
//                                   name="email"
//                                   placeholder="Enter your email"
//                                   value={formData.email}
//                                   onChange={handleChange}
//                                 />
//                               </div>
//                             </div>
//                             <div className="col-lg-6">
//                               <div className="mb-3">
//                                 <label htmlFor="alternativeEmail" className="form-label">Alternative Email</label>
//                                 <input
//                                   type="email"
//                                   className="form-control"
//                                   id="alternativeEmail"
//                                   name="alternativeEmail"
//                                   placeholder="Enter your alternative email"
//                                   value={formData.alternativeEmail}
//                                   onChange={handleChange}
//                                 />
//                               </div>
//                             </div>
//                             <div className="col-lg-6">
//                               <div className="mb-3">
//                                 <label htmlFor="phoneNumber" className="form-label">Phone Number</label>
//                                 <input
//                                   type="number"
//                                   className="form-control"
//                                   id="phoneNumber"
//                                   name="phoneNumber"
//                                   placeholder="Enter your contact number"
//                                   value={formData.phoneNumber}
//                                   onChange={handleChange}
//                                 />
//                               </div>
//                             </div>
//                             <div className="col-lg-6">
//                               <div className="mb-3">
//                                 <label htmlFor="contactPerson" className="form-label">Contact Person</label>
//                                 <input
//                                   type="text"
//                                   className="form-control"
//                                   id="contactPerson"
//                                   name="contactPerson"
//                                   placeholder="Enter your contact person name"
//                                   value={formData.contactPerson}
//                                   onChange={handleChange}
//                                 />
//                               </div>
//                             </div>
//                           </div>
//                         </div>

//                         <div className="d-flex align-items-start gap-3 mt-4">
//                           <button
//                             type="button"
//                             className="btn btn-success btn-label right ms-auto"
//                             onClick={() => setStep(2)}
//                           >
//                             <i className="ri-arrow-right-line label-icon align-middle fs-16 ms-2"></i> Go to more info
//                           </button>
//                         </div>
//                       </div>

//                       {/* Additional Steps (Step 2, Step 3) can be implemented similarly */}
//                     </div>
//                   </form>
//                 </div>
//               </div>
//             </div>
//           </div>
//         </div>
//       </div>
//     </>
//   );
// };

// export default ClientAdd;


// import React, { useState } from 'react';

// const ClientAdd = () => {
//   const [step, setStep] = useState(1);
//   const [formData, setFormData] = useState({
//     clientName: '',
//     email: '',
//     alternativeEmail: '',
//     phoneNumber: '',
//     contactPerson: '',
//     clientType: 'BROKER',
//     sendSMS: 'true',
//     clientStatus: 'true',
//     address1: '',
//     address2: '',
//     area: '',
//     state: '',
//     city: '',
//     zip: '',
//   });

//   const createClient = (e) => {
//     e.preventDefault();
//     // Handle form submission
//     console.log(formData);
//   };

//   const handleChange = (e) => {
//     setFormData({
//       ...formData,
//       [e.target.name]: e.target.value,
//     });
//   };

//   return (
//     <>
//       <div className="page-content">
//         <div className="row">
//           <div className="col-12">
//             <div className="page-title-box d-sm-flex align-items-center justify-content-between">
//               <h4 className="mb-sm-0">Clients</h4>

//               <div className="page-title-right">
//                 <ol className="breadcrumb m-0">
//                   <li className="breadcrumb-item"><a href="javascript: void(0);">Clients</a></li>
//                   <li className="breadcrumb-item"><a href="/app/Clients">Client List</a></li>
//                   <li className="breadcrumb-item active">Create Client</li>
//                 </ol>
//               </div>
//             </div>
//           </div>
//         </div>

//         <div className="container-fluid">
//           <div className="row">
//             <div className="col-xl-12">
//               <div className="card">
//                 <div className="card-header">
//                   <h4 className="card-title mb-0">Create New Client</h4>
//                 </div>
//                 <div className="card-body">
//                   <form onSubmit={createClient} className="form-steps" autoComplete="off">
//                     <div id="custom-progress-bar" className="progress-nav mb-4">
//                       <div className="progress" style={{ height: '1px' }}>
//                         <div className="progress-bar" role="progressbar" style={{ width: `${(step - 1) * 50}%` }} aria-valuenow={step - 1} aria-valuemin="0" aria-valuemax="100"></div>
//                       </div>

//                       <ul className="nav nav-pills progress-bar-tab custom-nav" role="tablist">
//                         <li className="nav-item" role="presentation">
//                           <button className={`nav-link rounded-pill ${step === 1 ? 'active' : ''}`} type="button" onClick={() => setStep(1)}>1</button>
//                         </li>
//                         <li className="nav-item" role="presentation">
//                           <button className={`nav-link rounded-pill ${step === 2 ? 'active' : ''}`} type="button" onClick={() => setStep(2)}>2</button>
//                         </li>
//                         <li className="nav-item" role="presentation">
//                           <button className={`nav-link rounded-pill ${step === 3 ? 'active' : ''}`} type="button" onClick={() => setStep(3)}>3</button>
//                         </li>
//                       </ul>
//                     </div>

//                     <div className="tab-content">
//                       {/* Step 1: Personal Details */}
//                       <div className={`tab-pane fade ${step === 1 ? 'show active' : ''}`}>
//                         <h5 className="mb-1">Personal Details</h5>
//                         <p className="text-muted">Fill all Information as below</p>
//                         <div className="row">
//                           {/* Personal detail fields */}
//                           {/* Similar to the original code */}
//                         </div>
//                         <div className="d-flex align-items-start gap-3 mt-4">
//                           <button
//                             type="button"
//                             className="btn btn-success btn-label right ms-auto"
//                             onClick={() => setStep(2)}
//                           >
//                             <i className="ri-arrow-right-line label-icon align-middle fs-16 ms-2"></i> Go to more info
//                           </button>
//                         </div>
//                       </div>

//                       {/* Step 2: Additional Info */}
//                       <div className={`tab-pane fade ${step === 2 ? 'show active' : ''}`}>
//                         <div className="row">
//                           <div className="col-lg-6 mb-3">
//                             <label htmlFor="project" className="form-label">Select Project</label>
//                             {/* Add project selection here */}
//                           </div>

//                           <div className="col-lg-6 mb-3">
//                             <label htmlFor="clientType" className="form-label">Client Type</label>
//                             <div className="d-flex">
//                               <div className="form-check">
//                                 <input className="form-check-input" type="radio" name="clientType" id="clientType1" value="BROKER" checked={formData.clientType === 'BROKER'} onChange={handleChange} />
//                                 <label className="form-check-label" htmlFor="clientType1">Broker</label>
//                               </div>
//                               <div className="form-check ms-4">
//                                 <input className="form-check-input" type="radio" name="clientType" id="clientType2" value="BUILDER" checked={formData.clientType === 'BUILDER'} onChange={handleChange} />
//                                 <label className="form-check-label" htmlFor="clientType2">Builder</label>
//                               </div>
//                               <div className="form-check ms-4">
//                                 <input className="form-check-input" type="radio" name="clientType" id="clientType3" value="OTHER" checked={formData.clientType === 'OTHER'} onChange={handleChange} />
//                                 <label className="form-check-label" htmlFor="clientType3">Other</label>
//                               </div>
//                             </div>
//                           </div>

//                           {/* Send SMS */}
//                           <div className="col-lg-6 mb-3">
//                             <label htmlFor="sendSMS" className="form-label">Send SMS</label>
//                             <div className="d-flex">
//                               <div className="form-check">
//                                 <input className="form-check-input" type="radio" name="sendSMS" id="sendSMS1" value="true" checked={formData.sendSMS === 'true'} onChange={handleChange} />
//                                 <label className="form-check-label" htmlFor="sendSMS1">Yes</label>
//                               </div>
//                               <div className="form-check ms-5">
//                                 <input className="form-check-input" type="radio" name="sendSMS" id="sendSMS2" value="false" checked={formData.sendSMS === 'false'} onChange={handleChange} />
//                                 <label className="form-check-label" htmlFor="sendSMS2">No</label>
//                               </div>
//                             </div>
//                           </div>

//                           {/* Client Status */}
//                           <div className="col-lg-6 mb-3">
//                             <label htmlFor="clientStatus" className="form-label">Client Status</label>
//                             <div className="d-flex">
//                               <div className="form-check">
//                                 <input className="form-check-input" type="radio" name="clientStatus" id="clientStatus1" value="true" checked={formData.clientStatus === 'true'} onChange={handleChange} />
//                                 <label className="form-check-label" htmlFor="clientStatus1">Enabled</label>
//                               </div>
//                               <div className="form-check ms-4">
//                                 <input className="form-check-input" type="radio" name="clientStatus" id="clientStatus2" value="false" checked={formData.clientStatus === 'false'} onChange={handleChange} />
//                                 <label className="form-check-label" htmlFor="clientStatus2">Disabled</label>
//                               </div>
//                             </div>
//                           </div>
//                         </div>

//                         <div className="d-flex align-items-start gap-3 mt-4">
//                           <button type="button" className="btn btn-link text-decoration-none btn-label" onClick={() => setStep(1)}>
//                             <i className="ri-arrow-left-line label-icon align-middle fs-16 me-2"></i> Back to General
//                           </button>
//                           <button type="button" className="btn btn-success btn-label right ms-auto" onClick={() => setStep(3)}>
//                             <i className="ri-arrow-right-line label-icon align-middle fs-16 ms-2"></i> Next
//                           </button>
//                         </div>
//                       </div>

//                       {/* Step 3: Address Info */}
//                       <div className={`tab-pane fade ${step === 3 ? 'show active' : ''}`}>
//                         <div className="row mb-3 mt-4">
//                           <div className="col-lg-6 mb-3">
//                             <label htmlFor="address1" className="form-label">Address 1</label>
//                             <input type="text" className="form-control" id="address1" name="address1" placeholder="Address 1" value={formData.address1} onChange={handleChange} />
//                           </div>
//                           <div className="col-lg-6 mb-3">
//                             <label htmlFor="address2" className="form-label">Address 2</label>
//                             <input type="text" className="form-control" id="address2" name="address2" placeholder="Address 2" value={formData.address2} onChange={handleChange} />
//                           </div>
//                           <div className="col-lg-6 mb-3">
//                             <label htmlFor="area" className="form-label">Area</label>
//                             <input type="text" className="form-control" id="area" name="area" placeholder="Area" value={formData.area} onChange={handleChange} />
//                           </div>
//                           <div className="col-lg-6 mb-3">
//                             <label htmlFor="state" className="form-label">State</label>
//                             <input type="text" className="form-control" id="state" name="state" placeholder="State" value={formData.state} onChange={handleChange} />
//                           </div>
//                           <div className="col-lg-6 mb-3">
//                             <label htmlFor="city" className="form-label">City</label>
//                             <input type="text" className="form-control" id="city" name="city" placeholder="City" value={formData.city} onChange={handleChange} />
//                           </div>
//                           <div className="col-lg-6 mb-3">
//                             <label htmlFor="zip" className="form-label">ZIP Code</label>
//                             <input type="text" className="form-control" id="zip" name="zip" placeholder="ZIP" value={formData.zip} onChange={handleChange} />
//                           </div>
//                         </div>
//                         <div className="d-flex align-items-start gap-3 mt-4">
//                           <button type="button" className="btn btn-link text-decoration-none btn-label" onClick={() => setStep(2)}>
//                             <i className="ri-arrow-left-line label-icon align-middle fs-16 me-2"></i> Back to More Info
//                           </button>
//                           <button type="submit" className="btn btn-success btn-label right ms-auto">
//                             <i className="ri-check-line label-icon align-middle fs-16 ms-2"></i> Submit
//                           </button>
//                         </div>
//                       </div>
//                     </div>
//                   </form>
//                 </div>
//               </div>
//             </div>
//           </div>
//         </div>
//       </div>
//     </>
//   );
// };

// export default ClientAdd;


import React, { useState } from 'react';
import Sidebar from '../Sidebar/Sidebar';

const ClientAdd = () => {
  const [step, setStep] = useState(1);
  const [formData, setFormData] = useState({
    clientName: '',
    email: '',
    alternativeEmail: '',
    phoneNumber: '',
    contactPerson: '',
    clientType: 'BROKER',
    sendSMS: 'true',
    clientStatus: 'true',
    address1: '',
    address2: '',
    area: '',
    state: '',
    city: '',
    zip: '',
  });

  const createClient = (e) => {
    e.preventDefault();
    // Handle form submission
    console.log(formData);
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div className='mt-5'  style={{
      display: "flex",
      minHeight: "100vh",
      backgroundColor: "#f0f0f0",
    }}>

      <Sidebar/>
      <div className="page-content flex-grow-1 p-3 mt-3">
        <div className="row">
          <div className="col-12"
          // style={{ backgroundColor: "white" }}
          >
            <div className="page-title-box d-sm-flex align-items-center justify-content-between">
              <h4 className="mb-sm-0">Clients</h4>

              <div className="page-title-right">
                <ol className="breadcrumb m-0">
                  <li className="breadcrumb-item"><a href="javascript: void(0);">Clients</a></li>
                  <li className="breadcrumb-item"><a href="/app/Clients">Client List</a></li>
                  <li className="breadcrumb-item active">Create Client</li>
                </ol>
              </div>
            </div>
          </div>
        </div>

        <div className="container-fluid">
          <div className="row">
            <div className="col-xl-12">
              <div className="card">
                <div className="card-header">
                  <h4 className="card-title mb-0">Create New Client</h4>
                </div>
                <div className="card-body mt-4">
                  <form onSubmit={createClient} className="form-steps" autoComplete="off">
                    <div id="custom-progress-bar" className="progress-nav mb-4">
                      <div className="progress" style={{ height: '1px' }}>
                        <div className="progress-bar" role="progressbar" style={{ width: `${(step - 1) * 50}%` }} aria-valuenow={step - 1} aria-valuemin="0" aria-valuemax="100"></div>
                      </div>

                      <ul className="nav nav-pills progress-bar-tab custom-nav" role="tablist">
                        <li className="nav-item" role="presentation">
                          <button className={`nav-link rounded-pill ${step === 1 ? 'active' : ''}`} type="button" onClick={() => setStep(1)}>1</button>
                        </li>
                        <li className="nav-item" role="presentation">
                          <button className={`nav-link rounded-pill ${step === 2 ? 'active' : ''}`} type="button" onClick={() => setStep(2)}>2</button>
                        </li>
                        <li className="nav-item" role="presentation">
                          <button className={`nav-link rounded-pill ${step === 3 ? 'active' : ''}`} type="button" onClick={() => setStep(3)}>3</button>
                        </li>
                      </ul>
                    </div>

                    <div className="tab-content">
                      {/* Step 1: Personal Details */}
                      <div className={`tab-pane fade ${step === 1 ? 'show active' : ''}`}>
                        <h5 className="mb-1 mx-4">Personal Details</h5>
                        <p className="text-muted mx-4">Fill all Information as below</p>
                        <div className="row">
                          <div className="col-lg-5 mb-3 mx-2">
                            <label htmlFor="clientName" className="form-label">Client Name</label>
                            <input type="text" className="form-control" id="clientName" name="clientName" placeholder="Client Name" value={formData.clientName} onChange={handleChange} required />
                          </div>
                          <div className="col-lg-6 mb-3 mx-2">
                            <label htmlFor="email" className="form-label">Email</label>
                            <input type="email" className="form-control" id="email" name="email" placeholder="Email" value={formData.email} onChange={handleChange} required />
                          </div>
                          <div className="col-lg-5 mb-3 mx-2">
                            <label htmlFor="alternativeEmail" className="form-label">Alternative Email</label>
                            <input type="email" className="form-control" id="alternativeEmail" name="alternativeEmail" placeholder="Alternative Email" value={formData.alternativeEmail} onChange={handleChange} />
                          </div>
                          <div className="col-lg-6 mb-3">
                            <label htmlFor="phoneNumber" className="form-label">Phone Number</label>
                            <input type="tel" className="form-control" id="phoneNumber" name="phoneNumber" placeholder="Phone Number" value={formData.phoneNumber} onChange={handleChange} required />
                          </div>
                          <div className="col-lg-6 mb-3 mx-2">
                            <label htmlFor="contactPerson" className="form-label">Contact Person</label>
                            <input type="text" className="form-control" id="contactPerson" name="contactPerson" placeholder="Contact Person" value={formData.contactPerson} onChange={handleChange} required />
                          </div>
                        </div>
                        <div className="d-flex align-items-start gap-3 mt-4">
                          <button
                            type="button"
                            className="btn btn-success btn-label right ms-auto mb-3 mx-4"
                            onClick={() => setStep(2)}
                          >
                            <i className="ri-arrow-right-line label-icon align-middle fs-16 ms-2"></i> Go to more info
                          </button>
                        </div>
                      </div>

                      {/* Step 2: Additional Info */}
                      <div className={`tab-pane fade ${step === 2 ? 'show active' : ''}`}>
                        <div className="row">
                          <div className="col-lg-5 mx-2 mb-3">
                            <label htmlFor="project" className="form-label">Select Project</label>
                            {/* Add project selection here */}
                          </div>

                          <div className="col-lg-6 mb-3">
                            <label htmlFor="clientType" className="form-label">Client Type</label>
                            <div className="d-flex">
                              <div className="form-check">
                                <input className="form-check-input" type="radio" name="clientType" id="clientType1" value="BROKER" checked={formData.clientType === 'BROKER'} onChange={handleChange} />
                                <label className="form-check-label" htmlFor="clientType1">Broker</label>
                              </div>
                              <div className="form-check ms-4">
                                <input className="form-check-input" type="radio" name="clientType" id="clientType2" value="BUILDER" checked={formData.clientType === 'BUILDER'} onChange={handleChange} />
                                <label className="form-check-label" htmlFor="clientType2">Builder</label>
                              </div>
                              <div className="form-check ms-4">
                                <input className="form-check-input" type="radio" name="clientType" id="clientType3" value="OTHER" checked={formData.clientType === 'OTHER'} onChange={handleChange} />
                                <label className="form-check-label" htmlFor="clientType3">Other</label>
                              </div>
                            </div>
                          </div>

                          {/* Send SMS */}
                          <div className="col-lg-5 mx-2 mb-3">
                            <label htmlFor="sendSMS" className="form-label">Send SMS</label>
                            <div className="d-flex">
                              <div className="form-check">
                                <input className="form-check-input" type="radio" name="sendSMS" id="sendSMS1" value="true" checked={formData.sendSMS === 'true'} onChange={handleChange} />
                                <label className="form-check-label" htmlFor="sendSMS1">Yes</label>
                              </div>
                              <div className="form-check ms-5">
                                <input className="form-check-input" type="radio" name="sendSMS" id="sendSMS2" value="false" checked={formData.sendSMS === 'false'} onChange={handleChange} />
                                <label className="form-check-label" htmlFor="sendSMS2">No</label>
                              </div>
                            </div>
                          </div>

                          {/* Client Status */}
                          <div className="col-lg-6 mb-3">
                            <label htmlFor="clientStatus" className="form-label">Client Status</label>
                            <div className="d-flex">
                              <div className="form-check">
                                <input className="form-check-input" type="radio" name="clientStatus" id="clientStatus1" value="true" checked={formData.clientStatus === 'true'} onChange={handleChange} />
                                <label className="form-check-label" htmlFor="clientStatus1">Active</label>
                              </div>
                              <div className="form-check ms-4">
                                <input className="form-check-input" type="radio" name="clientStatus" id="clientStatus2" value="false" checked={formData.clientStatus === 'false'} onChange={handleChange} />
                                <label className="form-check-label" htmlFor="clientStatus2">Inactive</label>
                              </div>
                            </div>
                          </div>
                        </div>

                        <div className="d-flex align-items-start gap-3 mt-4">
                          <button type="button" className="btn btn-link text-decoration-none btn-label mx-4 mb-2" onClick={() => setStep(1)}>
                            <i className="ri-arrow-left-line label-icon align-middle fs-16 me-2"></i> Back to Personal Info
                          </button>
                          <button type="button" className="btn btn-success btn-label right ms-auto" onClick={() => setStep(3)}>
                            <i className="ri-arrow-right-line label-icon align-middle fs-16 ms-2"></i> Go to Address Details
                          </button>
                        </div>
                      </div>

                      {/* Step 3: Address Details */}
                      <div className={`tab-pane fade ${step === 3 ? 'show active' : ''}`}>
                        <h5 className="mb-1 mx-4">Address Details</h5>
                        <p className="text-muted mx-4">Fill all Information as below</p>
                        <div className="row">
                          <div className="col-lg-5 mx-2 mb-3">
                            <label htmlFor="address1" className="form-label">Address 1</label>
                            <input type="text" className="form-control" id="address1" name="address1" placeholder="Address 1" value={formData.address1} onChange={handleChange} />
                          </div>
                          <div className="col-lg-6 mb-3 mx-2">
                            <label htmlFor="address2" className="form-label">Address 2</label>
                            <input type="text" className="form-control" id="address2" name="address2" placeholder="Address 2" value={formData.address2} onChange={handleChange} />
                          </div>
                          <div className="col-lg-5 mx-2 mb-3">
                            <label htmlFor="area" className="form-label">Area</label>
                            <input type="text" className="form-control" id="area" name="area" placeholder="Area" value={formData.area} onChange={handleChange} />
                          </div>
                          <div className="col-lg-6 mb-3">
                            <label htmlFor="state" className="form-label">State</label>
                            <input type="text" className="form-control" id="state" name="state" placeholder="State" value={formData.state} onChange={handleChange} />
                          </div>
                          <div className="col-lg-5 mx-2 mb-3">
                            <label htmlFor="city" className="form-label">City</label>
                            <input type="text" className="form-control" id="city" name="city" placeholder="City" value={formData.city} onChange={handleChange} />
                          </div>
                          <div className="col-lg-6 mb-3">
                            <label htmlFor="zip" className="form-label">ZIP Code</label>
                            <input type="text" className="form-control" id="zip" name="zip" placeholder="ZIP" value={formData.zip} onChange={handleChange} />
                          </div>
                        </div>
                        <div className="d-flex align-items-start gap-3 mt-4">
                          <button type="button" className="btn btn-link text-decoration-none btn-label" onClick={() => setStep(2)}>
                            <i className="ri-arrow-left-line label-icon align-middle fs-16 me-2"></i> Back to More Info
                          </button>
                          <button type="submit" className="btn btn-success btn-label right ms-auto mb-2 mx-2">
                            <i className="ri-check-line label-icon align-middle fs-16 ms-2"></i> Submit
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
  );
};

export default ClientAdd;
