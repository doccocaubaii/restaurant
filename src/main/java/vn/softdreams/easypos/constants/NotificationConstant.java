package vn.softdreams.easypos.constants;

public interface NotificationConstant {
    interface Type {
        Integer THONG_BAO_BEP = 0;
        Integer TAO_DON = 1;
    }

    interface Subject {
        String CHE_BIEN = "PROCESSING";
        String CUNG_UNG = "PROCESSED";
        String HUY_MON = "CANCEL";
    }

    interface Content {
        String SEND_REQUEST = "gửi yêu cầu chế biến";
        String CREATE_BILL = " bán đơn hàng ";
        String PROCESSING_REQUEST = " đã chế biến xong chờ cung ứng";
        String PROCESSED_REQUEST = " đã cung ứng";
    }
}
