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
		System.out.println(topic + "  " + message.toString());
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
		System.out.println("aaaaaaaaaaaaaaaaaaa");
		Scanner scan = new Scanner(System.in);

		System.out.println(" Deseja aceitar essa conexão?\n" +
				" Digite um valor maior que 0 para aprovar.\n" +
				" Digite 9 para bloquer o atual mensageiro");
		int answer = scan.nextInt();

		boolean blockUser = answer == 9;
		boolean acceptConnection = answer > 0;
		if (blockUser) {
			this.blockMessanger();
		} else if (acceptConnection) {
			this.createNewTopicWithMessanger();
		} else {
			System.out.println("Conexão recusada!");
		}
		scan.close();
	}

	private void createNewTopicWithMessanger() throws MqttException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime currenTime = LocalDateTime.now();
		String timeFormatted = currenTime.format(formatter);
		String newTopic = this.messangerId + 
			"_" + this.messanger.getMyClient().getClientId() + 
			"_" + timeFormatted;

		System.out.println("Assine o topico abaixo para poder conversar com o usuario!");
		System.out.println(newTopic);
		this.messanger.subscribeToTopic(newTopic, 1);
	}

	private void blockMessanger() {
		System.out.println("Usuario " + this.messangerId + " foi bloqueado!");
		this.blockedIds.add(this.messangerId);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println();
	}
}
