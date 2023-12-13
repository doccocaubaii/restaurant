import { Location } from '@angular/common';
import { Component, HostListener, Input, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from '../../../../shared/base/base.component';
import * as QRCode from 'qrcode';
import { ConfigService } from '../../../../layouts/config/service/config.service';
import { CkeditorPrintConfigService } from '../../../../layouts/modal/ckeditor-print-config/ckeditor-print-config.service';
import moment from 'moment';
import { BillService } from '../../service/bill.service';
import { PrintService } from '../../service/print.service';
import html2pdf from 'html2pdf.js';
import { ICON_CANCEL, ICON_PDF } from '../../../../shared/other/icon';

@Component({
  selector: 'jhi-pos-invoice',
  templateUrl: './pos-invoice.component.html',
  styleUrls: ['./pos-invoice.component.scss'],
})
export class PosInvoiceComponent extends BaseComponent implements OnInit {
  @Input() orderSelected?: any = {};
  @Input() orderDataPrint?: any = {};
  @Input() isHide?: boolean = false;
  @Input() type: number = 0;
  statusPrint = false;
  @Input() printConfigs: any;
  urlQR: any;
  lastCompany: any;
  isDefault = false;
  reviewContent: any;
  json: any;
  printConfigMap = new Map<string, string>();
  templateSelected: any;
  isSavingPdf: any = false;
  isPrint: any = false;
  isOpenNewTap: any = false;

  constructor(
    public activeModal: NgbActiveModal,
    protected modalService: NgbModal,
    private configService: ConfigService,
    private location: Location,
    private service: CkeditorPrintConfigService,
    private printService: PrintService,
    protected orderService: BillService
  ) {
    super();
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  async ngOnInit() {
    window.addEventListener('beforeprint', () => {
      this.closeModal();
    });

    window.addEventListener('afterprint', () => {
      this.closeModal();
    });
    if (!this.orderSelected) {
      this.isDefault = true;
    } else {
      this.isDefault = false;
    }
    this.lastCompany = await this.getCompany();
    if (!this.printConfigs || !this.printConfigs.length) {
      this.printConfigs = [];
      this.getAllPrintConfig();
    } else {
      this.printConfigs.forEach(item => {
        item.value.isBoldItem = item.value.isBold;
        item.value.isPrintItem = item.value.isPrint;
      });
    }
    this.service.getAllPrintConfig().subscribe(value => {
      if (this.orderSelected.id) {
        this.orderService.getDataPrint(this.orderSelected.id, this.lastCompany.id).subscribe(obj => {
          this.orderDataPrint = obj.body.data;
          const matchingConfig = value.data.find(config => {
            if (this.type === config.typeTemplate) {
              return true;
            }
            return false;
          });
          if (matchingConfig) {
            this.templateSelected = matchingConfig;
            this.reviewContent = matchingConfig.content;
            this.json = JSON.parse(matchingConfig.contentParams);
          }
          this.updateContent();
        });
      }
    });
  }

  updateContent() {
    const configs = this.json['json-value'];
    let tableName1: any = null;
    let tableName2: any = null;
    configs.forEach(config => {
      if (config.tableName && config.isTable && !config.isTopping) {
        tableName1 = config.tableName;
      }
      if (config.tableName && config.isTable && config.isTopping) {
        tableName2 = config.tableName;
      }
      if (!config.isTable && !config.tableName) {
        const value = this.orderDataPrint[config.columnName];
        if (config.variable) {
          if (config.columnName === 'qrCode') {
            this.genQrCode(value);
            this.reviewContent = this.reviewContent.replace(new RegExp(config.variable, 'g'), this.urlQR);
          }
          if (value !== null && value !== undefined) {
            if (config.isDate && this.reviewContent.includes(config.variable)) {
              this.reviewContent = this.replaceContent(this.reviewContent, config.variable, this.getDateFormat(config.variable, value));
            }
            this.reviewContent = this.replaceContent(this.reviewContent, config.variable, value);
          } else {
            this.reviewContent = this.replaceContent(this.reviewContent, config.variable, '');
          }
        }
      }
      if (!config.isTable && config.tableName) {
        if (this.orderDataPrint[config.tableName]) {
          const value = this.orderDataPrint[config.tableName][config.columnName];
          if (value !== null && value !== undefined) {
            this.reviewContent = this.replaceContent(this.reviewContent, config.variable, value);
          } else {
            this.reviewContent = this.replaceContent(this.reviewContent, config.variable, '');
          }
        } else {
          let htmlReplace = this.getContent(config.variable, this.reviewContent, '<tr', '</tr>');
          if (htmlReplace) {
            this.reviewContent = this.reviewContent.replace(htmlReplace, '');
          }
        }
      }
    });
    if (this.reviewContent.includes('{Ten_san_pham}')) {
      let bodyContent = this.getContent('{Ten_san_pham}', this.reviewContent, '<tbody>', '</tbody>');
      let contentSpliceBody = bodyContent.substring(bodyContent.indexOf('<tbody>') + 7, bodyContent.indexOf('</tbody>'));
      let rawContent = this.getContentParent('{Ten_san_pham}', contentSpliceBody, '<tr');
      let content = rawContent;
      if (this.orderDataPrint[tableName1]) {
        this.orderDataPrint[tableName1].forEach((value, index) => {
          if (content.includes('{San_pham_ban_kem}')) {
            let startToppingName = content.indexOf('{San_pham_ban_kem}');
            let startTopping1 = content.lastIndexOf('<tr', startToppingName);
            let startTopping2 = content.indexOf('</tr>', startToppingName) + '</tr>'.length;
            let toppingText = content.slice(startTopping1, startTopping2);
            let rawTopping = content.slice(startTopping1, startTopping2);

            if (value[tableName2]) {
              let discountVatRateReplace = this.getContent('{Thue_giam_tru}', content, '<tr', '</tr>');
              if (discountVatRateReplace) {
                content = content.replace(new RegExp(discountVatRateReplace, 'g'), '');
              }
              value[tableName2].forEach((topping, indexTopping) => {
                configs.forEach(config => {
                  if (config.isTable && config.isTopping) {
                    if (content.includes(config.variable)) {
                      const data = topping[config.columnName];
                      if (data !== null && data !== undefined) {
                        toppingText = this.replaceContent(toppingText, config.variable, data);
                      } else {
                        toppingText = this.replaceContent(toppingText, config.variable, '');
                      }
                    }
                  }
                });
                if (indexTopping < value[tableName2].length - 1) {
                  toppingText += rawTopping;
                }
              });
              content = content.replace(rawTopping, toppingText);
            } else {
              content = content.replace(rawTopping, '');
            }
          }
          configs.forEach(config => {
            if (config.isTable && !config.isTopping) {
              if (content.includes(config.variable)) {
                const data = value[config.columnName];
                if (data != null && data != undefined) {
                  content = this.replaceContent(content, config.variable, data);
                } else {
                  content = this.replaceContent(content, config.variable, '');
                }
              }
            }
          });
          if (index < this.orderDataPrint[tableName1].length - 1) {
            content += rawContent;
          }
        });
      } else {
        this.reviewContent = this.reviewContent.replace(rawContent, '');
      }
      this.reviewContent = this.reviewContent.replace(rawContent, content);
    }
  }

  replaceContent(content, variable, value) {
    if (content && variable) {
      if (typeof value === 'number') {
        if (
          value === 0 &&
          variable !== '{Thue_suat}' &&
          variable !== '{Thue_giam_tru}' &&
          variable !== '{Gia_san_pham}' &&
          variable !== '{Thanh_tien}' &&
          variable !== '{Tong_tien_san_pham}'
        ) {
          let htmlReplace = this.getContent(variable, content, '<tr', '</tr>');
          if (htmlReplace) {
            return content.replace(htmlReplace, '');
          } else {
            return content.replace(new RegExp(variable, 'g'), value);
          }
        } else {
          const formattedValue = value.toLocaleString();
          return content.replace(new RegExp(variable, 'g'), formattedValue);
        }
      } else if (
        (value === null || value === undefined || value === '') &&
        variable !== '{Don_vi}' &&
        variable !== '{Don_vi_topping}' &&
        variable !== '{Thue_giam_tru}'
      ) {
        let htmlReplace = this.getContent(variable, content, '<tr', '</tr>');
        if (htmlReplace) {
          return content.replace(htmlReplace, '');
        } else {
          return content.replace(new RegExp(variable, 'g'), value);
        }
      } else {
        return content.replace(new RegExp(variable, 'g'), value);
      }
    }
  }

  spliceContent(content: any, start: any, end: any): any {
    let textStart = content.indexOf(start) + start.length;
    let textEnd = content.indexOf(end);
    return content.slice(textStart, textEnd).trim();
  }

  getContent(value: any, content: any, start: any, end: any): any {
    if (value && content) {
      if (content.includes(value)) {
        let startContent = content.indexOf(value);
        let start1 = content.lastIndexOf(start, startContent);
        let end2 = content.indexOf(end, startContent) + end.length;
        return content.slice(start1, end2);
      }
    }
    return null;
  }

  getContentParent(value1: any, content: any, start: any): any {
    if (value1) {
      let startContent1 = content.indexOf(value1);
      let start1 = content.lastIndexOf(start, startContent1);
      let end2 = content.length;

      return content.slice(start1, end2);
    }
    return null;
  }
  formatDate(date: any): any {
    return moment(date, 'YYYY-MM-DD HH:mm:ss').format('DD/MM/YYYY HH:mm:ss');
  }

  getAllPrintConfig() {
    this.configService.getAllPrintConfig(this.lastCompany.id, { type: -1 }).subscribe(res => {
      this.printConfigs = res.body.data;
      this.printConfigs.forEach(item => {
        item.value.isBoldItem = item.value.isBold;
        item.value.isPrintItem = item.value.isPrint;
        this.printConfigMap.set(item.value.code, item.value.content);
      });
    });
  }

  closeModal() {
    this.activeModal.close();
  }
  printInvoid() {
    this.isPrint = true;
    this.statusPrint = !this.statusPrint;

    const element = document.createElement('div');
    element.innerHTML = this.reviewContent;
    if (this.templateSelected && this.templateSelected.pageSize) {
      element.style.padding = this.getPaddingSize(this.templateSelected.pageSize);
    }
    this.wait(100)
      .then(() => {
        this.alignCenterImg(element);
        this.setLineHeight(element);
      })
      .then(() => {
        this.isPrint = false;
        element.style.wordWrap = 'break-word';
        this.printService.printEl(element);
        if (element.parentNode) {
          element.parentNode.removeChild(element);
        }
        // this.printService.printContent(element.outerHTML);
      });
  }

  wait(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
  @HostListener('window:afterprint')
  onafterprint() {
    this.statusPrint = !this.statusPrint;
  }

  genQrCode(value: string) {
    if (value) {
      QRCode.toDataURL(value, (err, url) => {
        if (err) {
          // console.error(err);
          return;
        }
        this.urlQR = url;
        // In ra URL của mã QR (có thể sử dụng để hiển thị hoặc tải xuống)
      });
    }
  }
  async printToPDF() {
    this.isSavingPdf = true;
    let pageSizeItem = [71, 297]; // Default size is K80

    const element = document.createElement('div');
    element.innerHTML = this.reviewContent;
    if (this.templateSelected && this.templateSelected.pageSize) {
      element.style.padding = this.getPaddingSize(this.templateSelected.pageSize);
    }
    element.style.wordWrap = 'break-word';

    await this.alignCenterImg(element);
    // await this.reduceFontSize(element);
    await this.setLineHeight(element);
    pageSizeItem = await this.setPageSize(element, pageSizeItem);
    try {
      const options = {
        html2canvas: {
          scale: 5,
        },
        jsPDF: {
          format: pageSizeItem,
        },
      };
      const buffer = await html2pdf().set(options).from(element).outputPdf('arraybuffer');
      this.isSavingPdf = false;
      this.openPdfInNewTab(buffer);
      if (element.parentNode) {
        element.parentNode.removeChild(element);
      }
    } catch (error) {
      this.isSavingPdf = false;
    }
  }
  getPaddingSize(size: string) {
    let value = '8px';
    switch (size) {
      case 'K58':
      case 'A7':
      case 'A8':
      case 'A9':
      case 'A10':
      case 'K80': {
        value = '8px';
        break;
      }
      case 'A0':
      case 'A1':
      case 'A2':
      case 'A3':
      case 'A5':
      case 'A6':
      case 'A4': {
        value = '25px 30px';
        break;
      }
    }
    return value;
  }
  openPdfInNewTab(buffer: any) {
    const blob = new Blob([buffer], { type: 'application/pdf' });
    const file = new File([blob], 'filename.pdf', { type: 'application/pdf' });
    const fileURL = URL.createObjectURL(file);
    this.isSavingPdf = false;
    this.isOpenNewTap = true;
    window.open(fileURL, '_blank');
  }
  reduceFontSize(element: any) {
    element.querySelectorAll('span').forEach(spanElement => {
      if (this.templateSelected.pageSize == 'K58' || this.templateSelected.pageSize == 'K80') {
        let computedFontSize = spanElement.style.fontSize;
        if (computedFontSize) {
          let fontSizeValue = parseInt(computedFontSize) - 2;
          let newFontSize = fontSizeValue + 'px';
          spanElement.style.fontSize = newFontSize; // Áp dụng fontsize mới
        } else {
          spanElement.style.fontSize = '12px';
        }
      }
    });
  }
  setLineHeight(element: any) {
    element.querySelectorAll('th, td, p, span').forEach((e: any) => {
      e.style.lineHeight = '1';
      if (e.tagName.toLowerCase() === 'th' || e.tagName.toLowerCase() === 'td') {
        e.style.padding = '0';
      }
    });
  }
  setPageSize(element: any, pageSizeItem: any) {
    const customSizes = {
      K80: [71, 297],
      K58: [57, 297],
      A0: [841, 1189],
      A1: [594, 841],
      A2: [420, 594],
      A3: [297, 420],
      A4: [210, 297],
      A5: [148, 210],
      A6: [105, 148],
      A7: [74, 105],
      A8: [52, 74],
      A9: [37, 52],
      A10: [26, 37],
    };
    if (customSizes) {
      if (this.templateSelected && this.templateSelected.pageSize && this.templateSelected.pageSize in customSizes) {
        pageSizeItem = customSizes[this.templateSelected.pageSize];
      }
      document.body.appendChild(element);
      const height = element.offsetHeight;
      let dpi = 96; // 1 inch = 96px
      let mmPerInch = 25.4; // 1 inch = 25.4mm
      let heightInMm = (height / dpi) * mmPerInch; // Convert pixel sang mm
      pageSizeItem[1] = Number(heightInMm + 40);
    }
    return pageSizeItem;
  }
  alignCenterImg(element: any) {
    element.querySelectorAll('span > img').forEach(imgElement => {
      let parentElement = imgElement.parentElement;
      if (parentElement) {
        parentElement.style.display = 'flex';
        parentElement.style.justifyContent = 'center';
        parentElement.style.textAlign = 'center';
      }
    });
  }

  getDateFormat(variable: any, value: any) {
    const conditionFunction = () => {
      if (variable.includes('Date_NTN') && value) return 1;
      if (variable.includes('Date_N/T/N') && value) return 2;
      if (variable.includes('Date_N-T-N') && value) return 3;
      if (value) return 4;
      return 0;
    };

    switch (conditionFunction()) {
      case 1:
        return this.formatDateV2(value);
      case 2:
        return this.formatDateV3(value, 'YYYY-MM-DD HH:mm:ss', 'DD/MM/YYYY');
      case 3:
        return this.formatDateV3(value, 'YYYY-MM-DD HH:mm:ss', 'DD-MM-YYYY');
      case 4:
        return this.formatDateV3(value, 'YYYY-MM-DD HH:mm:ss', 'DD-MM-YYYY HH:mm:ss');
      default:
        return null;
    }
  }

  formatDateV2(date) {
    if (typeof date === 'string') {
      let dateObj = new Date(date);
      let year = dateObj.getFullYear();
      let month = ('0' + (dateObj.getMonth() + 1)).slice(-2);
      let day = ('0' + dateObj.getDate()).slice(-2);
      return 'Ngày ' + day + ' tháng ' + month + ' năm ' + year;
    } else {
      return undefined;
    }
  }

  formatDateV3(date: any, fromConvert: any, toConvert: any): any {
    return moment(date, fromConvert).format(toConvert);
  }

  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_PDF = ICON_PDF;
}
