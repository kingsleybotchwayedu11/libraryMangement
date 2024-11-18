
package library.controllers;

public class  StatusReport {

    private boolean opeartionStatus;
    private String message;
    private Object operationEntity;

    public boolean getOperationStatus() {
        return opeartionStatus;
    }

    public String getMessage() {
        return message;
    }
    public Object getOperationEntity() {
        return operationEntity;
    }

    //constructor
    public StatusReport(boolean status, String message, Object object) {
        this.message = message;
        this.opeartionStatus = status;
        this.operationEntity = object;
    }

    public StatusReport(boolean status, String message) {
        this.message = message;
        this.opeartionStatus = status;
    }

}