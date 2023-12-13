import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SetVatRateProductComponent } from './set-vat-rate-product.component';

describe('SetVatRateProductComponent', () => {
  let component: SetVatRateProductComponent;
  let fixture: ComponentFixture<SetVatRateProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SetVatRateProductComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SetVatRateProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
