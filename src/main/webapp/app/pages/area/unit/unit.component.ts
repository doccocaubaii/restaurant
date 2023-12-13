import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { createNewArea, IArea } from '../../../entities/area/area.model';
import { ToastrService } from 'ngx-toastr';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CreateUnitComponent } from './create-unit/create-unit.component';
import { IAreaUnit, InfoArea } from '../../../entities/area/area-unit.model';
import { UnitService } from './unit.service';
import { AreaService } from '../area.service';
import { UtilsService } from '../../../utils/Utils.service';
import { LoadingOption } from '../../../utils/loadingOption';
import { Router } from '@angular/router';
import { ConfirmDialogComponent } from '../../../shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from 'app/pages/order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader, UnitHeader } from '../../../constants/modal.const';
import { BILL, BILL_ORDER } from '../../../constants/app.routing.constants';
import { CreateAreaComponent } from '../create-area/create-area.component';
import { AreaOrderComponent } from '../area-order/area-order.component';
import { Authority } from '../../../config/authority.constants';
import {
  ICON_BILL_INFO,
  ICON_CANCEL,
  ICON_CREATE,
  ICON_DELETE,
  ICON_EDIT_SM, ICON_GET_UNIT,
  ICON_SAVE,
  ICON_TABLE, ICON_TIME_TILL_NOW, ICON_TOTAL_MONEY,
  ICON_UPDATE
} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-manage-area-unit',
  templateUrl: './unit.component.html',
  styleUrls: ['./unit.component.scss'],
})
export class UnitComponent implements OnInit {
  constructor(
    private areaService: AreaService,
    private unitService: UnitService,
    private utilsService: UtilsService,
    private router: Router,
    private modalService: NgbModal,
    private toastr: ToastrService,
    public loading: LoadingOption,
    public util: UtilsService
  ) {}
  // @ViewChild('updateAreaModel') updateAreaModel: ElementRef;
  @ViewChild('updateUnitModel') updateUnitModel: ElementRef;
  @ViewChild('detailUnit') detailUnitModel: ElementRef;
  @Input() updateAreaUnit: boolean;
  @Input() area: any;
  @Input() listOrder?: any;
  @Output() updateArea = new EventEmitter<boolean>();
  nameTmp: IAreaUnit;
  @Output() pickAreaUnit = new EventEmitter<any>();
  tmp: IArea = createNewArea(); // clone của khu vực chỉnh sưả
  areaArray: IArea[] = [];
  private checkModalRef: NgbModalRef | undefined;
  idDel = -1;
  unitDetail: IAreaUnit;
  authorAdd = Authority.AREA_UNIT_ADD;
  authorUpdate = Authority.AREA_UNIT_UPDATE;
  authorDelete = Authority.AREA_UNIT_DELETE;
  createUnit() {
    let listInfoArea: InfoArea[] = [];
    listInfoArea.push({ strValue: this.area.name, intValue: this.area.id });
    this.checkModalRef = this.modalService.open(CreateUnitComponent);
    this.checkModalRef.componentInstance.parent = listInfoArea;
    this.checkModalRef.componentInstance.area.id = this.area.id;
    this.checkModalRef.componentInstance.area.comId = this.area.comId;
    this.checkModalRef.result.then(
      result => {
        this.updateArea.emit(true);
      },
      reason => {
        // on dismiss
      }
    );
  }

  addInfo() {
    for (let i = 0; i < this.area.units.length; i++) {
      let totalMoney = 0;
      // if (this.area.units[i])

      let date1 = this.util.convertStringToDate(this.area.units[i].usingBills[0]?.updateTime);
      this.area.units[i].usingBills?.forEach(orderModifiers => {
        let date3 = this.util.convertStringToDate(orderModifiers.createTime);
        if (date3 < date1) date1 = date3;
      });
      let date2 = this.util.subDate(date1);
      // let date2;
      if (date2 >= 1000 * 60 * 60 * 24) {
        this.area.units[i].timeTillNow = Math.floor(date2 / (1000 * 60 * 60 * 24)) + ' ngày';
      } else if (date2 >= 1000 * 60 * 60) {
        this.area.units[i].timeTillNow = Math.floor(date2 / (1000 * 60 * 60)) + ' giờ';
      } else {
        if (date2 < 0) date2 = 0;
        this.area.units[i].timeTillNow = Math.floor(date2 / (1000 * 60)) + ' phút';
      }

      let usingBills = this.area.units[i]?.usingBills;
      if (usingBills) {
        for (let usingBill of usingBills) {
          totalMoney += +usingBill.totalAmount;
        }
      }
      this.area.units[i].totalMoney = totalMoney;
    }
    this.area.emptyTable = 0;
    for (let unit of this.area.units) if (unit == null || unit.usingBills?.length == 0) this.area.emptyTable = 1 + this.area.emptyTable;
  }

  ngOnInit(): void {
    this.addInfo();
  }

