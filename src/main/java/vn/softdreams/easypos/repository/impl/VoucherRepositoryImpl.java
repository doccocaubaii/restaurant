package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.VoucherConstants;
import vn.softdreams.easypos.dto.voucher.VoucherResponse;
import vn.softdreams.easypos.dto.voucher.VoucherWebResponse;
import vn.softdreams.easypos.repository.VoucherRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoucherRepositoryImpl implements VoucherRepositoryCustom {

    private final EntityManager entityManager;

    public VoucherRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<VoucherResponse> getWithPaging(
        Integer comId,
        Pageable pageable,
        String keyword,
        List<Integer> status,
        String fromDate,
        String toDate,
        Integer type
    ) {
        List<VoucherResponse> response;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from voucher v join voucher_company vc on v.id = vc.voucher_id where vc.com_id = :comId and v.status in :status "
        );
        params.put("comId", comId);
        params.put("status", status);
        if (type.equals(VoucherConstants.TypeRequestGetAll.IS_VOUCHER)) {
            strQuery.append(" and v.type = :type ");
            params.put("type", VoucherConstants.DiscountType.VOUCHER_DISCOUNT);
        } else {
            strQuery.append(" and v.type in :type ");
            params.put("type", VoucherConstants.DiscountType.CONDITION_EXTEND);
        }
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" AND (v.normalized_name LIKE :keyword or v.code LIKE :keyword) ");
            params.put("keyword", "%" + Common.normalizedName(List.of(keyword)) + "%");
        }

        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "v.start_time", "v.end_time", "voucher");
        }

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "select v.id id, v.code code, v.name name, v.discount_conditions discountConditions, v.start_time startTime, v.end_time endTime, v.status status, v.type type, v.ext_time_conditions extTimeCondition, v.different_ext_conditions differentExtCondition " +
                strQuery,
                "VoucherResponse"
            );
            Common.setParamsWithPageable(query, params, pageable);
            response = query.getResultList();
            return new PageImpl<>(response, pageable, count.longValue());
        }
        return new PageImpl<>(new ArrayList<>(), pageable, count.longValue());
    }

    @Override
    public Page<VoucherResponse> getVoucherForBill(Integer comId, Pageable pageable, String keyword, Integer customerId) {
        List<VoucherResponse> response;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_TIME_FORMAT);
        strQuery.append(
            " from voucher v join voucher_apply va on v.id = va.voucher_id " +
            " where va.com_id = :comId and va.customer_id = :customerId and v.status = 1 " +
            " and v.start_time <= :time and v.end_time >= :time "
        );
        params.put("comId", comId);
        params.put("customerId", customerId);
        params.put("time", formatter.format(ZonedDateTime.now()));
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" AND (v.normalized_name LIKE :keyword ) ");
            params.put("keyword", "%" + Common.normalizedName(List.of(keyword)) + "%");
        }

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "select distinct v.id id, v.code code, v.name name, v.discount_conditions discountConditions, v.start_time startTime, v.end_time endTime, v.status status, v.type type, v.ext_time_conditions extTimeCondition, v.different_ext_conditions differentExtCondition  " +
                strQuery,
                "VoucherResponse"
            );
            Common.setParamsWithPageable(query, params, pageable);
            response = query.getResultList();
            return new PageImpl<>(response, pageable, count.longValue());
        }
        return new PageImpl<>(new ArrayList<>(), pageable, count.longValue());
    }

    @Override
    public Page<VoucherWebResponse> getWithPagingWeb(
        Integer comId,
        Pageable pageable,
        String keyword,
        List<Integer> status,
        String fromDate,
        String toDate
    ) {
        List<VoucherWebResponse> response;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from voucher v join voucher_company vc on v.id = vc.voucher_id where vc.com_id = :comId and v.status in :status "
        );
        params.put("comId", comId);
        params.put("status", status);
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" AND (v.normalized_name LIKE :keyword ) ");
            params.put("keyword", "%" + Common.normalizedName(List.of(keyword)) + "%");
        }

        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "v.start_time", "v.end_time", "voucher");
        }

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "select v.id id, v.code code, v.name name, v.discount_conditions discountConditions, v.start_time startTime, v.end_time endTime, v.status status " +
                strQuery,
                "VoucherWebResponse"
            );
            Common.setParamsWithPageable(query, params, pageable);
            response = query.getResultList();
            return new PageImpl<>(response, pageable, count.longValue());
        }
        return new PageImpl<>(new ArrayList<>(), pageable, count.longValue());
    }

    @Override
    public Page<VoucherWebResponse> getVoucherForBillWeb(Integer comId, Pageable pageable, String keyword, Integer customerId) {
        List<VoucherWebResponse> response;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_TIME_FORMAT);
        strQuery.append(
            " from voucher v join voucher_apply va on v.id = va.voucher_id " +
            " where va.com_id = :comId and va.customer_id = :customerId and v.status = 1 " +
            " and v.start_time <= :time and v.end_time >= :time "
        );
        params.put("comId", comId);
        params.put("customerId", customerId);
        params.put("time", formatter.format(ZonedDateTime.now()));
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" AND (v.normalized_name LIKE :keyword ) ");
            params.put("keyword", "%" + Common.normalizedName(List.of(keyword)) + "%");
        }

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "select distinct v.id id, v.code code, v.name name, v.discount_conditions discountConditions, v.start_time startTime, v.end_time endTime, v.status status " +
                strQuery,
                "VoucherWebResponse"
            );
            Common.setParamsWithPageable(query, params, pageable);
            response = query.getResultList();
            return new PageImpl<>(response, pageable, count.longValue());
        }
        return new PageImpl<>(new ArrayList<>(), pageable, count.longValue());
    }
}
