package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.dto.employee.EmployeeResponse;
import vn.softdreams.easypos.dto.user.UserResponse;
import vn.softdreams.easypos.repository.UserRepositoryCustom;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.*;

import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.EMPLOYEE_NOT_FOUND_CODE;
import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.EMPLOYEE_NOT_FOUND_VI;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Autowired
    private EntityManager entityManager;

    @Override
    public UserResponse getUserResponse(Integer userId) {
        UserResponse userResponse = null;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" FROM ep_user ep JOIN company_user cu ON cu.user_id = ep.id WHERE ep.id = :userId ");
        params.put("userId", userId);
        Query query = entityManager.createNativeQuery(
            "SELECT cu.company_id as companyId, " +
            "ep.id, " +
            "ep.username, " +
            "ep.password, " +
            "ep.full_name as fullname, " +
            "ep.email, " +
            "ep.phone_number as phoneNumber, " +
            "ep.address, " +
            "ep.is_manager as isManager, " +
            "ep.creator, " +
            "ep.updater, " +
            "ep.create_time as createTime, " +
            "ep.update_time as updateTime " +
            strQuery,
            "UserResponse"
        );
        Common.setParams(query, params);
        List<UserResponse> userResponses = query.getResultList();
        if (userResponses.size() == 1) {
            userResponse = userResponses.get(0);
        }
        return userResponse;
    }

    @Override
    public Page<EmployeeResponse> findAllEmployeeByCompanyId(
        Integer companyId,
        Integer employeeId,
        String keyword,
        Pageable pageable,
        Boolean isCountAll,
        boolean paramCheckAll,
        List<Integer> ids
    ) {
        List<EmployeeResponse> responses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from ep_user u join company_user cu on u.id = cu.user_id " +
            "join user_role ur on u.id = ur.user_id " +
            "join company c on c.id = cu.company_id " +
            "join company_owner co on co.id = c.com_owner_id " +
            "join role r on ur.role_id = r.id " +
            "where cu.company_id = :companyId and u.status = 1 and co.owner_id <> u.id "
        );
        params.put("companyId", companyId);
        if (employeeId != null) {
            strQuery.append(" and u.id = :employeeId ");
            params.put("employeeId", employeeId);
        }
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" and u.normalized_name like :keyword ");
            params.put("keyword", "%" + Common.normalizedName(Arrays.asList(keyword)) + "%");
        }
        if (paramCheckAll) {
            if (ids != null && ids.size() > 0) {
                strQuery.append(" AND u.id not in :ids ");
                params.put("ids", ids);
            }
        } else {
            if (ids != null && ids.size() > 0) {
                strQuery.append(" AND u.id in :ids ");
                params.put("ids", ids);
            }
        }
        Number count = 0;
        if (Boolean.TRUE.equals(isCountAll)) {
            Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
            Common.setParams(countQuery, params);
            count = (Number) countQuery.getSingleResult();
        }
        strQuery.append(" order by u.full_name ");
        Query query = entityManager.createNativeQuery(
            "select u.id id, " +
            "cu.company_id comId, " +
            "u.full_name name, " +
            "r.id roleId, " +
            "r.name roleName, " +
            "u.username username, " +
            "u.email email, " +
            "u.phone_number phoneNumber, " +
            "u.create_time createTime, " +
            "u.update_time updateTime, " +
            "u.creator creator " +
            strQuery,
            "EmployeeResponse"
        );
        if (pageable == null) {
            Common.setParams(query, params);
            responses = query.getResultList();
            return new PageImpl<>(responses);
        } else {
            Common.setParamsWithPageable(query, params, pageable);
            responses = query.getResultList();
            return new PageImpl<>(responses, pageable, Boolean.TRUE.equals(isCountAll) ? count.longValue() : responses.size());
        }
    }

    @Override
    public EmployeeResponse findOneByIdAndCompanyId(Integer id, Integer companyId) {
        EmployeeResponse responses = new EmployeeResponse();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            "  from ep_user u join company_user cu on u.id = cu.user_id join user_role ur on ur.user_id = u.id and ur.com_id = cu.company_id join role r on ur.role_id = r.id where u.id = :id and cu.company_id = :companyId and u.status = 1 "
        );
        params.put("id", id);
        params.put("companyId", companyId);
        Query query = entityManager.createNativeQuery(
            "select u.id id, " +
            "cu.company_id comId, " +
            "u.full_name name, " +
            "r.id roleId, " +
            "r.name roleName, " +
            "u.username username, " +
            "u.email email, " +
            "u.phone_number phoneNumber, " +
            "u.create_time createTime, " +
            "u.update_time updateTime, " +
            "u.creator creator " +
            strQuery,
            "EmployeeResponse"
        );
        Common.setParams(query, params);
        try {
            responses = (EmployeeResponse) query.getSingleResult();
            return responses;
        } catch (NoResultException noResultException) {
            throw new BadRequestAlertException(EMPLOYEE_NOT_FOUND_VI, EMPLOYEE_NOT_FOUND_VI, EMPLOYEE_NOT_FOUND_CODE);
        }
    }

    @Override
    public String genCodeByTableName(Integer comId, String tableName) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  select count(*) from ");
        strQuery.append(tableName);
        strQuery.append("  where com_id = :comId ");
        params.put("comId", comId);
        Query countQuery = entityManager.createNativeQuery(strQuery.toString());
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        Long value = count.longValue();
        //        Trường hợp sinh KH thì mã bắt dầu từ KH3
        if (value > 0 && tableName.equals(Constants.TableName.CUSTOMER)) {
            value = value + 2;
        } else value = value + 1;
        return String.valueOf(value);
    }

    @Override
    public String getSeqByCompanyAndCode(String seqName) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT NEXT VALUE FOR ");
        strQuery.append(seqName);
        Query countQuery = entityManager.createNativeQuery(strQuery.toString());
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        log.error("USER_GET_SEQ:" + strQuery.toString());
        return String.valueOf(count.longValue());
    }

    @Override
    public ResultDTO autoGenSeqName(String seqName) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("CREATE SEQUENCE ");
        strQuery.append(seqName);
        strQuery.append(" START WITH 1 ");
        strQuery.append(" INCREMENT BY 1 ");
        Query query = entityManager.createNativeQuery(strQuery.toString());
        log.error("USER_AUTOGENSEQNAME:" + strQuery.toString());
        try {
            query.executeUpdate();
            log.error("USER_AUTOGENSEQNAME_SUCCESS:");
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS, true);
        } catch (Exception ex) {
            log.error("USER_AUTOGENSEQNAME_Error:" + strQuery.toString());
            for (StackTraceElement item : ex.getStackTrace()) {
                log.error(item.toString());
            }
            return new ResultDTO(ResultConstants.ERROR, ResultConstants.ERROR_SEQ_CREATE, false);
        }
    }
}
