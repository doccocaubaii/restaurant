import { Component, ElementRef, OnInit } from '@angular/core';
import { UtilsService } from '../../../utils/Utils.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { LoadingOption } from '../../../utils/loadingOption';
import { BaseComponent } from '../../../shared/base/base.component';
import { CardPolicyService } from './card-policy-service.component';
import { ConvertResponse } from '../../../config/convert-response';
import { CardPolicy } from '../../../entities/cardPolicy/card-policy';
import dayjs from 'dayjs/esm';
import { ConfirmDialogComponent } from '../../../shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../../order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from '../../../constants/modal.const';
import { CardPolicyApplyItem } from '../../../entities/cardPolicy/card-policy-apply-item';
import { Authority } from '../../../config/authority.constants';
import {ICON_CANCEL, ICON_EDIT_CART, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-card-policy',
  templateUrl: './card-policy.component.html',
  styleUrls: ['./card-policy.component.scss'],
})
export class CardPolicyComponent extends BaseComponent implements OnInit {
  paramCheckAll: boolean = false;
  paramCheckAllPage: any;
  listSelected: any = [];
  selectedItem: any = {};

  constructor(
    protected utilsService: UtilsService,
    private modalService: NgbModal,
    private toast: ToastrService,
    private elementRef: ElementRef,
    public loading: LoadingOption,
    public cardPolicyService: CardPolicyService
  ) {
    super();
  }

  keyword: string = '';
  tmp: any;
  idEditing: number = -1;
  company: any;
  companyId: number;

  conditionAll: CardPolicyApplyItem[] = [];
  conditionAllIds: number[] = [];
  cardPolicy: CardPolicy;
  cardPolicyType1: CardPolicy[] = [];
  cardPolicyType2: CardPolicy[] = [];
  type = 1;
  fromDate = dayjs();
  enableButtonSave = false;
  finalEnable = false;
  isSaved = false;
  authorVIEW = Authority.CARD_POLICY_VIEW;
  authorUPDATE = Authority.CARD_POLICY_UPDATE;

  async ngOnInit() {
    this.company = await this.getCompany();
    this.companyId = this.company.id;
    this.getAllCardPolicy();
  }

  changeType(number: number) {
    this.type = number;
    this.isSaved = false;
    this.finalEnable = true;
    this.enableButtonSave = true;
    this.cardPolicy.editTable = true;
    // this.refreshPolicy();
  }

  getAllCardPolicy() {
    this.cardPolicyService.getAllCardPolicy(this.companyId).subscribe(
      response => {
        if (response) {
          this.totalItems = response.count;
          this.totalPages = Math.ceil(this.totalItems / this.pageSize);
          this.cardPolicy = response.data;
          if (this.cardPolicy) {
            this.cardPolicy.conditions.forEach(item => {
              if (!this.conditionAllIds.includes(item.cardId) && item.checked) {
                this.conditionAll.push(item);
                this.conditionAllIds.push(item.cardId);
              } else if (this.conditionAllIds.includes(item.cardId)) {
                const index = this.conditionAllIds.indexOf(item.cardId);
                const { accumValue, redeemValue, upgradeValue, upgradeTime, checked } = this.conditionAll[index];
                item.upgradeTime = upgradeTime;
                item.accumValue = accumValue;
                item.redeemValue = redeemValue;
                item.upgradeValue = upgradeValue;
                item.checked = checked;
              }
            });
          }
          this.paginateData();
          if (this.cardPolicy?.fromDate) {
            this.fromDate = dayjs(this.cardPolicy.fromDate);
          } else {
            this.fromDate = dayjs();
          }
          if (this.cardPolicy?.upgradeType) {
            this.type = JSON.parse(JSON.stringify(this.cardPolicy.upgradeType));
          }
          if (this.isSaved) {
            this.finalEnable = false;
            this.isSaved = false;
          }
          if (this.finalEnable) {
            this.cardPolicy.editTable = true;
            this.enableButtonSave = true;
          }
        }
      },
      error => {
        this.toast.error(ConvertResponse.getDataFromServer(error, true));
      }
    );
  }

  getItem(card: any) {
    this.selectedItem = card;
  }

  dismiss() {
    this.conditionAllIds = [];
    this.conditionAll = [];
    this.currentPage = 1;
    this.finalEnable = false;
    this.enableButtonSave = false;
    this.getAllCardPolicy();
    // this.refreshPolicy();
    // this.cardPolicy.editTable = false;
  }

  refreshPolicy() {
    if (this.type === 1) {
      this.cardPolicy = this.cardPolicyType1[0];
    } else {
      this.cardPolicy = this.cardPolicyType2[0];
    }
    if (this.cardPolicy?.fromDate) {
      this.fromDate = dayjs(this.cardPolicy.fromDate);
    } else {
      this.fromDate = dayjs();
    }
  }

  private modalRef: NgbModalRef | undefined;

