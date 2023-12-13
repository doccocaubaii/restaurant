import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrintInvoiceComponent } from './print-invoice.component';

describe('PosInvoiceComponent', () => {
  let component: PrintInvoiceComponent;
  let fixture: ComponentFixture<PrintInvoiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PrintInvoiceComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PrintInvoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
