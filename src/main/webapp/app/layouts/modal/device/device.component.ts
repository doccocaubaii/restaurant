import { Component, OnDestroy, AfterViewInit, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from '../../../shared/base/base.component';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { InvoiceService } from '../../../pages/invoice/service/invoice.service';
import { ConfigService } from '../../config/service/config.service';
import { last_company, last_owner_device } from '../../../object-stores.constants';
import { OwnerDevice } from '../../../entities/ownerDevice/ownerDevice.model';
import { DataEncrypt } from '../../../entities/indexDatabase/data-encrypt.model';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-invoice-configuration',
  templateUrl: './device.component.html',
  styleUrls: ['./device.component.scss'],
})
export class DeviceComponent extends BaseComponent implements OnInit, OnDestroy {
  deviceName: any;
  deviceInfo: any;
  lastCompany: any;
  available = false;
  devices: OwnerDevice[];
  @Output() deviceChangeEvent: EventEmitter<string> = new EventEmitter<string>();

  ngOnDestroy(): void {}

  constructor(
    private modalService: NgbModal,
    public activeModal: NgbActiveModal,
    private configService: ConfigService,
    private toast: ToastrService
  ) {
    super();
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    this.devices = await this.findByID(last_owner_device, this.lastCompany.ownerId);
    if (!this.devices) {
      this.available = false;
    } else {
      this.available = true;
      this.getDeviceInfoDetail();
    }
  }

  closeModal() {
    this.activeModal.close();
  }

  saveDevice() {
    if (!this.deviceName) {
      this.toast.error('Tên thiết bị không được để trống', 'Thông báo');
      return;
    }
    this.configService
      .createDevice({
        taxCode: this.lastCompany.taxcode,
        name: this.deviceName,
      })
      .subscribe(async res => {
        if (res.status) {
          this.deviceInfo = res.data;
          this.deviceName = this.deviceInfo.name;
          this.lastCompany.deviceCode = this.deviceInfo.deviceCode;
          this.lastCompany.deviceName = this.deviceInfo.name;
          const dataEncryptCompany = this.genDataEncrypt(this.lastCompany, false);
          await this.updateById(last_company, this.lastCompany.id, dataEncryptCompany);
          const dataEncryptOwnerDevice = this.genDataEncrypt(
            new OwnerDevice(this.deviceName, this.lastCompany.ownerId, this.deviceInfo.deviceCode),
            true
          );
          if (this.available) {
            await this.updateById(last_owner_device, this.lastCompany.ownerId, dataEncryptOwnerDevice);
          } else {
            await this.addItem(last_owner_device, dataEncryptOwnerDevice);
          }
          this.activeModal.close();
          if (res.message[0].code && res.message[0].code === 'DEVICE_CODE_ALREADY') {
            this.toast.success('Tên thiết bị đã được đăng kí trước đó, mã thiết bị là ' + this.deviceInfo.deviceCode);
          } else {
            this.toast.success('Tạo mới tên thiết bị thành công, mã thiết bị của bạn là ' + this.deviceInfo.deviceCode);
          }
          this.deviceChangeEvent.emit(this.deviceInfo.deviceCode);
        }
      });
  }

  genDataEncrypt(value: any, isOwner: boolean) {
    if (value) {
      if (isOwner) {
        return { ownerId: value.ownerId, data: this.encryptFromData(value) };
      }
      return new DataEncrypt(value.id, this.encryptFromData(value));
    }
    return null;
  }

  getDeviceInfoDetail() {
    this.deviceInfo = this.devices;
    this.deviceInfo.taxCode = this.lastCompany.taxcode;
    this.deviceName = this.deviceInfo.name;
  }

    protected readonly ICON_SAVE = ICON_SAVE;
  protected readonly ICON_CANCEL = ICON_CANCEL;
}
