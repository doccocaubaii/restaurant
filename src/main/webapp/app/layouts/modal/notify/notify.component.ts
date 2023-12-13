import { Component, OnDestroy, AfterViewInit, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from '../../../shared/base/base.component';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { InvoiceService } from '../../../pages/invoice/service/invoice.service';
import { ConfigService } from '../../config/service/config.service';
import { last_company, last_owner_device } from '../../../object-stores.constants';
import { OwnerDevice } from '../../../entities/ownerDevice/ownerDevice.model';

@Component({
  selector: 'jhi-invoice-configuration',
  templateUrl: './notify.component.html',
  styleUrls: ['./notify.component.scss'],
})
export class NotifyComponent extends BaseComponent implements OnInit, OnDestroy {
  @Input()
  notifies: any[];
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

  async ngOnInit() {}

  closeModal() {
    this.activeModal.close();
  }
}
