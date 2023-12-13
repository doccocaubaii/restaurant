import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ConfigService } from '../../config/service/config.service';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from '../../../shared/base/base.component';
import { CkeditorPrintConfigService } from './ckeditor-print-config.service';
import moment from 'moment/moment';
import * as QRCode from 'qrcode';
import { lastValueFrom, Subscription } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { InvoiceService } from '../../../pages/invoice/service/invoice.service';
import { ModalConfirmDeleteComponent } from '../../../pages/product/modal-confirm-delete/modal-confirm-delete.component';
import { ICON_CANCEL, ICON_CANCEL_WHITE, ICON_RESET, ICON_SAVE } from '../../../shared/other/icon';
import { Authority } from '../../../config/authority.constants';

@Component({
  selector: 'jhi-ckeditor-print-config',
  templateUrl: './ckeditor-print-config.component.html',
  styleUrls: ['./ckeditor-print-config.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CkeditorPrintConfigComponent extends BaseComponent implements OnInit, OnDestroy {
  private onChange: (value: string) => void;
  private onTouched: () => void;
  @ViewChild('ckePrintConfig') ckePrintConfig: any;
  private subscription: Subscription;
  private modalRef: NgbModalRef | undefined;
  printConfigs: any = [];
  lastCompany: any = {};
  urlQR: any;
  type = 0;
  checkView: any = true;
  orderSelected: any;
  idTemplate: any;
  disabledButton: boolean = true;
  checkCreate: boolean = true;
  sizes: any = [
    {
      name: 'K58',
    },
    {
      name: 'K80',
    },
    {
      name: 'A4',
    },
  ];
  config: any = {
    height: 500,
    plugins: [
      'fullpage',
      'advlist',
      'autolink',
      'code',
      'codesample',
      'directionality',
      'emoticons',
      'lists',
      'link',
      'image',
      'charmap',
      'preview',
      'anchor',
      'searchreplace',
      'visualblocks',
      'fullscreen',
      'insertdatetime',
      'media',
      'table',
      'help',
      'wordcount',
    ],
    toolbar:
      'undo redo | casechange blocks | bold italic backcolor | alignleft aligncenter alignright alignjustify | bullist numlist checklist outdent indent | removeformat | a11ycheck code table help',
    image_advtab: true,
    imagetools_toolbar: 'rotateleft rotateright | flipv fliph | editimage imageoptions',
    table_sizing_mode: 'relative',
    table_resize_bars: 'col-resize', // chỉ hiển thị thanh kéo ngang
    table_default_styles: {
      height: '',
    },
    content_style:
      'p,span,strong{margin:0!important;font-family:Times New Roman,Times,serif;word-wrap:break-word}hr{margin:3px 0!important;opacity:1;width:100%;color:rgba(0,0,0,.5)}table th:first-child{padding:0!important}p,span,strong,td,th{padding:0!important;line-height:1.3}table{table-layout:fixed;word-wrap:break-word;max-width:100%;width:100%!important}table tbody tr th{border-bottom:1px solid rgba(0,0,0,.5)}',
    setup: editor => {},
  };
  data: any = {};
  reviewContent: any = '';
  json: any;
  printConfigMap = new Map<string, string>();
  listTemplate: any = [];
  listTemplateByType: any = [];
  invoiceConfiguration: any;
  isResetData: any = false;
  isShowInfo: any = false;
  authorADD = Authority.PRINT_TEMPLATE_ADD;
  authorUPDATE = Authority.PRINT_TEMPLATE_UPDATE;
  authorDELETE = Authority.PRINT_TEMPLATE_DELETE;
  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
  constructor(
    private modalService: NgbModal,
    private configService: ConfigService,
    private toast: ToastrService,
    public activeModal: NgbActiveModal,
    private service: CkeditorPrintConfigService,
    private translateService: TranslateService,
    private invoiceService: InvoiceService
  ) {
    super();
  }
  async ngOnInit() {
    this.printConfigs = [];
    this.data = {};
    this.reviewContent = '';
    this.json = '';
    this.lastCompany = await this.getCompany();
    this.invoiceService.getCompanyConfig(this.lastCompany.id).subscribe(async value => {
      this.invoiceConfiguration = await value.data;
      this.subscription = this.service.getDataDefault().subscribe(async list => {
        this.orderSelected = list.data[0].dataResponse;
        this.getCurrentTemplate();
      });
    });
    this.getAllPrintConfig();
  }

  public updateContent(): void {
    this.reviewContent = this.data.content;
    if (this.reviewContent) {
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
          const value = this.orderSelected[config.columnName];
          if (config.variable) {
            if (config.columnName === 'qrCode') {
              this.genQrCode(value);
              this.reviewContent = this.reviewContent.replace(new RegExp(config.variable, 'g'), this.urlQR ? this.urlQR : '');
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
          if (this.orderSelected[config.tableName]) {
            const value = this.orderSelected[config.tableName][config.columnName];
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
        if (this.orderSelected[tableName1]) {
          this.orderSelected[tableName1].forEach((value, index) => {
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
              if (content.includes(config.variable)) {
                if (config.isTable && !config.isTopping) {
                  const data = value[config.columnName];
                  if (data != null && data != undefined) {
                    content = this.replaceContent(content, config.variable, data);
                  } else {
                    content = this.replaceContent(content, config.variable, '');
                  }
                }
              }
            });
            if (index < this.orderSelected[tableName1].length - 1) {
              content += rawContent;
            }
          });
        } else {
          if (rawContent) {
            this.reviewContent = this.reviewContent.replace(rawContent, '');
          }
        }
        if (rawContent && content) {
          this.reviewContent = this.reviewContent.replace(rawContent, content);
        }
      }
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

  closeModal() {
    this.resetCKEditorContent();
    this.activeModal.dismiss();
  }

  savePrintConfig() {
    let content: any = this.data.content;
    content = this.convertWidthTable(content);
    if (typeof content === 'string') {
      content = content
        .replace(/< /g, '<') // Loại bỏ khoảng trắng sau các ký tự >
        .replace(/ >/g, '>') // Loại bỏ khoảng trắng trước các ký tự <
        .replace(/\n/g, ''); // Loại bỏ dấu xuống dòng
    }
    if (this.type !== null && this.type !== undefined) {
      if (!this.data.pageSize) {
        this.toast.error('Vui lòng chọn khổ in', 'Thông báo');
      } else if (!this.data.name) {
        this.toast.error('Vui lòng nhập tên mẫu in', 'Thông báo');
      } else if (!this.checkCreate) {
        if (this.data.id) {
          if (this.listTemplateByType.find(item => item.name === this.data.name && item.id !== this.data.id)) {
            this.toast.error('Tên mẫu in không được trùng nhau', 'Thông báo');
          } else {
            this.disabledButton = false;
            this.service
              .updatePrintConfig(
                this.data.id,
                this.data.name,
                content,
                this.data.pageSize,
                JSON.stringify(this.json),
                this.data.typeTemplate
              )
              .subscribe(
                value => {
                  this.toast.success(value.message[0].message);
                  this.getCurrentTemplate();
                  this.checkView = true;
                  this.disabledButton = true;
                },
                error => {
                  this.disabledButton = false;
                }
              );
          }
        }
      } else {
        if (this.listTemplateByType.find(item => item.name === this.data.name)) {
          this.toast.error('Tên mẫu in không được trùng nhau', 'Thông báo');
        } else {
          this.disabledButton = true;
          this.service
            .createPrintConfig(this.data.name, content, this.data.pageSize, JSON.stringify(this.json), this.data.typeTemplate)
            .subscribe(
              value => {
                this.toast.success(value.message[0].message);
                this.getCurrentTemplate();
                this.checkView = true;
                this.disabledButton = true;
              },
              error => {
                this.disabledButton = false;
              }
            );
        }
      }
    }
  }

  resetCKEditorContent() {
    if (this.ckePrintConfig && this.ckePrintConfig.instance) {
      setTimeout(() => {
        this.ckePrintConfig.instance.setData('');
      }, 100);
    }
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

  searchByType(number: number) {
    this.type = number;
  }
  formatDate(date: any): any {
    return moment(date, 'YYYY-MM-DD HH:mm:ss').format('DD/MM/YYYY HH:mm:ss');
  }
  formatDateV3(date: any, fromConvert: any, toConvert: any): any {
    return moment(date, fromConvert).format(toConvert);
  }
  dismiss(value: any) {
    this.resetCKEditorContent();
    this.activeModal.close(value);
  }

  async getConfigByType(type: any) {
    this.type = type;
    this.listTemplateByType = await this.listTemplate.filter(item => item.typeTemplate === type);
    if (this.listTemplateByType.length > 0) {
      this.data = await this.listTemplateByType[0];
      this.idTemplate = this.data.id;
      let contentRemove = this.removeExtraConfig(this.listTemplateByType[0].content);
      if (contentRemove) {
        this.data.content = contentRemove;
      } else {
        this.data.content = this.listTemplateByType[0].content;
      }
      this.reviewContent = await this.data.content;
      this.json = await JSON.parse(this.data.contentParams);
      await this.updateContent();
    } else {
      this.data = {};
      this.reviewContent = '';
    }
    this.checkView = true;
    this.disabledButton = true;
  }

  resetData() {
    this.isResetData = true;
    const setIsResetDataPromise = new Promise<void>(resolve => {
      resolve();
    });
    setIsResetDataPromise.then(() => {
      this.service.getPrintConfigDefault(this.type).subscribe(value => {
        if (value.body.data) {
          let contentRemove = this.removeExtraConfig(value.body.data.content);
          if (contentRemove) {
            this.data.content = contentRemove;
          } else {
            this.data.content = value.body.data.content;
          }
          this.data.contentParams = value.body.data.contentParams;
          this.reviewContent = JSON.parse(JSON.stringify(this.data.content));
          this.json = JSON.parse(this.data.contentParams);
          this.updateContent();
          this.isResetData = false;
        }
      });
    });
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

  async changeTemplate() {
    this.data = await this.listTemplateByType.find(item => item.id === this.idTemplate);
    this.idTemplate = this.data.id;
    this.checkView = true;
    this.disabledButton = true;
    let contentRemove = this.removeExtraConfig(this.data.content);
    if (contentRemove) {
      this.data.content = contentRemove;
    }
    this.reviewContent = await JSON.parse(JSON.stringify(this.data.content));
    this.json = await JSON.parse(this.data.contentParams);
    await this.updateContent();
  }

  createTemplate() {
    this.service.getPrintConfigDefault(this.type).subscribe(async value => {
      this.data = await {};
      this.data.contentParams = await value.body.data.contentParams;
      this.data.typeTemplate = await value.body.data.typeTemplate;
      this.data.name = '';
      this.data.pageSize = 'K58';
      let contentRemove = this.removeExtraConfig(value.body.data.content);
      if (contentRemove) {
        this.data.content = contentRemove;
      } else {
        this.data.content = value.body.data.content;
      }
      this.reviewContent = await this.data.content;
      this.json = await JSON.parse(this.data.contentParams);
      await this.updateContent();
    });
    this.checkView = false;
    this.checkCreate = true;
    this.disabledButton = false;
  }

  updateTemplate() {
    this.checkView = false;
    this.disabledButton = false;
    this.checkCreate = false;
  }

  cancel() {
    this.checkView = true;
  }

  deleteTemplate() {
    this.modalRef = this.modalService.open(ModalConfirmDeleteComponent, {
      size: 'dialog-centered',
      backdrop: 'static',
    });
    this.modalRef.componentInstance.title = 'Bạn có chắc chắn muốn xóa mẫu in ';
    this.modalRef.componentInstance.title2 = this.data.name + '?';
    this.modalRef.closed.subscribe((res?: any) => {
      if (res === 1) {
        this.service.deletePrintConfig(this.lastCompany.id, this.data.id).subscribe(value => {
          this.toast.success(value.message[0].message);
          this.getCurrentTemplate();
        });
      }
    });
  }

  getCurrentTemplate() {
    this.service.getAllPrintConfig().subscribe(async value => {
      if (value.data) {
        this.listTemplate = await value.data;
        this.listTemplateByType = this.listTemplate.filter(item => item.typeTemplate === this.type);
        if (this.listTemplateByType) {
          this.data = this.listTemplateByType[0];
          this.idTemplate = this.data.id;
          let borderReplace = 'border="1"';
          let dataRemove = this.removeExtraConfig(this.data.content);
          if (dataRemove) {
            this.data.content = dataRemove;
          }
        }
        this.data = JSON.parse(JSON.stringify(this.data));
        this.reviewContent = this.data.content;
        this.json = JSON.parse(this.data.contentParams);
        this.updateContent();
      }
    });
  }

  removeExtraConfig(content: any) {
    if (this.invoiceConfiguration && !this.invoiceConfiguration.serviceChargeConfig) {
      let htmlVat10 = this.getContent('{Thue_suat_10}', this.data.content, '<tr', '</tr>');
      let htmlVat8 = this.getContent('{Thue_suat_8}', this.data.content, '<tr', '</tr>');
      let htmlVat5 = this.getContent('{Sv_charge_5}', this.data.content, '<tr', '</tr>');
      if (htmlVat10) {
        content = content.replace(htmlVat10, '');
      }
      if (htmlVat8) {
        content = content.replace(htmlVat8, '');
      }
      if (htmlVat5) {
        content = content.replace(htmlVat5, '');
      }
      return content;
    }
  }
  convertWidthTable(content: string) {
    const listTable: string[] = [];
    // Tìm các đoạn chuỗi bắt đầu bằng '<table' và kết thúc bằng '</table>'
    const regex = /<table[\s\S]*?<\/table>/g;
    let match;
    while ((match = regex.exec(content)) !== null) {
      listTable.push(match[0]);
    }
    listTable.forEach((element, index) => {
      const table: any = document.querySelector('table');
      const maxWidth = table.offsetWidth;
      const a = element;
      if (element.includes('width:')) {
        let regex = /width:\s*(\d+)px/g;
        let matchEl: any = element.match(regex);
        if (matchEl) {
          matchEl.forEach(width => {
            let d = width.split('width:')[1].trim();
            let rawWidth = parseFloat(d.replace('px', ''));
            if (rawWidth > 1) {
              let convertWidth = (rawWidth / maxWidth) * 100;
              element = element.replace(d, convertWidth + '%');
            }
          });
          content = content.replace(a, element);
        }
      }
    });
    return content;
  }
  formatDateV2(date: any) {
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

  showInfoTemplate(event?) {
    if (event) {
      event.stopPropagation();
      this.isShowInfo = !this.isShowInfo;
    } else {
      this.isShowInfo = false;
    }
  }

  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
  protected readonly ICON_RESET = ICON_RESET;
  protected readonly ICON_CANCEL_WHITE = ICON_CANCEL_WHITE;
}
