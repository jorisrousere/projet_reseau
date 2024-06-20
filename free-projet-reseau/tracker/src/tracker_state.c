#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdbool.h>
#include <stdlib.h>
#include "tracker_state.h"



SLIST_HEAD(client_list, client) clients;

SLIST_HEAD(file_list, file) files;

bool tracker_file_exists(char *key, client_t* client, char* provider_type){
    file_t *target;
    SLIST_FOREACH(target, &files, entries) {
        if (strcmp(target->key, key) == 0) {
            if (strcmp(provider_type, "seed") == 0) {
                file_add_seeder(target, client);
            } 
            else if (strcmp(provider_type, "leech") == 0) {
                file_add_leecher(target, client);
            }
            printf("Client added to existing file\n");
            return true;
        }
    }
    return false;
}

void tracker_search_files_with_criterion(char* name, LinkedList* result){
    file_t *target;
    SLIST_FOREACH(target, &files, entries) {
        if (file_has_criterion(target, name)) {
            insert(result, target);
        }
    }
}

void tracker_init_lists(){
    SLIST_INIT(&files);
    SLIST_INIT(&clients);
}

void tracker_provide_peers_with_file(char* key, LinkedList* result) {
    file_t *target;
    SLIST_FOREACH(target, &files, entries) {
        if (strcmp(target->key, key) == 0) {
            duplicateList(target->seeders, result);
            duplicateList(target->leechers, result);
        }
    }
}





client_t* tracker_get_client(int fd) {
    client_t* target;
    SLIST_FOREACH(target, &clients, entries) {
        if (target->fd == fd) {
            return target;
        }
    }
    return NULL;
}

void tracker_update_file(char* key, client_t* client, char* provider_type){
    file_t *target;
    SLIST_FOREACH(target, &files, entries) {
        if (strcmp(target->key, key) == 0) {
            if (strcmp(provider_type, "seed") == 0) {
                file_remove_leecher(target,client);
                file_add_seeder(target, client);
            } 
            else if (strcmp(provider_type, "leech") == 0) {
                file_add_leecher(target, client);
            }
            printf("File updated\n");
            break;
        }
    }
}


void tracker_register_file(client_t * new_client, char* filename, int length, int pieceSize, char* key, char * provider_type){ 
    if (!tracker_file_exists(key, new_client, provider_type)) {
        printf("New file created: %s, Length: %d, Piece Size: %d, Key: %s\n", filename, length, pieceSize, key);
        file_t* new_file = file_create(filename, length, pieceSize, key);
        if (strcmp(provider_type, "seed") == 0) {
            file_add_seeder(new_file, new_client);
        } 
        else if (strcmp(provider_type, "leech") == 0) {
            file_add_leecher(new_file, new_client);
        }
        SLIST_INSERT_HEAD(&files, new_file, entries);
    } 
}

client_t * tracker_register_client(int fd, char* address, char* port_str) {
    client_t* existing_client = NULL;
    SLIST_FOREACH(existing_client, &clients, entries) {
        if (strcmp(existing_client->address, address) == 0 && strcmp(existing_client->port, port_str) == 0) {
            existing_client->fd = fd;
            printf("Client updated: %s:%s\n", existing_client->address, existing_client->port);
            return existing_client;
        }
    }
    client_t* new_client = client_create(fd, address);
    client_add_port(new_client, port_str);
    SLIST_INSERT_HEAD(&clients, new_client, entries);
    printf("New client created: %s:%s\n", new_client->address, new_client->port);
    return new_client;
}
