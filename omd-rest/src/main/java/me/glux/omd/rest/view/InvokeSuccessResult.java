package me.glux.omd.rest.view;

public class InvokeSuccessResult  extends AbstractInvokeResult {
    private Object data;
    
    public InvokeSuccessResult(Object data){
        this.data=data;
    }
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public InvokeStatus getStatus() {
        return InvokeStatus.SUCCESS;
    }

}
