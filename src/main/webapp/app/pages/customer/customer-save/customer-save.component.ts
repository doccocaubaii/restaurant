import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../../core/auth/account.service';
import { Account } from '../../../core/auth/account.model';
import { Customer } from '../../../entities/customer/customer';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from '../../../shared/base/base.component';
import { CustomerService } from '../customer.service';
import { ToastrService } from 'ngx-toastr';
import { LoadingOption } from '../../../utils/loadingOption';
import { listProvices } from '../../../entities/shared/province.entity';
import { CustomerCard } from '../../../entities/customer/customer-card';
import { CardDefaultItem } from '../../../entities/customer/card-default-item';
import { ConvertResponse } from '../../../config/convert-response';
import { ImportCustomerComponent } from '../../import/excel/customer/import-customer.component';
import { ConfirmDialogComponent } from 'app/shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../../order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from '../../../constants/modal.const';
import { UtilsService } from '../../../utils/Utils.service';
import dayjs from 'dayjs/esm';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";
@Component({
  selector: 'jhi-form-create',
  templateUrl: './customer-save.component.html',
  styleUrls: ['./customer-save.component.scss'],
})
export class CustomerSaveComponent extends BaseComponent implements OnInit {
  currentAccount: Account | null = null;
  listProvinces = listProvices;
  isSuplier = false;
  birthday: any;
  cus: Customer = {
    address: '',
    gender: 3,
    city: '',
    code: '',
    code2: '',
    comId: 0,
    createTime: '',
    description: '',
    district: '',
    email: '',
    id: 0,
    idNumber: null,
    name: '',
    phoneNumber: '',
    taxCode: '',
    type: 1,
    updateTime: '',
    cardInformation: new CustomerCard(),
    group: null,
    check: false,
    pointBalance: null,
    moneyBalance: null,
    birthday: null,
  };
  private modalRef2: NgbModalRef | undefined;

  company: any;
  companyId: number;
  noCard = false;
  constructor(
    private customerService: CustomerService,
    public activeModal: NgbActiveModal,
    private toastr: ToastrService,
    public loading: LoadingOption,
    private modalService: NgbModal,
    protected utilsService: UtilsService
  ) {
    super();
  }

  async ngOnInit() {
    if (this.isSuplier) {
      this.cus.type = 3;
    }
    this.company = await this.getCompany();
    this.companyId = this.company.id;
    if (!this.cus.cardInformation.cardId) {
      // check card default in company
      this.customerService.getCardDefaultInCompany(this.companyId).subscribe(response => {
        if (response.ok) {
          const responseConvert = ConvertResponse.getDataFromServer(response, false);
          let cardResult: CardDefaultItem;
          cardResult = responseConvert.data;
          if (!responseConvert.status) this.noCard = true;
          this.cus.cardInformation.cardName = cardResult.name;
        }
      });
    }
    if (this.cus?.birthday) {
      this.birthday = dayjs(this.cus.birthday);
    }
  }
  private readonly REGEX_TAX_CODE = /^(?:\d{10}|\d{10}-\d{3})$/;
  presave() {
    if (this.noCard && (this.cus.cardInformation.point || this.cus.cardInformation.amount)) {
      this.modalRef2 = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
      this.modalRef2.componentInstance.value = new DialogModal(
        ModalHeader.SAVE_CARD_NON_DEFAULT,
        ModalContent.SAVE_CARD_NON_DEFAULT,
        ModalBtn.AGREE,
        'check',
        'btn-save'
      );
      this.modalRef2.componentInstance.formSubmit.subscribe(
        res => {
          if (res) {
            // Sau khi bấm nút đồng ý thì thực hiện công việc và đóng model
            this.save();
            if (this.modalRef2) this.modalRef2.close();
          }
        },
        e => {
          return;
        }
      );
    } else this.save();
  }
  save() {
    if (this.birthday) {
      this.cus.birthday = this.utilsService.convertDate(this.birthday).toString().slice(0, 19).replace('T', ' ');
    }
    //on close
    this.cus.comId = this.companyId;
    // for (let key in this.cus) {
    //   if (this.cus[key] == '') {
    //     this.cus[key] = null;
    //   }
    // }
    if (this.cus.phoneNumber == '') this.cus.phoneNumber = null;
    if (this.cus.id) {
      //gọi api cập nhật
      this.customerService.putCustomer(this.cus as Customer).subscribe(response => {
        if (response.status) {
          this.toastr.success(response.message[0].message);
          this.activeModal.close(true);
        }
      });
    } else {
      //gọi api thêm mới
      this.customerService.postCustomer(this.cus as Customer).subscribe(response => {
        if (response.status) {
          this.toastr.success(response.message[0].message);
          const obj = {
            id: response.data.id,
            name: response.data.name,
          };
          this.activeModal.close(obj);
        }
      });
    }
  }

  dismiss() {
    this.activeModal.dismiss();
  }
  test(s: any) {
    alert(s);
  }

  getInfoFromTax() {
    if (this.cus.taxCode.length >= 10) {
      this.customerService.getInfoCustomerFromTax(this.cus.taxCode).subscribe(response => {
        if (response.status) {
          if (!response.data.data.ComName && !response.data.data.ComAddress) {
            this.toastr.error(response.message[0].message);
            return;
          }
          this.toastr.success(response.message[0].message);
          if (response.data.data.ComName) this.cus.name = response.data.data.ComName;
          if (response.data.data.ComAddress) this.cus.address = response.data.data.ComAddress;
        } else {
          this.toastr.error(response.message[0].message);
        }
      });
    }
  }

  isValidTaxCode(value: any) {
    if (value == null || value == '') return true;
    return this.REGEX_TAX_CODE.test(value);
  }

  isNumeric(value: any) {
    if (value == null || value == '') return true;
    const pattern = /^[0-9-]+$/; // Chỉ cho phép chuỗi chứa các chữ số
    return pattern.test(value);
  }
  isNumericWithZero(value: any) {
    if (value == null || value == '') return true;
    const pattern = /^0[0-9]*$/; // Chỉ cho phép chuỗi chứa các chữ số và chữ số đầu tiên là 0
    return pattern.test(value);
  }
  testMail(value: any) {
    if (value == null || value == '') return true;
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(value);
  }

  indexUpdate = 0;
  onChangeUpdateDetail(index: number) {
    this.indexUpdate = index;
  }

    protected readonly ICON_SAVE = ICON_SAVE;
  protected readonly ICON_CANCEL = ICON_CANCEL;
}
