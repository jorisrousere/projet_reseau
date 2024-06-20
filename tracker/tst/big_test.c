#include "test.h"


int test_big(){
    char announce_s_ab_l_c[] = "announce listen s_ab_l_c seed [file_a 1 1 a file_b 1 1 b]  leech [file_c 1 1 c]\n";
    char announce_s_bc_l_a[] = "announce listen s_bc_l_a seed [file_b 1 1 b file_c 1 1 c]  leech [file_a 1 1 a]\n";
    char announce_s_bc_l_a2[] = "announce listen s_bc_l_a seed [file_b 1 1 b file_c 1 1 c]  leech [file_a 1 1 a file_a 1 1 d]\n";

    char buffer_send[1024];
    handle_message(announce_s_ab_l_c, buffer_send, 5, "155.155.155.155");
    if(!strcmp(buffer_send,"ok") ) return 1;

    handle_message(announce_s_bc_l_a, buffer_send, 4, "155.155.155.155");
    if(!strcmp(buffer_send,"ok") ) return 1;

    handle_message(announce_s_bc_l_a2, buffer_send, 4, "155.155.155.155");
    if(!strcmp(buffer_send,"ok") ) return 1;


    char look[] = "look [filename=\"file_a\"]\n";
    handle_message(look, buffer_send, 5, "155.155.155.155");
    if(!strcmp(buffer_send,"list [file_a 1 1 d file_a 1 1 a]") ) return 1;


    char getfile[] = "getfile a\n";
    handle_message(getfile, buffer_send, 5, "155.155.155.155");
    if(!strcmp(buffer_send,"peers a [155.155.155.155:s_ab_ 155.155.155.155:s_bc_]") ) return 1;


    char getfile1[] = "getfile c\n";
    handle_message(getfile1, buffer_send, 5, "155.155.155.155");
    if(!strcmp(buffer_send,"peers c [155.155.155.155:s_bc_ 155.155.155.155:s_ab_]") ) return 1;


    char getfile2[] = "getfile d\n";
    handle_message(getfile2, buffer_send, 5, "155.155.155.155");
    if(!strcmp(buffer_send,"peers d [155.155.155.155:s_bc_]")) return 1;

    

    char update[] = "update seed [c] leech [d]\n";
    handle_message(update, buffer_send, 5, "155.155.155.155");
    if(!strcmp(buffer_send,"ok") ) return 1;


    char update1[] = "update seed [a]\n";
    handle_message(update1, buffer_send, 4, "155.155.155.155");
    if(!strcmp(buffer_send,"ok") ) return 1;

    

    char getfile0[] = "getfile a\n";
    handle_message(getfile0, buffer_send, 5, "155.155.155.155");
    if(!strcmp(buffer_send,"peers a [155.155.155.155:s_ab_ 155.155.155.155:s_bc_]") ) return 1;
    memset(buffer_send ,0 ,1024);


    char getfile11[] = "getfile c\n";
    handle_message(getfile11, buffer_send, 5, "155.155.155.155");
    if(!strcmp(buffer_send,"peers c [155.155.155.155:s_bc_ 155.155.155.155:s_ab_]") ) return 1;
    memset(buffer_send ,0 ,1024);


    char getfile21[] = "getfile d\n";
    handle_message(getfile21, buffer_send, 5, "155.155.155.155");
    if(!strcmp(buffer_send,"peers d [155.155.155.155:s_bc_ 155.155.155.155:s_ab_]") ) return 1;
    memset(buffer_send ,0 ,1024);

    return 0;
}



