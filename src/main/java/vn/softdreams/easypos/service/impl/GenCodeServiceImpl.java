package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.GenCode;
import vn.softdreams.easypos.repository.GenCodeRepository;
import vn.softdreams.easypos.service.GenCodeService;

import java.util.Optional;

/**
 * Service Implementation for managing {@link GenCode}.
 */
@Service
@Transactional
public class GenCodeServiceImpl implements GenCodeService {

    private final Logger log = LoggerFactory.getLogger(GenCodeServiceImpl.class);

    private final GenCodeRepository genCodeRepository;

    public GenCodeServiceImpl(GenCodeRepository genCodeRepository) {
        this.genCodeRepository = genCodeRepository;
    }

    @Override
    public GenCode save(GenCode genCode) {
        log.debug("Request to save GenCode : {}", genCode);
        return genCodeRepository.save(genCode);
    }

    @Override
    public GenCode update(GenCode genCode) {
        log.debug("Request to update GenCode : {}", genCode);
        return genCodeRepository.save(genCode);
    }

    @Override
    public Optional<GenCode> partialUpdate(GenCode genCode) {
        log.debug("Request to partially update GenCode : {}", genCode);

        return genCodeRepository
            .findById(genCode.getId())
            .map(existingGenCode -> {
                if (genCode.getCompanyId() != null) {
                    existingGenCode.setCompanyId(genCode.getCompanyId());
                }
                if (genCode.getTypeName() != null) {
                    existingGenCode.setTypeName(genCode.getTypeName());
                }
                if (genCode.getCurrentValue() != null) {
                    existingGenCode.setCurrentValue(genCode.getCurrentValue());
                }
                if (genCode.getLength() != null) {
                    existingGenCode.setLength(genCode.getLength());
                }
                if (genCode.getPrefix() != null) {
                    existingGenCode.setPrefix(genCode.getPrefix());
                }
                if (genCode.getSuffix() != null) {
                    existingGenCode.setSuffix(genCode.getSuffix());
                }

                return existingGenCode;
            })
            .map(genCodeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GenCode> findAll(Pageable pageable) {
        log.debug("Request to get all GenCodes");
        return genCodeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GenCode> findOne(Integer id) {
        log.debug("Request to get GenCode : {}", id);
        return genCodeRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete GenCode : {}", id);
        genCodeRepository.deleteById(id);
    }
}
