import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';

@Injectable({ providedIn: 'root' })
export class LoginService {
  constructor(private accountService: AccountService, private authServerProvider: AuthServerProvider) {}

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
    this.authServerProvider.logout().subscribe({ complete: () => this.accountService.authenticate(null) });
  }
}
