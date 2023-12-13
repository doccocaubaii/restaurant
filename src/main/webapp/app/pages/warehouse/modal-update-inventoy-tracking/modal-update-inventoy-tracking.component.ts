import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';
import { ProductService } from '../../product/product.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { WarehouseService } from '../warehouse.service';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-modal-update-inventoy-tracking',
  templateUrl: './modal-update-inventoy-tracking.component.html',
  styleUrls: ['./modal-update-inventoy-tracking.component.scss'],
})
export class ModalUpdateInventoyTrackingComponent implements OnInit {
  productInfo: any = {};
  disableBtn = false;

  constructor(
    public activeModal: NgbActiveModal,
    private location: Location,
    private serviceProduct: ProductService,
    private toastr: ToastrService,
    private translateService: TranslateService,
    private warehouseService: WarehouseService
  ) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {}

  onSave() {
    this.disableBtn = true;
    let req = {
      comId: this.productInfo.comId,
      productProductUnitId: this.productInfo.productProductUnitId,
      inventoryTracking: this.productInfo.inventoryTracking,
      inventoryCount: this.productInfo.inventoryCount,
      purchasePrice: this.productInfo.purchasePrice,
    };
    this.warehouseService.enableInvetoryTracking(req).subscribe(
      value => {
        let response = {
          inventoryTracking: req.inventoryTracking,
          purchasePrice: req.purchasePrice,
          inventoryCount: req.inventoryCount,
        };
        this.dismiss(response);
        this.disableBtn = false;
        this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
      },
      error => {
        this.disableBtn = false;
      }
    );
  }

  dismiss(check: any) {
    this.activeModal.close(check);
  }

  onChangeInventoryTracking() {
    this.productInfo.inventoryTracking = !this.productInfo.inventoryTracking;
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
