import { MaterialGoodsViewDto } from 'app/shared/model/dto/material-goods-view-dto.model';
import { SaPolicySalePriceGroupDto } from 'app/shared/model/dto/sa-policy-sale-price-group-dto.model';
import { AccountingObjectViewDto } from 'app/shared/model/dto/accounting-object-view-dto.model';
import { SaPolicyDiscountDto } from 'app/shared/model/dto/sa-policy-discount-dto.model';

export interface ISAPolicyPriceSetting {
  id?: string;
  companyID?: string;
  sAPolicyPriceName?: string;
  isActive?: boolean;
  description?: string;
  currencyID?: string;
  unitID?: number;
  price?: any;
  materialGoodsID?: any;
  lstMaterialGoodsViewDto?: MaterialGoodsViewDto[];
  lstSaPolicySalePriceGroupDto?: SaPolicySalePriceGroupDto[];
  lstMaterialGoodsViewPriceTableDto?: MaterialGoodsViewDto[];
  lstAccountingObjectViewDTOPage?: AccountingObjectViewDto[];
  lstMaterialGoodsViewPriceTableDtoPage?: MaterialGoodsViewDto[];
  lstSaPolicyDiscountDto?: SaPolicyDiscountDto[];
}

export class SAPolicyPriceSetting implements ISAPolicyPriceSetting {
  constructor(
    public id?: string,
    public companyID?: string,
    public sAPolicyPriceName?: string,
    public isActive?: boolean,
    public description?: string,
    public currencyID?: string,
    public unitID?: number,
    public price?: any,
    public materialGoodsID?: any,
    public lstMaterialGoodsViewDto?: MaterialGoodsViewDto[],
    public lstSaPolicySalePriceGroupDto?: SaPolicySalePriceGroupDto[],
    public lstMaterialGoodsViewPriceTableDto?: MaterialGoodsViewDto[],
    public lstSaPolicyDiscountDto?: SaPolicyDiscountDto[]
  ) {
    this.isActive = this.isActive || false;
  }
}
