import { Component, OnDestroy, AfterViewInit, OnInit, ElementRef, Renderer2, ViewChild, ChangeDetectorRef } from '@angular/core';
import appSettings from '../../../config/app-settings';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ModalDialogComponent } from '../../../shared/modal/modal-dialog.component';
import { InvoiceService } from '../service/invoice.service';
import { BaseComponent } from '../../../shared/base/base.component';
import { ToastrService } from 'ngx-toastr';
import { InvoiceDetailComponent } from '../invoice-detail/invoice-detail.component';
import { PrintConfigComponent } from '../../../layouts/modal/print-config/print-config.component';
import { DeviceComponent } from '../../../layouts/modal/device/device.component';
import { UtilsService } from '../../../utils/Utils.service';
import { last_company, last_owner_device, last_user } from '../../../object-stores.constants';
import { OwnerDevice } from '../../../entities/ownerDevice/ownerDevice.model';
import { TranslateService } from '@ngx-translate/core';
import { map } from 'rxjs/operators';
import { CkeditorPrintConfigComponent } from '../../../layouts/modal/ckeditor-print-config/ckeditor-print-config.component';
import { INVOICE_METHOD, ServiceConstants } from '../../../constants/invoice.constants';
import { CertificateService } from '../../../config/service/certificate-service.service';
import { AuthServerProvider } from '../../../core/auth/auth-jwt.service';
import { CommonLogin } from '../../../utils/common-login';
import { DataEncrypt } from '../../../entities/indexDatabase/data-encrypt.model';
import {
  ICON_CANCEL,
  ICON_DEVICE_CODE,
  ICON_PASSWORD_INVISIBLE,
  ICON_PASSWORD_VISIBLE,
  ICON_PRINT_CONFIG,
  ICON_UPDATE_CONFIG,
  ICON_UPDATE_PATH,
  ICON_UPDATE_V2,
} from '../../../shared/other/icon';
import {Authority} from "../../../config/authority.constants";

@Component({
  selector: 'jhi-invoice-configuration',
  templateUrl: './invoice-configuration.component.html',
  styleUrls: ['./invoice-configuration.component.scss'],
})
export class InvoiceConfigurationComponent extends BaseComponent implements OnInit, OnDestroy {
  @ViewChild(CkeditorPrintConfigComponent) formRecipe: CkeditorPrintConfigComponent;
  appSettings = appSettings;
  posMobileSidebarToggled = false;

  menuType: any;
  invoiceTypes: any = [];
  discountTypes: any = [];
  invoiceMethods: any = [];
  filter: any;
  comId: number = 1432;
  ownerInfo: any = {};
  lastCompany: any = {};
  patterns: any = [];
  allPatterns: any = [];
  templates: any = [];
  companies: any;
  invoiceConfig: any = {};
  errorMessage: any;
  printConfig: any = {};
  checkModalRef: NgbModalRef;
  deviceCode: any;
  deviceName: any;
  overStocks: any = [];
  editUrl = false;
  urlInvoice: any;
  type = 0;
  isEditTax = false;
  EI = ServiceConstants.EI;
  NGP = ServiceConstants.NGP;
  VTE = ServiceConstants.VTE;
  services = [
    { code: ServiceConstants.EI, name: 'EasyInvoice - EI' },
    { code: ServiceConstants.NGP, name: 'Ngô Gia Phát - NGP' },
    { code: ServiceConstants.VTE, name: 'Viettel - VTE' },
  ];
  authorCOMPANY_CONFIG_UPDATE = Authority.COMPANY_CONFIG_UPDATE;

  togglePosMobileSidebar() {
    this.posMobileSidebarToggled = !this.posMobileSidebarToggled;
  }

  constructor(
    private authServerProvider: AuthServerProvider,
    private commonLogin: CommonLogin,
    private modalService: NgbModal,
    private invoiceService: InvoiceService,
    public utilsService: UtilsService,
    private toast: ToastrService,
    private elementRef: ElementRef,
    private renderer: Renderer2,
    private translateService: TranslateService,
    private changeDetectorRef: ChangeDetectorRef
  ) {
    super();
    this.menuType = 0;
  }

