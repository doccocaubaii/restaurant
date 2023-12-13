export interface IFRTemplate {
  itemID?: any;
  reportID?: string;
  itemCode?: string;
  itemName?: string;
  itemNameEnglish?: String;
  itemIndex?: number;
  description?: string;
  formulaType?: number;
  formulaFrontEnd?: string;
  formula?: string;
  hidden?: boolean;
  isBold?: boolean;
  isItalic?: boolean;
  accountingSystem?: number;
  createdDate?: any;
  createdBy?: any;
  modifiedDate?: any;
  modifiedBy?: any;
  category?: number;
  companyID?: any;
  defaultItemID?: any;
  isEdit?: boolean;
  oldIndex?: any;
  page?: any;
  isNew?: boolean;
  isDelete?: boolean;
  sttWork?: number;
  isUp?: boolean;
  isSwap?: boolean;
  part?: any;
  partInTab?: any;
  openingBalance?: any;
  buildingAndRealty?: any;
  equipment_IssueRight_TotalPayAndCapital?: any;
  transport_CopyRight_PayInterestAndDecrease?: any;
  equipment_Trademark_PayDebitThisYearAndOtherCapital?: any;
  otherFA_Software_TotalPayLastYearAndTreasuryShare?: any;
  intFA_OrtherIntFA_PayDebitLastYearAndDiffExchangeRate?: any;
  license_PayInterestLastYearAndRevalueFA?: any;
  closingBalance?: any;
  total?: any;
  edit?: boolean;
}

export class FRTemplate implements IFRTemplate {
  constructor(
    public itemID?: any,
    public reportID?: string,
    public itemCode?: string,
    public itemName?: string,
    public itemNameEnglish?: string,
    public itemIndex?: number,
    public description?: string,
    public formulaType?: number,
    public formulaFrontEnd?: string,
    public formula?: string,
    public hidden?: boolean,
    public isBold?: boolean,
    public isItalic?: boolean,
    public accountingSystem?: number,
    public createdDate?: any,
    public createdBy?: any,
    public modifiedDate?: any,
    public modifiedBy?: any,
    public category?: number,
    public companyID?: any,
    public defaultItemID?: any,
    public isEdit?: boolean,
    public oldIndex?: any,
    public page?: any,
    public isNew?: boolean,
    public isDelete?: boolean,
    public sttWork?: number,
    public isUp?: boolean,
    public isSwap?: boolean,
    public part?: any,
    public partInTab?: any,
    public openingBalance?: any,
    public buildingAndRealty?: any,
    public equipment_IssueRight_TotalPayAndCapital?: any,
    public transport_CopyRight_PayInterestAndDecrease?: any,
    public equipment_Trademark_PayDebitThisYearAndOtherCapital?: any,
    public otherFA_Software_TotalPayLastYearAndTreasuryShare?: any,
    public intFA_OrtherIntFA_PayDebitLastYearAndDiffExchangeRate?: any,
    public license_PayInterestLastYearAndRevalueFA?: any,
    public closingBalance?: any,
    public total?: any,
    public edit?: boolean
  ) {
    this.hidden = this.hidden || false;
    this.isBold = this.isBold || false;
    this.isItalic = this.isItalic || false;
    this.isEdit = this.isEdit || false;
    this.isDelete = this.isDelete || false;
    this.isUp = this.isUp || false;
    this.isSwap = this.isSwap || false;
    this.edit = this.edit || false;
  }
}
