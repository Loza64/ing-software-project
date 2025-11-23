import React, { createContext, useContext, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { notificationService } from "../../services/notificationService";

interface NotificationContextType {
  notifySuccess: (message: string) => void;
  notifyError: (message: string) => void;
}

const NotificationContext = createContext<NotificationContextType | undefined>(undefined);

export const NotificationProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  
  const notifySuccess = (message: string) => {
    toast.success(message);
  };

  const notifyError = (message: string) => {
    toast.error(message);
  };

  // ⭐ REGISTRA LAS FUNCIONES EN EL SERVICIO GLOBAL
  useEffect(() => {
    notificationService.register(notifySuccess, notifyError);
  }, []);

  return (
    <NotificationContext.Provider value={{ notifySuccess, notifyError }}>
      {children}
      <ToastContainer />
    </NotificationContext.Provider>
  );
};

export const useNotification = () => {
  const context = useContext(NotificationContext);
  if (!context) {
    throw new Error("useNotification debe usarse dentro de NotificationProvider");
  }
  return context;
};
