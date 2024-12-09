import { Button } from "bootstrap/dist/js/bootstrap.bundle.min";


const styles = {
    iframeOverlay: {
      position: "fixed",
      top: 20,
      left: 0,
      width: "100%",
      height: "100%",
      // backgroundColor: "rgba(0, 0, 0, 0.8)", // Dimmed background
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      zIndex: 0, // High z-index to keep it on top
    },
    iframeContainer: {
      position: "relative",
      width: "80%", // Adjust width as needed
      height: "80%", // Adjust height as needed
      // backgroundColor: "#fff", // White background to frame the iframe
      // borderRadius: "10px", // Optional rounded corners
      overflow: "hidden", // Ensure the iframe fits neatly
      // boxShadow: "0 0 20px rgba(0, 0, 0, 0.5)", // Subtle shadow for depth
    },
    closeButton: {
      position: "absolute",
      top: "0px",
      right: "0px",
      backgroundColor: "transparent",
      border: "none",
      fontSize: "24px",
      borderRadius:"10px",
      fontWeight: "bold",
      color: "#000",
      cursor: "pointer",
      zIndex: 1100, // Ensure the button stays on top of the iframe
    },
    iframe: {
      position: "fixed",
      top: 0,
      left: 0,
      width: "100%",
      height: "100%",
      overflow: "hidden",
      border: "none",
    },
    counter:{
      position: "absolute",
      top: "5px",
      left: "40%",
      backgroundColor: "transparent",
      border: "none",
      fontSize: "20px",
      fontWeight: "bold",
      color: "blue",
      cursor: "pointer",
      zIndex: 1100,
    }
  };

  export {styles};