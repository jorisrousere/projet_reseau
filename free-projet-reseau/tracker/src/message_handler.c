#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "client.h"
#include "file.h"
#include "message_handler.h"
#define MAX_STR_LEN 256
#define MAX_FILES 10



void init_lists(){
    tracker_init_lists();
}

int check_request(char* token, char* response_buffer, char* error_message){
    if(!token){
        strcpy(response_buffer, error_message);
        return 1;
    }
    return 0;
}

void handle_announce(char* message, char* response, int fd, char* address ){

    char port_str[6];
    int check = sscanf(message, "%*s %*s %5s", port_str);
    if(check != 1){
        check_request(NULL, response, "Error parsing announce.\n");
    }
    

    void* new_client = tracker_register_client(fd, address, port_str);

    char* token = strtok(message, " ");
    check_request(token, response, "Error parsing announce.\n");


    for (int i = 0; i < 3; i++) {
        token = strtok(NULL, " ");
        check_request(token, response, "Error parsing announce : listening port\n");
    }
    char* provider_type = NULL;
    while (token != NULL) {
        if (strcmp(token, "seed") == 0 || strcmp(token, "leech") == 0) {
            provider_type = token;
            token = strtok(NULL, " ");
            check_request(token, response, "Error parsing announce : file format\n");
        } 
        char* filename = token;
        if (filename[0] == '[') 
            filename++;
        token = strtok(NULL, " ");
        check_request(token, response, "Error parsing announce : file format\n");
        
        int length = atoi(token);
        token = strtok(NULL, " ");
        check_request(token, response, "Error parsing announce : file format\n");

        int pieceSize = atoi(token);
        token = strtok(NULL, " ");
        check_request(token, response, "Error parsing announce : file format\n");
        char* key = token;
        if(key[strlen(key) - 1] == '\n')
            key[strlen(key) - 1] = '\0';
        if(key[strlen(key) - 1] == ']')
            key[strlen(key) - 1] = '\0';
        token = strtok(NULL, " ");
        
        tracker_register_file(new_client, filename, length, pieceSize, key, provider_type);
        
    }
    strcpy(response, "ok\n");
}


void generate_list_message(char* response, LinkedList* result) {
    strcpy(response, "list [");
    file_t* actual = (file_t*) pop(result);
    while(actual){
        sprintf(response + strlen(response), "%s %u %u %s ", actual->file_name, actual->length, actual->piece_size, actual->key);
        actual = pop(result);
    }
    if(response[strlen(response) - 1] != '[')
        response[strlen(response) - 1] = '\0';
    strcat(response, "]\n");
}

void handle_look(char* message, char* response) {
    char* token = strtok(message, " ");
    check_request(token, response, "Error parsing look : command format\n");

    token = strtok(NULL, " ");
    check_request(token, response, "Error parsing look : command format\n");
    
    char* filename = token;

    //On fait sauter les crochets
    filename++;
    filename[strlen(filename) - 1] = '\0'; 

    char extracted_filename[1000]; 

    int result_count = sscanf(filename, "filename=\"%99[^\"]\"", extracted_filename);
    if(result_count != 1){
        check_request(token, response, "Error parsing look : file name\n");
    }

    printf("filename %s\n",extracted_filename);
    LinkedList* result = createLinkedList();

    tracker_search_files_with_criterion(extracted_filename,result);

    generate_list_message(response,result);
    freeList(result);
}

void generate_peers_message(char* response, const char* key, LinkedList* peers) {
    strcpy(response, "peers ");
    strcat(response, key);
    strcat(response, " [");
    client_t* peer = pop(peers);
    while (peer){
        sprintf(response + strlen(response), "%s:%s ", peer->address, peer->port);
        peer = pop(peers);
        if(!peer)response[strlen(response)-1] = '\0';
    }
    
    strcat(response, "]\n");
}

void handle_getfile(char* message, char* response){
    char* token = strtok(message, " ");
    check_request(token, response, "Error parsing getfile : command format\n");
    token = strtok(NULL, " ");
    check_request(token, response, "Error parsing getfile : key\n");
    char* key = token;
    if(key[strlen(key) - 1] == '\n')
        key[strlen(key) - 1] = '\0';
    LinkedList* peers = createLinkedList();
    tracker_provide_peers_with_file(key, peers);
    generate_peers_message(response, key, peers);
    freeList(peers);
}
 
 


void handle_update(char* message, char* response, int fd){
    client_t * client = tracker_get_client(fd);

    char* token = strtok(message, " ");
    check_request(token, response, "Error parsing update : command format\n");
    token = strtok(NULL, " ");
    check_request(token, response, "Error parsing update : command format\n");

    char* provider_type = NULL;
    while (token != NULL) {
        if (strcmp(token, "seed") == 0 || strcmp(token, "leech") == 0) {
            provider_type = token;
            token = strtok(NULL, " ");
            check_request(token, response, "Error parsing update : command format\n");
        } 
        char* key = token;
        if (key[0] == '[') 
            key++;
        if(key[strlen(key) - 1] == ']')
            key[strlen(key) - 1] = '\0';
        if(key[strlen(key) - 2] == ']')
            key[strlen(key) - 2] = '\0';
        token = strtok(NULL, " ");
        
        printf("%s : %s\n", provider_type, key);
        
        tracker_update_file(key, client, provider_type);
    }
    
    strcpy(response, "ok\n");
}

void handle_message(char* message, char* response, int fd, char* addr) {
    printf("Recieved : > %s \n\n", message);
    char first_word[50];
    int result_count = sscanf(message, "%s", first_word);
    if(result_count != 1){
        check_request(NULL, response, "Error parsing look : file name\n");
    }

    if (strcmp(first_word, "announce") == 0) {
        handle_announce(message, response, fd, addr);
    } 
    else if (strcmp(first_word, "look") == 0) {
        handle_look(message, response);
    } 
    else if (strcmp(first_word, "getfile") == 0) {
        handle_getfile(message, response);
    } 
    else if (strcmp(first_word, "update") == 0) {
        handle_update(message, response, fd);
    } 
    else {
        printf("message nul \n");
    }
    printf("Sent : < %s \n", response);
}
