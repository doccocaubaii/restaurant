import { TestBed } from '@angular/core/testing';

import { AreaUnitService } from './area-unit.service';

describe('AreaUnitService', () => {
  let service: AreaUnitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AreaUnitService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
