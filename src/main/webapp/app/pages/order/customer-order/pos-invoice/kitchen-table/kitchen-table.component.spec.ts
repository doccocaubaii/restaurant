import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KitchenTableComponent } from './kitchen-table.component';

describe('PosInvoiceComponent', () => {
  let component: KitchenTableComponent;
  let fixture: ComponentFixture<KitchenTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [KitchenTableComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(KitchenTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
