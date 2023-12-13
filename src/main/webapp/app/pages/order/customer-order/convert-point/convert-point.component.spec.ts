import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConvertPointComponent } from './convert-point.component';

describe('ConvertPointComponent', () => {
  let component: ConvertPointComponent;
  let fixture: ComponentFixture<ConvertPointComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ConvertPointComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ConvertPointComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
