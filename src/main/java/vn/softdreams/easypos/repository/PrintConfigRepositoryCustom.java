package vn.softdreams.easypos.repository;

import vn.softdreams.easypos.dto.config.PrintConfigCompany;

import java.util.List;

public interface PrintConfigRepositoryCustom {
    List<PrintConfigCompany> findAllByCompanyID(Integer companyId, Integer type);
}