  // updateNameArea() {
  //   this.areaService.putArea(this.area.id, this.area.comId, this.tmp.name).subscribe(response => {
  //     if (response.status) {
  //       this.toastr.success(response.message[0].message);
  //       this.newItemEvent.emit(true);
  //       this.checkModalRef?.close();
  //       return;
  //     }
  //   });
  // }
  openUpdateArea(area: IArea) {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    for (let key in area) {
      if (area[key] !== null) {
        this.tmp[key] = area[key];
      }
    }
    this.checkModalRef = this.modalService.open(CreateAreaComponent);
    this.checkModalRef.componentInstance.area = this.tmp;
    this.checkModalRef.result.then(result => {
      this.updateArea.emit(true);
    });
  }
  openDeleteArea() {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.checkModalRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
    this.checkModalRef.componentInstance.value = new DialogModal(
      ModalHeader.DELETE_AREA,
      ModalContent.DELETE_AREA,
      ModalBtn.DELETE_AREA,
      'trash-can',
      'btn-delete'
    );
    this.checkModalRef.componentInstance.formSubmit.subscribe(res => {
      if (res) {
        this.areaService.deleteArea(this.area.id, this.area.comId).subscribe(response => {
          if (response.status) {
            this.updateArea.emit(true);
            this.toastr.success(response.message[0].message);
            this.checkModalRef?.close();
            return;
          }
        });
      }
    });
  }

  openDeleteUnit(event, unit: IAreaUnit) {
    event.stopPropagation();
    this.idDel = unit.id;
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.checkModalRef = this.modalService.open(ConfirmDialogComponent);
    this.checkModalRef.componentInstance.value = new DialogModal(
      UnitHeader.DELETE_UNIT,
      UnitHeader.DELETE_UNIT,
      UnitHeader.DELETE_UNIT,
      'trash-can',
      'btn-delete'
    );
    this.checkModalRef.componentInstance.formSubmit.subscribe(res => {
      if (res) {
        this.unitService.deleteUnit(unit.id, unit.areaId, unit.comId).subscribe(response => {
          if (response.status) {
            this.toastr.success(response.message[0].message);
            this.checkModalRef?.close();
            this.updateArea.emit(true);
            return;
          }
        });
      }
    });
  }

  openModelUnit(event, unit: IAreaUnit) {
    event.stopPropagation();
    this.nameTmp = unit;
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.checkModalRef = this.modalService.open(this.updateUnitModel);
    this.checkModalRef.result.then(result => {
      this.updateArea.emit(true);
    });
  }

  getAreaUnit(event, unit, area) {
    event.stopPropagation();
    this.pickAreaUnit.emit({ ...unit, areaId: area.id, areaName: area.name, type: 1 });
  }

  updateNameUnit(unit: IAreaUnit) {
    this.unitService.putUnit(unit.id, this.area.comId, this.area.id, unit.name).subscribe(response => {
      if (response.status) {
        this.toastr.success(response.message[0].message);
        this.checkModalRef?.close();
        return;
      }
    });
  }

  deleteArea() {
    this.areaService.deleteArea(this.area.id, this.area.comId).subscribe(response => {
      if (response.status) {
        this.toastr.success(response.message[0].message);
        this.checkModalRef?.close();
        return;
      }
    });
  }

  //  IAreaUnit là cái bàn
  // muốn nó xử lí hành động gì khi bấm vào cái bàn thì viết vào đây
  getLink(path: string, pathOffline?: string) {
    if (this.utilsService.isOnline()) {
      this.router.navigate([path]);
    } else if (pathOffline) {
      this.router.navigate([pathOffline]);
    }
  }
  openDetail(unit: IAreaUnit, area: IArea) {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.unitDetail = unit;
    if (this.updateAreaUnit) {
      this.pickAreaUnit.emit({ ...unit, areaId: area.id, areaName: area.name });
    } else {
      if ((unit.usingBills?.length || 0) > 1) {
        const posInvoiceRef = this.modalService.open(AreaOrderComponent, {
          size: 'lg',
          backdrop: 'static',
          windowClass: 'margin-5',
        });
        posInvoiceRef.componentInstance.unitDetail = this.unitDetail;
        posInvoiceRef.componentInstance.listOrder = this.listOrder;
        posInvoiceRef.closed.subscribe(result => {
          if (result) {
            this.pickAreaUnit.emit({ ...unit, areaId: area.id, areaName: area.name, idOrder: result, type: 1 });
          } else {
            this.pickAreaUnit.emit({ ...unit, areaId: area.id, areaName: area.name, type: 1 });
          }
        });
      } else {
        if (unit.usingBills?.length) {
          this.pickAreaUnit.emit({ ...unit, areaId: area.id, areaName: area.name, idOrder: unit.usingBills[0].id, type: 1 });
        } else {
          this.pickAreaUnit.emit({ ...unit, areaId: area.id, areaName: area.name, type: 1 });
        }
      }
    }
  }

  protected readonly BILL = BILL;
    protected readonly ICON_SAVE = ICON_SAVE;
  protected readonly ICON_CANCEL = ICON_CANCEL;
    protected readonly ICON_UPDATE = ICON_UPDATE;
  protected readonly ICON_DELETE = ICON_DELETE;
    protected readonly ICON_CREATE = ICON_CREATE;
  protected readonly ICON_TABLE = ICON_TABLE;
    protected readonly ICON_EDIT_SM = ICON_EDIT_SM;
    protected readonly ICON_GET_UNIT = ICON_GET_UNIT;
    protected readonly ICON_BILL_INFO = ICON_BILL_INFO;
    protected readonly ICON_TIME_TILL_NOW = ICON_TIME_TILL_NOW;
  protected readonly ICON_TOTAL_MONEY = ICON_TOTAL_MONEY;
}
