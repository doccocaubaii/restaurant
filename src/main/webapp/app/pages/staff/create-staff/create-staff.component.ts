import { Component, OnInit } from '@angular/core';
import { IStaff, Staff } from '../../../entities/staff/staff.model';
import { StaffService } from '../staff.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from '../../../shared/base/base.component';
import { LoadingOption } from '../../../utils/loadingOption';
import { FilterCompany, FilterRole, Page } from '../staff';
import { UtilsService } from '../../../utils/Utils.service';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-create-staff',
  templateUrl: './create-staff.component.html',
  styleUrls: ['./create-staff.component.scss'],
})
export class CreateStaffComponent extends BaseComponent implements OnInit {
  company: any;
  companyId: number;
  staff = Staff.createNewStaff();
  roleNames: any = [];
  companies: any = [];
  filterCompany: FilterCompany = { page: 0, size: Page.ONE_PAGE };
  listSelected: any = [];
  paramCheckAll: boolean = false;
  paramCheckAllPage: any;
  selectedItem: any = {};
  totalItems = 0;
  sizes = [
    {
      id: 10,
      name: '10',
    },
    {
      id: 20,
      name: '20',
    },
    {
      id: 30,
      name: '30',
    },
  ];
  idCompany = -1;
  onLoading = false;
  isView = false;
  filterRole: FilterRole = { page: 0, size: Page.ONE_PAGE };
  haveRole = false;
  isParent: any;

  constructor(
    private staffService: StaffService,
    public activeModal: NgbActiveModal,
    private toastr: ToastrService,
    public loading: LoadingOption,
    protected utilsService: UtilsService
  ) {
    super();
  }

  async ngOnInit() {
    this.company = await this.getCompany();
    this.companyId = this.company.id;
    this.isParent = this.company.isParent;
    this.getAllCompany();
    this.checkStaffCompany();
    if (this.staff.id > 0) {
      this.staff = this.getRoleIds(this.staff);
    }
  }

  close(typeEdit: string) {
    this.staff.comId = this.companyId;
    this.staff.username = this.staff.phoneNumber;
    this.onLoading = true;
    this.updateRoles(this.staff);
    this.checkRole();
    if (!this.haveRole) {
      return;
    }
    if (typeEdit == 'add') {
      this.staffService.postStaff(this.staff as IStaff).subscribe(
        response => {
          if (response.status) {
            this.onLoading = false;
            this.toastr.success(response.message[0].message);
            this.activeModal.close(true);
            this.staffService.triggerSearch();
          }
        },
        error => {
          this.onLoading = false;
        }
      );
    } else {
      this.staffService.putStaff(this.staff as IStaff).subscribe(
        response => {
          if (response.status) {
            this.onLoading = false;
            this.toastr.success(response.message[0].message);
            this.activeModal.close(true);
            this.staffService.triggerSearch();
          }
        },
        error => {
          this.onLoading = false;
        }
      );
    }
  }

  dismiss() {
    this.activeModal.dismiss();
  }

  testMail(value: any) {
    if (value == null || value == '') return true;
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(value);
  }

  getAllCompany() {
    const req = Object.assign({}, this.filterCompany);
    if (this.filterCompany.page > 0) {
      req.page = this.filterCompany.page - 1;
    }
    this.staffService.getCompany(req).subscribe(response => {
      this.companies = response.body.data;
      this.totalItems = response.body.count;
      let listCompany: any[] = [];
      for (const item of this.companies) {
        let newCompanies: any = {};
        newCompanies.companyName = item.name;
        newCompanies.comId = item.id;
        newCompanies.isParent = item.isParent;
        newCompanies.roles = [];
        newCompanies.listRole = item.roles;
        listCompany.push(newCompanies);
      }
      if (this.staff.id < 1) {
        this.staff.companies = listCompany;
      } else {
        if (this.staff.companies.length > 0) {
          for (const company1 of this.staff.companies) {
            for (let company2 of this.companies) {
              if (company1.comId == company2.id) {
                company1.listRole = company2.roles;
              }
            }
          }
        }
      }
    });
  }

  checkRolIds() {
    for (const item of this.staff.companies) {
      if (!item.check || item.check == false) {
        item.roles = [];
        item.roleIds = [];
      }
    }
  }

  checkStaffCompany() {
    if (this.staff.companies.length === 0) {
      this.paramCheckAll = false;
      this.paramCheckAllPage = false;
    } else {
      for (let item of this.staff.companies) {
        if (item.roles.length > 0) {
          item.check = true;
        }
      }
    }
  }

  errPermissions(staff: any) {
    if (!staff.check || staff.check == false) {
      this.toastr.error('Chưa chọn công ty', 'Lỗi');
    }
  }

  getRoleIds(staff: any): any {
    let roleIds = new Map<any, any>();
    for (const item of staff.companies) {
      for (let itemElement of item.roles) {
        let ids: any[] = [];
        if (roleIds.has(item.comId)) {
          ids = roleIds.get(item.comId);
        }
        ids.push(itemElement.roleId);
        roleIds.set(item.comId, ids);
      }
    }
    for (const item of staff.companies) {
      item.roleIds = roleIds.get(item.comId);
    }
    return staff;
  }

  updateRoles(staff: any) {
    for (let item1 of staff.companies) {
      item1.roles = [];
      let listRole: any[] = [];
      let roles = new Map<any, any>();
      for (let item of item1.listRole) {
        roles.set(item.roleId, item.roleName);
      }
      if (item1.roleIds && item1.roleIds.length > 0) {
        for (let roleId of item1.roleIds) {
          let role: any = {};
          if (roles.has(roleId)) {
            role.roleId = roleId;
            role.roleName = roles.get(roleId);
            listRole.push(role);
          }
        }
      }
      item1.roles = listRole;
    }
  }

  checkRole() {
    for (const company1 of this.staff.companies) {
      if (company1.roles.length > 0) {
        this.haveRole = true;
      }
    }
    if (!this.haveRole) {
      this.onLoading = false;
      this.toastr.error('Phải chọn vai trò cho nhân viên', 'Lỗi');
    }
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
