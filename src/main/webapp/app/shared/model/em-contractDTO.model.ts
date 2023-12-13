import { Moment } from 'moment';

export interface IEMContractDTO {
  id?: string;
  emContractCode?: string;
  emContractName?: string;
  checked?: boolean;
}
export class EMContractDTO implements IEMContractDTO {
  constructor(public id?: string, public emContractCode?: string, public emContractName?: string, public checked?: boolean) {
    this.checked = this.checked || false;
  }
}
