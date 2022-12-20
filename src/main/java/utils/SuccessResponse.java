package utils;

public class SuccessResponse extends Response{
    private Object data;
    public Object getData(){
        return data;
    }

    public void setData(Object o){
        this.data = o;
    }

    public SuccessResponse(Object o){
        setData(o);
    }
}

