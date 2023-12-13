import { Component, OnDestroy, OnInit } from '@angular/core';
import { BaseComponent } from '../../../shared/base/base.component';
import { CompanyService } from '../company.service';
import { ToastrService } from 'ngx-toastr';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { Location } from '@angular/common';
import { CompanySessionItem } from '../../../entities/companySession/CompanySessionItem.model';
import { last_company, last_user } from '../../../object-stores.constants';
import { CompanyOwner } from '../../../entities/company/company-owner';
import { PHONE_FORMAT } from '../../../constants/regex.constants';
import { LoadingOption } from '../../../utils/loadingOption';
import { UtilsService } from '../../../utils/Utils.service';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-company-modal-create',
  templateUrl: './company-modal-create.component.html',
  styleUrls: ['./company-modal-create.component.scss'],
})
export class CompanyModalCreateComponent extends BaseComponent implements OnDestroy, OnInit {
  companySelected: any = { id: 0, name: '' };
  companyArray: CompanyOwner[] = [];
  comOwnerId;
  lastCompany: any;
  lastUser: any = {};
  disableButton = false;
  checkCreate = false;
  validation = false;
  companies: CompanySessionItem[] = [];

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
    if (!this.companySelected.id) {
      this.checkCreate = true;
      this.companyArray.forEach(value => {
        value.label = (value.taxCode ?? '') + ' - ' + (value.name ?? '');
      });
      this.companyArray = JSON.parse(JSON.stringify(this.companyArray));
      // @ts-ignore
      this.companySelected.comOwnerId = this.companyArray.find(item => item.taxCode === this.lastCompany?.taxcode).id;
    }
  }

  onSaveUpdateCompany() {
    if (this.validateInput()) {
      if (this.checkCreate) {
        //this.companySelected.comId = this.lastCompany.id;
        //this.companySelected.comOwnerId = this.comOwnerId;
        this.service.createCompany({ ...this.companySelected }).subscribe(
          async value => {
            this.utilsService.refreshLastUser.next(true);
            this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.company.info.message'));
            // await this.updateById(last_user, this.lastUser.id, {
            //   ...this.lastUser,
            //   companies: [...this.lastUser.companies, {...this.companySelected, comOwnerId: this.comOwnerId}]
            // })
            this.activeModal.close(value.data);
          },
          error => {
            this.enableButton();
          }
        );
      } else {
        this.service.updateCompany({ ...this.companySelected, comOwnerId: this.comOwnerId }).subscribe(
          value => {
            this.utilsService.refreshLastUser.next(true);
            this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.company.info.message'));
            this.activeModal.dismiss();
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

  changeCompanyOwner(event) {
    this.comOwnerId = event;
  }

  validateInput() {
    if (this.companySelected.id == null) {
      this.toastr.error(
        this.translateService.instant('easyPos.company.info.companyOwnerError'),
        this.translateService.instant('easyPos.company.info.message')
      );
      this.enableButton();
      return false;
    }
    if (!this.companySelected.name || this.companySelected.name.trim().length === 0) {
      this.toastr.error(
        this.translateService.instant('easyPos.company.info.companyNameError'),
        this.translateService.instant('easyPos.company.info.message')
      );
      this.enableButton();
      return false;
    }
    if (this.companySelected.phone !== undefined && !this.companySelected.phone && this.companySelected.phone.trim().length !== 0) {
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
