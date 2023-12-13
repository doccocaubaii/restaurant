import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { BaseComponent } from '../../shared/base/base.component';
import {
  area,
  customer,
  last_company,
  last_config_device,
  last_permission,
  last_print_config,
  last_print_config_detail,
  last_sync,
  last_user,
  product,
  product_group,
} from '../../object-stores.constants';

@Injectable({ providedIn: 'root' })
export class LoginService extends BaseComponent {
  private companiesSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  getCompanies(): Observable<any> {
    return this.companiesSubject.asObservable();
  }

  setCompaniesToNull() {
    this.companiesSubject.next(null);
  }

  constructor(private accountService: AccountService, private authServerProvider: AuthServerProvider) {
    super();
  }

  login(credentials: any): Observable<any | null> {
    return this.authServerProvider.login(credentials).pipe(
      map((res: any) => {
        if (res && !res.status) {
          return res;
        } else {
          this.accountService.identity(true);
          return res;
        }
        // mergeMap(() => this.accountService.identity(true));
      })
    );
  }

  logout(): void {
    this.authServerProvider.logout().subscribe({
      complete: () => {
        this.accountService.authenticate(null);
        this.deleteAll(last_print_config);
        this.deleteAll(product_group);
        this.deleteAll(customer);
        this.deleteAll(area);
        this.deleteAll(product);
        this.deleteAll(last_company);
        // this.deleteAll(last_config_device);
        // this.deleteAll(last_owner_device);
        this.deleteAll(last_permission);
        this.deleteAll(last_print_config);
        this.deleteAll(last_print_config_detail);
        this.deleteAll(last_sync);
        this.deleteAll(last_user);
      },
    });
  }
}
