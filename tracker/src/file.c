#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include "file.h"


void file_delete(file_t *file) {
    if (file != NULL) {
        freeList(file->leechers);
        freeList(file->seeders);
        free(file->key);
        free(file->file_name); 
        free(file);
    }
}

bool file_has_criterion(file_t *file, const char *file_name) {
    return (strcmp(file->file_name, file_name) == 0);
}

 
file_t *file_create(char *file_name, unsigned int length, unsigned int piece_size, char *key) {
    file_t *file = (file_t *)malloc(sizeof(file_t));
    file->file_name = (char *)malloc(strlen(file_name) + 1); 
    strcpy(file->file_name, file_name);
    file->length = length;
    file->piece_size = piece_size;
    file->key = (char *)malloc(strlen(key) + 1);
    strcpy(file->key, key);
    file->seeders = createLinkedList();
    file->leechers = createLinkedList();

    return file;
}

bool client_exists(LinkedList* list, struct client* client) {
    Node* current = list->head;
    while (current != NULL) {
        if (current->data == client) {
            return true;
        }
        current = current->next;
    }
    return false;
}
void file_add_leecher(file_t *file, struct client *leecher) {
    if (file != NULL && leecher != NULL) {
        if (!client_exists(file->leechers, leecher)) {
            insert(file->leechers, leecher);
        }
    }
}


void file_add_seeder(file_t *file, struct client *peer) {
    if (file != NULL && peer != NULL) {
        if (!client_exists(file->seeders, peer)) {
            insert(file->seeders, peer);
        }
    }
}

void file_remove_leecher(file_t *file, struct client *leecher) {
    if (file != NULL && leecher != NULL) {
        removeNode(file->leechers, leecher);
    }
}

const char* file_get_name(file_t* file) {
    return file->file_name;
}

unsigned int file_get_length(file_t* file) {
    return file->length;
}

unsigned int file_get_piece_size(file_t* file) {
    return file->piece_size;
}

const char* file_get_key(file_t* file) {
    return file->key;
}