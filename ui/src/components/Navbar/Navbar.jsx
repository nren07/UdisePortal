// import * as React from "react";
// import { styled } from "@mui/material/styles";
// import MuiAppBar from "@mui/material/AppBar";
// import Box from "@mui/material/Box";
// import Toolbar from "@mui/material/Toolbar";
// import IconButton from "@mui/material/IconButton";
// import Badge from "@mui/material/Badge";
// import MenuItem from "@mui/material/MenuItem";
// import Menu from "@mui/material/Menu";
// import MenuIcon from "@mui/icons-material/Menu";
// import CloseIcon from "@mui/icons-material/Close"; // Import CloseIcon
// import AccountCircle from "@mui/icons-material/AccountCircle";
// import MailIcon from "@mui/icons-material/Mail";
// import NotificationsIcon from "@mui/icons-material/Notifications";
// import MoreIcon from "@mui/icons-material/MoreVert";
// import { useAppStore } from "../../appStore";
// import icon from "../../assets/images/icon.jpg";
// import logo from "../../assets/logo.png";

// import ListItemIcon from "@mui/material/ListItemIcon";
// import LogoutIcon from "@mui/icons-material/Logout";

// const AppBar = styled(
//   MuiAppBar,
//   {}
// )(({ theme }) => ({
//   zIndex: theme.zIndex.drawer + 1,
// }));

// export default function Navbar() {
//   const [anchorEl, setAnchorEl] = React.useState(null);
//   const [mobileMoreAnchorEl, setMobileMoreAnchorEl] = React.useState(null);
//   const updateOpen = useAppStore((state) => state.updateOpen);
//   const dopen = useAppStore((state) => state.dopen);

//   const isMenuOpen = Boolean(anchorEl);
//   const isMobileMenuOpen = Boolean(mobileMoreAnchorEl);

//   const handleDrawerToggle = () => {
//     updateOpen(!dopen);
//   };

//   const handleProfileMenuOpen = (event) => {
//     setAnchorEl(event.currentTarget);
//   };

//   const handleMobileMenuClose = () => {
//     setMobileMoreAnchorEl(null);
//   };

//   const handleMenuClose = () => {
//     setAnchorEl(null);
//     handleMobileMenuClose();
//   };

//   const handleMobileMenuOpen = (event) => {
//     setMobileMoreAnchorEl(event.currentTarget);
//   };

//   const menuId = "primary-search-account-menu";
//   const renderMenu = (
//     <Menu
//       anchorEl={anchorEl}
//       anchorOrigin={{
//         vertical: "bottom",
//         horizontal: "right",
//       }}
//       id={menuId}
//       keepMounted
//       transformOrigin={{
//         vertical: "top",
//         horizontal: "right",
//       }}
//       open={isMenuOpen}
//       onClose={handleMenuClose}
//       PaperProps={{
//         sx: {
//           width: "230px",
//           height: "80px",
//         },
//       }}
//     >
//       <MenuItem disabled sx={{ mt: -1 }}>
//         <h3>Welcome Suthar</h3>
//       </MenuItem>

//       <MenuItem onClick={handleMenuClose} sx={{ mt: -1 }}>
//         <ListItemIcon>
//           <LogoutIcon fontSize="small" />
//         </ListItemIcon>
//         Logout
//       </MenuItem>
//     </Menu>
//   );

//   const mobileMenuId = "primary-search-account-menu-mobile";
//   const renderMobileMenu = (
//     <Menu
//       anchorEl={mobileMoreAnchorEl}
//       anchorOrigin={{
//         vertical: "top",
//         horizontal: "right",
//       }}
//       id={mobileMenuId}
//       keepMounted
//       transformOrigin={{
//         vertical: "top",
//         horizontal: "right",
//       }}
//       open={isMobileMenuOpen}
//       onClose={handleMobileMenuClose}
//     >
//       <MenuItem>
//         <IconButton size="large" aria-label="show 4 new mails" color="inherit">
//           <Badge badgeContent={4} color="error">
//             <MailIcon />
//           </Badge>
//         </IconButton>
//         <p>Messages</p>
//       </MenuItem>
//       <MenuItem>
//         <IconButton
//           size="large"
//           aria-label="show 17 new notifications"
//           color="inherit"
//         >
//           <Badge badgeContent={17} color="error">
//             <NotificationsIcon />
//           </Badge>
//         </IconButton>
//         <p>Notifications</p>
//       </MenuItem>
//       <MenuItem onClick={handleProfileMenuOpen}>
//         <IconButton
//           size="large"
//           aria-label="account of current user"
//           aria-controls="primary-search-account-menu"
//           aria-haspopup="true"
//           color="inherit"
//         >
//           <AccountCircle />
//         </IconButton>
//         <p>Profile</p>
//       </MenuItem>
//     </Menu>
//   );

