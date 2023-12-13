export interface IDialogModal {
  title?: string;
  message?: string;
  btnText?: string;
  icon?: any;
  classBtn?: string;
  isHiddenCancel?: boolean;
}

export class DialogModal implements IDialogModal {
  constructor(
    public title?: string,
    public message?: string,
    public btnText?: string,
    public icon?: any,
    public classBtn?: string,
    public isHiddenCancel?: boolean
  ) {}
}
