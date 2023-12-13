import { Moment } from 'moment';

export interface ITM01GTGTAdjust {
  id?: number;
  tM01GTGTID?: number;
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
  oderPriority?: number;
  type?: number;
}

export class TM01GTGTAdjust implements ITM01GTGTAdjust {
  constructor(
    public id?: number,
    public tM01GTGTID?: number,
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
    public oderPriority?: number,
    public type?: number
  ) {}
}
