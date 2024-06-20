package org.peer;
import java.util.ArrayList;
import java.util.Base64;

import org.peer.AbstractPeer;

public class MessageGenerator {


    public String generateHaveMessage(String keyFile, BufferMap buffermap) {
        StringBuilder message = new StringBuilder();
        message.append("have [")
                .append(keyFile)
                .append("] %")
                .append(buffermap.toString())
                .append("%");
        return message.toString();
    }

    public String generateUpdateSeedMessage(String[] keyFilesSeed, String[] keyFilesLeech) {
        StringBuilder message = new StringBuilder();
        message.append("update seed [");
        for (String key : keyFilesSeed) {
            message.append(key).append(" ");
        } 
        if (keyFilesSeed.length > 0) {
            message.deleteCharAt(message.length() - 1); //Remove the excessive space
        }

        message.append("] leech [");
        for (String key : keyFilesLeech) {
            message.append(key).append(" ");
        }
        if (keyFilesLeech.length > 0) {
            message.deleteCharAt(message.length() - 1); //Remove the excessive space
        }
        message.append("]");
        return message.toString();
    }


    public String generateDataMessage(String keyFile, ArrayList<Integer> indices, ArrayList<byte[]> pieces) { 
        StringBuilder message = new StringBuilder();
        message.append("data ")
                .append(keyFile)
                .append(" [");
        for (int i = 0; i < indices.size(); i++) {
            message.append(indices.get(i))
                    .append(":%");
            byte[] currentPiece = pieces.get(i);
            for (int j = 0; j < currentPiece.length; j++) {
                message.append(currentPiece[j]);
            }
                message.append("% ");
        }
        if (indices.size() > 0) {
            message.deleteCharAt(message.length() - 1); // Remove the excessive space
        }
        message.append("]");
        return message.toString();
    }


}
