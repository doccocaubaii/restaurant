import { Component, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import appSettings from '../../config/app-settings';
import { ContentOption } from '../../utils/contentOption';
import { BaseComponent } from '../../shared/base/base.component';
import { Router } from '@angular/router';
import { UtilsService } from '../../utils/Utils.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RegisterRequest } from '../../entities/register/register-request';
import { ToastrService } from 'ngx-toastr';
import { RegisterService } from './register.service';
import { ConvertResponse } from '../../config/convert-response';
import { SendOtpComponent } from './send-otp/send-otp.component';
import { LoadingBarService } from '@ngx-loading-bar/core';
import { ICON_EMAIL_REGISTER, ICON_PHONE_REGISTER, ICON_STORE, ICON_TAX_CODE, ICON_USERNAME } from '../../shared/other/icon';

@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent extends BaseComponent implements OnInit, OnDestroy {
  appSettings = appSettings;
  registerReq: RegisterRequest;
  REGEX_PHONE_NUMBER_MOBI_PONE: RegExp = /^(0|\+84)(\s|\.)?((7[06-9])|(9[0|3]))(\d)(\s|\.)?(\d{3})(\s|\.)?(\d{3})$/;
  REGEX_PHONE_NUMBER: RegExp =
    /^(0|\+84)(\s|\.)?((2[0-9])|(3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\d)(\s|\.)?(\d{3})(\s|\.)?(\d{3})$/;
  REGEX_EMAIL: RegExp =
    /^([\w#!%$‘&+*/=?^_`{|}~-]+[\w#!%$‘&+*/=?^_`.{|}~-]*[\w#!%$‘&+*/=?^_`{|}~-]+){1,64}@[\w]{2,63}[\w-]*(\.[\w-]{2,63})*(\.[a-zA-Z]{2,63})$/;
  private readonly REGEX_TAX_CODE = /^(?:\d{10}|\d{10}-\d{3})$/;

  constructor(
    private router: Router,
    private renderer: Renderer2,
    private utilsService: UtilsService,
    private modalService: NgbModal,
    private contentOption: ContentOption,
    private toast: ToastrService,
    private registerService: RegisterService,
    private loadingBar: LoadingBarService
  ) {
    super();
    this.appSettings.appEmpty = true;
    this.contentOption.isHiddenOrder = true;
    this.renderer.addClass(document.body, 'bg-white');
  }

  ngOnInit(): void {
    this.registerReq = {
      phoneNumber: '',
      email: '',
      companyTaxCode: '',
      companyName: '',
      fullName: '',
      username: '',
    };
  }

  ngOnDestroy() {
    this.appSettings.appEmpty = false;
    this.contentOption.isHiddenOrder = false;
    this.renderer.removeClass(document.body, 'bg-white');
  }

  // @ViewChild('OTPModal') OTPModal: ElementRef | undefined;
  modalRef: NgbModalRef | undefined;
  countOpenModal = 0;
  disableBtnRegister = false;
  // timeCountOld = 0;
  // timeCountText = '';
  // otpString = '';
  onRegister() {
    this.countOpenModal++;
    if (this.validInput()) {
      this.disableBtnRegister = true;
      this.loadingBar.start();
      const { fullName, companyName, companyTaxCode, email, phoneNumber, username } = this.registerReq;
      const requestSendOtp = {
        companyTaxCode: companyTaxCode,
        email: email,
        phoneNumber: phoneNumber,
      };
      this.registerService.sendOtp(requestSendOtp).subscribe(
        response => {
          if (response.ok) {
            this.loadingBar.complete();
            this.toast.success(ConvertResponse.getDataFromServer(response, true));
            this.modalRef = this.modalService.open(SendOtpComponent, { size: 'md', backdrop: 'static' });
            this.modalRef.componentInstance.request = this.registerReq;
            this.modalRef.closed.subscribe((res?: any) => {
              this.disableBtnRegister = false;
            });
            this.registerReq.username = phoneNumber;
          } else {
            this.loadingBar.complete();
            this.toast.error(ConvertResponse.getDataFromServer(response, true));
          }
        },
        error => {
          this.loadingBar.complete();
          this.disableBtnRegister = false;
        }
      );
    }
  }

  private sendOtp() {}

  private validInput(): boolean {
    const { fullName, companyName, companyTaxCode, email, phoneNumber } = this.registerReq;
    if (!fullName) {
      this.toast.error('Không để trống Họ và tên');
      return false;
    }
    if (!companyName) {
      this.toast.error('Không để trống Tên cửa hàng');
      return false;
    }
    if (!phoneNumber) {
      this.toast.error('Không để trống Số điện thoại');
      return false;
    } else if (!this.REGEX_PHONE_NUMBER.test(phoneNumber)) {
      this.toast.error('Số điện thoại không hợp lệ');
      return false;
    }
    // else if (this.REGEX_PHONE_NUMBER_MOBI_PONE.test(phoneNumber) && !email) {
    //   this.toast.error('Hệ thống chưa hỗ trợ số mobiphone. Bạn vui lòng nhập email để hệ thống gửi mã xác thực về email!');
    //   return false;
    // }

    if (email && (!this.REGEX_EMAIL.test(email) || email.includes('..'))) {
      this.toast.error('Email không hợp lệ');
      return false;
    }
    return true;
  }
  isValidTaxCode(value: any) {
    if (value == null || value == '') return true;
    return this.REGEX_TAX_CODE.test(value);
  }

  isValidPhoneNumber(value: any) {
    if (value == null || value == '') return false;
    return this.REGEX_PHONE_NUMBER.test(value);
  }

  protected readonly ICON_USERNAME = ICON_USERNAME;
  protected readonly ICON_STORE = ICON_STORE;
  protected readonly ICON_TAX_CODE = ICON_TAX_CODE;
  protected readonly ICON_EMAIL_REGISTER = ICON_EMAIL_REGISTER;
  protected readonly ICON_PHONE_REGISTER = ICON_PHONE_REGISTER;
}
