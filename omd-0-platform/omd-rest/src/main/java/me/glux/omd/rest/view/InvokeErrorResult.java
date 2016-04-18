package me.glux.omd.rest.view;

public class InvokeErrorResult extends AbstractInvokeResult {
    private int code;
    private String message;
    public InvokeErrorResult(){}
    public InvokeErrorResult(int code,String message){
        this.code=code;
        this.message=message;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    @Override
    public InvokeStatus getStatus() {
        return InvokeStatus.ERROR;
    }
}
