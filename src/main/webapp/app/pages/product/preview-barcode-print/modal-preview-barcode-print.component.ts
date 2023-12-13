import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ProductService } from '../product.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from '../../../shared/base/base.component';
import { Location } from '@angular/common';
import {
  ICON_CANCEL,
  ICON_CARD_DEFAULT,
  ICON_CHECK,
  ICON_EXCEL,
  ICON_PDF,
  ICON_SAVE,
  ICON_SORT_BARCODE,
  ICON_SORT_PAGE_ITEM,
  SYNC_ICON,
} from '../../../shared/other/icon';
import html2canvas from 'html2canvas';
import html2pdf from 'html2pdf.js';
import jsbarcode from 'jsbarcode';
import { jsPDF } from 'jspdf';
import * as htmlToImage from 'html-to-image';
import domtoimage from 'dom-to-image';
import { ModalGenPdfBarcodeComponent } from '../modal-gen-pdf-barcode/modal-gen-pdf-barcode.component';
import { ExportCommon } from '../../export/export.common';
import dayjs from 'dayjs';

@Component({
  selector: 'jhi-modal-preview-barcode',
  templateUrl: './modal-preview-barcode-print.component.html',
  styleUrls: ['./modal-preview-barcode-print.component.scss'],
})
export class ModalPreviewBarcodePrintComponent extends BaseComponent implements OnInit {
  lastCompany: any;
  paramCheckAll = false;
  ids: any[] = [];
  listSelected: any[] = [];
  keyword = '';
  page = 1;
  size = 20;
  totalItems = 0;
  isBegin = true;
  isSort = false;
  isSavingPdf = false;
  private modalRef: NgbModalRef | undefined;
  constructor(
    private service: ProductService,
    private toast: ToastrService,
    public activeModal: NgbActiveModal,
    private translateService: TranslateService,
    protected modalService: NgbModal,
    public exportComponent: ExportCommon,
    private location: Location
  ) {
    super();
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  products: any[] = [];
  transerProducts: any[] = [];
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

  barcodeItems: any[] = [
    {
      id: 'companyName',
      text: 'Tên cửa hàng',
      value: 1,
      position: 1,
    },
    {
      id: 'productName',
      text: 'Tên sản phẩm',
      value: 1,
      position: 2,
    },
    {
      id: 'barcode',
      text: 'Mã vạch',
      value: 1,
      position: 3,
    },
    {
      id: 'salePrice',
      text: 'Giá bán',
      value: 1,
      position: 4,
    },
  ];

  pageSizes = [
    {
      id: 'page-size-1',
      image: 'assets/img/barcodeBase/Base72x22.jpg',
      text: 'Cuộn 2 tem (khổ 72x22 mm)',
      value: 1, // 1: được chọn
      format: {
        format: [22, 72], // kích thước trang pdf
        width: 36, // chiều dài 1 tem
        height: 22, // chiều cao 1 tem
        pieceNumber: 2, // số tem trên 1 hàng
        maxPiece: 2, // số tem tối đa trên 1 trang
        rows: 1, // số hàng tối đa trên 1 trang
        orientation: 'landscape', // landscape: trang ngang, portrait: trang dọc
      },
    },
    {
      id: 'page-size-2',
      image: 'assets/img/barcodeBase/Base104x22.jpg',
      text: 'Mẫu giấy cuộn 3 nhãn (khổ 104x22mm)',
      value: 0,
      format: {
        format: [22, 104],
        width: 52,
        height: 22,
        pieceNumber: 3,
        maxPiece: 3,
        rows: 1,
        orientation: 'landscape',
      },
    },
    {
      id: 'page-size-3',
      image: 'assets/img/barcodeBase/BaseTomy145.jpg',
      text: 'Khổ giấy in nhãn A4 - Tomy 145',
      value: 0,
      format: {
        format: [297, 210],
        width: 38,
        height: 21,
        pieceNumber: 5,
        maxPiece: 65,
        rows: 13,
        orientation: 'portrait',
      },
    },
  ];

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    await this.getProductList();
    this.listSelected = [];
    this.genTemplateBarcode();
  }

