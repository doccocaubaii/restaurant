package vn.softdreams.easypos.constants;

import java.util.List;

public class CardPolicyConstants {

    public interface UpgradeType {
        Integer TOTAL_SPENDING = 1;
        Integer TOTAL_DEPOSIT = 2;
    }

    public interface Condition {
        String DESC = "Chính sách cho %s";
    }

    public interface Status {
        Integer NOT_RUN = 0;
        Integer RUNNING = 1;
        List<Integer> STATUS_AVAILABLE = List.of(NOT_RUN, RUNNING);
    }
}
