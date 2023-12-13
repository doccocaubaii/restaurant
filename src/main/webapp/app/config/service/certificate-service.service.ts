import { Injectable } from '@angular/core';
import * as x509 from '@peculiar/x509';
import { from, interval, Observable, ReplaySubject } from 'rxjs';
import { map } from 'rxjs/operators';
import { ClientCertificateModel } from '../client-certificate.model';
import { NCCDV_EINVOICE } from '../../constants/invoice.constants';

declare const hwcrypto: any;

@Injectable({
  providedIn: 'root',
})
export class CertificateService {
  // private readonly clientCertificate = new ReplaySubject<ClientCertificate>(1);
  // clientCertificate$ = this.clientCertificate.asObservable();

  private readonly connectingSigningTool$ = new ReplaySubject<boolean>();
  public connectingSigningTool = this.connectingSigningTool$.asObservable();
  certificate: any;
  certificateSerial: any;

  constructor() {}

  tryConnectSigningTool() {
    hwcrypto.initWs();
    let numbers = interval(100);
    // subscribe function returns reference to observable
    let numberSubs = numbers.subscribe(x => {
      const isConnected = this.hasSigningTool();
      if (isConnected || x === 5) {
        numberSubs.unsubscribe();
        this.connectingSigningTool$.next(isConnected);
      }
    });
  }

  selectCertificate(): Observable<ClientCertificateModel> {
    return from(hwcrypto.selectCertSerial({ lang: 'en' })).pipe(
      map((m: any) => {
        const base64Cert = m.value.cert;
        const cert = new x509.X509Certificate(base64Cert);
        return <ClientCertificateModel>{
          base64Cert: base64Cert,
          serial: cert.serialNumber,
          owner: cert.subject,
          detail: cert.issuer,
        };
      })
    );
  }

  signHashData(base64Cert: string, options: any, lang: any) {
    return hwcrypto.signHashData(base64Cert, options, lang);
  }

  hasSigningTool(): boolean {
    return hwcrypto.hasSigningTool();
  }
  loadPluginFor = () => {
    return new Promise((resolve, reject) => {
      if (this.hasSigningTool()) {
        this.selectCertificate().subscribe(
          response => {
            this.certificate = response.base64Cert;
            this.certificateSerial = response.serial;
            resolve(response);
          },
          error => {
            // Plugin không đọc được dữ liệu
            // thông báo lỗi
            reject(null);
          }
        );
      } else {
        reject(null);
        // this.opendPopupGuide();
      }
    });
  };
}
