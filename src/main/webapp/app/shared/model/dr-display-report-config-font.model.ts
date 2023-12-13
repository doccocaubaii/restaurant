export interface IDRDisplayReportConfigFont {
  id?: number;
  displayReportConfigID?: string;
  titleFont?: string;
  subTitleFont?: string;
  headerFont?: string;
  bodyTextFont?: string;
  bodyNumberFont?: string;
  bodySumFont?: string;
  footerTextFont?: string;
  footerSumFont?: string;
  dayMonthFont?: string;
  positionFont?: string;
  typeSignedFont?: string;
  signedFont?: string;
}

export class DRDisplayReportConfigFont implements IDRDisplayReportConfigFont {
  constructor(
    public id?: number,
    public displayReportConfigID?: string,
    public titleFont?: string,
    public subTitleFont?: string,
    public headerFont?: string,
    public bodyTextFont?: string,
    public bodyNumberFont?: string,
    public bodySumFont?: string,
    public footerTextFont?: string,
    public footerSumFont?: string,
    public dayMonthFont?: string,
    public positionFont?: string,
    public typeSignedFont?: string,
    public signedFont?: string
  ) {}
}
