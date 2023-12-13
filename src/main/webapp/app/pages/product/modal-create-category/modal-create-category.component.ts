import { Component, OnDestroy, OnInit } from '@angular/core';
import { BaseComponent } from '../../../shared/base/base.component';
import { ProductService } from '../product.service';
import { ToastrService } from 'ngx-toastr';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { Location } from '@angular/common';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-modal-create-category',
  templateUrl: './modal-create-category.component.html',
  styleUrls: ['./modal-create-category.component.scss'],
})
export class ModalCreateCategoryComponent extends BaseComponent implements OnDestroy, OnInit {
  categorySelected: any = {};

  lastCompany: any = {};
  disableButton = false;
  constructor(
    private service: ProductService,
    private toastr: ToastrService,
    public activeModal: NgbActiveModal,
    private translateService: TranslateService,
    private location: Location
  ) {
    super();
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
  }

  onSaveUpdateCategory() {
    this.disableButton = true;
    this.disableButton = JSON.parse(JSON.stringify(this.disableButton));
    if (!this.categorySelected.name || this.categorySelected.name.trim().length == 0) {
      this.toastr.error(
        this.translateService.instant('easyPos.product.info.categoryNameError'),
        this.translateService.instant('easyPos.product.info.message')
      );
      this.enableButton();
    } else {
      if (!this.categorySelected.id) {
        this.categorySelected.comId = this.lastCompany.id;
        this.service.createCategory(this.categorySelected).subscribe(
          value => {
            this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
            this.dismiss(value);
          },
          error => {
            this.enableButton();
          }
        );
      } else {
        this.service.updateCategory(this.categorySelected).subscribe(
          value => {
            this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
            this.dismiss(value);
          },
          error => {
            this.enableButton();
          }
        );
      }
    }
  }
  dismiss(value: any) {
    this.activeModal.close(value);
  }
  enableButton() {
    this.disableButton = false;
    this.disableButton = JSON.parse(JSON.stringify(this.disableButton));
  }

  onTrimSpace() {
    this.categorySelected.name = this.categorySelected.name.trim();
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
    protected readonly ICON_SAVE = ICON_SAVE;
}
