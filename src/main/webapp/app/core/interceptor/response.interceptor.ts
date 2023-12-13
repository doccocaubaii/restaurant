import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { AlertService } from '../util/alert.service';
import { ToastrService } from 'ngx-toastr';
import { PING, LOGIN, ACCOUNT, REGISTER } from '../../constants/api.constants';
import { SidebarOption } from '../../utils/SidebarOption';
import { LoadingOption } from '../../utils/loadingOption';
import { TranslateService } from '@ngx-translate/core';
import { AuthServerProvider } from '../auth/auth-jwt.service';
import { ConfirmDialogComponent } from '../../shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../../pages/order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from '../../constants/modal.const';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { ACCOUNT_EXPIRED_CODE, TRIAL_EXPIRED_CODE, TOKEN_EXPIRED_CODE } from '../../constants/common.constants';
import { BaseComponent } from '../../shared/base/base.component';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { ElementRefOption } from '../../utils/elementRef';
import { SYNC_ICON } from '../../shared/other/icon';

@Injectable({ providedIn: 'root' })
export class ResponseInterceptor extends BaseComponent implements HttpInterceptor {
  constructor(
    private toastrService: ToastrService,
    private router: Router,
    protected modalService: NgbModal,
    public loading: LoadingOption,
    public elementRefOption: ElementRefOption,
    private translateService: TranslateService,
    private authServerProvider: AuthServerProvider
  ) {
    super();
  }
  checkModalRef: NgbModalRef;
  isShowEPX: boolean;

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.authServerProvider.isJwtExpired() && !this.isShowEPX && !this.router.url.includes('login') && !this.router.url.includes('/')) {
      const contentModel = new DialogModal(
        ModalHeader.LOGIN_EPX,
        ModalContent.LOGIN_EPX,
        ModalBtn.AGREE,
        'arrow-up-from-bracket',
        '',
        true
      );
      this.showErrorModel(contentModel);
      return throwError('');
    } else {
      // if (this.checkModalRef) {
      //   this.checkModalRef.close();
      // }
      this.loading.isLoading = !request.url.includes(PING);
      const isShowError = !(request.method === 'GET');
      // @ts-ignore
      return next.handle(request).pipe(
        tap(
          (event: HttpEvent<any>) => {
            if (event instanceof HttpResponse) {
              // Xử lý response thành công ở đây
              if (!event.body.status && event.body.message && !event.url?.includes(LOGIN) && isShowError) {
                this.toastrService.error(event.body.message[0].message, this.translateService.instant('global.info.notify'));
              }
              this.loading.isLoading = false;
              if (this.elementRefOption.elementRef) {
                this.elementRefOption.elementRef.nativeElement.disabled = false;
                if (this.elementRefOption.oldIcon) {
                  this.setIcon(this.elementRefOption.oldIcon);
                }
              }
              return event.body;
            }
          },
          error => {
            this.loading.isLoading = false;
            if (this.elementRefOption.elementRef) {
              this.elementRefOption.elementRef.nativeElement.disabled = false;
              if (this.elementRefOption.oldIcon) {
                this.setIcon(this.elementRefOption.oldIcon);
              }
            }
            if (error instanceof HttpErrorResponse) {
              const errorBody = error.error;
              if (
                errorBody &&
                errorBody.message &&
                !error.url?.includes(LOGIN) &&
                !error.url?.includes(ACCOUNT) &&
                !error.url?.includes(REGISTER)
              ) {
                const messageCode = errorBody.message[0].code;
                // xử lý mã lỗi tài khoản dùng thử
                if ([TRIAL_EXPIRED_CODE, ACCOUNT_EXPIRED_CODE].includes(errorBody.message[0].code)) {
                  const contentModel = new DialogModal(
                    ModalHeader.EXPIRED,
                    messageCode === TRIAL_EXPIRED_CODE ? ModalContent.TRIAL_EXPIRED : ModalContent.ACCOUNT_EXPIRED,
                    ModalBtn.AGREE,
                    'check',
                    '',
                    true
                  );
                  this.showErrorModel(contentModel);
                  return;
                }
                // token hết hạn
                if (TOKEN_EXPIRED_CODE == messageCode) {
                  const contentModel = new DialogModal(
                    ModalHeader.LOGIN_EPX,
                    ModalContent.LOGIN_EPX,
                    ModalBtn.AGREE,
                    'arrow-up-from-bracket',
                    '',
                    true
                  );
                  this.showErrorModel(contentModel);
                  return;
                }
              }
              return errorBody;
            }
          }
        ),
        catchError((error: any) => {
          // Xử lý lỗi ở đây
          this.loading.isLoading = false;
          if (this.elementRefOption.elementRef) {
            this.elementRefOption.elementRef.nativeElement.disabled = false;
            if (this.elementRefOption.oldIcon) {
              this.setIcon(this.elementRefOption.oldIcon);
            }
          }
          if (error.url?.includes(PING)) {
            return throwError('');
          }
          if (error.error && error.error.message) {
            // if (error.error.message[0].code === 'USER_NOT_FOUND') {
            //   this.showErrorModel();
            // }
            if (isShowError) {
              if (error.error.message[0].message) {
                this.toastrService.error(error.error.message[0].message, this.translateService.instant('global.info.notify'));
              } else if (error.error.message[0].code) {
                this.toastrService.error(error.error.message[0].code, this.translateService.instant('global.info.notify'));
              }
            }
          }
          return throwError('');
        })
      );
    }
  }

  showErrorModel(contentModel: DialogModal) {
    if (this.checkModalRef) {
      this.checkModalRef.close();
      this.modalService.dismissAll();
    }
    this.checkModalRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
    this.checkModalRef.componentInstance.value = contentModel;
    this.isShowEPX = true;
    this.checkModalRef.componentInstance.formSubmit.subscribe(res => {
      if (res) {
        this.authServerProvider.logout();
        this.checkModalRef.close();
        this.router.navigate(['./login']);
        this.isShowEPX = false;
      }
    });
  }

  setElementRef(el: any) {
    this.elementRefOption.elementRef = el;
    let innerHtml = this.elementRefOption.elementRef.nativeElement.innerHTML;
    if (innerHtml.lastIndexOf('<span') != -1) {
      let oldIcon = innerHtml.substring(0, innerHtml.lastIndexOf('<span'));
      this.setIcon(SYNC_ICON);
      this.elementRefOption.oldIcon = oldIcon;
    }
  }

  setIcon(icon: any) {
    if (this.elementRefOption.elementRef) {
      let innerHtml = this.elementRefOption.elementRef.nativeElement.innerHTML;
      let oldText = innerHtml.substring(innerHtml.lastIndexOf('<span'));
      this.elementRefOption.elementRef.nativeElement.innerHTML = icon + oldText;
    }
  }
}
