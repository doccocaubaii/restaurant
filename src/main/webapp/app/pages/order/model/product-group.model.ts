export interface IProductGroup {
  id: number;
  comId: number;
  name: string;
  description?: string;
  status: boolean;
}

export type NewIProductGroup = Omit<IProductGroup, 'id'> & { id: null };
