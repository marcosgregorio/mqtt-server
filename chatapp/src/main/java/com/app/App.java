package com.app;

import java.util.Scanner;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws MqttException {
        
        int option;
        Scanner scan = new Scanner(System.in);
        String broker = "tcp://localhost:1883";
        String userId = UUID.randomUUID().toString();
        MqttAsyncClient myClient = new MqttAsyncClient(broker, userId);
        System.out.println("Este é seu ID: \n" + userId);


        MyCallback myCallback = new MyCallback();

        myClient.setCallback(myCallback);
        System.out.println("Passei");

        IMqttToken token = myClient.connect();

        token.waitForCompletion();
        myClient.subscribe("teste", 2);
        myClient.subscribe("teste2", 0);
        String mensagem = "Testando o broooookeeeeeeeeer";
        MqttMessage msg = new MqttMessage(mensagem.getBytes());
        myClient.publish("teste", msg);
    }

}
