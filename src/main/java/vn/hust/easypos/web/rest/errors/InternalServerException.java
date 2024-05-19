package vn.hust.easypos.web.rest.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class InternalServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;

    public InternalServerException(String defaultMessage, String entityName, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
    }

    public InternalServerException(String defaultMessage, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, null, errorKey);
    }

    public InternalServerException(URI type, String defaultMessage, String entityName, String errorKey) {
        super( defaultMessage);
        this.entityName = entityName;
        this.errorKey = errorKey;
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
