#include "test.h"

#define RED  "\x1B[31m"
#define GRN  "\x1B[32m"
#define NRM  "\x1B[0m"


void print_result(int result, char *test){
    if(result == 0){
        printf("%sTesting %s... ->%s PASSED%s\n",NRM,test,GRN,NRM);
    }
    else if (result != -1){
        fprintf(stderr,"%sTesting %s... ->%s NOT PASSED%s\n",NRM,test,RED,NRM);
    }
    else{
        fprintf(stderr,"Invalid argument!%d%s\n",result,NRM);
    }
}

struct test {
  char* name;
  int (*func)(void);
};


struct test tests[] = { 
    {"test_big",test_big},
    {"list_test_createLinkedList", list_test_createLinkedList},
    {"list_test_insert", list_test_insert},
    {"list_test_pop", list_test_pop},
    {"list_test_duplicateList", list_test_duplicateList},
    {"list_test_removeNode", list_test_removeNode},
    {"file_client_test_create", file_client_test_create},
    {"file_client_test_has_criterion", file_client_test_has_criterion},
    {"file_client_test_add_leecher", file_client_test_add_leecher},
    {"file_client_test_add_seeder", file_client_test_add_seeder},
    {"file_client_test_remove_leecher", file_client_test_remove_leecher},
    {"file_client_test_get_name", file_client_test_get_name},
    {"file_client_test_get_length", file_client_test_get_length},
    {"file_client_test_get_piece_size", file_client_test_get_piece_size},
    {"file_client_test_get_key", file_client_test_get_key},
    {"file_client_test_functions", file_client_test_functions},
    {NULL, NULL}
};

/* ************************************************************************** */


int main (int argc, char *argv[]){
    if (argc == 1){
        for (struct test* pt = tests; pt->name && pt->func; pt++){
            print_result(pt->func(),pt->name);
        }
        return EXIT_SUCCESS;
    }
    else if(argc == 2){
        for (struct test* pt = tests; pt->name && pt->func; pt++){
            if(strcmp(argv[1], pt->name) == 0){
                print_result(pt->func(),pt->name);
                return EXIT_SUCCESS;
            }
        }
    }
    print_result(-1,NULL);
    return EXIT_SUCCESS;
}


/* ************************************************************************** */
