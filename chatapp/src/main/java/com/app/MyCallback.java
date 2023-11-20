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

public class MyCallback implements MqttCallback {
	private String messangerId;
	private Messenger messanger;
	private ArrayList<String> blockedIds = new ArrayList<>();

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

		if (this.isOneToOneSolicitacion(topic)) {
			this.handleMessageOneToOne();
		}
		
		if (this.isDisconnectedMessage(topic)) {
			this.handleUserDisconnection();
		}
		
		if (this.isAcceptedSession(topic)) {
			this.handleAcceptedSession();
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

	private boolean isOneToOneSolicitacion(String topic) {
		Pattern pattern = Pattern.compile("_Controll");
		Matcher matcher = pattern.matcher(topic);

		return matcher.find();
	}

	private void handleMessageOneToOne() throws MqttException {
		String sessionName;
		sessionName = this.messanger.getUserId() + "_" + this.messangerId;
		Session session = new Session(sessionName, this.messangerId);
		Contact contact = new Contact(this.messangerId, true);
		
		// this.messanger.getUserId();
		this.messanger.setSessions(session);
		this.messanger.addContacts(contact);

		System.out.println("Você recebeu um pedido de sessão individual.");
	}

	private boolean isDisconnectedMessage(String topic) {
		Pattern pattern = Pattern.compile(" <DISCONNECTED>");
		Matcher matcher = pattern.matcher(topic);

		return matcher.find();
	}

	private void handleUserDisconnection() {
		//tenho que pegar o id do usuario e mudar no objeto de contatos o status para false!
	}

	private boolean isAcceptedSession(String topic) {
		Pattern pattern = Pattern.compile("_Accepted");
		Matcher matcher = pattern.matcher(topic);

		return matcher.find();
	}

	private void handleAcceptedSession() {
		Contact contact = new Contact(this.messangerId, true);
		this.messanger.addContacts(contact);
	}
	
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println();
	}
}
