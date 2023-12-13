import { Moment } from 'moment';

export interface ITIAllocation {
  tiAllocationAllocateds?: any[];
  tiAllocationDetails?: any[];
  tiAllocationPosts?: any[];
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  date?: Moment | any;
  postedDate?: Moment | any;
  typeLedger?: number;
  noFBook?: string;
  nofBook?: string;
  noMBook?: string;
  reason?: string;
  month?: number;
  year?: number;
  totalAmount?: number;
  recorded?: boolean;
  templateID?: string;
  refVouchers?: any[];
  viewVouchers?: any[];
  total?: any;
}

export class TIAllocation implements ITIAllocation {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public date?: Moment | any,
    public postedDate?: Moment | any,
    public typeLedger?: number,
    public noFBook?: string,
    public nofBook?: string,
    public noMBook?: string,
    public reason?: string,
    public month?: number,
    public year?: number,
    public totalAmount?: number,
    public recorded?: boolean,
    public templateID?: string,
    public tiAllocationAllocateds?: any[],
    public tiAllocationDetails?: any[],
    public tiAllocationPosts?: any[],
    public refVouchers?: any[],
    public viewVouchers?: any[],
    public total?: any
  ) {
    this.recorded = this.recorded || false;
  }
}
