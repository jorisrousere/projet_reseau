#ifndef TRACKER_H
#define TRACKER_H

#include <stdlib.h>
#include <sys/types.h>

#include "sys/queue.h"
#include "config.h"
#include "sys/queue.h"
#include "file.h"
#include "list.h"
#include "client.h"
#include "message_handler.h"

#define Peer_Commands_NBR 3 

#define MAX_PEER 10 


void start_server(Config config);

#endif