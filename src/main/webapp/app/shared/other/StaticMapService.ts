import { EventEmitter, Injectable } from '@angular/core';
import { ConfigService } from '../../layouts/config/service/config.service';
import { AuthServerProvider } from '../../core/auth/auth-jwt.service';

@Injectable({
  providedIn: 'root', // Đăng ký service tại root level
})
export class StaticMapService {
  private myStaticMap: Map<string, any> = new Map<string, any>();
  eventEmitter = new EventEmitter<string>();
  constructor(private configService: ConfigService, private authService: AuthServerProvider) {
    this.reload();
  }

  reload() {
    this.myStaticMap.clear();
    var jsonObject;
    if (!this.authService.isJwtExpired()) {
      this.configService.getService().subscribe(res => {
        if (res.status) {
          jsonObject = JSON.parse(res.data.value);
          for (const item of jsonObject) {
            this.myStaticMap.set(item.code, item);
          }
          this.eventEmitter.emit();
        }
      });
    }
  }

  public getMap(): Map<string, any> {
    return this.myStaticMap;
  }

  public setMap(newMap: Map<string, any>) {
    this.myStaticMap = newMap;
  }
}
