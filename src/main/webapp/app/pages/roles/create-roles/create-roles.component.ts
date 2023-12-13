import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../../shared/base/base.component';
import { UtilsService } from '../../../utils/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { Location } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { RolesService } from '../roles.service';
import { ICON_CANCEL, ICON_SAVE } from 'app/shared/other/icon';

@Component({
  selector: 'jhi-create-roles',
  templateUrl: './create-roles.component.html',
  styleUrls: ['./create-roles.component.scss'],
})
export class CreateRolesComponent extends BaseComponent implements OnInit {
  roleDetail: any = {};
  totalItems = 0;
  idRole?: any = 0;
  children: any = [];
  listSelected: any = [];
  lastCompany?: any;
  disableButton = false;
  isView = false;
  protected readonly ICON_SAVE = ICON_SAVE;
  protected readonly ICON_CANCEL = ICON_CANCEL;

  constructor(
    private service: RolesService,
    protected utilsService: UtilsService,
    private toastr: ToastrService,
    private location: Location,
    private translateService: TranslateService,
    protected modalService: NgbModal,
    public activeModal: NgbActiveModal,
    protected activatedRoute: ActivatedRoute,
    private route: Router
  ) {
    super();
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    this.onSearch();
    if (this.idRole) {
      this.onRoleDetail(this.idRole);
    }
  }

  onSearch() {
    this.getPermission();
  }

  getPermission() {
    this.service.getPermissions().subscribe(value => {
      this.children = value.body.data;
      this.totalItems = value.body.count;
    });
  }

  dismiss(value: any) {
    this.activeModal.close(value);
  }

  create() {
    this.disableButton = true;
    this.roleDetail.comId = this.lastCompany.id;
    this.roleDetail.permissions = this.listSelected;
    const req = Object.assign({}, this.roleDetail);
    if (this.idRole > 0) {
      this.service.update(req).subscribe(
        value => {
          this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
          this.searchRolesComponent();
          this.dismiss(value);
          this.disableButton = false;
        },
        error => {
          this.disableButton = false;
        }
      );
    } else {
      this.service.create(req).subscribe(
        value => {
          this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
          this.searchRolesComponent();
          this.dismiss(value);
          this.disableButton = false;
        },
        error => {
          this.disableButton = false;
        }
      );
    }
  }

  onRoleDetail(id: any) {
    this.service.findRole(id).subscribe(value => {
      this.roleDetail = {
        id: value.body.data.id,
        comId: value.body.data.comId,
        code: value.body.data.code,
        name: value.body.data.name,
      };
      this.listSelected = value.body.data.permissions;
      // this.children = value.body.data;
      // this.totalItems = value.body.count;
    });
  }

  searchRolesComponent(): void {
    this.service.triggerSearch();
  }

  receiveListSelected(data: number[]) {
    this.listSelected = data;
  }
}
