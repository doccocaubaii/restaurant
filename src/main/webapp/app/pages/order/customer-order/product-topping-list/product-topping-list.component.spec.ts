import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductToppingListComponent } from './product-topping-list.component';

describe('ProductToppingListComponent', () => {
  let component: ProductToppingListComponent;
  let fixture: ComponentFixture<ProductToppingListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProductToppingListComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductToppingListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
