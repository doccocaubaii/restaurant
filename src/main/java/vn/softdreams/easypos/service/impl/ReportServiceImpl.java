package vn.softdreams.easypos.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.Report;
import vn.softdreams.easypos.repository.ReportRepository;
import vn.softdreams.easypos.service.ReportService;

/**
 * Service Implementation for managing {@link Report}.
 */
@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Report save(Report report) {
        log.debug("Request to save Report : {}", report);
        return reportRepository.save(report);
    }

    @Override
    public Report update(Report report) {
        log.debug("Request to update Report : {}", report);
        // no save call needed as we have no fields that can be updated
        return report;
    }

    @Override
    public Optional<Report> partialUpdate(Report report) {
        log.debug("Request to partially update Report : {}", report);

        return reportRepository
            .findById(report.getId())
            .map(existingReport -> {
                return existingReport;
            }); // .map(reportRepository::save)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Report> findAll(Pageable pageable) {
        log.debug("Request to get all Reports");
        return reportRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Report> findOne(Long id) {
        log.debug("Request to get Report : {}", id);
        return reportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Report : {}", id);
        reportRepository.deleteById(id);
    }
}
