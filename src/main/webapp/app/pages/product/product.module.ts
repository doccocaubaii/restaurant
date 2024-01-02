import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProductRoutingModule } from './product-routing.module';
import { ScProductComponent } from './sc-product/sc-product.component';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { MatSelect, MatSelectModule } from '@angular/material/select';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from '../../shared/shared.module';
import { ModalCreateProductComponent } from './modal-create-product/modal-create-product.component';
import { ModalConfirmDeleteProductComponent } from './modal-confirm-delete-product/modal-confirm-delete-product.component';

@NgModule({
  declarations: [ScProductComponent, ModalCreateProductComponent, ModalConfirmDeleteProductComponent],
  imports: [
    CommonModule,
    ProductRoutingModule,
    PerfectScrollbarModule,
    MatSelectModule,
    ReactiveFormsModule,
    NgMultiSelectDropDownModule.forRoot(),
    FormsModule,
    NgSelectModule,
    SharedModule,
  ],
})
export class ProductModule {}
