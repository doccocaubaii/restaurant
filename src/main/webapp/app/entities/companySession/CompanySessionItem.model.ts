export class CompanySessionItem {
  id: number | null;
  name?: string | null;

  constructor(id: number, name: string | null) {
    this.id = id;
    this.name = name;
  }
}
