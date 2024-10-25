import axios from "axios";
import { createContext, useContext, useState } from "react";

const myContextApi = createContext();

export const GlobalState = ({ children }) => {
  const [userToken, setUserToken] = useState(null);

  const login = async (email, password) => {
    try {
      const payload = {
        email,
        password,
      };
      const response = await axios.post(
        "https://lmsapi.propdoors.com:3000/user/login",
        payload,
        {
          headers: {
            "Content-Type": "applocation/json",
          },
        }
      );

      const data = response.data;
      console.log(data);
    } catch (error) {}
  };

  return <myContextApi.Provider value={{login}}>{children}</myContextApi.Provider>;
};


export const globalState = () => {

  return useContext(myContextApi)
}