package sg.edu.astar.ihpc.passenger.util;

public class Response {

	private int code;
	private String response;
	private Boolean status;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Response [code=" + code + ", response=" + response
				+ ", status=" + status + "]";
	}

}
