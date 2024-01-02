import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { AlertService } from '../util/alert.service';
import { ToastrService } from 'ngx-toastr';
import { LOGIN, PING } from '../../constants/api.constants';
import { SidebarOption } from '../../utils/SidebarOption';
import { LoadingOption } from '../../utils/loadingOption';
import { TranslateService } from '@ngx-translate/core';

@Injectable()
export class ResponseInterceptor implements HttpInterceptor {
  constructor(private toastrService: ToastrService, public loading: LoadingOption, private translateService: TranslateService) {}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.loading.isLoading = !request.url.includes(PING);
    return next.handle(request).pipe(
      tap((event: HttpEvent<any>) => {
        this.loading.isLoading = false;
        if (event instanceof HttpResponse) {
          // Xử lý response thành công ở đây
          if (!event.body.status && event.body.message && !event.url?.includes(LOGIN)) {
            this.toastrService.error(event.body.message[0].message, this.translateService.instant('global.info.notify'));
          }
          return event.body;
        }
      }),
      catchError((error: HttpErrorResponse) => {
        // Xử lý lỗi ở đây
        this.loading.isLoading = false;
        if (error.url?.includes(PING)) {
          return throwError(error);
        }
        if (error.error && error.error.message) {
          if (error.error.message[0].message) {
            this.toastrService.error(error.error.message[0].message, this.translateService.instant('global.info.notify'));
          } else if (error.error.message[0].code) {
            this.toastrService.error(error.error.message[0].code, this.translateService.instant('global.info.notify'));
          }
        }
        return throwError(error);
      })
    );
  }
}
