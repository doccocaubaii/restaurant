import { Component, ElementRef, OnInit, Renderer2, ViewChild } from '@angular/core';
import { ListProductToppingReq } from '../product';
import { ProductService } from '../product.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { AsyncSubject, debounceTime, distinctUntilChanged, Subject, switchMap, tap } from 'rxjs';
import { BaseComponent } from '../../../shared/base/base.component';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ModalCreateCategoryComponent } from '../modal-create-category/modal-create-category.component';
import { ModalConfirmDeleteComponent } from '../modal-confirm-delete/modal-confirm-delete.component';
import { ModalCreateUnitComponent } from '../modal-create-unit/modal-create-unit.component';
import { Location } from '@angular/common';
import { ConfirmInventoryTrackingComponent } from '../confirm-inventory-tracking/confirm-inventory-tracking.component';
import { last_config_device } from '../../../object-stores.constants';
import { ModalCreateProcessingAreaComponent } from '../../processing-area/modal-create-processing-area/modal-create-processing-area.component';
import { DataEncrypt } from '../../../entities/indexDatabase/data-encrypt.model';
import { ICON_CANCEL, ICON_DELETE_RED, ICON_PLUS, ICON_SAVE, ICON_SELECT_IMG } from '../../../shared/other/icon';

@Component({
  selector: 'jhi-modal-create-product',
  templateUrl: './modal-create-product.component.html',
  styleUrls: ['./modal-create-product.component.scss'],
})
export class ModalCreateProductComponent extends BaseComponent implements OnInit {
  @ViewChild('imageInput')
  imageInput!: ElementRef;

  @ViewChild('modalBody', { static: false })
  modalBody: ElementRef;
  @ViewChild('inventoryCount') inventoryCount: ElementRef;
  selectedProduct: any = {};
  id: any = 0;
  isVatRate: boolean = false;
  categories: any = [];

  lstUnit: any = [];
  unitName = '';
  private modalRef: NgbModalRef | undefined;
  lastCompany: any = {};
  checkInventoryTracking = false;
  configDevice: any = {};
  currentCount = 0;
  disableButton = false;
  type = 0;
  checkDropUnit = false;
  checkDropTopping = false;
  checkDropDiscountVatRate = false;
  taxReductionType: any = 0;
  lstVatRate = [
    {
      id: 'notVatRate',
      value: -4,
      title: 'Không áp dụng thuế',
    },
    {
      id: 'vatRate0',
      value: 0,
      title: '0%',
    },
    {
      id: 'vatRate5',
      value: 5,
      title: '5%',
    },
    {
      id: 'vatRate8',
      value: 8,
      title: '8%',
    },
    {
      id: 'vatRate10',
      value: 10,
      title: '10%',
    },
    {
      id: 'vatRateKct',
      value: -1,
      title: 'KCT',
    },
    {
      id: 'vatRateKtt',
      value: -2,
      title: 'KTT',
    },
    {
      id: 'vatRateK',
      value: -3,
      title: 'Khác',
    },
  ];

  listDiscountVatRate = [
    {
      id: 1,
      name: '1%',
    },
    {
      id: 2,
      name: '2%',
    },
    {
      id: 3,
      name: '3%',
    },
    {
      id: 5,
      name: '5%',
    },
  ];
  firstUnitId: any;
  listProductToppingReq: ListProductToppingReq;
  listProductTopping: any = [];
  listGroupTopping: any = [];
  productToppings: any = [];
  groupToppings: any = [];
  checkNullData = true;
  keywordToppings$ = new Subject<string>();
  listProcessingArea: any = [];
  filterProcessingArea: any = { page: 0, size: 100, active: 1, comId: null };

  isSalePriceZero: any = false;

