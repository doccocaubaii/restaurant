import { Component, OnDestroy, OnInit } from '@angular/core';
import { BaseComponent } from '../../../shared/base/base.component';
import { CompanyService } from '../company.service';
import { ToastrService } from 'ngx-toastr';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { Location } from '@angular/common';
import { last_company, last_user } from '../../../object-stores.constants';
import { PHONE_FORMAT } from '../../../constants/regex.constants';
import { LoadingOption } from '../../../utils/loadingOption';
import { DataEncrypt } from '../../../entities/indexDatabase/data-encrypt.model';
import { UtilsService } from '../../../utils/Utils.service';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-company-modal-update',
  templateUrl: './company-modal-update.component.html',
  styleUrls: ['./company-modal-update.component.scss'],
})
export class CompanyModalUpdateComponent extends BaseComponent implements OnDestroy, OnInit {
  companySelected: any = {};
  comOwnerId;
  lastCompany: any = {};
  disableButton = false;
  lastUser: any = {};

  constructor(
    private service: CompanyService,
    private toastr: ToastrService,
    public activeModal: NgbActiveModal,
    private translateService: TranslateService,
    private location: Location,
    public loading: LoadingOption,
    private utilsService: UtilsService
  ) {
    super();
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  async ngOnInit() {
    this.lastUser = await this.getFirstItemIndexDB(last_user);
    this.lastCompany = await this.getCompany();
  }

  onSaveUpdateCompany() {
    if (this.validateInput()) {
      this.companySelected.comOwnerId = this.comOwnerId;
      if (!this.companySelected.id) {
        this.companySelected.comId = this.lastCompany.id;
        this.service.createCompany(this.companySelected).subscribe(
          value => {
            this.utilsService.refreshLastUser.next(true);
            this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.company.info.message'));
            // @ts-ignore
            this.activeModal.close(value);
          },
          error => {
            this.enableButton();
          }
        );
      } else {
        this.service.updateCompany(this.companySelected).subscribe(
          async value => {
            this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.company.info.message'));
            const indexToUpdate = this.lastUser.companies.findIndex(item => item.id === this.companySelected.id);
            this.lastUser.companies[indexToUpdate] = this.companySelected;

            const newSessionName = this.lastUser.companies.find(item => item.id === this.lastUser.comId).name;
            const newSessionAddress = this.lastUser.companies.find(item => item.id === this.lastUser.comId).address;

            // update last user
            const userUpdate = { ...this.lastUser, comName: newSessionName, companies: this.lastUser.companies };
            await this.updateById(last_user, this.lastUser.id, new DataEncrypt(this.lastUser.id, this.encryptFromData(userUpdate)));

            // update last company
            const companyUpdate = { ...this.companySelected, name: newSessionName, address: newSessionAddress };
            await this.updateById(
              last_company,
              this.companySelected.id,
              new DataEncrypt(this.companySelected.id, this.encryptFromData(companyUpdate))
            );

            this.utilsService.refreshLastUser.next(true);
            this.activeModal.close(value);
          },
          error => {
            this.enableButton();
          }
        );
      }
    }
  }

  dismiss(value: any) {
    this.activeModal.dismiss();
  }

  enableButton() {
    this.disableButton = false;
    this.disableButton = JSON.parse(JSON.stringify(this.disableButton));
  }

  validateInput() {
    if (!this.companySelected.name || this.companySelected.name.trim().length === 0) {
      this.toastr.error(
        this.translateService.instant('easyPos.company.info.companyNameError'),
        this.translateService.instant('easyPos.company.info.message')
      );
      this.enableButton();
      return false;
    }
    if (this.companySelected.phone !== undefined && this.companySelected.phone !== null && this.companySelected.phone.trim().length !== 0) {
      if (!PHONE_FORMAT.test(this.companySelected.phone)) {
        this.toastr.error(
          this.translateService.instant('easyPos.company.info.companyPhoneError'),
          this.translateService.instant('easyPos.company.info.message')
        );
        this.enableButton();
        return false;
      }
    }
    return true;
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
