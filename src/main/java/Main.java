import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    protected static final int PORT = 8989;

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String words = in.readLine();
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    var pageEntry = engine.search(words);
                    if (pageEntry == null) {
                        out.println("Ничего не удалось найти");
                    } else {
                        out.println(gson.toJson(pageEntry));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Сервер не запускается");
            e.printStackTrace();
        }
    }
}