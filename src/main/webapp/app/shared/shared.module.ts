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
import { ConfirmExitDialogComponent } from './modal/confirm-exit-dialog/confirm-exit-dialog.component';
import { StringNumberOnlyDirective } from './other/string-number-only.directive';
import { DateFormatPipe } from './date/date-format.pipe';
import { ResponseModalComponent } from './modal/response-modal/response-modal.component';
import { BaseFormProductComponent } from 'app/pages/base-form-product/base-form-product/base-form-product.component';
import { BaseCreateProductComponent } from 'app/pages/base-create-product/base-create-product/base-create-product.component';
import { ShortDateFormatPipe } from './date/short-date-format.pipe';
import { CustomDatepickerDirective } from './other/custom-date-picker.directive';
import { PositiveNumberPipe } from './date/positive-number.pipe';
import { PositiveNumberDirective } from './directive/positive-number.directive';
import { MediumDateFormatPipe } from './date/medium-date-format.pipe';
import { SafeHtmlPipe } from './other/pipe-html.pipe';

import { JhiJsonCustomDirective } from './directive/jhi-json-custom.directive';
import { DateFormatDirective } from './other/date-format.directive';
import { NoEmojiDirective } from './directive/no-emoji.directive';
import { EnterFocusDirective } from './directive/enter-focus-directive';
import { RemoveProductComponent } from './modal/remove-product/remove-product.component';
import { JhiJsonCustomOrDirective } from './directive/jhi-json-custom-or.directive';
import { JhiJsonCustomAndDirective } from './directive/jhi-json-custom-and.directive';
import { SignaturesPopupComponent } from './signatures/signatures-popup.component';
import { DisableButtonDirective } from './directive/disbale-button.directive';

@NgModule({
  imports: [SharedLibsModule, NgSelectModule, ToastrModule.forRoot(), NgbPaginationModule],
  declarations: [
    ConfirmDialogComponent,
    ResponseModalComponent,
    ConfirmExitDialogComponent,
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
    StringNumberOnlyDirective,
    NumberOnlyDirective,
    ItemCountComponent,
    FilterComponent,
    ModalDialogComponent,
    PrintInvoiceComponent,
    DisableDirective,
    CurrencyDirective,
    DateFormatPipe,
    BaseFormProductComponent,
    BaseCreateProductComponent,
    ShortDateFormatPipe,
    MediumDateFormatPipe,
    CustomDatepickerDirective,
    PositiveNumberPipe,
    PositiveNumberDirective,
    DateFormatDirective,
    MediumDateFormatPipe,
    SafeHtmlPipe,
    JhiJsonCustomDirective,
    PositiveNumberDirective,
    DateFormatDirective,
    NoEmojiDirective,
    EnterFocusDirective,
    SignaturesPopupComponent,
    CustomDatepickerDirective,
    RemoveProductComponent,
    DisableButtonDirective,
    JhiJsonCustomOrDirective,
    JhiJsonCustomAndDirective,
    SafeHtmlPipe,
    RemoveProductComponent,
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
    NgSelectModule,
    ToastrModule,
    NgbPaginationModule,
    PrintInvoiceComponent,
    CurrencyDirective,
    StringNumberOnlyDirective,
    DateFormatPipe,
    BaseFormProductComponent,
    BaseCreateProductComponent,
    ShortDateFormatPipe,
    MediumDateFormatPipe,
    CustomDatepickerDirective,
    PositiveNumberPipe,
    PositiveNumberDirective,
    DateFormatDirective,
    MediumDateFormatPipe,
    SafeHtmlPipe,
    RemoveProductComponent,
    JhiJsonCustomDirective,
    PositiveNumberDirective,
    NoEmojiDirective,
    EnterFocusDirective,
    DisableButtonDirective,
    JhiJsonCustomOrDirective,
    JhiJsonCustomAndDirective,
    DisableButtonDirective,
  ],
})
export class SharedModule {}
