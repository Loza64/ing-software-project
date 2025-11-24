import { jsx as _jsx, jsxs as _jsxs } from "react/jsx-runtime";
import { createContext, useContext, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { notificationService } from "../../services/notificationService";
const NotificationContext = createContext(undefined);
export const NotificationProvider = ({ children }) => {
    const notifySuccess = (message) => {
        toast.success(message);
    };
    const notifyError = (message) => {
        toast.error(message);
    };
    // ⭐ REGISTRA LAS FUNCIONES EN EL SERVICIO GLOBAL
    useEffect(() => {
        notificationService.register(notifySuccess, notifyError);
    }, []);
    return (_jsxs(NotificationContext.Provider, { value: { notifySuccess, notifyError }, children: [children, _jsx(ToastContainer, {})] }));
};
export const useNotification = () => {
    const context = useContext(NotificationContext);
    if (!context) {
        throw new Error("useNotification debe usarse dentro de NotificationProvider");
    }
    return context;
};
