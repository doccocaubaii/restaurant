import { Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { BaseComponent } from './../../shared/base/base.component';
import { DArea, IArea } from '../../entities/area/area.model';
import { AreaService } from './area.service';
import { ToastrService } from 'ngx-toastr';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CreateAreaComponent } from './create-area/create-area.component';
import { PerfectScrollbarComponent } from 'ngx-perfect-scrollbar';
import { IAreaUnit, InfoArea, initUnit } from '../../entities/area/area-unit.model';
import { UnitService } from './unit/unit.service';
import { LoadingOption } from '../../utils/loadingOption';
import { ConfirmDialogComponent } from '../../shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from 'app/pages/order/model/dialogModal.model';
import { UnitHeader } from '../../constants/modal.const';
import { CreateUnitComponent } from './unit/create-unit/create-unit.component';
import { ActivatedRoute, Router } from '@angular/router';
import { UtilsService } from '../../utils/Utils.service';
import { AREA, AREA_UNIT, BILL, BILL_ORDER } from '../../constants/app.routing.constants';
import { Authority } from '../../config/authority.constants';
import { ICON_CANCEL, ICON_DATE, ICON_SAVE, ICON_TABLE_SM } from '../../shared/other/icon';

@Component({
  selector: 'jhi-manage-table',
  templateUrl: './manage-area.component.html',
  styleUrls: ['./manage-area.component.scss'],
})
export class ManageAreaComponent extends BaseComponent implements OnInit {
  @ViewChild('updateUnitModel') updateUnitModel: ElementRef;
  @ViewChild('detailUnit') detailUnitModel: ElementRef;
  nameTmp: IAreaUnit = initUnit();
  company: any;
  totalArea: number = 0;
  emptyArea: number = 0;
  companyId: number;
  keyword: string = '';
  selectArea: number = -1;
  page: number = 1;
  pageSize: number = 20;
  totalSize: number = 0;
  areaArray: IArea[];
  unitArray: IAreaUnit[];
  tmpArray: IArea[];
  unitDetail: IAreaUnit;
  idEditing: number = -1;
  currentPage = 1;
  private checkModalRef: NgbModalRef | undefined;
  public index = 0;
  private stt: number;
  sizes = [
    {
      id: 10,
      name: '10',
    },
    {
      id: 20,
      name: '20',
    },
    {
      id: 30,
      name: '30',
    },
  ];
  authorAdd = Authority.AREA_UNIT_ADD;
  authorView = Authority.AREA_UNIT_VIEW;
  authorUpdate = Authority.AREA_UNIT_UPDATE;
  authorDelete = Authority.AREA_UNIT_DELETE;
  authorADDBill = Authority.BILL_ADD;

  constructor(
    private areaService: AreaService,
    private modalService: NgbModal,
    private toastr: ToastrService,
    private unitService: UnitService,
    private utilsService: UtilsService,
    private router: Router,
    public loading: LoadingOption,
    private route: ActivatedRoute,
    protected activatedRoute: ActivatedRoute
  ) {
    super();
  }

  getAreas(holdTypeArea?: boolean): void {
    let idArea: any = null;
    if (this.selectArea != -1) idArea = this.selectArea;
    else if (this.areaArray?.length == 1) {
      idArea = this.areaArray[0].id;
    }
    this.areaService.getAreas(100, 1000, null, null, this.keyword, idArea).subscribe(
      response => {
        if (response.data) this.areaArray = response.data;
        this.index = 0;
        if (holdTypeArea) {
          this.changeArea();
        } else {
          this.getInfo(this.areaArray);
          this.tmpArray = this.areaArray;
        }
      },
      error => {}
    );
  }

  async ngOnInit() {
    this.company = await this.getCompany();
    this.companyId = this.company.id;
    this.route.params.subscribe(params => {
      const areaId = params['id']; // Lấy giá trị của tham số 'id' từ URL
      if (areaId) {
        this.selectArea = parseInt(areaId); // Gán giá trị của tham số vào biến selectArea
      }
    });
    this.getAreas(true);
  }

