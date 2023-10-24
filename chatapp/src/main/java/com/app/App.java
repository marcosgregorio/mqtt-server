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

        System.out.println("Passei");

        messanger.getToken().waitForCompletion();
        messanger.getMyClient().subscribe("teste", 2);
        messanger.getMyClient().subscribe("teste2", 0);
        String mensagem = "Testando o broooookeeeeeeeeer";

        messanger.sendMessage("teste", mensagem);

        System.out.println("Teste");
        // System.out.println(options.setPassword(nu););
        do {
            System.out.println("Escolha uma opção!");
            printOptions();
            System.out.println("Entredaaaaaaaaaaa");
            option = scan.nextInt();
            scan.nextLine();
            System.out.println("passei");
            switch (option) {
                case 1:
                    System.out.println("Entreeeei");
                    messanger.submitMessageOneToOne(scan);
                    // MyRunnable myRunnable = new MyRunnable(messanger);
                    // Thread thread = new Thread(myRunnable);
                    // thread.start();
                    // try {
                    //     thread.join(); // Aguarda até que a thread termine
                    // } catch (InterruptedException e) {
                    //     e.printStackTrace();
                    // }
                    break;

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
        System.out.println("1 - Enviar mensagem");
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
