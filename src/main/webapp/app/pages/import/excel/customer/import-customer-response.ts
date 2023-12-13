import { ImportCustomerResponseData } from './import-customer-response-data';

export interface ImportCustomerResponse {
  data?: ImportCustomerResponseData[];
  countValid?: number | null;
  countInvalid?: number | null;
}
