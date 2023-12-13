package vn.softdreams.easypos.repository;

import vn.softdreams.easypos.dto.customerCard.CustomerCardInformation;

import java.util.List;

public interface CustomerCardRepositoryCustom {
    List<CustomerCardInformation> getAllCardByCustomerIds(Integer comId, List<Integer> customerIds);
}
