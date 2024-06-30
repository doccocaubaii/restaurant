package vn.hust.restaurant.constants;

public interface UserConstants {
    interface Status {
        Integer ACTIVATE_EMAIL = 2;

        Integer ACTIVATE = 1;
        Integer INACTIVE = 0;
    }

    interface Manager {
        Boolean IS_MANAGER_TRUE = true;
        Boolean IS_MANAGER_FALSE = false;
    }

    String LANGKEY = "vi";
}
