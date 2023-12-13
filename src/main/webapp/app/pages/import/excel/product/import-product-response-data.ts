export interface ImportProductResponseData {
  name?: string | null;
  barCode?: string | null;
  code2?: string | null;
  groupName?: string | null;
  inPrice?: number | null;
  outPrice?: number | null;
  unit?: string | null;
  unitId?: number | null;
  inventoryTracking?: boolean | null;
  inventoryCount?: string | null;
  vatRate?: number | null;
  description?: string | null;
  messageErrorMap?: Map<number, string>;
  messageResponse?: string[] | null;
  status?: boolean;
}
