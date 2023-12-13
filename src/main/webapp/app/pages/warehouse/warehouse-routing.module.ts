import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ScWarehouseComponent } from './sc-warehouse/sc-warehouse.component';
import { WarehouseIntakeComponent } from './warehouse-intake/warehouse-intake.component';
import { INOUT_WARD } from '../../constants/app.routing.constants';

const routes: Routes = [
  { path: '', component: ScWarehouseComponent },
  { path: INOUT_WARD, component: WarehouseIntakeComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class WarehouseRoutingModule {}
