// import React from "react";
// import {
//   Card,
//   CardHeader,
//   CardContent,
//   List,
//   ListItem,
//   Typography,
//   IconButton,
//   Box,
// } from "@mui/material";
// import CallIcon from "@mui/icons-material/Call";

// const Lead = () => {
//   const leads = [
//     { name: "Vikas Sharma", time: "4:01 PM", date: "26", phone: "+9711818218" },
//     { name: "Akash", time: "1:49 PM", date: "02", phone: "+9833746100" },
//     {
//       name: "Ankush Sharma",
//       time: "4:01 PM",
//       date: "26",
//       phone: "+8178777920",
//     },
//     {
//       name: "Sudhir Saidas",
//       time: "4:01 PM",
//       date: "26",
//       phone: "+9917186727",
//     },
//     { name: "Raunak", time: "4:01 PM", date: "26", phone: "+9560440546" },
//   ];

//   return (
//     <Card sx={{ border: "none" }}>
     
//       <CardContent sx={{ padding: "1rem 2rem" }}>
//         <List>
//           {leads.map((lead, index) => (
//             <ListItem
//               key={index}
//               sx={{
//                 padding: 0,
//                 display: "flex",
//                 alignItems: "center",
//                 marginBottom: "1rem", 
//               }}
//             >
//               <Box
//                 sx={{
//                   width: "8rem",
//                   height: "5rem",
//                   bgcolor: "lightgrey",
//                   marginRight: "1rem",
//                   display: "flex",
//                   flexDirection: "column",
//                   alignItems: "center",
//                   justifyContent: "center",
//                 }}
//               >
//                 <Typography variant="h5">{lead.date}</Typography>
//                 <Typography color="textSecondary">{lead.time}</Typography>
//               </Box>
//               <div>
//                 <Typography
//                   variant="body2"
//                   color="textSecondary"
//                   sx={{
//                     fontSize: "1.25rem",
//                     display: "flex",
//                     alignItems: "center",
//                   }} // Increased font size
//                 >
//                   {lead.name}
//                   <IconButton
//                     href={`tel:${lead.phone}`}
//                     sx={{ marginLeft: "1rem" }} 
//                   >
//                     <CallIcon />
//                   </IconButton>
//                   <Typography
//                     component="span"
//                     sx={{ marginLeft: "0.5rem" }} 
//                   >
//                     {lead.phone}
//                   </Typography>
//                 </Typography>
//               </div>
//             </ListItem>
//           ))}
//         </List>
//       </CardContent>
//     </Card>
//   );
// };

// export default Lead;
