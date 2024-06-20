
#ifndef _TEST_H__
#define __TEST_H__
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "../src/message_handler.h"
#include "../src/list.h"
#include "../src/file.h"
#include "../src/client.h"

int test_big(void);
int test_live(void);

int list_test_createLinkedList(void);
int list_test_insert(void);
int list_test_pop(void);
int list_test_duplicateList(void);
int list_test_removeNode(void);

int file_client_test_create(void);
int file_client_test_has_criterion(void);
int file_client_test_add_leecher(void);
int file_client_test_add_seeder(void);
int file_client_test_remove_leecher(void);
int file_client_test_get_name(void);
int file_client_test_get_length(void);
int file_client_test_get_piece_size(void);
int file_client_test_get_key(void);
int file_client_test_functions(void);
#endif  // __TEST_H__