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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.Serializable;


public class FilePieceData implements Serializable {
    
    private static final long serialVersionUID = 1L;  // Recommended to ensure version compatibility
    
    private byte[] data;
    private BufferMap bufferMap;
    
    public FilePieceData(byte[] data, BufferMap bufferMap) {
        this.data = data;
        this.bufferMap = bufferMap;
    }
    
    public byte[] getData() {
        return data;
    }

    public BufferMap getBufferMap() {
        return bufferMap;
    }
    
    public String getFileKey() {
        return bufferMap.getKey();
    }
    
}
