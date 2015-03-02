package com.moreapp.demo;

public class Message {

	public String message;
	public String sender_name;
	public String send_time;
	public boolean isMine;

	public Message(String message, String sender_name, String send_time, boolean isMine) {
		super();
		this.message = message;
		this.sender_name = sender_name;
		this.send_time = send_time;
		this.isMine = isMine;
	}
}
