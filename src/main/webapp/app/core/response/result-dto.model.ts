import { IMessageModel } from './message.model';

export interface IResultDTO {
  message: IMessageModel[];
  reason?: string | null;
  status?: string | null;
  data?: object | null;
  count?: number;
}
