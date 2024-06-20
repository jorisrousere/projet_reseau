package org.peer;

import java.io.Serializable;  // Import nécessaire pour Serializable

public class BufferMap implements Serializable {
    private static final long serialVersionUID = 1L;  // Ajout d'un serialVersionUID pour la gestion de version

    private boolean[] bufferMap;
    private String key;

    public BufferMap(int numPieces, String key) {
        this.bufferMap = new boolean[numPieces];
        this.key = key;
    }

    public void setPiecePresent(int pieceIndex) {
        if (pieceIndex >= 0 && pieceIndex < bufferMap.length) {
            bufferMap[pieceIndex] = true;
        }
    }

    public void setPieceAbsent(int pieceIndex) {
        if (pieceIndex >= 0 && pieceIndex < bufferMap.length) {
            bufferMap[pieceIndex] = false;
        }
    }

    public int getMapSize() {
        return bufferMap.length;
    }

    public boolean isPiecePresent(int pieceIndex) {
        return pieceIndex >= 0 && pieceIndex < bufferMap.length && bufferMap[pieceIndex];
    }

    public boolean areAllPiecesPresent() {
        for (boolean piece : bufferMap) {
            if (!piece) {
                return false;
            }
        }
        return true;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (boolean piece : bufferMap) {
            sb.append(piece ? "1" : "0");
        }
        return sb.toString();
    }
}
