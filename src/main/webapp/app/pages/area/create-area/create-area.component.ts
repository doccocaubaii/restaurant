import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { createNewArea, DArea, IArea, IAreaDto } from '../../../entities/area/area.model';
import { AreaService } from '../area.service';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from '../../../shared/base/base.component';
import { LoadingOption } from '../../../utils/loadingOption';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-create-area',
  templateUrl: './create-area.component.html',
  styleUrls: ['./create-area.component.scss'],
})
export class CreateAreaComponent extends BaseComponent implements OnInit {
  constructor(
    public activeModal: NgbActiveModal,
    private areaService: AreaService,
    public loading: LoadingOption,
    private toastr: ToastrService
  ) {
    super();
  }
  company: any;
  companyId: number;
  area: IArea = createNewArea();
  async ngOnInit() {
    this.company = await this.getCompany();
    this.companyId = this.company.id;
  }
  close(type: string) {
    //gọi api
    if (type == 'add') {
      this.areaService.postArea(new DArea(this.companyId, this.area.name)).subscribe(response => {
        if (response.status) {
          this.toastr.success(response.message[0].message);
          this.activeModal.close();
        }
      });
    } else {
      this.areaService.putArea(this.area.id, this.area.comId, this.area.name).subscribe(response => {
        if (response.status) {
          this.toastr.success(response.message[0].message);
          this.activeModal.close();
        }
      });
    }
  }
  dismiss() {
    this.activeModal.dismiss();
  }
  protected readonly alert = alert;
  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
