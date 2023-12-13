import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BaseFormProductComponent } from './base-form-product.component';

describe('BaseFormProductComponent', () => {
  let component: BaseFormProductComponent;
  let fixture: ComponentFixture<BaseFormProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BaseFormProductComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(BaseFormProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
