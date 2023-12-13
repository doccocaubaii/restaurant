package vn.softdreams.easypos.dto.printTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ContentParam {

    @JsonProperty("json-value")
    private List<JsonValue> jsonValue;

    public List<JsonValue> getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(List<JsonValue> jsonValue) {
        this.jsonValue = jsonValue;
    }

    public static class JsonValue {

        private String variable;
        private String columnName;
        private String tableName;
        private Boolean isTable;
        private Boolean isTopping;

        public String getVariable() {
            return variable;
        }

        public void setVariable(String variable) {
            this.variable = variable;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public Boolean getTable() {
            return isTable;
        }

        public void setTable(Boolean table) {
            isTable = table;
        }

        public Boolean getTopping() {
            return isTopping;
        }

        public void setTopping(Boolean topping) {
            isTopping = topping;
        }
    }
}
