import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../shared/base/base.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CardModalComponent } from './modal/card-modal.component';
import { UtilsService } from '../../utils/Utils.service';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import appSettings from 'app/config/app-settings';
import { CardService } from './card.service';
import { Subject } from 'rxjs';
import { CardItem } from './card.model';
import { FilterProduct, Page } from '../product/product';
import { UpdateInvoiceComponent } from '../../layouts/modal/invoice-update/update-invoice.component';
import { Authority } from '../../config/authority.constants';
import {ICON_SORTTING_CART} from "../../shared/other/icon";

@Component({
  selector: 'jhi-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss'],
})
export class CardComponent extends BaseComponent implements OnInit {
  keyword: any;
  cardItems: any;
  totalItems: any;
  selectedCard: any = {};
  lstCard: CardItem[] = [];
  lstOriginalCard: CardItem[] = [];
  divItems = ['Khối 1', 'Khối 2', 'Khối 3'];
  appSettings = appSettings;
  lastCompany: any = {};
  keywordCategory$ = new Subject<string>();
  paramCheckAll: boolean = false;
  paramCheckAllPage: any;
  listSelected: any = [];
  selectedItem: any = {};
  filterCard: FilterProduct = { page: Page.PAGE_NUMBER, size: Page.PAGE_SIZE };
  idEditing: any;
  init = true;
  checkModalRef: NgbModalRef;
  authorADD = Authority.CARD_ADD;
  authorVIEW = Authority.CARD_VIEW;
  authorUPDATE = Authority.CARD_UPDATE;
  authorDELETE = Authority.CARD_DELETE;
  authorSORT = Authority.CARD_SORT;

  onDrop(event: any) {
    const previousIndex = event.previousIndex;
    const currentIndex = event.currentIndex;

    if (previousIndex !== currentIndex) {
      const item = this.divItems.splice(previousIndex, 1)[0];
      this.divItems.splice(currentIndex, 0, item);
    }
  }

  constructor(
    private service: CardService,
    protected utilsService: UtilsService,
    protected modalService: NgbModal,
    private translateService: TranslateService,
    private toastr: ToastrService
  ) {
    super();
    this.appSettings.appEmpty = false;
    this.appSettings.appContentFullHeight = false;
  }

  async ngOnInit() {
    this.selectedCard = {};
    this.keyword = '';
    this.lastCompany = await this.getCompany();
    this.onSearch();
    this.getListCard();
  }

  onSearch() {
    if (!this.keyword) {
      this.getListCard();
    }
  }

  private modalRef: NgbModalRef | undefined;

  onCardAction(type: number, card?: any) {
    if (this.modalRef) {
      this.modalRef.close();
    }
    if (type !== 4) {
      this.modalRef = this.modalService.open(CardModalComponent, { size: '', backdrop: 'static' });
      this.modalRef.componentInstance.type = type;
      this.modalRef.componentInstance.lstCard = JSON.parse(JSON.stringify(this.lstOriginalCard));
      this.modalRef.componentInstance.selectCard = {};
      if (type === 3) {
        this.modalRef.componentInstance.name = card.name;
        this.modalRef.componentInstance.id = card.id;
      }
      this.modalRef.closed.subscribe((res?: any) => {
        if (res == 'Saved' || res == 'Deleted') {
          this.init = true;
          this.getListCard();
          this.idEditing = -1;
        }
      });
    } else {
      this.service.getCardById(card.id).subscribe(res => {
        this.modalRef = this.modalService.open(CardModalComponent, { size: '', backdrop: 'static' });
        this.modalRef.componentInstance.type = type;
        this.modalRef.componentInstance.lstCard = JSON.parse(JSON.stringify(this.lstCard));
        // @ts-ignore
        this.modalRef.componentInstance.selectCard = res.data;
        this.modalRef.componentInstance.oldCard = JSON.parse(JSON.stringify(res.data));
        this.modalRef.componentInstance.id = card.id;
        this.modalRef.closed.subscribe((res?: any) => {
          if (res == 'Saved' || res == 'Deleted') {
            this.init = true;
            this.getListCard();
            this.idEditing = -1;
          }
        });
      });
    }
  }

  getItem(card: any) {
    this.selectedItem = card;
  }

  deleteMultiCard() {
    if (!this.paramCheckAll && this.listSelected.length == 0) {
      return;
    }
    this.modalRef = this.modalService.open(CardModalComponent, { size: '', backdrop: 'static' });
    this.modalRef.componentInstance.type = 3;
    this.modalRef.componentInstance.listSelected = this.listSelected;
    this.modalRef.componentInstance.keyword = this.keyword;
    this.modalRef.componentInstance.paramCheckAll = this.paramCheckAll;
    this.modalRef.closed.subscribe((res?: any) => {
      if (res == 'Deleted') {
        this.getListCard();
        this.idEditing = -1;
      }
    });
  }
  viewCard(card: any) {
    this.idEditing = this.idEditing == card.id ? -1 : card.id;
    this.service.getCardById(card.id).subscribe(res => {
      this.selectedCard = res.data;
    });
  }

  loadMore($event: any) {
    if (this.paramCheckAllPage && !this.paramCheckAll) {
      this.paramCheckAllPage = false;
      this.listSelected = [];
    }
    this.filterCard.page = $event;
    this.getListCard();
  }

  getListCard() {
    this.service.getListCard(this.keyword).subscribe(value => {
      this.lstCard = value.data;
      if (this.init) {
        this.service.getListCard('').subscribe(value => {
          this.lstOriginalCard = value.data;
          this.init = false;
        });
      }
      this.totalItems = value.count;
      if (this.paramCheckAll) {
        this.lstCard.forEach(n => {
          n.check = !this.listSelected.some(m => m === n.id);
        });
      } else {
        this.listSelected = [];
        this.lstCard.forEach(n => {
          if (this.paramCheckAllPage) {
            n.check = true;
            this.listSelected.push(n.id);
          } else {
            n.check = false;
          }
        });
      }
    });
  }

    protected readonly ICON_SORTTING_CART = ICON_SORTTING_CART;
}
