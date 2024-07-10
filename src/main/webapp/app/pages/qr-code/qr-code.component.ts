import { Component, OnInit } from '@angular/core';
import * as QRCode from 'qrcode';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'jhi-qr-code',
  templateUrl: './qr-code.component.html',
  styleUrls: ['./qr-code.component.scss']
})

export class QrCodeComponent implements OnInit {

  urlQR: any;
  model1: any;
  tableId: number = 1;

  constructor(private toast: ToastrService) {
  }

  ngOnInit(): void {
  }



  genQrCode() {

    if (this.tableId < 0 || !this.tableId) {
      this.toast.error('Số bàn không hợp lệ');
      return;
    }
    let currentUrl = document.location.protocol + "//" + document.location.hostname + ":" + document.location.port;
    console.log(currentUrl);
      let value = currentUrl + '/pos/ban-hang/1/' + this.tableId;
    if (value) {
      QRCode.toDataURL(value, (err, url) => {
        if (err) {
          console.log(err);
          return;
        }
        this.urlQR = url;
        // In ra URL của mã QR (có thể sử dụng để hiển thị hoặc tải xuống)
      });
    }
  }

}
