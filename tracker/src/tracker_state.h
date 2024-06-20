#ifndef TRACKER_STATE_H
#define TRACKER_STATE_H

#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdbool.h>
#include <stdlib.h>
#include <stdlib.h>
#include <sys/types.h>

#include "sys/queue.h"
#include "config.h"
#include "sys/queue.h"
#include "file.h"
#include "list.h"
#include "client.h"
#include "message_handler.h"

void tracker_init_lists();

client_t* tracker_get_client(int fd);

void tracker_update_file(char* key, client_t* client, char* provider_type);

client_t * tracker_register_client(int fd, char* address, char* port_str);

void tracker_register_file(client_t * new_client, char* filename, int length, int pieceSize, char* key, char * provider_type);


bool tracker_file_exists(char *key, client_t* client, char* provider_type);

void tracker_search_files_with_criterion(char* name, LinkedList* result);

void tracker_provide_peers_with_file(char* key, LinkedList* result);


#endif