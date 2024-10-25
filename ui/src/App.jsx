// App.jsx

import React, { Suspense, lazy } from "react";
import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";
import { Provider } from "react-redux";
import store from "./store/store.js";
import { WebSocketProvider } from "./components/context/WebSocketProvider.jsx";

// Lazy load components
const Dashboard = lazy(() => import("./Pages/Dashboard"));
const Projects = lazy(() => import("./Pages/Projects"));
const Leads = lazy(() => import("./Pages/Leads"));
const Users = lazy(() => import("./Pages/Users"));
const Client = lazy(() => import("./Pages/Client"));
const ProjectAdd = lazy(() => import("./components/project/ProjectAdd"));
const CreateLeads = lazy(() => import("./components/create/CreateLeads"));
const ClientAdd = lazy(() => import("./components/client/ClientAdd"));
const ProjectTitle = lazy(() => import("./components/project/ProjectTitle"));
const Navbar = lazy(() => import("./components/Navbar/Navbar"));
const Login = lazy(() => import("./components/Login/Login"));
const Footer = lazy(() => import("./components/footer/Footer"));
// const EventListener = lazy(() => import("./components/Dummy/EventListener.jsx"));
const Upload = lazy(() => import("./components/Uploads/Upload.jsx"));

const App = () => {
  const location = useLocation();

  return (
    <div>
      {location.pathname !== "/" && <Navbar />}
      <Suspense fallback={<div>Loading...</div>}>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/Upload" element={<Upload />} />
          <Route path="/Dashboard" element={<Dashboard />} />
          <Route path="/Projects" element={<Projects />} />
          <Route path="/Leads" element={<Leads />} />
          <Route path="/Client" element={<Client />} />
          <Route path="/Users" element={<Users />} />
          <Route path="/ProjectAdd" element={<ProjectAdd />} />
          <Route path="/CreateLeads" element={<CreateLeads />} />
          <Route path="/ClientAdd" element={<ClientAdd />} />
          <Route path="/ProjectTitle/:id" element={<ProjectTitle />} />
        </Routes>
      </Suspense>
      {location.pathname !== "/" && <Footer />}
    </div>
  );
};

const AppWrapper = () => (
  <Provider store={store}>
     <WebSocketProvider>
        <BrowserRouter>
            <App />
        </BrowserRouter>
    </WebSocketProvider>
   </Provider>
);

export default AppWrapper;
