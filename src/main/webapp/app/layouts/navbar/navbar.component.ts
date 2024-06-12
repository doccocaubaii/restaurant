import { Component, EventEmitter, Input, OnInit, Output, Renderer2 } from '@angular/core';

import appSettings from '../../config/app-settings';
import { Router } from '@angular/router';
import { UtilsService } from '../../utils/Utils.service';
import { LoginService } from '../../pages/login/login.service';
import { SidebarOption } from '../../utils/SidebarOption';
import { BaseComponent } from '../../shared/base/base.component';
import { last_user } from '../../object-stores.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent extends BaseComponent implements OnInit {
  @Input() appSidebarTwo;
  @Output() appSidebarEndToggled = new EventEmitter<boolean>();
  @Output() appSidebarMobileToggled = new EventEmitter<boolean>();
  @Output() appSidebarEndMobileToggled = new EventEmitter<boolean>();
  @Output() appSidebarNone = new EventEmitter<boolean>();
  appSettings = appSettings;
  lastUser: any = {
    fullname: 'Khách lẻ'
  };
  lastCompany: any;
  languages = LANGUAGES;

  constructor(
    private renderer: Renderer2,
    private utilsService: UtilsService,
    public sidebarOption: SidebarOption,
    private router: Router,
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private loginService: LoginService
  ) {
    super();
    this.lastUser = {};
  }

  toggleAppSidebarMobile() {
    this.appSidebarMobileToggled.emit(true);
  }

  toggleAppSidebarEnd() {
    this.appSidebarEndToggled.emit(true);
  }

  toggleAppSidebarEndMobile() {
    this.appSidebarEndMobileToggled.emit(true);
  }

  toggleAppSidebarNone() {
    this.appSidebarNone.emit(false);
    this.sidebarOption.isShow = !this.sidebarOption.isShow;
  }

  toggleAppTopMenuMobile() {
    this.appSettings.appTopMenuMobileToggled = !this.appSettings.appTopMenuMobileToggled;
  }

  toggleAppHeaderMegaMenuMobile() {
    this.appSettings.appHeaderMegaMenuMobileToggled = !this.appSettings.appHeaderMegaMenuMobileToggled;
  }

  ngOnDestroy() {
    this.appSettings.appTopMenuMobileToggled = false;
    this.appSettings.appHeaderMegaMenuMobileToggled = false;
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    this.lastUser = await this.getFirstItemIndexDB(last_user);
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }

  getLink(path: string, pathOffline?: string) {
    if (this.utilsService.isOnline()) {
      this.router.navigate([path]);
    } else if (pathOffline) {
      this.router.navigate([pathOffline]);
    }
  }

  changeLanguage(languageKey: string): void {
    this.sessionStorageService.store('locale', languageKey);
    this.translateService.use(languageKey);
  }
}
