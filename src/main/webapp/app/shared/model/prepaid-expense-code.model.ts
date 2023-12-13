import { Moment } from 'moment';
import { IPrepaidExpenseCode } from 'app/shared/model/prepaid-expense-code.model';

export interface IPrepaidExpenseCode {
  id?: any;
  code?: string;
  name?: string;
  type?: number;
  species?: string;
}

// export class PrepaidExpenseCode implements IPrepaidExpenseCode {
//     constructor(
//         public id?: any,
//         public code?: string,
//         public name?: string,
//         public type?: number,
//         public species?: string
//     ) {
//
//     }
// }
