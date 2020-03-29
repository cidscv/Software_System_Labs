package sample;

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Server extends Application {

    private TextArea ta = new TextArea();
    private int clientNo = 0;

    @Override
    public void start(Stage primaryStage) {

        GridPane grid = new GridPane();
        Button btExit = new Button("Exit");
        grid.add(ta, 0, 0);
        grid.add(btExit, 0, 1);

        Scene scene = new Scene(grid, 450, 200);
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        btExit.setOnAction(e -> {
            System.exit(0);
        });

        new Thread( () -> {
            try {

                ServerSocket serverSocket = new ServerSocket(8000);

                ta.appendText("MultiThreadServer started at " + new Date() + '\n');

                while (true) {

                    Socket socket = serverSocket.accept();

                    clientNo++;

                    Platform.runLater( () -> {
                        ta.appendText("Starting thread for client " + clientNo + " at " + new Date() + '\n');

                        InetAddress inetAddress = socket.getInetAddress();
                        ta.appendText("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
                        ta.appendText("Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + "\n");
                    });


                    new Thread(new HandleClient(socket)).start();
                }
            }
            catch(IOException ex) {
                System.err.println(ex);
            }
        }).start();

    }

    class HandleClient implements Runnable {
        private Socket socket;

        public HandleClient(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {

                DataInputStream inputFromClient = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(
                        socket.getOutputStream());


                while (true) {

                    String username = inputFromClient.readUTF();
                    String message = inputFromClient.readUTF();

                    Platform.runLater(() -> {
                        ta.appendText(username + ": " + message + '\n');
                    });
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
