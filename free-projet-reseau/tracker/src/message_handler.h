#ifndef MESSAGE_HANDLER_H
#define MESSAGE_HANDLER_H


#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include"tracker_state.h"

#define MAX_STR_LEN 256
#define MAX_FILES 10


void init_lists();

void handle_message(char* message, char* response, int fd, char* addr);

#endif