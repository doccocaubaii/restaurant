package vn.softdreams.easypos.service.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.constants.VersionUpdateConstants;
import vn.softdreams.easypos.domain.VersionUpdate;
import vn.softdreams.easypos.dto.versionUpdate.SaveVersionUpdateRequest;
import vn.softdreams.easypos.repository.VersionUpdateRepository;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.VersionUpdateService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.util.ImagePathConstants;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link VersionUpdate}.
 */
@Service
@Transactional
public class VersionUpdateServiceImpl implements VersionUpdateService {

    private final Logger log = LoggerFactory.getLogger(VersionUpdateServiceImpl.class);
    private final String ENTITY_NAME = "version update";
    private final VersionUpdateRepository versionUpdateRepository;
    private final UserService userService;

    public VersionUpdateServiceImpl(VersionUpdateRepository versionUpdateRepository, UserService userService) {
        this.versionUpdateRepository = versionUpdateRepository;
        this.userService = userService;
    }

    @Override
    public ResultDTO save(MultipartFile images, SaveVersionUpdateRequest request, HttpServletRequest httpRequest) {
        Integer comId = userService.getCompanyId();
        String url = "";
        VersionUpdate versionUpdate;
        if (request.getId() != null) {
            Optional<VersionUpdate> versionUpdateOptional = versionUpdateRepository.findById(request.getId());
            versionUpdate = versionUpdateOptional.get();
            url = versionUpdate.getImage();
        } else {
            versionUpdate = new VersionUpdate();
        }
        if (images != null && !Strings.isNullOrEmpty(images.getOriginalFilename())) {
            url = Common.saveFile(images, Constants.IMAGE_FORMAT, ImagePathConstants.PRODUCT + comId.toString(), httpRequest);
        }
        versionUpdate.setImage(url);
        BeanUtils.copyProperties(request, versionUpdate);
        if (Strings.isNullOrEmpty(request.getStartDate())) {
            versionUpdate.setStartDate(ZonedDateTime.now());
        } else {
            versionUpdate.setStartDate(request.getStartDate());
        }
        if (Strings.isNullOrEmpty(request.getEndDate())) {
            versionUpdate.setEndDate(ZonedDateTime.now());
        } else {
            versionUpdate.setEndDate(request.getEndDate());
        }
        if (Strings.isNullOrEmpty(request.getDate())) {
            versionUpdate.setDate(ZonedDateTime.now());
        } else {
            versionUpdate.setDate(request.getDate());
        }
        if (request.getSystem() == null) {
            versionUpdate.setSystem(VersionUpdateConstants.System.BOTH);
        }
        versionUpdateRepository.save(versionUpdate);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CREATE_VERSION_SUCCESS, true);
    }

    @Override
    public ResultDTO getNotificationToday(Integer system) {
        List<Integer> systems = new ArrayList<>();
        if (system != null && !system.equals(VersionUpdateConstants.System.BOTH)) {
            systems.add(system);
        } else {
            systems = List.of(VersionUpdateConstants.System.BOTH, VersionUpdateConstants.System.WEB, VersionUpdateConstants.System.APP);
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_FORMAT);
        String formattedDate = zonedDateTime.format(formatter);
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.SUCCESS_GET_LIST,
            true,
            versionUpdateRepository.findAllForToday(userService.getCompanyId(), formattedDate, systems)
        );
    }

    @Override
    public Page<VersionUpdate> getWithPaging(Pageable pageable, Integer comId, String fromDate, String toDate) {
        return versionUpdateRepository.getWithPaging(pageable, comId, fromDate, toDate);
    }

    @Override
    public VersionUpdate update(VersionUpdate versionUpdate) {
        log.debug("Request to update VersionUpdate : {}", versionUpdate);
        return versionUpdateRepository.save(versionUpdate);
    }

    @Override
    public Optional<VersionUpdate> partialUpdate(VersionUpdate versionUpdate) {
        log.debug("Request to partially update VersionUpdate : {}", versionUpdate);

        return versionUpdateRepository
            .findById(versionUpdate.getId())
            .map(existingVersionUpdate -> {
                if (versionUpdate.getComId() != null) {
                    existingVersionUpdate.setComId(versionUpdate.getComId());
                }
                if (versionUpdate.getVersion() != null) {
                    existingVersionUpdate.setVersion(versionUpdate.getVersion());
                }
                if (versionUpdate.getDescription() != null) {
                    existingVersionUpdate.setDescription(versionUpdate.getDescription());
                }
                if (versionUpdate.getLink() != null) {
                    existingVersionUpdate.setLink(versionUpdate.getLink());
                }
                if (versionUpdate.getDate() != null) {
                    existingVersionUpdate.setDate(versionUpdate.getDate());
                }
                if (versionUpdate.getStartDate() != null) {
                    existingVersionUpdate.setStartDate(versionUpdate.getStartDate());
                }
                if (versionUpdate.getEndDate() != null) {
                    existingVersionUpdate.setEndDate(versionUpdate.getEndDate());
                }
                if (versionUpdate.getImage() != null) {
                    existingVersionUpdate.setImage(versionUpdate.getImage());
                }

                return existingVersionUpdate;
            })
            .map(versionUpdateRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VersionUpdate> findAll(Pageable pageable) {
        log.debug("Request to get all VersionUpdates");
        return versionUpdateRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VersionUpdate> findOne(Integer id) {
        log.debug("Request to get VersionUpdate : {}", id);
        return versionUpdateRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete VersionUpdate : {}", id);
        versionUpdateRepository.deleteById(id);
    }

    @Override
    public ResultDTO deleteResult(Integer id) {
        Optional<VersionUpdate> configOptional = versionUpdateRepository.findById(id);
        if (configOptional.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.CONFIG_NOT_EXISTS_VI,
                ENTITY_NAME,
                ExceptionConstants.CONFIG_NOT_EXISTS_CODE
            );
        }
        versionUpdateRepository.deleteById(id);
        log.debug(ENTITY_NAME + "Request to delete VersionUpdate: " + ResultConstants.VERSION_UPDATE_DELETE_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.VERSION_UPDATE_DELETE_SUCCESS_VI, true);
    }
}
