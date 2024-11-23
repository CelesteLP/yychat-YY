package com.yychat.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data

public class Message implements Serializable,MessageType {
	static final long serialVersionUID = 1L;
	String sender;
	String receiver;
	String content;
	String messageType;
	Date sendTime;
}