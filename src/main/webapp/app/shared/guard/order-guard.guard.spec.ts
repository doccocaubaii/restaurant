import { TestBed } from '@angular/core/testing';

import { OrderGuardGuard } from './order-guard.guard';

describe('OrderGuardGuard', () => {
  let guard: OrderGuardGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(OrderGuardGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
