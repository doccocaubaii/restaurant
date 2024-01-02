import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiscountTaxProductComponent } from './discount-tax-product.component';

describe('DiscountTaxProductComponent', () => {
  let component: DiscountTaxProductComponent;
  let fixture: ComponentFixture<DiscountTaxProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DiscountTaxProductComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DiscountTaxProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
