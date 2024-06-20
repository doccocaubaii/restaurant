import { Component, OnInit } from '@angular/core';
import { FilterProduct } from '../../product/product';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from '../../../shared/base/base.component';
import { StaffService } from '../staff.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'jhi-modal-create-staff',
  templateUrl: './modal-create-staff.component.html',
  styleUrls: ['./modal-create-staff.component.scss']
})
export class ModalCreateStaffComponent  extends BaseComponent implements OnInit {
  selectedItem: any = {
  };
  id: any = 0;
  lastCompany: any = {};

  constructor(    public activeModal: NgbActiveModal,
                  protected service : StaffService,
                  private toastr: ToastrService,
  ) {
    super();
  }

  ngOnInit(): void {
    this.lastCompany = this.getCompany();
  }

  dismiss(value: any) {
    this.activeModal.dismiss();
  }

  onSave() {
    this.selectedItem.comId = this.lastCompany.id;
    this.service.updateStaff(this.selectedItem).subscribe(value => {
      this.toastr.success(value.message[0].message, value.message[0].code);
      this.activeModal.close();
    },
      error => {
      this.toastr.error(error.message)
      });
  }
}
