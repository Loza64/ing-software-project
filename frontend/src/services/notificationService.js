let notifySuccess = () => { };
let notifyError = () => { };
export const notificationService = {
    notifySuccess: (msg) => notifySuccess(msg),
    notifyError: (msg) => notifyError(msg),
    register: (successFn, errorFn) => {
        notifySuccess = successFn;
        notifyError = errorFn;
    }
};
