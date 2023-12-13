export class FilterCompany {
  size?: any;
  page?: any;
  keyword?: any;
}

export enum Page {
  FIRST_PAGE = 0,
  PAGE_SIZE = 20,
  PAGE_NUMBER = 1,
  TOTAL_ITEM = 0,
  ONE_PAGE = 1000,
}

export class FilterRole {
  size?: any;
  page?: any;
  keyword?: any;
}
