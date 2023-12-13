import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VoucherOrderComponent } from './voucher-order.component';

describe('VoucherOrderComponent', () => {
  let component: VoucherOrderComponent;
  let fixture: ComponentFixture<VoucherOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VoucherOrderComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(VoucherOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
