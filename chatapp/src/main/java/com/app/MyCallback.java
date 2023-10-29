package com.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MyCallback implements MqttCallback {
	private Messanger messanger;
	private String[] blockedIds;

	public Messanger getClient() {
		return messanger;
	}

	public void setClient(Messanger messanger) {
		this.messanger = messanger;
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Conexão perdida com o broker!");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		if (this.isOneToOneSolicitacion(topic)) {
			this.handleMessageOneToOne(message.toString());
		}
		System.out.println(topic + "  " + message.toString());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println();
	}

	private boolean isOneToOneSolicitacion(String topic) {
		Pattern pattern = Pattern.compile("_Controll");
		Matcher matcher = pattern.matcher(topic);

		return matcher.find();
	}

	private void handleMessageOneToOne(String message) {
		String menssangerId = this.extractIdFromMessage(message);
		Scanner scan = new Scanner(System.in);

		System.out.println("Deseja aceitar essa conexão? \n Digite um valor maior que 0 para aprovar.");
		int answer = scan.nextInt();
		if (answer > 0) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			LocalDateTime currenTime = LocalDateTime.now();
			String timeFormatted = currenTime.format(formatter);
			String newTopic = menssangerId + "_" + this.messanger.getMyClient().getClientId() + "_" + horaFormatada;

			System.out.println("Assine o topico abaixo para poder conversar com o usuario!");
			System.out.println(this.messanger.getMyClient().getClientId());
			this.messanger.getMyClient().subscribe(newTopic, 1);

		}
		scan.close();
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

}
