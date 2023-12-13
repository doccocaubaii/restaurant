import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductToppingComponent } from './product-topping.component';

describe('ProductToppingComponent', () => {
  let component: ProductToppingComponent;
  let fixture: ComponentFixture<ProductToppingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProductToppingComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductToppingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
