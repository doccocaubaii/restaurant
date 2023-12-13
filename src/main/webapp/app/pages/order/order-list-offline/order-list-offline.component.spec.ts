import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderListOfflineComponent } from './order-list-offline.component';

describe('OrderListOfflineComponent', () => {
  let component: OrderListOfflineComponent;
  let fixture: ComponentFixture<OrderListOfflineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OrderListOfflineComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(OrderListOfflineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
