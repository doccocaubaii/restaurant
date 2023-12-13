import { TestBed } from '@angular/core/testing';

import { ReportInvoiceService } from './report-invoice.service';

describe('ReportInvoiceService', () => {
  let service: ReportInvoiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportInvoiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
