package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.LoyaltyCard;
import vn.softdreams.easypos.dto.card.CarPolicySaveRequest;
import vn.softdreams.easypos.dto.loyaltyCard.SaveLoyaltyCardRequest;
import vn.softdreams.easypos.dto.loyaltyCard.SortRankCardRequest;
import vn.softdreams.easypos.dto.product.DeleteProductList;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.List;

/**
 * Service Interface for managing {@link LoyaltyCard}.
 */
public interface CardManagementService {
    ResultDTO getAllCustomerCard(Pageable pageable, String keyword, Boolean isCountAll);
    ResultDTO saveCard(SaveLoyaltyCardRequest request);
    ResultDTO getDetailById(Integer id);
    ResultDTO deleteCard(Integer id);
    ResultDTO deleteListCard(DeleteProductList req);
    ResultDTO sortCard(List<SortRankCardRequest> request);
    ResultDTO saveCardPolicy(CarPolicySaveRequest request);
    ResultDTO getAllCardPolicy(Integer comId);
    ResultDTO getAllHistory(
        Integer comId,
        Integer customerId,
        Integer type,
        boolean getWithPaging,
        Pageable pageable,
        String fromDate,
        String toDate
    );

    ResultDTO getCardDefault(Integer comId);
}
