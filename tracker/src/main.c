
#include <netinet/in.h>
#include "config.h"
#include "tracker.h"

int main(int argc, char **argv){
    const char* config_file_path;
    Config config;
    if(argc > 2){
        if(!strcmp("port",argv[1])){
            config.port = atoi(argv[2]);
        }
        else{
            printf("Usage: %s configuration file path\n", argv[0]);
            return EXIT_SUCCESS;
        }
    }
    else if( argc > 1){
        config_file_path = argv[1];
        config = read_config(config_file_path);
    }
    else{
        config_file_path = "config.ini";
        config = read_config(config_file_path);
    }
    start_server(config);

    return EXIT_SUCCESS;
}