import { Component, Input, OnInit } from '@angular/core';
import { VoucherModel } from '../../../entities/voucher/Voucher.model';
import { VoucherConstants } from '../../../constants/voucher.constants';
import { formatDate } from '@angular/common';
import { VoucherConditionsModel } from '../../../entities/voucher/VoucherConditions.model';
import { DOW_MAP } from '../../../constants/times.constants';
import { ProductService } from '../../order/service/product.service';
import { ProductGroupService } from '../../order/service/product-group.service';
import { lastValueFrom } from 'rxjs';
import { BaseComponent } from '../../../shared/base/base.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { PromotionUsageComponent } from '../usage/promotion-usage.component';
import dayjs from 'dayjs';
import { VoucherDifferentExtConditionsModel } from '../../../entities/voucher/VoucherDifferentExtConditions.model';

@Component({
  selector: 'promotions-detail',
  templateUrl: './promotions-detail.component.html',
  styleUrls: ['./promotions-detail.component.scss'],
})
export class PromotionsDetailComponent extends BaseComponent implements OnInit {
  @Input() promotion: VoucherModel;
  indexPromotionInfo: number = 0;
  extMonth: string;
  extDays: string;
  extBirthDay: boolean;
  extDayOfWeek: string;
  extHour: string;
  extIgnore: string;
  promotionType: number;
  conditions: VoucherConditionsModel[] = [];
  differentExtConditions: VoucherDifferentExtConditionsModel;
  companyId: number;
  company: any;

  constructor(private productService: ProductService, private productGroupService: ProductGroupService, private modalService: NgbModal) {
    super();
  }

  async ngOnInit() {
    this.company = await this.getCompany();
    this.companyId = this.company.id;
    if (this.indexPromotionInfo === 0) {
      this.convertDataDetail();
    }
  }

  convertDataDetail() {
    const { type, conditions, extTimeConditions, differentExtConditions } = this.promotion;
    this.promotionType = type;
    this.conditions = conditions;
    this.differentExtConditions = differentExtConditions;
    this.convertConditionDetail();
    if (this.promotion && extTimeConditions && extTimeConditions.length > 0) {
      extTimeConditions.map((item, index) => {
        let { type, values, birthday } = item;
        const valueConvert = values.toString().replace('[', '').replace(']', '').toString();
        if (type === VoucherConstants.MONTHS) {
          this.extMonth = valueConvert;
        } else if (type === VoucherConstants.DAYS) {
          this.extDays = valueConvert;
          this.extBirthDay = birthday;
        } else if (type === VoucherConstants.DAYS_OF_THE_WEEK) {
          let valuesConvert: string[] = [];
          values.forEach(v => {
            const valueConvert = DOW_MAP.get(v);
            if (valueConvert) {
              valuesConvert.push(valueConvert);
            }
          });
          this.extDayOfWeek = valuesConvert.toString().replace('[', '').replace(']', '').toString();
        } else if (type === VoucherConstants.TIME_SLOTS) {
          this.extHour = valueConvert;
        } else if (type === VoucherConstants.IGNORE_DAYS) {
          const values = valueConvert.split(',');
          let resultConvert: string = '';
          if (values && values.length > 0) {
            values.forEach(value => {
              const valueFormat = dayjs(value, 'YYYY-MM-DD').format('DD/MM/YYYY');
              resultConvert += resultConvert === '' ? valueFormat : ', ' + valueFormat;
            });
          }
          this.extIgnore = resultConvert;
        }
      });
    }
  }

  convertConditionDetail() {
    this.conditions.forEach(item => {
      const propertiesToCheck = ['getProductProductUnitId', 'getProductGroupId', 'buyProductProductUnitId', 'buyProductGroupId'];

      for (const property of propertiesToCheck) {
        if (!item[property]) {
          item[property] = [];
        }
      }

      item.conditionGetContent = this.convertContentCondition(item.getProductProductUnitId, item.getProductGroupId);
      item.conditionBuyContent = this.convertContentCondition(item.buyProductProductUnitId, item.buyProductGroupId);
      this.setConditionType(item, item.conditionGetContent, 'conditionGetType');
      this.setConditionType(item, item.conditionBuyContent, 'conditionBuyType');
    });
    this.mapConditionIdAndName();
  }

  prodProdIds: any[] = [];
  groupIds: any[] = [];
  prodResultMap = new Map();
  groupResultMap = new Map();

  setConditionType(item, content, typePropertyName) {
    if (content.includes(VoucherConstants.PROMOTION_CONDITION_GROUP)) {
      item[typePropertyName] = 0;
      this.groupIds.push(...item.getProductGroupId);
      this.groupIds.push(...item.buyProductGroupId);
    } else {
      item[typePropertyName] = 1;
      this.prodProdIds.push(...item.getProductProductUnitId);
      this.prodProdIds.push(...item.buyProductProductUnitId);
    }
  }

  async mapConditionIdAndName() {
    if (this.groupIds.length > 0) {
      const uniqueNumbers = [...new Set(this.groupIds)];
      const req = {
        comId: this.companyId,
        ids: uniqueNumbers,
      };

      let response = await lastValueFrom(this.productGroupService.getProductGroupFromProductGroupId(req));
      let result = response.body.data;
      if (result) {
        result.forEach(res => {
          this.groupResultMap.set(res.id, res.name);
        });
      }
    }

    if (this.prodProdIds.length > 0) {
      const uniqueNumbers = [...new Set(this.prodProdIds)];
      const req = {
        comId: this.companyId,
        ids: uniqueNumbers,
      };

      let response = await lastValueFrom(this.productService.getProductFromProductId(req));
      let result = response.body.data;
      if (result) {
        result.forEach(res => {
          this.prodResultMap.set(res.productProductUnitId, res);
        });
      }
    }
  }

  conditionGetContentSet = new Set();

  private convertContentCondition(prodProdUnit: number[], prodGroup: number[]) {
    const conditions = [
      { values: prodGroup, label: VoucherConstants.PROMOTION_CONDITION_GROUP },
      { values: prodProdUnit, label: VoucherConstants.PROMOTION_CONDITION_PRODUCT },
    ];

    conditions.forEach(condition => {
      if (condition.values?.length > 0) {
        this.conditionGetContentSet.add(condition.label);
      } else {
        this.conditionGetContentSet.delete(condition.label);
      }
    });

    return Array.from(this.conditionGetContentSet).sort().join(' - ');
  }

  convertDate(date, pattern) {
    if (date) {
      const myDate = new Date(date);
      return formatDate(myDate, pattern, 'en-US');
    }
    return date;
  }

  protected readonly VoucherConstants = VoucherConstants;

  private modalRef: NgbModalRef | undefined;

  viewMoreUsage(id: any) {
    const data = {
      comId: this.companyId,
      voucherId: id,
    };
    this.modalRef = this.modalService.open(PromotionUsageComponent, { size: 'xl', backdrop: 'static' });
    this.modalRef.componentInstance.data = data;
  }
}
