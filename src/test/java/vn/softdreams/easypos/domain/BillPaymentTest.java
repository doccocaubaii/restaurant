package vn.softdreams.easypos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vn.softdreams.easypos.web.rest.TestUtil;

class BillPaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillPayment.class);
        BillPayment billPayment1 = new BillPayment();
        billPayment1.setId(1L);
        BillPayment billPayment2 = new BillPayment();
        billPayment2.setId(billPayment1.getId());
        assertThat(billPayment1).isEqualTo(billPayment2);
        billPayment2.setId(2L);
        assertThat(billPayment1).isNotEqualTo(billPayment2);
        billPayment1.setId(null);
        assertThat(billPayment1).isNotEqualTo(billPayment2);
    }
}
