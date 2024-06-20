#include <assert.h>
#include "test.h"

int list_test_createLinkedList() {
    LinkedList* list = createLinkedList();
    if (list == NULL || list->head != NULL || list->size != 0) {
        freeList(list);
        return 1;
    }
    freeList(list);
    return 0;
}

int list_test_insert() {
    LinkedList* list = createLinkedList();
    int data1 = 10;
    int data2 = 20;
    insert(list, &data1);
    if (list->head == NULL || list->size != 1 || *(int*)(list->head->data) != data1) {
        freeList(list);
        return 1;
    }
    insert(list, &data2);
    if (list->size != 2 || *(int*)(list->head->next->data) != data2) {
        freeList(list);
        return 1;
    }
    freeList(list);
    return 0;
}

int list_test_pop() {
    LinkedList* list = createLinkedList();
    int data1 = 10;
    int data2 = 20;
    insert(list, &data1);
    insert(list, &data2);
    void* poppedData = pop(list);
    if (poppedData == NULL || *(int*)poppedData != data1 || list->size != 1) {
        freeList(list);
        return 1;
    }
    poppedData = pop(list);
    if (poppedData == NULL || *(int*)poppedData != data2 || list->size != 0) {
        freeList(list);
        return 1;
    }
    poppedData = pop(list);
    if (poppedData != NULL) {
        freeList(list);
        return 1;
    }
    freeList(list);
    return 0;
}

int list_test_duplicateList() {
    LinkedList* list = createLinkedList();
    int data1 = 10;
    int data2 = 20;
    insert(list, &data1);
    insert(list, &data2);
    LinkedList* newList = createLinkedList();
    duplicateList(list, newList);
    if (newList->size != list->size) {
        freeList(list);
        freeList(newList);
        return 1;
    }
    Node* current = list->head;
    Node* newCurrent = newList->head;
    while (current != NULL && newCurrent != NULL) {
        if (*(int*)(current->data) != *(int*)(newCurrent->data)) {
            freeList(list);
            freeList(newList);
            return 1;
        }
        current = current->next;
        newCurrent = newCurrent->next;
    }
    freeList(list);
    freeList(newList);
    return 0;
}

int list_test_removeNode() {
    LinkedList* list = createLinkedList();
    int data1 = 10;
    int data2 = 20;
    int data3 = 30;
    insert(list, &data1);
    insert(list, &data2);
    insert(list, &data3);
    removeNode(list, &data2);
    if (list->size != 2) {
        freeList(list);
        return 1;
    }
    Node* current = list->head;
    while (current != NULL) {
        if (*(int*)(current->data) == data2) {
            freeList(list);
            return 1;
        }
        current = current->next;
    }
    freeList(list);
    return 0;
}
