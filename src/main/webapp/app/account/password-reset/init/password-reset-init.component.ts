import { Component, AfterViewInit, ElementRef, ViewChild, OnDestroy } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { PasswordResetInitService } from './password-reset-init.service';
import appSettings from 'app/config/app-settings';
import { ContentOption } from 'app/utils/contentOption';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { CHANGE_FORGOT_PASSWORD } from 'app/constants/app.routing.constants';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PasswordResetFinishComponent } from '../finish/password-reset-finish.component';
import {ICON_CANCEL} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-password-reset-init',
  templateUrl: './password-reset-init.component.html',
  styleUrls: ['./password-reset-init.component.scss'],
})
export class PasswordResetInitComponent implements AfterViewInit, OnDestroy {
  @ViewChild('input1') input1: ElementRef;
  @ViewChild('input2') input2: ElementRef;
  @ViewChild('input3') input3: ElementRef;
  @ViewChild('input4') input4: ElementRef;
  @ViewChild('input5') input5: ElementRef;
  @ViewChild('input6') input6: ElementRef;
  @ViewChild('email', { static: false })
  email?: ElementRef;
  appSettings = appSettings;
  isSaving = false;
  isConfirm = false;

  success = false;
  mess = '';
  remainingTime: number;
  resetRequestForm = this.fb.group({
    username: [
      '',
      [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(100),
        // Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$|^(\+\d{1,3}[- ]?)?\d{10,11}$/)
      ],
    ],
  });

  constructor(
    private passwordResetInitService: PasswordResetInitService,
    private fb: FormBuilder,
    private contentOption: ContentOption,
    public activeModal: NgbActiveModal,
    private toastr: ToastrService,
    private translateService: TranslateService,
    private modalService: NgbModal,
    private router: Router
  ) {
    this.appSettings.appEmpty = true;
    this.contentOption.isHiddenOrder = true;
  }

  ngAfterViewInit(): void {
    if (this.email) {
      this.email.nativeElement.focus();
    }
  }

  onInput(event: Event, current: HTMLInputElement, nextInput: HTMLInputElement): void {
    const currentInput = event.target as HTMLInputElement;
    if (currentInput.value === '-') {
      event.preventDefault();
      current.value = '';
      return;
    }
    const currentLength = currentInput.value.length;
    if (currentLength > 1) {
      current.value = currentInput.value.substring(1, 2);
    }

    if (currentLength >= 1) {
      nextInput.focus();
    }
    if (this.concatenateOtp().length < 6) {
      this.isConfirm = false;
    } else {
      this.isConfirm = true;
      this.confirmOtp();
    }
  }

  onPressBackspace(event: KeyboardEvent, currentIndex: number): void {
    if (event.key === 'Backspace' && currentIndex !== 0) {
      const previousInputElement = document.getElementById('input' + currentIndex) as HTMLInputElement;
      if (previousInputElement) {
        previousInputElement.focus();
      }
    }
  }

  resendOtp() {
    this.isSaving = true;
    let patternMail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    let patternPhone = /^(\+\d{1,3}[- ]?)?\d{10,11}$/;

    this.passwordResetInitService.save(this.resetRequestForm.get(['username'])!.value).subscribe(
      value => {
        this.isSaving = false;
        this.resetOTPBox();
        if (value.message[0].code === 'SUCCESS') {
          var username = this.resetRequestForm.get(['username'])!.value;
          if (patternMail.test(username)) {
            const index = username.indexOf('@');
            username = username.substring(0, index - 3) + '***' + username.substring(index, username.length);
            this.mess =
              this.translateService.instant('reset.request.messages.email.text1') +
              username +
              this.translateService.instant('reset.request.messages.email.text2');
          } else if (patternPhone.test(username)) {
            username = username.substring(0, username.length - 4) + '***' + username.substring(username.length - 1, username.length);
            this.mess =
              this.translateService.instant('reset.request.messages.phone.text1') +
              username +
              this.translateService.instant('reset.request.messages.phone.text2');
          } else {
            username = username.substring(0, username.length - 4) + '***' + username.substring(username.length - 1, username.length);
            this.mess =
              this.translateService.instant('reset.request.messages.username.text1') +
              username +
              this.translateService.instant('reset.request.messages.username.text2');
          }
          this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
          this.success = true;
          this.remainingTime = 60;
          let a = setInterval(() => {
            if (this.remainingTime != 0) {
              this.remainingTime -= 1;
            } else {
              clearInterval(a);
            }
          }, 1000);
        }
      },
      error => (this.isSaving = false)
    );
  }

  resetOTPBox() {
    this.input1.nativeElement.value = '';
    this.input2.nativeElement.value = '';
    this.input3.nativeElement.value = '';
    this.input4.nativeElement.value = '';
    this.input5.nativeElement.value = '';
    this.input6.nativeElement.value = '';
    this.input1.nativeElement.focus();
  }

  requestReset(): void {
    this.isSaving = true;
    let patternMail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    let patternPhone = /^(\+\d{1,3}[- ]?)?\d{10,11}$/;

    this.passwordResetInitService.save(this.resetRequestForm.get(['username'])!.value).subscribe(
      value => {
        this.isSaving = false;
        if (value.message[0].code === 'SUCCESS') {
          var username = this.resetRequestForm.get(['username'])!.value;
          if (patternMail.test(username)) {
            const index = username.indexOf('@');
            username = username.substring(0, index - 3) + '***' + username.substring(index, username.length);
            this.mess =
              this.translateService.instant('reset.request.messages.email.text1') +
              username +
              this.translateService.instant('reset.request.messages.email.text2');
          } else if (patternPhone.test(username)) {
            username = username.substring(0, username.length - 4) + '***' + username.substring(username.length - 1, username.length);
            this.mess =
              this.translateService.instant('reset.request.messages.phone.text1') +
              username +
              this.translateService.instant('reset.request.messages.phone.text2');
          } else {
            username = username.substring(0, username.length - 4) + '***' + username.substring(username.length - 1, username.length);
            this.mess =
              this.translateService.instant('reset.request.messages.username.text1') +
              username +
              this.translateService.instant('reset.request.messages.username.text2');
          }
          this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
          this.success = true;
          this.remainingTime = 60;
          let a = setInterval(() => {
            if (this.remainingTime != 0) {
              this.remainingTime -= 1;
            } else {
              clearInterval(a);
            }
          }, 1000);
        }
      },
      error => (this.isSaving = false)
    );
  }

  concatenateOtp(): string {
    const otpArray = [
      this.input1.nativeElement.value,
      this.input2.nativeElement.value,
      this.input3.nativeElement.value,
      this.input4.nativeElement.value,
      this.input5.nativeElement.value,
      this.input6.nativeElement.value,
    ];

    return otpArray.join('');
  }

  dismiss(value: any) {
    this.activeModal.close(value);
  }

  confirmOtp() {
    const otpValue = this.concatenateOtp();
    if (otpValue.length != 6) {
      return;
    }
    let req = {
      username: this.resetRequestForm.get(['username'])!.value,
      otp: otpValue,
    };

    this.passwordResetInitService.confirmOtp(req).subscribe(value => {
      this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
      if (value.message[0].code === 'SUCCESS') {
        this.activeModal.close();
        this.passwordResetInitService.saveData = req;
        this.modalService.open(PasswordResetFinishComponent, {
          size: 'dialog-centered',
          backdrop: 'static',
        });
      }
    });
  }
  ngOnDestroy() {
    this.contentOption.isHiddenOrder = true;
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
}
