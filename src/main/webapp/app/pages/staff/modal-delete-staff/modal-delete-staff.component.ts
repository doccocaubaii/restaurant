import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { ProductService } from '../../product/product.service';
import { StaffService } from '../staff.service';

@Component({
  selector: 'jhi-modal-delete-staff',
  templateUrl: './modal-delete-staff.component.html',
  styleUrls: ['./modal-delete-staff.component.scss']
})
export class ModalDeleteStaffComponent implements OnInit {
  id: any = 0;

  constructor(public activeModal: NgbActiveModal,
              private toastr: ToastrService,
              private service: StaffService
  ) {
  }

  ngOnInit(): void {
  }

  onDeteleStaff() {
    this.service.deleteStaffById(this.id).subscribe(value => {
      this.toastr.success(value.message[0].message, value.message[0].code);
      this.dismiss();
    });
  }

  dismiss() {
    this.activeModal.close();
  }
}
