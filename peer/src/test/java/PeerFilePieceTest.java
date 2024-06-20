
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.LinkedList;
import java.io.File;
import java.util.regex.Pattern; 
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.peer.FilePiece;
import org.peer.FilePieceData;
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

public class PeerFilePieceTest {


    static File test = new File("./src/main/java/org/peer/FilePeer");
    static File[] files = test.listFiles();



    static public void main(String[] args) {
        try{
            //Test constructeur numéro de FilePiece
            FilePiece filePiece = new FilePiece(files[0].getAbsolutePath(), 1);
            String log = filePiece.getFilename() + " " + filePiece.getKey() + " " + filePiece.getPieceSize() + " " + filePiece.getPieceNumber() + ""; 
            System.out.println(log);
            System.out.println("\n\n\n\n\n");
            System.out.println("Test cutFile method\n");

            //Test cutFile pour diviser un fichier en Pieces
            filePiece.cutFile(files[0].getAbsolutePath());
            log = filePiece.getFilename() + " " + filePiece.getKey() + " " + filePiece.getPieceSize() + " " + filePiece.getPieceNumber() + ""; 
            System.out.println(log);
            System.out.println("\n\n\n\n\n");
            System.out.println("Test HaveAllFilePieces method\n");

            //Test HaveAllFilePices qui itére sur la bufferMap (permet aussi de tester HavePiece)
            System.out.println(filePiece.HaveAllFilePieces());


            System.out.println("\n\n\n\n\n");
            System.out.println("Test getPieces method\n");

            //Test getPieces avec pieces et HavePiece
            
            ArrayList<Integer> tableauMap = new ArrayList<Integer>();
            for(int i = 0 ; i < 30 ; i++) {
                tableauMap.add(i);
            }
            Map<Integer,FilePieceData> pieceMap = filePiece.getPiecesToSend(tableauMap);
            ArrayList <byte[]> pieceByte = filePiece.getAllPieces();
            System.out.println("La taille de pieceByte est de " + pieceByte.size());    

            for (int i = 0; i < 30; i++) {
                if(pieceMap.get(i).getData() == pieceByte.get(i)) {
                    System.out.println("OK Les deux tableaux sont égaux pour l'index " + i);
                } else {
                    System.out.println("KO Les deux tableaux ne sont pas égaux pour l'index " + i);
                }
            }

            // pieceMap.forEach((k,v) -> System.out.println("Key : " + k + " Value : " + v));
            // //Test de setPieces avec un deuxième filePiece celui de file[1]
            // System.out.println("\n\n\n\n\n");
            // FilePiece filePiece2 = new FilePiece(files[1].getAbsolutePath(), 1);
            // log = filePiece2.getFilename() + " " + filePiece2.getKey() + " " + filePiece2.getPieceSize() + " " + filePiece2.getPieceNumber() + "";
            // System.out.println(log);

            // System.out.println("\n\n\n\n\n");
            // System.out.println("comparaison avec le premier filePiece sur les 30 premiers pour vérifier setPieces\n");

            // ArrayList<byte[]> pieceByte2 = filePiece2.getAllPieces();
            // filePiece2.setPieces(pieceMap);
            // for(int i = 0 ; i < 30 ; i++) {
            //     if(pieceByte2.get(i) == pieceMap.get(i)) {
            //         System.out.println("OK Les deux tableaux sont égaux pour l'index " + i);
            //     } else {
            //         System.out.println("KO Les deux tableaux ne sont pas égaux pour l'index " + i);
            //     }
            // }

            // //Test addPiece avec un troisième filePiece celui de file[2]
            // System.out.println("\n\n\n\n\n");
            // FilePiece filePiece3 = new FilePiece(files[2].getAbsolutePath(), 1);
            // log = filePiece3.getFilename() + " " + filePiece3.getKey() + " " + filePiece3.getPieceSize() + " " + filePiece3.getPieceNumber() + "";
            // System.out.println("\n\n\n\n\n");
            // System.out.println(log);
            // System.out.println("\n\n\n\n\n");

            // //ON ajoute Toutes les pieces du 3 dans le 1 et on vérifie piece et la buffer Map
            // ArrayList<byte[]> pieceByte3 = filePiece3.getAllPieces();
            // for(int i = 0 ; i < pieceByte3.size(); i++) {
            //     filePiece.addPiece(i,pieceByte3.get(i));
            // }

            // for(int i = 0 ; i < pieceByte.size(); i++){
            //     if(pieceByte.get(i) == pieceByte3.get(i)) {
            //         System.out.println("OK Les deux tableaux sont égaux pour l'index " + i);
            //     } else {
            //         System.out.println("KO Les deux tableaux ne sont pas égaux pour l'index " + i);
            //     }   
            // }

            // assert(filePiece3.HaveAllFilePieces() == true);
            // assert(filePiece3.HavePiece(0) == true);
            // assert(filePiece3.HavePiece(1) == true);
            // assert(filePiece3.getPieceNumber() == pieceByte3.size());


        } catch (IOException e) {
            e.printStackTrace();
        }    
    }
}