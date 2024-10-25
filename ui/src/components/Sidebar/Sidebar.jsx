import * as React from "react";
import { styled, useTheme } from "@mui/material/styles";
import Box from "@mui/material/Box";
import MuiDrawer from "@mui/material/Drawer";
import Divider from "@mui/material/Divider";
import IconButton from "@mui/material/IconButton";
import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import { Link } from "react-router-dom";
import { useAppStore } from "../../appStore";
import { useNavigate } from "react-router-dom";
import "remixicon/fonts/remixicon.css";
import { useSelector } from 'react-redux';
import UploadFileIcon from '@mui/icons-material/UploadFile';


const drawerWidth = 240;

const openedMixin = (theme) => ({
  width: drawerWidth,
  transition: theme.transitions.create("width", {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.enteringScreen,
  }),
  overflowX: "hidden",
});

const closedMixin = (theme) => ({
  transition: theme.transitions.create("width", {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  overflowX: "hidden",
  width: `calc(${theme.spacing(7)} + 1px)`,
  [theme.breakpoints.up("sm")]: {
    width: `calc(${theme.spacing(8)} + 1px)`,
  },
});

const DrawerHeader = styled("div")(({ theme }) => ({
  display: "flex",
  alignItems: "center",
  justifyContent: "flex-end",
  padding: theme.spacing(0, 1),
  ...theme.mixins.toolbar,
}));

const Drawer = styled(MuiDrawer, {
  shouldForwardProp: (prop) => prop !== "open",
})(({ theme, open }) => ({
  width: drawerWidth,
  flexShrink: 0,
  whiteSpace: "nowrap",
  boxSizing: "border-box",
  ...(open && {
    ...openedMixin(theme),
    "& .MuiDrawer-paper": openedMixin(theme),
  }),
  ...(!open && {
    ...closedMixin(theme),
    "& .MuiDrawer-paper": closedMixin(theme),
  }),
}));

export default function Sidebar() {
  const theme = useTheme();
  const dopen = useAppStore((state) => state.dopen);
  const updateOpen = useAppStore((state) => state.updateOpen);

  const handleDrawerToggle = () => {
    updateOpen(!dopen);
  };
  const role = useSelector((state) => state.user.role);

  return (
    <Box sx={{ display: "flex" }}>
      <Drawer variant="permanent" open={dopen}>
        <DrawerHeader>
          <IconButton onClick={handleDrawerToggle}>
            {theme.direction === "rtl" ? (
              <ChevronRightIcon />
            ) : (
              <ChevronLeftIcon />
            )}
          </IconButton>
        </DrawerHeader>
        <Divider />
        <List>
          <ListItem disablePadding sx={{ display: "block" }}>
            <ListItemButton
              component={Link}
              to="/dashboard"
              sx={{
                minHeight: 48,
                justifyContent: dopen ? "initial" : "center",
                px: 2.5,
                "&:hover": {
                  backgroundColor: "white",
                  color: "blue",
                },
              }}
            >
              <ListItemIcon
                sx={{
                  minWidth: 0,
                  mr: dopen ? 3 : "auto",
                  justifyContent: "center",
                  color: "inherit",
                }}
              >
                <i className="ri-speed-up-fill" style={{ fontSize: 32 }}></i>
              </ListItemIcon>
              <ListItemText
                primary="Dashboard"
                sx={{ opacity: dopen ? 1 : 0 }}
                primaryTypographyProps={{ fontSize: "1.4rem" }}
              />
            </ListItemButton>
          </ListItem>

          <ListItem disablePadding sx={{ display: "block" }}>
            <ListItemButton
              component={Link}
              to="/Projects"
              sx={{
                minHeight: 48,
                justifyContent: dopen ? "initial" : "center",
                px: 2.5,
                "&:hover": {
                  backgroundColor: "white",
                  color: "blue",
                  "& .MuiListItemText-root": {
                    opacity: 1, // Show text on hover
                    display: "block", // Make sure text is displayed as block
                    zIndex: 1111,
                    backgroundColor: "yellow",
                    opacity: 1,
                    display: "block",
                    zIndex: 1111,
                    backgroundColor: "#6d7080",
                    color: "white",
                    position: "absolute",
                    left: 0,
                    top: "50%",
                    transform: "translateY(-50%)",
                    width: "100%", // Adjust width as needed
                  },
                },
              }}
            >
              <ListItemIcon
                sx={{
                  minWidth: 0,
                  mr: dopen ? 3 : "auto",
                  justifyContent: "center",
                  color: "inherit",
                }}
              >
                <i className="ri-building-2-line" style={{ fontSize: 32 }}></i>
              </ListItemIcon>
              <ListItemText
                primary="Projects"
                sx={{ opacity: dopen ? 1 : 0 }}
                primaryTypographyProps={{ fontSize: "1.4rem" }}
              />
            </ListItemButton>
          </ListItem>

          <ListItem disablePadding sx={{ display: role=="CLIENT" ?"block":"none" }}>
            <ListItemButton
              component={Link}
              to="/Users"
              sx={{
                minHeight: 48,
                justifyContent: dopen ? "initial" : "center",
                px: 2.5,
                "&:hover": {
                  backgroundColor: "white",
                  color: "blue",
                },
              }}
            >
              <ListItemIcon
                sx={{
                  minWidth: 0,
                  mr: dopen ? 3 : "auto",
                  justifyContent: "center",
                  color: "inherit",
                }}
              >
                <i className="ri-team-line" style={{ fontSize: 32 }}></i>
              </ListItemIcon>
              <ListItemText
                primary="Users"
                sx={{ opacity: dopen ? 1 : 0 }}
                primaryTypographyProps={{ fontSize: "1.4rem" }}
              />
            </ListItemButton>
          </ListItem>
          <ListItem disablePadding sx={{ display: role=="ADMIN" ?"block":"none" }}>
            <ListItemButton
              component={Link}
              to="/Client"
              sx={{
                minHeight: 48,
                justifyContent: dopen ? "initial" : "center",
                px: 2.5,
                "&:hover": {
                  backgroundColor: "white",
                  color: "blue",
                },
              }}
            >
              <ListItemIcon
                sx={{
                  minWidth: 0,
                  mr: dopen ? 3 : "auto",
                  justifyContent: "center",
                  color: "inherit",
                }}
              >
                <i className="ri-team-line" style={{ fontSize: 32 }}></i>
              </ListItemIcon>
              <ListItemText
                primary="Client"
                sx={{ opacity: dopen ? 1 : 0 }}
                primaryTypographyProps={{ fontSize: "1.4rem" }}
              />
            </ListItemButton>
          </ListItem>
        </List>
        <Divider />
      </Drawer>
    </Box>
  );
}
