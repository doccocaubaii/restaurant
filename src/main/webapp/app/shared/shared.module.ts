import { NgModule } from '@angular/core';

import { SharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { TranslateDirective } from './language/translate.directive';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { DurationPipe } from './date/duration.pipe';
import { FormatMediumDatetimePipe } from './date/format-medium-datetime.pipe';
import { FormatMediumDatePipe } from './date/format-medium-date.pipe';
import { SortByDirective } from './sort/sort-by.directive';
import { SortDirective } from './sort/sort.directive';
import { ItemCountComponent } from './pagination/item-count.component';
import { FilterComponent } from './filter/filter.component';
import { ModalDialogComponent } from './modal/modal-dialog.component';
import { PrintPageComponent } from '../pages/print-page/print-page.component';
import { HasAnyAuthorityAndBooleanDirective } from './auth/has-any-authority-and-boolean.directive';
import { HasAnyAuthorityOrBooleanDirective } from './auth/has-any-authority-or-boolean.directive';
import { NumberFormatPipe } from './date/number-format.pipe';
import { NgSelectModule } from '@ng-select/ng-select';
import { ToastrModule } from 'ngx-toastr';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmDialogComponent } from './modal/confirm-dialog/confirm-dialog.component';
import { PrintInvoiceComponent } from './print-invoice/print-invoice.component';
import { DisableDirective } from './directive/disable.directive';
import { CurrencyDirective } from './directive/currentcy.directive';
import { NumberOnlyDirective } from './other/number-only.directive';

@NgModule({
  imports: [SharedLibsModule, NgSelectModule, ToastrModule.forRoot(), NgbPaginationModule],
  declarations: [
    ConfirmDialogComponent,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    HasAnyAuthorityAndBooleanDirective,
    HasAnyAuthorityOrBooleanDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    NumberFormatPipe,
    SortByDirective,
    SortDirective,
    NumberOnlyDirective,
    ItemCountComponent,
    FilterComponent,
    ModalDialogComponent,
    PrintPageComponent,
    PrintInvoiceComponent,
    DisableDirective,
    CurrencyDirective,
  ],
  exports: [
    SharedLibsModule,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    HasAnyAuthorityAndBooleanDirective,
    HasAnyAuthorityOrBooleanDirective,
    DisableDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    NumberFormatPipe,
    SortByDirective,
    SortDirective,
    NumberOnlyDirective,
    ItemCountComponent,
    FilterComponent,
    ModalDialogComponent,
    PrintPageComponent,
    NgSelectModule,
    ToastrModule,
    NgbPaginationModule,
    PrintInvoiceComponent,
    CurrencyDirective,
  ],
})
export class SharedModule {}
