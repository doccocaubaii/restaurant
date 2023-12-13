import { Component, ElementRef, Input, OnInit, Renderer2, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from '../../../shared/base/base.component';
import { CardItem } from '../card.model';
import { CardService } from '../card.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { Overlay, OverlayRef } from '@angular/cdk/overlay';
import { TemplatePortal } from '@angular/cdk/portal';
import { last_company } from '../../../object-stores.constants';
import { Router } from '@angular/router';
import { CARD_POLICY } from '../../../constants/app.routing.constants';
import { DataEncrypt } from '../../../entities/indexDatabase/data-encrypt.model';
import {ICON_AGREE, ICON_CANCEL, ICON_CARD_DEFAULT, ICON_CONFIRM_V2, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-card-modal',
  templateUrl: './card-modal.component.html',
  styleUrls: ['./card-modal.component.scss'],
})
export class CardModalComponent extends BaseComponent implements OnInit {
  @ViewChild(TemplateRef) _dialogTemplate: TemplateRef<any>;
  private _overlayRef: OverlayRef;
  private _portal: TemplatePortal;
  // 1: create, 2: sort, 3: delete
  @Input() type: number;
  @Input() lstCard: any;
  @Input() id: any;
  @Input() name: any;
  @Input() selectCard: any;
  @Input() oldCard: any;
  title: string;
  lastCompany: any = {};
  @Input() keyword: string;
  @Input() paramCheckAll: boolean;
  @Input() listSelected: any[] = [];
  deleteMultiple = false;
  cardName: any;
  isDefaultCheck = true;
  status = true;
  showAgain = false;

  countAll = 0;
  countSuccess = 0;
  countFalse = 0;
  data: any = [];

  constructor(
    private activeModal: NgbActiveModal,
    private elementRef: ElementRef,
    private service: CardService,
    private router: Router,
    private renderer: Renderer2,
    private translateService: TranslateService,
    private _overlay: Overlay,
    private _viewContainerRef: ViewContainerRef,
    private toastr: ToastrService
  ) {
    super();
  }

  onDrop(event: any) {
    const previousIndex = event.previousIndex;
    const currentIndex = event.currentIndex;

    if (currentIndex == 0) {
      this.toastr.error('Thẻ mặc định phải luôn là hạng 1', this.translateService.instant('easyPos.product.info.message'));
      return;
    }

    if (previousIndex !== currentIndex) {
      const item = this.lstCard.splice(previousIndex, 1)[0];
      this.lstCard.splice(currentIndex, 0, item);
    }

    for (let i = 0; i < this.lstCard.length; i++) {
      let item = this.lstCard[i];
      item.rank = i + 1;
    }
  }

  async ngOnInit() {
    this.innitData();
    this.lastCompany = await this.getCompany();
    if (!this.id) {
      this.selectCard = {
        name: '',
        isDefault: this.isDefaultCheck,
        status: 1,
      };
    }
    if (this.name) {
      this.name = this.name.toUpperCase();
    }
    if (this.type == 3) {
      if (this.paramCheckAll || this.listSelected.length > 0) {
        this.deleteMultiple = true;
      }
    }
  }

  async getCardDetail(id: number) {
    this.service.getCardById(id).subscribe(value => {
      this.selectCard = {};
      this.service.getCardById(id).subscribe(res => {
        this.selectCard = res.data;
      });
    });
  }

  changeDefault(event: any) {
    if (this.lstCard.length > 0) {
      this.selectCard.isDefault = !this.selectCard.isDefault;
    } else {
      event.preventDefault();
    }
  }

  changeStatus() {
    this.selectCard.status = (this.selectCard.status + 1) % 2;
  }

  checkSaveCard() {
    if (!this.selectCard.name) {
      this.toastr.error('Tên thẻ không được bỏ trống', this.translateService.instant('easyPos.product.info.message'));
      return;
    }
    if (this.oldCard && this.oldCard.isDefault && this.selectCard.status == 0) {
      this.toastr.error('Thẻ mặc định bắt buộc hoạt động', this.translateService.instant('easyPos.product.info.message'));
      return;
    }
    if ((this.selectCard.isDefault || this.lstCard.length == 0) && this.selectCard.status == 0) {
      this.toastr.error('Thẻ mặc định bắt buộc hoạt động', this.translateService.instant('easyPos.product.info.message'));
      return;
    }
    if (!this.selectCard.id) {
      let id = this.lstCard.find(card => card.isDefault)?.id;
      if (!id) {
        this.saveCard();
        return;
      }
      if (this.selectCard.isDefault) {
        this.type = 6;
        this.title = 'Xác nhận thẻ';
      } else {
        this.saveCard();
      }
      return;
    }
    if (this.lstCard.length > 0 && this.selectCard.isDefault) {
      let id = this.lstCard.find(card => card.isDefault)?.id;
      if (!id) {
        this.saveCard();
        return;
      }
      if (id != this.selectCard.id && this.selectCard.isDefault) {
        this.type = 6;
        this.title = 'Xác nhận thẻ';
      } else {
        this.saveCard();
      }
    } else if (this.lstCard.length && this.selectCard.status == 0) {
      this.type = 7;
      this.title = 'Xác nhận thẻ';
    } else {
      this.saveCard();
    }
  }

