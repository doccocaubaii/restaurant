import { Moment } from 'moment';
import { EMContractDetails } from 'app/shared/model/em-contract-details.model';
import { Currency } from 'app/shared/model/currency.model';
import { AccountingObject } from 'app/shared/model/accounting-object.model';

export interface IEMContract {
  id?: string;
  companyID?: string;
  branchID?: string;
  date?: Moment | any;
  typeID?: number;
  typeLedger?: number;
  no?: string;
  noFBook?: string;
  noMBook?: string;
  name?: string;
  description?: string;
  signedDate?: Moment | any;
  currencyID?: string;
  exchangeRate?: number;
  amount?: number;
  amountOriginal?: number;
  totalVATAmount?: number;
  totalVATAmountOriginal?: number;
  totalDiscountAmount?: number;
  totalDiscountAmountOriginal?: number;
  accountingObjectID?: string;
  accountingObjectName?: string;
  signName?: string;
  startedDate?: Moment | any;
  closedDate?: Moment | any;
  contractState?: number;
  isWatchForCostPrice?: boolean;
  billReceived?: boolean;
  isActive?: boolean;
  checked?: boolean;
  ppOrderDetails?: EMContractDetails[];
  accountingObjectId?: string;
  isProject?: boolean;
  quantity?: number;
  quantityDelivery?: number;
  unitPrice?: number;
  discountAmount?: number;
  discountAmountOriginal?: number;
  totalAmountSum?: number;
  checkType?: boolean;
  totalOriginal?: number;
  total?: number;
}

export class EMContract implements IEMContract {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public typeLedger?: number,
    public noFBook?: string,
    public noMBook?: string,
    public name?: string,
    public no?: string,
    public description?: string,
    public signedDate?: Moment | any,
    public currencyID?: string,
    public exchangeRate?: number,
    public amount?: number,
    public totalAmount?: number,
    public amountOriginal?: number,
    public totalAmountOriginal?: number,
    public totalVATAmount?: number,
    public totalVATAmountOriginal?: number,
    public accountingObjectID?: string,
    public accountingObjectName?: string,
    public signName?: string,
    public startedDate?: Moment | any,
    public closedDate?: Moment | any,
    public contractState?: number,
    public isWatchForCostPrice?: boolean,
    public billReceived?: boolean,
    public isActive?: boolean,
    public checked?: boolean,
    public totalDiscountAmount?: number,
    public totalOriginal?: number,
    public total?: number,
    public totalDiscountAmountOriginal?: number,
    public emContractDetail?: EMContractDetails[],
    public currency?: Currency,
    public accountingObjectId?: string,
    public reason?: string,
    public quantity?: number,
    public quantityDelivery?: number,
    public unitPrice?: number,
    public discountAmount?: number,
    public discountAmountOriginal?: number,
    public totalAmountSum?: number,
    public isProject?: boolean,
    public checkType?: boolean
  ) {
    this.isWatchForCostPrice = this.isWatchForCostPrice || false;
    this.billReceived = this.billReceived || false;
    this.isActive = this.isActive || false;
  }
}
