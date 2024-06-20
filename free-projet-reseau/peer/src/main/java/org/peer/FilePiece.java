
package org.peer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.io.File;
import java.util.Collections;


public class FilePiece {

    private String filename;
    private String key;
    private BufferMap bufferMap;
    private int pieceSize; //Taille en Ko
    private int pieceNumber;
    private ArrayList<byte[]> pieces;
 
    public FilePiece(String filename, String key, BufferMap bufferMap, int pieceSize, int pieceNumber) throws IOException {
        this.filename = filename;
        this.key = key;
        this.bufferMap = bufferMap;
        this.pieceSize = pieceSize*1024; //Si l'on met 1 c'est convertie en 1ko avec * 1024 
        this.pieceNumber = pieceNumber; // A definir par nous même 

        this.pieces = new ArrayList<>(Collections.nCopies(pieceNumber, null));
    }

    //Deuxième constructeur pour le cas où le peer possèd déjà le fichier en local
    public FilePiece(String filePath, int pieceSize) throws IOException {
        this.filename = filePath;
        this.pieces = new ArrayList<byte[]>();
        this.key = KeyGen.keysGenerator(filename);
        this.pieceSize = pieceSize*1024; //Si l'on met 1 c'est convertie en 1ko avec * 1024 
        cutFile(filePath);
        //Ici pieceNumber est défini par la taille du fichier
    }

    // cutFile method
    public void cutFile(String filePath) {
        FileInputStream fis = null;
        System.out.println("\nCutting the file"+ filePath + " into pieces of size " + this.pieceSize + " bytes.");
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            byte[] buffer = new byte[this.pieceSize];
            int n;
            int i = 0;
            while ((n = fis.read(buffer)) != -1) {
                // Create a copy of the buffer to store in the list
                byte[] pieceCopy = Arrays.copyOf(buffer, n);
                this.pieces.add(pieceCopy);
                i++;
            }
            // Initialize pieceNumber
            this.pieceNumber = this.pieces.size();

            // Initialize the bufferMap
            this.bufferMap = new BufferMap(this.pieceNumber, this.key);
            for (int j = 0; j < this.pieceNumber; j++) {
                this.bufferMap.setPiecePresent(j);
            }
            System.out.println("File cut into " + this.pieceNumber + " pieces.\n");
        } catch (IOException e) {
            System.err.println("Failed to process the file: " + e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println("Failed to close file input stream: " + e.getMessage());
                }
            }
        }
    }


    public void setPieceNumber(int pieceNumber) {
        this.pieceNumber = pieceNumber;
        if(this.pieces.size() < pieceNumber) {
            this.pieces.ensureCapacity(pieceNumber);
        }
    }


    public boolean HaveAllFilePieces() {
        for (int i = 0 ; i < this.pieceNumber; i ++){
            if(this.bufferMap.isPiecePresent(i) == false){
                return false;
            }
        }
        return true;
    }

    public boolean HavePiece(int index) {
        return this.bufferMap.isPiecePresent(index);
    } 


    public Map<Integer,FilePieceData> getPiecesToSend(ArrayList<Integer> piecesToSend) {
        Map<Integer, FilePieceData> piecesToSendMap = new HashMap<>();
        for (int index : piecesToSend) {
            byte[] pieceData = this.pieces.get(index);
            FilePieceData piece = new FilePieceData(pieceData, this.bufferMap);
            piecesToSendMap.put(index, piece);
        }
        return piecesToSendMap;
    }

    public void setPiecesReceived(Map<Integer, FilePieceData> piecesReceived) {
        // First, ensure the pieces list is large enough to accommodate all indices in the received map.
        int maxIndex = piecesReceived.keySet().stream().max(Integer::compare).orElse(0);
        ensureCapacity(maxIndex + 1);  // Adjust size to handle all indices

        for (Map.Entry<Integer, FilePieceData> entry : piecesReceived.entrySet()) {
            int index = entry.getKey() - 1;  // Adjust for 0-based index if necessary
            if (index >= 0 && index < this.pieces.size()) {  // Check if index is valid
                FilePieceData piece = entry.getValue();
                this.addPiece(index, piece.getData());
            }
        }
    }
    

    // Ensure the pieces ArrayList has at least 'capacity' elements, adding null where necessary.
    private void ensureCapacity(int capacity) {
        while (this.pieces.size() < capacity) {
            this.pieces.add(null);
        }
    }

    public void addPiece(int index, byte[] pieceData) {
        if (index >= 0 && index < this.pieces.size()) {
            this.pieces.set(index, pieceData);
            this.bufferMap.setPiecePresent(index);
        } else {
            System.err.println("Attempted to add piece at invalid index: " + index);
        }
    }

    public ArrayList<byte[]> getAllPieces() {
        return this.pieces;
    }

    public void setAllPieces(ArrayList<byte[]> pieces) {
        this.pieces = pieces;
    }


    public BufferMap getBufferMap() {
        return this.bufferMap;
    }

    public String getFilename() {
        String[] filenameWithoutPath = this.filename.split("/");
        return filenameWithoutPath[filenameWithoutPath.length - 1];
    }

    public int getLength() {
        return this.pieceNumber;
    }

    public int getFileSize(){
        return this.pieceNumber * this.pieceSize; 
    }

    public int getPieceSize() {
        return this.pieceSize;
    }


    public String getKey() {
        return this.key;
    }
    
    public byte[] getPiece(int index){
        return this.pieces.get(index);
    }

    public int getPieceNumber() {
        return this.pieceNumber;
    }
}
