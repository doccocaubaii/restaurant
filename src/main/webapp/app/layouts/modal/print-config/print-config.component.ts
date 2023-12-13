import { Component, OnDestroy, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from '../../../shared/base/base.component';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfigService } from '../../config/service/config.service';
import { db } from '../../../db';

@Component({
  selector: 'jhi-invoice-configuration',
  templateUrl: './print-config.component.html',
  styleUrls: ['./print-config.component.scss'],
})
export class PrintConfigComponent extends BaseComponent implements OnInit, OnDestroy {
  printConfigs: any = [];
  printConfig: any = {};
  lastCompany: any = {};
  numbers: number[] = Array.from({ length: 100 }, (_, i) => i + 1);
  type = 0;
  ngOnDestroy(): void {}
  constructor(
    private modalService: NgbModal,
    private configService: ConfigService,
    private toast: ToastrService,
    public activeModal: NgbActiveModal
  ) {
    super();
  }
  async ngOnInit() {
    this.printConfigs = [];
    this.lastCompany = await this.getCompany();
    await this.getAllPrintConfig();
  }
  getAllPrintConfig() {
    this.configService.getAllPrintConfig(this.lastCompany.id, { type: -1 }).subscribe(res => {
      this.printConfigs = res.body.data;
      for (let item of this.printConfigs) {
        item.value.isBoldItem = item.value.isBold;
        item.value.isPrintItem = item.value.isPrint;
      }
    });
  }
  closeModal() {
    this.activeModal.close();
  }
  changeNumber(printConfig: any, isReduce: boolean) {
    isReduce ? printConfig.fontSize-- : printConfig.fontSize++;
  }

  savePrintConfig() {
    this.configService.updateListPrintConfig(this.printConfigs).subscribe(async res => {
      if (res.status) {
        this.toast.success(res.message[0].message);
        this.closeModal();
        await db.last_print_config.bulkPut(this.printConfigs);
      }
    });
  }

  changeBold(value, index) {
    this.printConfigs[index].value.isBold = value ? 1 : 0;
  }

  changeIsPrint(isPrintItem: any, i) {
    this.printConfigs[i].value.isPrint = isPrintItem ? 1 : 0;
  }

  changeAlignText(i: number, value: number) {
    this.printConfigs[i].value.alignText = value;
  }

  searchByType(number: number) {
    this.type = number;
  }
}
