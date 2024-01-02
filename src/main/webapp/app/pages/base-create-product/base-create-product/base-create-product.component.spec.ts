import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BaseCreateProductComponent } from './base-create-product.component';

describe('BaseCreateProductComponent', () => {
  let component: BaseCreateProductComponent;
  let fixture: ComponentFixture<BaseCreateProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BaseCreateProductComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(BaseCreateProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
