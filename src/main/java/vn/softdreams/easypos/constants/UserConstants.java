package vn.softdreams.easypos.constants;

public interface UserConstants {
    interface Status {
        Integer ACTIVATE = 1;
        Integer INACTIVE = 0;
    }

    interface Manager {
        Boolean IS_MANAGER_TRUE = true;
        Boolean IS_MANAGER_FALSE = false;
    }

    interface Service {
        String NGP = "NGP";
        String VTE = "VTE";
        String ADMIN = "ADMIN";
    }

    String LANGKEY = "vi";
}
