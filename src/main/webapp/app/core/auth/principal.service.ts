import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { AccountService } from './account.service';

@Injectable({ providedIn: 'root' })
export class Principal {
  private userIdentity: any;
  private arayAuthorities = [];
  private authenticated = false;
  private authenticationState = new Subject<any>();

  constructor(private account: AccountService) {}

  authenticate(identity) {
    this.userIdentity = identity;
    this.authenticated = identity !== null;
    this.authenticationState.next(this.userIdentity);
  }

  hasAnyAuthority(authorities: string[]): Promise<boolean> {
    return Promise.resolve(this.hasAnyAuthorityDirect(authorities));
  }

  hasAnyAuthorityDirect(authorities: string[]): boolean {
    if (!this.authenticated || !this.userIdentity || !this.userIdentity.authorities) {
      return false;
    }

    for (let i = 0; i < authorities.length; i++) {
      this.filterAuthForSDSAdmin();
      if (this.userIdentity.authorities.includes(authorities[i])) {
        return true;
      }
    }

    return false;
  }

  hasAuthority(authority: string): Promise<boolean> {
    if (!this.authenticated) {
      return Promise.resolve(false);
    }

    return this.identity().then(
      id => {
        return Promise.resolve(id.authorities && id.authorities.includes(authority));
      },
      () => {
        return Promise.resolve(false);
      }
    );
  }

  hasAllAuthority(authority: string[]): Promise<boolean> {
    if (!this.authenticated) {
      return Promise.resolve(false);
    }

    return this.identity().then(
      id => {
        if (!id.authorities) {
          return Promise.resolve(false);
        }
        let isTrue = true;
        for (const v of authority) {
          if (!id.authorities.includes(v)) {
            isTrue = false;
          }
        }
        return Promise.resolve(isTrue);
      },
      () => {
        return Promise.resolve(false);
      }
    );
  }

  identity(force?: boolean): Promise<any> {
    if (force === true) {
      this.userIdentity = undefined;
    }
    // check and see if we have retrieved the userIdentity data from the server.
    // if we have, reuse it by immediately resolving
    if (this.userIdentity) {
      return Promise.resolve(this.userIdentity);
    }

    // retrieve the userIdentity data from the server, update the identity object, and then resolve.
    return this.account
      .getAuthenticationState()
      .toPromise()
      .then((response: any) => {
        const account = response.body;
        if (account && account.login == 'admin') {
          this.authenticated = true;
          this.userIdentity = account;
          return this.userIdentity;
        } else if (account) {
          let org = account.ebUserPackage.companyID;
          window.localStorage.setItem('org', `${org}`);
          window.sessionStorage.setItem('org', `${org}`);
          this.userIdentity = account;
          this.authenticated = true;
        } else {
          this.userIdentity = null;
          this.authenticated = false;
        }
        this.authenticationState.next(this.userIdentity);
        this.filterAuthForSDSAdmin();
        return this.userIdentity;
      })
      .catch(err => {
        this.userIdentity = null;
        this.authenticated = false;
        this.authenticationState.next(this.userIdentity);
        return null;
      });
  }

  filterAuthForSDSAdmin() {
    if (localStorage.getItem('checkSDSAmin') != null) {
      for (let i = 0; i < this.userIdentity.authorities.length; i++) {
        if (
          this.userIdentity.authorities[i].length == 6 &&
          (this.userIdentity.authorities[i].substring(4) == '01' ||
            this.userIdentity.authorities[i].substring(4) == '06' ||
            this.userIdentity.authorities[i].substring(4) == '07')
        ) {
          // @ts-ignore
          if (!this.arayAuthorities.includes(this.userIdentity.authorities[i])) {
            // @ts-ignore
            this.arayAuthorities.push(this.userIdentity.authorities[i]);
          }
        } else if (this.userIdentity.authorities[i].length < 6) {
          // @ts-ignore
          if (!this.arayAuthorities.includes(this.userIdentity.authorities[i])) {
            // @ts-ignore
            this.arayAuthorities.push(this.userIdentity.authorities[i]);
          }
        }
      }
      // @ts-ignore
      this.arayAuthorities.push('ROLE_USER');
      this.userIdentity.authorities = [];
      this.userIdentity.authorities = this.arayAuthorities;
    }
  }

  isAuthenticated(): boolean {
    return this.authenticated;
  }

  isIdentityResolved(): boolean {
    return this.userIdentity !== undefined;
  }

  getAuthenticationState(): Observable<any> {
    return this.authenticationState.asObservable();
  }

  getImageUrl(): string {
    return this.isIdentityResolved() ? this.userIdentity.imageUrl : null;
  }
}
