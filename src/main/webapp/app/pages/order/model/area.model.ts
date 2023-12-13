import { AreaUnit } from './area-unit.model';

export interface Area {
  id: number;
  comId: number;
  name: string;
  units: AreaUnit[];
}
