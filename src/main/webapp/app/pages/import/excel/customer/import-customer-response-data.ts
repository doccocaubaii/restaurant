export interface ImportCustomerResponseData {
  comId?: number | null;
  name?: string | null;
  code2?: string | null;
  address?: string | null;
  phoneNumber?: string | null;
  email?: string | null;
  taxCode?: string | null;
  idNumber?: number | null;
  type?: number | null;
  city?: string | null;
  district?: string | null;
  description?: string | null;
  messageErrorMap?: Map<number, string>;
  messageResponse?: string[] | null;
  status?: boolean;
}
