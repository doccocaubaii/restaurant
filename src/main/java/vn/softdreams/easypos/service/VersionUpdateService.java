package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.easypos.domain.VersionUpdate;
import vn.softdreams.easypos.dto.versionUpdate.SaveVersionUpdateRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Service Interface for managing {@link VersionUpdate}.
 */
public interface VersionUpdateService {
    ResultDTO save(MultipartFile image, SaveVersionUpdateRequest request, HttpServletRequest httpRequest);
    ResultDTO getNotificationToday(Integer system);
    Page<VersionUpdate> getWithPaging(Pageable pageable, Integer comId, String fromDate, String toDate);
    VersionUpdate update(VersionUpdate versionUpdate);
    Optional<VersionUpdate> partialUpdate(VersionUpdate versionUpdate);
    Page<VersionUpdate> findAll(Pageable pageable);
    Optional<VersionUpdate> findOne(Integer id);
    void delete(Integer id);
    ResultDTO deleteResult(Integer id);
}
