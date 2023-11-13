package com.app;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

final public class Messenger {

    private ArrayList<String> signedTopics = new ArrayList<>();;
    private String broker = "tcp://localhost:1883";
    private MqttConnectOptions options;
    private String userId;
    private String controllId;
    private MqttAsyncClient myClient;
    private IMqttToken token;
    private ArrayList<Session> sessions = new ArrayList<>();
    
    public Messenger() throws MqttException {
        this.options = new MqttConnectOptions();
        this.userId = UUID.randomUUID().toString();
        this.controllId = this.userId + "_Controll";
        this.myClient = new MqttAsyncClient(this.broker, this.userId);
        MyCallback myCallback = new MyCallback();
        this.myClient.setCallback(myCallback);

        this.token = myClient.connect();
        // this.token.
        token.waitForCompletion();
        int qualityOfSignal = 1;
        this.myClient.subscribe(this.controllId, qualityOfSignal);
        myCallback.setClient(this);
        System.out.println("Este é seu ID: \n" + userId);
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public MqttConnectOptions getOptions() {
        return options;
    }

    public void setOptions(MqttConnectOptions options) {
        this.options = options;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MqttAsyncClient getMyClient() {
        return myClient;
    }

    public void setMyClient(MqttAsyncClient myClient) {
        this.myClient = myClient;
    }

    public IMqttToken getToken() {
        return token;
    }

    public void setToken(IMqttToken token) {
        this.token = token;
    }

    public void sendMessage(String topic, String message) throws MqttPersistenceException, MqttException {
        MqttMessage msg = new MqttMessage(message.getBytes());
        IMqttToken token = this.getMyClient().publish(topic, msg);
    }

    public void askPermissionToChat(Scanner scan) throws MqttPersistenceException, MqttException {
        System.out.println("Qual o ID do usuario que desejava conversar?");
        String id = scan.next();
        scan.nextLine();
        String topic = id + "_Controll";
        String message = "O usuario com o ID " + this.userId
                + " enviou uma solicitação de sessão para você.";
        this.sendMessage(topic, message);
        this.subscribeToTopic(topic, 1);
    }

    public void subscribeToSpecifiedTopic(Scanner scan) throws MqttException {
        System.out.println("Digite o tópico que deseja se inscrever");
        String newTopic = scan.nextLine();
        System.out.println("Digite a qualidade do sinal que deseja ter.");
        int qualityOfSignal = scan.nextInt();
        this.subscribeToTopic(newTopic, qualityOfSignal);
    }

    public void subscribeToTopic(String topic, int qualityOfSignal) throws MqttException {
        this.getMyClient().subscribe(topic, qualityOfSignal);
        this.signedTopics.add(topic);
    }

    public void sendMessageToSpecifiedTopic(Scanner scan) throws MqttPersistenceException, MqttException {
        String selectedTopic, message;
        int index;

        System.out.println("Qual tópico deseja enviar a mensagem?\n" +
                "Tópicos assinados:");
        this.printSignedTopics();
        index = scan.nextInt();
        scan.nextLine();

        try {
            selectedTopic = this.signedTopics.get(index);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Não encontrado o indice " + index);
            return;
        }
        
        System.out.println("Digite sua mensagem: ");
        message = scan.nextLine();
        scan.nextLine();
        System.out.println("Enviando...");

        this.sendMessage(selectedTopic, message);
    }

    public void printSignedTopics() {
        int i = 0;

        for (String topic : this.signedTopics) {
            System.out.println("[" + i + "]" + " " + topic);
            i++;
        }
    }

    public void showPendentSessions(Scanner scan) throws MqttException {
        Session session = null;
        this.listSessions();

        System.out.println();
        System.out.println("Deseja aceitar uma sessão? S/N");
        System.out.println();

        String answer = scan.nextLine();
        answer = answer.trim();
        if (isConfirmAnswer(answer)) {
            System.out.println("Selecione uma sessão");
            int selectedSession = scan.nextInt();
            scan.nextLine();
            try {
                session = this.getSessions().get(selectedSession);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Não encontrado o indice " + selectedSession);
                return;
            }
            this.subscribeToTopic(session.getSessionName(), 1);
        }
    }

    private boolean isConfirmAnswer(String answer) {
        return answer.equalsIgnoreCase("Y") || answer.equalsIgnoreCase("S");
    }

    private void listSessions() {
        if (this.getSessions().isEmpty()) {
            System.out.println("Não há nenhuma sessão");
        }
        int i = 0;

        for (Session session : this.getSessions()) {
            System.out.println("[" + i + "]" + " " + "Sessão: " + session.getSendersId());
            i++;
        }
    }


    public ArrayList<String> getSignedTopics() {
        return signedTopics;
    }

    public void setSignedTopics(ArrayList<String> signedTopics) {
        this.signedTopics = signedTopics;
    }

    public String getControllId() {
        return controllId;
    }

    public void setControllId(String controllId) {
        this.controllId = controllId;
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Session sessions) {
        this.sessions.add(sessions);
    }
}