  constructor(
    private service: ProductService,
    private toastr: ToastrService,
    private location: Location,
    private translateService: TranslateService,
    protected modalService: NgbModal,
    public activeModal: NgbActiveModal,
    private renderer: Renderer2
  ) {
    super();
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  async ngOnInit() {
    const subject = new AsyncSubject();
    this.lastCompany = await this.getCompany();
    this.searchToppings();
    this.getTaxReductionType();
    this.configDevice = await this.findByID(last_config_device, this.lastCompany.id);
    this.selectedProduct = {};
    this.selectedProduct.conversionUnits = [];
    this.listProductToppingReq = new ListProductToppingReq();
    this.listProductToppingReq.page = 0;
    this.listProductToppingReq.size = 20;
    this.listProductToppingReq.isSingleList = true;
    this.listProductToppingReq.id = this.id;
    this.filterProcessingArea.comId = this.lastCompany.id;
    this.getLstUnit();
    this.getCategory();
    this.getListProcessingArea(false);
    if (this.id) {
      this.onProductDetail(this.id);
    } else {
      this.selectedProduct.vatRate = -4;
    }
    await this.getListProductTopping();
  }

  onInputFile() {
    this.imageInput.nativeElement.click();
  }

  onImageSelected(event) {
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];
      const maxSizeInBytes = 2 * 1024 * 1024; // 2Mb

      // Kiểm tra kích thước tệp
      if (file.size > maxSizeInBytes) {
        this.toastr.error(
          this.translateService.instant('easyPos.product.info.imageError'),
          this.translateService.instant('easyPos.product.info.message')
        );
      } else {
        const reader = new FileReader();
        reader.onload = () => {
          this.selectedProduct.imageUrl = reader.result as string;
        };
        reader.readAsDataURL(file);
        this.selectedProduct.images = file;
      }
    }

