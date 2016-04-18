package me.glux.omd.rest.exception;

public class InvokeBaseException extends RuntimeException {
    private static final long serialVersionUID = -6088695831724553122L;
    private int code;
    private String errorMessage;
    public InvokeBaseException(int code,String errorMessage){
        this.code=code;
        this.errorMessage=errorMessage;
    }
    public int getCode() {
        return code;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