  saveCard() {
    if (!this.selectCard.name) {
      this.toastr.error('Tên thẻ không được bỏ trống', this.translateService.instant('easyPos.product.info.message'));
      return;
    }
    if (!this.id) {
      this.service.createCard(this.selectCard).subscribe(value => {
        if (this.lastCompany.showCardPopup) {
          this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
          this.activeModal.close('Saved');
        } else {
          this.type = 8;
          this.title = 'Thông báo';
        }
      });
    } else {
      this.service.updateCard(this.selectCard).subscribe(value => {
        this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
        this.activeModal.close('Saved');
      });
    }
  }

  async closeShowPopup() {
    if (this.showAgain) {
      const res: any = await this.updateById(
        last_company,
        this.lastCompany.id,
        new DataEncrypt(
          this.lastCompany.id,
          this.encryptFromData({
            ...this.lastCompany,
            showCardPopup: 1,
          })
        )
      );
    }
    this.activeModal.close('Saved');
  }

  changeShowAgain() {
    this.showAgain = !this.showAgain;
  }

  async goToPolicy() {
    if (this.showAgain) {
      const res: any = await this.updateById(
        last_company,
        this.lastCompany.id,
        new DataEncrypt(
          this.lastCompany.id,
          this.encryptFromData({
            ...this.lastCompany,
            showCardPopup: 1,
          })
        )
      );
    }
    this.activeModal.close('Closed');
    await this.router.navigate([CARD_POLICY]);
  }
  sortCard() {
    let sortList: any[] = [];
    for (let i = 0; i < this.lstCard.length; i++) {
      let item = this.lstCard[i];
      let req = {
        id: item.id,
        rank: item.rank,
      };
      sortList.push(req);
    }
    this.service.sortCard(sortList).subscribe(value => {
      this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
      this.activeModal.close('Saved');
    });
  }

  innitData() {
    if (this.type === 1) {
      if (this.lstCard) {
        for (let i = 0; i < this.lstCard.length; i++) {
          if (this.lstCard[i].isDefault) {
            this.isDefaultCheck = false;
          }
        }
      }
      this.title = 'Thêm thẻ thành viên';
      this.selectCard.isDefault = this.isDefaultCheck;
    } else if (this.type === 2) {
      this.title = 'Sắp xếp';
    } else if (this.type === 3) {
      this.title = 'Xoá thẻ';
    } else if (this.type === 4) {
      this.title = 'Cập nhật thẻ';
      this.isDefaultCheck = false;
    } else if (this.type === 6 || this.type === 7) {
      this.title = 'Xác nhận thẻ';
    }
  }

  dismiss($event: MouseEvent) {
    this.activeModal.dismiss();
  }

  closeConfirmPopup() {
    if (this.selectCard.id) {
      this.type = 4;
      this.title = 'Cập nhật thẻ';
    } else {
      this.type = 1;
      this.title = 'Thêm thẻ thành viên';
    }
  }

  closeDelete($event: MouseEvent) {
    this.activeModal.close('Deleted');
  }

  onSave() {
    // console.log(this.inputValue)
  }

  deleteCard() {
    if (!this.deleteMultiple) {
      if (this.listSelected.length == 1) {
        this.id = this.listSelected[0];
      }
      this.service.deleteCard(this.id).subscribe(value => {
        this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
        this.activeModal.close('Deleted');
      });
    } else {
      if (!this.paramCheckAll && this.listSelected.length == 0) {
        return;
      }
      let req = {
        comId: this.lastCompany.id,
        keyword: this.keyword,
        paramCheckAll: this.paramCheckAll,
        ids: this.listSelected,
      };
      this.service.deleteListCard(req).subscribe(value => {
        this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
        this.countAll = value.data.countAll;
        this.countSuccess = value.data.countSuccess;
        this.countFalse = value.data.countFalse;
        this.data = value.data.dataFalse;
        this.type = 5;
      });
    }
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
    protected readonly ICON_CARD_DEFAULT = ICON_CARD_DEFAULT;
    protected readonly ICON_AGREE = ICON_AGREE;
    protected readonly ICON_CONFIRM_V2 = ICON_CONFIRM_V2;
}
