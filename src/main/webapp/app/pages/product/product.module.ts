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
import { ModalCreateCategoryComponent } from './modal-create-category/modal-create-category.component';
import { ModalConfirmDeleteComponent } from './modal-confirm-delete/modal-confirm-delete.component';
import { ModalCreateUnitComponent } from './modal-create-unit/modal-create-unit.component';
import { ConfirmInventoryTrackingComponent } from './confirm-inventory-tracking/confirm-inventory-tracking.component';
import { ModalDetailDeleteMultiComponent } from './modal-detail-delete-multi/modal-detail-delete-multi.component';
import { ModalPreviewBarcodePrintComponent } from './preview-barcode-print/modal-preview-barcode-print.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { ModalGenPdfBarcodeComponent } from './modal-gen-pdf-barcode/modal-gen-pdf-barcode.component';

@NgModule({
  declarations: [
    ScProductComponent,
    ModalCreateProductComponent,
    ModalCreateCategoryComponent,
    ModalConfirmDeleteComponent,
    ModalCreateUnitComponent,
    ConfirmInventoryTrackingComponent,
    ModalDetailDeleteMultiComponent,
    ModalPreviewBarcodePrintComponent,
    ModalGenPdfBarcodeComponent,
  ],
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
    DragDropModule,
  ],
})
export class ProductModule {}
