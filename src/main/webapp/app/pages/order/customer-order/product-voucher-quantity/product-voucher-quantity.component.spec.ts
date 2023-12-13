import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductVoucherQuantityComponent } from './product-voucher-quantity.component';

describe('ProductVoucherQuantityComponent', () => {
  let component: ProductVoucherQuantityComponent;
  let fixture: ComponentFixture<ProductVoucherQuantityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProductVoucherQuantityComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductVoucherQuantityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
