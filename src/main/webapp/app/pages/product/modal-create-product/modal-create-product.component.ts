import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FilterProduct } from '../product';
import { ProductService } from '../product.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from '../../../shared/base/base.component';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';

@Component({
  selector: 'jhi-modal-create-product',
  templateUrl: './modal-create-product.component.html',
  styleUrls: ['./modal-create-product.component.scss']
})
export class ModalCreateProductComponent extends BaseComponent implements OnInit {
  @ViewChild('imageInput')
  imageInput!: ElementRef;
  selectedProduct: any = {};
  filterCategory: FilterProduct = { page: 0, size: 200, isCountAll: true };
  id: any = 0;
  isVatRate: boolean = false;
  categories: any = [];
  isConvertUnit = false;
  unitName = '';
  convertUnitDetail: any = {};
  lastCompany: any = {};
  private modalRef: NgbModalRef | undefined;

  constructor(
    private service: ProductService,
    private toastr: ToastrService,
    private location: Location,
    private translateService: TranslateService,
    protected modalService: NgbModal,
    public activeModal: NgbActiveModal
  ) {
    super();
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    this.selectedProduct = {};
    this.selectedProduct.conversionUnits = [];
    if (this.selectedProduct.vatRate !== null && this.selectedProduct.vatRate !== undefined) {
      this.isVatRate = true;
    } else {
      this.isVatRate = false;
    }
    if (this.id !== 0) {
      this.onProductDetail(this.id);
    }
  }

  onInputFile() {
    this.imageInput.nativeElement.click();
  }

  onDismisImage(event: any) {
    event.stopPropagation();
    this.selectedProduct.images = null;
    this.selectedProduct.imageUrl = null;
  }

  onImageSelected(event) {
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.onload = () => {
        this.selectedProduct.imageUrl = reader.result as string;
      };
      reader.readAsDataURL(file);
      this.selectedProduct.images = file;
      // Khi người dùng thay đổi ảnh, cập nhật selectedProduct.imageUrl
      // Bạn có thể thêm mã vào đây để lưu ảnh lên máy chủ hoặc xử lý theo nhu cầu
    }
  }

  onProductDetail(id: any) {
    this.service.getProductById(id).subscribe(value => {
      this.selectedProduct = {};
      this.selectedProduct = {
        images: null,
        id: value.data.id,
        comId: value.data.comId,
        code: value.data.code,
        name: value.data.name,
        unit: value.data.unit,
        purchasePrice: value.data.purchasePrice,
        salePrice: value.data.salePrice,
        description: value.data.description,
        imageUrl: value.data.imageUrl
      };
    });
  }

  onCheckboxChange() {
    this.selectedProduct.inventoryTracking = !this.selectedProduct.inventoryTracking;
  }

  minus() {
    if (this.selectedProduct.inventoryCount) {
      if (this.selectedProduct.inventoryCount >= 1) {
        this.selectedProduct.inventoryCount = this.selectedProduct.inventoryCount - 1;
      }
    }
  }

  plus() {
    if (this.selectedProduct.inventoryCount) {
      this.selectedProduct.inventoryCount += 1;
    } else {
      this.selectedProduct.inventoryCount = 1;
    }
  }

  onSelectVatRate(value: any) {
    this.selectedProduct.vatRate = value;
  }

  onChangeUnit($event) {
    this.selectedProduct.unitId = $event.id;
    this.selectedProduct.unit = $event.name;
  }

  onUpdateConversionUnit(convertUnit: any) {
    this.convertUnitDetail = {};
    this.convertUnitDetail = JSON.parse(JSON.stringify(convertUnit));
    this.convertUnitDetail.checkUpdateConvertUnit = true;
    this.isConvertUnit = true;
  }

  onCreateConversionUnit() {
    if (!this.selectedProduct.unitId) {
      this.toastr.error(
        this.translateService.instant('easyPos.product.info.unitNull'),
        this.translateService.instant('easyPos.product.info.message')
      );
    } else {
      this.convertUnitDetail = {};
      if (!this.selectedProduct.id) {
        if (this.selectedProduct.salePrice) {
          this.convertUnitDetail.salePrice = JSON.parse(JSON.stringify(this.selectedProduct.salePrice));
        } else {
          this.convertUnitDetail.salePrice = null;
        }
      }
      this.convertUnitDetail.checkUpdateConvertUnit = false;
      this.convertUnitDetail.directSale = true;
      this.convertUnitDetail.formula = 0;
      this.isConvertUnit = true;
    }
  }

  onSaveProduct() {
    this.selectedProduct.comId = this.lastCompany.id;
    if (this.selectedProduct.inventoryTracking == null) {
      this.selectedProduct.inventoryTracking = false;
    }
    if (!this.isConvertUnit) {
      if (this.selectedProduct.id) {
        this.service.updateProduct(this.selectedProduct).subscribe(value => {
          this.toastr.success(value.message[0].message, value.message[0].code);
          this.dismiss(value);
        });
      } else {
        this.selectedProduct.isTopping = false;
        this.service.createProduct(this.selectedProduct).subscribe(value => {
          this.toastr.success(value.message[0].message, value.message[0].code);
          this.dismiss(value);
        });
      }
    } else {
      this.toastr.error(
        this.translateService.instant('easyPos.product.info.isConvertUnitError'),
        this.translateService.instant('easyPos.product.info.message')
      );
    }
  }

  onCategorySelected($event: any) {
  }

  onChangeSalePrice() {
  }

  dismiss(value: any) {
    this.activeModal.close(value);
  }

  ngOnDestroy() {
  }
}
