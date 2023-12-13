import { Moment } from 'moment';

export interface ITM01TAINAdjust {
  id?: number;
  tM01TAINID?: number;
  code?: string;
  name?: string;
  declaredAmount?: number;
  adjustAmount?: number;
  differAmount?: number;
  lateDays?: number;
  lateAmount?: number;
  explainAmount?: number;
  commandNo?: string;
  commandDate?: Moment | any;
  taxCompanyName?: string;
  taxCompanyDecisionName?: string;
  receiveDays?: number;
  explainLateAmount?: number;
  differReason?: string;
  orderPriority?: number;
}

export class TM01TAINAdjust implements ITM01TAINAdjust {
  constructor(
    public id?: number,
    public tM01TAINID?: number,
    public code?: string,
    public name?: string,
    public declaredAmount?: number,
    public adjustAmount?: number,
    public differAmount?: number,
    public lateDays?: number,
    public lateAmount?: number,
    public explainAmount?: number,
    public commandNo?: string,
    public commandDate?: Moment | any,
    public taxCompanyName?: string,
    public taxCompanyDecisionName?: string,
    public receiveDays?: number,
    public explainLateAmount?: number,
    public differReason?: string,
    public orderPriority?: number
  ) {}
}
