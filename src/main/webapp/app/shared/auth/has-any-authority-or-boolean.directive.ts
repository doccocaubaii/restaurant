import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { Subject } from 'rxjs';
import { AccountService } from '../../core/auth/account.service';
import { takeUntil } from 'rxjs/operators';

/**
 * @whatItDoes Conditionally includes an HTML element if current user has any
 * of the authorities passed as the `expression`.
 *
 * @howToUse
 * ```
 *     <some-element *sdsHasAuthorityOrBoolean="'ROLE_ADMIN'">...</some-element>
 *      <some-element *sdsHasAuthorityOrBoolean="1 === 1">...</some-element>
 *     <some-element *sdsHasAuthorityOrBoolean="['ROLE_ADMIN', 1 === 1]">...</some-element>
 * ```
 */
@Directive({
  selector: '[jhiHasAuthorityOrBoolean]',
})
export class HasAnyAuthorityOrBooleanDirective {
  private authoritiesOrBoolean!: string | string[];
  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private templateRef: TemplateRef<any>, private viewContainerRef: ViewContainerRef) {}

  @Input()
  set jhiHasAuthorityOrBoolean(value: any | any[]) {
    this.authoritiesOrBoolean = [];
    let isTrue: boolean;
    isTrue = false;
    for (const v of value) {
      if (typeof v === 'string') {
        this.authoritiesOrBoolean.push(v);
      } else if (typeof v === 'boolean') {
        if (v === true) {
          isTrue = true;
        }
      } else {
        this.authoritiesOrBoolean = [...this.authoritiesOrBoolean, v];
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
    const hasAnyAuthority = this.accountService.hasAnyAuthority(this.authoritiesOrBoolean);
    this.viewContainerRef.clear();
    if (hasAnyAuthority || isTrue) {
      this.viewContainerRef.createEmbeddedView(this.templateRef);
    }
  }
}
