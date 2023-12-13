package vn.softdreams.easypos.service.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vn.softdreams.easypos.constants.BillConstants;
import vn.softdreams.easypos.constants.NotificationConstant;
import vn.softdreams.easypos.constants.ProcessingAreaConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.bill.DeleteDishRequest;
import vn.softdreams.easypos.dto.processingRequest.ChangeProcessingStatus;
import vn.softdreams.easypos.repository.BillRepository;
import vn.softdreams.easypos.repository.NotificationRepository;
import vn.softdreams.easypos.repository.ProcessingProductRepository;
import vn.softdreams.easypos.service.ProcessingRequestManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ProcessingRequestManagementServiceImpl implements ProcessingRequestManagementService {

    private final Logger log = LoggerFactory.getLogger(ProcessingAreaManagementServiceImpl.class);

    private final String ENTITY_NAME = "processing_notify";
    private final ProcessingProductRepository processingProductRepository;
    private final BillRepository billRepository;
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public ProcessingRequestManagementServiceImpl(
        ProcessingProductRepository processingProductRepository,
        BillRepository billRepository,
        NotificationRepository notificationRepository,
        UserService userService
    ) {
        this.processingProductRepository = processingProductRepository;
        this.billRepository = billRepository;
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    @Override
    public ResultDTO changeProcessingStatus(ChangeProcessingStatus req) {
        User user = userService.getUserWithAuthorities();
        Optional<Bill> billOptional = billRepository.findByIdAndComId(req.getBillId(), user.getCompanyId());
        if (billOptional.isEmpty()) {
            throw new BadRequestAlertException(ExceptionConstants.BILL_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.BILL_NOT_FOUND);
        }
        if (Objects.equals(req.getStatus(), ProcessingAreaConstants.Status.PROCESSING)) {
            // Ưu tiên
            if (req.getType().equals(ProcessingAreaConstants.Type.UU_TIEN) || req.getType().equals(ProcessingAreaConstants.Type.THEO_BAN)) {
                if (req.getDoneWithTable() != null && req.getDoneWithTable()) {
                    List<ProcessingProduct> processingProducts = processingProductRepository.findAllByBillId(req.getBillId());
                    for (ProcessingProduct processingProduct : processingProducts) {
                        processingProduct.setProcessedQuantity(
                            processingProduct.getProcessedQuantity().add(processingProduct.getProcessingQuantity())
                        );
                        processingProduct.setProcessingQuantity(BigDecimal.ZERO);
                    }
                    notificationRepository.save(
                        createTableProcessingNotification(billOptional.get(), billOptional.get().getAreaName(), user)
                    );
                } else {
                    if (req.getId() == null) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.PROCESSING_REQUEST_NOT_FOUND_VI,
                            ENTITY_NAME,
                            ExceptionConstants.PROCESSING_REQUEST_NOT_FOUND
                        );
                    }
                    if (req.getQuantity() == null) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.PROCESSING_PRODUCT_QUANTITY_INVALID_VI,
                            ENTITY_NAME,
                            ExceptionConstants.PROCESSING_PRODUCT_QUANTITY_INVALID
                        );
                    }
                    doneWithTopping(req, billOptional.get(), user);
                }
            }
            // Theo món
            if (req.getType().equals(ProcessingAreaConstants.Type.THEO_MON)) {
                if (req.getQuantity() == null) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.PROCESSING_PRODUCT_QUANTITY_INVALID_VI,
                        ENTITY_NAME,
                        ExceptionConstants.PROCESSING_PRODUCT_QUANTITY_INVALID
                    );
                }
                if (req.getHaveTopping() != null && req.getHaveTopping()) {
                    doneWithTopping(req, billOptional.get(), user);
                } else {
                    List<ProcessingProduct> processingProducts = processingProductRepository.findByProductProductUnitIdAndRefIdOrder(
                        req.getProductProductUnitId()
                    );
                    int index = 0;
                    notificationRepository.save(
                        createProcessingNotification(
                            billOptional.get().getAreaName(),
                            user,
                            processingProducts.get(index).getProcessingRequestDetail().getProductName(),
                            req.getQuantity(),
                            billOptional.get().getId()
                        )
                    );
                    while (req.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                        ProcessingProduct processingProduct = processingProducts.get(index);
                        if (req.getQuantity().compareTo(processingProduct.getProcessingQuantity()) >= 0) {
                            processingProduct.setProcessedQuantity(
                                processingProduct.getProcessedQuantity().add(processingProduct.getProcessingQuantity())
                            );
                            req.setQuantity(req.getQuantity().subtract(processingProduct.getProcessingQuantity()));
                            processingProduct.setProcessingQuantity(BigDecimal.ZERO);
                            index++;
                        } else {
                            processingProduct.setProcessingQuantity(processingProduct.getProcessingQuantity().subtract(req.getQuantity()));
                            processingProduct.setProcessedQuantity(processingProduct.getProcessedQuantity().add(req.getQuantity()));
                            req.setQuantity(BigDecimal.ZERO);
                        }
                    }
                }
            }
        }

        if (Objects.equals(req.getStatus(), ProcessingAreaConstants.Status.PROCESSED)) {
            List<ProcessingProduct> processingProducts = processingProductRepository.findForUpdateWithTopping(req.getBillId(), req.getId());
            for (ProcessingProduct processingProduct : processingProducts) {
                processingProduct.setDeliveredQuantity(processingProduct.getDeliveredQuantity().add(req.getQuantity()));
                processingProduct.setProcessedQuantity(processingProduct.getProcessedQuantity().subtract(req.getQuantity()));
                if (processingProduct.getRefId() == null) {
                    notificationRepository.save(
                        createProcessedNotification(
                            billOptional.get().getAreaName(),
                            user,
                            processingProduct.getProcessingRequestDetail().getProductName(),
                            req.getQuantity(),
                            billOptional.get().getId()
                        )
                    );
                }
            }
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_UPDATE, true);
    }

    private void doneWithTopping(ChangeProcessingStatus req, Bill bill, User user) {
        List<ProcessingProduct> processingProducts = processingProductRepository.findForUpdateWithTopping(req.getBillId(), req.getId());
        BigDecimal parentQuantity = BigDecimal.ZERO;
        for (ProcessingProduct processingProduct : processingProducts) {
            if (processingProduct.getRefId() == null) {
                parentQuantity = processingProduct.getProcessingQuantity();
                processingProduct.setProcessedQuantity(processingProduct.getProcessedQuantity().add(req.getQuantity()));
                processingProduct.setProcessingQuantity(processingProduct.getProcessingQuantity().subtract(req.getQuantity()));
                notificationRepository.save(
                    createProcessingNotification(
                        bill.getAreaName(),
                        user,
                        processingProduct.getProcessingRequestDetail().getProductName(),
                        req.getQuantity(),
                        bill.getId()
                    )
                );
            }
        }
        for (ProcessingProduct processingProduct : processingProducts) {
            if (processingProduct.getRefId() != null) {
                BigDecimal quantity = processingProduct
                    .getProcessingQuantity()
                    .multiply(req.getQuantity())
                    .divide(parentQuantity, RoundingMode.HALF_UP)
                    .setScale(6, RoundingMode.HALF_UP);
                processingProduct.setProcessedQuantity(processingProduct.getProcessedQuantity().add(quantity));
                processingProduct.setProcessingQuantity(processingProduct.getProcessingQuantity().subtract(quantity));
            }
        }
    }

    @Override
    public ResultDTO deleteDish(DeleteDishRequest request) {
        User user = userService.getUserWithAuthorities();
        Optional<Bill> billOptional = billRepository.findByIdAndComId(request.getBillId(), user.getCompanyId());
        if (billOptional.isEmpty()) {
            throw new BadRequestAlertException(ExceptionConstants.BILL_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.BILL_NOT_FOUND);
        }
        Bill bill = billOptional.get();
        if (!Objects.equals(bill.getStatus(), BillConstants.Status.BILL_DONT_COMPLETE)) {
            throw new BadRequestAlertException(
                ExceptionConstants.BILL_STATUS_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.BILL_STATUS_INVALID
            );
        }
        processingProductRepository.findForDeleteDish(request.getBillId(), request.getId());
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.DELETE_DISH_SUCCESS, true);
    }

    private static Notification createProcessingNotification(
        String areaName,
        User user,
        String productName,
        BigDecimal quantity,
        Integer billID
    ) {
        String content =
            quantity.stripTrailingZeros().toPlainString() +
            " " +
            productName +
            " " +
            (!Strings.isNullOrEmpty(areaName) ? areaName : ProcessingAreaConstants.NO_AREA) +
            NotificationConstant.Content.PROCESSING_REQUEST;
        return createNotification(areaName, user, productName, quantity, content, NotificationConstant.Subject.CHE_BIEN, billID);
    }

    private static Notification createTableProcessingNotification(Bill bill, String areaName, User user) {
        String content =
            (!Strings.isNullOrEmpty(areaName) ? areaName : (ProcessingAreaConstants.NO_AREA + " - " + bill.getCode())) +
            NotificationConstant.Content.PROCESSING_REQUEST;
        return createNotification(areaName, user, null, null, content, NotificationConstant.Subject.CHE_BIEN, bill.getId());
    }

    private static Notification createProcessedNotification(
        String areaName,
        User user,
        String productName,
        BigDecimal quantity,
        Integer billId
    ) {
        String content =
            quantity.stripTrailingZeros().toPlainString() +
            " " +
            productName +
            " " +
            (!Strings.isNullOrEmpty(areaName) ? areaName : ProcessingAreaConstants.NO_AREA) +
            NotificationConstant.Content.PROCESSED_REQUEST;
        return createNotification(areaName, user, productName, quantity, content, NotificationConstant.Subject.CUNG_UNG, billId);
    }

    private static Notification createNotification(
        String areaName,
        User user,
        String productName,
        BigDecimal quantity,
        String content,
        String subject,
        Integer billID
    ) {
        Notification notification = new Notification();
        notification.setSubject(subject);
        notification.setContent(content);
        notification.setBillId(billID);
        notification.setType(NotificationConstant.Type.THONG_BAO_BEP);
        notification.setComId(user.getCompanyId());
        notification.setIsRead(Boolean.FALSE);
        NotificationUser notificationUser = new NotificationUser();
        notificationUser.setNotification(notification);
        notificationUser.setUserId(user.getId());
        notification.setNotificationUsers(List.of(notificationUser));
        return notification;
    }

    private void doneWithTopping(ChangeProcessingStatus req, String statusName) {
        //        List<ProcessingProduct> processingProducts = processingProductRepository.findForDoneWithPagingByTable(
        //            req.getProcessingRequestId(),
        //            ProcessingAreaConstants.StatusMap.statusMap.get(req.getStatus())
        //        );
        //        Integer refId = -1;
        //        for (ProcessingProduct processingProduct : processingProducts) {
        //            if (processingProduct.getRefId() == null) {
        //                processingProduct.setStatus(statusName);
        //                refId = processingProduct.getId();
        //                break;
        //            }
        //        }
        //        if (refId != -1) {
        //            for (ProcessingProduct processingProduct : processingProducts) {
        //                if (Objects.equals(processingProduct.getRefId(), refId)) {
        //                    processingProduct.setStatus(statusName);
        //                }
        //            }
        //        }
    }
}
