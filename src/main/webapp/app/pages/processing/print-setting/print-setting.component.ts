import { Component, ElementRef, OnInit } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from 'app/shared/base/base.component';
import { UtilsService } from 'app/utils/Utils.service';
import { last_company } from '../../../object-stores.constants';
import appSettings from '../../../config/app-settings';
import { ContentOption } from '../../../utils/contentOption';
import { ProcessingService } from './../processing.service';
import { Subject } from 'rxjs';
import { Page } from '../../product/product';
import { PrintSettingService } from './print-setting.service';

@Component({
  selector: 'processing',
  templateUrl: './print-setting.component.html',
  styleUrls: ['./print-setting.component.scss'],
})
export class PrintSettingComponent extends BaseComponent implements OnInit {
  selectedSetting: any = {};
  lstSetting: any[] = [];
  listSelected: any = [];
  selectedItem: any = {};
  categories: any = [];
  allCategories: any = [];
  lastCompany: any = {};
  totalItems: any = 0;
  filterSetting = { page: Page.PAGE_NUMBER, size: Page.PAGE_SIZE };
  lstPrinter: any[] = [];
  lstPrintTemplate: any[] = [];

  private modalRef: NgbModalRef | undefined;
  paramCheckAll: boolean = false;
  paramCheckAllPage: any;

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

  constructor(
    protected modalService: NgbModal,
    protected utilsService: UtilsService,
    protected service: PrintSettingService,
    private elementRef: ElementRef,
    private route: Router,
    private toast: ToastrService,
    private contentOption: ContentOption,
    protected activatedRoute: ActivatedRoute
  ) {
    super();
    this.contentOption.isHiddenOrder = true;
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    await this.getAllPrinter();
    await this.getAllPrintTemplate();
    await this.getLstSetting();
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

  async getAllPrinter() {
    this.service.getListPrinter().subscribe(res => {
      if (res.status) {
        this.lstPrinter = res.body.data;
      }
    });
  }

  async getAllPrintTemplate() {
    this.service.getListPrintTemplate().subscribe(res => {
      if (res.status) {
        this.lstPrintTemplate = res.body.data;
      }
    });
  }

  async getLstSetting() {
    let page = this.filterSetting.page;
    let req = {
      page: page - 1,
      size: this.filterSetting.size,
    };
    this.service.getListSetting(req).subscribe(res => {
      if (res.status) {
        this.lstSetting = res.body.data;
      }
    });
  }
  onChangedPage(event: any): void {
    this.filterSetting.page = event;
    this.getLstSetting();
  }
}
