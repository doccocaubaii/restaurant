import { ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { UtilsService } from '../../../utils/Utils.service';
import { BaseComponent } from '../../../shared/base/base.component';
import { VoucherModel } from '../../../entities/voucher/Voucher.model';
import { VoucherConditionsModel } from '../../../entities/voucher/VoucherConditions.model';
import dayjs from 'dayjs/esm';
import { DOW_VALUE_UPPER_CASE, MONTHS_DEFAULT } from '../../../constants/times.constants';
import { Page } from '../../const/customer-order.const';
import { FilterProduct } from '../../order/model/filterProduct.model';
import { ProductService } from '../../order/service/product.service';
import { ProductGroupService } from '../../order/service/product-group.service';
import { IProduct } from '../../order/model/product.model';
import { FilterProductGroup } from '../../order/model/filterProductGroup.mode';
import { IProductGroup } from '../../order/model/product-group.model';
import { VoucherConstants } from '../../../constants/voucher.constants';
import { VoucherService } from '../../voucher/voucher.service';
import {
  PRODUCT_CODE_DEFAULT,
  PRODUCT_CODE_DISCOUNT_BILL_DEFAULT,
  PRODUCT_CODE_NOTE_DEFAULT,
  PRODUCT_CODE_PROMOTION_DEFAULT,
  PRODUCT_CODE_SERVICE_CHARGE_DEFAULT,
} from '../../../constants/common.constants';
import { lastValueFrom } from 'rxjs';
import { LoadingBarService } from '@ngx-loading-bar/core';

@Component({
  selector: 'jhi-promotion-save',
  templateUrl: './promotion-save.component.html',
  styleUrls: ['./promotion-save.component.scss'],
})
export class PromotionSaveComponent extends BaseComponent implements OnInit {
  @Input() promotionInput: VoucherModel;
  promotion: VoucherModel;
  conditionDefault: VoucherConditionsModel = {
    discountType: 0,
    discountValue: 0,
    discountPercent: null,
    minValue: null,
    maxValue: null,
    getQuantity: null,
    getProductProductUnitId: [],
    getProductProductUnitIdView: [],
    getProductGroupId: [],
    getProductGroupIdView: [],
    buyQuantity: null,
    buyProductProductUnitId: [],
    buyProductProductUnitIdView: [],
    buyProductGroupId: [],
    buyProductGroupIdView: [],
    desc: '',
    conditionGetContent: '',
    conditionBuyContent: '',
    conditionGetType: 0,
    conditionBuyType: 0,
  };
  conditions: VoucherConditionsModel[] = [];
  discountType: number = 0;
  conditionType: any;
  companyId: number;
  company: any;
  conditionGetContentSet = new Set();
  isLoadingSuccess = false;
  isUpdate = false;
  isLoadingProdAndGroup = false;

  constructor(
    public activeModal: NgbActiveModal,
    private toast: ToastrService,
    protected utilsService: UtilsService,
    private productService: ProductService,
    private productGroupService: ProductGroupService,
    private voucherService: VoucherService,
    private cdref: ChangeDetectorRef,
    private loadingBar: LoadingBarService
  ) {
    super();
  }

  disableAllBox() {
    this.visibleIgnoreDays = false;
    if (this.viewBuyProd) {
      this.conditions.forEach(item => {
        item.buyProductProductUnitIdView = JSON.parse(JSON.stringify(item.buyProductProductUnitId));
        item.buyProductGroupIdView = JSON.parse(JSON.stringify(item.buyProductGroupId));
      });
    } else if (this.viewGetProd) {
      this.conditions.forEach(item => {
        item.getProductProductUnitIdView = JSON.parse(JSON.stringify(item.getProductProductUnitId));
        item.getProductGroupIdView = JSON.parse(JSON.stringify(item.getProductGroupId));
      });
    }
    this.onResetDataGetProd(false);
  }

  async ngOnInit() {
    this.promotion = JSON.parse(JSON.stringify(this.promotionInput));
    this.company = await this.getCompany();
    this.companyId = this.company.id;
    if (!this.promotion) {
      this.promotion = {
        id: null,
        comId: this.companyId,
        name: '',
        status: 1,
        type: 100,
        code: '',
        startTime: dayjs(),
        endTime: null,
        applyType: '',
        conditions: [],
        historyUsage: [],
        extTimeConditions: [],
        active: true,
        differentExtConditions: {
          autoApplyVoucher: true,
          isFixedQuantity: true,
        },
      };
      this.conditionType = null;
    } else {
      this.isUpdate = true;
      this.conditionType = this.promotion.type;
    }
    if (!this.promotion.conditions || this.promotion.conditions.length == 0) {
      this.promotion.conditions.push(JSON.parse(JSON.stringify(this.conditionDefault)));
    } else {
      this.conditions = JSON.parse(JSON.stringify(this.promotion.conditions));
    }
    this.promotion.startTime = dayjs(this.promotion.startTime);
    this.onStartTimeChange(false);
    if (this.promotion.endTime) {
      this.promotion.endTime = dayjs(this.promotion.endTime);
      this.onEndTimeChange(false);
    }
    const { extTimeConditions } = this.promotion;
    if (extTimeConditions) {
      extTimeConditions.forEach(item => {
        const values = item.values;
        switch (item.type) {
          case VoucherConstants.DAYS_OF_THE_WEEK: {
            this.dowSave = values;
            break;
          }
          case VoucherConstants.DAYS: {
            this.daysSave = values;
            this.birthDaySave = item.birthday;
            break;
          }
          case VoucherConstants.IGNORE_DAYS: {
            values.forEach(value => {
              this.ignoreDaysSave.push(dayjs(value).format('DD/MM/YYYY'));
            });
            this.ignoreDaysSave.forEach(value => {
              this.datesSelected.push(this.parseDate(value));
            });
            break;
          }
          case VoucherConstants.MONTHS: {
            values.forEach(value => {
              this.monthsSave.push(Number(value));
            });
            break;
          }
          case VoucherConstants.TIME_SLOTS: {
            values.forEach(value => {
              if (value && value.length === 11) {
                const timeSlot = {
                  from: value.slice(0, 5),
                  to: value.slice(6, 11),
                };
                this.timeSlots.push(timeSlot);
              }
            });
            const lengthTimeSlots = this.timeSlots.length - 1;
            if (this.timeSlots.length > 0) {
              this.timeSlotFromStr = this.timeSlots[lengthTimeSlots].from;
              this.timeSlotToStr = this.timeSlots[lengthTimeSlots].to;
              this.timeSlotValid = true;
            }
            break;
          }
        }
      });
    }

    if (this.conditionType && [102, 200].includes(this.conditionType)) {
      this.loadingBar.start();
      this.conditions.forEach(item => {
        const propertiesToCheck = ['getProductProductUnitId', 'getProductGroupId', 'buyProductProductUnitId', 'buyProductGroupId'];

        for (const property of propertiesToCheck) {
          if (!item[property]) {
            item[property] = [];
          }
        }
        item.getProductProductUnitIdView = JSON.parse(JSON.stringify(item.getProductProductUnitId));
        item.getProductGroupIdView = JSON.parse(JSON.stringify(item.getProductGroupId));
        item.buyProductProductUnitIdView = JSON.parse(JSON.stringify(item.buyProductProductUnitId));
        item.buyProductGroupIdView = JSON.parse(JSON.stringify(item.buyProductGroupId));
        item.conditionGetContent = this.convertContentCondition(item.getProductProductUnitId, item.getProductGroupId);
        item.conditionBuyContent = this.convertContentCondition(item.buyProductProductUnitId, item.buyProductGroupId);
        this.setConditionType(item, item.conditionGetContent, 'conditionGetType');
        this.setConditionType(item, item.conditionBuyContent, 'conditionBuyType');
      });
      await this.mapConditionIdAndName();
      this.loadingBar.complete();
    }
    this.checkLoadAllSuccess();
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
      this.prodProdIds.push(...item.getProductProductUnitIdView);
      this.prodProdIds.push(...item.buyProductProductUnitId);
      this.prodProdIds.push(...item.buyProductProductUnitIdView);
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
          this.prodResultMap.set(res.productProductUnitId, res.productName);
        });
      }
    }
  }

  dismiss() {
    this.activeModal.dismiss();
  }

  isEnableDiscountType = true;
  isEnableExtTime = true;
  discountConditionsTypes: any[] = [
    {
      id: 100,
      name: 'Khuyến mại theo đơn hàng - Giảm giá theo tổng tiền',
    },
    {
      id: 102,
      name: 'Khuyến mại theo đơn hàng - Tặng hàng hoá',
    },
    {
      id: 200,
      name: 'Khuyến mại theo sản phẩm - Mua hàng khuyến mại hàng',
    },
  ];

  months: any[] = [];
  days: any[] = [];
  ignoreDays: any[] = [];
  dows: any[] = []; // days_of_the_week

  discountCondition: VoucherConditionsModel[] = [];

  changeViewDiscountType() {
    this.isEnableDiscountType = !this.isEnableDiscountType;
  }

  changeViewExtTime() {
    this.isEnableExtTime = !this.isEnableExtTime;
  }

  private checkDiscountCondition(discountType: number, discountValue: number, discountPercent: number): any {
    if ([VoucherConstants.VOUCHER_DISCOUNT_BILL_BONUS_PRODUCT].includes(Number(this.conditionType))) {
      return true;
    }
    if (discountType === 0) {
      if (!discountPercent) {
        this.toast.error('Giá trị giảm giá theo phần trăm không để trống');
        return false;
      } else if (discountPercent > 100) {
        this.toast.error('Giá trị giảm giá theo phần trăm không hợp lệ');
        return false;
      }
    } else if (discountType === 1 && !discountValue) {
      this.toast.error('Giá trị giảm giá theo giá tiền không để trống');
      return false;
    }
    return true;
  }

  private checkCondition(item: VoucherConditionsModel) {
    if (
      [VoucherConstants.VOUCHER_DISCOUNT_BILL_BONUS_PRODUCT, VoucherConstants.VOUCHER_DISCOUNT_BILL_TOTAL_AMOUNT].includes(
        this.conditionType
      )
    ) {
      if (!item.minValue) {
        this.toast.error('Không để trống Giá trị đơn hàng tối thiểu');
        return false;
      }

      if (item.maxValue && Number(item.minValue) >= Number(item.maxValue)) {
        this.toast.error('Giá trị đơn hàng tối đa phải lớn hơn giá trị tối thiểu');
        return false;
      }
    }

    if (VoucherConstants.VOUCHER_DISCOUNT_BILL_BUY_AND_BONUS_PRODUCT === this.conditionType) {
      if (!item.buyQuantity) {
        this.toast.error('Số lượng sản phẩm/ nhóm sản phẩm mua không để trống');
        return false;
      }
      if (item.buyProductGroupId.length < 1 && item.buyProductProductUnitId.length < 1) {
        this.toast.error('Sản phẩm/ nhóm sản phẩm mua không để trống');
        return false;
      }
    }
    if (!this.checkDiscountCondition(item.discountType, item.discountValue, item.discountPercent)) {
      return false;
    }

    if (
      [VoucherConstants.VOUCHER_DISCOUNT_BILL_BONUS_PRODUCT, VoucherConstants.VOUCHER_DISCOUNT_BILL_BUY_AND_BONUS_PRODUCT].includes(
        this.conditionType
      )
    ) {
      if (!item.getQuantity) {
        this.toast.error('Số lượng sản phẩm/ nhóm sản phẩm khuyến mại không để trống');
        return false;
      }
      if (item.getProductGroupId.length < 1 && item.getProductProductUnitId.length < 1) {
        this.toast.error('Sản phẩm/ nhóm sản phẩm khuyến mại không để trống');
        return false;
      }
    }
    return true;
  }

  private checkAllConditions(checkFinal: boolean) {
    let checkValid = false;
    let maxValueOld: number = 0;
    for (let i = 0; i < this.conditions.length; i++) {
      const item = this.conditions[i];
      if (
        ![VoucherConstants.VOUCHER_DISCOUNT_BILL_BUY_AND_BONUS_PRODUCT].includes(Number(this.conditionType)) &&
        Number(maxValueOld) >= Number(item.minValue)
      ) {
        this.toast.error(`Giá trị đơn hàng tối thiểu ${Number(item.minValue)}(VND) không hợp lệ`);
        checkValid = false;
        break;
      }
      checkValid = this.checkCondition(item);
      if (!checkValid) break;
      if (![VoucherConstants.VOUCHER_DISCOUNT_BILL_BUY_AND_BONUS_PRODUCT].includes(Number(this.conditionType))) {
        if (!checkFinal) {
          if (!item.maxValue) {
            this.toast.error('Không để trống Giá trị đơn hàng tối đa');
            return false;
          }
        } else {
          if (!item.maxValue && i < this.conditions.length - 1) {
            this.toast.error('Không để trống Giá trị đơn hàng tối đa');
            return false;
          }
        }
      }
      maxValueOld = item.maxValue;
    }
    return checkValid;
  }

  onAddNewCondition() {
    if (!this.conditions || this.conditions.length >= 0) {
      if (this.checkAllConditions(false)) {
        this.onResetDataGetProd(false);
        this.conditions.push(JSON.parse(JSON.stringify(this.conditionDefault)));
      }
    }
  }

  onDeleteConditionItem(indexRequest: number) {
    if (indexRequest >= 0 && indexRequest < this.conditions.length) {
      const condition = this.conditions[indexRequest];
      if (condition.conditionBuyType === 1) {
        condition.buyProductProductUnitIdView.forEach(id => {
          const data = this.getValidGroupIdsRemove(id);
          this.removeProdUnitId(true, data);
        });
        condition.buyProductProductUnitId = [];
        condition.buyProductProductUnitIdView = [];
      } else {
        condition.buyProductGroupIdView.forEach(id => {
          const data = this.getValidProdIdsRemove(id);
          this.removeProdUnitId(true, data);
        });
        condition.buyProductProductUnitId = [];
        condition.buyProductProductUnitIdView = [];
      }
      this.conditions.splice(indexRequest, 1);
    }
  }

  getAfterPresent(date: any) {
    let present = dayjs();
    if (present.isAfter(date)) return present;
    return date;
  }

  private checkFromDateAndToDate() {
    if (!this.promotion.startTime) {
      this.toast.error('Không để trống ngày bắt đầu chương trình');
      return false;
    } else if (!this.promotion.endTime) {
      // this.toast.error('Không để trống ngày kết thúc chương trình');
      // return false;
    }
    return true;
  }

  onStartTimeChange(changeStatus: boolean) {
    const { startTime, endTime } = this.promotion;
    if (startTime) {
      this.formatAllDateTime(startTime, endTime);
    }
    if (startTime.isAfter(endTime)) {
      this.promotion.endTime = null;
    }
    if (changeStatus) {
      if (startTime.isAfter(dayjs())) {
        this.promotion.status = 0;
      } else {
        this.promotion.status = 1;
      }
    }
  }

  private formatAllDateTime(startTime: any, endTime: any) {
    let endTimeValue: any;
    if (endTime) {
      endTimeValue = endTime;
    } else {
      endTimeValue = JSON.parse(JSON.stringify(startTime.add(1, 'year')));
    }
    const monthInRange = this.utilsService.getAllMonthInRange(startTime, endTimeValue);
    this.months = [];
    MONTHS_DEFAULT.forEach(item => {
      if (monthInRange.includes(item.id)) {
        this.months.push(item);
      }
    });
    this.dows = this.utilsService.getWeekdaysInRange(startTime, endTimeValue);
    this.days = this.utilsService.getDaysInRange(startTime, endTimeValue);

    const monthIds = this.months.map(value => value.id);
    if (endTime) {
      this.ignoreDays = this.utilsService.getAllDatesInRange(startTime, endTime, monthIds);
    }
    const dowIds = this.dows.map(value => value.id);
    if (this.monthsSave) {
      if (this.monthsSave) {
        this.monthsSave = this.monthsSave.filter(item => monthIds.includes(item));
      } else {
        this.monthsSave = monthIds;
      }
    }
    if (this.dowSave) {
      if (this.dowSave) {
        this.dowSave = this.dowSave.filter(item => dowIds.includes(item));
      } else {
        this.dowSave = dowIds;
      }
    }
    if (this.daysSave) {
      if (this.daysSave) {
        this.daysSave = this.daysSave.filter(item => this.days.includes(item));
      } else {
        this.daysSave = this.days;
      }
    }

    if (this.ignoreDaysSave && this.ignoreDaysSave.length > 0) {
      let allItemUnSelected: NgbDateStruct[] = [];
      if (endTime) {
        const jsDateArray = this.ignoreDays.map(date => dayjs(date).add(1, 'day'));
        const formattedDateArray = jsDateArray.map(date => {
          return date.toISOString().split('T')[0];
        });

        this.ignoreDaysSave = this.ignoreDaysSave.filter((item, index) => {
          const itemConvert = dayjs(item, 'DD/MM/YYYY').format('YYYY-MM-DD');
          if (formattedDateArray.includes(itemConvert)) {
            return formattedDateArray.includes(itemConvert);
          } else {
            allItemUnSelected.push(this.parseDate(item));
            return false;
          }
        });
      } else {
        const ignoreOldStart = dayjs(this.ignoreDaysSave[0], 'DD/MM/YYYY');
        const formatStartTime = dayjs(startTime.format('DD/MM/YYYY'), 'DD/MM/YYYY');
        if (formatStartTime.isAfter(ignoreOldStart)) {
          const end = startTime.subtract(1, 'day');
          const dateUnselected = this.utilsService.getAllDatesInRange(ignoreOldStart, end, []);
          let dateString: any[] = [];
          if (dateUnselected && dateUnselected.length > 0) {
            dateUnselected.forEach(item => {
              const format = item.format('DD/MM/YYYY');
              dateString.push(format);
              allItemUnSelected.push(this.parseDate(format));
            });
            this.ignoreDaysSave = this.ignoreDaysSave.filter(item => {
              return !dateString.includes(item);
            });
          }
        }
      }

      this.requestUnSelected = {
        indexUnSelected: [],
        itemUnSelected: allItemUnSelected,
        itemDisabled: [],
        onlyMonths: [],
      };

      // this.ignoreDaysSave = this.ignoreDaysSave.filter((item, index) => {
      //   const itemConvert = dayjs(item, 'DD/MM/YYYY').format('YYYY-MM-DD');
      //   if (formattedDateArray.includes(itemConvert)) {
      //     return formattedDateArray.includes(itemConvert);
      //   } else {
      //     allItemUnSelected.push(this.parseDate(item));
      //     return false;
      //   }
      // });
    }
  }

  onEndTimeChange(changeStatus: boolean) {
    const { startTime, endTime } = this.promotion;
    if (startTime) {
      this.formatAllDateTime(startTime, endTime);
    }
    if (changeStatus) {
      if (dayjs().format('YYYY-MM-DD') !== endTime.format('YYYY-MM-DD') && dayjs().isAfter(endTime)) {
        this.promotion.status = 0;
      } else {
        this.promotion.status = 1;
      }
    }
  }

  disableSave = false;

  onSave() {
    if (this.validatePromotion() && this.checkAllConditions(true) && this.checkFromDateAndToDate()) {
      this.loadingBar.start();
      let { startTime, endTime } = this.promotion;
      const extTimeConditionsValid = this.convertExtTimePromotion();
      if (!extTimeConditionsValid) {
        this.loadingBar.complete();
        this.disableSave = false;
        return;
      }
      this.promotion.extTimeConditions = extTimeConditionsValid;
      this.promotion.conditions = this.conditions;
      let req = Object.assign({}, this.promotion);
      req.startTime = startTime?.format('YYYY-MM-DD');
      req.endTime = endTime ? endTime.format('YYYY-MM-DD') : null;
      req.comId = this.companyId;
      if (![VoucherConstants.VOUCHER_DISCOUNT_BILL_BONUS_PRODUCT].includes(Number(this.conditionType))) {
        req.conditions.forEach(item => {
          if (item.discountType === 0) {
            item.discountValue = item.discountPercent;
          }
          if (typeof item.discountValue === 'string') {
            item.discountValue = Number(item.discountValue.replace(',', ''));
          }
        });
      }
      req.type = this.conditionType;
      if (!this.promotion.id) {
        this.promotion.id = null;
        // console.log(req);
        this.voucherService.postVoucher(req).subscribe(
          response => {
            this.loadingBar.complete();
            if (response.status) {
              this.toast.success(response.message[0].message);
              this.activeModal.close(true);
            }
          },
          e => {
            this.loadingBar.complete();
            this.disableSave = false;
            // this.toast.error("Lưu thông tin CTKM không thành công");
            console.log(e);
          }
        );
      } else {
        this.voucherService.putVoucher(req).subscribe(
          response => {
            this.loadingBar.complete();
            if (response.status) {
              this.toast.success(response.message[0].message);
              this.activeModal.close(true);
            }
          },
          e => {
            this.loadingBar.complete();
            this.disableSave = false;
            // this.toast.error("Lưu thông tin CTKM không thành công");
            console.log(e);
          }
        );
      }
    } else {
      this.disableSave = false;
    }
  }

  private convertExtTimePromotion() {
    let extTimeConditions: any[] = [];
    if (this.dowSave && this.dowSave.length > 0) {
      extTimeConditions.push({
        type: VoucherConstants.DAYS_OF_THE_WEEK,
        values: this.dowSave,
        birthday: null,
      });
    }
    if (this.monthsSave && this.monthsSave.length > 0) {
      extTimeConditions.push({
        type: VoucherConstants.MONTHS,
        values: this.monthsSave,
        birthday: null,
      });
    }
    extTimeConditions.push({
      type: VoucherConstants.DAYS,
      values: this.daysSave && this.daysSave.length > 0 ? this.daysSave : [],
      birthday: this.birthDaySave,
    });
    if (this.ignoreDaysSave && this.ignoreDaysSave.length > 0) {
      const ignoreDaysReq = this.ignoreDaysSave.map(item => dayjs(item, 'DD/MM/YYYY').format('YYYY-MM-DD'));
      extTimeConditions.push({
        type: VoucherConstants.IGNORE_DAYS,
        values: ignoreDaysReq,
        birthday: null,
      });
    }
    if (this.timeSlots && this.timeSlots.length > 0) {
      this.checkTimeSlotValid();
      if (!this.timeSlotsIsEmpty()) {
        if (!this.timeSlotValid) {
          this.toast.error('Thời gian áp dụng theo giờ không hợp lệ');
          return null;
        } else {
          this.timeSlotsSave = [];
          this.timeSlots.forEach(item => {
            if (item) {
              const convertItem = item.from + '-' + item.to;
              if (!this.timeSlotsSave.includes(convertItem)) this.timeSlotsSave.push(item.from + '-' + item.to);
            }
          });
        }
      }
    }

    if (this.timeSlotsSave && this.timeSlotsSave.length > 0) {
      extTimeConditions.push({
        type: VoucherConstants.TIME_SLOTS,
        values: this.timeSlotsSave,
        birthday: null,
      });
    }
    return extTimeConditions;
  }

  private validatePromotion() {
    if (
      !this.conditionType ||
      ![
        VoucherConstants.VOUCHER_DISCOUNT_BILL_TOTAL_AMOUNT,
        VoucherConstants.VOUCHER_DISCOUNT_BILL_BONUS_PRODUCT,
        VoucherConstants.VOUCHER_DISCOUNT_BILL_BUY_AND_BONUS_PRODUCT,
      ].includes(Number(this.conditionType))
    ) {
      this.toast.error('Không để trống Hình thức khuyến mại');
      return false;
    }
    const { code, name } = this.promotion;
    if (!code || code.trim() === '') {
      this.toast.error('Không để trống mã Code Chương trình khuyến mại');
      return false;
    } else if (!name || name.trim() === '') {
      this.toast.error('Không để trống Tên Chương trình khuyến mại');
      return false;
    }
    return true;
  }

  monthsSave: any[] = [];
  daysSave: string[] = [];
  dowSave: any[] = [];
  timeSlotsSave: any[] = [];
  ignoreDaysSave: any[] = [];
  birthDaySave = true;

  onMonthChange(months: any) {
    const { startTime, endTime } = this.promotion;
    this.monthsSave = months;
    this.monthsSave.sort((a, b) => a - b);
    // const monthIds = this.monthsSave.map(value => Number(value));
    // this.ignoreDays = this.utilsService.getAllDatesInRange(startTime, endTime, monthIds);
    // if (this.ignoreDaysSave) {
    // const jsDateArray = this.ignoreDays.map(date => dayjs(date).add(1, 'day'));
    // const formattedDateArray = jsDateArray.map(date => {
    //   return date.toISOString().split('T')[0];
    // });
    //
    // let allItemUnSelected: NgbDateStruct[] = [];
    // let allItemDisabled: NgbDateStruct[] = [];
    // this.ignoreDaysSave = this.ignoreDaysSave.filter((item, index) => {
    //   const itemConvert = dayjs(item, 'DD/MM/YYYY').format('YYYY-MM-DD');
    //   if (formattedDateArray.includes(itemConvert)) {
    //     return formattedDateArray.includes(itemConvert);
    //   } else {
    //     allItemUnSelected.push(this.parseDate(item));
    //     return false;
    //   }
    // });
    // if (this.monthsSave.length > 0) {
    //   const allItem = this.utilsService.getDatesInRangeExcludingSpecificMonths(startTime, endTime, months);
    //   allItem.forEach(item => {
    //     const itemConvert = item.format('DD/MM/YYYY');
    //     allItemDisabled.push(this.parseDate(itemConvert));
    //   });
    // }
    //
    // this.itemUnSelected = allItemUnSelected;
    // if (!endTime) {
    //   this.requestUnSelected = {
    //     indexUnSelected: [],
    //     itemUnSelected: this.itemUnSelected,
    //     itemDisabled: [],
    //     onlyMonths: monthIds
    //   };
    // } else {
    //   this.requestUnSelected = {
    //     indexUnSelected: [],
    //     itemUnSelected: this.itemUnSelected,
    //     itemDisabled: allItemDisabled,
    //     onlyMonths: []
    //   };
    // }

    // } else {
    //   this.ignoreDaysSave = this.ignoreDays;
    // }
    // this.ignoreDaysSave = this.sortDayJsString(this.ignoreDaysSave);
  }

  onDayChange(days: any) {
    this.daysSave = days;
    this.daysSave.sort((a, b) => Number(a) - Number(b));
  }

  onDowChange(dow: any) {
    this.dowSave = dow;
    this.dowSave = this.dowSave.sort((a, b) => {
      return DOW_VALUE_UPPER_CASE.indexOf(a) - DOW_VALUE_UPPER_CASE.indexOf(b);
    });
  }

  onIgnoreDaysChange(items: any) {
    this.ignoreDaysSave = items;

    // const months: number[] = [];
    // const days: number[] = [];
    // const daysOfWeek: number[] = [];
    // const daysOfWeekNames: string[] = [];
    //
    // items.forEach((dateString) => {
    //   const dateObject: dayjs.Dayjs = dayjs(dateString, 'DD/MM/YYYY');
    //
    //   const month: number = dateObject.month() + 1;
    //   const day: number = dateObject.date();
    //   const dayOfWeek: number = dateObject.day() - 1;
    //   let dayOfWeekShortName: string = DOW_VALUE[dayOfWeek];
    //
    //   if (!months.includes(month)) {
    //     months.push(month);
    //   }
    //   if (!days.includes(day)) {
    //     days.push(day);
    //   }
    //   if (dayOfWeekShortName && !daysOfWeekNames.includes(dayOfWeekShortName.toUpperCase())) {
    //     dayOfWeekShortName = dayOfWeekShortName.toUpperCase();
    //     if (DOW_MAP.has(dayOfWeekShortName)) {
    //       daysOfWeek.push(Number(DOW_MAP.get(dayOfWeekShortName)));
    //     }
    //     daysOfWeekNames.push(dayOfWeekShortName);
    //   }
    // });
    //
    // if (months) {
    //   months.forEach(item => {
    //     if (this.monthsSave.includes(item)) {
    //       this.monthsSave = this.monthsSave.filter(month => month !== item);
    //     }
    //   })
    // }
    // if (days) {
    //   days.forEach(item => {
    //     if (this.daysSave.includes(item)) {
    //       this.daysSave = this.daysSave.filter(day => day !== item);
    //     }
    //   })
    // }
    // if (daysOfWeek) {
    //   daysOfWeek.forEach(item => {
    //     if (this.dowSave.includes(item)) {
    //       this.dowSave = this.dowSave.filter(dow => dow !== item);
    //     }
    //   })
    // }
  }

  timeSlotFromStr: string = '';
  timeSlotToStr: string = '';
  timeSlots: any[] = [];
  timeSlotValid: boolean = false;

  // onTimeSlotToChange(value: { hour: string, minute: string }) {
  //   this.timeSlotToStr = `${value.hour}:${value.minute}`;
  // }
  onAddNewTimeSlots() {
    if (!this.timeSlotValid) return;
    type Comparator<T> = (a: T, b: T) => number;
    const comparator: Comparator<{ from: string; to: string }> = (a, b) => {
      const fromTime1 = dayjs(a.from, 'HH:mm');
      const fromTime2 = dayjs(b.from, 'HH:mm');

      if (fromTime1.isBefore(fromTime2)) {
        return -1;
      } else if (fromTime1.isAfter(fromTime2)) {
        return 1;
      } else {
        return 0;
      }
    };
    this.timeSlots.sort(comparator);
    this.timeSlots.push(null);
    this.timeSlotFromStr = '';
    this.timeSlotToStr = '';
    this.timeSlotValid = false;
    const fromTimeInput = document.getElementById('fromTimeSlot') as HTMLInputElement;
    if (fromTimeInput) {
      fromTimeInput.focus();
    }
  }

  checkNewTimeSlot() {
    const newTime = {
      from: this.timeSlotFromStr,
      to: this.timeSlotToStr,
    };
    let checkDuplicate = false;
    this.timeSlots.forEach(item => {
      if (item && item.from === newTime.from && item.to === newTime.to) {
        checkDuplicate = true;
      }
    });
    if (!checkDuplicate) {
      const sizeTimeSlot = this.timeSlots.length;
      sizeTimeSlot === 0 ? this.timeSlots.push(newTime) : (this.timeSlots[this.timeSlots.length - 1] = newTime);
    }
  }

  onRemoveTimeSlotItem(index: number) {
    this.timeSlots?.splice(index, 1);
    this.checkTimeSlotValid();
    this.checkNewTimeSlot();
  }

  onTimeSlotFromChange() {
    this.checkTimeSlotValid();
    this.checkNewTimeSlot();
  }

  onTimeSlotToChange() {
    this.checkTimeSlotValid();
    this.checkNewTimeSlot();
  }

  private timeSlotsIsEmpty() {
    return this.timeSlots && this.timeSlots.length === 1 && !this.timeSlotFromStr && !this.timeSlotToStr;
  }

  private checkTimeSlotValid() {
    if (!this.timeSlotsIsEmpty() && this.timeSlotFromStr.length === 5 && this.timeSlotToStr.length === 5) {
      if (this.compareTime(this.timeSlotFromStr, this.timeSlotToStr)) {
        this.timeSlotValid = false;
        return;
      }
      if (this.timeSlots && this.timeSlots.length > 1) {
        for (let i = 0; i < this.timeSlots.length - 1; i++) {
          const item = this.timeSlots[i];
          if (this.compareTime(this.timeSlotFromStr, item.from) && this.compareTime(item.to, this.timeSlotToStr)) {
            this.timeSlotValid = false;
            return;
          }
        }
      }
      this.timeSlotValid = true;
    } else {
      this.timeSlotValid = false;
      return;
    }
    this.timeSlotValid = true;
  }

  compareTime(from: string, to: string): boolean {
    const parsedFrom = dayjs(`1970-01-01 ${from}`);
    const parsedTo = dayjs(`1970-01-01 ${to}`);

    return parsedFrom.isSame(parsedTo) || parsedFrom.isAfter(parsedTo);
  }

  onDiscountChange(event: any, index: number) {
    let conditionItem = this.conditions[index];
    if (conditionItem) {
      if (conditionItem.discountType === 0) {
        conditionItem.discountPercent = event?.target.value;
      } else {
        conditionItem.discountValue = event?.target.value;
      }
    }
  }

  viewGetProd = false;
  viewBuyProd = false;
  idViewGetProd: any;
  idViewBuyProd: any;
  filterProduct: FilterProduct = { page: Page.PAGE_NUMBER, size: 20, groupIds: [] };
  getProducts: IProduct[] = [];
  getProductGroups: IProductGroup[] | null = [];
  getProdKeyword: '';
  getProdGroupKeyword: '';
  filterProductGroup: FilterProductGroup = { page: Page.PAGE_NUMBER, size: 20 };

  onChangeDiscountType() {
    if (this.conditionType && this.promotion.type === this.conditionType) {
      this.conditions = this.promotion.conditions;
    } else {
      this.conditions = [];
      this.conditions.push(JSON.parse(JSON.stringify(this.conditionDefault)));
    }
    this.onResetDataGetProd(this.conditionType !== VoucherConstants.VOUCHER_DISCOUNT_BILL_TOTAL_AMOUNT);
  }

  getListGetProduct() {
    this.loadingBar.start();
    const productCodeDefault = [
      PRODUCT_CODE_DEFAULT,
      PRODUCT_CODE_NOTE_DEFAULT,
      PRODUCT_CODE_PROMOTION_DEFAULT,
      PRODUCT_CODE_SERVICE_CHARGE_DEFAULT,
      PRODUCT_CODE_DISCOUNT_BILL_DEFAULT,
    ];
    this.productService.query(this.filterProduct).subscribe(res => {
      const result = res.body;
      if (result) {
        result.forEach(item => {
          if (!productCodeDefault.includes(item.productCode)) {
            this.getProducts?.push(item);
          }
        });
      }
    });
    this.loadingBar.complete();
  }

  loadMore(event, conditionType: number) {
    if (event.target.scrollTop === 0) {
      return;
    }
    if (event.target.offsetHeight + event.target.scrollTop >= event.target.scrollHeight) {
      if (conditionType === 1) {
        if (this.filterProduct.page !== undefined) {
          this.filterProduct.page = this.filterProduct.page + 1;
        }
        this.getListGetProduct();
      } else {
        if (this.filterProductGroup.page !== undefined) {
          this.filterProductGroup.page = this.filterProductGroup.page + 1;
        }
        this.getListGetProductGroup();
      }
    }
  }

  onGetProdKeywordChange(event: any, conditionType: number) {
    let keyword = event.target.value;
    if (!keyword) keyword = '';
    if (conditionType === 1) {
      this.getProdKeyword = keyword;
      this.filterProduct.keyword = keyword;
      this.filterProduct.page = 0;
      this.getProducts = [];
      this.getListGetProduct();
    } else {
      this.getProdGroupKeyword = keyword;
      this.filterProductGroup.keyword = keyword;
      this.filterProductGroup.page = 0;
      this.getProductGroups = [];
      this.getListGetProductGroup();
    }
  }

  getListGetProductGroup(): void {
    this.loadingBar.start();
    this.productGroupService.query(this.filterProductGroup).subscribe({
      next: res => {
        const result = res.body;
        if (result) {
          this.getProductGroups?.push(...result);
        }
      },
    });
    this.loadingBar.complete();
  }

  onSelectGetProdItem(data: any, indexCondition: number) {
    event?.stopPropagation();
    let element: any;
    const condition = this.conditions[indexCondition];
    const selectedList = condition.conditionGetType === 1 ? condition.getProductProductUnitIdView : condition.getProductGroupIdView;

    const index = selectedList.indexOf(condition.conditionGetType === 0 ? data.id : data.productProductUnitId);
    const check = condition.conditionGetType === 0;
    if (check) {
      element = document.getElementById('getGroupCheckBox' + data.id) as HTMLInputElement;
    } else {
      element = document.getElementById('getProductCheckBox' + data.productProductUnitId) as HTMLInputElement;
    }
    if (index === -1) {
      if (check) {
        selectedList.push(data.id);
        this.groupResultMap.set(data.id, data.name);
      } else {
        selectedList.push(data.productProductUnitId);
        this.prodResultMap.set(data.productProductUnitId, data.productName);
      }
      element.checked = true;
    } else {
      element.checked = false;
      selectedList.splice(index, 1);
    }
    condition.conditionGetContent = this.convertContentCondition(condition.getProductProductUnitIdView, condition.getProductGroupIdView);
  }

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

  groupIdsBySelectProduct = new Set();
  prodIdsBySelectGroup = new Set();
  selectedGroupMap = new Map();
  selectedProdMap = new Map();

  onSelectBuyProdItem(data: any, indexCondition: number) {
    event?.stopPropagation();
    const condition = this.conditions[indexCondition];
    let selectedList: any[] = [];
    let checkDuplicate = 0;
    let element: any;
    let groupIds: number[] = [];
    if (data.groups) {
      data.groups.forEach(item => groupIds.push(item.id));
    }
    // product
    if (condition.conditionBuyType === 1) {
      for (let i = 0; i < this.conditions.length; i++) {
        if (i != indexCondition) {
          const conditionItem = this.conditions[i];
          const ppuIdsCondition = conditionItem.buyProductProductUnitIdView;
          const pgIdsCondition = conditionItem.buyProductGroupIdView;
          if (ppuIdsCondition.length > 0 && ppuIdsCondition.includes(data.productProductUnitId)) {
            checkDuplicate = 1;
            break;
          }
          if (groupIds) {
            for (const item of pgIdsCondition) {
              if (groupIds.includes(item)) {
                checkDuplicate = 3;
                break;
              }
            }
          }
        }
      }
      if (data.groups) {
        data.groups.forEach(item => {
          this.groupIdsBySelectProduct.add(item.id);
        });
      }
      selectedList = condition.buyProductProductUnitIdView;
      element = document.getElementById('buyProductCheckBox' + data.productProductUnitId) as HTMLInputElement;
    } else {
      // group
      if (this.groupIdsBySelectProduct && this.groupIdsBySelectProduct.has(data.id)) {
        checkDuplicate = 2;
      } else {
        selectedList = condition.buyProductGroupIdView;
        element = document.getElementById('buyGroupCheckBox' + data.id) as HTMLInputElement;
      }
    }

    const subDesc = ' đã trùng với điều kiện của giá trị khuyến mại khác, vui lòng kiểm tra lại!';
    if ([1, 3].includes(checkDuplicate)) {
      this.toast.error(VoucherConstants.PROMOTION_CONDITION_PRODUCT + ' ' + data.productName + subDesc);
    } else if (checkDuplicate == 2) {
      this.toast.error(VoucherConstants.PROMOTION_CONDITION_GROUP + ' ' + data.name + subDesc);
    } else {
      const index = selectedList.indexOf(condition.conditionBuyType === 0 ? data.id : data.productProductUnitId);
      const check = condition.conditionBuyType === 0;
      if (index === -1) {
        if (check) {
          let valid = true;
          for (let i = 0; i < this.conditions.length; i++) {
            if (i != indexCondition) {
              const pgIdsCondition = this.conditions[i].buyProductGroupIdView;

              if (pgIdsCondition.includes(data.id)) {
                valid = false;
                break;
              } else if (data.productIds && data.productIds.length > 0) {
                for (let pgId of pgIdsCondition) {
                  const dataCheck = this.selectedProdMap.get(pgId);
                  if (dataCheck && dataCheck.length > 0) {
                    for (const pid of data.productIds) {
                      if (dataCheck.includes(pid)) {
                        valid = false;
                        break;
                      }
                    }
                  }
                }
              }
            }
          }
          if (valid) {
            if (data.productIds && data.productIds.length > 0) {
              data.productIds.forEach(pid => this.prodIdsBySelectGroup.add(pid));
              this.selectedProdMap.set(data.id, data.productIds);
            } else {
              this.selectedProdMap.set(data.id, []);
            }
            element.checked = true;
            selectedList.push(data.id);
            this.groupResultMap.set(data.id, data.name);
          } else {
            this.toast.error(VoucherConstants.PROMOTION_CONDITION_GROUP + ' ' + data.name + subDesc);
          }
        } else {
          element.checked = true;
          selectedList.push(data.productProductUnitId);
          this.prodResultMap.set(data.productProductUnitId, data.productName);
          this.selectedGroupMap.set(data.productProductUnitId, groupIds);
        }
      } else {
        element.checked = false;
        selectedList.splice(index, 1);
        if (!check) {
          const dataGroups = this.getValidGroupIdsRemove(data.productProductUnitId);
          this.removeProdUnitId(true, dataGroups);
        } else {
          const dataProds = this.getValidProdIdsRemove(data.id);
          this.removeProdId(true, dataProds);
        }
      }
      condition.conditionBuyContent = this.convertContentCondition(condition.buyProductProductUnitIdView, condition.buyProductGroupIdView);
    }
  }

  getValidProdIdsRemove(itemId: number) {
    const dataProdIDs = this.selectedProdMap.get(itemId);
    if (dataProdIDs && dataProdIDs.length > 0) {
      this.selectedProdMap.forEach((value, key) => {
        if (key != itemId) {
          value.forEach(id => {
            if (dataProdIDs.includes(id)) {
              const index = dataProdIDs.indexOf(id);
              if (index != -1) {
                dataProdIDs.splice(index, 1);
              }
            }
          });
        }
      });
    }
    this.selectedProdMap.delete(itemId);
    return dataProdIDs;
  }

  private removeProdId(check: boolean, prodIds: number[]) {
    if (check && prodIds) {
      prodIds.forEach(item => {
        if (this.prodIdsBySelectGroup.has(item)) {
          this.prodIdsBySelectGroup.delete(item);
        }
      });
    }
  }

  private removeProdUnitId(check: boolean, dataGroupIDs: number[]) {
    if (check && dataGroupIDs) {
      dataGroupIDs.forEach(item => {
        if (this.groupIdsBySelectProduct.has(item)) {
          this.groupIdsBySelectProduct.delete(item);
        }
      });
    }
  }

  getValidGroupIdsRemove(itemId: number) {
    const dataGroupIDs = this.selectedGroupMap.get(itemId);
    if (dataGroupIDs && dataGroupIDs.length > 0) {
      this.selectedGroupMap.forEach((value, key) => {
        if (key != itemId) {
          value.forEach(id => {
            if (dataGroupIDs.includes(id)) {
              const index = dataGroupIDs.indexOf(id);
              if (index != -1) {
                dataGroupIDs.splice(index, 1);
              }
            }
          });
        }
      });
    }
    this.selectedGroupMap.delete(itemId);
    return dataGroupIDs;
  }

  removeGetProdGroupItem(indexReq: number, indexCondition: number) {
    const condition = this.conditions[indexCondition];
    const selectedListView = condition.conditionGetType === 1 ? condition.getProductProductUnitIdView : condition.getProductGroupIdView;
    const selectedList = condition.conditionGetType === 1 ? condition.getProductProductUnitId : condition.getProductGroupId;

    const item = selectedList[indexReq];
    if (item) {
      selectedList.splice(indexReq, 1);
      selectedListView.splice(indexReq, 1);
    }
    condition.conditionGetContent = this.convertContentCondition(condition.getProductProductUnitId, condition.getProductGroupId);
    event?.stopPropagation();
  }

  removeBuyProdGroupItem(indexReq: number, indexCondition: number) {
    const condition = this.conditions[indexCondition];
    const check = condition.conditionBuyType === 1;
    const selectedListView = check ? condition.buyProductProductUnitIdView : condition.buyProductGroupIdView;
    const selectedList = check ? condition.buyProductProductUnitId : condition.buyProductGroupId;

    const itemId = selectedList[indexReq];
    if (itemId) {
      selectedList.splice(indexReq, 1);
      selectedListView.splice(indexReq, 1);
      if (check) {
        const data = this.getValidGroupIdsRemove(itemId);
        this.removeProdUnitId(true, data);
      } else {
        const data = this.getValidProdIdsRemove(itemId);
        this.removeProdId(true, data);
      }
    }
    condition.conditionBuyContent = this.convertContentCondition(condition.buyProductProductUnitId, condition.buyProductGroupId);
    event?.stopPropagation();
  }

  onResetDataGetProd(recallApi: boolean) {
    this.viewGetProd = false;
    this.viewBuyProd = false;
    this.idViewGetProd = null;
    this.idViewGetProd = null;
    this.getProdGroupKeyword = '';
    this.getProdKeyword = '';
    this.filterProduct.keyword = '';
    this.filterProductGroup.keyword = '';
    this.filterProductGroup.page = 0;
    // this.getProductGroups = [];
    this.filterProduct.page = 0;
    // this.getProducts = [];
    if (recallApi) {
      this.getProductGroups = [];
      this.getProductGroups = [];
      this.getListGetProductGroup();
      this.getListGetProduct();
    }
  }

  onClearDataCondition(indexCondition: number, itemType: number) {
    const condition = this.conditions[indexCondition];
    if (itemType === 0) {
      if (condition.conditionBuyType === 1) {
        condition.buyProductProductUnitId.forEach(id => {
          const data = this.getValidGroupIdsRemove(id);
          this.removeProdUnitId(true, data);
        });
        condition.buyProductProductUnitId = [];
        condition.buyProductProductUnitIdView = [];
      } else {
        condition.buyProductGroupId.forEach(id => {
          const data = this.getValidProdIdsRemove(id);
          this.removeProdId(true, data);
        });
        condition.buyProductGroupId = [];
        condition.buyProductGroupIdView = [];
      }
    } else {
      if (condition.conditionGetType === 1) {
        condition.getProductProductUnitIdView = [];
        condition.getProductProductUnitId = [];
      } else {
        condition.getProductGroupIdView = [];
        condition.getProductGroupId = [];
      }
    }
  }

  datesSelected: NgbDateStruct[] = [];
  visibleIgnoreDays = false;

  change(value: NgbDateStruct[]) {
    this.datesSelected = value;
    let ignoreDaysConvert: any[] = [];
    this.datesSelected.forEach(item => {
      ignoreDaysConvert.push(dayjs(new Date(item.year, item.month - 1, item.day)).format('DD/MM/YYYY'));
    });
    this.ignoreDaysSave = ignoreDaysConvert;
    this.ignoreDaysSave = this.sortDayJsString(this.ignoreDaysSave);
  }

  toggleDatePicker(event) {
    event.stopPropagation();
    this.visibleIgnoreDays = !this.visibleIgnoreDays;
  }

  indexUnSelected: number[] = [];
  itemUnSelected: NgbDateStruct[] = [];
  requestUnSelected: {
    indexUnSelected: number[];
    itemUnSelected: NgbDateStruct[];
    itemDisabled: NgbDateStruct[];
    onlyMonths: number[];
  };

  onRemoveIgnoreDate(event, item: any, index: number) {
    this.itemUnSelected = [this.parseDate(item)];
    this.requestUnSelected = {
      indexUnSelected: [],
      itemUnSelected: this.itemUnSelected,
      itemDisabled: [],
      onlyMonths: [],
    };
    this.ignoreDaysSave.splice(index, 1);
    event?.stopPropagation();
  }

  unSelectedDate(event: boolean) {
    if (event) {
      this.indexUnSelected = [];
      this.itemUnSelected = [];
      this.requestUnSelected = {
        indexUnSelected: [],
        itemUnSelected: [],
        itemDisabled: [],
        onlyMonths: [],
      };
      this.cdref.detectChanges();
    }
  }

  clearAllIgnoreDate() {
    if (this.ignoreDaysSave) {
      let allIndex: number[] = [];
      for (let i = 0; i < this.ignoreDaysSave.length; i++) {
        allIndex.push(i);
      }
      this.indexUnSelected = allIndex;
      this.requestUnSelected = {
        indexUnSelected: this.indexUnSelected,
        itemUnSelected: [],
        itemDisabled: [],
        onlyMonths: [],
      };
      this.ignoreDaysSave = [];
    }
  }

  private parseDate(dateString: string): any {
    const parts = dateString.split('/');
    if (parts.length === 3) {
      return {
        year: parseInt(parts[2], 10),
        month: parseInt(parts[1], 10),
        day: parseInt(parts[0], 10),
      };
    }
  }

  private sortDayJsString(values: any) {
    const dayjsArray = values.map(dateString => dayjs(dateString, 'DD/MM/YYYY'));
    const sortedDayjsArray = dayjsArray.sort((a, b) => {
      if (a.isBefore(b)) return -1;
      if (a.isAfter(b)) return 1;
      return 0;
    });

    return sortedDayjsArray.map(item => dayjs(item).format('DD/MM/YYYY'));
  }

  checkLoadAllSuccess() {
    if (
      this.promotion &&
      (!this.conditionType ||
        (this.conditionType &&
          (this.conditionType === 100 ||
            ([102, 200].includes(this.conditionType) &&
              ((this.prodResultMap && this.prodResultMap.size > 0) || (this.groupResultMap && this.groupResultMap.size > 0))))))
    ) {
      this.isLoadingSuccess = true;
    } else {
      this.isLoadingSuccess = false;
    }
  }

  onSaveDiscountItem(index: number, type: number) {
    let item = this.conditions[index];
    if (type === 0) {
      item.buyProductProductUnitId = JSON.parse(JSON.stringify(item.buyProductProductUnitIdView));
      item.buyProductGroupId = JSON.parse(JSON.stringify(item.buyProductGroupIdView));
    } else {
      item.getProductProductUnitId = JSON.parse(JSON.stringify(item.getProductProductUnitIdView));
      item.getProductGroupId = JSON.parse(JSON.stringify(item.getProductGroupIdView));
    }
  }
}
