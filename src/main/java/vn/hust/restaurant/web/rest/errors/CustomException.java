package vn.hust.restaurant.web.rest.errors;

public class CustomException extends RuntimeException {
    private String message;

    private String code = "400";

    public CustomException(String message) {
        super(message);
        this.message = message;
    }

    public CustomException(String message, String code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
