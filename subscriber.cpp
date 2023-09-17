#include <iostream>
#include <stdlib.h>
#include <mosquitto.h>
using namespace std;

void on_connect(struct mosquitto *mosq, void *client_id, int error_code) {
  cout << "ID: " << (int * ) client_id;
  if (error_code) {
    cout << "Error with result code: " << error_code << endl;
    exit(-1);
  }
  mosquitto_subscribe(mosq, NULL, "test/t1", 0);
}

void on_message(struct mosquitto *mosq, void *message_id, const struct mosquitto_message *msg) {
  cout << "New message with topic " << msg->topic << " " << (char *) msg->payload;

}

int main () {
  int response_code, id = 12;
  bool is_clean_session = true;

  // initialize the library
  mosquitto_lib_init();
  
  struct mosquitto *mosq;

  mosq = mosquitto_new("subscriber-test", is_clean_session, &id);

  mosquitto_connect_callback_set(mosq, on_connect); 
  mosquitto_message_callback_set(mosq, on_message);

  response_code = mosquitto_connect(mosq, "localhost", 1883, 10);
  if (response_code) {
    cout << "Error with result code: " << response_code << endl;
    return -1;
  }

  mosquitto_loop_start(mosq);

  cout << "Press Enter to quit... " << endl;

  getchar();
  mosquitto_loop_stop(mosq, true);

  mosquitto_disconnect(mosq);
  mosquitto_destroy(mosq);
  mosquitto_lib_cleanup();

  return 0;
}

