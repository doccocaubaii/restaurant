package vn.hust.easypos.service.dto;

import lombok.Data;

@Data
public class ChatDTO {
    private Integer type;
    private Integer userId;
    private String userName;
    private Integer tableId;
    private String content;
}
