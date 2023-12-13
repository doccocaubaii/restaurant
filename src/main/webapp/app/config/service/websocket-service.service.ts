// websocket.service.ts
import { Injectable } from '@angular/core';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WebsocketService {
  statusConnectingWebSocket = new BehaviorSubject<any>(null);
  statusConnectingWebSocket$ = this.statusConnectingWebSocket.asObservable();
  private socket$: WebSocketSubject<any>;

  constructor(private toastr: ToastrService, private translateService: TranslateService) {
    this.socket$ = webSocket('ws://127.0.0.1:8189'); // Replace with your WebSocket server URL
  }

  connect() {
    this.socket$.subscribe(
      msg => {
        console.log('Received:', msg);
        this.statusConnectingWebSocket.next(true);
      },
      err => {
        this.statusConnectingWebSocket.next(false);
      },
      () => console.log('WebSocket connection closed')
    );
  }

  sendMessage(message: any) {
    this.connect();
    this.socket$.next(message);
  }
}
