import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiscountTaxOrderComponent } from './discount-tax-order.component';

describe('DiscountTaxOrderComponent', () => {
  let component: DiscountTaxOrderComponent;
  let fixture: ComponentFixture<DiscountTaxOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DiscountTaxOrderComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DiscountTaxOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
