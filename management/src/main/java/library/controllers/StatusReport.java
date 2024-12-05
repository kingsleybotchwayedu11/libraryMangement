
package library.controllers;

public class  StatusReport {

    private boolean opeartionStatus; //tracks operation success
    private String message; // message for the cause
    private Object operationEntity; // the entity of the operation

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