    // Đặt giá trị của phần tử input thành rỗng để luôn gọi onImageSelected mỗi khi chọn lại hình ảnh
    event.target.value = '';
  }

  onProductDetail(id: any) {
    this.service.getProductById(id).subscribe(value => {
      this.selectedProduct = {};
      this.selectedProduct = {
        images: null,
        id: value.data.id,
        comId: value.data.comId,
        code: value.data.code,
        code2: value.data.code2,
        name: value.data.name,
        unit: value.data.unit,
        unitId: value.data.unitId,
        purchasePrice: value.data.purchasePrice,
        salePrice: value.data.salePrice,
        vatRate: value.data.vatRate,
        inventoryTracking: value.data.inventoryTracking,
        inventoryCount: value.data.inventoryCount,
        description: value.data.description,
        groupsObj: value.data.groups,
        groups: [],
        conversionUnits: [],
        imageUrl: value.data.imageUrl,
        barcode: value.data.barcode,
        discountVatRate: value.data.discountVatRate,
        productProductUnitId: value.data.productProductUnitId,
        directSale: value.data.directSale,
        isTopping: value.data.isTopping,
        toppings: value.data.toppings ? value.data.toppings : [],
        processingArea: value.data.processingArea ? value.data.processingArea.id : null,
      };
      this.firstUnitId = value.data.unitId;
      this.checkInventoryTracking = value.data.inventoryTracking;
      if (!this.selectedProduct.inventoryTracking) {
        this.selectedProduct.inventoryCount = null;
      }
      if (value.data.barcode) {
        this.currentCount = value.data.barcode.length;
      } else {
        this.currentCount = 0;
      }
      if (value.data.vatRate == null && value.data.vatRate == undefined) {
        this.selectedProduct.vatRate = -4;
      }
      value.data.conversionUnits.forEach(value => {
        if (!value.isPrimary) {
          this.selectedProduct.conversionUnits.push(value);
        }
      });
      this.selectedProduct.conversionUnits.forEach(value => {
        if (value.unitName) {
          value.checkUnit = true;
        }
        if (value.processingArea) {
          value.processingArea = value.processingArea.id;
        }
      });
      this.selectedProduct.toppings.forEach(value => {
        if (value.id) {
          if (value.isTopping) {
            this.productToppings.push(value.id);
          } else {
            this.groupToppings.push(value.id);
          }
        }
      });
      this.selectedProduct.groupsObj.forEach(value => {
        if (value.id) {
          this.selectedProduct.groups.push(value.id);
        }
      });
      this.isVatRate = JSON.parse(JSON.stringify(this.isVatRate));
      this.selectedProduct = JSON.parse(JSON.stringify(this.selectedProduct));
      this.productToppings = JSON.parse(JSON.stringify(this.productToppings));
      this.groupToppings = JSON.parse(JSON.stringify(this.groupToppings));
    });
  }

  getCategory() {
    this.service.getAllCategory().subscribe(value => {
      this.categories = value.data;
    });
  }

  onCreateCategory() {
    this.modalRef = this.modalService.open(ModalCreateCategoryComponent, { size: '', backdrop: 'static' });
    this.modalRef.closed.subscribe((res?: any) => {
      if (res) {
        if (!this.selectedProduct.groups) {
          this.selectedProduct.groups = [];
        }
        this.getCategory();
        this.selectedProduct.groups.push(res.data.id);
        this.selectedProduct.groups = JSON.parse(JSON.stringify(this.selectedProduct.groups));
      }
    });
  }

  onCheckboxChange() {
    const comId = this.lastCompany.id;
    if (this.checkInventoryTracking) {
      this.selectedProduct.inventoryTracking.disabled = true;
    } else {
      if (this.configDevice.inventoryTracking) {
        this.modalRef = this.modalService.open(ConfirmInventoryTrackingComponent, {
          size: 'dialog-centered',
          backdrop: 'static',
        });
        this.modalRef.closed.subscribe((res?: any) => {
          if (res) {
            setTimeout(() => {
              this.inventoryCount.nativeElement.focus();
            }, 100);
            this.configDevice.inventoryTracking = false;
            this.selectedProduct.inventoryTracking = true;

            if (res.isNotShowAgain) {
              this.updateById(
                last_config_device,
                comId,
                new DataEncrypt(comId, this.encryptFromData({ id: comId, inventoryTracking: false }))
              );
              // this.editByID(last_config_device, this.lastCompany.id, 'inventoryTracking', false);
            }
          } else {
            this.selectedProduct.inventoryTracking = false;
          }
        });
      } else {
        this.selectedProduct.inventoryTracking = !this.selectedProduct.inventoryTracking;
      }
    }
  }

  minus() {
    if (this.selectedProduct.inventoryCount) {
      if (this.selectedProduct.inventoryCount >= 1) {
        this.selectedProduct.inventoryCount = Number(this.selectedProduct.inventoryCount) - 1;
      } else if (this.selectedProduct.inventoryCount > 0 && this.selectedProduct.inventoryCount < 1) {
        this.selectedProduct.inventoryCount = 0;
      }
    }
  }

  plus() {
    if (this.selectedProduct.inventoryCount) {
      this.selectedProduct.inventoryCount = Number(this.selectedProduct.inventoryCount) + 1;
    } else {
      this.selectedProduct.inventoryCount = 1;
    }
  }

  onChangeUnit($event) {
    if ($event) {
      this.selectedProduct.unitId = $event.id;
      this.selectedProduct.unit = $event.name;
    } else {
      this.selectedProduct.unit = null;
      this.selectedProduct.unitId = this.firstUnitId;
    }
    this.disableUnit();
  }

  onCreateUnit(checkIsConvertUnit: any, i: number) {
    this.modalRef = this.modalService.open(ModalCreateUnitComponent, {
      size: '',
      backdrop: 'static',
    });
    this.modalRef.closed.subscribe((res?: any) => {
      if (res) {
        this.getLstUnit();
        if (!checkIsConvertUnit) {
          this.selectedProduct.unitId = res.data.id;
          this.selectedProduct.unit = res.data.name;
        } else {
          this.lstUnit.push(res.data);
          this.selectedProduct.conversionUnits[i].productUnitId = res.data.id;
          this.selectedProduct.conversionUnits[i].unitName = res.data.name;
          this.getDescConvertUnit(this.selectedProduct.conversionUnits[i]);
          Object.assign({}, this.selectedProduct.conversionUnits);
        }
      }
    });
  }

  deleteProductConversionUnit(convertUnit: any) {
    this.modalRef = this.modalService.open(ModalConfirmDeleteComponent, {
      size: 'dialog-centered',
      backdrop: 'static',
    });
    this.modalRef.componentInstance.title = this.translateService.instant('easyPos.product.info.deleteConvertUnitMessage');
    this.modalRef.closed.subscribe((res?: any) => {
      if (res === 1) {
        if (convertUnit.id) {
          this.service.deleteProductConversionUnit(convertUnit.id, this.lastCompany.id).subscribe(value => {
            this.selectedProduct.conversionUnits = this.selectedProduct.conversionUnits.filter(item => item.id !== convertUnit.id);
            this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
          });
        } else {
          this.selectedProduct.conversionUnits = this.selectedProduct.conversionUnits.filter(
            item => item.unitName !== convertUnit.unitName
          );
          this.toastr.success(
            this.translateService.instant('easyPos.product.info.deleteConvertUnitMess'),
            this.translateService.instant('easyPos.product.info.message')
          );
        }
      }
    });
  }

  onCreateConversionUnit() {
    if (!this.selectedProduct.unitId) {
      this.toastr.error(
        this.translateService.instant('easyPos.product.info.unitNull'),
        this.translateService.instant('easyPos.product.info.message')
      );
    } else if (this.validateConversionUnit()) {
      return;
    } else {
      let newConvertUnit = {
        directSale: true,
        formula: 0,
        convertRate: null,
        productUnitId: null,
        salePrice: null,
        unitName: null,
      };
      this.selectedProduct.conversionUnits.push(newConvertUnit);
      this.scrollToBottom();
    }
    this.disableUnit();
  }

  disableUnit() {
    this.lstUnit.forEach(value => {
      value.disabled = false;
    });
    let check1 = this.lstUnit.find(unitName => unitName.name === this.selectedProduct.unit);
    let check2 = this.lstUnit.filter(({ name }) => this.selectedProduct.conversionUnits.some(({ unitName }) => name === unitName));
    if (check1) {
      check1.disabled = true;
    }
    if (check2.length > 0) {
      check2.forEach(value => {
        value.disabled = true;
      });
    }
    this.lstUnit = JSON.parse(JSON.stringify(this.lstUnit));
  }

  onChangeUnitConvert($event, convertUnit) {
    convertUnit.productUnitId = $event.id;
    convertUnit.unitName = $event.name;
    this.getDescConvertUnit(convertUnit);
  }

  onChangeConvertRate(convertUnit: any) {
    if (convertUnit.convertRate !== null && convertUnit.convertRate !== undefined) {
      if (this.selectedProduct.salePrice) {
        convertUnit.salePrice = this.selectedProduct.salePrice * convertUnit.convertRate;
      }
      this.getDescConvertUnit(convertUnit);
    } else {
      convertUnit.salePrice = 0;
    }
  }

  getDescConvertUnit(convertUnit: any) {
    if (!convertUnit.unitName) {
      convertUnit.unitName = '';
    }
    if (convertUnit.convertRate == undefined) {
      convertUnit.description = '1 ' + convertUnit.unitName + ' = ' + ' ' + this.selectedProduct.unit;
    } else {
      convertUnit.description = '1 ' + convertUnit.unitName + ' = ' + convertUnit.convertRate + ' ' + this.selectedProduct.unit;
    }
  }

  getLstUnit() {
    this.service.getProductUnit().subscribe(value => {
      this.lstUnit = value.data;
    });
  }

  onSaveProduct() {
    return new Promise((resolve, reject) => {
      this.disableButton = JSON.parse(JSON.stringify(this.disableButton));
      this.selectedProduct.comId = this.lastCompany.id;
      if (this.validateConversionUnit()) {
        this.toastr.error('Lỗi: Đơn vị chuyển đổi không hợp lệ');
        return;
      }
      if (!this.selectedProduct.name || this.selectedProduct.name.trim().length === 0) {
        this.toastr.error(
          this.translateService.instant('easyPos.product.info.productNameError'),
          this.translateService.instant('easyPos.product.info.message')
        );
        return;
      }
      if (this.selectedProduct.salePrice === null || this.selectedProduct.salePrice === undefined) {
        this.toastr.error(
          this.translateService.instant('easyPos.product.info.salePriceError'),
          this.translateService.instant('easyPos.product.info.message')
        );
        return;
      }
      if (this.selectedProduct.inventoryTracking) {
        if (this.selectedProduct.inventoryCount === null || this.selectedProduct.inventoryCount === undefined) {
          this.toastr.error(
            this.translateService.instant('easyPos.product.info.inventoryCountError'),
            this.translateService.instant('easyPos.product.info.message')
          );
          return;
        }
        if (this.selectedProduct.purchasePrice === null || this.selectedProduct.purchasePrice === undefined) {
          this.toastr.error(
            this.translateService.instant('easyPos.product.info.purchasePriceError'),
            this.translateService.instant('easyPos.product.info.message')
          );
          return;
        }
      }
      if (this.selectedProduct.salePrice === 0 && !this.isSalePriceZero) {
        this.modalRef = this.modalService.open(ModalConfirmDeleteComponent, {
          size: 'dialog-centered',
          backdrop: 'static',
        });
        this.modalRef.componentInstance.title = 'Sản phẩm có giá bán = 0. Bạn có muốn tiếp tục lưu không?';
        this.modalRef.componentInstance.isBlueButton = true;

        this.modalRef.closed.subscribe((res?: any) => {
          if (res === 1) {
            this.isSalePriceZero = true;
            resolve(this.onSave());
          }
          // in case user rejects, we "reject" the promise
          else {
            reject();
          }
        });
      } else {
        // Only resolve here if salePrice is not 0 or isSalePriceZero is true
        resolve(this.onSave());
      }
    });
  }

  onSave() {
    return new Promise((resolve, reject) => {
      if (this.selectedProduct.vatRate == null || this.selectedProduct.vatRate == undefined) {
        this.selectedProduct.vatRate = -4;
      }
      if (this.selectedProduct.id) {
        this.getToppings();
        this.disableButton = true;
        this.service.updateProduct(this.selectedProduct).subscribe(
          value => {
            this.disableButton = false;
            this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
            this.dismiss(value);
            resolve(value);
            this.disableButton = false;
          },
          error => {
            this.disableButton = false;
            reject(error);
          }
        );
      } else {
        this.getToppings();
        this.service.createProduct(this.selectedProduct).subscribe(
          value => {
            this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
            this.dismiss(value);
            resolve(value);
            this.disableButton = false;
          },
          error => {
            this.disableButton = false;
            reject(error);
          }
        );
      }
    });
  }

  onCategorySelected($event: any) {}

  onChangeSalePrice() {}

  dismiss(value: any) {
    this.activeModal.close(value);
  }

  ngOnDestroy() {}

  onCountInputChar() {
    this.currentCount = this.selectedProduct.barcode.length;
  }

  onSelectTab(event: any) {
    this.type = event;
  }

  dropdownUnit() {
    this.checkDropUnit = !this.checkDropUnit;
    if (this.checkDropUnit) {
      this.scrollToBottom(); // thêm dòng này để gọi scrollToBottom()
    }
    // this.scrollToTargets()
  }

  dropdownTopping() {
    this.checkDropTopping = !this.checkDropTopping;
    this.scrollToBottom(); // thêm dòng này để gọi scrollToBottom()
  }
  dropdownDiscountVatRate() {
    this.checkDropDiscountVatRate = !this.checkDropDiscountVatRate;
    this.scrollToBottom(); // thêm dòng này để gọi scrollToBottom()
  }
  scrollToTargets(): void {
    const targetElements = document.querySelector('#formUnitInfor');
    // targetElements.forEach(el => {
    targetElements && targetElements.scrollIntoView({ behavior: 'smooth', block: 'center' });
    // });
  }

  scrollToBottom() {
    setTimeout(() => {
      // this.modalBody.nativeElement.scrollTop = this.modalBody.nativeElement.scrollHeight;
      this.scrollToTargets();
    }, 0);
  }

  validateConversionUnit(): any {
    for (let value of this.selectedProduct.conversionUnits) {
      if (!value.unitName || value.unitName.trim().length == 0) {
        this.toastr.error(
          this.translateService.instant('easyPos.product.info.unitNameError'),
          this.translateService.instant('easyPos.product.info.message')
        );
        return true;
      } else if (!value.convertRate) {
        this.toastr.error(
          this.translateService.instant('easyPos.product.info.vaterateError'),
          this.translateService.instant('easyPos.product.info.message')
        );
        return true;
      } else if (!value.salePrice && value.salePrice !== 0) {
        this.toastr.error(
          this.translateService.instant('easyPos.product.info.salePriceError'),
          this.translateService.instant('easyPos.product.info.message')
        );
        return true;
      }
    }
    return false;
  }

  getListProductTopping() {
    return new Promise((resolve, reject) => {
      this.service.getListProductTopping(this.listProductToppingReq).subscribe(
        result => {
          this.listProductTopping = result.body.data.productToppings;
          this.listGroupTopping = result.body.data.groupToppings;
          resolve(result);
        },
        error => {
          reject(error);
        }
      );
    });
  }

  getProductToppingSelected(): any[] {
    return this.listProductTopping.filter(product => this.productToppings.includes(product.id));
  }
  getGroupToppingSelected(): any[] {
    return this.listGroupTopping.filter(product => this.groupToppings.includes(product.id));
  }
  getToppings() {
    this.selectedProduct.toppings = this.getGroupToppingSelected().concat(this.getProductToppingSelected());
  }
  loadMoreToppings() {
    if (this.checkNullData) {
      this.listProductToppingReq.page++;
      this.service.getListProductTopping(this.listProductToppingReq).subscribe(
        result => {
          if (result.body.data.productToppings?.length == 0 || result.body.data.groupToppings?.length == 0) {
            this.checkNullData = false;
          }
          this.listProductTopping.push(...result.body.data.productToppings);
          this.listGroupTopping.push(...result.body.data.groupToppings);
        },
        error => {}
      );
    }
  }

  searchToppings() {
    this.keywordToppings$
      .pipe(
        debounceTime(600),
        distinctUntilChanged(),
        tap(keyword => {
          if (keyword) {
            this.listProductToppingReq.keyword = keyword;
            this.listProductToppingReq.page = 0;
          } else {
            this.listProductToppingReq.keyword = '';
          }
        }),
        switchMap(() => this.service.getListProductTopping(this.listProductToppingReq))
      )
      .subscribe(res => {
        this.listProductTopping = res.body.data.productToppings;
        this.listGroupTopping = res.body.data.groupToppings;
      });
  }
  getTaxReductionType() {
    this.service.getTaxReductionType(this.lastCompany.id).subscribe(value => {
      this.taxReductionType = value.data.taxReductionType;
    });
  }
  getListProcessingArea(checkScroll: any) {
    this.service.getProcessingArea(this.filterProcessingArea).subscribe(value => {
      if (!checkScroll) {
        this.listProcessingArea = value.body.data;
      } else {
        if (value.body.data) {
          value.body.data.forEach(value => {
            this.listProcessingArea.push(value);
          });
        }
      }
      this.listProcessingArea = JSON.parse(JSON.stringify(this.listProcessingArea));
    });
  }
  loadMoreProcessingArea() {
    this.filterProcessingArea.page += 1;
    this.getListProcessingArea(true);
  }
  async onCreateProcessingArea() {
    this.modalRef = this.modalService.open(ModalCreateProcessingAreaComponent, { size: 'xl', backdrop: 'static' });
    this.modalRef.componentInstance.id = 0;
    this.modalRef.componentInstance.idProcessingArea = JSON.parse(JSON.stringify(0));
    this.modalRef.closed.subscribe((res?: any) => {
      this.getListProcessingArea(false);
      this.selectedProduct.processingArea = res.data.processingArea.id;
    });
  }

  protected readonly ICON_SAVE = ICON_SAVE;
  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SELECT_IMG = ICON_SELECT_IMG;
  protected readonly ICON_DELETE_RED = ICON_DELETE_RED;
  protected readonly ICON_PLUS = ICON_PLUS;
}
