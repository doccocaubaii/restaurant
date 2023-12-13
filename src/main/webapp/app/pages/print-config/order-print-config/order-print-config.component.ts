import {Component, forwardRef, OnInit, Pipe, PipeTransform, ViewEncapsulation, EventEmitter, Output} from '@angular/core';
import {OrderPrintConfig} from './order-print-config';
import {NG_VALUE_ACCESSOR} from '@angular/forms';
import {product} from "../../../object-stores.constants";
import {equals} from "@ngx-translate/core/lib/util";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {OrderPrintConfigService} from "./order-print-config.service";

@Component({
  selector: 'jhi-order-print-config',
  templateUrl: './order-print-config.component.html',
  styleUrls: ['./order-print-config.component.scss'],
  // encapsulation: ViewEncapsulation.None,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => OrderPrintConfigComponent),
      multi: true,
    },
  ],
})
export class OrderPrintConfigComponent implements OnInit{
  @Output() contentChange = new EventEmitter<string>();
  private onChange: (value: string) => void;
  private onTouched: () => void;
  // public Editor : any = ClassicEditor;
  public dataPrintConfig: OrderPrintConfig = new OrderPrintConfig();
  content2 = "";
  content : any = '';

  qrCode = "data:image/png;base64,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";

  style = `<style>
          .cke_show_borders table.cke_show_border,
          .cke_show_borders table.cke_show_border > thead > tr > th,
          .cke_show_borders table.cke_show_border > tbody > tr > td {
            border: none !important;
          }
          p {
            margin: 0 !important;
            font-family:Times New Roman,Times,serif;
          }
          table thead {
            background-color: #FFFFFF !important;
          }
          table th:first-child {
             padding: 0px!important;

          }
          span {
            font-family:Times New Roman,Times,serif;
          }
          hr {
          margin: 0px !important;
          }
          strong {
            font-family:Times New Roman,Times,serif;
          }
        </style>`;
  constructor(
    private service: OrderPrintConfigService,
  ) {
  }
  ngOnInit(): void {
    this.dataPrintConfig = {
      company: 'Công ty cổ phần đầu tư và công nghệ SoftDreams',
      hotline: '1900 3369',
      local: '8 Phạm Hùng, Mễ Trì, Nam Từ Liêm, Hà Nội',
      process: 'CHẾ BIẾN',
      no: 'DH358',
      dateTime: '14:23 19/05/2023',
      taxCode: 'M1-23-NRCE6-00184480974',
      customer: 'Khách lẻ',
      provisonal: '50,000,000',
      discount: "0",
      totalAmount: "50,000,000",
      totalTax: "0",
      totalPreTax: "0",
      customerMustPay:"50,000,000",
      customerPayment: "50,000,000",
      extraMoney: "0",
      pamentMethod: "TIỀN MẶT",
      searchCode: "M1-23-NRCE6-00184480974",
      url: "0123456789hd.softdreams.vn",
      description: "mt",
      listProduct: [
        {
          productName:"Test 1",
          productSale: "100,000,000",
          quantity: "100,000,000",
          amount: "100,000,000"
        },
        {
          productName:"Test 2",
          productSale: "100,000,000",
          quantity: "100,000,000",
          amount: "100,000,000"
        },
        {
          productName:"Test 3",
          productSale: "100,000,000",
          quantity: "100,000,000",
          amount: "100,000,000"
        }
      ]
    };
    this.service.getAllPrintConfig().subscribe(value => {
      this.content = value.data[2].content;
      this.updateContent();
    });
    this.ckEditorConfig.extraPlugins = 'divarea,justify,font';
  }

  ckEditorConfig: any = {
    toolbar: [
      ['Source', 'Templates', 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'CopyFormatting', 'RemoveFormat'],
      ['Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo'],
      ['Find', 'Replace', '-', 'SelectAll', '-', 'Scayt'],
      [
        'NumberedList',
        'BulletedList',
        '-',
        'Outdent',
        'Indent',
        '-',
        'Blockquote',
        'CreateDiv',
        '-',
        'JustifyLeft',
        'JustifyCenter',
        'JustifyRight',
        'JustifyBlock',
        '-',
        'BidiLtr',
        'BidiRtl',
      ],
      ['Link', 'Unlink', 'Anchor'],
      ['Image', 'Table', 'HorizontalRule', 'Smiley', 'SpecialChar', 'PageBreak', 'Iframe'],
      ['Styles', 'Format', 'Font', 'FontSize'],
      ['TextColor', 'BGColor'],
      ['Maximize', 'ShowBlocks'],
    ],
  };

