export enum TypeInvoice {
  HOA_DON_HOP_LE = 1,
  HOA_DON_KHONG_HOP_LE = -2,
  HOA_DON_DANG_KIEM_TRA = -1,
  HOA_DON_CHUA_PHAT_HANH = 0,
}
export const STATUS_MAP = new Map<number, string>([
  [-1, 'Chờ ký'],
  [0, 'Mới tạo lập'],
  [1, 'Đã phát hành'],
  [2, 'Đã khai báo thuế'],
  [3, 'Bị thay thế'],
  [4, 'Bị điều chỉnh'],
  [5, 'Bị hủy'],
  [6, 'Đã duyệt'],
]);

export const TCT_CHECK_STATUS_MAP = new Map<number, string>([
  [-2, 'Không hợp lệ'],
  [-1, 'Đang kiểm tra'],
  [1, 'Hợp lệ '],
]);
export enum ServiceConstants {
  VTE = 'VTE',
  EI = 'EI',
  NGP = 'NGP',
}
export const NCCDV_EINVOICE = {
  SDS: 'SDS',
  SIV: 'SIV',
  SIV_2: 'SIV_2.0',
  MIV: 'MIV',
  VNPT: 'VNPT',
  BKAV: 'BKAV',
  CYBER: 'CYBER',
  NGP: 'NGP',
};
export const INVOICE_METHOD = {
  HSM_TU_DONG: 0,
  HSM_THU_CONG: 1,
  HSM_MOI_TAO_LAP: 2,
  TOKEN_MOI_TAO_LAP: 3,
};

export const ALL_STATUS = [1, 3, 4, 5];
export const ALL_TYPE = [1, 2, 3, 4];
