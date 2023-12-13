import { Component, OnInit, AfterViewInit, ElementRef, ViewChild, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { PasswordResetFinishService } from './password-reset-finish.service';
import { ContentOption } from 'app/utils/contentOption';
import { PasswordResetInitService } from '../init/password-reset-init.service';
import * as CryptoJS from 'crypto-js';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import dayjs from 'dayjs';
import { LOGIN } from 'app/constants/app.routing.constants';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {
  ICON_CANCEL, ICON_PASSWORD_INVISIBLE,
  ICON_PASSWORD_VISIBLE,
} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-password-reset-finish',
  templateUrl: './password-reset-finish.component.html',
  styleUrls: ['./password-reset-finish.component.scss'],
})
export class PasswordResetFinishComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('newPassword', { static: false })
  newPassword?: ElementRef;
  saveData: any;
  HASHMD5 = 'c0d0ae95e47f72ebf05527b3be47520c';

  // initialized = false;
  doNotMatch = false;
  doNotMatchShow = false;
  error = false;
  success = false;
  key = '';

  passwordForm = new FormGroup({
    newPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(6), Validators.maxLength(50)],
    }),
    confirmPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(6), Validators.maxLength(50)],
    }),
  });

  constructor(
    private passwordResetFinishService: PasswordResetFinishService,
    private passwordResetInitService: PasswordResetInitService,
    private contentOption: ContentOption,
    private router: Router,
    private toastr: ToastrService,
    private translateService: TranslateService,
    public activeModal: NgbActiveModal,
    private route: ActivatedRoute
  ) {
    this.contentOption.isHiddenOrder = true;
  }

  ngOnInit(): void {
    // this.route.queryParams.subscribe(params => {
    //   if (params['key']) {
    //     this.key = params['key'];
    //   }
    //   this.initialized = true;
    // });
    this.saveData = this.passwordResetInitService.saveData;
    if (!this.saveData || !this.saveData.username) {
      this.router.navigate([LOGIN]);
    }
  }

  ngAfterViewInit(): void {
    if (this.newPassword) {
      this.newPassword.nativeElement.focus();
    }
    this.passwordResetInitService.saveData = {};
  }

  dismiss(value: any) {
    this.activeModal.close(value);
  }

  checkMatch() {
    this.doNotMatch = false;
    this.doNotMatchShow = false;
    const { newPassword, confirmPassword } = this.passwordForm.getRawValue();
    if (newPassword !== confirmPassword) {
      this.doNotMatch = true;
      if (confirmPassword.length >= 6 && confirmPassword.length <= 50 && newPassword.length >= 6 && newPassword.length <= 50) {
        this.doNotMatchShow = true;
      }
    } else {
      this.doNotMatch = false;
      this.doNotMatchShow = false;
    }
  }

  finishReset(): void {
    this.doNotMatch = false;
    this.error = false;

    const { newPassword, confirmPassword } = this.passwordForm.getRawValue();

    if (newPassword !== confirmPassword) {
      this.doNotMatch = true;
    } else {
      const dateTime = Math.floor(dayjs().valueOf() / 1000 / 300);
      let hashText = this.saveData.username + newPassword + dateTime + this.HASHMD5;

      let req = {
        username: this.saveData.username,
        newPassword: newPassword,
        confirmPassword: confirmPassword,
        otp: this.saveData.otp,
        hash: CryptoJS.MD5(hashText).toString(),
      };
      this.passwordResetFinishService.changePass(req).subscribe(value => {
        this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
        if (value.message[0].code === 'SUCCESS') {
          this.passwordResetInitService.saveData = req;
          this.activeModal.close();
        }
      });
    }
  }

  passwordVisible = false;
  showPassword() {
    this.passwordVisible ? (this.passwordVisible = false) : (this.passwordVisible = true);
  }

  passwordVisible2 = false;
  showPassword2() {
    this.passwordVisible2 ? (this.passwordVisible2 = false) : (this.passwordVisible2 = true);
  }

  ngOnDestroy() {
    // this.contentOption.isHiddenOrder = false;
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_PASSWORD_VISIBLE = ICON_PASSWORD_VISIBLE;
  protected readonly ICON_PASSWORD_INVISIBLE = ICON_PASSWORD_INVISIBLE;
}
