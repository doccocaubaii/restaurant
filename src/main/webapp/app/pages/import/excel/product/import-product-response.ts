import { ImportProductResponseData } from './import-product-response-data';

export interface ImportProductResponse {
  data?: ImportProductResponseData[];
  countValid?: number | null;
  countInvalid?: number | null;
}
