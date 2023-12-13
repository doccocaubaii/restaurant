package vn.softdreams.easypos.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.easypos.dto.config.PrintConfigCompany;
import vn.softdreams.easypos.repository.PrintConfigRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintConfigRepositoryImpl implements PrintConfigRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<PrintConfigCompany> findAllByCompanyID(Integer companyId, Integer type) {
        List<PrintConfigCompany> configList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  from print_config WHERE com_id = :companyId ");
        params.put("companyId", companyId);
        if (type != null) {
            strQuery.append(" AND type = :type ");
            params.put("type", type);
        }
        //        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        //        Common.setParams(countQuery, params);
        //        Number count = (Number) countQuery.getSingleResult();
        //        if (count.intValue() > 0) {
        strQuery.append(" order by type asc, is_body asc, position asc ");
        Query query = entityManager.createNativeQuery(
            "SELECT id      id,  " +
            "       com_id      comId,  " +
            "       title        title,  " +
            "       type        type,  " +
            "       code        code,  " +
            "       name        content,  " +
            "       font_size   fontSize,  " +
            "       align_text  alignText,  " +
            "       is_bold     isBold,  " +
            "       is_print    isPrint,  " +
            "       is_header   isHeader,  " +
            "       is_editable isEditable, " +
            "       is_body isBody, " +
            "       position position " +
            strQuery,
            "PrintConfigCompanyResponse"
        );
        Common.setParams(query, params);
        configList = query.getResultList();
        //        }
        return configList;
    }
}