//   return (
//     <Box sx={{ flexGrow: 1 }}>
//       <AppBar
//         position="fixed"
//         elevation={0}
//         sx={{ backgroundColor: "#ffffff" }}
//       >
//         <Toolbar>
//           {/* Logo Icon (comes first) */}
//           {/* <Box component="div" sx={{ display: { xs: "none", sm: "block" } }}>
//             <img
//               src={icon}
//               alt="Logo"
//               style={{ height: "40px", cursor: "pointer" }}
//             />
//           </Box> */}
//           <Box component="div" sx={{ display: { xs: "none", sm: "block" } }}>
//             {dopen ? (
//               <img
//                 src={logo} // use another image when drawer is open
//                 alt="Alternative Logo"
//                 style={{
//                   width: "100px",
//                   height: "60px",
//                   cursor: "pointer",
//                   marginLeft: "40px",
//                 }}
//               />
//             ) : (
//               <img
//                 src={icon} // original logo
//                 alt="Logo"
//                 style={{ height: "40px", cursor: "pointer" }}
//               />
//             )}
//           </Box>
//           You'll need to define
//           {/* Menu Icon */}
//           <IconButton
//             size="large"
//             edge="start"
//             color="black"
//             aria-label="open drawer"
//             onClick={handleDrawerToggle}
//             sx={{
//               mr: 2,
//               ml: 2,
//               transition: "transform 0.1s ease-in-out",

//               transform: dopen ? "translateX(-100px)" : "translateX(0)",

//               // transform: dopen
//               // ? "rotate(90deg) translateX(160px)"
//               // : "rotate(0deg) translateX(0)",
//             }}
//           >
//             {dopen ? <CloseIcon /> : <MenuIcon />}
//           </IconButton>
//           <Box sx={{ flexGrow: 1 }} />
//           {/* Account, Notification, and Mail Icons */}
//           <Box sx={{ display: { xs: "none", md: "flex" } }}>
//             <IconButton
//               size="large"
//               aria-label="show 4 new mails"
//               color="black"
//             >
//               <Badge badgeContent={4} color="error">
//                 <MailIcon />
//               </Badge>
//             </IconButton>
//             <IconButton
//               size="large"
//               aria-label="show 17 new notifications"
//               color="black"
//             >
//               <Badge badgeContent={17} color="error">
//                 <NotificationsIcon />
//               </Badge>
//             </IconButton>
//             <IconButton
//               size="large"
//               edge="end"
//               aria-label="account of current user"
//               aria-controls={menuId}
//               aria-haspopup="true"
//               onClick={handleProfileMenuOpen}
//               color="black"
//               sx={{ mt: 2, mb: 2, ml: 4, pt: 1, pr: 2, pb: 1, pl: 2 }}
//               style={{ textDecoration: "none" }}
//             >
//               <AccountCircle />
//               <Box
//                 component="span"
//                 sx={{
//                   ml: 4,
//                   mt: -2,
//                   display: "flex",
//                   alignItems: "center",
//                   flexDirection: "column",
//                 }}
//               >
//                 {/* <h6 style={{ margin: 2 }}>Suthar</h6>
//                 <p style={{ margin: 0, fontSize: "0.875rem" }}>6350513177</p> */}
//               </Box>
//             </IconButton>
//           </Box>
//           <Box sx={{ display: { xs: "flex", md: "none" } }}>
//             <IconButton
//               size="large"
//               aria-label="show more"
//               aria-controls={mobileMenuId}
//               aria-haspopup="true"
//               onClick={handleMobileMenuOpen}
//               color="black"
//             >
//               <MoreIcon />
//             </IconButton>
//           </Box>
//         </Toolbar>
//       </AppBar>
//       {renderMobileMenu}
//       {renderMenu}
//     </Box>
//   );
// }

