import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ProductService } from '../product.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from '../../../shared/base/base.component';
import { Location } from '@angular/common';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-modal-create-unit',
  templateUrl: './modal-create-unit.component.html',
  styleUrls: ['./modal-create-unit.component.scss'],
})
export class ModalCreateUnitComponent extends BaseComponent implements OnInit {
  unit: any = {};
  name = '';
  description = '';
  lastCompany: any;
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

  onSaveUnit() {
    if (!this.unit.name || this.unit.name.trim().length == 0) {
      this.toastr.error(
        this.translateService.instant('easyPos.product.info.unitNameError'),
        this.translateService.instant('easyPos.product.info.message')
      );
    } else {
      if (this.unit.id) {
        this.disableButton = true;
        this.unit.comId = this.lastCompany.id;
        this.service.updateUnit(this.unit).subscribe(
          value => {
            this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
            this.dismiss(value);
            this.disableButton = false;
          },
          error => {
            this.disableButton = false;
          }
        );
      } else {
        this.disableButton = true;
        this.service.createUnit(this.lastCompany.id, this.unit.name, this.unit.description).subscribe(
          value => {
            this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
            this.dismiss(value);
            this.disableButton = false;
          },
          error => {
            this.disableButton = false;
          }
        );
      }
    }
  }
  dismiss(value: any) {
    this.activeModal.close(value);
  }

    protected readonly ICON_SAVE = ICON_SAVE;
  protected readonly ICON_CANCEL = ICON_CANCEL;
}
