import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { LoadingBarService } from '@ngx-loading-bar/core';
import { BaseComponent } from '../../shared/base/base.component';
import { last_user } from '../../object-stores.constants';
import { Router } from '@angular/router';
import { HOME } from '../../constants/app.routing.constants';

@Component({
  selector: 'jhi-security-policy',
  templateUrl: './security-policy.component.html',
  styleUrls: ['./security-policy.component.scss'],
})
export class SecurityPolicyComponent extends BaseComponent implements OnInit {
  securityPolicyFileUrl = '/content/file/security-policy.pdf';
  sanitizedUrl: any;
  lastUser: any;

  constructor(private sanitizer: DomSanitizer, private loadingBar: LoadingBarService, private router: Router) {
    super();
  }

  async ngOnInit() {
    this.lastUser = await this.getFirstItemIndexDB(last_user);
    if (this.lastUser && this.lastUser.username && this.lastUser.username === 'demo') {
      await this.loadPdfAndConvertToBase64();
    } else {
      this.router.navigate([HOME]);
    }
  }

  async loadPdfAndConvertToBase64() {
    this.loadingBar.start();
    fetch(this.securityPolicyFileUrl)
      .then(response => response.blob())
      .then(blob => {
        return new Promise((resolve, _) => {
          const reader = new FileReader();
          reader.onloadend = () => resolve(reader.result);
          reader.readAsDataURL(blob);
        });
      })
      .then((data: any) => {
        this.sanitizedUrl = this.sanitizer.bypassSecurityTrustResourceUrl(data.toString());
        this.loadingBar.complete();
      });
  }
}
