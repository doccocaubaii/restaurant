import { AccountingObjectViewDto } from 'app/shared/model/dto/accounting-object-view-dto.model';

export class SaPolicySalePriceGroupDto {
  id: string;
  sAPolicyPriceSettingID: string;
  salePriceGroupID: string;
  salePriceGroupCode: string;
  salePriceGroupName: string;
  basedOn: number;
  method: number;
  amountAdjust: number;
  price: number;
  description: string;
  checked: any;
  lstAccountingObjectViewDto: AccountingObjectViewDto[];
  checkedAllTab5: boolean;
}
