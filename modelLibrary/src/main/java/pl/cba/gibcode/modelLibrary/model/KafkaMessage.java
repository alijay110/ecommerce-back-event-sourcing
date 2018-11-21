package pl.cba.gibcode.modelLibrary.model;

public class KafkaMessage {
	private String message;

	public KafkaMessage(){

	}
	public KafkaMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
