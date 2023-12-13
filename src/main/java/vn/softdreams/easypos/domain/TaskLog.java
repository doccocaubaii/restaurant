package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.easypos.dto.taskLog.TaskLogItemResponse;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A TaskLog.
 */
@Entity
@DynamicUpdate
@Table(name = "task_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "TaskLogItemResponseDTO",
            classes = {
                @ConstructorResult(
                    targetClass = TaskLogItemResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "com_id", type = Integer.class),
                        @ColumnResult(name = "companyName", type = String.class),
                        @ColumnResult(name = "type", type = String.class),
                        @ColumnResult(name = "content", type = String.class),
                        @ColumnResult(name = "status", type = Integer.class),
                        @ColumnResult(name = "error_message", type = String.class),
                        @ColumnResult(name = "create_time", type = String.class),
                    }
                ),
            }
        ),
    }
)
public class TaskLog extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Size(max = 50)
    @Column(name = "type")
    private String type;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private Integer status = 0;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "ref_id")
    private Integer refId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComId() {
        return this.comId;
    }

    public TaskLog comId(Integer comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getType() {
        return this.type;
    }

    public TaskLog type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public TaskLog content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return this.status;
    }

    public TaskLog status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public TaskLog errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskLog)) {
            return false;
        }
        return id != null && id.equals(((TaskLog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskLog{" +
            "id=" + getId() +
            ", comId=" + getComId() +
            ", type='" + getType() + "'" +
            ", content='" + getContent() + "'" +
            ", status=" + getStatus() +
            ", errorMessage='" + getErrorMessage() + "'" +
            "}";
    }
}
