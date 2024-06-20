CC = gcc
CFLAGS = -Wall -Wextra #-g -fprofile-arcs -ftest-coverage
SRC_DIR = src
TST_DIR = tst
BUILD_DIR = build
INSTALL_DIR = install

TARGET = $(BUILD_DIR)/main
TEST_TARGET = $(BUILD_DIR)/test
MAIN_FILE = main.o
TEST_FILES = file_client_test.o list_test.o big_test.o test.o 
SOURCE_FILES = config.o client.o list.o file.o message_handler.o tracker_state.o tracker.o


.PHONY: clean

clean:
	rm -rf $(BUILD_DIR)
	rm -rf $(INSTALL_DIR)
	rm *.o
	rm ./$(SRC_DIR)/*.gch

CONFIG_DIR = config
CONFIG_FILES = $(wildcard $(CONFIG_DIR)/*.config)

$(TEST_TARGET): $(CONFIG_FILES)

%.o: $(SRC_DIR)/%.c
	mkdir -p $(SRC_DIR)
	mkdir -p $(BUILD_DIR)
	$(CC) -c $(CFLAGS) -o $(BUILD_DIR)/$@ $<

%.o: $(TST_DIR)/%.c
	mkdir -p $(TST_DIR)
	mkdir -p $(BUILD_DIR)
	$(CC) -c $(CFLAGS) -o $(BUILD_DIR)/$@ $<


tracker: $(MAIN_FILE) $(SOURCE_FILES)
	mkdir -p $(INSTALL_DIR)
	$(CC) $(CFLAGS) $(addprefix $(BUILD_DIR)/, $^) -o $(INSTALL_DIR)/tracker

test: $(TEST_FILES) $(SOURCE_FILES)	
	mkdir -p $(INSTALL_DIR)
	$(CC) $(CFLAGS) $(addprefix $(BUILD_DIR)/, $^) -o $(INSTALL_DIR)/test