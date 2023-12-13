import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { map } from 'rxjs';
import { CREATE_AREA, GET_ALL_LIST_AREA, GET_AREA_BY_ID, UPDATE_AREA } from 'app/constants/api.constants';
import { Area } from '../model/area.model';
import { FilterArea } from '../model/filterArea.model';
import html2canvas from 'html2canvas';
import html2pdf from 'html2pdf.js';
import domtoimage from 'dom-to-image';
import { jsPDF } from 'jspdf';

@Injectable({
  providedIn: 'root',
})
export class PrintService {
  public print(printEl: any) {
    let elementCopy = printEl.cloneNode(true);
    // document.body.remove();
    document.body.appendChild(elementCopy);

    window.print();
  }

  public printContent(printEl: any) {
    let printEl1 = document.createElement('div');
    printEl1.innerHTML = printEl;
    document.body.appendChild(printEl1);

    window.print();
  }

  public printEl(printEl: any) {
    document.body.appendChild(printEl);

    window.print();
  }
}
