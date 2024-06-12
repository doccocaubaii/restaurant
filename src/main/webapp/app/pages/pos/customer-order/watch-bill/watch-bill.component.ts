import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-watch-bill',
  templateUrl: './watch-bill.component.html',
  styleUrls: ['./watch-bill.component.scss']
})
export class WatchBillComponent implements OnInit {
  @Input() orderSelected?: any = {};

  constructor(private activeModal: NgbActiveModal) {
  }

  ngOnInit(): void {
  }

  decline() {
    this.activeModal.dismiss();
  }
}
