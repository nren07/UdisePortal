import{r as a,a as b,b as x,c as T,j as e}from"./index-4C0lOHpC.js";function y(){const[r,n]=a.useState({jobType:"",file:null,jobTitle:""}),[i,c]=a.useState({TitleError:"",optionError:"",fileError:""}),[d,f]=a.useState(!1),p=b(x),u=b(T),m=o=>["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"].includes(o.type)?o.size>5242880?"File size exceeds 5MB.":"":"Invalid file type. Only .xlsx files are allowed.",j=()=>r.file&&r.jobTitle&&r.jobType,h=a.useCallback(async o=>{if(j()){f(!0);const t=new FormData;t.append("jobType",r.jobType),t.append("file",r.file),t.append("jobTitle",r.jobTitle);try{const l=await fetch(`https://udise.pytosoft.com/v1/fileupload/${p}/upload`,{method:"POST",body:t,headers:{Authorization:`Bearer ${u}`},signal:o});if(!l.ok)throw new Error("Failed to upload");n({jobType:"",file:null,jobTitle:""}),console.log(await l.json())}catch(l){l.name==="AbortError"?console.log("Request aborted"):(console.error("Error uploading file:",l),alert("An error occurred while uploading the file"))}finally{f(!1)}}},[r,p,u]),E=o=>{o.preventDefault();const t={TitleError:"",optionError:"",fileError:""};let l=!1;if(r.jobTitle||(t.TitleError="Please provide a job title",l=!0),r.jobType||(t.optionError="Please select a job type",l=!0),!r.file)t.fileError="Please choose a file",l=!0;else{const s=m(r.file);s&&(t.fileError=s,l=!0)}if(c(t),!l){const s=new AbortController;return h(s.signal),()=>s.abort()}};return a.useEffect(()=>{c({TitleError:"",optionError:"",fileError:""})},[r]),e.jsx("div",{className:"container my-5",children:e.jsx("div",{className:"row justify-content-center",children:e.jsx("div",{className:"col-lg-6 col-md-8 col-sm-12",children:e.jsxs("form",{onSubmit:E,className:"p-4 shadow rounded bg-white",children:[e.jsxs("div",{className:"mb-3",children:[e.jsx("label",{htmlFor:"jobTitle",className:"form-label",children:"Job Title"}),e.jsx("input",{id:"jobTitle",type:"text",className:`form-control ${i.TitleError&&"is-invalid"}`,value:r.jobTitle,onChange:o=>n({...r,jobTitle:o.target.value})}),i.TitleError&&e.jsx("div",{className:"invalid-feedback",children:i.TitleError})]}),e.jsxs("div",{className:"mb-3",children:[e.jsx("label",{htmlFor:"dropdown",className:"form-label",children:"Job Type"}),e.jsxs("select",{id:"dropdown",className:`form-select ${i.optionError&&"is-invalid"}`,value:r.jobType,onChange:o=>n({...r,jobType:o.target.value}),children:[e.jsx("option",{value:"",disabled:!0,children:"Select an option"}),e.jsx("option",{value:"UDISE",children:"UDISE"}),e.jsx("option",{value:"DUMMY",children:"DUMMY"})]}),i.optionError&&e.jsx("div",{className:"invalid-feedback",children:i.optionError})]}),e.jsxs("div",{className:"mb-3",children:[e.jsx("label",{htmlFor:"fileInput",className:"form-label",children:"Choose File"}),e.jsx("input",{id:"fileInput",type:"file",className:`form-control ${i.fileError&&"is-invalid"}`,onChange:o=>n({...r,file:o.target.files[0]})}),i.fileError&&e.jsx("div",{className:"invalid-feedback",children:i.fileError})]}),e.jsx("button",{type:"submit",className:"btn btn-success w-100",disabled:d,children:d?"Uploading...":"Submit"})]})})})})}export{y as default};