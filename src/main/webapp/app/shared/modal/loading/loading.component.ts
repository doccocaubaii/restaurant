import { Component, OnInit } from '@angular/core';
import { EventManager } from '../../../core/util/event-manager.service';
import { Principal } from '../../../core/auth/principal.service';

@Component({
  selector: 'eb-loading',
  templateUrl: './loading.component.html',
  styleUrls: ['./loading.css'],
})
export class LoadingComponent implements OnInit {
  constructor(private eventManager: EventManager, private principal: Principal) {}

  ngOnInit(): void {}
}
