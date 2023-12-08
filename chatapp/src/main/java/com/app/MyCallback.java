package com.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;

public class MyCallback implements MqttCallback {
	private String messangerId;
	private Messenger messanger;
	private ArrayList<String> blockedIds = new ArrayList<>();
	private String message;

	public Messenger getClient() {
		return messanger;
	}

	public void setClient(Messenger messanger) {
		this.messanger = messanger;
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Conexão perdida com o broker!");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		this.messangerId = this.extractIdFromMessage(message.toString());
		this.message = new String(message.getPayload());

		if (topic.endsWith("_Controll")) {
			this.handleOneToOneSolicitacion();
		}

		if (topic.endsWith("_Group")) {
			this.handleGroupSolicitation();
		}
		
		if (topic.endsWith(" <DISCONNECTED>")) {
			this.handleUserDisconnection();
		}
		
		if (topic.endsWith("_Accepted")) {
			this.handleAcceptedSession(topic);
		}

		if (topic.endsWith("_Message")) {
			this.handleMessage();
		}
	}

	private String extractIdFromMessage(String message) {
		String id = "";
		Pattern pattern = Pattern.compile("ID ([a-fA-F0-9-]+)");
		Matcher matcher = pattern.matcher(message);
		if (matcher.find()) {
			id = matcher.group(1);
			System.out.println("ID encontrado: " + id);
		}
		return id; 
	}

	private void handleOneToOneSolicitacion() throws MqttException {
		String sessionName;
		sessionName = this.messanger.getUserId() + "_" + this.messangerId;
		Session session = new Session(sessionName, this.messangerId);
		Contact contact = new Contact(this.messangerId, true);
		
		// this.messanger.getUserId();
		this.messanger.setSessions(session);
		this.messanger.addContacts(contact);

		System.out.println("Você recebeu um pedido de sessão individual.");
	}

	// private boolean isGroupSolicitacion(String topic) {
	// 	Pattern pattern = Pattern.compile("_Group");
	// 	Matcher matcher = pattern.matcher(topic);

	// 	return matcher.find();
	// }

	private void handleGroupSolicitation() {
		String sessionName;
		sessionName = this.messanger.getUserId() + "_" + this.messangerId;
		Session session = new Session(sessionName, this.messangerId);
		Contact contact = new Contact(this.messangerId, true);
		
		// this.messanger.getUserId();
		this.messanger.setSessions(session);
		this.messanger.addContacts(contact);
	}

	private void handleUserDisconnection() {
		//tenho que pegar o id do usuario e mudar no objeto de contatos o status para false!
	}

	private void handleAcceptedSession(String topic) throws MqttException {
		Contact contact = new Contact(topic + "_Message", true);
		this.messanger.addContacts(contact);
		this.messanger.subscribeToTopic(topic + "_Message", 1);
	}

	private void handleMessage() {
		Gson gson = new Gson();
		MyMessage myMessage = gson.fromJson(this.message, MyMessage.class);
		System.out.println(myMessage.id + ": " + myMessage.message);
	}
	
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println();
	}
}
