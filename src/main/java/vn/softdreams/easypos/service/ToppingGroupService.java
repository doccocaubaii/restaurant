package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.ToppingGroup;
import vn.softdreams.easypos.dto.toppingGroup.ToppingGroupUpdateRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.Optional;

/**
 * Service Interface for managing {@link ToppingGroup}.
 */
public interface ToppingGroupService {
    ResultDTO save(ToppingGroupUpdateRequest request);

    ToppingGroup update(ToppingGroup toppingGroup);

    Optional<ToppingGroup> partialUpdate(ToppingGroup toppingGroup);

    ResultDTO getAllWithPaging(Pageable pageable, Integer comId, String keyword, Boolean isCountAll);

    ResultDTO getToppingGroupDetail(Integer id);

    ResultDTO delete(Integer id);

    ResultDTO getListToppingForProduct(Integer page, Integer size, Integer id, Boolean isSingleList, String keyword);
    ResultDTO getListToppingByProductId(Integer id);
}
