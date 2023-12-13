import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'jhi-base-form-product',
  templateUrl: './base-form-product.component.html',
  styleUrls: ['./base-form-product.component.scss'],
})
export class BaseFormProductComponent implements OnInit {
  @Input() product!: any;
  @Input() type!: string;
  constructor() {}

  ngOnInit(): void {}
}
