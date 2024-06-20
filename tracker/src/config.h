#ifndef CONFIG_H
#define CONFIG_H

#include "assert.h"
#include "stdio.h"
#include "string.h"
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

typedef struct {
    int port;
} Config;

Config read_config(const char *file_path);

#endif 