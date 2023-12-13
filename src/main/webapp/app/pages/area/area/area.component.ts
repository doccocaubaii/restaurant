import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { BaseComponent } from '../../../shared/base/base.component';
import { LoadingOption } from '../../../utils/loadingOption';
import { AreaService } from '../area.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { UnitService } from '../unit/unit.service';
import { createNewArea, IArea } from '../../../entities/area/area.model';
import { CreateAreaComponent } from '../create-area/create-area.component';
import { UtilsService } from '../../../utils/Utils.service';
import { Router } from '@angular/router';
import { DialogModal } from 'app/pages/order/model/dialogModal.model';
import { ConfirmDialogComponent } from '../../../shared/modal/confirm-dialog/confirm-dialog.component';
import { ModalBtn, ModalContent, ModalHeader } from '../../../constants/modal.const';
import { AREA, AREA_UNIT } from '../../../constants/app.routing.constants';
import { Authority } from '../../../config/authority.constants';
@Component({
  selector: 'jhi-area',
  templateUrl: './area.component.html',
  styleUrls: ['./area.component.scss'],
})
export class AreaComponent extends BaseComponent implements OnInit {
  protected readonly alert = alert;
  protected readonly AREA_UNIT = AREA_UNIT;
  companyId: number;
  company: any;
  pageSize: number = 20;
  totalSize: number = 100;
  areaArray: IArea[];
  keyword: string = '';
  currentPage = 1;
  tmp: IArea = createNewArea(); // clone của khu vực chỉnh sưả
  idDel: number = -1;
  private checkModalRef: NgbModalRef | undefined;
  @ViewChild('updateAreaModel') updateAreaModel: ElementRef;
  @ViewChild('delArea') delArea: ElementRef;
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
  authorADD = Authority.AREA_ADD;
  authorVIEW = Authority.AREA_VIEW;
  authorUPDATE = Authority.AREA_UPDATE;
  authorDELETE = Authority.AREA_DELETE;
  constructor(
    private areaService: AreaService,
    private modalService: NgbModal,
    private toastr: ToastrService,
    private utilsService: UtilsService,
    private router: Router,
    public loading: LoadingOption
  ) {
    super();
  }

  async ngOnInit() {
    this.company = await this.getCompany();
    this.companyId = this.company.id;
    this.getAreas();
  }

  getAreas(): void {
    this.areaService.getAreas(100, 1000, null, this.keyword, null).subscribe(
      response => {
        this.areaArray = response.data;
        this.totalSize = response.count;
        for (let x of this.areaArray) {
          x.emptyTable = 0;
          if (x.units) {
            x.emptyTable = x.units.length;
            for (let u of x.units) {
              if (u.usingBills && u.usingBills.length > 0) {
                x.emptyTable = x.emptyTable - 1;
              }
            }
          }
        }
      },
      error => {}
    );
  }
  openCreateArea() {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.checkModalRef = this.modalService.open(CreateAreaComponent);
    this.checkModalRef.result.then(result => {
      this.getAreas();
    });
  }
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
      this.getAreas();
    });
  }

  openDeleteArea(idDel: number) {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.idDel = idDel;
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
        this.deleteArea();
        if (this.checkModalRef) this.checkModalRef.close();
      }
    });
  }
  deleteArea() {
    this.areaService.deleteArea(this.idDel, this.companyId).subscribe(response => {
      if (response.status) {
        this.toastr.success(response.message[0].message);
        this.getAreas();
        this.checkModalRef?.close();
        return;
      }
    });
  }

  getLink(path: string, pathOffline?: string) {
    if (this.utilsService.isOnline()) {
      this.router.navigate([path]);
    } else if (pathOffline) {
      this.router.navigate([pathOffline]);
    }
  }
  onDeleteKeywordSearch() {
    if (!this.keyword) this.getAreas();
  }
}
