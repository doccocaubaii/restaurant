package vn.softdreams.easypos.dto.card;

import java.io.Serializable;
import java.time.ZonedDateTime;

public interface CardPolicyItems extends Serializable {
    Integer getId();
    Integer getUpgradeType();
    String getConditions();
    ZonedDateTime getStartTime();
}
