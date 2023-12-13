package vn.softdreams.easypos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static vn.softdreams.easypos.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.IntegrationTest;
import vn.softdreams.easypos.domain.BillPayment;
import vn.softdreams.easypos.repository.BillPaymentRepository;

/**
 * Integration tests for the {@link BillPaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillPaymentResourceIT {

    private static final Integer DEFAULT_BILL_ID = 1;
    private static final Integer UPDATED_BILL_ID = 2;

    private static final Integer DEFAULT_PAYMENT_METHOD = 1;
    private static final Integer UPDATED_PAYMENT_METHOD = 2;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_REFUND = new BigDecimal(1);
    private static final BigDecimal UPDATED_REFUND = new BigDecimal(2);

    private static final Integer DEFAULT_DEBT_TYPE = 1;
    private static final Integer UPDATED_DEBT_TYPE = 2;

    private static final BigDecimal DEFAULT_DEBT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEBT = new BigDecimal(2);

    private static final Integer DEFAULT_CREATOR = 1;
    private static final Integer UPDATED_CREATOR = 2;

    private static final Integer DEFAULT_UPDATER = 1;
    private static final Integer UPDATED_UPDATER = 2;

    private static final LocalDate DEFAULT_CREATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PAYMENT_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_TOTAL_BILL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_BILL = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/bill-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BillPaymentRepository billPaymentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillPaymentMockMvc;

    private BillPayment billPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillPayment createEntity(EntityManager em) {
        BillPayment billPayment = new BillPayment()
            .billID(DEFAULT_BILL_ID)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .amount(DEFAULT_AMOUNT)
            .refund(DEFAULT_REFUND)
            .debtType(DEFAULT_DEBT_TYPE)
            .debt(DEFAULT_DEBT)
            .creator(DEFAULT_CREATOR)
            .updater(DEFAULT_UPDATER)
            .createTime(DEFAULT_CREATE_TIME)
            .updateTime(DEFAULT_UPDATE_TIME)
            .paymentTime(DEFAULT_PAYMENT_TIME)
            .totalBill(DEFAULT_TOTAL_BILL);
        return billPayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillPayment createUpdatedEntity(EntityManager em) {
        BillPayment billPayment = new BillPayment()
            .billID(UPDATED_BILL_ID)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .amount(UPDATED_AMOUNT)
            .refund(UPDATED_REFUND)
            .debtType(UPDATED_DEBT_TYPE)
            .debt(UPDATED_DEBT)
            .creator(UPDATED_CREATOR)
            .updater(UPDATED_UPDATER)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .paymentTime(UPDATED_PAYMENT_TIME)
            .totalBill(UPDATED_TOTAL_BILL);
        return billPayment;
    }

    @BeforeEach
    public void initTest() {
        billPayment = createEntity(em);
    }

    @Test
    @Transactional
    void createBillPayment() throws Exception {
        int databaseSizeBeforeCreate = billPaymentRepository.findAll().size();
        // Create the BillPayment
        restBillPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billPayment)))
            .andExpect(status().isCreated());

        // Validate the BillPayment in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeCreate + 1);
        BillPayment testBillPayment = billPaymentList.get(billPaymentList.size() - 1);
        assertThat(testBillPayment.getBillID()).isEqualTo(DEFAULT_BILL_ID);
        assertThat(testBillPayment.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testBillPayment.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testBillPayment.getRefund()).isEqualByComparingTo(DEFAULT_REFUND);
        assertThat(testBillPayment.getDebtType()).isEqualTo(DEFAULT_DEBT_TYPE);
        assertThat(testBillPayment.getDebt()).isEqualByComparingTo(DEFAULT_DEBT);
        assertThat(testBillPayment.getCreator()).isEqualTo(DEFAULT_CREATOR);
        assertThat(testBillPayment.getUpdater()).isEqualTo(DEFAULT_UPDATER);
        assertThat(testBillPayment.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testBillPayment.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testBillPayment.getPaymentTime()).isEqualTo(DEFAULT_PAYMENT_TIME);
        assertThat(testBillPayment.getTotalBill()).isEqualByComparingTo(DEFAULT_TOTAL_BILL);
    }

    @Test
    @Transactional
    void createBillPaymentWithExistingId() throws Exception {
        // Create the BillPayment with an existing ID
        billPayment.setId(1L);

        int databaseSizeBeforeCreate = billPaymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billPayment)))
            .andExpect(status().isBadRequest());

        // Validate the BillPayment in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBillPayments() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList
        restBillPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].billID").value(hasItem(DEFAULT_BILL_ID)))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].refund").value(hasItem(sameNumber(DEFAULT_REFUND))))
            .andExpect(jsonPath("$.[*].debtType").value(hasItem(DEFAULT_DEBT_TYPE)))
            .andExpect(jsonPath("$.[*].debt").value(hasItem(sameNumber(DEFAULT_DEBT))))
            .andExpect(jsonPath("$.[*].creator").value(hasItem(DEFAULT_CREATOR)))
            .andExpect(jsonPath("$.[*].updater").value(hasItem(DEFAULT_UPDATER)))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].paymentTime").value(hasItem(DEFAULT_PAYMENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].totalBill").value(hasItem(sameNumber(DEFAULT_TOTAL_BILL))));
    }

    @Test
    @Transactional
    void getBillPayment() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get the billPayment
        restBillPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, billPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(billPayment.getId().intValue()))
            .andExpect(jsonPath("$.billID").value(DEFAULT_BILL_ID))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.refund").value(sameNumber(DEFAULT_REFUND)))
            .andExpect(jsonPath("$.debtType").value(DEFAULT_DEBT_TYPE))
            .andExpect(jsonPath("$.debt").value(sameNumber(DEFAULT_DEBT)))
            .andExpect(jsonPath("$.creator").value(DEFAULT_CREATOR))
            .andExpect(jsonPath("$.updater").value(DEFAULT_UPDATER))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.toString()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.paymentTime").value(DEFAULT_PAYMENT_TIME.toString()))
            .andExpect(jsonPath("$.totalBill").value(sameNumber(DEFAULT_TOTAL_BILL)));
    }

    @Test
    @Transactional
    void getNonExistingBillPayment() throws Exception {
        // Get the billPayment
        restBillPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBillPayment() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        int databaseSizeBeforeUpdate = billPaymentRepository.findAll().size();

        // Update the billPayment
        BillPayment updatedBillPayment = billPaymentRepository.findById(billPayment.getId()).get();
        // Disconnect from session so that the updates on updatedBillPayment are not directly saved in db
        em.detach(updatedBillPayment);
        updatedBillPayment
            .billID(UPDATED_BILL_ID)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .amount(UPDATED_AMOUNT)
            .refund(UPDATED_REFUND)
            .debtType(UPDATED_DEBT_TYPE)
            .debt(UPDATED_DEBT)
            .creator(UPDATED_CREATOR)
            .updater(UPDATED_UPDATER)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .paymentTime(UPDATED_PAYMENT_TIME)
            .totalBill(UPDATED_TOTAL_BILL);

        restBillPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBillPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBillPayment))
            )
            .andExpect(status().isOk());

        // Validate the BillPayment in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeUpdate);
        BillPayment testBillPayment = billPaymentList.get(billPaymentList.size() - 1);
        assertThat(testBillPayment.getBillID()).isEqualTo(UPDATED_BILL_ID);
        assertThat(testBillPayment.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testBillPayment.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testBillPayment.getRefund()).isEqualByComparingTo(UPDATED_REFUND);
        assertThat(testBillPayment.getDebtType()).isEqualTo(UPDATED_DEBT_TYPE);
        assertThat(testBillPayment.getDebt()).isEqualByComparingTo(UPDATED_DEBT);
        assertThat(testBillPayment.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testBillPayment.getUpdater()).isEqualTo(UPDATED_UPDATER);
        assertThat(testBillPayment.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testBillPayment.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testBillPayment.getPaymentTime()).isEqualTo(UPDATED_PAYMENT_TIME);
        assertThat(testBillPayment.getTotalBill()).isEqualByComparingTo(UPDATED_TOTAL_BILL);
    }

    @Test
    @Transactional
    void putNonExistingBillPayment() throws Exception {
        int databaseSizeBeforeUpdate = billPaymentRepository.findAll().size();
        billPayment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillPayment in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBillPayment() throws Exception {
        int databaseSizeBeforeUpdate = billPaymentRepository.findAll().size();
        billPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillPayment in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBillPayment() throws Exception {
        int databaseSizeBeforeUpdate = billPaymentRepository.findAll().size();
        billPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billPayment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillPayment in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillPaymentWithPatch() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        int databaseSizeBeforeUpdate = billPaymentRepository.findAll().size();

        // Update the billPayment using partial update
        BillPayment partialUpdatedBillPayment = new BillPayment();
        partialUpdatedBillPayment.setId(billPayment.getId());

        partialUpdatedBillPayment
            .billID(UPDATED_BILL_ID)
            .amount(UPDATED_AMOUNT)
            .refund(UPDATED_REFUND)
            .debtType(UPDATED_DEBT_TYPE)
            .debt(UPDATED_DEBT)
            .updater(UPDATED_UPDATER)
            .updateTime(UPDATED_UPDATE_TIME)
            .paymentTime(UPDATED_PAYMENT_TIME)
            .totalBill(UPDATED_TOTAL_BILL);

        restBillPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBillPayment))
            )
            .andExpect(status().isOk());

        // Validate the BillPayment in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeUpdate);
        BillPayment testBillPayment = billPaymentList.get(billPaymentList.size() - 1);
        assertThat(testBillPayment.getBillID()).isEqualTo(UPDATED_BILL_ID);
        assertThat(testBillPayment.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testBillPayment.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testBillPayment.getRefund()).isEqualByComparingTo(UPDATED_REFUND);
        assertThat(testBillPayment.getDebtType()).isEqualTo(UPDATED_DEBT_TYPE);
        assertThat(testBillPayment.getDebt()).isEqualByComparingTo(UPDATED_DEBT);
        assertThat(testBillPayment.getCreator()).isEqualTo(DEFAULT_CREATOR);
        assertThat(testBillPayment.getUpdater()).isEqualTo(UPDATED_UPDATER);
        assertThat(testBillPayment.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testBillPayment.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testBillPayment.getPaymentTime()).isEqualTo(UPDATED_PAYMENT_TIME);
        assertThat(testBillPayment.getTotalBill()).isEqualByComparingTo(UPDATED_TOTAL_BILL);
    }

    @Test
    @Transactional
    void fullUpdateBillPaymentWithPatch() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        int databaseSizeBeforeUpdate = billPaymentRepository.findAll().size();

        // Update the billPayment using partial update
        BillPayment partialUpdatedBillPayment = new BillPayment();
        partialUpdatedBillPayment.setId(billPayment.getId());

        partialUpdatedBillPayment
            .billID(UPDATED_BILL_ID)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .amount(UPDATED_AMOUNT)
            .refund(UPDATED_REFUND)
            .debtType(UPDATED_DEBT_TYPE)
            .debt(UPDATED_DEBT)
            .creator(UPDATED_CREATOR)
            .updater(UPDATED_UPDATER)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .paymentTime(UPDATED_PAYMENT_TIME)
            .totalBill(UPDATED_TOTAL_BILL);

        restBillPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBillPayment))
            )
            .andExpect(status().isOk());

        // Validate the BillPayment in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeUpdate);
        BillPayment testBillPayment = billPaymentList.get(billPaymentList.size() - 1);
        assertThat(testBillPayment.getBillID()).isEqualTo(UPDATED_BILL_ID);
        assertThat(testBillPayment.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testBillPayment.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testBillPayment.getRefund()).isEqualByComparingTo(UPDATED_REFUND);
        assertThat(testBillPayment.getDebtType()).isEqualTo(UPDATED_DEBT_TYPE);
        assertThat(testBillPayment.getDebt()).isEqualByComparingTo(UPDATED_DEBT);
        assertThat(testBillPayment.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testBillPayment.getUpdater()).isEqualTo(UPDATED_UPDATER);
        assertThat(testBillPayment.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testBillPayment.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testBillPayment.getPaymentTime()).isEqualTo(UPDATED_PAYMENT_TIME);
        assertThat(testBillPayment.getTotalBill()).isEqualByComparingTo(UPDATED_TOTAL_BILL);
    }

    @Test
    @Transactional
    void patchNonExistingBillPayment() throws Exception {
        int databaseSizeBeforeUpdate = billPaymentRepository.findAll().size();
        billPayment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillPayment in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBillPayment() throws Exception {
        int databaseSizeBeforeUpdate = billPaymentRepository.findAll().size();
        billPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillPayment in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBillPayment() throws Exception {
        int databaseSizeBeforeUpdate = billPaymentRepository.findAll().size();
        billPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(billPayment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillPayment in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBillPayment() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        int databaseSizeBeforeDelete = billPaymentRepository.findAll().size();

        // Delete the billPayment
        restBillPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, billPayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