  genTemplateBarcode() {
    const barcodeValue = '0123456789';
    const canvas = document.getElementById('template-barcode');
    jsbarcode(canvas, barcodeValue, {
      format: 'CODE39',
      displayValue: true,
    });
  }

  onChangeSwitch(pageSize: any) {
    if (pageSize && pageSize.id == 'barcode' && pageSize.value == 1) {
      this.genTemplateBarcode();
    }
  }

  async getProductList() {
    let req: any = {
      page: this.page - 1,
      size: this.size,
      keyword: this.keyword,
      paramCheckAll: this.paramCheckAll,
      isProductId: this.isBegin,
      ids: this.isBegin ? this.listSelected : this.ids,
    };
    if (this.isBegin) {
      req.page = null;
      req.size = null;
    }
    this.service.getWithPagingBarcode(req).subscribe(res => {
      this.products = res.body.data;
      this.totalItems = res.body.count;
      if (this.isBegin) {
        if (!this.paramCheckAll) {
          for (const item of this.products) {
            this.ids.push(item.productProductUnitId);
          }
        }
        this.products = this.products.slice(0, this.size);
        this.transerProducts = this.products;
        this.isBegin = false;
      } else {
        for (const product of this.products) {
          let fixedProduct = this.transerProducts.find(item => item.productProductUnitId == product.productProductUnitId);
          if (fixedProduct) {
            product.inventoryCount = fixedProduct.inventoryCount;
            product.salePrice = fixedProduct.salePrice;
          }
        }
      }
    });
  }

  onChangeSalePrice(p: any) {
    let product = this.transerProducts.find(item => item.productProductUnitId == p.productProductUnitId);
    if (product) {
      product.salePrice = p.salePrice;
    }
  }

  onChangeInventory(p: any) {
    let product = this.transerProducts.find(item => item.productProductUnitId == p.productProductUnitId);
    if (product) {
      product.inventoryCount = 0 - p.inventoryCount;
    }
  }

  dismiss(value: any) {
    this.activeModal.close(value);
  }

  onClickNext() {
    console.log(this.products);
  }

  async onClickDelete(data: any) {
    this.products = this.products.filter(item => item.productProductUnitId !== data.productProductUnitId);
    this.transerProducts = this.transerProducts.filter(item => item.productProductUnitId !== data.productProductUnitId);
    this.totalItems = this.totalItems - 1;
    if (this.products && this.products.length == this.size - 1) {
      let req = {
        page: this.page,
        size: this.size,
        keyword: this.keyword,
        paramCheckAll: this.paramCheckAll,
        isProductId: false,
        ids: this.ids,
      };
      this.service.getWithPagingBarcode(req).subscribe(res => {
        let data = res.body.data;
        if (data && data.length > 0) {
          this.products.push(data[0]);
        }
      });
    }
    if (this.paramCheckAll) {
      this.ids.push(data.productProductUnitId);
    } else {
      this.ids = this.ids.filter(item => item !== data.productProductUnitId);
    }
  }

  async loadMore($event: any) {
    this.page = $event;
    await this.getProductList();
  }

  onSearch() {
    if (!this.transerProducts || this.transerProducts.length == 0) {
      return;
    }
    this.page = 1;
    this.getProductList();
  }

  onDeleteKeywordSearch() {
    if (!this.keyword) {
      this.onSearch();
    }
  }

  changePageSize(pageSize: any) {
    for (const page of this.pageSizes) {
      if (page.value == 1 && page.id != pageSize.id) {
        page.value = 0;
      }
      if (page.id == pageSize.id) {
        page.value = 1;
      }
    }
  }

  minus(product: any) {
    if (product.inventoryCount) {
      if (product.inventoryCount >= 1) {
        product.inventoryCount = Number(product.inventoryCount) - 1;
      } else if (product.inventoryCount > 0 && product.inventoryCount < 1) {
        product.inventoryCount = 0;
      }
    }
    let p = this.transerProducts.find(item => item.productProductUnitId == product.productProductUnitId);
    if (p) {
      p.inventoryCount = product.inventoryCount;
    }
  }

