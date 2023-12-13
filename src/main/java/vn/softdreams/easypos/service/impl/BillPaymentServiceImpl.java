package vn.softdreams.easypos.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.BillPayment;
import vn.softdreams.easypos.repository.BillPaymentRepository;
import vn.softdreams.easypos.service.BillPaymentService;

/**
 * Service Implementation for managing {@link BillPayment}.
 */
@Service
@Transactional
public class BillPaymentServiceImpl implements BillPaymentService {

    private final Logger log = LoggerFactory.getLogger(BillPaymentServiceImpl.class);

    private final BillPaymentRepository billPaymentRepository;

    public BillPaymentServiceImpl(BillPaymentRepository billPaymentRepository) {
        this.billPaymentRepository = billPaymentRepository;
    }

    @Override
    public BillPayment save(BillPayment billPayment) {
        log.debug("Request to save BillPayment : {}", billPayment);
        return billPaymentRepository.save(billPayment);
    }

    @Override
    public BillPayment update(BillPayment billPayment) {
        log.debug("Request to update BillPayment : {}", billPayment);
        return billPaymentRepository.save(billPayment);
    }

    @Override
    public Optional<BillPayment> partialUpdate(BillPayment billPayment) {
        log.debug("Request to partially update BillPayment : {}", billPayment);

        return billPaymentRepository
            .findById(billPayment.getId())
            .map(existingBillPayment -> {
                if (billPayment.getBillID() != null) {
                    existingBillPayment.setBillID(billPayment.getBillID());
                }
                if (billPayment.getPaymentMethod() != null) {
                    existingBillPayment.setPaymentMethod(billPayment.getPaymentMethod());
                }
                if (billPayment.getAmount() != null) {
                    existingBillPayment.setAmount(billPayment.getAmount());
                }
                if (billPayment.getRefund() != null) {
                    existingBillPayment.setRefund(billPayment.getRefund());
                }
                if (billPayment.getDebtType() != null) {
                    existingBillPayment.setDebtType(billPayment.getDebtType());
                }
                if (billPayment.getDebt() != null) {
                    existingBillPayment.setDebt(billPayment.getDebt());
                }
                if (billPayment.getCreator() != null) {
                    existingBillPayment.setCreator(billPayment.getCreator());
                }
                if (billPayment.getUpdater() != null) {
                    existingBillPayment.setUpdater(billPayment.getUpdater());
                }
                if (billPayment.getCreateTime() != null) {
                    existingBillPayment.setCreateTime(billPayment.getCreateTime());
                }
                if (billPayment.getUpdateTime() != null) {
                    existingBillPayment.setUpdateTime(billPayment.getUpdateTime());
                }
                if (billPayment.getPaymentTime() != null) {
                    existingBillPayment.setPaymentTime(billPayment.getPaymentTime());
                }
                if (billPayment.getTotalBill() != null) {
                    existingBillPayment.setTotalBill(billPayment.getTotalBill());
                }

                return existingBillPayment;
            })
            .map(billPaymentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillPayment> findAll() {
        log.debug("Request to get all BillPayments");
        return billPaymentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillPayment> findOne(Long id) {
        log.debug("Request to get BillPayment : {}", id);
        return billPaymentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillPayment : {}", id);
        billPaymentRepository.deleteById(id);
    }
}
