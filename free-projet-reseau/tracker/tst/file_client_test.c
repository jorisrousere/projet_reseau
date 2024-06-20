#include "test.h"
#include <assert.h>


int file_client_test_create() {
    file_t *file = file_create("test_file.txt", 100, 10, "abc123");
    int result = 0;
    if (strcmp(file->file_name, "test_file.txt") != 0) {
        result = 1;
    }
    if (file->length != 100) {
        result = 1;
    }
    if (file->piece_size != 10) {
        result = 1;
    }
    if (strcmp(file->key, "abc123") != 0) {
        result = 1;
    }
    if (file->seeders->head != NULL) {
        result = 1;
    }
    if (file->leechers->head != NULL) {
        result = 1;
    }
    file_delete(file);
    return result;
}

int file_client_test_has_criterion() {
    file_t *file = file_create("test_file.txt", 100, 10, "abc123");
    int result = 0;
    if (!file_has_criterion(file, "test_file.txt")) {
        result = 1;
    }
    if (file_has_criterion(file, "another_file.txt")) {
        result = 1;
    }
    file_delete(file);
    return result;
}

int file_client_test_add_leecher() {
    file_t *file = file_create("test_file.txt", 100, 10, "abc123");
    struct client *leecher1 = client_create(2, "5000");
    struct client *leecher2 = client_create(3, "5001");
    file_add_leecher(file, leecher1);
    file_add_leecher(file, leecher2);
    int result = 0;
    if (file->leechers->head->data != leecher1) {
        result = 1;
    }
    if (file->leechers->head->next->data != leecher2) {
        result = 1;
    }
    file_delete(file);
    return result;
}

int file_client_test_add_seeder() {
    file_t *file = file_create("test_file.txt", 100, 10, "abc123");
    struct client *seeder1 = client_create(1,"127.0.0.1");
    struct client *seeder2 = client_create(2,"127.0.0.1");
    file_add_seeder(file, seeder1);
    file_add_seeder(file, seeder2);
    int result = 0;
    if (file->seeders->head->data != seeder1) {
        result = 1;
    }
    if (file->seeders->head->next->data != seeder2) {
        result = 1;
    }
    file_delete(file);
    return result;
}

int file_client_test_remove_leecher() {
    file_t *file = file_create("test_file.txt", 100, 10, "abc123");
    struct client *leecher1 = client_create(1,"127.0.0.1");
    struct client *leecher2 = client_create(2,"127.0.0.1");
    file_add_leecher(file, leecher1);
    file_add_leecher(file, leecher2);
    file_remove_leecher(file, leecher1);
    int result = 0;
    if (file->leechers->head->data != leecher2) {
        result = 1;
    }
    file_delete(file);
    return result;
}

int file_client_test_get_name() {
    file_t *file = file_create("test_file.txt", 100, 10, "abc123");
    int result = 0;
    if (strcmp(file_get_name(file), "test_file.txt") != 0) {
        result = 1;
    }
    file_delete(file);
    return result;
}

int file_client_test_get_length() {
    file_t *file = file_create("test_file.txt", 100, 10, "abc123");
    int result = 0;
    if (file_get_length(file) != 100) {
        result = 1;
    }
    file_delete(file);
    return result;
}

int file_client_test_get_piece_size() {
    file_t *file = file_create("test_file.txt", 100, 10, "abc123");
    int result = 0;
    if (file_get_piece_size(file) != 10) {
        result = 1;
    }
    file_delete(file);
    return result;
}

int file_client_test_get_key() {
    file_t *file = file_create("test_file.txt", 100, 10, "abc123");
    int result = 0;
    if (strcmp(file_get_key(file), "abc123") != 0) {
        result = 1;
    }
    file_delete(file);
    return result;
}
int file_client_test_functions() {  
    client_t *client = client_create(1,"127.0.0.1");
    client_add_port(client,"5000");
    int result = 0;

    if (strcmp(client_get_address(client), "127.0.0.1") != 0) {
        result = 1;
    }

    if (strcmp(client_get_port(client), "5000") != 0) {
        result = 1;
    }

    client_delete(client);
    return result;
}