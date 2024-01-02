import { Component, OnInit } from '@angular/core';
import { ProductService } from '../product.service';
import { ToastrService } from 'ngx-toastr';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {Location} from "@angular/common";

@Component({
  selector: 'jhi-modal-confirm-delete-product',
  templateUrl: './modal-confirm-delete-product.component.html',
  styleUrls: ['./modal-confirm-delete-product.component.scss'],
})
export class ModalConfirmDeleteProductComponent implements OnInit {
  id: any = 0;

  constructor(private service: ProductService,
              private toastr: ToastrService,
              public activeModal: NgbActiveModal,
              private location: Location,) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {}

  onDeteleProduct() {
    this.service.deleteProductById(this.id).subscribe(value => {
      this.toastr.success(value.message[0].message, value.message[0].code);
      this.dismiss();
    });
  }

  dismiss() {
    this.activeModal.close();
  }
}
