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
    private MqttAsyncClient client;
	private String[] blockedIds;
	

	public MqttAsyncClient getClient() {
		return client;
	}

	public void setClient(MqttAsyncClient client) {
		this.client = client;
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Conexão perdida com o broker!");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		if (this.isOneToOneSolicitacion(topic)) {
			String menssangerId = this.extractIdFromMessage(message.toString());
			Scanner scan = new Scanner(System.in);

			System.out.println("Deseja aceitar essa conexão? \n Digite um valor maior que 0 para aprovar.");
			int answer = scan.nextInt();
			if (answer > 0) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
				LocalDateTime horaAtual = LocalDateTime.now();
				String horaFormatada = horaAtual.format(formatter);
				String newTopic = menssangerId + "_" + this.client.getClientId() + "_" + horaFormatada;
				
				System.out.println("Id do usuario atual!!!!!");
				System.out.println(this.client.getClientId());
				this.client.subscribe(newTopic, 1);
			}
			scan.close();
		} 
		System.out.println(topic + "  " + message.toString());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println();
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
}
