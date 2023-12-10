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

import com.google.gson.Gson;

final public class Messenger {

    private ArrayList<String> signedTopics = new ArrayList<>();
    private String broker = "tcp://localhost:1883";
    private MqttConnectOptions options;
    private String userId;
    private String controllId;
    private MqttAsyncClient myClient;
    private IMqttToken token;
    private ArrayList<Session> sessions = new ArrayList<>();
    private ArrayList<Session> groupSessions = new ArrayList<>();
    private ArrayList<Contact> contacts = new ArrayList<>();
    private ArrayList<Group> groups = new ArrayList<>();

    public Messenger() throws MqttException {
        this.options = new MqttConnectOptions();
        this.userId = UUID.randomUUID().toString();
        this.controllId = this.userId + "_Controll";
        this.myClient = new MqttAsyncClient(this.broker, this.userId);
        MyCallback myCallback = new MyCallback();
        this.myClient.setCallback(myCallback);

        this.token = myClient.connect();
        token.waitForCompletion();
        int qualityOfSignal = 1;
        this.myClient.subscribe(this.controllId, qualityOfSignal);
        this.myClient.subscribe("GROUP", qualityOfSignal);
        myCallback.setClient(this);
        System.out.println("Este é seu ID: \n" + userId);
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
        MyMessage myMessage = new MyMessage(topic, message, "Invite", this.getUserId());
        Gson gson = new Gson();
        String payload = gson.toJson(myMessage);
        this.sendMessage(topic, payload);
    }

    public void showPendentGroupSessions(Scanner scan) throws MqttException {

        if (this.getGroupSessions().isEmpty()) {
            System.out.println("Não há sessões para serem aceitas\n");
            return;
        }
        Session session;
        this.listGroupSessions();

        System.out.println();

        System.out.println("Selecione uma sessão");
        int selectedSession = scan.nextInt();
        scan.nextLine();
        try {
            session = this.getGroupSessions().get(selectedSession);
            this.getGroupSessions().remove(selectedSession);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Não encontrado o indice " + selectedSession);
            return;
        }
        int groupIndex = session.getGroupIndex();

        try {
            Contact contact = new Contact(new User(session.getSessionName(), true));
            this.getGroups().get(groupIndex).addContacts(contact);
        } catch (Exception e) {
            System.out.println("Não foi encontrado um grupo com esse index");
            throw e;
        }
        ArrayList<Group> groups = this.getGroups();
        String groupName = this.getGroups().get(groupIndex).getGroupName();
        String msg = this.userId;
        String topic = session.getSessionName() + "_Controll";
        MyMessage myMessage = new MyMessage(topic, msg, "Accepted_Group", this.userId, groupIndex, groups, groupName);
        Gson gson = new Gson();
        String payload = gson.toJson(myMessage);
        this.sendMessage(topic, payload);
    }

    private void listGroupSessions() {
        int i = 0;

        for (Session session : this.getGroupSessions()) {
            System.out.println("[" + i + "]" + " " + "Sessão: " + session.getSendersId());
            i++;
        }
    }

    public void subscribeToTopic(String topic, int qualityOfSignal) throws MqttException {
        this.getMyClient().subscribe(topic, qualityOfSignal);
        this.signedTopics.add(topic);
    }

    public void sendMessageToSpecifiedContact(Scanner scan) throws MqttPersistenceException, MqttException {
        Contact selectedContact;
        String message;
        int index;

        System.out.println("Qual contato deseja enviar a mensagem?\n" +
                "Contatos:");
        this.printContacts();
        index = scan.nextInt();
        scan.nextLine();

        try {
            selectedContact = this.contacts.get(index);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Não encontrado o indice " + index);
            return;
        }

        System.out.println("Digite sua mensagem: ");
        message = scan.nextLine();
        MyMessage myMessage = new MyMessage(message, "Message", this.getUserId());
        Gson gson = new Gson();
        String payload = gson.toJson(myMessage);

        System.out.println("Enviando...");

        String topicFilter = selectedContact.getName() + "_Controll";
        this.sendMessage(topicFilter, payload);
    }

    public void printSignedTopics() {
        int i = 0;

        for (String topic : this.signedTopics) {
            System.out.println("[" + i + "]" + " " + topic);
            i++;
        }
    }

