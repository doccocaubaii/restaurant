export class FilterRoles {
  comId?: number;
  code?: string;
  name?: string;
  size?: any;
  page?: any;
  keyword?: any;
}

export class FilterPermission {
  comId?: number;
  code?: string;
  name?: string;
  size?: any;
  page?: any;
  keyword?: any;
}

export enum Page {
  FIRST_PAGE = 0,
  PAGE_SIZE = 20,
  PAGE_NUMBER = 1,
  TOTAL_ITEM = 0,
}

export interface ITreeViewItem {
  id: any;
  text: string;
  value: any;
  disabled: boolean;
  checked: boolean;
  collapsed: boolean;
  children: ITreeViewItem[];
  parentId?: any;
}

export class TreeViewItem implements ITreeViewItem {
  constructor(
    public id: any,
    public text: string,
    public value: any,
    public disabled: boolean,
    public checked: boolean,
    public collapsed: boolean,
    public children: ITreeViewItem[],
    public parentId?: any
  ) {}
}
