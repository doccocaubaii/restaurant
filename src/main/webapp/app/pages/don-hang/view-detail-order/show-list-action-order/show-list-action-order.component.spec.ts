import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowListActionOrderComponent } from './show-list-action-order.component';

describe('ShowListActionOrderComponent', () => {
  let component: ShowListActionOrderComponent;
  let fixture: ComponentFixture<ShowListActionOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShowListActionOrderComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ShowListActionOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