  ngAfterViewInit() {
    var targets = [].slice.call(document.querySelectorAll('.pos-menu [data-filter]'));

    targets.map(function (target: any) {
      target.onclick = function (e) {
        e.preventDefault();

        var targetBtn = e.target;
        var targetFilter = targetBtn.getAttribute('data-filter');

        targetBtn.classList.add('active');

        var allFilter = [].slice.call(document.querySelectorAll('.pos-menu [data-filter]'));

        allFilter.map(function (filterElm: any) {
          var filterElmFilter = filterElm.getAttribute('data-filter');

          if (targetFilter != filterElmFilter) {
            filterElm.classList.remove('active');
          }
        });

        var allContent = [].slice.call(document.querySelectorAll('.pos-content [data-type]'));
        allContent.map(function (contentElm: any) {
          var contentType = contentElm.getAttribute('data-type');

          if (targetFilter == 'all') {
            contentElm.classList.remove('d-none');
          } else {
            if (contentType != targetFilter) {
              contentElm.classList.add('d-none');
            } else {
              contentElm.classList.remove('d-none');
            }
          }
        });
      };
    });
  }

  ngOnDestroy() {
    this.appSettings.appEmpty = false;
    this.appSettings.appContentFullHeight = false;
  }

  openPopup() {
    this.modalService.open(ModalDialogComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
  }

  async ngOnInit() {
    this.filter = 'all';
    this.lastCompany = await this.getCompany();
    this.invoiceConfig.service = this.lastCompany.service ? this.lastCompany.service : this.EI;
    this.invoiceTypes = [
      { id: 0, name: this.translateService.instant('easyPos.invoice.config.type.sale') },
      { id: 1, name: this.translateService.instant('easyPos.invoice.config.type.oneTax') },
      { id: 2, name: this.translateService.instant('easyPos.invoice.config.type.manyTaxes') },
    ];
    this.overStocks = [
      { id: 0, name: this.translateService.instant('easyPos.invoice.config.overStocks.notAllow') },
      { id: 1, name: this.translateService.instant('easyPos.invoice.config.overStocks.allow') },
    ];
    this.discountTypes = [
      { id: 0, name: this.translateService.instant('easyPos.invoice.config.discountType.byOrder') },
      { id: 1, name: this.translateService.instant('easyPos.invoice.config.discountType.byProduct') },
      { id: 2, name: this.translateService.instant('easyPos.invoice.config.discountType.byOrderAndProduct') },
      { id: 3, name: this.translateService.instant('easyPos.invoice.config.discountType.noDiscount') },
    ];
    this.invoiceMethods = [
      { id: INVOICE_METHOD.HSM_TU_DONG, name: this.translateService.instant('easyPos.invoice.config.method.auto') },
      { id: INVOICE_METHOD.HSM_THU_CONG, name: this.translateService.instant('easyPos.invoice.config.method.manual') },
      { id: INVOICE_METHOD.HSM_MOI_TAO_LAP, name: this.translateService.instant('easyPos.invoice.config.method.hsmNewSetting') },
      { id: INVOICE_METHOD.TOKEN_MOI_TAO_LAP, name: this.translateService.instant('easyPos.invoice.config.method.newSetting') },
    ];
    // this.loginForm.password = this.lastCompany.easyinvoicePass;
    // this.loginForm.username = this.lastCompany.easyinvoiceAccount;
    this.getOwnerInfo();
    this.getCompanyConfig();
    await this.getDeviceName();
    if (this.utilsService.getStyleByCode('HD/CD').includes('display:none !important;')) {
      this.type = 1;
    } else {
      this.type = 0;
    }
  }

  async getDeviceName() {
    const ownerDevice = await this.findByID(last_owner_device, this.lastCompany.ownerId);
    this.deviceCode = ownerDevice && ownerDevice.deviceCode ? ownerDevice.deviceCode : '';
  }

  getOwnerInfo() {
    this.invoiceService.getOwnerInfo(this.lastCompany.id).subscribe(res => {
      this.invoiceConfig.taxRegisterCode = res.data.taxRegisterCode;
      this.invoiceConfig.password = res.data.easyInvoicePass ? res.data.easyInvoicePass : '';
      this.invoiceConfig.username = res.data.easyInvoiceAccount ? res.data.easyInvoiceAccount : '';
      this.invoiceConfig.url = res.data.easyInvoiceUrl ? res.data.easyInvoiceUrl : '';
      this.allPatterns = res.data.patterns;
      this.patterns = this.allPatterns;
      this.templates = res.data.templates;
    });
  }

  getCompanyConfig() {
    this.invoiceService.getCompanyConfig(this.lastCompany.id).subscribe(res => {
      this.invoiceConfig.invoicePattern = res.data.invoicePattern;
      this.invoiceConfig.invoiceType = res.data.invoiceType;
      this.invoiceConfig.invoiceTemplate = res.data.invoiceTemplate;
      this.invoiceConfig.invoiceMethod = res.data.invoiceMethod;
      this.ownerInfo.typeDiscount = res.data.typeDiscount;
      this.ownerInfo.overStock = res.data.overStock;
      this.ownerInfo.isBuyer = res.data.isBuyer;
      this.ownerInfo.invDynamicDiscountName = res.data.invDynamicDiscountName;
      this.ownerInfo.discountVat = res.data.discountVat;
      this.ownerInfo.taxReductionType = res.data.taxReductionType;
      this.ownerInfo.voucherApply = res.data.voucherApply;
      this.ownerInfo.combineVoucherApply = res.data.combineVoucherApply;
      this.ownerInfo.invDynamicDiscountName = res.data.invDynamicDiscountName;

      this.changeInvoicePattern(false);
    });
  }

  updateInvoiceConfig() {
    if (this.invoiceConfig.url && !this.invoiceConfig.invoicePattern && this.invoiceConfig.invoicePattern != 0) {
      this.toast.error('Mẫu số, ký hiệu không được bỏ trống', this.translateService.instant('global.info.notify'));
      return;
    }
    if (!this.invoiceConfig.invoiceType && this.invoiceConfig.invoiceType != 0) {
      this.toast.error('Loại hình hóa đơn không được bỏ trống', this.translateService.instant('global.info.notify'));
      return;
    }
    if (!this.ownerInfo.typeDiscount && this.ownerInfo.typeDiscount != 0) {
      this.toast.error('Loại hình giảm giá không được bỏ trống', this.translateService.instant('global.info.notify'));
      return;
    }
    if (!this.invoiceConfig.invoiceMethod && this.invoiceConfig.invoiceMethod != 0) {
      this.toast.error('Loại hình phát hành hóa đơn không được bỏ trống', this.translateService.instant('global.info.notify'));
      return;
    }
    this.ownerInfo.comId = this.lastCompany.id;
    this.invoiceService.updateInvoiceConfig(this.ownerInfo).subscribe(async res => {
      if (res.status) {
        const dataUpdateCom = {
          ...this.lastCompany,
          discountType: this.ownerInfo.typeDiscount,
          overStock: this.ownerInfo.overStock,
          taxReductionType: this.ownerInfo.taxReductionType,
          isBuyer: this.ownerInfo.isBuyer,
          invDynamicDiscountName: this.ownerInfo.invDynamicDiscountName,
          discountVat: this.ownerInfo.discountVat,
        };
        await this.updateById(last_company, this.lastCompany.id, new DataEncrypt(this.lastCompany.id, this.encryptFromData(dataUpdateCom)));
        this.toast.success(res.reason);
      } else {
        this.toast.error(res.message[0].message);
      }
    });
  }

  printPage() {
    window.print();
  }

  login() {
    this.invoiceConfig.comId = this.lastCompany.id;
    this.invoiceService.updateInfoConfig(this.invoiceConfig).subscribe(async res => {
      this.toast.success(res.reason);
      const dataUpdateCom = {
        ...this.lastCompany,
        easyinvoiceAccount: this.invoiceConfig.username,
        easyinvoicePass: this.ownerInfo.password,
        easyinvoiceUrl: this.ownerInfo.url,
        invoiceMethod: this.invoiceConfig.invoiceMethod,
        invoicePattern: this.invoiceConfig.invoicePattern,
        invoiceType: this.invoiceConfig.invoiceType,
      };
      await this.updateById(last_company, this.lastCompany.id, new DataEncrypt(this.lastCompany.id, this.encryptFromData(dataUpdateCom)));
      this.getCompanyConfig();
      this.saveChangeSession();
    });
  }

  openDeviceModal() {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.checkModalRef = this.modalService.open(DeviceComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    this.checkModalRef.componentInstance.deviceChangeEvent.subscribe(res => {
      this.deviceCode = res;
      this.checkModalRef.close();
    });
  }

  openPrintConfig() {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    // this.checkModalRef = this.modalService.open(PrintConfigComponent, {
    //   size: 'xl',
    //   backdrop: 'static',
    //   windowClass: 'margin-5',
    // });
    this.checkModalRef = this.modalService.open(CkeditorPrintConfigComponent, {
      size: 'xl',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    this.checkModalRef.closed.subscribe((res?: any) => {
      this.changeDetectorRef.detectChanges();
    });
  }

  setPrintConfig() {}

  changeInvoicePattern(isEdit?: boolean) {
    if (this.invoiceConfig.invoicePattern && this.invoiceConfig.invoicePattern.charAt(0) === '1') {
      this.invoiceTypes = [
        { id: 1, name: this.translateService.instant('easyPos.invoice.config.type.oneTax') },
        { id: 2, name: this.translateService.instant('easyPos.invoice.config.type.manyTaxes') },
      ];
      if (isEdit) {
        this.invoiceConfig.invoiceType = 1;
      }
    } else if (this.invoiceConfig.invoicePattern && this.invoiceConfig.invoicePattern.charAt(0) === '2') {
      this.invoiceTypes = [{ id: 0, name: this.translateService.instant('easyPos.invoice.config.type.sale') }];
      if (isEdit) {
        this.invoiceConfig.invoiceType = 0;
      }
    } else {
      this.invoiceTypes = [
        { id: 0, name: this.translateService.instant('easyPos.invoice.config.type.sale') },
        { id: 1, name: this.translateService.instant('easyPos.invoice.config.type.oneTax') },
        { id: 2, name: this.translateService.instant('easyPos.invoice.config.type.manyTaxes') },
      ];
    }
  }

  checkSaveInvoice() {
    if (
      (!this.invoiceConfig.invoiceType && this.invoiceConfig.invoiceType != 0) ||
      (!this.invoiceConfig.invoiceMethod && this.invoiceConfig.invoiceMethod != 0) ||
      (!this.ownerInfo.overStock && this.ownerInfo.overStock != 0) ||
      (!this.ownerInfo.typeDiscount && this.ownerInfo.typeDiscount != 0) ||
      !this.invoiceConfig.invoicePattern
    ) {
      return true;
    }
    return false;
  }

  focusInput(index: number) {
    const elementToFocus = this.elementRef.nativeElement.querySelector('[tabindex= ' + index + ']');
    if (elementToFocus) {
      this.renderer.selectRootElement(elementToFocus).focus();
    }
  }

  updateUrlInvoice() {
    this.invoiceConfig.url = this.urlInvoice;
  }

  openModalUrlInvoice(modal: any) {
    this.urlInvoice = this.invoiceConfig.url;
    this.checkModalRef = this.modalService.open(modal, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
  }

  changeType(number: number) {
    this.type = number;
  }

  editTaxCode() {
    this.isEditTax = !this.isEditTax;
    if (this.isEditTax) {
      setTimeout(() => {
        (document.getElementById('taxAuthorityCode1') as HTMLInputElement).select();
      }, 100);
    }
  }

  changeInvoiceTemplate() {
    this.invoiceConfig.invoicePattern = null;
    if (this.invoiceConfig.invoiceTemplate) {
      this.patterns = this.allPatterns.filter(item => item.template === this.invoiceConfig.invoiceTemplate);
      if (this.patterns.length === 1) {
        this.invoiceConfig.invoicePattern = this.patterns[0].pattern;
      }
    } else {
      this.patterns = this.allPatterns;
    }
  }

  saveChangeSession() {
    if (this.lastCompany.id) {
      this.authServerProvider
        .changeSession(this.lastCompany.id)
        .pipe(
          map((res: any) => {
            if (res && !res.status) {
              return res;
            } else {
              return res;
            }
            // mergeMap(() => this.accountService.identity(true));
          })
        )
        .subscribe(
          async res => {
            await this.commonLogin.insertDataLoginSuccess(res, this.companies, false);
            this.lastCompany = await this.getCompany();
          },
          error => {
            this.toast.error('Đổi phiên làm việc thất bại');
            // console.log(error);
          }
        );
    } else {
      this.toast.error('Vui lòng chọn cửa hàng của bạn');
    }
  }

  passwordVisible = false;
  showPassword() {
    this.passwordVisible ? (this.passwordVisible = false) : (this.passwordVisible = true);
  }

  protected readonly ICON_PRINT_CONFIG = ICON_PRINT_CONFIG;
  protected readonly ICON_DEVICE_CODE = ICON_DEVICE_CODE;
  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_UPDATE_V2 = ICON_UPDATE_V2;
  protected readonly ICON_UPDATE_PATH = ICON_UPDATE_PATH;
  protected readonly ICON_UPDATE_CONFIG = ICON_UPDATE_CONFIG;
  protected readonly ICON_PASSWORD_INVISIBLE = ICON_PASSWORD_INVISIBLE;
  protected readonly ICON_PASSWORD_VISIBLE = ICON_PASSWORD_VISIBLE;
}
