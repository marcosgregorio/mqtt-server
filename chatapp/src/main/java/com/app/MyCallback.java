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
	private String message;
	private Gson gson;
	private MyMessage MyMessage;
	// private Users onlineUsers;

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
		this.message = new String(message.getPayload());
		this.gson = new Gson();
		MyMessage myMessage = gson.fromJson(this.message, MyMessage.class);
		this.messangerId = myMessage.id;

		if (myMessage.type.equals("Invite")) {
			this.handleOneToOneSolicitation();
		}

		if (myMessage.type.equals("Group")) {
			this.createGroup(myMessage.message);
		}

		if (myMessage.type.equals("Group_Solicitation")) {
			this.handleGroupSolicitation(myMessage.message);
		}

		if (myMessage.type.equals("Accepted_Group")) {
			this.handleAcceptedGroup(myMessage);
		}

		if (myMessage.type.equals("Group_Message")) {
			this.handleMessageGroup();
		}

		if (topic.endsWith(" <DISCONNECTED>")) {
			this.handleUserDisconnection();
		}

		if (myMessage.type.equals("Accepted")) {
			this.handleAcceptedSession();
		}

		if (myMessage.type.equals("Message")) {
			this.handleMessage();
		}
	}

	private void setOnlineUsers() {
		User user = new User(this.messangerId, true);
		// this.onlineUsers.users.add(user);
	}

	private void handleOneToOneSolicitation() throws MqttException {
		String sessionName;
		sessionName = this.messanger.getUserId() + "_" + this.messangerId;
		Session session = new Session(sessionName, this.messangerId);

		this.messanger.setSessions(session);

		System.out.println("Você recebeu um pedido de sessão individual.");
	}

	private void createGroup(String groupName) {
		Contact contact = new Contact(new User(this.messangerId, true));
		ArrayList<Contact> contacts = new ArrayList<>();
		contacts.add(contact);

		Group group = new Group(groupName, this.messangerId, contacts);
		this.messanger.addGroup(group);
	}

	private void handleGroupSolicitation(String index) {
		System.out.println("Alguem lhe enviou uma solicitação para entrar em um grupo.");
		int groupIndex = Integer.parseInt(index);
		Session session = new Session(messangerId, messangerId, groupIndex); 
		this.messanger.addGroupSessions(session);
	}

	private void handleAcceptedGroup(MyMessage myMessage) throws MqttException {
		this.messanger.setGroups(myMessage.groups);
		this.messanger.subscribeToTopic(myMessage.groupName + "_" + myMessage.message, 0);
	}

	private void handleMessageGroup() {
		Gson gson = new Gson();
		MyMessage myMessage = gson.fromJson(this.message, MyMessage.class);
		System.out.println(myMessage.id + ": " + myMessage.message);
	}

	private void handleUserDisconnection() {
		// tenho que pegar o id do usuario e mudar no objeto de contatos o status para
		// false!
	}

	private void handleAcceptedSession() throws MqttException {
		Contact contact = new Contact(new User(this.messangerId, true));
		this.messanger.addContacts(contact);
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
