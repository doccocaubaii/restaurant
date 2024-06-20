import { AfterViewInit, Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { StaffService } from '../staff/staff.service';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { Observable } from 'rxjs';
import { createRequestOption } from '../../core/request/request-util';
import { OrderResponse } from '../pos/model/orderResponse.model';
import { ACTIVE_ACCOUNT, GET_LIST_STAFF, SEND_EMAIL } from '../../constants/api.constants';

@Component({
  selector: 'jhi-email-active',
  templateUrl: './email-active.component.html',
  styleUrls: ['./email-active.component.scss']
})
export class EmailActiveComponent implements OnInit , AfterViewInit{
  email : string;
  id : any;
  otp: string;
  constructor(public activeModal: NgbActiveModal,
              private toastr: ToastrService,
              private http: HttpClient,
              protected applicationConfigService: ApplicationConfigService
  ) { }

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');

  ngOnInit(): void {

  }

  ngAfterViewInit(): void {
    if (this.id) this.sendEmail({"id":this.id}).subscribe();
  }



  dismiss(value: any) {
    this.activeModal.dismiss(value);
  }
  onSave() {
    this.activeEmail({"otp":this.otp, "id":this.id}).subscribe(value => {
      if (value.body.status)
      {
        this.toastr.success("Kích hoạt tài khoản thành công ");
        this.activeModal.close();
      } else {
        this.toastr.error("Kích hoạt tài khoản thất bại");
      }
    });
  }

  activeEmail(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get<any>(`${this.resourceUrl}${ACTIVE_ACCOUNT}`, {
      params: options,
      observe: 'response'
    });
  }

  sendEmail(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get<any>(`${this.resourceUrl}${SEND_EMAIL}`, {
      params: options,
      observe: 'response'
    });
  }
}
