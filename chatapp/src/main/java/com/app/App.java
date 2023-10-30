package com.app;

import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws MqttException {
        Messanger messanger = new Messanger();
        int option;
        Scanner scan = new Scanner(System.in);

        messanger.getToken().waitForCompletion();
        // messanger.getMyClient().subscribe("teste", 2);
        // messanger.getMyClient().subscribe("teste2", 0);

        do {
            System.out.println("Escolha uma opção!");
            printOptions();
            option = scan.nextInt();
            scan.nextLine();
            switch (option) {
                case 1:
                    messanger.askPermissionToChat(scan);
                    break;
                case 2:
                    messanger.subscribeToSpecifiedTopic(scan);
                    break;
                case 3:
                    messanger.sendMessageToSpecifiedTopic(scan);
                case 9:
                    System.out.println("Fechando aplicação...");
                    break;
                default:
                    break;
            }
        } while (option != 9);
        scan.close();
    }

    public static void printOptions() {
        System.out.println("1 - Solicitar uma conversa com um novo usuário");
        System.out.println("2 - Inscrever-se em um tópico");
    }

    public static void submitMessageOneToOne(Messanger messanger) {

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
