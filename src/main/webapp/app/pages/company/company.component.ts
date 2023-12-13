import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Company } from '../../entities/company/company';
import { BaseComponent } from '../../shared/base/base.component';
import { CompanyService } from './company.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CompanyModalCreateComponent } from './company-modal-create/company-modal-create.component';
import { ToastrService } from 'ngx-toastr';
import { LoadingOption } from '../../utils/loadingOption';
import { CompanyModalUpdateComponent } from './company-modal-update/company-modal-update.component';
import { CompanyOwner } from '../../entities/company/company-owner';
import { last_company, last_config_device, last_user } from '../../object-stores.constants';
import { CompanySessionItem } from '../../entities/companySession/CompanySessionItem.model';
import { DataEncrypt } from '../../entities/indexDatabase/data-encrypt.model';
import { ICON_EDIT_BLUE } from '../../shared/other/icon';
import { Authority } from '../../config/authority.constants';

const FILTER_PAG_REGEX = /\D/g;

@Component({
  selector: 'jhi-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.scss'],
})
export class CompanyComponent extends BaseComponent implements OnInit {
  sort: string;
  typeCompany: number = 0;
  keyword: string = '';
  branchArray: Company[] = [];
  companyArray: CompanyOwner[] = [];
  companies: CompanySessionItem[] = [];
  page: number = 1;
  pageSize: number = 20;
  tmp: any;
  idEditing: number = -1;
  totalSize: number = 0;
  totalItems: number = 0;
  idDel: number = -1;
  comOwnerId: number;
  lastCompany: any;
  posSidebarFooterStatus: any = 0;
  lastUser: any = {};
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
  @ViewChild('delCus') delCus: ElementRef;
  authorADD = Authority.COMPANY_ADD;
  authorUpdate = Authority.COMPANY_UPDATE;

  constructor(
    private companyService: CompanyService,
    private modalService: NgbModal,
    private toastr: ToastrService,
    private elementRef: ElementRef,
    private loading: LoadingOption
  ) {
    super();
  }
  async ngOnInit(): Promise<void> {
    this.lastUser = await this.getFirstItemIndexDB(last_user);
    this.lastCompany = await this.getCompany();
    this.getCompanies();
  }

  getCompanies(isCreate?: boolean, companyIdCreated?: number): void {
    let companyCreated;
    this.companyArray = [];
    this.branchArray = [];
    this.companyService.getCompany(this.page, this.pageSize, this.sort, this.keyword).subscribe(res => {
      this.totalSize = res.count;
      this.companyArray = res.data;
      for (const item of this.companyArray.map(com => com.companies.filter(obj => !obj.parent))) {
        if (isCreate) {
          companyCreated = item.find(x => x.id === companyIdCreated);
          if (companyCreated !== null && companyCreated !== undefined) break;
        }
      }
      if (isCreate) {
        const companies: Company[] = JSON.parse(JSON.stringify(this.lastUser.companies));
        companies.push(companyCreated);
        this.updateById(
          last_user,
          this.lastUser.id,
          new DataEncrypt(
            this.lastUser.id,
            this.encryptFromData({
              ...this.lastUser,
              companies,
            })
          )
        );
      }
    });
  }

  toggleCompany(com: CompanyOwner) {
    this.comOwnerId = com.id;
    if (this.posSidebarFooterStatus === com.id) {
      this.posSidebarFooterStatus = 0;
    } else {
      this.posSidebarFooterStatus = com.id;
    }
  }

  openCreate(com?: Company) {
    if (this.modalRef) {
      this.modalRef.close();
    }

    this.modalRef = this.modalService.open(CompanyModalCreateComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.componentInstance.comOwnerId = this.comOwnerId;
    this.modalRef.componentInstance.companyArray = this.companyArray;
    this.modalRef.componentInstance.lastCompany = this.lastCompany;
    if (com) {
      this.idEditing = -1;
      for (let key in com) {
        if (com[key] !== null) {
          this.modalRef.componentInstance.com[key] = com[key];
        }
      }
    }
    this.modalRef.result.then(
      result => {
        this.getCompanies(true, result);
      },
      reason => {}
    );
  }

  openUpdate(com?: Company) {
    if (this.modalRef) {
      this.modalRef.close();
    }
    this.modalRef = this.modalService.open(CompanyModalUpdateComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.componentInstance.comOwnerId = this.comOwnerId;
    this.modalRef.componentInstance.companySelected = JSON.parse(JSON.stringify(com));
    this.modalRef.componentInstance.lastCompany = this.lastCompany;
    this.modalRef.result.then(
      result => {
        this.getCompanies(false);
      },
      reason => {}
    );
  }

  search() {
    this.getCompanies();
  }

  getCount(company: any): number {
    const filteredCompanies = company.companies.filter(item => !item.parent);
    return filteredCompanies.length;
  }

  protected readonly ICON_EDIT_BLUE = ICON_EDIT_BLUE;
}
