package vn.softdreams.easypos.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;

public class IntegrationException extends AbstractThrowableProblem {

    private Party partyName;
    private String message;

    public IntegrationException() {}

    public IntegrationException(Party partyName, String message) {
        this.partyName = partyName;
        this.message = message;
    }

    public Party getPartyName() {
        return partyName;
    }

    public void setPartyName(Party partyName) {
        this.partyName = partyName;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum Party {
        Easybooks88("Easybooks88"),
        EasyInvoice("EasyInvoice");

        private final String name;

        Party(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
