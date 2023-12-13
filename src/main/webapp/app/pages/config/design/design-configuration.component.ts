import { Component, ElementRef, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import appSettings from '../../../config/app-settings';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalDialogComponent } from '../../../shared/modal/modal-dialog.component';
import { BaseComponent } from '../../../shared/base/base.component';
import { ToastrService } from 'ngx-toastr';
import { UtilsService } from '../../../utils/Utils.service';
import { TranslateService } from '@ngx-translate/core';
import { selectRows } from '@swimlane/ngx-datatable';
import { ConfigService } from '../../../layouts/config/service/config.service';
import { StaticMapService } from '../../../shared/other/StaticMapService';
import { EventManager, EventWithContent } from '../../../core/util/event-manager.service';
import { lastValueFrom } from 'rxjs';
import { InvoiceConfiguration, LastCompany } from '../../order/model/bill-payment.model';
import { InvoiceService } from '../../invoice/service/invoice.service';
import { Authority } from '../../../config/authority.constants';
import { ICON_LIST_ITEM, ICON_PENCIL, ICON_RIGHT, ICON_SAVE, ICON_SORT } from '../../../shared/other/icon';

@Component({
  selector: 'jhi-design-configuration',
  templateUrl: './design-configuration.component.html',
  styleUrls: ['./design-configuration.component.scss'],
})
export class DesignConfigurationComponent extends BaseComponent implements OnInit, OnDestroy {
  appSettings = appSettings;
  posMobileSidebarToggled = false;
  isSort = true;
  lstNavbarDesign: any;
  displaySetting: any;

  selectedItem: any;
  displayName: any;
  dataChild: any;
  lastCompany: LastCompany;
  invoiceConfiguration: InvoiceConfiguration;
  authorUPDATE_HIDE_SHOW = Authority.DESIGN_CONFIGURATION_UPDATE_HIDE_SHOW;

  togglePosMobileSidebar() {
    this.posMobileSidebarToggled = !this.posMobileSidebarToggled;
  }

  constructor(
    private modalService: NgbModal,
    private utilsService: UtilsService,
    private toast: ToastrService,
    private elementRef: ElementRef,
    private renderer: Renderer2,
    private configService: ConfigService,
    public eventManager: EventManager,
    private translateService: TranslateService,
    private invoiceService: InvoiceService,
    private staticMapService: StaticMapService
  ) {
    super();
  }

  ngOnDestroy() {
    this.selectedItem = {};
    this.appSettings.appEmpty = false;
    this.appSettings.appContentFullHeight = false;
  }

  getData(): any {
    if (this.lstNavbarDesign) return this.lstNavbarDesign.filter(item => item.parentCode === null);
  }

  getDataChild(parentCode: string) {
    if (this.lstNavbarDesign) {
      let data = this.lstNavbarDesign.filter(item => item.parentCode === parentCode);
      let showData: any[] = [];
      if (parentCode == 'BILL_ORDER') {
        for (const item of data) {
          if (item.code == 'TTT/BH' && this.invoiceConfiguration.invoiceType == 0) {
            continue;
          }
          if (item.code == 'GGDH/BH' && this.invoiceConfiguration.typeDiscount == 3) {
            continue;
          }
          if (item.code == 'TDH/BH' && (this.invoiceConfiguration.invoiceType == 0 || this.invoiceConfiguration.invoiceType == 2)) {
            continue;
          }
          if (item.code == 'GTT/BH' && this.invoiceConfiguration.discountVat == 0) {
            continue;
          }
          showData.push(item);
        }
        return showData;
      }
      return data;
    }
    return null;
  }

