import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { AccountService } from '../../core/auth/account.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

/**
 * @whatItDoes Conditionally includes an HTML element if current user has any
 * of the authorities passed as the `expression`.
 *
 * @howToUse
 * ```
 *     <some-element *jhiHasAuthorityAndBoolean="'ROLE_ADMIN'">...</some-element>
 *      <some-element *jhiHasAuthorityAndBoolean="1 === 1">...</some-element>
 *     <some-element *jhiHasAuthorityAndBoolean="['ROLE_ADMIN', 1 === 1]">...</some-element>
 * ```
 */
@Directive({
  selector: '[jhiHasAuthorityAndBoolean]',
})
export class HasAnyAuthorityAndBooleanDirective {
  private authoritiesAndBoolean!: string | string[];

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private templateRef: TemplateRef<any>, private viewContainerRef: ViewContainerRef) {}

  @Input()
  set jhiHasAuthorityAndBoolean(value: string | string[]) {
    this.authoritiesAndBoolean = [];
    let isTrue: boolean;
    isTrue = true;
    for (const v of value) {
      if (v === undefined || v === 'undefined' || v === null || v === 'null') {
        isTrue = false;
        break;
      }
      if (typeof v === 'string') {
        this.authoritiesAndBoolean.push(v);
      } else if (typeof v === 'boolean') {
        if (v === false) {
          isTrue = false;
          break;
        }
      } else {
        this.authoritiesAndBoolean = [...this.authoritiesAndBoolean, v];
      }
    }
    this.updateView(isTrue);
    // Get notified each time authentication state changes.
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.updateView(isTrue);
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private updateView(isTrue: boolean): void {
    const hasAnyAuthority = this.accountService.hasAnyAuthority(this.authoritiesAndBoolean);
    this.viewContainerRef.clear();
    if (hasAnyAuthority && isTrue) {
      this.viewContainerRef.createEmbeddedView(this.templateRef);
    }
  }
}