  plus(product: any) {
    if (product.inventoryCount) {
      product.inventoryCount = Number(product.inventoryCount) + 1;
    } else {
      product.inventoryCount = 1;
    }
    let p = this.transerProducts.find(item => item.productProductUnitId == product.productProductUnitId);
    if (p) {
      p.inventoryCount = product.inventoryCount;
    }
  }

  openPdfInNewTab(buffer: any) {
    const blob = new Blob([buffer], { type: 'application/pdf' });
    const file = new File([blob], 'filename.pdf', { type: 'application/pdf' });
    const fileURL = URL.createObjectURL(file);
    window.open(fileURL, '_blank');
  }

  getBarCodeItem(id: any) {
    let barcodeItem = this.barcodeItems.find(item => item.id == id);
    if (barcodeItem) {
      return barcodeItem.value;
    }
    return 0;
  }

  getBarCodeItemPosition(id: any) {
    let barcodeItem = this.barcodeItems.find(item => item.id == id);
    if (barcodeItem) {
      return barcodeItem.position;
    }
    return 10;
  }

  onDrop(event: any) {
    const previousIndex = event.previousIndex;
    const currentIndex = event.currentIndex;

    if (previousIndex !== currentIndex) {
      const item = this.barcodeItems.splice(previousIndex, 1)[0];
      this.barcodeItems.splice(currentIndex, 0, item);
    }

    for (let i = 0; i < this.barcodeItems.length; i++) {
      let item = this.barcodeItems[i];
      item.position = i + 1;
    }
  }

  async printToPDF() {
    this.isSavingPdf = true;
    if (!this.transerProducts || this.transerProducts.length == 0) {
      this.toast.error('Danh sách sản phẩm không được để trống', 'Thông báo');
      this.isSavingPdf = false;
      return;
    }
    let totalItem = 0;
    for (const product of this.transerProducts) {
      totalItem += product.inventoryCount;
    }
    if (totalItem > 10000) {
      this.toast.error('Đã vượt quá 10.000 tem, không thể in', 'Thông báo');
      this.isSavingPdf = false;
      return;
    }
    let check = false;
    for (const item of this.barcodeItems) {
      if (item.value == 1) {
        check = true;
        break;
      }
    }
    if (!check) {
      this.toast.error('Mẫu in không thể để trống', 'Thông báo');
      this.isSavingPdf = false;
      return;
    }
    this.modalRef = this.modalService.open(ModalGenPdfBarcodeComponent, {
      size: 'barcode-modal',
      backdrop: 'static',
    });
    let format: any;
    for (const pageSize of this.pageSizes) {
      if (pageSize.value == 1) {
        format = pageSize.format;
        break;
      }
    }
    this.modalRef.componentInstance.format = format;
    this.modalRef.componentInstance.companyName = this.lastCompany.name;
    this.modalRef.componentInstance.pageSizes = this.pageSizes;
    this.modalRef.componentInstance.products = this.transerProducts;
    this.modalRef.componentInstance.barcodeItems = this.barcodeItems;
    this.modalRef.closed.subscribe((res?: any) => {
      this.isSavingPdf = false;
    });
  }

  onExportExcel() {
    if (!this.transerProducts || this.transerProducts.length == 0) {
      this.toast.error('Danh sách sản phẩm không được để trống', 'Thông báo');
      return;
    }
    if (this.transerProducts?.length > 0) {
      this.service.exportBarcodeExcel(this.transerProducts).subscribe(
        value => {
          this.exportComponent.saveExcelFromByteArray(value.body, 'BangGiaSanPham_' + dayjs().unix());
        },
        error => {
          console.log(error);
        }
      );
    }
  }

  protected readonly ICON_SAVE = ICON_SAVE;
  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_EXCEL = ICON_EXCEL;
  protected readonly ICON_PDF = ICON_PDF;
  protected readonly ICON_SORT_PAGE_ITEM = ICON_SORT_PAGE_ITEM;
  protected readonly ICON_CHECK = ICON_CHECK;
  protected readonly SYNC_ICON = SYNC_ICON;
  protected readonly ICON_CARD_DEFAULT = ICON_CARD_DEFAULT;
  protected readonly ICON_SORT_BARCODE = ICON_SORT_BARCODE;
}
