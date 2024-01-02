import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { last_user } from '../../object-stores.constants';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { PasswordService } from './password.service';
import { TranslateService } from '@ngx-translate/core';
import { LoginService } from '../../pages/login/login.service';
import { BaseComponent } from '../../shared/base/base.component';
@Component({
  selector: 'jhi-password',
  templateUrl: './password.component.html',
})
export class PasswordComponent extends BaseComponent implements OnInit {
  lastCompany: any;
  isLogout: boolean = false;
  doNotMatch = false;
  error = false;
  success = false;
  lastUser: any;
  passwordForm = new FormGroup({
    currentPassword: new FormControl('', { nonNullable: true, validators: Validators.required }),
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

  constructor(
    private passwordService: PasswordService,
    private accountService: AccountService,
    public translateService: TranslateService,
    private loginService: LoginService
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
    } else {
      if (optionLogoutControl) this.isLogout = optionLogoutControl;
      this.passwordService.save(currentPassword, newPassword, confirmPassword, this.isLogout).subscribe({
        next: () => (this.success = true),
        error: () => (this.error = true),
      });
    }
  }
}
