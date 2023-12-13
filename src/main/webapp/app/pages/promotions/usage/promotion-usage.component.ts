import { Component, Input, OnInit } from '@angular/core';
import { VoucherService } from '../../voucher/voucher.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UtilsService } from '../../../utils/Utils.service';
import { ConvertResponse } from '../../../config/convert-response';
import { VoucherUsageModel } from '../../../entities/voucher/VoucherUsage.model';

@Component({
  selector: 'jhi-promotion-usage',
  templateUrl: './promotion-usage.component.html',
  styleUrls: ['./promotion-usage.component.scss'],
})
export class PromotionUsageComponent implements OnInit {
  @Input() data: any;
  title: string = 'Chi tiết lịch sử khuyến mại';

  constructor(private voucherService: VoucherService, private activeModal: NgbActiveModal, protected utilsService: UtilsService) {}

  ngOnInit(): void {
    this.getAllUsage();
  }

  dismiss($event: MouseEvent) {
    this.activeModal.dismiss();
  }

  fromDate: any;
  toDate: any;
  page: number = 1;
  pageSize: number = 20;
  totalSize: number = 0;
  voucherUsages: VoucherUsageModel[] = [];
  keyword: string = '';
  isSearch = false;

  getAllUsage() {
    const { comId, voucherId } = this.data;
    const request = {
      comId: comId,
      voucherId: voucherId,
      page: this.page,
      pageSize: 20,
      fromDate: this.fromDate ? this.utilsService.convertDate(this.fromDate).toString().slice(0, 19).replace('T', ' ') : null,
      toDate: this.toDate ? this.utilsService.convertDate(this.toDate).toString().slice(0, 19).replace('T', ' ') : null,
      keyword: this.keyword,
    };

    this.voucherService.getVoucherUsageDetail(request).subscribe(
      response => {
        this.voucherUsages = response.data;
        this.totalSize = response.count;
      },
      error => {
        this.voucherUsages = [];
        this.totalSize = 0;
      }
    );
  }

  loadMore() {
    this.getAllUsage();
  }
}
