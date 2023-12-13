import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthServerProvider } from '../../core/auth/auth-jwt.service';
import { ConfirmDialogComponent } from '../modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../../pages/order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from '../../constants/modal.const';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HOME } from '../../constants/app.routing.constants';
import { LoginService } from '../../pages/login/login.service';
import { AccountService } from '../../core/auth/account.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthServerProvider,
    private router: Router,
    protected modalService: NgbModal,
    private loginService: LoginService,
    private authServerProvider: AuthServerProvider,
    private accountService: AccountService
  ) {}
  checkModalRef: NgbModalRef;
  async canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
    const returnUrl = state.url; // Lấy đường dẫn hiện tại
    localStorage.setItem('currentUrl', returnUrl);
    if (next.data.permissions) {
      let permission = await new Promise(resolve => {
        this.accountService.identity().subscribe(identity => {
          resolve(this.accountService.hasAnyAuthority(next.data.permissions));
        });
      });
      if (!permission) {
        this.router.navigate(['./easy-pos/home']);
      }
      return true;
    }
    if (!this.authService.isJwtExpired()) {
      if (returnUrl.includes('/login') || returnUrl == '/') {
        this.router.navigate(['./easy-pos/home']);
      }
      return true;
    } else {
      if (!returnUrl.includes('/login') && returnUrl !== '/') {
        localStorage.setItem('returnUrl', returnUrl); // Lưu lại đường dẫn trước đó
        this.showErrorModel();
        return false;
      }
      return true;
    }
  }
  showErrorModel() {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.checkModalRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
    this.checkModalRef.componentInstance.value = new DialogModal(
      ModalHeader.LOGIN_EPX,
      ModalContent.LOGIN_EPX,
      ModalBtn.AGREE,
      'arrow-up-from-bracket',
      '',
      true
    );

    this.checkModalRef.componentInstance.formSubmit.subscribe(() => {
      this.authServerProvider.logout();
      this.checkModalRef.close();
      this.router.navigate(['./login']);
      return;
    });
    this.checkModalRef.result.then(() => {
      this.authServerProvider.logout();
      this.checkModalRef.close();
      this.router.navigate(['./login']);
      this.loginService.setCompaniesToNull();
      return;
    });
  }
}
