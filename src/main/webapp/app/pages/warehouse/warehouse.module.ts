import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { WarehouseRoutingModule } from './warehouse-routing.module';
import { SharedModule } from '../../shared/shared.module';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { NgSelectModule } from '@ng-select/ng-select';
import { ScWarehouseComponent } from './sc-warehouse/sc-warehouse.component';
import { WarehouseIntakeComponent } from './warehouse-intake/warehouse-intake.component';
import { ModalConfirmExitComponent } from './modal-confirm-exit/modal-confirm-exit.component';
import { ModalUpdateInventoyTrackingComponent } from './modal-update-inventoy-tracking/modal-update-inventoy-tracking.component';
@NgModule({
  declarations: [ScWarehouseComponent, WarehouseIntakeComponent, ModalConfirmExitComponent, ModalUpdateInventoyTrackingComponent],
  imports: [CommonModule, WarehouseRoutingModule, SharedModule, PerfectScrollbarModule, NgSelectModule],
})
export class WarehouseModule {}
