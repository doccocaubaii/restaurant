import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../../shared/base/base.component';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { FilterRoles, Page } from '../roles';
import { RolesService } from '../roles.service';
import { CreateRolesComponent } from '../create-roles/create-roles.component';
import { last_user } from '../../../object-stores.constants';
import { DialogModal } from '../../order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from '../../../constants/modal.const';
import { ConfirmDeleteComponent } from '../confirm-delete/confirm-delete.component';
import { Authority } from '../../../config/authority.constants';
import { ICON_COPY, ICON_CREATE, ICON_DELETE, ICON_UPDATE } from 'app/shared/other/icon';

@Component({
  selector: 'jhi-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.scss'],
})
export class RolesComponent extends BaseComponent implements OnInit {
  totalItems = 0;
  lastCompany: any;
  listSelected: any = [];
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
  roles: any = [];
  permissions: any = [];
  filterRoles: FilterRoles = { page: Page.FIRST_PAGE, size: Page.PAGE_SIZE };
  idRole: any = 0;
  checkUser = false;
  user: any;
  company: any;
  ownerId: any;
  userId: any;
  authorAdd = Authority.ROLE_ADD;
  authorUpdate = Authority.ROLE_UPDATE;
  authorDelete = Authority.ROLE_DELETE;
  authorCopy = Authority.ROLE_COPY;

  private modalRef: NgbModalRef | undefined;
  private searchSubscription: Subscription;

  protected readonly ICON_UPDATE = ICON_UPDATE;
  protected readonly ICON_DELETE = ICON_DELETE;
  protected readonly ICON_COPY = ICON_COPY;

  constructor(
    private service: RolesService,
    private toastr: ToastrService,
    private route: Router,
    protected modalService: NgbModal,
    private translateService: TranslateService,
    protected activatedRoute: ActivatedRoute
  ) {
    super();
    this.searchSubscription = this.service.searchObservable$.subscribe(() => {
      this.onSearch();
    });
  }

  async ngOnInit() {
    this.onSearch();
    if (this.roles.length > 0) {
      this.totalItems = this.roles.length;
    }
    this.company = await this.getCompany();
    this.user = await this.lastUser();
    this.ownerId = this.company.ownerId;
    this.userId = this.user.id;
    if (this.ownerId == this.userId) {
      this.checkUser = true;
    }
    // const permissions = this.covertArr(this.user.permissions);
    // if (permissions.length > 0) {
    //   for (const item of permissions) {
    //     if (item.trim() == Authority.ROLE_ADD) {
    //       this.checkUser = true;
    //     }
    //   }
    // }
  }

  onSearch() {
    this.getRoles();
  }

  getRoles() {
    const req = Object.assign({}, this.filterRoles);
    if (this.filterRoles.page > 0) {
      req.page = this.filterRoles.page - 1;
    } else {
      req.page = this.filterRoles.page;
    }
    this.service.getRoles(req).subscribe(value => {
      this.roles = value.body.data;
      this.totalItems = value.body.count;
    });
  }

  onChangedPage(event: any): void {
    this.filterRoles.page = event;
    this.handleNavigation(this.filterRoles.page, this.filterRoles.size);
  }

  protected handleNavigation(page: number, size: number): void {
    const queryParamsObj = {
      page,
      size,
    };
    this.route.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  async onCreateRole() {
    this.openModalCreateRole(0);
  }

  openModalCreateRole(id: any) {
    this.modalRef = this.modalService.open(CreateRolesComponent, { size: 'xl', backdrop: 'static' });
    this.modalRef.componentInstance.idRole = JSON.parse(JSON.stringify(id));
    this.modalRef.closed.subscribe((res?: any) => {});
  }

  async onDetailRoles(id: any) {
    this.openModalCreateRole(id);
  }

  delete(role: any) {
    this.modalRef = this.modalService.open(ConfirmDeleteComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.componentInstance.value = new DialogModal(
      ModalHeader.DELETE_ROLE,
      ModalContent.DELETE_ROLE,
      ModalHeader.DELETE_ROLE,
      'trash-can',
      'btn-delete'
    );
    this.modalRef.componentInstance.name = role.name;
    this.modalRef.componentInstance.formSubmit.subscribe(res => {
      if (res) {
        // @ts-ignore
        this.modalRef.dismiss();
        this.service.delete(role.id).subscribe(value => {
          this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
          this.getRoles();
        });
      }
    });
  }

  onDeleteKeyWord() {
    if (!this.filterRoles.keyword) {
      this.onSearch();
    }
  }

  copyPermissions(id: any) {
    this.service.findRole(id).subscribe(value => {
      this.toastr.success(
        'Sao chép quyền của vai trò ' + value.body.data.name + ' thành công.',
        this.translateService.instant('easyPos.product.info.message')
      );
      this.permissions = value.body.data.permissions;
      this.modalRef = this.modalService.open(CreateRolesComponent, { size: 'xl', backdrop: 'static' });
      this.modalRef.componentInstance.listSelected = JSON.parse(JSON.stringify(this.permissions));
      this.modalRef.closed.subscribe((res?: any) => {});
    });
  }

  async lastUser() {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    const data = await this.getAllByObjectStore(last_user);
    return data ? data[0] : {};
  }

  covertArr(permissions: any) {
    const cleanedPermissions = permissions.slice(1, -1);
    const permissionsArray = cleanedPermissions.split(',');
    return permissionsArray;
  }

  view(id: any) {
    this.modalRef = this.modalService.open(CreateRolesComponent, { size: 'xl', backdrop: 'static' });
    this.modalRef.componentInstance.idRole = JSON.parse(JSON.stringify(id));
    this.modalRef.componentInstance.isView = true;
    this.modalRef.closed.subscribe((res?: any) => {});
  }
}
