import React, { useEffect, useRef } from 'react';

// Make sure to include noVNC in your project
// You can install it via npm or include it in your public/index.html file
// npm install novnc

const VncViewer = () => {
    const vncCanvasRef = useRef(null);
    const rfbRef = useRef(null);

    const port = 5900; // Change to the port your VNC server uses
    const backendServer = "http://your-backend-server"; // Replace with your backend server URL

    useEffect(() => {
        // Load the noVNC script dynamically
        const script = document.createElement('script');
        script.src = "https://cdnjs.cloudflare.com/ajax/libs/noVNC/1.3.0/core/rfb.js";
        script.async = true;
        script.onload = () => {
            // Initialize RFB once the script is loaded
            rfbRef.current = new RFB(vncCanvasRef.current, `${backendServer}/vnc?port=${port}`, {
                credentials: { password: 'secret' } // Use if necessary
            });

            rfbRef.current.addEventListener("connect", () => {
                console.log("Connected to VNC");
            });
            rfbRef.current.addEventListener("disconnect", () => {
                console.log("Disconnected from VNC");
            });
        };
        document.body.appendChild(script);

        // Cleanup function to disconnect and remove event listeners
        return () => {
            if (rfbRef.current) {
                rfbRef.current.disconnect();
                rfbRef.current = null;
            }
        };
    }, [backendServer, port]);

    return (
        <div style={{ width: '100%', height: '100%' }}>
            <canvas ref={vncCanvasRef} style={{ width: '100%', height: '100%' }} />
        </div>
    );
};

export default VncViewer;
