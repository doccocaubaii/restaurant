package vn.hust.easypos.web.rest.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class RequestAlertException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;
    private final Object data;

    public RequestAlertException(String defaultMessage, String entityName, String errorKey, Object data) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey, data);
    }

    public RequestAlertException(URI type, String defaultMessage, String entityName, String errorKey, Object data) {
        super( defaultMessage);
        this.entityName = entityName;
        this.errorKey = errorKey;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }
}
