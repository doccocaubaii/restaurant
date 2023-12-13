import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VatRateDiscountOrderComponent } from './vat-rate-discount-order.component';

describe('VatRateDiscountOrderComponent', () => {
  let component: VatRateDiscountOrderComponent;
  let fixture: ComponentFixture<VatRateDiscountOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VatRateDiscountOrderComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(VatRateDiscountOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