// import * as React from "react";
import React, { useEffect, useState } from "react";
import { styled } from "@mui/material/styles";
import MuiAppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Badge from "@mui/material/Badge";
import MenuItem from "@mui/material/MenuItem";
import Menu from "@mui/material/Menu";
import MenuIcon from "@mui/icons-material/Menu";
import CloseIcon from "@mui/icons-material/Close";
import AccountCircle from "@mui/icons-material/AccountCircle";
import MailIcon from "@mui/icons-material/Mail";
import NotificationsIcon from "@mui/icons-material/Notifications";
import MoreIcon from "@mui/icons-material/MoreVert";
import { useAppStore } from "../../appStore";
import icon from "../../assets/images/icon.jpg";
import logo from "../../assets/logo.png";
import ListItemIcon from "@mui/material/ListItemIcon";
import LogoutIcon from "@mui/icons-material/Logout";
import { Button, Popover, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { clearUser } from "../../store/userSlice";
import { selectUserId, selectRole, selectToken, selectTokenExpiration } from "../../store/useSelectors";

const AppBar = styled(MuiAppBar)(({ theme }) => ({
  zIndex: theme.zIndex.drawer + 1,
}));

export default function Navbar() {
  const [anchorEl, setAnchorEl] = React.useState(null);
  const [notificationAnchorEl, setNotificationAnchorEl] = React.useState(null);
  const [mobileMoreAnchorEl, setMobileMoreAnchorEl] = React.useState(null);
  const updateOpen = useAppStore((state) => state.updateOpen);
  const dopen = useAppStore((state) => state.dopen);

  const isMenuOpen = Boolean(anchorEl);
  const isMobileMenuOpen = Boolean(mobileMoreAnchorEl);
  const isNotificationOpen = Boolean(notificationAnchorEl);

  const userId = useSelector(selectUserId);
  const token = useSelector(selectToken);
  const expirationTime = useSelector(selectTokenExpiration);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    if (Date.now() >= expirationTime || !token || !userId) {
      dispatch(clearUser());
      navigate("/login"); // Ensure the login route
    }
  }, [dispatch, navigate, expirationTime, token, userId]);

  const handleDrawerToggle = () => {
    updateOpen(!dopen);
  };

  const handleProfileMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleNotificationToggle = (event) => {
    setNotificationAnchorEl(event.currentTarget);
  };

  const handleNotificationClose = () => {
    setNotificationAnchorEl(null);
  };

  const handleMobileMenuClose = () => {
    setMobileMoreAnchorEl(null);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
    handleMobileMenuClose();
  };

  const logout = () => {
    dispatch(clearUser());
    handleMenuClose(); // Close the profile menu
    navigate("/"); // Navigate to the login page
  };

  const handleMobileMenuOpen = (event) => {
    setMobileMoreAnchorEl(event.currentTarget);
  };

  const menuId = "primary-search-account-menu";
  const renderMenu = (
    <Menu
      anchorEl={anchorEl}
      anchorOrigin={{
        vertical: "bottom",
        horizontal: "right",
      }}
      id={menuId}
      keepMounted
      transformOrigin={{
        vertical: "top",
        horizontal: "right",
      }}
      open={isMenuOpen}
      onClose={handleMenuClose}
    >
      <MenuItem onClick={logout} sx={{ mt: -1 }}>
        <ListItemIcon>
          <LogoutIcon fontSize="small" />
        </ListItemIcon>
        Logout
      </MenuItem>
    </Menu>
  );

  const mobileMenuId = "primary-search-account-menu-mobile";
  const renderMobileMenu = (
    <Menu
      anchorEl={mobileMoreAnchorEl}
      anchorOrigin={{
        vertical: "top",
        horizontal: "right",
      }}
      id={mobileMenuId}
      keepMounted
      transformOrigin={{
        vertical: "top",
        horizontal: "right",
      }}
      open={isMobileMenuOpen}
      onClose={handleMobileMenuClose}
    >
      <MenuItem>
        <IconButton size="large" aria-label="show 4 new mails" color="inherit">
          <Badge badgeContent={4} color="error">
            <MailIcon />
          </Badge>
        </IconButton>
        <p>Messages</p>
      </MenuItem>
      <MenuItem>
        <IconButton
          size="large"
          aria-label="show 17 new notifications"
          color="inherit"
        >
          <Badge badgeContent={17} color="error">
            <NotificationsIcon />
          </Badge>
        </IconButton>
        <p>Notifications</p>
      </MenuItem>
      <MenuItem onClick={handleProfileMenuOpen}>
        <IconButton
          size="large"
          aria-label="account of current user"
          aria-controls="primary-search-account-menu"
          aria-haspopup="true"
          color="inherit"
        >
          <AccountCircle />
        </IconButton>
        <p>Profile</p>
      </MenuItem>
    </Menu>
  );

  const renderNotifications = (
    <Popover
      anchorEl={notificationAnchorEl}
      open={isNotificationOpen}
      onClose={handleNotificationClose}
      anchorOrigin={{
        vertical: "bottom",
        horizontal: "right",
      }}
    >
      <Box sx={{ p: 2, width: 300 }}>
        <Typography variant="h6" sx={{ mb: 1 }}>
          Notifications
        </Typography>
        <div>
          <Box sx={{ display: "flex", mb: 2 }}>
            <Badge badgeContent={3} color="error">
              <NotificationsIcon />
            </Badge>
            <Typography sx={{ ml: 2 }}>You have 3 new leads.</Typography>
          </Box>
          <Button fullWidth variant="outlined" color="success">
            View All Notifications
          </Button>
        </div>
      </Box>
    </Popover>
  );

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="fixed" elevation={0} sx={{ backgroundColor: "#ffffff" }}>
        <Toolbar>
          {/* Logo Icon */}
          <Box component="div" sx={{ display: { xs: "none", sm: "block" } }}>
            {dopen ? (
              <img
                src={logo}
                alt="Alternative Logo"
                style={{
                  width: "100px",
                  height: "60px",
                  cursor: "pointer",
                  marginLeft: "40px",
                }}
              />
            ) : (
              <img
                src={icon}
                alt="Logo"
                style={{ height: "40px", cursor: "pointer" }}
              />
            )}
          </Box>

          {/* Menu Icon */}
          <IconButton
            size="large"
            edge="start"
            color="black"
            aria-label="open drawer"
            onClick={handleDrawerToggle}
            sx={{
              mr: 2,
              ml: 2,
              transition: "transform 0.1s ease-in-out",
              transform: dopen ? "translateX(50px)" : "translateX(0)",
            }}
          >
            {dopen ? <CloseIcon /> : <MenuIcon />}
          </IconButton>

          <Box sx={{ flexGrow: 1 }} />

          {/* Notifications, Messages, and Profile Icons */}
          <Box sx={{ display: { xs: "none", md: "flex" } }}>
            <IconButton size="large" aria-label="show 4 new mails" color="black">
              <Badge badgeContent={4} color="error">
                <MailIcon />
              </Badge>
            </IconButton>
            <IconButton
              size="large"
              aria-label="show 17 new notifications"
              color="black"
              onClick={handleNotificationToggle}
            >
              <Badge badgeContent={17} color="error">
                <NotificationsIcon />
              </Badge>
            </IconButton>
            <IconButton
              size="large"
              edge="end"
              aria-label="account of current user"
              aria-controls={menuId}
              aria-haspopup="true"
              onClick={handleProfileMenuOpen}
              color="black"
              sx={{ mt: 2, mb: 2, ml: 4, pt: 1, pr: 2, pb: 1, pl: 2 }}
              style={{ textDecoration: "none" }}
            >
              <AccountCircle />
            </IconButton>
          </Box>

          {/* Mobile Menu */}
          <Box sx={{ display: { xs: "flex", md: "none" } }}>
            <IconButton
              size="large"
              aria-label="show more"
              aria-controls={mobileMenuId}
              aria-haspopup="true"
              onClick={handleMobileMenuOpen}
              color="black"
            >
              <MoreIcon />
            </IconButton>
          </Box>
        </Toolbar>
      </AppBar>
      {renderMobileMenu}
      {renderMenu}
      {renderNotifications}
    </Box>
  );
}