  private replaceVariables(content: string, variable: string, value: string): string {
    return content.replace('{' + variable + '}', value);
  }

  public updateContent(): void {
    this.content = this.style + this.content;
    this.content2 = this.content;
    this.content2 = this.replaceVariables(this.content2, 'Ten_cua_hang', this.dataPrintConfig.company);
    this.content2 = this.replaceVariables(this.content2, 'So_dien_thoai', this.dataPrintConfig.hotline);
    this.content2 = this.replaceVariables(this.content2, 'Dia_chi_cua_hang', this.dataPrintConfig.local);
    this.content2 = this.replaceVariables(this.content2, 'Tieu_de', this.dataPrintConfig.process);
    this.content2 = this.replaceVariables(this.content2, 'Ma_don', this.dataPrintConfig.no);
    this.content2 = this.replaceVariables(this.content2, 'Thoi_gian_in', this.dataPrintConfig.dateTime);
    this.content2 = this.replaceVariables(this.content2, 'Ma_co_quan_thue', this.dataPrintConfig.taxCode);
    this.content2 = this.replaceVariables(this.content2, 'Ten_khach_hang', this.dataPrintConfig.customer);
    this.content2 = this.replaceVariables(this.content2, 'Tam_tinh', this.dataPrintConfig.provisonal);
    this.content2 = this.replaceVariables(this.content2, 'Giam_gia', this.dataPrintConfig.discount);
    this.content2 = this.replaceVariables(this.content2, 'Tong_cong', this.dataPrintConfig.totalAmount);
    this.content2 = this.replaceVariables(this.content2, 'Tong_tien_thue', this.dataPrintConfig.totalTax);
    this.content2 = this.replaceVariables(this.content2, 'Giam_tru_thue', this.dataPrintConfig.totalPreTax);
    this.content2 = this.replaceVariables(this.content2, 'Khach_phai_tra', this.dataPrintConfig.customerMustPay);
    this.content2 = this.replaceVariables(this.content2, 'Khach_tra', this.dataPrintConfig.customerPayment);
    this.content2 = this.replaceVariables(this.content2, 'Tien_thua', this.dataPrintConfig.extraMoney);
    this.content2 = this.replaceVariables(this.content2, 'Phuong_thuc_thanh_toan', this.dataPrintConfig.pamentMethod);
    this.content2 = this.replaceVariables(this.content2, 'Ma_tra_cuu', this.dataPrintConfig.searchCode);
    this.content2 = this.replaceVariables(this.content2, 'Link_tra_cuu', this.dataPrintConfig.url);
    this.content2 = this.replaceVariables(this.content2, 'Mo_ta', this.dataPrintConfig.description);
    this.content2 = this.replaceVariables(this.content2, 'qrCode', this.qrCode);

    let startProductName = this.content.indexOf('{Ten_san_pham}');
    let startAmount = this.content.indexOf('{Thanh_tien}', startProductName);

    let start1 = this.content.lastIndexOf('<tr>', startProductName);
    let end2 = this.content.indexOf('</tr>', startAmount) + '</tr>'.length;

    let listProduct = this.content.slice(start1, end2);
    let rawText = this.content.slice(start1, end2);
    this.dataPrintConfig.listProduct.forEach((value,index )=> {
      listProduct = this.replaceVariables(listProduct, 'Ten_san_pham', value.productName);
      listProduct = this.replaceVariables(listProduct, 'Gia_san_pham', value.productSale);
      listProduct = this.replaceVariables(listProduct, 'So_luong', value.quantity);
      listProduct = this.replaceVariables(listProduct, 'Thanh_tien', value.amount);

      if (index < this.dataPrintConfig.listProduct.length -1) {
        listProduct += rawText;
      }
    });
    this.content2 = this.content2.replace(rawText, listProduct);
  }

  writeValue(value: any): void {
    this.updateContent();
  }

  registerOnChange(fn: (value: any) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }
  onEditorChange(value: string): void {
    this.content = value;
    if (this.onChange) this.onChange(value);
    if (this.onTouched) this.onTouched();
    this.updateContent();
  }

  // createPrintConfig() {
  //   this.service.createPrintConfig("ORDER_PRINT_CONFIG_V3", "OrderPrintConfigV3", this.style + this.content, "A1").subscribe(value => {
  //
  //   });
  // }
}
