// package org.peer;
// import java.io.*;
// import java.net.*;
// import java.util.Scanner;
// import java.util.LinkedList;
// import java.io.File;
// import java.util.regex.Pattern; 
// import java.util.ArrayList;
// import java.util.Map;
// import java.util.HashMap;
// import org.peer.MessageGenerator;
// import java.lang.Integer;
// import java.util.Timer;
// import java.util.TimerTask;
// import java.util.concurrent.atomic.AtomicInteger;
// import java.util.concurrent.BlockingQueue;
// import java.util.concurrent.ThreadPoolExecutor;
// import java.util.concurrent.TimeUnit;
// import java.util.concurrent.LinkedBlockingQueue;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;

// public class PeerDataExchange1 {

//     static AbstractPeer peer = new AbstractPeer("./src/main/java/org/peer/FilePeer");

//     static public void main(String[] args) {
//         try {
//             peer.initializePossessedFiles();
//             System.out.println("PeerDataExchange initialized and number of possessed files: " + peer.possessedFiles.size());  

//             // ArrayList<Integer> tableauMap = new ArrayList<Integer>();
//             // for(int i = 0 ; i < 30 ; i++) {
//             //     tableauMap.add(i);
//             // }
//             // peer.sendPartialFile("localhost", 41871,1,tableauMap);

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }



// }