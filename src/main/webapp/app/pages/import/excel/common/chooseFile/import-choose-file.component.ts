import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BaseComponent } from '../../../../../shared/base/base.component';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { LoadingOption } from '../../../../../utils/loadingOption';
import * as XLSX from 'xlsx';
import {ICON_SELECT_FILE} from "../../../../../shared/other/icon";

type AOA = any[][];
@Component({
  selector: 'import-excel-choose-file',
  templateUrl: './import-choose-file.component.html',
  styleUrls: ['./import-choose-file.component.scss'],
})
export class ImportChooseFileComponent extends BaseComponent implements OnInit {
  private modalRef: NgbModalRef | undefined;
  isLoading = false;
  importTypes: any = [];
  importTypeValue = 1;
  sheets: any = [];
  sheetValue: any;
  urlFile: string;
  data: AOA = [
    [1, 2],
    [3, 4],
  ];
  @Output() importTypeChangeEvent: EventEmitter<any> = new EventEmitter();
  @Output() sheetChangeEvent: EventEmitter<any> = new EventEmitter();
  @Output() openFileEvent: EventEmitter<any> = new EventEmitter();
  @Input() urlFileTemplate: string;

  constructor(
    public activeModal: NgbActiveModal,
    public loading: LoadingOption,
    protected modalService: NgbModal,
    public loadingOption: LoadingOption
  ) {
    super();
  }

  companyId: number;
  company: any;
  fileData: any;
  fileDataHeader: any;
  fileName: any;
  fileType = ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'application/vnd.ms-excel'];

  async ngOnInit() {
    this.importTypes = [
      { id: 1, name: 'Import mới' },
      // {id: 2, name: 'Import cập nhật'}
    ];
    this.importTypeChangeEvent.emit(this.importTypeValue);
    this.urlFile = this.urlFileTemplate;
  }

  onLoadFile() {
    const reader = new FileReader();
    reader.readAsBinaryString(this.fileData);
    try {
      reader.onload = async e => {
        const workbook = await XLSX.read(reader.result, { type: 'binary' });
        const sheetsResult = workbook.SheetNames;
        for (let i = 0; i < sheetsResult.length; i++) {
          let workSheet = workbook.Sheets[sheetsResult[i]];
          const item = {
            id: i,
            name: sheetsResult[i],
          };
          this.sheets.push(item);
          this.fileDataHeader = <AOA>XLSX.utils.sheet_to_json(workSheet, { header: 1 })[0];
        }
        this.sheetValue = this.sheets[0].id;
        this.sheetChangeEvent.emit(this.sheetValue);
      };
    } catch (e) {
      console.error(e);
    }
  }

  async openFile(event: any) {
    this.sheets = [];
    this.fileData = event.target.files[0];

    if (this.fileType.includes(this.fileData.type)) {
      await this.onLoadFile();
      this.fileName = this.fileData.name;
      const fileDataResult = {
        name: this.fileName,
        data: this.fileData,
        header: this.fileDataHeader,
      };
      this.openFileEvent.emit(fileDataResult);
    }
  }

  changeType(importTypeValue: number) {
    this.importTypeChangeEvent.emit(importTypeValue);
  }

  changeSheet(sheetValue: number) {
    this.sheetChangeEvent.emit(sheetValue);
  }

    protected readonly ICON_SELECT_FILE = ICON_SELECT_FILE;
}
