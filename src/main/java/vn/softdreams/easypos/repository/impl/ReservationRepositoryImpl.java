package vn.softdreams.easypos.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.reservation.ReservationResponse;
import vn.softdreams.easypos.repository.ReservationRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<ReservationResponse> findAll(Pageable pageable, Integer comId, Integer status) {
        List<ReservationResponse> responses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  from reservation e where e.com_id = :comId ");
        params.put("comId", comId);
        if (status != null) {
            strQuery.append(" and e.status = :status ");
            params.put("status", status);
        }

        Query query = entityManager.createNativeQuery(
            "select e.id id, e.com_id comId, e.customer_phone customerPhone, " +
            "e.customer_name customerName, e.order_date orderDate, " +
            "e.order_time orderTime, e.arrival_time arrivalTime,  " +
            " e.people_count peopleCount, e.status status, e.note note, " +
            "e.create_time createTime, e.update_time updateTime " +
            strQuery,
            "ReservationResponse"
        );
        Common.setParamsWithPageable(query, params, pageable);
        responses = query.getResultList();
        return new PageImpl<>(responses, pageable, responses.size());
    }
}
