export interface ImportProductGroupResponseData {
  name?: string | null;
  description?: string | null;
  messageErrorMap?: Map<number, string>;
  messageResponse?: string[] | null;
  status?: boolean;
}
