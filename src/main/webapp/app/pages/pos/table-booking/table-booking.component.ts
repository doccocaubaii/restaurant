import { Component, OnDestroy } from '@angular/core';
import appSettings from '../../../config/app-settings';
import { RS_INOUT_WARD } from '../../../constants/app.routing.constants';

@Component({
  selector: 'table-booking',
  templateUrl: './table-booking.component.html',
})
export class PosTableBookingPage implements OnDestroy {
  appSettings = appSettings;
  time = '00:00';

  handleStartTime() {
    var today = new Date();
    var h = today.getHours();
    var m = today.getMinutes();
    var a = h > 12 ? h - 12 : h;
    var b = m < 10 ? '0' + m : m;
    var c = h > 11 ? 'pm' : 'am';

    this.time = a + ':' + b + c;
    setTimeout(this.handleStartTime, 500);
  }

  constructor() {
    this.appSettings.appEmpty = true;
    this.appSettings.appContentFullHeight = true;
    this.handleStartTime();
  }

  ngOnDestroy() {
    this.appSettings.appEmpty = false;
    this.appSettings.appContentFullHeight = false;
  }

  protected readonly RS_INOUT_WARD = RS_INOUT_WARD;
}
