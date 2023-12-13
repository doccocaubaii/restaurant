package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.taskLog.TaskLogItemResponse;
import vn.softdreams.easypos.repository.TaskLogRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskLogRepositoryImpl implements TaskLogRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<TaskLogItemResponse> filter(
        Pageable pageable,
        Integer status,
        String taxCode,
        Integer companyOwnerName,
        String fromDate,
        String toDate,
        String type
    ) {
        List<TaskLogItemResponse> taskLogList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from task_log t ");
        strQuery.append(" join company c on t.com_id = c.id ");
        strQuery.append(" join company_owner co on c.com_owner_id = co.id ");
        strQuery.append(" WHERE 1 = 1");
        if (companyOwnerName != null && companyOwnerName != -1) {
            strQuery.append(" AND t.com_id = :comId ");
            params.put("comId", companyOwnerName);
        }
        if (status != null && status != -1) {
            strQuery.append(" AND status = :status ");
            params.put("status", status);
        }
        if (!Strings.isNullOrEmpty(taxCode)) {
            strQuery.append(" AND tax_code like :taxCode ");
            params.put("taxCode", "%" + taxCode + "%");
        }
        if (!Strings.isNullOrEmpty(type)) {
            strQuery.append(" AND type like :type ");
            params.put("type", "%" + type + "%");
        }
        Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "t.create_time", "task_log");
        Common.replaceWhere(strQuery);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            strQuery.append(" ORDER BY ").append(pageable.getSort().toString().replace(":", ""));
            Query query = entityManager.createNativeQuery(
                "select " +
                "t.id id, " +
                "t.com_id com_id, " +
                "c.name companyName, " +
                "t.type type, " +
                "t.content content, " +
                "t.status status, " +
                "t.error_message error_message, " +
                "t.create_time create_time " +
                strQuery,
                "TaskLogItemResponseDTO"
            );
            Common.setParamsWithPageable(query, params, pageable, count);
            taskLogList = query.getResultList();
        }
        return new PageImpl<>(taskLogList, pageable, count.longValue());
    }
}
