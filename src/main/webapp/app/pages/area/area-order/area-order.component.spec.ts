import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AreaOrderComponent } from './area-order.component';

describe('AreaOrderComponent', () => {
  let component: AreaOrderComponent;
  let fixture: ComponentFixture<AreaOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AreaOrderComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AreaOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
