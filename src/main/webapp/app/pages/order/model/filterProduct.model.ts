export class FilterProduct {
  page: number | undefined;
  size: number | undefined;
  sort?: string;
  groupIds: number[];
  groupId?: number;
  keyword?: string;
}
