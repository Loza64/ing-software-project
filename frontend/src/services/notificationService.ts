let notifySuccess: (msg: string) => void = () => {};
let notifyError: (msg: string) => void = () => {};

export const notificationService = {
  notifySuccess: (msg: string) => notifySuccess(msg),
  notifyError: (msg: string) => notifyError(msg),

  register: (
    successFn: (msg: string) => void,
    errorFn: (msg: string) => void
  ) => {
    notifySuccess = successFn;
    notifyError = errorFn;
  }
};
