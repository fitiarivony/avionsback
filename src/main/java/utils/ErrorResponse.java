package utils;

public class ErrorResponse extends Response{
	private String status;
	private String message;
	public void setStatus(String status) {
		this.status = status;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public String getMessage() {
		return message;
	}
	public ErrorResponse(String status , String message){
		setStatus(status);
		setMessage(message);
	}
}
