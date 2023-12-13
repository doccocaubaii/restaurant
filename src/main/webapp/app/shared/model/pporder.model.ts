import { Moment } from 'moment';
import { IPporderdetail, PPOrderDetail } from 'app/shared/model/pporderdetail.model';
import { AccountingObject } from 'app/shared/model/accounting-object.model';
import { Currency } from 'app/shared/model/currency.model';
import { IMaterialGoodsInStock, MaterialGoods } from 'app/shared/model/material-goods.model';

export interface IPporder {
  id?: string;
  companyId?: string;
  branchId?: string;
  typeId?: any;
  date?: Moment | any;
  no?: string;
  accountingObjectId?: string;
  accountingObjectName?: string;
  accountingObjectAddress?: string;
  companyTaxCode?: string;
  reason?: string;
  currencyId?: string;
  exchangeRate?: number;
  deliverDate?: any;
  shippingPlace?: string;
  transportMethodId?: string;
  employeeId?: string;
  totalAmount?: number;
  totalAmountOriginal?: number;
  totalVATAmount?: number;
  totalVATAmountOriginal?: number;
  totalDiscountAmount?: number;
  totalDiscountAmountOriginal?: number;
  templateId?: string;
  contactName?: string;
  status?: number;
  ppOrderDetails?: IPporderdetail[];
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
}

export class Pporder implements IPporder {
  constructor(
    public id?: string,
    public companyId?: string,
    public branchId?: string,
    public typeId?: any,
    public typeLedger?: number,
    public date?: any,
    public no?: string,
    public noMBook?: string,
    public accountingObjectId?: string,
    public accountingObjectName?: string,
    public accountingObjectAddress?: string,
    public companyTaxCode?: string,
    public reason?: string,
    public currencyId?: string,
    public exchangeRate?: number,
    public deliverDate?: any,
    public shippingPlace?: string,
    public transportMethodId?: string,
    public employeeId?: string,
    public contactName?: string,
    public totalAmount?: number,
    public totalAmountOriginal?: number,
    public totalVATAmount?: number,
    public totalVATAmountOriginal?: number,
    public total?: number,
    public accountingObject?: AccountingObject,
    public accountingObjectEmployee?: AccountingObject,
    public currency?: Currency,
    public totalOriginal?: number,
    public totalDiscountAmount?: number,
    public totalDiscountAmountOriginal?: number,
    public templateId?: string,
    public ppOrderDetails?: PPOrderDetail[],
    public status?: number,
    public materialGoodsInStock?: MaterialGoods,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string
  ) {}
}
