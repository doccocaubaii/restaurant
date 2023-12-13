import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderOfflineManagementComponent } from './order-offline-management.component';

describe('OrderOfflineManagementComponent', () => {
  let component: OrderOfflineManagementComponent;
  let fixture: ComponentFixture<OrderOfflineManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OrderOfflineManagementComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(OrderOfflineManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
