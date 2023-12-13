import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { last_config_device, last_user } from '../../object-stores.constants';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { PasswordService } from './password.service';
import { TranslateService } from '@ngx-translate/core';
import { LoginService } from '../../pages/login/login.service';
import { BaseComponent } from '../../shared/base/base.component';
import { ToastrService } from 'ngx-toastr';
import { LoadingOption } from '../../utils/loadingOption';
import { LocalStorageService } from 'ngx-webstorage';
import { Router } from '@angular/router';
import { UtilsService } from '../../utils/Utils.service';
import { HOME } from '../../constants/app.routing.constants';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DataEncrypt } from '../../entities/indexDatabase/data-encrypt.model';
import {ICON_PASSWORD_INVISIBLE, ICON_PASSWORD_VISIBLE} from "../../shared/other/icon";
@Component({
  selector: 'jhi-password',
  templateUrl: './password.component.html',
  styleUrls: ['./password.component.scss'],
})
export class PasswordComponent extends BaseComponent implements OnInit {
  lastCompany: any;
  isLogout: boolean = false;
  doNotMatch = false;
  error = false;
  success = false;
  lastUser: any;

  passwordForm = new FormGroup({
    currentPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(6), Validators.maxLength(50)],
    }),
    newPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(6), Validators.maxLength(50)],
    }),
    confirmPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(6), Validators.maxLength(50)],
    }),
    optionLogoutControl: new FormControl(false),
  });
  passwordVisible: boolean = false;
  newPasswordVisible: boolean = false;
  confirmPasswordVisible: boolean = false;
  isLoading: boolean = false;
  protected readonly HOME = HOME;

  constructor(
    protected toast: ToastrService,
    private passwordService: PasswordService,
    private accountService: AccountService,
    public translateService: TranslateService,
    private loginService: LoginService,
    private localStorageService: LocalStorageService,
    public activeModal: NgbActiveModal
  ) {
    super();
    this.lastUser = {};
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    this.lastUser = await this.getFirstItemIndexDB(last_user);
  }

  changePassword(): void {
    this.error = false;
    this.success = false;
    this.doNotMatch = false;

    const { newPassword, confirmPassword, currentPassword, optionLogoutControl } = this.passwordForm.getRawValue();
    if (newPassword !== confirmPassword) {
      this.doNotMatch = true;
      this.toast.error(
        this.translateService.instant('global.messages.error.dontmatch'),
        this.translateService.instant('global.info.notify')
      );
    } else {
      if (optionLogoutControl) this.isLogout = optionLogoutControl;
      this.isLoading = true;
      this.passwordService.save(currentPassword, newPassword, confirmPassword, this.isLogout).subscribe(
        async res => {
          this.isLoading = false;
          if (res.status) {
            if (optionLogoutControl) {
              // set jwt cuar lastuser trong index db
              let user = await this.findByID(last_user, this.lastCompany.ownerId);
              user.jwt = res.data.id_token;
              this.updateById(
                last_user,
                this.lastCompany.ownerId,
                new DataEncrypt(this.lastCompany.ownerId, this.encryptFromData({ ...user }))
              );
              // set jwt trong local storage
              this.localStorageService.store('authenticationToken', res.data.id_token);
            }
            this.success = true;
            this.toast.success(res.message[0].message, this.translateService.instant('global.info.notify'));
          }
          this.activeModal.close(true);
        },
        error => {
          this.error = true;
          this.isLoading = false;
        }
      );
    }
  }
  dismiss() {
    this.activeModal.dismiss();
  }

    protected readonly ICON_PASSWORD_INVISIBLE = ICON_PASSWORD_INVISIBLE;
    protected readonly ICON_PASSWORD_VISIBLE = ICON_PASSWORD_VISIBLE;
}
