import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { FilterProcessingArea, FilterProductProcessingAreas, Page } from '../processing-area';
import { ProcessingAreaService } from '../processing-area.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ModalConfirmDeleteComponent } from '../../product/modal-confirm-delete/modal-confirm-delete.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from '../../../shared/base/base.component';
import { ModalCreateProcessingAreaComponent } from '../modal-create-processing-area/modal-create-processing-area.component';
import { Subscription } from 'rxjs';
import { ConfirmChangeComponent } from '../confirm-change/confirm-change.component';
import { Authority } from '../../../config/authority.constants';
import { CreateProductProcessingAreaComponent } from '../create-product-processing-area/create-product-processing-area.component';

@Component({
  selector: 'jhi-list-processing-area',
  templateUrl: './list-processing-area.component.html',
  styleUrls: ['./list-processing-area.component.scss'],
})
export class ListProcessingAreaComponent extends BaseComponent implements OnInit {
  filterProcessingArea: FilterProcessingArea = { page: 0, size: Page.PAGE_SIZE };
  processingAreas: any = [];
  totalItems = 0;
  totalItemDetails = 0;
  lastCompany: any;
  listSelected: any = [];
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
  private modalRef: NgbModalRef | undefined;
  idEditing: any;
  processingAreaSelect: any;
  isHidden = false;
  productProcessingAreas: any = [];
  private searchSubscription: Subscription;
  filterProductProcessingAreas: FilterProductProcessingAreas = {};
  authorADD = Authority.PROCESSING_AREA_ADD;
  authorVIEW = Authority.PROCESSING_AREA_VIEW;
  authorUPDATE = Authority.PROCESSING_AREA_UPDATE;
  authorDELETE = Authority.PROCESSING_AREA_DELETE;

  constructor(
    private service: ProcessingAreaService,
    private toastr: ToastrService,
    private route: Router,
    protected modalService: NgbModal,
    private translateService: TranslateService,
    protected activatedRoute: ActivatedRoute,
    private elRef: ElementRef
  ) {
    super();
    this.searchSubscription = this.service.searchObservable$.subscribe(() => {
      this.onSearch();
    });
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    this.filterProcessingArea.comId = this.lastCompany.id;
    this.activatedRoute.queryParamMap.subscribe(params => {
      this.getProcessingArea();
    });
  }

  onSearch() {
    this.getProcessingArea();
  }

  getProcessingArea() {
    this.hideDiv();
    const req = Object.assign({}, this.filterProcessingArea);
    if (this.filterProcessingArea.page > 0) {
      req.page = this.filterProcessingArea.page - 1;
    } else {
      req.page = this.filterProcessingArea.page;
    }
    this.service.getProcessingArea(req).subscribe(value => {
      this.processingAreas = JSON.parse(JSON.stringify(value.body.data));
      this.totalItems = value.body.count;
    });
  }

  onDeleteKeyWord() {
    if (!this.filterProcessingArea.name) {
      this.onSearch();
    }
  }

  onConfirmDeleteCategory(id: any) {
    this.modalRef = this.modalService.open(ConfirmChangeComponent, {
      size: 'dialog-centered',
      backdrop: 'static',
    });
    this.filterProductProcessingAreas.processingAreaId = id;
    this.modalRef.componentInstance.title = this.translateService.instant('processingArea.message.deleteProcessingAreaNotProduct');
    const filterProductProcessingAreas = Object.assign({}, this.filterProductProcessingAreas);
    this.service.getProductByProcessingAreaId(filterProductProcessingAreas).subscribe(value => {
      this.productProcessingAreas = value.body.data;
      if (this.productProcessingAreas.length > 0) {
        // @ts-ignore
        this.modalRef.componentInstance.title2 = this.translateService.instant('processingArea.message.deleteProcessingAreaHaveProduct');
      }
    });
    this.modalRef.closed.subscribe((res?: any) => {
      if (res === 1) {
        this.service.deleteProcessingArea(id).subscribe(value => {
          this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
          const index = this.listSelected.indexOf(id);
          if (index > -1) {
            this.listSelected.splice(index, 1);
          }
          this.getProcessingArea();
        });
      }
    });
  }

  onChangedPage(event: any): void {
    this.filterProcessingArea.page = event;
    this.handleNavigation(this.filterProcessingArea.page, this.filterProcessingArea.size, this.filterProcessingArea.active);
  }

  protected handleNavigation(page: number, size: number, status?: number): void {
    const queryParamsObj = {
      page,
      size,
      status,
    };
    this.route.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  async onCreateProcessingArea() {
    this.openModalCreateProcessingArea(0);
  }

  openModalCreateProcessingArea(id: any) {
    this.modalRef = this.modalService.open(ModalCreateProcessingAreaComponent, { size: 'xl', backdrop: 'static' });
    this.modalRef.componentInstance.id = id;
    this.modalRef.componentInstance.idProcessingArea = JSON.parse(JSON.stringify(id));
    this.modalRef.closed.subscribe((res?: any) => {});
  }

  async onDetailProcessingArea(id: any) {
    this.openModalCreateProcessingArea(id);
  }

  async viewProcessingArea(id: any) {
    this.isHidden = false;
    this.idEditing = this.idEditing == id ? -1 : id;
    let req = {
      page: 0,
      size: 10,
    };
    this.service.findProcessingArea(id, req).subscribe(res => {
      this.processingAreaSelect = res.body.data;
      this.totalItemDetails = res.body.count;
    });
  }

  openModalCreateProductProcessingArea(id: any) {
    // this.processingAreaDetail.listProduct = this.listSelected;
    this.modalRef = this.modalService.open(CreateProductProcessingAreaComponent, { size: 'xl', backdrop: 'static' });
    this.modalRef.componentInstance.id = id;
    this.modalRef.componentInstance.showMore = true;
    this.modalRef.componentInstance.paramCheckAllShowMore = this.processingAreaSelect.processingArea.paramCheckAll;
    this.modalRef.componentInstance.idsShowMore = this.processingAreaSelect.processingArea.ids;
    this.modalRef.closed.subscribe((res?: any) => {
      if (res.message) {
      }
    });
  }

  getColspan() {
    return 10;
  }

  hideDiv() {
    this.isHidden = true;
    this.idEditing = -1;
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: MouseEvent) {
    if (!this.elRef.nativeElement.contains(event.target)) {
      this.hideDiv();
    }
  }
}
