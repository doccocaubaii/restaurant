package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.CommonConstants;
import vn.softdreams.easypos.constants.ProcessingAreaConstants;
import vn.softdreams.easypos.domain.Bill;
import vn.softdreams.easypos.dto.audit.ActivityHistoryResponse;
import vn.softdreams.easypos.dto.audit.ActivityHistoryResult;
import vn.softdreams.easypos.dto.bill.*;
import vn.softdreams.easypos.dto.product.HotSaleProductResult;
import vn.softdreams.easypos.repository.BillRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BillRepositoryImpl implements BillRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<BillUnitResponse> getBillUnit(Integer comId, Integer deliveryType, Integer status, List<Integer> areaUnitIds) {
        List<BillUnitResponse> billUnitResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from bill b join area_unit au on au.id = b.area_unit_id where b.com_id = :comId " +
            "and b.delivery_type = :deliveryType and b.status = :status and au.id in :areaUnits order by b.id asc "
        );
        params.put("comId", comId);
        params.put("areaUnits", areaUnitIds);
        params.put("status", status);
        params.put("deliveryType", deliveryType);

        Query query = entityManager.createNativeQuery(
            "select " +
            "b.id, b.com_id comId, b.area_unit_id areaUnitId, b.status status, " +
            "b.total_amount totalAmount, b.create_time createTime, " +
            "b.update_time updateTime " +
            strQuery,
            "BillUnitResponse"
        );
        Common.setParams(query, params);
        billUnitResponses = query.getResultList();
        return billUnitResponses;
    }

    @Override
    public Page<BillItemResponse> searchBills(
        Pageable pageable,
        Integer status,
        String fromDate,
        String toDate,
        String keyword,
        Integer comId,
        Boolean isCountAll
    ) {
        List<BillItemResponse> billList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from BILL b ");
        strQuery.append(" left join bill_payment bp on b.id = bp.bill_id ");
        strQuery.append(" WHERE com_id = :comId ");
        params.put("comId", comId);
        if (status != null) {
            strQuery.append(" AND status = :status ");
            params.put("status", status);
        }
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "b.create_time", "bill");
        }
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" AND (b.customer_normalized_name like :keyword or b.code like :keyword) ");
            params.put("keyword", "%" + Common.normalizedName(Arrays.asList(keyword)) + "%");
        }
        String sort = " order by b.update_time desc ";
        Number count = 0;
        if (isCountAll != null && isCountAll) {
            Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
            Common.setParams(countQuery, params);
            count = (Number) countQuery.getSingleResult();
        }

        Query query = entityManager.createNativeQuery(
            "select b.id id, " +
            "       b.code code, " +
            "       b.code2 code2, " +
            "       b.total_amount  totalAmount, " +
            "       b.customer_id   customerId, " +
            "       b.customer_name customerName, " +
            "       b.status status, " +
            "       bp.payment_method paymentMethod, " +
            "       bp.debt debt, " +
            "       bp.refund refund, " +
            "       bp.amount amount, " +
            "       b.create_time createTime, " +
            "       b.bill_date billDate, " +
            "       b.bill_id_returns billIdReturns " +
            strQuery +
            sort,
            "BillResponseItemDTO"
        );
        Common.setParamsWithPageable(query, params, pageable);
        billList = query.getResultList();
        //        }
        return new PageImpl<>(billList, pageable, Boolean.TRUE.equals(isCountAll) ? count.longValue() : billList.size());
    }

    @Override
    public Optional<Bill> findByIdAndComIdCustom(Integer billId, Integer companyId) {
        Bill results;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from bill b where b.id = :id and b.com_id = :com_id ");
        params.put("id", billId);
        params.put("com_id", companyId);
        Query query = entityManager.createNativeQuery("select * " + strQuery, Bill.class);
        Common.setParams(query, params);
        results = (Bill) query.getSingleResult();
        return Optional.of(results);
    }

    @Override
    public BigDecimal getBillRevenue(String fromDate, String toDate, Integer comId) {
        Object results;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from bill b where status = 1 and com_id = :comId ");
        params.put("comId", comId);

        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "b.bill_date", "bill");
        }

        Query query = entityManager.createNativeQuery("select SUM(b.total_amount) revenue " + strQuery, "BillRevenueResult");
        Common.setParams(query, params);
        results = query.getSingleResult();

        return ((BillRevenueResult) results).getRevenue() == null
            ? BigDecimal.ZERO.setScale(CommonConstants.REGISTER_PASSWORD_LENGTH, RoundingMode.UNNECESSARY)
            : ((BillRevenueResult) results).getRevenue();
    }

    @Override
    public List<BillStatItem> getBillMoney(Integer comId, String fromDate, String toDate, String format) {
        List<BillStatItem> items = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" FROM bill b ");
        strQuery.append(" WHERE b.com_id = :com_id and status = 1 ");
        params.put("com_id", comId);
        Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "b.bill_date", "bill");
        Query query = entityManager.createNativeQuery(
            "SELECT format(bill_date, '" +
            format +
            "') time, SUM(b.total_amount) money " +
            strQuery +
            " GROUP BY format(bill_date, '" +
            format +
            "') " +
            " ORDER BY format(bill_date, '" +
            format +
            "') ",
            "BillMoneyResultItem"
        );
        Common.setParams(query, params);
        items = query.getResultList();
        return items;
    }

    @Override
    public List<BillStatItem> getBillExpense(Integer comId, String fromDate, String toDate, String format) {
        List<BillStatItem> items = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" FROM rs_inoutward ri " + "JOIN rs_inoutward_detail rid on ri.id = rid.rs_inoutward_id ");
        strQuery.append(" WHERE ri.com_id = :com_id AND ri.bill_id is not null AND ri.type = 2 AND ri.type_desc like N'%Xuất%' ");
        params.put("com_id", comId);
        Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "ri.date", "bill");
        Query query = entityManager.createNativeQuery(
            "SELECT format(date, '" +
            format +
            "') time, " +
            "  SUM((IIF((rid.unit_price IS NULL), 0, rid.unit_price) * rid.quantity)) money " +
            strQuery +
            " GROUP BY format(date, '" +
            format +
            "') " +
            " ORDER BY format(date, '" +
            format +
            "') ",
            "BillMoneyResultItem"
        );
        Common.setParams(query, params);
        items = query.getResultList();
        return items;
    }

    @Override
    public List<BillProductChangeUnit> checkChangeUnit(
        Integer comId,
        List<Integer> productIds,
        List<Integer> productUnitIds,
        List<String> unitNames
    ) {
        List<BillProductChangeUnit> billList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from product_product_unit ppu join product p on ppu.product_id = p.id ");
        strQuery.append(" WHERE ppu.com_id = :comId AND (");
        params.put("comId", comId);
        for (int i = 0; i < productIds.size(); i++) {
            strQuery.append(
                "or (ppu.product_id " +
                modifyWhere(productIds.get(i)) +
                " and ppu.product_unit_id " +
                modifyWhere(productUnitIds.get(i)) +
                " and unit_name " +
                modifyWhere(unitNames.get(i)) +
                ") "
            );
        }
        strQuery.append(")");
        Query query = entityManager.createNativeQuery(
            "select ppu.id id, ppu.product_id productId, ppu.product_unit_id productUnitId, ppu.unit_name unitName, p.name productName " +
            strQuery.toString().replace("(or", "("),
            "BillProductChangeUnit"
        );
        Common.setParams(query, params);
        billList = query.getResultList();
        return billList;
    }

    private String modifyWhere(Integer input) {
        if (input == null) {
            return " is null ";
        }
        return " = " + input;
    }

    private String modifyWhere(String input) {
        if (input == null) {
            return " is null ";
        }
        return " = N'" + input + "'";
    }

    @Override
    public List<HotSaleProductResult> getHotSaleProductStats(Integer comId, String fromDate, String toDate, Integer type) {
        List<HotSaleProductResult> results = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        List<String> productCodeDefault = List.of(
            Constants.PRODUCT_CODE_DEFAULT,
            Constants.PRODUCT_CODE_NOTE_DEFAULT,
            Constants.PRODUCT_CODE_PROMOTION_DEFAULT,
            Constants.PRODUCT_CODE_SERVICE_CHARGE_DEFAULT
        );
        strQuery.append(
            " with tbl_bill_product as ( " +
            "select p.id, p.name, bp.unit_id unitId, p.unit, p.image, " +
            "sum(bp.quantity) as quantity, sum(bp.total_amount) as amount, bp.product_product_unit_id " +
            "from bill b join bill_product bp on b.id = bp.bill_id " +
            "join product p on bp.product_id = p.id " +
            "where b.com_id = :comId and p.active = 1 and b.status = 1 and p.code not in :productCodeDefault "
        );
        params.put("comId", comId);
        params.put("productCodeDefault", productCodeDefault);
        Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "b.bill_date", "bill");
        strQuery.append(" and p.active = 1 and b.status = 1 and p.code not in ('SP1', 'SPGC', 'SPKM') ");
        strQuery.append(" group by p.id, p.name, bp.unit_id, p.image, p.unit, bp.product_product_unit_id, p.image) ");

        String orderByString = " order by totalAmount desc ";
        if (Objects.equals(type, 1)) {
            orderByString = " order by totalQuantity desc ";
        }
        Query query = entityManager.createNativeQuery(
            strQuery +
            "select top 10 t.id, t.name, t.unit unitName, " +
            "sum(t.quantity * iif(ppu.convert_rate is null, 1, ppu.convert_rate)) totalQuantity, " +
            "sum(t.amount) totalAmount, t.image image from tbl_bill_product t " +
            "join product_product_unit ppu on ppu.product_id = t.id " +
            "and (ppu.product_unit_id is null and t.unitId is null) or ppu.id = t.product_product_unit_id " +
            "group by t.id, t.name, t.unit, t.image " +
            orderByString,
            "HotSaleProductResult"
        );
        Common.setParams(query, params);
        results = query.getResultList();
        return results;
    }

    @Override
    public Page<ActivityHistoryResult> getActivityHistory(Integer comId, String fromDate, String toDate, Pageable pageable) {
        List<ActivityHistoryResult> results = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " with t as((select min(rev) as rev, min(b.id) as id, min(b.code) as code, 1 type, min(bill_aud.update_time) as update_time, row_number() over (partition by b.id order by min(bill_aud.update_time) desc) rowNumber  " +
            "from audit.bill_aud join bill b on b.id = bill_aud.id " +
            "where bill_aud.com_id = :comId  "
        );
        Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "bill_aud.update_time", "audit.bill_aud");
        strQuery.append(
            " group by b.id, bill_aud.vat_amount, bill_aud.total_amount, bill_aud.status) " +
            "union  " +
            "(select t1.rev, t1.id, t1.code, t1.type, t1.updateTime, t1.rowNumber " +
            "from (select status, min(arising_date) arising_date, min(revtype) revtype, min(rev) rev, id, cast(no as varchar) code, " +
            " 2 type, min(update_time) updateTime, row_number() over (partition by id order by min(update_time) desc) rowNumber " +
            "from audit.invoice_aud where company_id = :comId and (status is null or status = 1) " +
            "and (error_publish is null or error_publish = '') group by id, no, vat_amount, total_amount, status) t1 " +
            "where NOT (t1.revtype = 0 AND t1.status IS NULL) "
        );
        Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "updateTime", "t1");
        Query countQuery = entityManager.createNativeQuery(strQuery + " ))" + "SELECT COUNT(*) from t");
        params.put("comId", comId);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            strQuery.append(
                " )) select t.rev, t.id, t.code, t.type, t.rowNumber, t.update_time updateTime from t order by t.update_time desc "
            );
            Query query = entityManager.createNativeQuery(String.valueOf(strQuery), "ActivityHistoryResult");
            Common.setParamsWithPageable(query, params, pageable);
            results = query.getResultList();
        }
        return new PageImpl<>(results, pageable, count.longValue());
    }

    @Override
    public List<ActivityHistoryResponse> getAllActivityHistory(List<Integer> billIds, List<Integer> invoiceIds) {
        List<ActivityHistoryResponse> results;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " with t as (select min(rev) rev, min(revtype) revtype, billAud.id id, billAud.status, b.code, b.code billCode, b.customer_id customerId, full_name fullName, " +
            "billAud.total_amount totalAmount, null vatAmount, min(billAud.update_time) updateTime, 1 type, " +
            "row_number() over (partition by billAud.id order by min(billAud.update_time) desc) rowNumber " +
            "from audit.bill_aud billAud " +
            "join bill b on b.id = billAud.id " +
            "join ep_user u on billAud.updater = u.id " +
            "where billAud.id in :billIds " +
            "group by billAud.id, billAud.status,b.code, b.customer_id, full_name, billAud.total_amount " +
            "union " +
            "select min(rev), min(revtype), invoiceAud.id id, invoiceAud.status, cast(i.no as varchar) code, b.code billCode, b.customer_id customerId, full_name fullName, " +
            "invoiceAud.total_amount totalAmount, invoiceAud.vat_amount vatAmount, min(invoiceAud.update_time) updateTime, 2 type, " +
            "row_number() over (partition by invoiceAud.id order by min(invoiceAud.update_time) desc) rowNumber " +
            "from audit.invoice_aud invoiceAud " +
            "join invoice i on i.id = invoiceAud.id " +
            "join bill b on b.id = i.bill_id " +
            "join ep_user u on i.updater = u.id " +
            "where invoiceAud.id in :invoiceIds and (invoiceAud.error_publish is null or invoiceAud.error_publish = '') " +
            "group by invoiceAud.id, invoiceAud.status, i.no, b.code, b.customer_id, full_name, invoiceAud.total_amount, invoiceAud.vat_amount) " +
            "select rev, revtype, id, status, code, billCode, customerId, fullName, totalAmount, iif(vatAmount is null, 0, vatAmount) vatAmount, rowNumber, type, updateTime " +
            "from t " +
            "order by id desc, rowNumber desc;"
        );
        params.put("billIds", billIds);
        params.put("invoiceIds", invoiceIds);

        Query query = entityManager.createNativeQuery(strQuery + "", "ActivityHistoryResponse");
        Common.setParams(query, params);
        results = query.getResultList();
        return results;
    }

    @Override
    public BigDecimal countTotalMoneyForUpdateCard(Integer cusId, String fromDate, String toDate) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from bill b join bill_payment bp on b.id = bp.bill_id where b.customer_id = :cusId and b.status = 1 ");
        params.put("cusId", cusId);
        if (!Strings.isNullOrEmpty(fromDate)) {
            strQuery.append(" and CAST(b.bill_date AS date) >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            strQuery.append(" and CAST(b.bill_date AS date) <= :toDate ");
            params.put("toDate", toDate);
        }

        Query countQuery = entityManager.createNativeQuery(
            "select IIF(SUM(bp.amount - IIF(bp.refund IS NULL, 0, bp.refund)) IS NULL, 0, SUM(bp.amount - IIF(bp.refund IS NULL, 0, bp.refund))) " +
            strQuery
        );
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        return BigDecimal.valueOf(count.longValue());
    }

    @Override
    public Page<BillProductProcessing> getForProcessing(Integer comId, Integer type, Integer status, List<Integer> ids) {
        List<BillProductProcessing> results;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from processing_product pp join processing_request_detail prd on pp.request_detail_id = prd.id join product_product_unit ppu on prd.product_product_unit_id = ppu.id join bill b on pp.bill_id = b.id join ep_user eu on pp.creator = eu.id "
        );
        strQuery.append(" where b.com_id = :comId ");
        params.put("comId", comId);
        //        if (Objects.equals(type, ProcessingAreaConstants.Type.THEO_BAN)) {
        //            strQuery.append(" and b.delivery_type = 1 and b.area_unit_id is not null ");
        //        }
        if (Objects.equals(status, ProcessingAreaConstants.Status.PROCESSING)) {
            strQuery.append(
                " and (pp.processing_quantity > 0 or (pp.processing_quantity = 0 and (prd.ref_id in :ids or prd.id in :ids))) "
            );
            params.put("ids", ids);
        } else if (Objects.equals(status, ProcessingAreaConstants.Status.PROCESSED)) {
            strQuery.append(" and (pp.processed_quantity > 0 or (pp.processed_quantity = 0 and (prd.ref_id in :ids or prd.id in :ids))) ");
            params.put("ids", ids);
        }
        //        if (Objects.equals(type, ProcessingAreaConstants.Type.UU_TIEN)) {
        //            if (Objects.equals(status, ProcessingAreaConstants.Status.DANG_CHE_BIEN)) {
        //                strQuery.append(" and pp.processing_quantity is not null and pp.processing_quantity > 0 ");
        //            } else if (Objects.equals(status, ProcessingAreaConstants.Status.CHO_CUNG_UNG)) {
        //                strQuery.append(" and pp.processed_quantity is not null and pp.processed_quantity > 0 ");
        //            }
        //        }
        if (Objects.equals(type, ProcessingAreaConstants.Type.THEO_BAN)) {
            strQuery.append(" order by prd.position ");
        } else if (Objects.equals(type, ProcessingAreaConstants.Type.UU_TIEN)) {
            strQuery.append(" order by prd.create_time ");
        } else if (Objects.equals(type, ProcessingAreaConstants.Type.THEO_MON)) {
            strQuery.append(" order by prd.product_name ");
        }
        Query query = entityManager.createNativeQuery(
            "select " +
            "pp.id, " +
            "prd.product_product_unit_id productProductUnitId, " +
            "prd.product_name productName, " +
            "ppu.unit_name unitName, " +
            "pp.notified_quantity notifiedQuantity, " +
            "pp.processing_quantity processingQuantity, " +
            "pp.processed_quantity processedQuantity, " +
            "pp.canceled_quantity canceledQuantity, " +
            "pp.is_topping isTopping, " +
            "pp.bill_id billId, " +
            "b.code billCode, " +
            "pp.ref_id refId, " +
            "eu.full_name creatorName, " +
            "b.area_unit_id areaUnitId, " +
            "b.area_unit_name areaUnitName, " +
            "b.area_name areaName, " +
            "pp.update_time updateTime, " +
            "pp.create_time createTime" +
            strQuery,
            "BillProcessingType"
        );
        Common.setParams(query, params);
        results = query.getResultList();
        return new PageImpl<>(results);
    }

    @Override
    public Page<BillProductProcessing> getDeleteRequest(Integer comId) {
        List<BillProductProcessing> results;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from processing_request_detail prd join processing_request_detail prd2 on prd.ref_id = prd2.id join processing_product pp on prd2.id = pp.request_detail_id join bill b on pp.bill_id = b.id join ep_user eu on prd.creator = eu.id "
        );
        strQuery.append(" where b.com_id = :comId and prd.ref_id is not null and prd.is_topping = 0 ");
        params.put("comId", comId);
        Query query = entityManager.createNativeQuery(
            "select pp.id, 0 - prd.quantity quantity, eu.full_name creatorName, prd.create_time createTime" + strQuery,
            "BillProcessingDelete"
        );
        Common.setParams(query, params);
        results = query.getResultList();
        return new PageImpl<>(results);
    }

    @Override
    public BillStatsResult getBillStats(Integer comId, String fromDate, String toDate) {
        List<BillStatsResult> results;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from bill b where b.com_id = :com_id ");
        params.put("com_id", comId);
        Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "b.bill_date", "bill");
        Query query = entityManager.createNativeQuery(
            "select sum(iif(b.status = 0, 1, 0)) processingCount, count(b.id) allCount " + strQuery,
            "BillStatsResult"
        );
        Common.setParams(query, params);
        results = query.getResultList();

        if (!results.isEmpty()) {
            return results.get(0);
        }
        return new BillStatsResult();
    }
}
