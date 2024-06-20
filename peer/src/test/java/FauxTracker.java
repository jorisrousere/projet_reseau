import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.InputStreamReader;

class FauxTracker{
    public static void main(String[] args){
        try{
            ServerSocket serv = new ServerSocket(3000);

            Socket clientSocket = serv.accept();
            System.out.println("Connexion acceptée de " + clientSocket.getRemoteSocketAddress());
            try{
                BufferedReader plec = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter pred = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
                String inputLine = null;
                while ((inputLine=plec.readLine()) != null) {
                        // Handle exit command
                        if ("exit".equalsIgnoreCase(inputLine)) {
                            System.out.println("Déconnexion du client " + clientSocket.getRemoteSocketAddress());
                            break;
                        }
                        String[] parts = inputLine.split(" ");
                        switch (parts[0]) {
                            case "announce":
                                pred.println("yes");
                                break;
                            case "look":
                                pred.println("yes");
                                break;
                            case "getfile":
                                pred.println("yes");
                                break;
                            case "interested":
                                pred.println("yes");
                                break;
                            default:
                                pred.println("no");
                                break;
                        }
                    }
                pred.close();
                plec.close();
                serv.close();
            }catch (Exception e){
                System.err.println("Erreur du serveur: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Erreur du serveur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}