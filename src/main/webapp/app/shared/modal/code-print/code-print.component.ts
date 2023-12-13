import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Component, OnInit } from '@angular/core';
import { IUser } from 'app/entities/user/user.model';
import { CodePrint } from 'app/shared/modal/code-print/code-print.model';
import { Principal } from '../../../core/auth/principal.service';
import { EventManager } from '../../../core/util/event-manager.service';
@Component({
  selector: 'eb-code-print',
  templateUrl: 'code-print.component.html',
  styleUrls: ['code-print.component.css'],
})
export class CodePrintComponent implements OnInit {
  data: CodePrint;
  modalData: any[];
  strCodePrint?: any;

  constructor(
    public activeModal: NgbActiveModal,
    private principal: Principal,
    private toastr: ToastrService,
    public translate: TranslateService,
    public eventManager: EventManager
  ) {}

  ngOnInit(): void {
    this.principal.identity().then(account => {});
  }

  trackId(index: number, item: IUser) {
    return item.id;
  }

  copyCodePrint() {
    const selBox = document.createElement('textarea');
    selBox.style.position = 'fixed';
    selBox.style.left = '0';
    selBox.style.top = '0';
    selBox.style.opacity = '0';
    selBox.value = this.strCodePrint;
    document.body.appendChild(selBox);
    selBox.focus();
    selBox.select();
    document.execCommand('copy');
    document.body.removeChild(selBox);
    this.toastr.success('Đã sao chép mã in!');
  }
}
