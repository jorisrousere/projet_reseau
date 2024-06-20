#include <stdlib.h>
#include <string.h>

#include "client.h"

client_t* client_create(int fd, const char *address) {
    client_t* client = malloc(sizeof(client_t));

    client->address = malloc(strlen(address) + 1);
    strcpy(client->address, address);
    
    client->fd = fd;
    return client;
}
void client_add_port(client_t* client, const char* port) {
    client->port = malloc(strlen(port) + 1);
    strcpy(client->port, port);
}
const char* client_get_address(client_t* client) {
    return client->address;
}

const char* client_get_port(client_t* client) {
    return client->port;
}

void client_delete(client_t* client) {
    free(client->address);
    free(client->port);
    free(client);
}

