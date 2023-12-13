import { CardPolicyApplyItem } from './card-policy-apply-item';
import dayjs from 'dayjs';

export interface CardPolicy {
  id: any;
  comId: any;
  upgradeType: any;
  fromDate?: string;
  conditions: CardPolicyApplyItem[];
  editTable: boolean;
}
