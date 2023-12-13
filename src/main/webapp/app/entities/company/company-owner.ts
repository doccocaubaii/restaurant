import { Company } from './company';

export interface CompanyOwner {
  id: any;
  name: any;
  taxCode: any;
  ownerName: any;
  ownerId: any;
  companies: Company[];
  label: string;
}
