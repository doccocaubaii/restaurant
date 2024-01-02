import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ScProductComponent } from './sc-product/sc-product.component';
const routes: Routes = [
  {
    path: '',
    component: ScProductComponent,
    children: [],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ProductRoutingModule {}
