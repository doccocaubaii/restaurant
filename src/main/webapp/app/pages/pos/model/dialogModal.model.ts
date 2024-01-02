export interface IDialogModal {
  title?: string;
  message?: string;
  btnText?: string;
  icon?: any;
  classBtn?: string;
}

export class DialogModal implements IDialogModal {
  constructor(public title?: string, public message?: string, public btnText?: string, public icon?: any, public classBtn?: string) {}
}
