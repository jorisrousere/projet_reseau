#ifndef FILE_LIST_H
#define FILE_LIST_H

#include "client.h"
#include "list.h"
#include "sys/queue.h"
#include <stdbool.h>


typedef struct file {
    char *file_name;
    unsigned int length;
    unsigned int piece_size;
    char *key;    
    LinkedList* leechers;
    LinkedList* seeders;
    SLIST_ENTRY(file) entries; // Entry for queue.h
} file_t;


void file_delete(file_t * );

bool file_has_criterion(file_t * , const char* );

file_t* file_create(char* , unsigned int , unsigned int , char* );

void file_add_seeder(file_t *, struct client *);

void file_add_leecher(file_t *, struct client *);

void file_remove_leecher(file_t *, struct client *);

const char* file_get_name(file_t* file);

unsigned int file_get_length(file_t* file);

unsigned int file_get_piece_size(file_t* file);

const char* file_get_key(file_t* file);


#endif // FILE_LIST_H
