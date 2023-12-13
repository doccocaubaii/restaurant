package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.ToppingToppingGroup;
import vn.softdreams.easypos.repository.ToppingToppingGroupRepository;
import vn.softdreams.easypos.service.auto_service.ToppingToppingGroupService;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ToppingToppingGroup}.
 */
@Service
@Transactional
public class ToppingToppingGroupServiceImpl implements ToppingToppingGroupService {

    private final Logger log = LoggerFactory.getLogger(ToppingToppingGroupServiceImpl.class);

    private final ToppingToppingGroupRepository toppingToppingGroupRepository;

    public ToppingToppingGroupServiceImpl(ToppingToppingGroupRepository toppingToppingGroupRepository) {
        this.toppingToppingGroupRepository = toppingToppingGroupRepository;
    }

    @Override
    public ToppingToppingGroup save(ToppingToppingGroup toppingToppingGroup) {
        log.debug("Request to save ToppingToppingGroup : {}", toppingToppingGroup);
        return toppingToppingGroupRepository.save(toppingToppingGroup);
    }

    @Override
    public ToppingToppingGroup update(ToppingToppingGroup toppingToppingGroup) {
        log.debug("Request to update ToppingToppingGroup : {}", toppingToppingGroup);
        return toppingToppingGroupRepository.save(toppingToppingGroup);
    }

    @Override
    public Optional<ToppingToppingGroup> partialUpdate(ToppingToppingGroup toppingToppingGroup) {
        log.debug("Request to partially update ToppingToppingGroup : {}", toppingToppingGroup);

        return toppingToppingGroupRepository
            .findById(toppingToppingGroup.getId())
            .map(existingToppingToppingGroup -> {
                if (toppingToppingGroup.getToppingGroupId() != null) {
                    existingToppingToppingGroup.setToppingGroupId(toppingToppingGroup.getToppingGroupId());
                }
                if (toppingToppingGroup.getProductId() != null) {
                    existingToppingToppingGroup.setProductId(toppingToppingGroup.getProductId());
                }

                return existingToppingToppingGroup;
            })
            .map(toppingToppingGroupRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ToppingToppingGroup> findAll(Pageable pageable) {
        log.debug("Request to get all ToppingToppingGroups");
        return toppingToppingGroupRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ToppingToppingGroup> findOne(Integer id) {
        log.debug("Request to get ToppingToppingGroup : {}", id);
        return toppingToppingGroupRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete ToppingToppingGroup : {}", id);
        toppingToppingGroupRepository.deleteById(id);
    }
}