  openPopup() {
    this.modalService.open(ModalDialogComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
  }

  async ngOnInit() {
    this.getConfig();
    this.lastCompany = await this.getCompany();
    const invoiceConfiguration = await lastValueFrom(this.invoiceService.getCompanyConfig(this.lastCompany.id));
    this.invoiceConfiguration = invoiceConfiguration.data;
  }

  onDrop(event: any) {
    let parent = event.item.data.parentCode;
    const listTmp = this.getDataChild(parent);
    let previous = listTmp[event.previousIndex];
    let current = listTmp[event.currentIndex];
    let previousIndex = 0;
    let currentIndex = 0;
    for (let i = 0; i < this.lstNavbarDesign.length; i++) {
      if (this.lstNavbarDesign[i].code == previous.code) previousIndex = i;
      if (this.lstNavbarDesign[i].code == current.code) currentIndex = i;
    }

    if (previousIndex !== currentIndex) {
      const item = this.lstNavbarDesign.splice(previousIndex, 1)[0];
      this.lstNavbarDesign.splice(currentIndex, 0, item);
    }

    for (let i = 0; i < this.lstNavbarDesign.length; i++) {
      let item = this.lstNavbarDesign[i];
      item.position = i + 1;
    }
  }

  protected readonly selectRows = selectRows;

  focusOnInput(id: string, card: any) {
    setTimeout(() => {
      const inputElement = document.getElementById(id);
      if (inputElement) {
        if (!card) {
          this.displayName = this.selectedItem.displayName;
        }
        inputElement.focus();
      }
    }, 100); //
  }

  changeChiddName(card: any) {
    if (!card.displayName || card.displayName.trim().length === 0) {
      this.toast.error('Tên danh mục không hợp lệ', 'Thông báo');
      card.displayName = this.dataChild.find(item => item.code === card.code).displayName;
    }
  }

  selectRowsItem(card: any) {
    this.selectedItem = card;
    this.selectedItem.oldDisplayName = this.selectedItem.displayName;
    this.displayName = card.displayName;
    this.dataChild = JSON.parse(JSON.stringify(this.getDataChild(this.selectedItem.code)));
  }

  onChangeDisplayName() {
    this.selectedItem.isEdit = false;
    if (!this.displayName || this.displayName.trim().length === 0) {
      this.displayName = this.selectedItem.displayName;
      this.toast.error('Tên danh mục không hợp lệ', 'Thông báo');
    } else {
      this.selectedItem.displayName = this.displayName;
    }
  }
  saveConfig() {
    const values: any[] = [];
    for (let item of this.lstNavbarDesign) {
      let itemValue = {
        code: item.code,
        displayName: item.displayName,
        position: item.position,
        active: item.active,
        isHiddenAble: item.isHiddenAble,
        isRenameAble: item.isRenameAble,
        parentCode: item.parentCode,
      };
      if (this.selectedItem.code == item.code) {
        itemValue = {
          code: this.selectedItem.code,
          displayName: this.selectedItem.displayName,
          position: this.selectedItem.position,
          active: this.selectedItem.active,
          isHiddenAble: item.isHiddenAble,
          isRenameAble: item.isRenameAble,
          parentCode: this.selectedItem.parentCode,
        };
      }
      values.push(itemValue);
    }
    this.displaySetting.value = JSON.stringify(values);
    this.configService.updateDisplaySetting(this.displaySetting).subscribe(res => {
      if (res.status) {
        this.toast.success('Cập nhật cấu hình hiển thị thành công', 'Thông báo');
        this.staticMapService.reload();
        this.getConfig();
      }
    });
    this.eventManager.broadcast(new EventWithContent('easyPosFrontEndApp.saveConfig', { message: JSON.stringify(values) }));
  }

  getConfig() {
    this.configService.getService().subscribe(res => {
      if (res.status) {
        this.displaySetting = res.data;
        this.lstNavbarDesign = JSON.parse(this.displaySetting.value);
        if (this.selectedItem) {
          let x = this.selectedItem.code;
          this.selectedItem = this.lstNavbarDesign.filter(item => item.code === x)[0];
          this.selectedItem.oldDisplayName = this.selectedItem.displayName;
        }
        this.getData();
      }
    });
  }

  changeActiveParent(selectItem: any) {
    this.lstNavbarDesign.filter(item => {
      if (selectItem.code === item.parentCode) {
        if (item.isHiddenAble) {
          item.active = selectItem.active;
        }
      }
    });
  }

  changeActiveChildrent(card: any) {
    let listItem: any = this.lstNavbarDesign.filter(item => item.parentCode === card.parentCode);
    if (listItem.every((element: any) => element.active === false)) {
      this.selectedItem.active = false;
    } else {
      this.selectedItem.active = true;
    }
  }

  protected readonly ICON_LIST_ITEM = ICON_LIST_ITEM;
  protected readonly ICON_RIGHT = ICON_RIGHT;
  protected readonly ICON_PENCIL = ICON_PENCIL;
  protected readonly ICON_SORT = ICON_SORT;
  protected readonly ICON_SAVE = ICON_SAVE;
}
