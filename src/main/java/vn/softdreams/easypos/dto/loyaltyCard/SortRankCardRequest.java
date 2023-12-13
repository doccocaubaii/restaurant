package vn.softdreams.easypos.dto.loyaltyCard;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;

public class SortRankCardRequest {

    @NotNull(message = ExceptionConstants.CARD_ID_NOT_NULL)
    private Integer id;

    @NotNull(message = ExceptionConstants.CARD_RANK_NOT_NULL)
    private Integer rank;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
