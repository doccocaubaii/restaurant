import { OpAccountModel } from 'app/shared/model/op-account.model';
import { OpMaterialGoodsModel } from 'app/shared/model/op-material-goods.model';

export interface IVoucherList {
  typeId?: any;
  orderPriority?: number;
  id?: string;
  companyID?: string;
  branchID?: string;
  accountingType?: number;

  voucherName?: string;
  debitAccount?: string;
  creditAccount?: string;

  accountNameGlobal?: string;
  description?: string;
  parentAccountID?: string;
  isParentNode?: boolean;
  parentNode?: boolean;
  accountGroupKind?: number;
  getAccountGroupKind?: string;
  detailType?: string;
  active?: boolean;
  isActive?: boolean;
  detailByAccountObject?: number;
  isForeignCurrency?: boolean;
  accountGroupID?: string;
  isCheck?: boolean;
  debitAmountOriginal?: number;
  amountOriginal?: number;
  creditAmountOriginal?: number;
  isAccountList?: boolean;
  children?: IVoucherList[];
  opAccountDTOList?: OpAccountModel[];
  opMaterialGoodsDTOs?: OpMaterialGoodsModel[];
  count?: number;
  checked?: boolean;
  closingDebitAmount?: number;
  closingCreditAmount?: number;
  closingDebitAmountString?: string;
  closingCreditAmountString?: string;
}

export class VoucherList implements IVoucherList {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public accountingType?: number,
    public voucherName?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public accountNameGlobal?: string,
    public description?: string,
    public isParentNode?: boolean,
    public parentNode?: boolean,
    public accountGroupKind?: number,
    public getAccountGroupKind?: string,
    public detailType?: string,
    public active?: boolean,
    public isActive?: boolean,
    public detailByAccountObject?: number,
    public isForeignCurrency?: boolean,
    public parentAccountID?: string,
    public accountGroupID?: string,
    public isCheck?: boolean,
    public debitBalance?: number,
    public creditBalance?: number,
    public count?: number,
    public isAccountList?: boolean,
    public children?: IVoucherList[],
    public checked?: boolean,
    public closingDebitAmount?: number,
    public closingCreditAmount?: number,
    public closingDebitAmountString?: string,
    public closingCreditAmountString?: string
  ) {
    this.isParentNode = this.isParentNode || false;
    this.isActive = this.isActive || false;
    this.isForeignCurrency = this.isForeignCurrency || false;
    this.isCheck = this.isCheck || false;
  }
}
