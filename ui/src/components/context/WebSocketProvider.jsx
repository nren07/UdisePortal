import React, { createContext, useContext, useEffect, useRef, useState } from 'react';
import { Client } from '@stomp/stompjs';
import { useSelector } from 'react-redux'; // Import useSelector to access Redux state
import { selectUserId } from "../../store/useSelectors"; // Your custom selector

// Create a SocketContext to share WebSocket state
const SocketContext = createContext(null);

// Custom hook to access the WebSocket context
export const useSocket = () => useContext(SocketContext);

// WebSocket Provider component
export const WebSocketProvider = ({ children }) => {
  const stompClientRef = useRef(null); // Ref to store the STOMP client instance
  const [isConnected, setIsConnected] = useState(false); // Track connection status
  const [messages, setMessages] = useState(""); // Store messages
  const userId = useSelector(selectUserId); // Get userId from Redux

  useEffect(() => {
    if (!userId) return; // Ensure userId is available before connecting

    const client = new Client({
      brokerURL: 'ws://13.233.228.245/ws', // Backend WebSocket URL
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,

      onConnect: () => {
        console.log('Connected to WebSocket');
        setIsConnected(true);

        // Subscribe to the user-specific topic after connecting
        const subscriptionPath = `/topic/${userId}`;
        console.log(`Subscribed to: ${subscriptionPath}`);

        client.subscribe(subscriptionPath, (msg) => {
          const { body } = msg;
          const data=JSON.parse(body);
          console.log(data);
          setMessages(data.eventType);
        });
      },

      onDisconnect: () => {
        console.log('Disconnected from WebSocket');
        setIsConnected(false);
      },

      onStompError: (error) => {
        console.error('STOMP Error:', error);
        setIsConnected(false);
      },

      onWebSocketError: (error) => {
        console.error('WebSocket Error:', error);
        setIsConnected(false);
      },
    });

    client.activate(); // Activate the WebSocket connection
    stompClientRef.current = client; // Store the client in the ref

    // Cleanup on component unmount
    return () => {
      if (stompClientRef.current) stompClientRef.current.deactivate();
    };
  }, [userId]); // Re-run effect if userId changes

  return (
    <SocketContext.Provider value={{ isConnected, messages }}>
      {children}
    </SocketContext.Provider>
  );
};
