import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DUnit, InfoArea } from '../../../../entities/area/area-unit.model';
import { ToastrService } from 'ngx-toastr';
import { UnitService } from '../unit.service';
import { createNewArea, IArea } from '../../../../entities/area/area.model';
import { LoadingOption } from '../../../../utils/loadingOption';
import {ICON_CANCEL, ICON_SAVE} from "../../../../shared/other/icon";

@Component({
  selector: 'jhi-create-unit',
  templateUrl: './create-unit.component.html',
  styleUrls: ['./create-unit.component.scss'],
})
export class CreateUnitComponent implements OnInit {
  name: string = '';
  parent: InfoArea[] = [];
  area: IArea = createNewArea();
  constructor(
    public activeModal: NgbActiveModal,
    public loading: LoadingOption,
    private toastr: ToastrService,
    private unitService: UnitService
  ) {}

  ngOnInit(): void {}

  close(result: any) {
    this.unitService.postUnit(new DUnit(this.area.comId, this.area.id, result)).subscribe(response => {
      if (response.status) {
        this.toastr.success(response.message[0].message);
        this.activeModal.close(result);
      }
    });
  }

  dismiss() {
    this.activeModal.dismiss();
  }

  protected readonly alert = alert;
    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
