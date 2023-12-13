import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../../../shared/base/base.component';
import { CustomerService } from '../../../customer/customer.service';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { LoadingOption } from '../../../../utils/loadingOption';
import { ImportService } from '../../import.service';
import { ConvertResponse } from '../../../../config/convert-response';
import { ImportProductResponse } from './import-product-response';
import { ImportProductResponseData } from './import-product-response-data';
import { EXPORT_EXCEL_PRODUCT, IMPORT_EXCEL_SAVE_PRODUCT, IMPORT_EXCEL_VALIDATE_PRODUCT } from '../../../../constants/api.constants';
import { ExportCommon } from '../../../export/export.common';
import { ImportCommon } from '../common/import.common';
import {ICON_ARROW_RIGHT, ICON_CANCEL, ICON_GO_BACK} from "../../../../shared/other/icon";

@Component({
  selector: 'import-excel',
  templateUrl: './import-product.component.html',
  styleUrls: ['../common/import.component.scss'],
})
export class ImportProductComponent extends BaseComponent implements OnInit {
  private modalRef: NgbModalRef | undefined;
  isLoading = false;
  importStatus: any = [];
  importStatusValue = 0;
  importTypeValue: null;
  urlFileTemplate = 'https://app.easyposs.vn/client/file/excel/product/TaoSanPham.xlsx';

  constructor(
    private customerService: CustomerService,
    public activeModal: NgbActiveModal,
    private toast: ToastrService,
    public loading: LoadingOption,
    protected modalService: NgbModal,
    protected importService: ImportService,
    protected importCommon: ImportCommon,
    public loadingOption: LoadingOption,
    public exportComponent: ExportCommon
  ) {
    super();
  }

  companyId: number;
  company: any;

  async ngOnInit() {
    this.company = await this.getCompany();
    this.companyId = this.company.id;
    this.detailMessage = null;
    this.importStatus = this.importCommon.statusFilter();
  }

  dismiss(value: any) {
    this.chooseFile();
    this.activeModal.close(value);
  }

  fileData: any;
  fileName: string;
  fileDataHeader: string[];

  fileType = ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'application/vnd.ms-excel'];
  dataValidateResponse?: ImportProductResponseData[];
  dataValidResponse?: ImportProductResponseData[];
  dataInvalidResponse?: ImportProductResponseData[];
  validateResponse?: ImportProductResponse;
  countTotal: number | undefined = 0;
  selectedFile = false;
  sheetIndex = -1;
  messagesRequire: string[];

  typeChangeEvent(type: any) {
    this.importTypeValue = type;
  }

  openFileEvent(fileDataResult: any) {
    this.fileData = fileDataResult.data;
    this.fileName = fileDataResult.name;
    this.fileDataHeader = fileDataResult.header;
    this.validateResponse = {};
  }

  sheetChangeEvent(index: number) {
    this.sheetIndex = index;
  }

  async validateData(): Promise<any> {
    const getMessage = this.importCommon.checkInput(this.fileData, this.sheetIndex, this.importTypeValue);

    if (getMessage !== '') {
      this.toast.error(getMessage);
    } else {
      this.selectedFile = true;
      this.isLoading = true;

      await this.importService
        .validateImport(this.fileData, this.sheetIndex, this.companyId, IMPORT_EXCEL_VALIDATE_PRODUCT)
        .subscribe(response => {
          if (response.ok) {
            let responseBody = ConvertResponse.getDataFromServer(response, false);
            this.validateResponse = responseBody.data;
            this.dataValidateResponse = this.validateResponse?.data;
            this.countTotal = responseBody.count;
            if (this.countTotal === 2000) {
              this.toast.error('Danh sách sản phẩm vượt quá số lượng cho phép (2000 sản phẩm)');
            }
            // foreach not work in async
            this.dataValidateResponse?.forEach(item => {
              if (item.messageErrorMap) {
                item.messageResponse = Object.keys(item.messageErrorMap).map(function (key) {
                  if (item.messageErrorMap) {
                    return item.messageErrorMap[key];
                  }
                });
              }
            });
            this.dataValidResponse = this.dataValidateResponse?.filter(data => data.status === true);
            this.dataInvalidResponse = this.dataValidateResponse?.filter(data => data.status === false);
          } else {
            this.toast.error(ConvertResponse.getDataFromServer(response, true));
          }
          this.isLoading = false;
        });
    }
  }

  viewMessage = false;
  detailMessage: string[] | null | undefined;

  viewStatusDetail(item: ImportProductResponseData) {
    if (item) {
      this.viewMessage = true;
      this.detailMessage = item.messageResponse;
      if (!this.detailMessage) {
        this.detailMessage = ['Sản phẩm hợp lệ'];
      }
    } else {
      this.detailMessage = ['Sản phẩm không tồn tại'];
    }
  }

  hideMessage() {
    this.viewMessage = false;
    this.detailMessage = [];
  }

  isCompleted = false;
  countSuccess: any;
  messageError = '';

  async completed() {
    this.isLoading = true;
    this.messageError = '';
    this.isCompleted = true;
    let lastCompany = await this.getCompany();
    if (this.dataValidResponse && this.dataValidResponse.length > 0) {
      const request = {
        comId: lastCompany.id,
        data: this.dataValidResponse,
      };

      await this.importService.saveDataImport(request, IMPORT_EXCEL_SAVE_PRODUCT).subscribe(
        response => {
          this.isLoading = false;
          if (response.ok) {
            let responseBody = ConvertResponse.getDataFromServer(response, false);
            this.countSuccess = responseBody.data;
            this.messageError = '';
          } else {
            this.messageError = ConvertResponse.getDataFromServer(response, true);
          }
        },
        error => {
          this.isLoading = false;
          this.messageError = ConvertResponse.getDataFromServer(error, true);
        }
      );
    } else {
      this.isLoading = false;
    }
  }

  chooseFile() {
    this.selectedFile = false;
    this.fileData = null;
    this.fileName = '';
    this.dataValidateResponse = [];
    this.dataValidResponse = [];
    this.dataInvalidResponse = [];
    this.hideMessage();
    this.countTotal = 0;
    this.isCompleted = false;
    this.sheetIndex = -1;
    // this.importTypeValue = null;
    this.fileDataHeader = [];
  }

  checkFile() {
    this.isCompleted = false;
    this.hideMessage();
  }

  changeType(value: any) {
    this.importTypeValue = value;
  }

  changeStatus(importStatusValue: any) {
    this.filterType(importStatusValue);
  }

  filterType(type: number) {
    if (type === 1) {
      this.dataValidateResponse = this.dataValidResponse;
    } else if (type === 2) {
      this.dataValidateResponse = this.dataInvalidResponse;
    } else if (type === 0 || !type) {
      this.dataValidateResponse = this.validateResponse?.data;
    }
    this.countTotal = this.dataValidateResponse?.length;
    // this.hideMessage();
  }

  downloadErrorData() {
    if (this.dataInvalidResponse) {
      this.exportComponent.exportFileExcel(this.dataInvalidResponse, 'DanhSachSanPhamLoi', this.companyId, EXPORT_EXCEL_PRODUCT);
    }
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_GO_BACK = ICON_GO_BACK;
    protected readonly ICON_ARROW_RIGHT = ICON_ARROW_RIGHT;
}
