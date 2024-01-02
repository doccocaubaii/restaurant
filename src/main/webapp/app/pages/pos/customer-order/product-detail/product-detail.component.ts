import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { IProduct } from '../../model/product.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';

@Component({
  selector: 'jhi-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
})
export class ProductDetailComponent implements OnInit, OnDestroy {
  @Input() product!: IProduct;
  // productLocal = JSON.parse(JSON.stringify(this.product));
  constructor(public activeModal: NgbActiveModal, private location: Location) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {}

  closeModal() {
    this.activeModal.close();
  }

  addToCart() {
    this.activeModal.close(true);
  }

  ngOnDestroy() {
    this.activeModal.dismiss();
  }
}
