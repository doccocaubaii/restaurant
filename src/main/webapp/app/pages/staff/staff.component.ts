import { Component, OnInit } from '@angular/core';
import { Page } from '../const/customer-order.const';
import { StaffService } from './staff.service';
import { ModalCreateProductComponent } from '../product/modal-create-product/modal-create-product.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ModalCreateStaffComponent } from './modal-create-staff/modal-create-staff.component';
import {
  ModalConfirmDeleteProductComponent
} from '../product/modal-confirm-delete-product/modal-confirm-delete-product.component';
import { ModalDeleteStaffComponent } from './modal-delete-staff/modal-delete-staff.component';
import { last_user } from '../../object-stores.constants';
import { BaseComponent } from '../../shared/base/base.component';

@Component({
  selector: 'jhi-staff',
  templateUrl: './staff.component.html',
  styleUrls: ['./staff.component.scss']
})
export class StaffComponent extends BaseComponent implements OnInit {
  list: any;
  filterObj: any = {
    page: Page.PAGE_NUMBER,
    size: Page.PAGE_SIZE,
    totalItem: 0
  };
  lastUser : any;
  private modalRef: NgbModalRef | undefined;

  constructor(private service: StaffService,
              protected modalService: NgbModal
  ) {
    super();
  }

  async ngOnInit() {
    this.lastUser = await this.getFirstItemIndexDB(last_user);
    console.log(this.lastUser);
    this.getList();
  }

  onChangedPage(event: any): void {
    this.filterObj.page = event - 1;
    this.getList();
  }

  protected getList() {
    this.service.page(this.filterObj).subscribe({
      next: (res: any) => {
        this.list = res.body.data;
        this.filterObj.totalItem = res.body.count;
      }
    });
  }

  onCreateStaff(id: any) {
    this.modalRef = this.modalService.open(ModalCreateStaffComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.componentInstance.id = id;
    this.modalRef.closed.subscribe((res?: any) => {
      if (res) {
          this.getList();
      }
    });
  }

  onStaffDetail(item) {
    this.modalRef = this.modalService.open(ModalCreateStaffComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.componentInstance.id = item.id;
    this.modalRef.componentInstance.selectedItem = item;
    this.modalRef.closed.subscribe((res?: any) => {
      if (res) {
        this.getList();
      }
    });
  }

  onConfirmDeleteStaff(id) {
    this.modalRef = this.modalService.open(ModalDeleteStaffComponent, { size: 'dialog-centered', backdrop: 'static' });
    this.modalRef.componentInstance.id = id;
    this.modalRef.closed.subscribe((res?: any) => {
      this.filterObj.page = 1;
      this.getList();
    });
  }
}
