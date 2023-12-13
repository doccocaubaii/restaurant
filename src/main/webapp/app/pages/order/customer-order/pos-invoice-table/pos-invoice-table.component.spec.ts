import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PosInvoiceTableComponent } from './pos-invoice-table.component';

describe('PosInvoiceComponent', () => {
  let component: PosInvoiceTableComponent;
  let fixture: ComponentFixture<PosInvoiceTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PosInvoiceTableComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PosInvoiceTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
