package org.peer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.Serializable;
import java.util.Collections;
import java.util.ArrayList;  
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Set;


// on part du principe que le client spécifie les informations de connexion du pair dans un fichier de configuration 
public class AbstractPeer implements AbstractPeerInterface{

    // Définition des variables de configuration pour le ThreadPool
    /**
     * Le nombre minimal de threads actifs dans le pool. Même si les threads ne sont pas utilisés,
     * ils resteront en vie jusqu'à ce que le nombre de threads actifs descende en dessous de ce seuil.
     */
    private final int corePoolSize = 10; 

    /**
     * Le nombre maximal de threads qui peuvent être présents dans le pool. Ce nombre inclut les threads actifs
     * et les threads inactifs en attente de travail. Si le nombre de tâches dépasse le nombre de threads actifs,
     * de nouveaux threads seront créés jusqu'à ce plafond.
     */
    private final int maximumPoolSize = 50; 

    /**
     * Le temps qu'un thread au-delà du nombre de threads de base (corePoolSize) restera en vie
     * sans recevoir de tâches avant d'être terminé. Cela permet de réduire la consommation de ressources
     * lorsque la demande de traitement est faible.
     */
    private final long keepAliveTime = 1; 

    /**
     * L'unité de temps pour le paramètre 'keepAliveTime'. Définit comment le temps d'inactivité est mesuré.
     * Ici, les minutes sont utilisées comme unité de mesure.
     */
    private final TimeUnit unit = TimeUnit.MINUTES;

    /**
     * La file d'attente utilisée pour stocker les tâches avant qu'elles ne soient exécutées. Cette file d'attente
     * permet de gérer l'ordre et la disponibilité des tâches pour les threads dans le pool.
     * 'LinkedBlockingQueue' est une file d'attente basée sur des liens qui peut théoriquement prendre un nombre
     * illimité de tâches en attente.
     */
    private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();

    // Initialisation du ThreadPoolExecutor avec les configurations spécifiées
    /**
     * Crée une instance de ThreadPoolExecutor qui gère un pool de threads selon les paramètres définis.
     * Ce gestionnaire de threads permet d'exécuter simultanément plusieurs tâches de manière efficace et
     * contrôlée, améliorant la performance globale de l'application en répartissant le travail sur plusieurs threads.
     */
    ExecutorService executor = new ThreadPoolExecutor(
        corePoolSize,
        maximumPoolSize,
        keepAliveTime,
        unit,
        workQueue
    );



    String entireFilesDirectory;
    int portT; 
    String trackerIp;
    int portServ;
    String servIP;
    LinkedList<FilePiece> possessedFiles; //Fichiers possédés au lancement du programme
    LinkedList<FilePiece> incompleteFiles; // Files that are being downloaded
    Map<String, String> MapReceivedOriginal; // Map of the original file key and the file key of the file being downloaded
    
    LinkedList<String> DownloadingFilesKey; // Result of the reseach with criteria 
    Socket skt; // Socket utilisée pour se connecter au tracker
    ServerSocket serv; // Socket utilisée pour le serveur du peer
    Scanner sa; //Scanner permettant de lire les entrées de l'utilisateur
    MessageGenerator mg;

    public AbstractPeer(){  
        try{
            sa = new Scanner(System.in);

            readConfiguration("./Config.ini");  
            System.out.println("Valeur de l'IP du tracker: " + this.trackerIp + ", Valeur du port: " + this.portT);

            entireFilesDirectory = System.getProperty("user.dir");
            initializePossessedFiles();
            initializeMapReceivedOriginal();
            this.mg = new MessageGenerator();
        } catch(Exception e) {
            System.err.println("The connexion has not been opened\n");
            e.printStackTrace();
        }
    }

    public AbstractPeer(int n) {
        try{          
            sa = new Scanner(System.in);

            readConfiguration("./Config.ini");  
            System.out.println("Valeur de l'IP du tracker: " + this.trackerIp + ", Valeur du port: " + this.portT);

            entireFilesDirectory = System.getProperty("user.dir");
            initializePossessedFiles(n);
            initializeMapReceivedOriginal();
            this.mg = new MessageGenerator();
        } catch(Exception e) {
            System.err.println("The connexion has not been opened\n");
            e.printStackTrace();
        }
    }


