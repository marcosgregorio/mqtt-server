package com.app;

import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttException;

public class App {

    public static void main(String[] args) throws MqttException {
        DatabaseConnection conn = new DatabaseConnection();
        Messenger messanger = new Messenger();
        String option;
        Scanner scan = new Scanner(System.in);

        messanger.getToken().waitForCompletion();

        do {
            System.out.println("Escolha uma opção!");
            printOptions();
            option = scan.nextLine();
            switch (option) {
                case "1":
                    messanger.askPermissionToChat();
                    break;
                case "2":
                    messanger.subscribeToSpecifiedTopic();
                    break;
                case "3":
                    messanger.sendMessageToSpecifiedTopic();
                case "9":
                    System.out.println("Fechando aplicação...");
                    break;
                default:
                    break;
            }
        } while (option != "9");
        scan.close();
    }

    public static void printOptions() {
        System.out.println("1 - Solicitar uma conversa com um novo usuário");
        System.out.println("2 - Inscrever-se em um tópico");
        System.out.println("3 - Enviar mensagem para um tópico");
    }

    public static void submitMessageOneToOne(Messenger messanger) {

    }

    // static class MyRunnable implements Runnable {
    //     private Messanger messanger;

    //     public MyRunnable(Messanger messanger) {
    //         this.messanger = messanger;
    //     }

    //     @Override
    //     public void run() {
    //         try {
    //             messanger.submitMessageOneToOne();
    //         } catch (MqttException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }
}