  preSave() {
    if (this.cardPolicy.upgradeType && this.cardPolicy.upgradeType !== this.type) {
      this.modalRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
      this.modalRef.componentInstance.value = new DialogModal(
        ModalHeader.SAVE_POLICY,
        ModalContent.SAVE_POLICY,
        ModalBtn.AGREE,
        'check',
        'btn-save'
      );
      this.modalRef.componentInstance.formSubmit.subscribe(res => {
        if (res) {
          // Sau khi bấm nút đồng ý thì thực hiện công việc và đóng model
          this.save();
          if (this.modalRef) this.modalRef.close();
        }
      });
    } else {
      this.save();
    }
  }

  save() {
    this.cardPolicy.upgradeType = this.type;
    this.cardPolicyService.saveCardPolicy(this.cardPolicy, this.fromDate, this.conditionAll, this.conditions).subscribe(
      response => {
        if (response.ok) {
          this.cardPolicy.editTable = false;
          this.enableButtonSave = false;
          this.toast.success(ConvertResponse.getDataFromServer(response, true));
          this.conditionAllIds = [];
          this.conditionAll = [];
          // this.conditions = [];
          this.isSaved = true;
          this.getAllCardPolicy();
        }
      },
      error => {
        this.toast.error(ConvertResponse.getDataFromServer(error, true));
      }
    );
  }

  editCardPolicy() {
    this.finalEnable = true;
    this.cardPolicy.editTable = true;
    this.enableButtonSave = true;
  }

  checkAllForPageCustom(objectList: any[], listSelected: any, paramCheckAllPage: boolean) {
    if (paramCheckAllPage) {
      objectList.forEach(n => (n.checked = true));
      for (let i = 0; i < objectList.length; i++) {
        listSelected.push(objectList[i].id);
      }
    } else {
      objectList.forEach(n => (n.checked = false));
      listSelected.splice(0, listSelected.length);
    }
  }

  loadMore() {
    this.getAllCardPolicy();
  }

  changeItem(item: CardPolicyApplyItem) {
    if (this.finalEnable) {
      if (item.checked) {
        if (this.conditionAllIds.includes(item.cardId)) {
          item.checked = false;
          const index = this.conditionAllIds.indexOf(item.cardId);
          if (index > -1) {
            this.conditionAll.splice(index, 1);
            this.conditionAllIds.splice(index, 1);
          }
        }
      } else {
        item.checked = true;
        if (!this.conditionAllIds.includes(item.cardId) && item.checked) {
          this.conditionAll.push(item);
          this.conditionAllIds.push(item.cardId);
        }
      }
    }
  }

  currentPage = 1;
  pageSize = 10;
  totalItems = 0;
  totalPages = 0;

  onPageChange(indexPage: number) {
    this.currentPage = indexPage;
    const mapConditionPage = new Map<number, CardPolicyApplyItem>();
    for (const item of this.conditions) {
      mapConditionPage.set(item.cardId, item);
    }
    this.cardPolicy.conditions.forEach(item => {
      if (mapConditionPage.has(item.cardId)) {
        const itemMap = mapConditionPage.get(item.cardId);
        item.upgradeTime = itemMap?.upgradeTime;
        item.accumValue = itemMap?.accumValue;
        item.redeemValue = itemMap?.redeemValue;
        item.upgradeValue = itemMap?.upgradeValue;
        item.checked = <boolean>itemMap?.checked;
      }
    });
    this.paginateData();
  }
  conditions: CardPolicyApplyItem[] = [];

  paginateData(): void {
    this.conditions = [];
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    const dataInPage = JSON.parse(JSON.stringify(this.cardPolicy.conditions.slice(startIndex, endIndex)));
    dataInPage.forEach(item => {
      if (this.conditionAllIds.includes(item.cardId)) {
        const index = this.conditionAllIds.indexOf(item.cardId);
        const { accumValue, redeemValue, upgradeValue, upgradeTime, checked } = this.conditionAll[index];
        item.upgradeTime = upgradeTime;
        item.accumValue = accumValue;
        item.redeemValue = redeemValue;
        item.upgradeValue = upgradeValue;
        item.checked = checked;
      }
      this.conditions.push(item);
    });
  }

  upgradeValueChange(item: CardPolicyApplyItem) {
    if (this.finalEnable) {
      if (item.upgradeValue !== null && item.upgradeValue !== undefined) {
        if (!this.conditionAllIds.includes(item.cardId)) {
          item.checked = true;
          if (!this.conditionAllIds.includes(item.cardId)) {
            this.conditionAll.push(item);
            this.conditionAllIds.push(item.cardId);
          }
        }
      } else {
        item.checked = false;
        const index = this.conditionAllIds.indexOf(item.cardId);
        if (index > -1) {
          this.conditionAll.splice(index, 1);
          this.conditionAllIds.splice(index, 1);
        }
      }
    }
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
    protected readonly ICON_EDIT_CART = ICON_EDIT_CART;
}
