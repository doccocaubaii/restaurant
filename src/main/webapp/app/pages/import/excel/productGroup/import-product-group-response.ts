import { ImportProductGroupResponseData } from './import-product-group-response-data';

export interface ImportProductGroupResponse {
  data?: ImportProductGroupResponseData[];
  countValid?: number | null;
  countInvalid?: number | null;
}
