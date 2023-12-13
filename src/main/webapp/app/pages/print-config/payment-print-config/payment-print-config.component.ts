import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-payment-print-config',
  templateUrl: './payment-print-config.component.html',
  styleUrls: ['./payment-print-config.component.scss']
})
export class PaymentPrintConfigComponent implements OnInit {

  content2 = "";
  public content = '';
  qrCode = "data:image/png;base64,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";

  dataPrintConfig: any = {};

  private onChange: (value: string) => void;
  private onTouched: () => void;

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
        </style>`;
  constructor() {
    this.content = `
      <p style="text-align:center"><span><span style="font-size:22px"><strong>Th&ocirc;ng tin đơn h&agrave;ng</strong></span></span></p>

      <p style="text-align:center"><span><strong>{Ten_cua_hang}</strong></span></p>

      <p style="text-align:center"><span>Hotline: {So_dien_thoai}</span></p>

      <p style="text-align:center"><span>{Dia_chi_cua_hang}</span></p>

      <p style="text-align:center"><span><strong>{Tieu_de}</strong></span></p>
      <div style="text-align: center;">
      <table style="margin: 0 auto;width: 100%;">
        <tbody>
        <tr>
          <td style="text-align: left; width: 50%;"><span><p>{Ma_don}</p></span></td>
          <td style="text-align: left; width: 50%;"><span><p>{Thoi_gian_in}</p></span></td>
        </tr>
        <tr style="border: none !important;">
          <td style="text-align: left; width: 50%;"><span><p>Mã cơ quan thuế</p></span></td>
          <td style="text-align: left; width: 50%;"><span style="font-family:Times New Roman,Times,serif"><p>{Ma_co_quan_thue}</p></span></td>
        </tr>
        <tr style="border: none !important;">
          <td style="text-align: left; width: 50%;"><span><p>Khu vực - Bàn</p></span></td>
          <td style="text-align: left; width: 50%;"><span style="font-family:Times New Roman,Times,serif"><p>{Khu_vuc}</p></span></td>
        </tr>
        <tr>
          <td style="text-align: left; width: 50%;"><span><p>Khách hàng</p></span></td>
          <td style="text-align: left; width: 50%;"><span><p>{Ten_khach_hang}</p></span></td>
        </tr>
        <tr>
          <td style="text-align: left; width: 50%;"><span><p>Mã số thuế</p></span></td>
          <td style="text-align: left; width: 50%;"><span><p>{Ma_so_thue}</p></span></td>
        </tr>
        <tr>
          <td style="text-align: left; width: 50%;"><span><p>CCCD</p></span></td>
          <td style="text-align: left; width: 50%;"><span><p>{cccd}</p></span></td>
        </tr>
        </tbody>
      </table>
      </div>
      <p style="text-align:center">&nbsp;</p>
      <div style="text-align: center;">
      <hr>
      <table style="width: 100%; margin: 0 auto;">
        <thead>
          <tr>
            <th style="text-align: left; width: 33.33%;"><span><strong>Sản phẩm</strong></span></th>
            <th style="text-align: center; width: 33.33%;"><span><strong>Số lượng</strong></span></th>
            <th style="text-align: right; width: 33.33%;"><span><strong>Thành tiền</strong></span></th>
          </tr>
        </thead>
      </table>
      <hr>
      <table style="margin: 0 auto;width: 100%;">
        <tbody>
           <tr>
              <td style="text-align: left;"><span><strong>{Ten_san_pham}</strong></span></td>
           </tr>
           <tr>
              <td style="text-align: left; width: 33.33%;"><span><p>{Gia_san_pham}</p></span></td>
              <td style="text-align: center; width: 33.33%;"><span><p>{So_luong}</p></span></td>
              <td style="text-align: right; width: 33.33%;"><span><p>{Thanh_tien}</p></span></td>
           </tr>
        </tbody>
      </table>
      <hr>
      <table style="margin: 0 auto;width: 100%;">
        <tbody>
          <tr>
            <td style="text-align: left; width: 33.33%;"><span><p>Tạm tính</p></span></td>
            <td style="text-align: center; width: 33.33%;"><span></span></td>
            <td style="text-align: right; width: 33.33%;"><span style="text-align: right;"><p>{Tam_tinh}</p></span></td>
          </tr>
          <tr>
            <td style="text-align: left; width: 33.33%;"><span><p>Giảm giá</p></span></td>
            <td style="text-align: center; width: 33.33%;"><span></span></td>
            <td style="text-align: right; width: 33.33%;"><span style="text-align: right;"><p>{Giam_gia}</p></span></td>
          </tr>
          <tr>
            <td style="text-align: left; width: 33.33%;"><span><p>Tổng cộng</p></span></td>
            <td style="text-align: center; width: 33.33%;"><span></span></td>
            <td style="text-align: right; width: 33.33%;"><span style="text-align: right;"><p>{Tong_cong}</p></span></td>
          </tr>
          <tr>
            <td style="text-align: left; width: 33.33%;"><span><p>Tổng tiền thuế</p></span></td>
            <td style="text-align: center; width: 33.33%;"><span></span></td>
            <td style="text-align: right; width: 33.33%;"><span style="text-align: right;"><p>{Tong_tien_thue}</p></span></td>
          </tr>
          <tr>
            <td style="text-align: left; width: 33.33%;"><span><p>Giảm trừ thuế</p></span></td>
            <td style="text-align: center; width: 33.33%;"><span></span></td>
            <td style="text-align: right; width: 33.33%;"><span style="text-align: right;"><p>{Giam_tru_thue}</p></span></td>
          </tr>
          <tr>
            <td style="text-align: left; width: 33.33%;"><span><p>Khách phải trả</p></span></td>
            <td style="text-align: center; width: 33.33%;"><span></span></td>
            <td style="text-align: right; width: 33.33%;"><span style="text-align: right;"><p>{Khach_phai_tra}</p></span></td>
          </tr>
          <tr>
            <td style="text-align: left; width: 33.33%;"><span><p>Khách trả</p></span></td>
            <td style="text-align: center; width: 33.33%;"><span></span></td>
            <td style="text-align: right; width: 33.33%;"><span style="text-align: right;"><p>{Khach_tra}</p></span></td>
          </tr>
          <tr>
            <td style="text-align: left; width: 33.33%;"><span><p>Tiền thừa</p></span></td>
            <td style="text-align: center; width: 33.33%;"><span></span></td>
            <td style="text-align: right; width: 33.33%;"><span style="text-align: right;"><p>{Tien_thua}</p></span></td>
          </tr>
        </tbody>
      </table>
      </div>
        <p style="text-align: center;">Thanh toán {Phuong_thuc_thanh_toan}</p>
        <p style="text-align: center; font-size: 12px;">CẢM ƠN QUÝ KHÁCH</p>
        <hr>
        <p style="text-align:center"><span style="font-size:14px">Mã QR tra cứu</span></p>

        <p style="text-align: center;"><img alt="" src="{qrCode}"/></p>

        <p style="text-align: center;"><span style="font-size:14px">Tra cứu hóa đơn tại</span></p>

        <p style="text-align:center"><span style="font-size:14px">{Link_tra_cuu}</span></p>

        <p style="text-align:center"><span style="font-size:14px">Mã tra cứu</span></p>

        <p style="text-align:center"><span style="font-size:14px">{Ma_tra_cuu}</span></p>

        <p style="text-align:center"><span style="font-size:14px">{Mo_ta}</span></p>`;
    this.content2 =  this.content;
  }

  ngOnInit(): void {
    this.dataPrintConfig = {
      company: 'Công ty cổ phần đầu tư và công nghệ SoftDreams',
      hotline: 'Hotline: 1900 3369',
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
    this.content = this.content.replace('{qrCode}', this.qrCode);
    this.updateContent();
    this.updateContent();
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
    this.content2 = this.style + this.content;
  }

  onEditorChange(value: string): void {
    this.content = value;
    if (this.onChange) this.onChange(value);
    if (this.onTouched) this.onTouched();
    this.updateContent();
  }
}
