package com.app;

import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttException;

public class App {

    public static void main(String[] args) throws Exception {
        DatabaseConnection conn = new DatabaseConnection();
        Messenger messanger = new Messenger();
        String option;
        Scanner scan = new Scanner(System.in);
        messanger.getToken().waitForCompletion();

        do {
            printOptions();
            option = scan.nextLine();
            switch (option) {
                case "1":
                    messanger.askPermissionToChat(scan);
                    break;
                case "2":
                    messanger.subscribeToSpecifiedTopic(scan);
                    break;
                case "3":
                    messanger.sendMessageToSpecifiedTopic(scan);
                    break;
                case "4":
                    messanger.showPendentSessions(scan);
                    break;
                case "5":
                    messanger.listOnlineUsers();
                    break;
                case "9":
                    messanger.disconnectFromBroker();
                    System.out.println("Fechando aplicação...");
                    break;
                default:
                    break;
            }
        } while (option != "9");
        scan.close();
    }

    public static void printOptions() {
        System.out.println("Escolha uma opção!");
        System.out.println("1 - Solicitar uma conversa com um novo usuário");
        System.out.println("2 - Inscrever-se em um tópico");
        System.out.println("3 - Enviar mensagem para um tópico");
        System.out.println("4 - Sessões pendentes");
    }

    public static void submitMessageOneToOne(Messenger messanger) {

    }

    static class MyRunnable implements Runnable {
        private Messenger messanger;
        private Scanner scan;

        public MyRunnable(Messenger messanger, Scanner scan) {
            this.messanger = messanger;
            this.scan = scan;
        }

        @Override
        public void run() {
            try {
                messanger.askPermissionToChat(this.scan);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    } 
}
