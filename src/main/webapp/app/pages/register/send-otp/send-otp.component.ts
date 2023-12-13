import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { ConvertResponse } from '../../../config/convert-response';
import { NgOtpInputComponent } from 'ng-otp-input';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { RegisterService } from '../register.service';
import { RegisterRequest } from '../../../entities/register/register-request';
import { LoadingBarService } from '@ngx-loading-bar/core';
import { Router } from '@angular/router';
import { LOGIN } from '../../../constants/app.routing.constants';
import dayjs from 'dayjs/esm';
import { REGISTER_HASH_CODE } from '../../../constants/common.constants';
import * as CryptoJS from 'crypto-js';
import {ICON_CANCEL} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-send-otp',
  templateUrl: './send-otp.component.html',
  styleUrls: ['./send-otp.component.scss'],
})
export class SendOtpComponent implements OnInit {
  @Input() request: any;
  registerReq: RegisterRequest;
  registerSuccess = false;

  constructor(
    private modalService: NgbModal,
    private toast: ToastrService,
    private registerService: RegisterService,
    private activeModal: NgbActiveModal,
    private loadingBar: LoadingBarService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerReq = this.request;
    clearInterval(this.ticker);
    this.otpString = '';
    this.timeInSecs = 299;
    this.startTimer(299);
    this.countdown = '05:00';
  }

  dismiss($event: MouseEvent) {
    this.activeModal.close();
    clearInterval(this.ticker);
  }

  @ViewChild('ngOtpInput') ngOtpInput: NgOtpInputComponent;
  config = {
    allowNumbersOnly: true,
    length: 6,
    isPasswordInput: false,
    disableAutoFocus: false,
    inputStyles: {
      width: '50px',
      height: '50px',
      'border-radius': '8px',
      border: 'border: 1px solid #dadce0',
      display: 'inline',
      color: '#58647a',
      'font-size': '24px',
    },
    inputClass: 'form-control',
  };
  disabledConfirm = true;
  disabledBackToLogin = false;
  disabledResendOtp = false;
  otpString = '';

  onOtpChange(input: string) {
    this.disabledConfirm = !(input && input.length === 6);
    this.otpString = input;
  }

  onConfirmOTP() {
    if (!this.otpString || this.otpString.length < 6) {
      this.toast.error('Vui lòng nhập đầy đủ mã xác nhận');
      return;
    }
    this.loadingBar.start();
    this.disabledConfirm = true;
    if (this.ngOtpInput.otpForm) {
      if (this.ngOtpInput.otpForm.disabled) {
        this.ngOtpInput.otpForm.enable();
      } else {
        this.ngOtpInput.otpForm.disable();
      }
    }
    clearInterval(this.ticker);

    const requestCheckOtp = {
      username: this.registerReq.username,
      otp: this.otpString,
    };
    let checkOtpSuccess = false;
    this.registerService.checkOtp(requestCheckOtp).subscribe(
      response => {
        if (response.ok) {
          this.toast.success(ConvertResponse.getDataFromServer(response, true));
          const { fullName, companyName, companyTaxCode, email, phoneNumber, username } = this.registerReq;
          const dateNow = dayjs();
          const md5Hash = CryptoJS.MD5(companyTaxCode + username + REGISTER_HASH_CODE).toString();
          const registerRequestFinal = {
            companyName: companyName,
            companyTaxCode: companyTaxCode,
            fullName: fullName,
            username: username,
            email: email,
            phoneNumber: phoneNumber,
            packageId: null,
            startDate: dateNow.format('YYYY-MM-DD'),
            endDate: dateNow.add(15, 'day').format('YYYY-MM-DD'),
            hashCode: md5Hash,
          };
          this.registerService.register(registerRequestFinal).subscribe(
            response => {
              if (response.ok) {
                this.loadingBar.complete();
                this.registerSuccess = true;
              } else {
                this.loadingBar.complete();
                this.toast.error(ConvertResponse.getDataFromServer(response, true));
              }
            },
            error => {
              this.loadingBar.complete();
              console.log(error);
            }
          );
        } else {
          this.loadingBar.complete();
          this.toast.error(ConvertResponse.getDataFromServer(response, true));
          this.ngOtpInput.otpForm.enable();
          this.startTimer(this.timeInSecs);
        }
      },
      error => {
        this.loadingBar.complete();
        this.ngOtpInput.otpForm.enable();
        this.startTimer(this.timeInSecs);
      }
    );
  }

  isResendOtp: boolean = false;
  onResendOtp() {
    if (!this.isResendOtp) {
      this.isResendOtp = true;
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
            this.isResendOtp = false;
            this.loadingBar.complete();
            this.toast.success(ConvertResponse.getDataFromServer(response, true));
            clearInterval(this.ticker);
            this.countdown = '05:00';
            this.timeInSecs = 299;
            this.startTimer(299);
            this.otpString = '';
            this.ngOtpInput.setValue('');
            this.disabledResendOtp = true;
            this.countDownResend = '60';
            this.countDownTimeResend(59);
            this.disabledConfirm = true;
            if (this.ngOtpInput.otpForm) {
              if (this.ngOtpInput.otpForm.disabled) {
                this.ngOtpInput.otpForm.enable();
              }
            }
            this.isResendOtp = false;
          } else {
            this.isResendOtp = false;
            this.loadingBar.complete();
            this.toast.error(ConvertResponse.getDataFromServer(response, true));
          }
        },
        error => {
          this.isResendOtp = false;
          this.loadingBar.complete();
        }
      );
    }
  }

  countDownResend: string = '60';
  countDownTimeResend(i) {
    let timer = setInterval(() => {
      this.countDownResend = i;
      i-- || clearInterval(timer);
      if (i === -1) {
        this.disabledResendOtp = false;
      }
    }, 1000);
  }

  timeInSecs: number;
  ticker: any;
  countdown: string = '';

  startTimer(secs: number): void {
    this.timeInSecs = parseInt(secs.toString());
    this.ticker = setInterval(() => this.tick(), 1000);
  }

  tick(): void {
    let secs = this.timeInSecs;
    if (secs > 0) {
      this.timeInSecs--;
    } else {
      clearInterval(this.ticker);
      this.startTimer(5 * 60); // 5 minutes in seconds
    }

    let mins = Math.floor(secs / 60);
    secs %= 60;
    this.countdown = (mins < 10 ? '0' : '') + mins + ':' + (secs < 10 ? '0' : '') + secs;
  }

  onBackToLogin() {
    this.disabledBackToLogin = true;
    this.modalService.dismissAll();
    this.router.navigate([LOGIN]);
  }

  protected readonly ICON_CANCEL = ICON_CANCEL;
}
