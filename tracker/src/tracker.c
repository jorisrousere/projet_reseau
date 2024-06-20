#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdbool.h>
#include <stdlib.h>
#include "tracker.h"
#include <sys/select.h>

#define BUFFER_SIZE 1024

void exit_if(int val, const char *error_message){
    if (val) {
        perror(error_message);
        exit(EXIT_FAILURE);
    }
}

int setup_server(struct sockaddr_in* address, int portno){
    int tracker_fd = socket(AF_INET, SOCK_STREAM, 0);
    exit_if(tracker_fd == 0, "socket failed");


    address->sin_family = AF_INET;
    address->sin_addr.s_addr = INADDR_ANY;
    address->sin_port = htons(portno);

    exit_if(bind(tracker_fd, (struct sockaddr *)&address, sizeof(address)) < 0, "bind failed");

    exit_if(listen(tracker_fd, 3) < 0, "listen failed");

    printf("Tracker listening on port %d\n", portno);

    return tracker_fd;
}

void start_server(Config config) {
    int valread, portno = config.port;
    struct sockaddr_in address;
    int addrlen = sizeof(address);

    char buffer_read[BUFFER_SIZE], buffer_send[BUFFER_SIZE];


    int max_sd, sd, activity;
    fd_set readfds, bufferfds;
    int client_socket[30];
    for (int i = 0; i < 30; i++) {
        client_socket[i] = 0;
    }

    int tracker_fd = socket(AF_INET, SOCK_STREAM, 0);
    exit_if(tracker_fd == 0, "socket failed");


    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(portno);

    exit_if(bind(tracker_fd, (struct sockaddr *)&address, sizeof(address)) < 0, "bind failed");

    exit_if(listen(tracker_fd, 3) < 0, "listen failed");

    printf("Tracker listening on port %d\n", portno);


    init_lists();

    FD_ZERO(&readfds);
    FD_ZERO(&bufferfds);


    struct sockaddr_in peer_addr;
    socklen_t peer_addr_len = sizeof(peer_addr);
    char peer_ip[INET_ADDRSTRLEN];


    while (1) {
        fd_set read_fds;
        FD_ZERO(&read_fds);
        FD_SET(tracker_fd, &readfds);
        max_sd = tracker_fd;
        for (int i = 0; i < 30; i++) {
            sd = client_socket[i];
            if (sd > 0) {
                FD_SET(sd, &readfds);
            }
            if (sd > max_sd) {
                max_sd = sd;
            }
        }
        bufferfds = readfds;
        printf("\n");
        activity = select(max_sd + 1, &readfds, NULL, NULL, NULL);
        exit_if(activity < 0, "select failed");
        if (FD_ISSET(tracker_fd, &readfds)) {
            exit_if((sd = accept(tracker_fd, (struct sockaddr *)&address, (socklen_t *)&addrlen)) < 0, "accept failed");
                for (int i = 0; i < 30; i++) {
                    if (client_socket[i] == 0) {
                        client_socket[i] = sd;
                        FD_SET(sd, &readfds);
                        max_sd++;
                        break;
                    }
                }
        }
        for (int i = 0; i < 30; i++) {
            sd = client_socket[i];
            if (FD_ISSET(sd, &readfds)) {
                getpeername(sd, (struct sockaddr *)&peer_addr, &peer_addr_len);
                inet_ntop(AF_INET, &peer_addr.sin_addr, peer_ip, INET_ADDRSTRLEN);
                if ((valread = read(sd, buffer_read, BUFFER_SIZE)) == 0) {
                    printf("Host disconnected, ip %s, port %d\n", inet_ntoa(address.sin_addr), ntohs(address.sin_port));
                    close(sd);
                    client_socket[i] = 0;
                }
                else {
                    buffer_read[valread] = '\0';
                    handle_message(buffer_read, buffer_send, sd, peer_ip);
                    send(sd, buffer_send, strlen(buffer_send), 0);
                    memset(buffer_send,0,1024);
                }
            }
        }
        


    }
}