#ifndef LIST_H
#define LIST_H

typedef struct Node {
    void* data;
    struct Node* next;
} Node;

typedef struct LinkedList {
    Node* head;
    int size;
} LinkedList;



LinkedList* createLinkedList();
void removeNode(LinkedList* list, void* data);
void insert(LinkedList* list, void* data);
void printList(LinkedList* list);
void freeList(LinkedList* list);
void* pop(LinkedList* list);

void duplicateList(LinkedList* list, LinkedList* newList);
#endif // LIST_H
