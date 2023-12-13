import {Component, OnInit} from '@angular/core';
import {ProductService} from "../product.service";
import {ToastrService} from "ngx-toastr";
import {TranslateService} from "@ngx-translate/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {Location} from "@angular/common";
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-confirm-inventory-tracking',
  templateUrl: './confirm-inventory-tracking.component.html',
  styleUrls: ['./confirm-inventory-tracking.component.scss']
})
export class ConfirmInventoryTrackingComponent implements OnInit {

  isNotShowAgain: false;

  constructor(
    private service: ProductService,
    private toastr: ToastrService,
    private translateService: TranslateService,
    public activeModal: NgbActiveModal,
    private location: Location
  ) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {
  }

  onInventoryTracking() {
    this.activeModal.close({
        isNotShowAgain: this.isNotShowAgain,
        inventoryTracking: true
      }
    );
  }

  dismiss() {
    this.activeModal.close();
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