    public void initializePossessedFiles() {


        StringBuilder rep = new StringBuilder(entireFilesDirectory);
        rep.append("/FilePeer");

        possessedFiles = new LinkedList<>();
        File directory = new File(rep.toString());
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        FilePiece fp = new FilePiece(file.getAbsolutePath(), 1);
                        possessedFiles.add(fp);
                    } catch (IOException e) {
                        System.err.println("Failed to initialize file piece: " + e.getMessage());
                    }
                }
            }
        }
    }

    public void initializeMapReceivedOriginal(){
        MapReceivedOriginal = new HashMap<>();
        if(this.possessedFiles != null && this.possessedFiles.size() > 0){
            for(int i = 0; i < this.possessedFiles.size(); i++){
                MapReceivedOriginal.put(this.possessedFiles.get(i).getKey(), this.possessedFiles.get(i).getKey());
            }
        }
    }

    public void initializePossessedFiles(int n) {

        StringBuilder rep = new StringBuilder(entireFilesDirectory);
        rep.append("/FilePeer")
            .append(Integer.toString(n));

        possessedFiles = new LinkedList<>();
        File directory = new File(rep.toString());
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        FilePiece fp = new FilePiece(file.getAbsolutePath(), 1);
                        possessedFiles.add(fp);
                    } catch (IOException e) {
                        System.err.println("Failed to initialize file piece: " + e.getMessage());
                    }
                }
            }
        }
    }


    void readConfiguration(String filePath) {
        Properties prop = new Properties();
        try {
            FileInputStream input = new FileInputStream(filePath);
            prop.load(input);

            // Assignation des valeurs de configuration aux variables d'instance
            this.trackerIp = prop.getProperty("tracker-address");
            this.portT = Integer.parseInt(prop.getProperty("tracker-port"));

            System.out.println("Configuration loaded: Tracker IP = " + this.trackerIp + ", Tracker Port = " + this.portT);
        } catch (IOException ex) {
            System.err.println("Error reading the configuration file: " + ex.getMessage());
        }
    }

    
    public int getPortTracker(){
        return this.portT;
    }

    public String getTrackerIP(){
        return this.trackerIp;
    }

    public int getPortServ(){
        return this.portServ;
    }

    public String getServIP(){
        return this.servIP;
    }


    public void connexionTracker(){

        
        new Thread(() -> {

        try {
            this.skt = new Socket(getTrackerIP(), getPortTracker());   
            System.out.println("Je suis connecté au port " + getPortTracker());
            Scanner scan = new Scanner(System.in);
            BufferedReader plec = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            PrintWriter pred = new PrintWriter(new BufferedWriter(new OutputStreamWriter(skt.getOutputStream())), true);
            self_announce(plec, pred);
            TimerTask task = new TimerTask() {
            public void run() {
                try{
                    upd_track(plec, pred);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            };
            Timer timer = new Timer();
            int delay = 1000;
            timer.schedule(task, 0 ,delay * 120);
            System.out.println("< ");
            while (true){
                String command = sa.nextLine();
                if (command.length() != 0){

                if (command.equals("exit")) { System.out.println("You have been disconnected"); break;}
                sendCommand(command, plec, pred);
            }
                System.out.print("< ");
            }

            plec.close();
            pred.close();
            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        }).start();
    }
    

    void upd_track(BufferedReader plec, PrintWriter pred) throws Exception{
        StringBuilder mess = new StringBuilder();
        mess.append("update seed [");
        if (possessedFiles != null){
            for(int i = 0; i < possessedFiles.size(); i++){
                mess.append(possessedFiles.get(i).getKey());
                if(i != this.possessedFiles.size() -1 ) mess.append(" ");
            }
        }
        mess.append("] leech [");
        if (DownloadingFilesKey != null){
            //for(int i = 0; i < DownloadingFilesKey.size(); i++){
            //    mess.append(DownloadingFilesKey.get(i).getKey());
            //    if(i != this.possessedFiles.size() -1 ) mess.append(" ");
            //}
        }
        mess.append("]");
        if (DownloadingFilesKey != null) DownloadingFilesKey.clear();
        System.out.println("<" + mess.toString());
        pred.println(mess.toString());
        System.out.println("> " + plec.readLine());
        System.out.print("<");
    }

    public void sendCommand(String command, BufferedReader plec, PrintWriter pred)throws Exception{

        pred.println(command);
        
        System.out.println("> " + plec.readLine());
        System.out.println(plec.readLine());
    }

    public void sendCommand(String command, ObjectOutputStream objectOut, ObjectInputStream objectIn) throws Exception {
        objectOut.writeObject(command);
        objectOut.flush();

       // Object response = objectIn.readObject();
       // if (response instanceof String){
       //     System.out.println("> " + response);
       // }
    }




    // la connexion est fermé lorsque plec et pred sont clos
    public void closeConnTracker(){
        try {
            this.skt.close();            
        } catch (Exception e) {
            e.printStackTrace();
            }
    }


    public void closeServ()throws Exception{
        this.serv.close();
    }


    int indexFileByKey(LinkedList<FilePiece> list, String key) {
        if(key != null && list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getKey().equals(key)) {
                    return i;
                }
            }
        } 
        return -1;
    }


    String correspondanceOriginKey(Map<String, String> map, String key) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    int indexMapMAx(Map<Integer, FilePieceData> map) {
        int max = 0;
        for (Map.Entry<Integer, FilePieceData> entry : map.entrySet()) {
            if (entry.getKey() > max) {
                max = entry.getKey();
            }
        }
        return max;
    }


    public int connectToPeer(String host, int port){


        try (Socket socket = new Socket(host, port);
             ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream())) {            
            System.out.println("Connected to " + host + ":" + port);
            
            Thread listenerThread = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        Object response = objectIn.readObject();

                        if (response instanceof Map) {
                            // Assuming the map's value type is correctly known
                            Map<Integer, FilePieceData> map = (Map<Integer, FilePieceData>) response;


                            //fill in the globalCollectorPiece for each file key
                            if(map == null && map.size() == 0){

                                System.out.println("No pieces of the file with key");

                            } else {
                                Set<Integer> keys = map.keySet();
                                Integer[] keysArray = keys.toArray(new Integer[0]);  // Convertit le Set en tableau
                                String keyMap = null;
                                if (keysArray.length > 0) {
                                    keyMap = map.get(keysArray[0]).getFileKey();  // Utilise la première clé pour obtenir la valeur
                                } else {
                                    System.err.println("Error: Map is empty");
                                }

                                // String keyMap = map.get(1).getFileKey();


                                //CONDITION 1 THE FILE IS ALREADY POSSESSED
                                if ((indexFileByKey(this.possessedFiles, correspondanceOriginKey(MapReceivedOriginal, keyMap)) != -1) || (indexFileByKey(this.possessedFiles, keyMap) != -1)) {
                                    

                                    if (indexFileByKey(this.possessedFiles, keyMap) != -1) { // SAME ORIGIN FILE AND KEY


                                        int indexPossessed = indexFileByKey(this.possessedFiles,keyMap);

                                        this.possessedFiles.get(indexPossessed).setPiecesReceived(map);

                                        String keyFile = this.possessedFiles.get(indexPossessed).getKey();

                                        String nexFileN = "NewFile" + keyMap.substring(0, 4)+ keyFile.substring(0, 4);

                                        writeIncompleteToFile(this.possessedFiles.get(indexPossessed), nexFileN);

                                    } else if( ( indexFileByKey( this.possessedFiles , correspondanceOriginKey(MapReceivedOriginal , keyMap ) ) != -1 ) ){ //DIFFERENT ORIGIN  FILE AND KEY
                                        

                                        int indexPossessed = indexFileByKey(this.possessedFiles,correspondanceOriginKey(MapReceivedOriginal , keyMap ));

                                        this.possessedFiles.get(indexPossessed).setPiecesReceived(map);

                                        String keyFile = this.possessedFiles.get(indexPossessed).getKey();

                                        String nexFileN = "NewFile" + keyMap.substring(0, 4)+ keyFile.substring(0, 4);

                                        writeIncompleteToFile(this.possessedFiles.get(indexPossessed), nexFileN);
                                    }


                                } else { //CONDITION 2 THE FILE IS INCOMPLETE


                                    if( ( indexFileByKey(this.incompleteFiles, correspondanceOriginKey(MapReceivedOriginal, keyMap)) != -1 )) { //INCOMPLETE BUT WE ALREADY STARTED THE DOWNLOAD
                                        

                                        int indexIncomplete = indexFileByKey(this.incompleteFiles, correspondanceOriginKey(MapReceivedOriginal, keyMap));

                                        this.incompleteFiles.get(indexIncomplete).setPiecesReceived(map);

                                        String keyFile = this.incompleteFiles.get(indexIncomplete).getKey();

                                        String nexFileN = "NewFile" + keyMap.substring(0, 4)+ keyFile.substring(0, 4);

                                        writeIncompleteToFile(this.incompleteFiles.get(indexIncomplete), nexFileN);

                                        if(this.incompleteFiles.get(indexIncomplete).getBufferMap().areAllPiecesPresent()){


                                            FilePiece fp = this.incompleteFiles.get(indexIncomplete);

                                            this.incompleteFiles.remove(indexIncomplete);

                                            if(this.possessedFiles == null || this.possessedFiles.size() == 0){
                                                this.possessedFiles = new LinkedList<FilePiece>();
                                                this.possessedFiles.add(fp);
                                            } else {
                                                this.possessedFiles.add(fp);
                                            }

                                        }

                                    } else {


                                        if(this.incompleteFiles == null || this.incompleteFiles.size() == 0){
                                            this.incompleteFiles = new LinkedList<FilePiece>();
                                        }


                                        int bufferMapOriginSize = map.get(keysArray[0]).getBufferMap().getMapSize(); // Get the size of the bufferMap


                                        String filename = "NewFile" + keyMap.substring(0, 4);

                                        String keyFile = KeyGen.keysGenerator(filename);
 
                                        BufferMap bm = new BufferMap(bufferMapOriginSize, keyFile);

                                        FilePiece fpFile = new  FilePiece(filename + keyFile.substring(0,4) , keyFile, bm , 1 , bufferMapOriginSize);

                                        fpFile.setPiecesReceived(map);

                                        if(fpFile.getBufferMap().areAllPiecesPresent()){


                                            if(this.possessedFiles == null || this.possessedFiles.size() == 0){
                                                this.possessedFiles = new LinkedList<FilePiece>();
                                                this.possessedFiles.add(fpFile);
                                            } else {
                                                this.possessedFiles.add(fpFile);
                                            }

                                        } else {
                                            this.incompleteFiles.add(fpFile);
                                        }

                                        this.MapReceivedOriginal.put(keyMap, keyFile);  // Add the original key and the new key to the map

                                        String nexFileN = "NewFile" + keyMap.substring(0, 4) + keyFile.substring(0, 4);

                                        writeIncompleteToFile(fpFile, nexFileN);

                                    }


                                }

                            }
     
                        } else if (response instanceof String) {
                            System.out.println("> "+ response);
                            if ("exit".equals(response)) break;
                        }
                    }
                    

                } catch (Exception e) {
                    System.err.println("Error while receiving data: " + e.getMessage());
                    e.printStackTrace();
                }
            });
    
            listenerThread.start();
    
            Scanner sa = new Scanner(System.in);
            while (true) {
                System.out.print("< ");
                String command = sa.nextLine().trim();
                if ("exit".equalsIgnoreCase(command)) {
                    System.out.println("You have been disconnected");
                    listenerThread.interrupt();  // Inform the listener thread to stop listening
                    break;
                }
                synchronized (objectOut) {
                    try {
                        sendCommand(command, objectOut, objectIn);
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
    
            // Cleanup
            try {
                listenerThread.join();  // Ensure listener thread finishes properly
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted: " + e.getMessage());
            }
    
            // Clean up after loop
            //try {
            //    //listenerThread.join(); // Ensure listener thread finishes properly
            //} catch (InterruptedException e) {
            //    Thread.currentThread().interrupt();
            //    System.err.println("Thread interrupted: " + e.getMessage());
            //}
    
            objectOut.close();
            objectIn.close();
            sa.close();
            return 0;
        } catch (IOException e) {
            System.err.println("Error connecting to peer: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    void writeIncompleteToFile(FilePiece filePiece, String filename) {
        // Setup the file output path and create a file output stream
        File outputFile = new File(filename);
        try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
            ArrayList<byte[]> pieces = filePiece.getAllPieces(); // Assuming getAllPieces returns the ArrayList of byte arrays
            for (byte[] piece : pieces) {
                if (piece != null) {  // Only write non-null pieces
                    fileOut.write(piece);
                }
            }
            System.out.println("File written successfully: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public boolean havePossessedFiles(String f) {
        if(this.possessedFiles != null && this.possessedFiles.size() != 0) {
            for(int i=0 ; i < possessedFiles.size() ; i++){
                String key = getPossessedKey(i);
                if (key.equals(f)) return true;
            }
        }
        return false;
    }

    public boolean haveincompleteFiles(String f) {
        if(this.incompleteFiles != null && this.incompleteFiles.size() != 0)  {
            for(int i=0 ; i < incompleteFiles.size() ; i++){
                String key = getIncompleteKey(i);
                if (key.equals(f)) return true;
            }
        }
        return false;
    }


    public int getPossessedFilesIdx(String f){
        if(this.possessedFiles != null && this.possessedFiles.size() != 0){
            for(int i=0 ; i < possessedFiles.size() ; i++){
                String key = getPossessedKey(i);
                if (key.equals(f)) return i;
            }
        }
        return -1;
    }

    public int getIncompleteFilesIdx(String f) {
        if(this.incompleteFiles != null && this.incompleteFiles.size() != 0)  {
            for(int i=0 ; i < incompleteFiles.size() ; i++){
                String key = getIncompleteKey(i);
                if (key.equals(f)) return i;
            }
        }
        return -1;
    }

    public String getPossessedKey(int idx){
        return possessedFiles.get(idx).getKey();
     }

     public String getIncompleteKey(int idx){
        return incompleteFiles.get(idx).getKey();
     }

     public BufferMap getPossessedBM(int idx){
        return possessedFiles.get(idx).getBufferMap();
     }

     public BufferMap getIncompleteBM(int idx){
        return incompleteFiles.get(idx).getBufferMap();

     }





// Utilize the thread pool to send parts of files asynchronously to other peers.
    public void sendPartialFile(String host, int port, int idxFile, ArrayList<Integer> idxPieces) {
        executor.execute(() -> {
            try (Socket socket = new Socket(host, port);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                // Prepare file pieces to be sent
                FilePiece fp = possessedFiles.get(idxFile);
                Map<Integer, FilePieceData> pieces = fp.getPiecesToSend(idxPieces);

                // Send file pieces
                out.writeObject(pieces);
                out.flush();
                System.out.println("File pieces sent to " + host + ":" + port);

                // Wait for acknowledgment from the peer
                Object response = in.readObject();
                if (response instanceof String) {
                    String ack = (String) response;
                    System.out.println("Acknowledgment received from " + host + ": " + ack);
                }

            } catch (IOException e) {
                System.err.println("Error sending file pieces: " + e.getMessage());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found while reading acknowledgment: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }


    public Map<Integer, byte[]> verificationSamePieces(Map<Integer, byte[]> receivedPieces, ArrayList<Integer> idxPieces){
        Map<Integer, byte[]> result = new HashMap<>();
        for (int i = 0; i < idxPieces.size(); i++) {
            if (receivedPieces.containsKey(idxPieces.get(i))) {
                result.put(idxPieces.get(i), receivedPieces.get(idxPieces.get(i)));
            }
        }
        return result;
    }

    void self_announce(BufferedReader plec, PrintWriter pred) throws IOException{
        StringBuilder message = new StringBuilder();
        message.append("announce listen ")
                .append(this.portServ)
                .append(" seed [");
        for(int i = 0 ; i < this.possessedFiles.size(); i++){
            message.append(this.possessedFiles.get(i).getFilename())
                    .append(" ")
                    .append(this.possessedFiles.get(i).getFileSize())
                    .append(" ")
                    .append(this.possessedFiles.get(i).getPieceSize())
                    .append(" ")
                    .append(this.possessedFiles.get(i).getKey());
                    if(i != this.possessedFiles.size() -1 ) message.append(" ");
        }
        message.append("]");
        System.out.println("< " + message);
        pred.println(message);
        System.out.println("> " + plec.readLine());
    }

    
}

