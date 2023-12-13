import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { StaffService } from './staff.service';
import { BaseComponent } from '../../shared/base/base.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IStaff } from '../../entities/staff/staff.model';
import { CreateStaffComponent } from './create-staff/create-staff.component';
import { ToastrService } from 'ngx-toastr';
import { LoadingOption } from '../../utils/loadingOption';
import { ConfirmDialogComponent } from '../../shared/modal/confirm-dialog/confirm-dialog.component';
import { ModalBtn, ModalContent, ModalHeader } from '../../constants/modal.const';
import { TranslateService } from '@ngx-translate/core';
import { DialogModal } from '../order/model/dialogModal.model';
import { last_user } from '../../object-stores.constants';
import { Authority } from '../../config/authority.constants';
import { Subscription } from 'rxjs';
import { ICON_CREATE, ICON_DELETE, ICON_UPDATE } from 'app/shared/other/icon';
const FILTER_PAG_REGEX = /\D/g;

@Component({
  selector: 'jhi-staff',
  templateUrl: './staff.component.html',
  styleUrls: ['./staff.component.scss'],
})
export class StaffComponent extends BaseComponent implements OnInit {
  sort: string;
  totalSize: number = 0;
  keyword: string;
  staffArray: IStaff[] = [];
  idEditing: number = -1;
  page: number = 1;
  pageSize: number = 20;
  company: any;
  companyId: number;
  idDel = -1;
  private modalRef: NgbModalRef | undefined;
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
  checkUser = false;
  user: any;
  ownerId: any;
  userId: any;
  authorAdd = Authority.EMPLOYEE_ADD;
  authorUpdate = Authority.EMPLOYEE_UPDATE;
  authorDelete = Authority.EMPLOYEE_DELETE;
  private searchSubscription: Subscription;
  openModal = true;
  protected readonly ICON_UPDATE = ICON_UPDATE;
  protected readonly ICON_DELETE = ICON_DELETE;
  protected readonly ICON_CREATE = ICON_CREATE;

  constructor(
    private staffService: StaffService,
    private modalService: NgbModal,
    private toastr: ToastrService,
    public loading: LoadingOption,
    private translateService: TranslateService
  ) {
    super();
    this.searchSubscription = this.staffService.searchObservable$.subscribe(() => {
      this.search();
    });
  }

  async ngOnInit() {
    this.company = await this.getCompany();
    this.companyId = this.company.id;
    this.getStaff();
    this.user = await this.getFirstItemIndexDB(last_user);
    this.ownerId = this.company.ownerId;
    this.userId = this.user.id;
    if (this.ownerId == this.userId) {
      this.checkUser = true;
    }
    // const permissions = this.covertArr(this.user.permissions);
    // if (permissions.length > 0) {
    //   for (const item of permissions) {
    //     if (item.trim() == Authority.EMPLOYEE_ADD) {
    //       this.checkUser = true;
    //     }
    //   }
    // }
  }
  openCreate() {
    if (this.modalRef) {
      this.modalRef.close();
    }
    this.modalRef = this.modalService.open(CreateStaffComponent, { size: 'xl', backdrop: 'static' });
    this.modalRef.componentInstance.isView = false;
    this.modalRef.result.then(
      result => {
        this.getStaff();
      },
      reason => {}
    );
  }

  selectPage(page: string) {
    this.page = parseInt(page, 10) || 1;
    this.getStaff();
  }
  formatInput(input: HTMLInputElement) {
    input.value = input.value.replace(FILTER_PAG_REGEX, '');
  }
  getStaff() {
    this.staffArray = [];
    this.staffService.getStaff(this.page, this.pageSize, this.sort, this.keyword).subscribe(res => {
      this.staffArray = res.data;
      this.totalSize = res.count;
    });
  }
  search() {
    this.getStaff();
  }

  openUpdate(staff: IStaff) {
    if (this.openModal) {
      this.openModal = false;
      if (this.modalRef) {
        this.modalRef.close();
      }
      this.staffService.findStaff(staff.id).subscribe(value => {
        this.modalRef = this.modalService.open(CreateStaffComponent, { size: 'xl', backdrop: 'static' });
        // @ts-ignore
        this.modalRef.componentInstance.staff = value.body.data;
        this.modalRef.componentInstance.isView = false;
        this.openModal = true;
      });
    }
  }

  openDeleteStaff() {
    if (this.openModal) {
      this.openModal = false;
      if (this.modalRef) {
        this.modalRef.close();
      }
      this.modalRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
      this.openModal = true;
      this.modalRef.componentInstance.value = new DialogModal(
        ModalHeader.DELETE_STAFF,
        ModalContent.DELETE_STAFF,
        ModalBtn.DELETE_STAFF,
        'trash-can',
        'btn-delete'
      );
      this.modalRef.componentInstance.formSubmit.subscribe(res => {
        if (res) {
          // Sau khi bấm nút đồng ý thì thực hiện công việc và đóng model
          this.deleteStaff();
          if (this.modalRef) this.modalRef.close();
        }
      });
    }
  }

  deleteStaff() {
    this.staffService.delStaff(this.idDel).subscribe(response => {
      if (response.status) {
        this.toastr.success(response.message[0].message);
        this.getStaff();
        return;
      }
    });
  }

  test() {}

  onDeleteKeywordSearch() {
    if (!this.keyword) this.getStaff();
  }

  covertArr(permissions: any) {
    const cleanedPermissions = permissions.slice(1, -1);
    const permissionsArray = cleanedPermissions.split(',');
    return permissionsArray;
  }

  view(id: any) {
    if (this.openModal) {
      this.openModal = false;
      if (this.modalRef) {
        this.modalRef.close();
      }
      this.staffService.findStaff(id).subscribe(value => {
        this.modalRef = this.modalService.open(CreateStaffComponent, { size: 'xl', backdrop: 'static' });
        // @ts-ignore
        this.modalRef.componentInstance.staff = value.body.data;
        this.modalRef.componentInstance.isView = true;
        this.openModal = true;
      });
    }
  }
}
