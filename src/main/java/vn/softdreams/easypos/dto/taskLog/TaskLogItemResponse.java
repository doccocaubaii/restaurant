package vn.softdreams.easypos.dto.taskLog;

public class TaskLogItemResponse {

    private Integer id;
    private Integer comId;
    private String companyName;
    private String type;
    private String content;
    private Integer status;
    private String errorMessage;
    private String createTime;

    public TaskLogItemResponse() {}

    public TaskLogItemResponse(
        Integer id,
        Integer comId,
        String companyName,
        String type,
        String content,
        Integer status,
        String errorMessage,
        String createTime
    ) {
        this.id = id;
        this.comId = comId;
        this.companyName = companyName;
        this.type = type;
        this.content = content;
        this.status = status;
        this.errorMessage = errorMessage;
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