    public void showPendentSessions(Scanner scan) throws MqttException {

        if (this.getSessions().isEmpty()) {
            System.out.println("Não há sessões para serem aceitas\n");
            return;
        }
        Session session = null;

        this.listSessions();

        System.out.println();

        System.out.println("Selecione uma sessão");
        int selectedSession = scan.nextInt();
        scan.nextLine();
        try {
            session = this.getSessions().get(selectedSession);
            this.getSessions().remove(selectedSession);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Não encontrado o indice " + selectedSession);
            return;
        }
        this.addContact(session.getSendersId());
        String msg = "Sessão aceita!";
        String topic = session.getSendersId() + "_Controll";
        MyMessage myMessage = new MyMessage(topic, msg, "Accepted", this.userId);
        Gson gson = new Gson();
        String payload = gson.toJson(myMessage);
        this.sendMessage(topic, payload);
    }

    private void listSessions() {
        int i = 0;

        for (Session session : this.getSessions()) {
            System.out.println("[" + i + "]" + " " + "Sessão: " + session.getSendersId());
            i++;
        }
    }

    private void addContact(String contactName) {
        Contact contact = new Contact(new User(contactName, true));
        this.addContacts(contact);
    }

    public void listOnlineUsers() {
        this.printContacts();
    }

    private void printContacts() {
        int i = 0;
        for (Contact contact : this.getContacts()) {
            System.out.println("[" + i + "]" + " " + "Nome: " + contact.getName() + " Status: " + contact.getStatus());

            i++;
        }
    }

    public void createGroup(Scanner scan) throws MqttPersistenceException, MqttException {
        String groupName, topic, payload;
        MyMessage myMessage;
        Gson gson;
        String admId = this.userId;

        System.out.println("Insira o nome do seu grupo:");
        groupName = scan.nextLine();
        topic = "GROUP";
        myMessage = new MyMessage(topic, groupName, "Group", admId);
        gson = new Gson();
        payload = gson.toJson(myMessage);

        this.sendMessage(topic, payload);
        this.myClient.subscribe(groupName + "_" + admId, 1);
    }

    public void listGroups() {
        if (this.getGroups().isEmpty()) {
            System.out.println("Não há nenhum grupo criado ainda.");
            return;
        }
        this.printGroups();
    }

    private void printGroups() {
        int i = 0;
        for (Group group : this.getGroups()) {
            System.out.println("[" + i + "]" + " " + "Nome: " + group.getGroupName());

            if (group.getContacts() == null) {
                System.out.println("Não há membros no grupo!");
                continue;
            }
            System.out.println("Adm: " + group.getContacts().get(0).getName());
            for (Contact contact : group.getContacts()) {
                System.out.println(
                        " " + "Nome: " + contact.getName() + " Status: " + contact.getStatus());
            }
            i++;
        }
    }

    public void enterInGroup(Scanner scan) throws MqttPersistenceException, MqttException {
        Group selectedGroup;
        this.listGroups();
        System.out.println("Selecione um grupo");
        int index = scan.nextInt();
        scan.nextLine();

        try {
            selectedGroup = this.getGroups().get(index);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Não foi achado um grupo para o indice passado");
            return;
        }
        String topic, payload;
        MyMessage myMessage;
        Gson gson;
        String admId = selectedGroup.getAdministrator();
        String message = Integer.toString(index);
        topic = admId + "_Controll";

        myMessage = new MyMessage(admId + "_Controll", message, "Group_Solicitation", this.userId);
        gson = new Gson();
        payload = gson.toJson(myMessage);

        this.sendMessage(topic, payload);
    }

    public void disconnectFromBroker() throws MqttPersistenceException, MqttException {
        String topic, msg;
        topic = this.getControllId() + " <DISCONNECTED>";
        msg = "O usuário" + this.getUserId() + " se desconectou";
        this.sendMessage(topic, msg);
    }

    public void sendMessageToGroup(Scanner scan) throws MqttPersistenceException, MqttException {
        Group selectedGroup;
        this.listGroups();
        System.out.println("Selecione um grupo");
        int index = scan.nextInt();
        scan.nextLine();

        try {
            selectedGroup = this.getGroups().get(index);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Não foi achado um grupo para o indice passado");
            return;
        }
        String topic, payload;
        MyMessage myMessage;
        Gson gson;
        String admId = selectedGroup.getAdministrator();
        System.out.println("Digite sua mensagem:");
        String message = scan.nextLine();
        topic = selectedGroup.getGroupName() + "_" + admId;

        myMessage = new MyMessage(topic, message, "Group_Message", this.userId);
        gson = new Gson();
        payload = gson.toJson(myMessage);

        this.sendMessage(topic, payload);
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

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public void addContacts(Contact contact) {
        this.contacts.add(contact);
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group) {
        this.groups.add(group);
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

    public ArrayList<Session> getGroupSessions() {
        return groupSessions;
    }

    public void setGroupSessions(ArrayList<Session> groupSessions) {
        this.groupSessions = groupSessions;
    }

    public void addGroupSessions(Session groupSession) {
        this.groupSessions.add(groupSession);
    }
}
