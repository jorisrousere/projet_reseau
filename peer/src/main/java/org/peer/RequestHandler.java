package org.peer;
import java.util.ArrayList;
import java.util.Scanner;
import org.peer.AbstractPeer;
import org.peer.MessageGenerator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.Integer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream ;





public class RequestHandler extends AbstractPeer{
    
    public RequestHandler() {
        super();
        startServer();
    }

    public RequestHandler(int n) {
        super(n);
        startServer();
    }
    
    void handleRequest(String request, ObjectOutputStream out) {
        String[] parts = request.split(" ");
        String command = parts[0];

        switch (command) {


            case "interested":
                handleInterestedRequest(parts, out);
                break;
            case "getpieces":
                handleGetpiecesRequests(parts, out);
                break;
            case "have":
                handleHaveRequests(parts, out);
                break;
            default:
                System.out.println("Unknown command: " + command);
                break;
            }
    }


    private void handleGetpiecesRequests(String[] parts, ObjectOutputStream out) {
        int pLength = parts.length;
        if (pLength <= 2) {
            System.err.println("Lack of information. Should be : getpieces $Key [$Index1 $Index2 $Index3 …]\n");
            try {
                out.writeObject("ERROR: Insufficient information for getpieces");
            } catch (IOException e) {
                System.err.println("Can't write to output stream: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            String key = parts[1];
            parts[pLength-1] = parts[pLength-1].replace("]", "");
            parts[2] = parts[2].replace("[", "");
            ArrayList<byte[]> pieces = new ArrayList<>();
            ArrayList<Integer> idx = new ArrayList<Integer>();
    
            // Collect all indices first
            for (int i = 2; i < pLength; i++) {
                idx.add(Integer.parseInt(parts[i]));
            }
    
            // Check if possessed files exist and process them
            if (this.havePossessedFiles(key)) {
                int j = this.getPossessedFilesIdx(key);
                FilePiece fp = possessedFiles.get(j);
                Map<Integer, FilePieceData> idxFP = fp.getPiecesToSend(idx);
    
    
                for (Integer index : idx) {
                    if (idxFP.containsKey(index) && idxFP.get(index) != null) {
                        pieces.add(idxFP.get(index).getData());
                    }
                }
                try {
                    out.writeObject(this.mg.generateDataMessage(key, idx, pieces));
                    out.writeObject(idxFP);
                } catch (Exception e) {
                    System.err.println("Can't write to output stream: " + e.getMessage());
                    e.printStackTrace();
                }
            } else if (this.haveincompleteFiles(key)) {
                int j = this.getIncompleteFilesIdx(key);
                FilePiece fp = incompleteFiles.get(j);
                Map<Integer, FilePieceData> idxFP = fp.getPiecesToSend(idx);
    
                for (Map.Entry<Integer, FilePieceData> entry : idxFP.entrySet()) {
                    if (entry.getValue() != null) {
                        pieces.add(entry.getValue().getData());
                    }
                }
                try {
                    out.writeObject(this.mg.generateDataMessage(key, idx, pieces));

                } catch (Exception e) {
                    System.err.println("Can't write to output stream: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                try {
                    out.writeObject("ERROR: No files found for key " + key);
                } catch (IOException e) {
                    System.err.println("Error writing to output stream: " + e.getMessage());
                }
            }
    
            // Final catch for any remaining cases (outside main logic)
            try {
                if (pieces.isEmpty()) {
                    out.writeObject("ERROR in getpieces, no pieces retrieved");
                }
            } catch (IOException e) {
                System.err.println("Error writing to output stream at final check: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private void handleInterestedRequest(String[] parts, ObjectOutputStream out) {
        int pLength = parts.length;
        if (pLength <= 1){
            System.err.println("There is no file's key specified\n");
        }
        for(int i = 1; i < pLength; i++){
            if (this.havePossessedFiles(parts[i])){
                int idx = this.getPossessedFilesIdx(parts[i]);
                try {
                    out.writeObject(this.mg.generateHaveMessage(parts[i], this.getPossessedBM(idx)));
                } catch (Exception e) {
                    System.err.println("Can't write");
                    e.printStackTrace();
                }
            }
            else if(this.haveincompleteFiles(parts[i])){
                int idx = this.getIncompleteFilesIdx(parts[i]);
                try {
                    out.writeObject(this.mg.generateHaveMessage(parts[i], this.getIncompleteBM(idx)));
                } catch (Exception e) {
                    System.err.println("Can't write");
                    e.printStackTrace();
                }
            }
            else {
                System.err.println("The file has not been found\n");
            }
        }
    }

    private void handleHaveRequests(String[] parts, ObjectOutputStream out){
        int pLength = parts.length;
        if(pLength <= 1){
            System.err.println("There is no file's key or no buffermap specified\n");
        }
        String key = parts[1];
        if(this.havePossessedFiles(key)){
            int idx = this.getPossessedFilesIdx(key);
            BufferMap BMF = this.getPossessedBM(idx);

            if (idx == -1 ) System.out.println("File has not been found");
            try {
                out.writeObject(mg.generateHaveMessage(key, BMF));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(this.haveincompleteFiles(key)){
            int idx = this.getIncompleteFilesIdx(key);
            BufferMap BMF = this.getIncompleteBM(idx);
            if (idx == -1 ) System.out.println("File has not been found");
            try {
                out.writeObject(mg.generateHaveMessage(key, BMF));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handlePeersRequest(String[] parts) {
        int pLength = parts.length;
        if (pLength <= 2){
            System.err.println("Lack of information. Should be : peers $Key [$IP1:$Port1 $IP2:$Port2 …]\n");
        }
        else {
            parts[pLength-1] = parts[pLength-1].replace("]", "");
            parts[2] = parts[2].replace("[", "");
            for (int i = 2; i < pLength; i++){
                String ip = parts[i].split(":")[0];
                int port = Integer.parseInt(parts[i].split(":")[1]);
                int res = connectToPeer(ip, port);
                if (res == 0) break;

            }            
        }
    }

    private void handleClient(Socket clientSocket) {
        executor.execute(() -> {
            try (
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ) {
                Object inputObject;

                // Process incoming objects which could be Strings or Maps (for file pieces)
                while ((inputObject = in.readObject()) != null) {
                    if (inputObject instanceof String) {
                        String inputLine = (String) inputObject;
                        // Handle exit command
                        if ("exit".equalsIgnoreCase(inputLine)) {
                            System.out.println("Client has disconnected: " + clientSocket.getRemoteSocketAddress());
                            break;
                        }
                        // Log received messages and handle other text-based commands
                        handleRequest(inputLine, out);
                        out.flush();
                    } 
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error managing client connection: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        });
    }
    
/**
 * Lance le serveur pour écouter les connexions entrantes sur un port dynamiquement attribué.
 * Utilise un ExecutorService pour gérer les connexions entrantes de manière efficace et non bloquante.
 */
    void startServer() {
        // Utilise le pool de threads pour gérer les nouvelles connexions de manière asynchrone.
        executor.execute(() -> {
            try {
                // Crée un ServerSocket qui écoute sur un port attribué automatiquement.
                serv = new ServerSocket(0);
                this.portServ = serv.getLocalPort(); // Stocke le port sur lequel le serveur a été démarré.
                System.out.println("Serveur démarré sur le port: " + this.portServ);

                // Boucle infinie pour accepter continuellement de nouvelles connexions client.
                while (true) {
                    // Attend et accepte une nouvelle connexion client.
                    Socket clientSocket = serv.accept();
                    // Affiche une confirmation dans la console du serveur lorsque chaque connexion est établie.
                    System.out.println("Connexion acceptée de " + clientSocket.getRemoteSocketAddress());

                    // Passe la gestion du client connecté à la méthode handleClient.
                    // handleClient s'exécute dans un thread séparé pour chaque client, permettant ainsi de traiter plusieurs clients simultanément.
                    handleClient(clientSocket);
                }
            } catch (IOException e) {
                // Gère les exceptions liées aux opérations réseau.
                // Si une exception se produit, elle est enregistrée et la stack trace est imprimée pour faciliter le débogage.
                System.err.println("Erreur du serveur: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void sendCommand(String command, BufferedReader plec, PrintWriter pred)throws Exception{
        pred.println(command);
        String answer = plec.readLine();
        String[] parts = answer.split(" ");

        if (parts[0].equals("peers")){
            System.out.println("> " + answer);
            handlePeersRequest(parts);
        }
        else {
        System.out.println("> " + answer);
        }
        //plec.close();
        // pred.close();
    }
    

}