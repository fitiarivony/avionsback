package exception;

public class OutOfConnectionException extends Exception{
	int code;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public OutOfConnectionException(String message,int code) {
		super(message);
		this.setCode(code);
	}
}
