#include "config.h"

Config read_config(const char *file_path){
    FILE* file = fopen(file_path, "r");
    assert(file);
    Config config;

    //Searches for integer after equals sign
    fscanf(file, "%*[^=]=%d", &config.port); 
    
    fclose(file);
    return config;
}
