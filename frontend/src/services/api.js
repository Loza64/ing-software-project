import axios from 'axios';
import endpoints from '../utils/endpoints';
import { notificationService } from './notificationService';
const isTokenValid = () => {
    const token = localStorage.getItem('token');
    if (!token) {
        console.warn('⚠️ No token found');
        return false;
    }
    try {
        const payloadBase64 = token.split('.')[1];
        const decodedPayload = JSON.parse(atob(payloadBase64));
        const expirationTime = decodedPayload.exp * 1000;
        const currentTime = Date.now();
        return currentTime < expirationTime;
    }
    catch (error) {
        console.error('❌ Error parsing token:', error);
        return false;
    }
};
const api = axios.create({
    baseURL: endpoints.baseURL,
    headers: { 'Content-Type': 'application/json' },
    timeout: 10000,
});
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    const tokenValid = isTokenValid();
    if (token && tokenValid) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    else {
        if (token && !tokenValid) {
            notificationService.notifyError("Tu sesión expiró. Por favor inicia sesión nuevamente.");
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = '/login';
        }
    }
    return config;
});
api.interceptors.response.use((response) => {
    return response;
}, (error) => {
    if (!error.response) {
        notificationService.notifyError("No hay conexión con el servidor ❌");
        return Promise.reject(error);
    }
    const status = error.response.status;
    if (status === 401) {
        notificationService.notifyError("Tu sesión ha expirado 🔐");
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        setTimeout(() => {
            window.location.href = '/login';
        }, 800);
    }
    if (status === 403) {
        notificationService.notifyError("No tienes permisos para realizar esta acción 🚫");
    }
    if (status === 404) {
        notificationService.notifyError("Recurso no encontrado 🔍");
    }
    if (status >= 500) {
        notificationService.notifyError("Error interno del servidor 💥");
    }
    return Promise.reject(error);
});
export default api;
