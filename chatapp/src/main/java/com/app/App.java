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
            System.out.println("Escolha uma opção!");
            printOptions();
            option = scan.nextLine();
            switch (option) {
                case "1":
                    messanger.askPermissionToChat();
                    // System.in.close();
                    // Ver de utilizar funções do sistema operacional
                    // e utlizar threds.
                    
                    // try {
                        
                    //     MyRunnable runnable = new MyRunnable(messanger); 
                    //     Thread thread = new Thread(runnable);
                    //     thread.start();
                        
                    // } catch (Exception e) {
                    //     throw new Exception("Ocorreu um erro na thred de solicitação" + e.getMessage());
                    // }
                    break;
                case "2":
                    messanger.subscribeToSpecifiedTopic();
                    break;
                case "3":
                    messanger.sendMessageToSpecifiedTopic();
                case "4":
                    messanger.showPendentSessions(scan);
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
        System.out.println("4 - Sessões pendentes");
    }

    public static void submitMessageOneToOne(Messenger messanger) {

    }

    static class MyRunnable implements Runnable {
        private Messenger messanger;

        public MyRunnable(Messenger messanger) {
            this.messanger = messanger;
        }

        @Override
        public void run() {
            try {
                messanger.askPermissionToChat();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    } 
}
