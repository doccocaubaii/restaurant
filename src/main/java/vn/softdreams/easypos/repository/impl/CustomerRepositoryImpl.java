package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.constants.CommonConstants;
import vn.softdreams.easypos.constants.CustomerConstants;
import vn.softdreams.easypos.dto.customer.CustomerItemResult;
import vn.softdreams.easypos.dto.customer.CustomerResponse;
import vn.softdreams.easypos.repository.CustomerRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<CustomerResponse> getAllWithPaging(
        Pageable pageable,
        String keyword,
        Integer companyId,
        Integer active,
        Integer type,
        boolean isCountAll,
        Boolean isHiddenDefault,
        boolean paramCheckAll,
        List<Integer> ids
    ) {
        List<CustomerResponse> customers = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  from customer c where c.com_id = :companyId and c.active = :active ");
        params.put("companyId", companyId);
        params.put("active", active);
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" AND (c.normalized_name LIKE :keyword OR c.tax_code LIKE :keyword) ");
            params.put("keyword", "%" + Common.normalizedName(Arrays.asList(keyword)) + "%");
        }
        if (Objects.equals(type, CustomerConstants.Type.CUSTOMER_AND_SUPPLIER) || Objects.equals(type, CustomerConstants.Type.OTHER)) {
            strQuery.append(" AND c.type = :type ");
            params.put("type", type);
        } else if (Objects.equals(type, CustomerConstants.Type.CUSTOMER) || Objects.equals(type, CustomerConstants.Type.SUPPLIER)) {
            List<Integer> types = new ArrayList<>();
            types.add(type);
            types.add(CustomerConstants.Type.CUSTOMER_AND_SUPPLIER);
            strQuery.append(" AND c.type in :type ");
            params.put("type", types);
        }
        if (isHiddenDefault != null && isHiddenDefault) {
            strQuery.append(" AND c.code != :codeDefault ");
            params.put("codeDefault", CommonConstants.CUSTOMER_CODE_DEFAULT);
        }
        if (paramCheckAll) {
            if (ids != null && ids.size() > 0) {
                strQuery.append(" AND c.id not in :ids ");
                params.put("ids", ids);
            }
        } else {
            if (ids != null && ids.size() > 0) {
                strQuery.append(" AND c.id in :ids ");
                params.put("ids", ids);
            }
        }

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            strQuery.append(" order by case when c.code = :codeDefault then 0 else 1 end, c.code2 asc, c.name asc ");
            params.put("codeDefault", CommonConstants.CUSTOMER_CODE_DEFAULT);
            Query query = entityManager.createNativeQuery(
                "SELECT c.id id, " +
                "c.com_id comId, " +
                "c.name name, " +
                "c.gender gender, " +
                "c.type type, " +
                "c.code code, " +
                "c.code2 code2, " +
                "c.address address, " +
                "c.phone_number phoneNumber, " +
                "c.email email, " +
                "c.tax_code taxCode, " +
                "c.id_number idNumber," +
                "c.description description, " +
                "c.city city, " +
                "c.district district, " +
                "c.birthday birthday, " +
                "c.create_time createTime, " +
                "c.update_time updateTime " +
                strQuery,
                "CustomerResponse"
            );
            if (pageable == null) {
                Common.setParams(query, params);
                customers = query.getResultList();
                return new PageImpl<>(customers);
            } else {
                Common.setParamsWithPageable(query, params, pageable);
                customers = query.getResultList();
                return new PageImpl<>(customers, pageable, count.longValue());
            }
        }
        return new PageImpl<>(new ArrayList<>(), pageable, count.longValue());
    }

    @Override
    public List<CustomerResponse> getAllForOffline(Integer companyId, Integer active, Integer type) {
        List<CustomerResponse> customers = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  FROM customer c WHERE c.com_id = :companyId AND c.active = :active ");
        params.put("companyId", companyId);
        params.put("active", active);
        if (Objects.equals(type, CustomerConstants.Type.CUSTOMER_AND_SUPPLIER) || Objects.equals(type, CustomerConstants.Type.OTHER)) {
            strQuery.append(" AND c.type = :type ");
            params.put("type", type);
        } else if (Objects.equals(type, CustomerConstants.Type.CUSTOMER) || Objects.equals(type, CustomerConstants.Type.SUPPLIER)) {
            List<Integer> types = new ArrayList<>();
            types.add(type);
            types.add(CustomerConstants.Type.CUSTOMER_AND_SUPPLIER);
            strQuery.append(" AND c.type in :type ");
            params.put("type", types);
        }

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            strQuery.append(" order by case when c.code = :codeDefault then 0 else 1 end, c.code2 asc, c.name asc ");
            params.put("codeDefault", CommonConstants.CUSTOMER_CODE_DEFAULT);
            Query query = entityManager.createNativeQuery(
                "SELECT c.id id, " +
                "c.com_id comId, " +
                "c.name name, " +
                "c.gender gender, " +
                "c.type type, " +
                "c.code code, " +
                "c.code2 code2, " +
                "c.address address, " +
                "c.phone_number phoneNumber, " +
                "c.email email, " +
                "c.tax_code taxCode, " +
                "c.id_number idNumber," +
                "c.description description, " +
                "c.city city, " +
                "c.district district, " +
                "c.birthday birthday, " +
                "c.create_time createTime, " +
                "c.update_time updateTime " +
                strQuery,
                "CustomerResponse"
            );
            Common.setParams(query, params);
            customers = query.getResultList();
        }
        return customers;
    }

    @Override
    public Page<CustomerItemResult> getAllCustomerItem(Integer comId, Pageable pageable, String keyword) {
        List<CustomerItemResult> customers;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  from customer c where c.com_id = :comId and c.active = 1 and c.type != 3 ");
        params.put("comId", comId);
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" and (c.normalized_name like :keyword or c.tax_code like :keyword) ");
            params.put("keyword", "%" + Common.normalizedName(List.of(keyword)) + "%");
        }

        Query countQuery = entityManager.createNativeQuery("select count(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "select c.id customerId, c.name customerName, c.tax_code taxCode, c.code2 code2 " + strQuery,
                "CustomerItemResult"
            );
            Common.setParamsWithPageable(query, params, pageable);
            customers = query.getResultList();
            return new PageImpl<>(customers, pageable, count.longValue());
        }
        return new PageImpl<>(new ArrayList<>(), pageable, count.longValue());
    }
}
