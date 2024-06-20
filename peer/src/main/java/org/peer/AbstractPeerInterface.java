package org.peer;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.LinkedList;
import java.io.File;
import java.util.regex.Pattern; 
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.peer.MessageGenerator;
import java.lang.Integer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Interface defining the operations that can be performed by a peer in a peer-to-peer network.
 */
public interface AbstractPeerInterface {

    /**
     * Initializes the list of files that the peer completely possesses.
     * @throws IOException If an I/O error occurs while initializing file pieces.
     */
    void initializePossessedFiles() throws IOException;

    /**
     * Returns the port of the tracker that the peer is connected to.
     * @return the port number of the tracker.
     */
    int getPortTracker();

    /**
     * Returns the IP address of the tracker that the peer is connected to.
     * @return the IP address of the tracker.
     */
    String getTrackerIP();

    /**
     * Returns the service port number that the peer is listening on.
     * @return the service port number.
     */
    int getPortServ();

    /**
     * Returns the IP address that the peer is using for its server functionality.
     * @return the server IP address.
     */
    String getServIP();

 
    /**
     * Establishes a connection to the tracker to start communication.
     */
    void connexionTracker();

    /**
     * Sends a command to the tracker or another peer.
     * @param command The command to send.
     * @param plec BufferedReader to read responses.
     * @param pred PrintWriter to send the command.
     * @throws Exception If an error occurs during command transmission.
     */
    void sendCommand(String command, BufferedReader plec, PrintWriter pred) throws Exception;

    /**
     * Closes the connection with the tracker.
     * @throws Exception If an error occurs during the closing of the connection.
     */
    void closeConnTracker() throws Exception;


    /**
     * Closes the server socket.
     * @throws Exception If an error occurs during the closing of the server.
     */
    void closeServ() throws Exception;

    /**
     * Connects to another peer.
     * @param host The host IP of the peer to connect to.
     * @param port The port number of the peer to connect to.
     */
    int connectToPeer(String host, int port);

    /**
     * Checks if a file is possessed by the peer.
     * @param f The key or identifier of the file to check.
     * @return true if the file is possessed, false otherwise.
     */
    boolean havePossessedFiles(String f);

    /**
     * Checks if a file is being downloaded by the peer.
     * @param f The key or identifier of the file to check.
     * @return true if the file is incomplete and being downloaded, false otherwise.
     */
    boolean haveincompleteFiles(String f);

 
    /**
     * Retrieves the index of a possessed file based on its identifier.
     * @param f The file identifier or key.
     * @return The index of the file in the possessed files list, or -1 if not found.
     */
    int getPossessedFilesIdx(String f);

    /**
     * Retrieves the index of an incomplete file based on its identifier.
     * @param f The file identifier or key.
     * @return The index of the file in the incomplete files list, or -1 if not found.
     */
    int getIncompleteFilesIdx(String f);

    /**
     * Retrieves the key associated with a possessed file at a specific index.
     * @param idx The index of the file in the possessed files list.
     * @return The key of the file.
     */
    String getPossessedKey(int idx);

    /**
     * Retrieves the key associated with an incomplete file at a specific index.
     * @param idx The index of the file in the incomplete files list.
     * @return The key of the file.
     */
    String getIncompleteKey(int idx);

    /**
     * Retrieves the buffer map of a possessed file at a specific index.
     * @param idx The index of the file in the possessed files list.
     * @return The buffer map of the file.
     */
    BufferMap getPossessedBM(int idx);

    /**
     * Retrieves the buffer map of an incomplete file at a specific index.
     * @param idx The index of the file in the incomplete files list.
     * @return The buffer map of the file.
     */
    BufferMap getIncompleteBM(int idx);

    /**
     * Verifies and matches pieces received from another peer against expected pieces.
     * @param receivedPieces The map of piece indices to their byte arrays received from another peer.
     * @param idxPieces The indices of pieces expected to match.
     * @return A map of piece indices to their byte arrays that have been verified and matched.
     */
    Map<Integer, byte[]> verificationSamePieces(Map<Integer, byte[]> receivedPieces, ArrayList<Integer> idxPieces);



    /**
     * Sends parts of a file to another peer asynchronously. This method should handle network operations,
     * including opening a socket, sending data, and closing the connection.
     * 
     * @param host The IP address of the recipient peer.
     * @param port The port number on the recipient peer.
     * @param idxFile The index of the file in the local list from which pieces are sent.
     * @param idxPieces A list of indices representing the pieces of the file to send.
     * @throws IOException If there are issues with network communication.
     */
    void sendPartialFile(String host, int port, int idxFile, ArrayList<Integer> idxPieces) throws IOException;

}


