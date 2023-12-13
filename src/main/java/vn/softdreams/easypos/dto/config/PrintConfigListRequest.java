package vn.softdreams.easypos.dto.config;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class PrintConfigListRequest {

    @Valid
    @NotNull(message = ExceptionConstants.PRINT_CONFIGS_NOT_NULL)
    private List<PrintConfigCompany> printConfigs;

    public List<PrintConfigCompany> getPrintConfigs() {
        return printConfigs;
    }

    public void setPrintConfigs(List<PrintConfigCompany> printConfigs) {
        this.printConfigs = printConfigs;
    }
}
