package vn.softdreams.easypos.service.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.EpMessage;
import vn.softdreams.easypos.domain.MessageIntegration;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.message.IntegrationSendMessageRequest;
import vn.softdreams.easypos.dto.message.SendMessageReponse;
import vn.softdreams.easypos.integration.easybooks88.api.dto.CommonResponse;
import vn.softdreams.easypos.repository.EpMessageRepository;
import vn.softdreams.easypos.repository.MessageIntegrationRepository;
import vn.softdreams.easypos.service.EpMessageManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.CommonIntegrated;
import vn.softdreams.easypos.util.Util;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import java.time.Instant;
import java.util.Optional;

/**
 * Service Implementation for managing {@link EpMessage}.
 */
@Service
@Transactional
public class EpMessageManagementServiceImpl implements EpMessageManagementService {

    private final Logger log = LoggerFactory.getLogger(EpMessageManagementServiceImpl.class);

    private final EpMessageRepository epMessageRepository;
    private final MessageIntegrationRepository messageIntegrationRepository;
    private final RestTemplate restTemplate;
    private final UserService userService;

    public EpMessageManagementServiceImpl(
        EpMessageRepository epMessageRepository,
        MessageIntegrationRepository messageIntegrationRepository,
        RestTemplate restTemplate,
        UserService userService
    ) {
        this.epMessageRepository = epMessageRepository;
        this.messageIntegrationRepository = messageIntegrationRepository;
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    @Override
    public EpMessage save(EpMessage epMessage) {
        User user = userService.getUserWithAuthorities();
        epMessage.setReceive(user.getPhoneNumber());
        epMessageRepository.save(epMessage);
        try {
            SendMessageReponse sendMessageReponse = CommonIntegrated.sendMessage(epMessage, restTemplate);
        } catch (Exception ex) {}
        return epMessage;
    }

    public ResultDTO sendMessage(EpMessage message) {
        if (message.getType() == null && message.getType() != 1 && message.getType() != 2) {
            throw new BadRequestAlertException(
                ExceptionConstants.TYPE_MESSAGE_VALID_VI,
                ExceptionConstants.TYPE_MESSAGE_VALID_VI,
                ExceptionConstants.TYPE_MESSAGE_VALID
            );
        }
        if (Strings.isNullOrEmpty(message.getReceive())) {
            throw new BadRequestAlertException(
                ExceptionConstants.RECEIVE_NOT_NULL_VI,
                ExceptionConstants.RECEIVE_NOT_NULL_VI,
                ExceptionConstants.TYPE_MESSAGE_VALID
            );
        }
        if (
            (message.getType() == 1 && Strings.isNullOrEmpty(message.getTextContent())) ||
            (message.getType() == 2 && !Strings.isNullOrEmpty(message.getHtmlContent()))
        ) throw new BadRequestAlertException(
            ExceptionConstants.CONTENT_NOT_NULL_vi,
            ExceptionConstants.CONTENT_NOT_NULL_vi,
            ExceptionConstants.CONTENT_NOT_NULL
        );
        //TODO: Trước mắt là lưu vào epMessage, sau đó có thể gửi lên QUEUE
        epMessageRepository.save(message);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_CREATE, true);
    }

    @Override
    public EpMessage update(EpMessage epMessage) {
        log.debug("Request to update EpMessage : {}", epMessage);
        return epMessageRepository.save(epMessage);
    }

    @Override
    public Optional<EpMessage> partialUpdate(EpMessage epMessage) {
        log.debug("Request to partially update EpMessage : {}", epMessage);

        return epMessageRepository
            .findById(epMessage.getId())
            .map(existingEpMessage -> {
                if (epMessage.getType() != null) {
                    existingEpMessage.setType(epMessage.getType());
                }
                if (epMessage.getReceive() != null) {
                    existingEpMessage.setReceive(epMessage.getReceive());
                }
                if (epMessage.getCc() != null) {
                    existingEpMessage.setCc(epMessage.getCc());
                }
                if (epMessage.getTextContent() != null) {
                    existingEpMessage.setTextContent(epMessage.getTextContent());
                }
                if (epMessage.getHtmlContent() != null) {
                    existingEpMessage.setHtmlContent(epMessage.getHtmlContent());
                }
                if (epMessage.getStatus() != null) {
                    existingEpMessage.setStatus(epMessage.getStatus());
                }
                if (epMessage.getErrorMessage() != null) {
                    existingEpMessage.setErrorMessage(epMessage.getErrorMessage());
                }

                return existingEpMessage;
            })
            .map(epMessageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EpMessage> findAll(Pageable pageable) {
        log.debug("Request to get all EpMessages");
        return epMessageRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EpMessage> findOne(Integer id) {
        log.debug("Request to get EpMessage : {}", id);
        return epMessageRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete EpMessage : {}", id);
        epMessageRepository.deleteById(id);
    }

    @Override
    public ResultDTO sendIntegrationMessage(IntegrationSendMessageRequest request) {
        if (!request.getReceive().matches(Constants.PHONE_NUMBER_REGEX)) {
            throw new BadRequestAlertException(
                ExceptionConstants.RECEIVE_INVALID_VI,
                ExceptionConstants.RECEIVE_INVALID_VI,
                ExceptionConstants.RECEIVE_INVALID
            );
        }
        long dateTime = Instant.now().toEpochMilli() / (1000 * 60);
        String hash = Util.createMd5(request.getReceive() + request.getBrandName() + "easyPos" + dateTime + Constants.HASH_INTEGRATION_MD5);
        if (!hash.equals(request.getHash())) {
            throw new BadRequestAlertException(
                ExceptionConstants.HASH_INCORRECT_VI,
                ExceptionConstants.HASH_INCORRECT_VI,
                ExceptionConstants.HASH_INCORRECT_CODE
            );
        }
        try {
            MessageIntegration messageIntegration = new MessageIntegration();
            messageIntegration.setType(Constants.MESSAGE_TYPE_SMS);
            messageIntegration.setReceive(request.getReceive());
            messageIntegration.setSubject(request.getSubject());
            messageIntegration.setTextContent(request.getTextContent());
            messageIntegration.setService(request.getService());
            messageIntegration.setBrandName(request.getBrandName());
            messageIntegrationRepository.save(messageIntegration);
            SendMessageReponse sendMessageReponse = CommonIntegrated.sendMessageIntegration(messageIntegration, restTemplate);
            messageIntegration.setStatus(sendMessageReponse.getCode() == 1 ? 1 : 2);
            if (messageIntegration.getStatus() != 1 && !Strings.isNullOrEmpty(sendMessageReponse.getMessage())) {
                messageIntegration.setErrorMessage(sendMessageReponse.getMessage());
            }
            messageIntegrationRepository.save(messageIntegration);
            CommonResponse response = new CommonResponse();
            response.setStatus(messageIntegration.getStatus());
            response.setMessage(messageIntegration.getErrorMessage());
            if (response.getStatus() == 1) {
                return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_SEND_INTEGRATION, true, response);
            }
            return new ResultDTO(ResultConstants.FAIL, ResultConstants.FAIL_SEND_INTEGRATION, false, response);
        } catch (Exception ex) {
            return new ResultDTO(ResultConstants.FAIL, ResultConstants.FAIL_SEND_INTEGRATION, false);
        }
    }
}
