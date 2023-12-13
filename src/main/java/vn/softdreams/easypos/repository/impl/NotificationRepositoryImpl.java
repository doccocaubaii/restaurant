package vn.softdreams.easypos.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.notification.NotificationItemResponse;
import vn.softdreams.easypos.repository.InvoiceRepositoryCustom;
import vn.softdreams.easypos.repository.NotificationRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(InvoiceRepositoryCustom.class);

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<NotificationItemResponse> getWithPaging(Pageable pageable, Boolean isUnRead, Integer comId, Integer userId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        List<NotificationItemResponse> list = new ArrayList<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from notification n join notification_user nu on n.id = nu.notification_id  " +
            " where com_id = :comId and user_id = :userId and CAST(n.create_time as date) = CAST(GETDATE() as date) "
        );
        params.put("comId", comId);
        params.put("userId", userId);
        if (isUnRead != null && isUnRead) {
            strQuery.append(" and is_read = 0 ");
        }
        if (type != null) {
            strQuery.append(" and type = :type ");
            params.put("type", type);
        }
        Number count = 0;
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            strQuery.append(" order by n.create_time desc ");
            Query query = entityManager.createNativeQuery(
                "select n.id, n.bill_id billId, n.subject, n.content, n.create_time createTime, n.is_read isRead, n.type type " + strQuery,
                "NotificationItemResponse"
            );
            Common.setParamsWithPageable(query, params, pageable);
            list = query.getResultList();
        }
        return new PageImpl<>(list, pageable, count.longValue());
    }
}
