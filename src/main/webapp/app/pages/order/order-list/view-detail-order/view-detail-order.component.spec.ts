import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewDetailOrderComponent } from './view-detail-order.component';

describe('ViewDetailOrderComponent', () => {
  let component: ViewDetailOrderComponent;
  let fixture: ComponentFixture<ViewDetailOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewDetailOrderComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ViewDetailOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
