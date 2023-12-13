package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.dto.rsInoutWardDetail.GetOneByIdDetailResponse;
import vn.softdreams.easypos.dto.rsinoutward.GetOneByIdResponse;
import vn.softdreams.easypos.dto.rsinoutward.RsInOutWardStatusResult;
import vn.softdreams.easypos.dto.rsinoutward.RsInoutWardResponse;
import vn.softdreams.easypos.repository.RsInoutWardRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class RsInoutWardRepositoryImpl implements RsInoutWardRepositoryCustom {

    private final EntityManager entityManager;

    public RsInoutWardRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Object getAllTransactions(
        Integer comId,
        String fromDate,
        String toDate,
        Integer type,
        String keyword,
        Boolean getWithPaging,
        Pageable pageable
    ) {
        List<RsInoutWardResponse> rsInoutWardList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from rs_inoutward r left join rs_inoutward_detail r2 on r2.rs_inoutward_id = r.id where r.com_Id = :comId ");
        params.put("comId", comId);

        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "r.update_time", "rs_inoutward");
        }

        if (type != null) {
            strQuery.append(" and r.type = :type ");
            params.put("type", type);
        }

        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" and r.customer_normalized_name like :keyword ");
            params.put("keyword", "%" + Common.normalizedName(Arrays.asList(keyword)) + "%");
        }
        strQuery.append(" group by r.id, r.type, r.date, r.no, r.quantity, r.total_amount, format(r2.update_time, 'yyyy-MM-dd HH:mm'), ");

        StringBuilder strQuerySelect = new StringBuilder();
        strQuerySelect.append(
            "select r.id id," +
            "r.type type," +
            "r.date date," +
            "r.no no," +
            "count(r2.product_id) productTypes," +
            "r.quantity quantity," +
            "r.total_amount totalAmount, "
        );

        if (type != null) {
            if (type.equals(Constants.RS_INWARD_TYPE)) {
                strQuerySelect.append(" r.supplier_name customerName, r.supplier_id customerId, COUNT(*) OVER () countAll ");
                strQuery.append("r.supplier_name, r.supplier_id");
            } else {
                strQuerySelect.append(" r.customer_name customerName, r.customer_id customerId, COUNT(*) OVER () countAll ");
                strQuery.append("r.customer_name, r.customer_id");
            }
        } else {
            strQuerySelect.append(
                " iif(r.customer_name is null, r.supplier_name, r.customer_name) customerName, " +
                "iif(r.customer_id is null, r.supplier_id, r.customer_id) customerId, COUNT(*) OVER () countAll "
            );
            strQuery.append("r.customer_name, r.customer_id, r.supplier_name, r.supplier_id");
        }
        strQuery.append(" order by format(r2.update_time, 'yyyy-MM-dd HH:mm') desc ");
        Query query = entityManager.createNativeQuery(String.valueOf(strQuerySelect.append(strQuery)), "RsInoutWardResponse");
        if (getWithPaging != null && getWithPaging) {
            Common.setParamsWithPageable(query, params, pageable);
            rsInoutWardList = query.getResultList();
            if (!rsInoutWardList.isEmpty()) {
                return new PageImpl<>(rsInoutWardList, pageable, rsInoutWardList.get(0).getCountAll());
            }
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        } else {
            Common.setParams(query, params);
            rsInoutWardList = query.getResultList();
            return rsInoutWardList;
        }
    }

    @Override
    public RsInOutWardStatusResult getTotalAmountStatus(Integer comId, String fromDate, String toDate, Integer type, String keyword) {
        List<RsInOutWardStatusResult> result;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from rs_inoutward r where r.com_Id = :comId ");
        params.put("comId", comId);

        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "date", "rs_inoutward");
        }

        if (type != null) {
            strQuery.append(" and r.type = :type ");
            params.put("type", type);
        }

        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" and r.customer_normalized_name like :keyword ");
            params.put("keyword", "%" + Common.normalizedName(Arrays.asList(keyword)) + "%");
        }

        Query query = entityManager.createNativeQuery(
            "select sum(iif(r.type = 1, r.total_amount, 0)) totalAmountInWard, sum(iif(r.type = 2, r.total_amount, 0)) totalAmountOutWard " +
            strQuery,
            "RsInoutWardAmountStatusResult"
        );
        Common.setParams(query, params);
        result = query.getResultList();
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public GetOneByIdResponse getOneById(Integer id, Integer companyId) {
        List<GetOneByIdResponse> responses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" FROM rs_inoutward r1 left join ep_user eu on r1.creator = eu.id ");
        strQuery.append(" WHERE r1.id = :id and r1.com_id = :companyId ");
        params.put("id", id);
        params.put("companyId", companyId);

        Query query = entityManager.createNativeQuery(
            " SELECT r1.id id, " +
            " r1.bill_Id billId, " +
            " r1.type type, " +
            " r1.type_desc typeDesc, " +
            " r1.date date, " +
            " r1.no no, " +
            " r1.supplier_name supplierName, " +
            " r1.supplier_id supplierId, " +
            " r1.customer_name customerName, " +
            " r1.customer_id customerId, " +
            " r1.quantity quantity, " +
            " r1.amount amount, " +
            " r1.discount_amount discountAmount, " +
            " r1.cost_amount costAmount, " +
            " r1.total_amount totalAmount, " +
            " r1.description description, " +
            " eu.id creator " +
            strQuery,
            "GetOneByIdResponse"
        );
        Common.setParams(query, params);
        responses = query.getResultList();
        if (!responses.isEmpty()) {
            return responses.get(0);
        }
        return null;
    }

    @Override
    public List<GetOneByIdDetailResponse> getDetailByRsInoutWardId(Integer id) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        List<GetOneByIdDetailResponse> responses = new ArrayList<>();
        strQuery.append(" FROM rs_inoutward_detail r2 ");
        strQuery.append(" WHERE r2.rs_inoutward_id = :id ");
        params.put("id", id);
        Query queryDetail = entityManager.createNativeQuery(
            "SELECT r2.rs_inoutward_id id, " +
            " r2.position position, " +
            " r2.product_name productName, " +
            " r2.product_code productCode, " +
            " r2.unit_name unitName, " +
            " r2.quantity quantity, " +
            " r2.unit_price unitPrice, " +
            " r2.amount amount, " +
            " r2.discount_amount discountAmount, " +
            " r2.total_amount totalAmount, " +
            " r2.lot_no lotNo " +
            strQuery,
            "GetOneByIdDetailResponse"
        );
        Common.setParams(queryDetail, params);
        responses = queryDetail.getResultList();
        return responses;
    }
}
