import { SaPolicySalePriceGroupDto } from 'app/shared/model/dto/sa-policy-sale-price-group-dto.model';
import { SaPolicyDiscountDto } from 'app/shared/model/dto/sa-policy-discount-dto.model';
import { AmountDiscountViewDto } from 'app/shared/model/dto/amount-discount-view-dto.model';

export class MaterialGoodsViewDto {
  id: string;
  materialGoodsCode: string;
  materialGoodsName: string;
  materialGoodsCategoryID: string;
  materialGoodsCategoryCode: string;
  materialGoodsCategoryName: string;
  fixedSalePrice: number;
  salePrice1: number;
  salePrice2: number;
  salePrice3: number;
  price: number;
  sAPolicyPriceSettingID: string;
  salePriceGroupID: string;
  checkedAll: boolean;
  checked: any;
  lstSaPolicySalePriceGroupDtoSelected: SaPolicySalePriceGroupDto[];
  saPolicyDiscountDtoSelected: SaPolicyDiscountDto[];
  amountDiscountViewDtos: AmountDiscountViewDto[];
}
