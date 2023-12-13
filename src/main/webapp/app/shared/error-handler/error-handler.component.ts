import { ErrorHandler, Injectable } from '@angular/core';
import { IndexDBError } from '../../entities/error/IndexDBError';
import { LoginService } from '../../pages/login/login.service';
import { Router } from '@angular/router';
import { INDEX_DB_ERROR } from '../../constants/error.constants';
import { last_owner_device } from '../../object-stores.constants';
import { BaseComponent } from '../base/base.component';

@Injectable()
export class ErrorHandlerComponent extends BaseComponent implements ErrorHandler {
  constructor(private loginService: LoginService, private router: Router) {
    super();
  }

  handleError(error): void {
    if (error instanceof IndexDBError || error.message?.toString().includes(INDEX_DB_ERROR)) {
      this.loginService.logout();
      this.deleteAll(last_owner_device);
      this.router.navigate(['/login']);
    } else {
      console.error('Unhandled Error:', error);
    }
    return;
  }
}
