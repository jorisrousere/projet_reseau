#include <stdio.h>
#include <stdlib.h>
#include "list.h"

LinkedList* createLinkedList() {
    LinkedList* list = (LinkedList*)malloc(sizeof(LinkedList));
    list->head = NULL;
    list->size = 0;
    return list;
}

void insert(LinkedList* list, void* data) {
    Node* newNode = (Node*)malloc(sizeof(Node));
    newNode->data = data;
    newNode->next = NULL;

    if (list->head == NULL) {
        list->head = newNode;
    } else {
        Node* current = list->head;
        while (current->next != NULL) {
            current = current->next;
        }
        current->next = newNode;
    }

    list->size++;
}

void* pop(LinkedList* list) {
    if (list->head == NULL) {
        return NULL;
    }
    Node* firstNode = list->head;
    void* data = firstNode->data;
    list->head = firstNode->next;
    free(firstNode);
    list->size--;
    return data;
}

void duplicateList(LinkedList* list, LinkedList* newList) {
    Node* current = list->head;
    while (current != NULL) {
        insert(newList, current->data);
        current = current->next;
    }
}

void removeNode(LinkedList* list, void* data) {
    Node* current = list->head;
    Node* previous = NULL;

    while (current != NULL) {
        if (current->data == data) {
            if (previous == NULL) {
                list->head = current->next;
            } else {
                previous->next = current->next;
            }
            free(current);
            list->size--;
            return;
        }
        previous = current;
        current = current->next;
    }
}

void freeList(LinkedList* list) {
    Node* current = list->head;
    while (current != NULL) {
        Node* temp = current;
        current = current->next;
        free(temp);
    }
    free(list);
}