import dayjs from 'dayjs/esm';

export class FilterOrder {
  page!: number;
  size!: number;
  sort?: string;
  totalItem?: number;
  status?: number;
  fromDate?: string;
  toDate?: string;
  keyword?: string;
  isCountAll?: boolean;
}
