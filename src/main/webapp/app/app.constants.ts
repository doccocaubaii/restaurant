// These constants are injected via webpack DefinePlugin variables.
// You can add more variables in webpack.common.js or in profile specific webpack.<dev|prod>.js files.
// If you change the values in the webpack config files, you need to re run webpack to update the application

declare const __DEBUG_INFO_ENABLED__: boolean;
declare const __VERSION__: string;

export const VERSION = __VERSION__;
export const DEBUG_INFO_ENABLED = __DEBUG_INFO_ENABLED__;
export const CentimeterToPixel = 37.795275591; // Quy đổi 1cm sang pixel
export const PicaPointToPixel = 1.3281472327365; // Quy đổi 1pt sang pixel
export const percentPortrait = 1.265; // Tỉ lệ scale khi in báo cáo
export const percentLandScape = 1.215; // Tỉ lệ scale khi in báo cáo
export const Max_Row_Count = 2000;
export const ROLE = {
  ROLE_MGT: 'ROLE_MGT',
  ROLE_PERMISSION: 'ROLE_PERMISSION',
  ROLE_ADMIN_KH: 'ROLE_ADMIN_KH',
  ROLE_REPORT: 'ROLE_REPORT',
  ROLE_ADMIN: 'ROLE_ADMIN',
  // 1.Tien Mat Ngan Hang
  TienMatNganHang: '01',
};
export const ViTri = {
  TenCCTC: 'headerOrgFont',
  TenThemCCTC: 'headerAddInfoOrgFont',
  SoChungTu: 'headerNoFont',
  TieuDeChinh: 'titleFont',
  TieuDePhu: 'subTitleFont',
  TieuDeCot: 'headerFont',
  NoiDungKieuChu: 'bodyTextFont',
  NoiDungKieuSo: 'bodyNumberFont',
  PhanChungKieuChu: 'generalTextFont',
  PhanChungKieuSo: 'generalNumberFont',
  NoiDungTongCong: 'bodySumFont',
  NoiDungTongCongCustom: 'bodySumFontCustom',
  CuoiTrangKieuChu: 'footerTextFont',
  CuoiTrangTongCong: 'footerSumFont',
  NgayThang: 'monthDayFont',
  ChucDanh: 'positionFont',
  LoaiKy: 'typeSignedFont',
  ChuKy: 'signedFont',
};
export const BaoCao = {
  TongHop: {
    THONG_KE_LOI_NHUAN_SAN_PHAM: 'THONG_KE_LOI_NHUAN_SAN_PHAM',
    THONG_KE_LOI_NHUAN_SAN_PHAM_XLS: 'Thong_Ke_Loi_Nhuan_San_Pham',
  },
};
export const BaoCaoDong = {
  TYPE_PREVIEW_PROFIT: 1,
};
export const ResponseAsync = {
  EXPORT_EXCEL_IN_PROGRESS: 'Quá trình kết xuất đang được xử lý. Vui lòng chờ trong giây lát!',
  EXPORT_PDF_IN_PROGRESS: 'Quá trình kết xuất đang được xử lý. Vui lòng chờ trong giây lát!',
  ERROR: 'Có lỗi xảy ra',
};
