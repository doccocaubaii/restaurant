import { AfterViewInit, Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ProductService } from '../product.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from '../../../shared/base/base.component';
import { Location } from '@angular/common';
import jsbarcode from 'jsbarcode';
import html2pdf from 'html2pdf.js';
import { PDFDocument, PDFPage } from 'pdf-lib';
import { ICON_CANCEL, ICON_EXCEL, ICON_PDF, ICON_SAVE, SYNC_ICON } from '../../../shared/other/icon';

@Component({
  selector: 'jhi-modal-preview-barcode',
  templateUrl: './modal-gen-pdf-barcode.component.html',
  styleUrls: ['./modal-gen-pdf-barcode.component.scss'],
})
export class ModalGenPdfBarcodeComponent extends BaseComponent implements OnInit, AfterViewInit {
  limitNumber = 2000;
  isLoading = true;
  companyName: any = '';
  paramCheckAll = false;
  totalPage: any = 0;
  totalBarcode: any = 0;
  ids: any[] = [];
  keyword = '';
  products: any[] = [];
  barcodeItems: any[] = [];
  showCompany = false;
  showProduct = false;
  showBarcode = false;
  showPrice = false;
  format: any;
  progressValue = 0;
  progressBar: any;
  percentageText: any;
  countAppear = 0;
  arrayBuffers: any[] = [];
  constructor(
    private service: ProductService,
    private toastr: ToastrService,
    public activeModal: NgbActiveModal,
    private translateService: TranslateService,
    private location: Location
  ) {
    super();
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  async ngOnInit() {
    for (const product of this.products) {
      this.totalBarcode += product.inventoryCount;
    }
    this.totalPage = this.totalBarcode / this.format.maxPiece;
    // await this.getProductList();
  }

  getBarcodeFormat() {
    this.barcodeItems = this.barcodeItems.sort((a, b) => a.position - b.position);

    for (const item of this.barcodeItems) {
      if (item.id == 'companyName') {
        this.showCompany = item.value;
        this.countAppear++;
      } else if (item.id == 'productName') {
        this.showProduct = item.value;
        this.countAppear++;
      } else if (item.id == 'barcode') {
        this.showBarcode = item.value;
      } else if (item.id == 'salePrice') {
        this.showPrice = item.value;
        this.countAppear++;
      }
    }
  }

  async mergePDFs(pdfBytesArray: any[]) {
    const mergedPdf = await PDFDocument.create();

    // Các mảng byte của các đoạn PDF cần gộp

    for (const pdfBytes of pdfBytesArray) {
      const pdf = await PDFDocument.load(pdfBytes);

      const pages = await mergedPdf.copyPages(pdf, pdf.getPageIndices());
      pages.forEach((page: PDFPage) => {
        mergedPdf.addPage(page);
      });
      if (this.progressValue < 100) {
        this.updateProgress(this.progressValue + 50 / pdfBytesArray.length);
      }
    }

    const mergedPdfBytes = await mergedPdf.save();

    // Tạo tệp tin PDF và tải xuống
    const blob = new Blob([mergedPdfBytes], { type: 'application/pdf' });
    const file = new File([blob], 'filename.pdf', { type: 'application/pdf' });
    const fileURL = URL.createObjectURL(file);
    window.open(fileURL, '_blank');
  }

  formatCurrency(number: any) {
    if (!number) {
      return '0 VNĐ';
    }
    return number
      .toLocaleString('vi-VN', {
        style: 'currency',
        currency: 'VND',
      })
      .replace('₫', 'VNĐ');
  }

  updateProgress(value: any) {
    this.progressValue = value;
    if (this.progressBar) {
      this.progressBar.setAttribute('style', `width: ${this.progressValue}%`);
    }
    if (this.percentageText) {
      this.percentageText.innerHTML = Math.ceil(this.progressValue) + '%';
    }
  }

  async ngAfterViewInit() {
    setTimeout(() => {
      this.printToPDF4();
    }, 100);
  }

  async printToPDF4() {
    this.progressBar = document.getElementById('progress');
    this.percentageText = document.getElementById('percentage');
    let pageTem = document.getElementById('data');
    const newValue = async () => {
      // for (const  value of array){
      let index = 0;
      this.getBarcodeFormat();
      let width = 100 / this.format.pieceNumber;
      let lastBuffer: any;
      // Duyệt danh sach product
      for (const product of this.products) {
        let oldRemain = false;
        if (pageTem && pageTem.children.length == this.format.maxPiece) {
          if (lastBuffer) {
            this.arrayBuffers.push(lastBuffer);
          }
          pageTem.innerHTML = '';
        }
        if (pageTem && pageTem.children.length > 0) {
          oldRemain = true;
        }
        // Duyet qua tung so luong san pham trong product
        for (let i = 0; i < product.inventoryCount; i++) {
          //  children : 1 children là 1 tem
          // format.pieceNumber: Số lượng tem trên 1 trang được cấu hiình
          // pageTem: là 1 trang tem
          // Kểm tra số luợng children trong 1 trang có bằng  Số lượng tem trên 1 trang được cấu hình không
          if (pageTem && pageTem.children.length == this.format.maxPiece && lastBuffer) {
            this.arrayBuffers.push(lastBuffer);
            if (this.progressValue < 100) {
              this.updateProgress(this.progressValue + 50 / this.totalPage);
            }
            // Kiểm tra xem sản phẩm trước có đang dư chỗ trống hay không thì xóa mẫu tem hiện tại để vẽ lại tem mới
            if (oldRemain) {
              oldRemain = false;
              if (pageTem) {
                pageTem.innerHTML = '';
              }
            } else {
              // Nếu tem thứ i + số lượng tem trong 1 trang nhỏ hơn tổng số tem thì tiếp tục in tem không thì vẽ mới
              if (i + this.format.maxPiece < product.inventoryCount) {
                i = i + this.format.maxPiece - 1;
                continue;
              } else {
                pageTem.innerHTML = '';
              }
            }
          }
          let span1: any;
          let span2: any;
          let span3: any;
          let canvas: any;

          if (this.showCompany) {
            span1 = document.createElement('span');
            span1.setAttribute('class', 'text-bold text-center');
            span1.setAttribute('style', `font-size: ${this.format.height * (this.getRate(this.companyName) / this.countAppear)}mm;`);
            span1.innerHTML = this.companyName;
          }
          if (this.showProduct) {
            span2 = document.createElement('span');
            span2.setAttribute('style', `font-size: ${this.format.height * (this.getRate(product.name) / this.countAppear)}mm`);
            span2.innerHTML = product.name;
            if (product.unit) {
              span2.innerHTML = span2.innerHTML + ' (' + product.unit + ')';
            }
          }
          if (this.showPrice) {
            let salePriceText = this.formatCurrency(product.salePrice);
            span3 = document.createElement('span');
            span3.setAttribute('class', 'text-bold');
            span3.setAttribute('style', `font-size: ${this.format.height * (this.getRate(salePriceText) / this.countAppear)}mm`);
            span3.innerHTML = salePriceText;
          }
          if (this.showBarcode) {
            canvas = document.createElement('canvas');
            canvas.getContext('2d').imageSmoothingEnabled = true;
            canvas?.setAttribute(
              'style',
              `width: 98%; border: none !important; height: ${this.format.height * (product.barcode.length > 10 ? 0.7 : 0.56)}mm`
            );
            jsbarcode(canvas, product.barcode, {
              format: 'CODE39',
              displayValue: true,
              height: product.barcode.length >= 10 ? 95 : product.barcode.length * 9,
            });
          }

          const div = document.createElement('div');
          div?.setAttribute('class', 'd-flex flex-column align-items-center' + (this.format.rows > 1 ? ' mb-2' : ''));
          div?.setAttribute('style', `width: ${width}%; height: ${this.format.height * 3.7}px; margin-bottom: 0.27px`);
          div.setAttribute('id', `div${index++}`);

          for (const item of this.barcodeItems) {
            if (item.id == 'companyName' && this.showCompany) {
              div?.appendChild(span1);
            } else if (item.id == 'productName' && this.showProduct) {
              div?.appendChild(span2);
            } else if (item.id == 'barcode' && this.showBarcode) {
              div?.appendChild(canvas);
            } else if (item.id == 'salePrice' && this.showPrice) {
              div?.appendChild(span3);
            }
          }
          pageTem?.appendChild(div);

          if (pageTem?.children.length == this.format.maxPiece) {
            try {
              const options = {
                html2canvas: {
                  scale: 5,
                },
                jsPDF: {
                  format: this.format.format,
                  orientation: this.format.orientation,
                  unit: 'mm',
                },
              };
              const html2pdfInstance = html2pdf().set(options);
              lastBuffer = await html2pdfInstance.from(pageTem).outputPdf('arraybuffer');
            } catch (error) {}
          }
        }
      }
    };
    await newValue();

    if (pageTem && pageTem.children.length > 0) {
      try {
        const options = {
          html2canvas: {
            scale: 5,
            useCORS: true,
          },
          jsPDF: {
            format: this.format.format,
            orientation: this.format.orientation,
            unit: 'mm',
          },
        };
        const html2pdfInstance = html2pdf().set(options);
        const buffer = await html2pdfInstance.from(pageTem).outputPdf('arraybuffer');
        this.arrayBuffers.push(buffer);
        if (this.progressValue < 100) {
          this.updateProgress(this.progressValue + 50 / this.totalPage);
        }
        if (pageTem) {
          pageTem.innerHTML = '';
        }
      } catch (error) {}
    }

    await this.mergePDFs(this.arrayBuffers);

    if (this.progressValue < 100) {
      this.updateProgress(100);
    }
    this.activeModal.close('done');
  }

  dismiss(value: any) {
    this.activeModal.close(value);
  }

  onClickContinue() {
    this.isLoading = true;
    setTimeout(() => {
      this.printToPDF4();
    }, 100);
  }

  getRate(text: any) {
    if (!text) {
      return 1;
    }
    let length = text.length;
    if (length <= 35) {
      return 0.3;
    } else {
      return 0.3 / (length / 24);
    }
  }

  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_EXCEL = ICON_EXCEL;
  protected readonly ICON_PDF = ICON_PDF;
  protected readonly SYNC_ICON = SYNC_ICON;
  protected readonly ICON_SAVE = ICON_SAVE;
}