  getInfo(array: IArea[]) {
    this.stt = 0;
    this.totalArea = 0;
    this.emptyArea = 0;
    this.unitArray = [];
    for (let index in array) {
      array[index].emptyTable = 0;
      let unitTotal = array[index].units.length;
      this.totalArea += unitTotal;
      for (let unit of array[index].units) {
        this.stt = this.stt + 1;
        this.unitArray.push(unit);
        unit.areaName = array[index].name;
        unit.stt = this.stt;
        if (unit == null || unit.usingBills?.length == 0) array[index].emptyTable = 1 + array[index].emptyTable;
      }
      this.emptyArea += array[index].emptyTable;
    }
    this.totalSize = this.totalArea;
  }

  changeArea() {
    if (this.selectArea == -1) {
      this.tmpArray = this.areaArray;
      this.getInfo(this.tmpArray);
      return;
    }
    for (let a of this.areaArray) {
      if (a.id == this.selectArea) {
        this.tmpArray = [];
        this.tmpArray.push(a);
        this.getInfo(this.tmpArray);
        return;
      }
    }
    this.selectArea = -1;
  }

  openCreate() {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.checkModalRef = this.modalService.open(CreateAreaComponent);
    this.checkModalRef.result.then(
      result => {
        this.getAreas(true);
      },
      reason => {}
    );
  }

  createUnit() {
    let listInfoArea: InfoArea[] = [];
    for (let i of this.areaArray) {
      listInfoArea.push({ strValue: i.name, intValue: i.id });
    }

    this.checkModalRef = this.modalService.open(CreateUnitComponent);
    this.checkModalRef.componentInstance.parent = listInfoArea;
    this.checkModalRef.componentInstance.area.comId = this.companyId;
    if (this.selectArea > 0) {
      this.checkModalRef.componentInstance.area.id = this.selectArea;
    }
    this.checkModalRef.result.then(
      result => {
        this.getAreas(true);
      },
      reason => {
        // on dismiss
      }
    );
  }

  openUpdateUnit(unit: IAreaUnit) {
    for (let key in unit) {
      if (unit[key] !== null) {
        this.nameTmp[key] = unit[key];
      }
    }
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.checkModalRef = this.modalService.open(this.updateUnitModel);
  }
  updateNameUnit(unit: IAreaUnit) {
    this.unitService.putUnit(unit.id, unit.comId, unit.areaId, unit.name).subscribe(response => {
      if (response.status) {
        this.toastr.success(response.message[0].message);
        this.getAreas(true);
        this.checkModalRef?.close();
        return;
      }
    });
  }

  openModalDeleteUnit(unit: IAreaUnit) {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    // Gọi model
    this.checkModalRef = this.modalService.open(ConfirmDialogComponent);
    // Tạo đối tượng gửi sang model với các value
    // title: tiêu đề model
    // message: Nội dung model
    // btnText: Nội dung nút đồng ý
    // icon: Icon nút đồng ý
    // classBtn: Class nút đồng ý
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
            this.getAreas(true);
            return;
          }
        });
      }
    });
  }

  openDetail(unit: IAreaUnit) {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.unitDetail = unit;
    this.checkModalRef = this.modalService.open(this.detailUnitModel);
  }

  protected readonly alert = alert;

  changeAreaApi() {
    let t = this.selectArea == -1;
    if (this.selectArea == -1) this.getLink(AREA_UNIT);
    else this.getLink(AREA + '/' + this.selectArea);
    this.getAreas(true);
  }
  getLinkTable(path: string, idTable?: number, pathOffline?: string) {
    if (this.utilsService.isOnline()) {
      const queryParamsObj = {
        table: idTable,
        display: 1,
      };
      this.router.navigate([path], {
        // relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    } else if (pathOffline) {
      this.router.navigate([pathOffline]);
    }
  }
  getLink(path: string, pathOffline?: string) {
    if (this.utilsService.isOnline()) {
      this.router.navigate([path]);
    } else if (pathOffline) {
      this.router.navigate([pathOffline]);
    }
  }
  getLinkBill(path: string, id_order?: number, pathOffline?: string) {
    if (this.utilsService.isOnline()) {
      const queryParamsObj = {
        id_order: id_order,
        display: 1,
      };
      this.router.navigate([path], {
        // relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    } else if (pathOffline) {
      this.router.navigate([pathOffline]);
    }
  }
  onDeleteKeywordSearch() {
    if (!this.keyword) this.getAreas();
  }
  protected readonly BILL_ORDER = BILL_ORDER;
  protected readonly ICON_SAVE = ICON_SAVE;
  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_TABLE_SM = ICON_TABLE_SM;
  protected readonly ICON_DATE = ICON_DATE;
  billPath = BILL;
}
