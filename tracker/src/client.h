#ifndef CLIENT_H
#define CLIENT_H

#include "sys/queue.h"

typedef struct client {
    int fd;
    char *address;
    char *port;
    SLIST_ENTRY(client)entries;
} client_t;

client_t* client_create(int fd, const char *address);
void client_add_port(client_t* client, const char* port);
void client_delete(client_t* client);
const char* client_get_address(client_t* client);
const char* client_get_port(client_t* client);

#endif // CLIENT_H
