package vn.softdreams.easypos.dto.queue;

public interface ObjectAsyncResponse {
    Integer getId();

    String getName();

    Integer getComId();
    // đánh type khi đồng bộ tasklog cho Customer
    // các chứng năng khác để type = null
    Integer getType();
}
