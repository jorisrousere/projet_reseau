#include "../src/message_handler.h"
#include <assert.h>
#include "test.h"


int test_announce(){
    char announce_s_ab_l_c[] = "announce listen s_ab_l_c seed [file_a 1 1 a file_b 1 1 b]  leech [file_c 1 1 c]";
    char announce_s_bc_l_a[] = "announce listen s_bc_l_a seed [file_b 1 1 b file_c 1 1 c]  leech [file_a 1 1 a]";
    char announce_s_bc_l_a2[] = "announce listen s_bc_l_a seed [file_b 1 1 b file_c 1 1 c]  leech [file_a 1 1 a file_a 1 1 d]";

    char buffer_send[50];
    handle_message(announce_s_ab_l_c, buffer_send, 5, "155.155.155.155");
    printf("%s\n",buffer_send);

    handle_message(announce_s_bc_l_a, buffer_send, 4, "155.155.155.155");
    printf("%s\n",buffer_send);

    handle_message(announce_s_bc_l_a2, buffer_send, 4, "155.155.155.155");
    printf("%s\n",buffer_send);


    char look[] = "look [filename=\"file_a\"]";
    handle_message(look, buffer_send, 5, "155.155.155.155");
    printf("%s\n",buffer_send);


    char getfile[] = "getfile [a]";
    handle_message(getfile, buffer_send, 5, "155.155.155.155");
    printf("%s\n",buffer_send);


    char getfile1[] = "getfile [c]";
    handle_message(getfile1, buffer_send, 5, "155.155.155.155");
    printf("%s\n",buffer_send);


    char getfile2[] = "getfile [d]";
    handle_message(getfile2, buffer_send, 5, "155.155.155.155");
    printf("%s\n",buffer_send);

    printf("\n\n\n\n\n");

    char update[] = "update seed [c] leech [d]";
    handle_message(update, buffer_send, 5, "155.155.155.155");
    printf("%s\n",buffer_send);


    char update1[] = "update seed [a]";
    handle_message(update1, buffer_send, 4, "155.155.155.155");
    printf("%s\n",buffer_send);

    printf("\n\n\n\n\n");

    char getfile0[] = "getfile [a]";
    handle_message(getfile0, buffer_send, 5, "155.155.155.155");
    printf("%s\n",buffer_send);


    char getfile11[] = "getfile [c]";
    handle_message(getfile11, buffer_send, 5, "155.155.155.155");
    printf("%s\n",buffer_send);


    char getfile21[] = "getfile [d]";
    handle_message(getfile21, buffer_send, 5, "155.155.155.155");
    printf("%s\n",buffer_send);
    return 0;
}
