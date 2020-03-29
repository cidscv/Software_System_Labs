package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Application {

    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane grid = new GridPane();

        Label user = new Label("Username: ");
        TextField username = new TextField();

        grid.add(user, 0, 0);
        grid.add(username, 1, 0);

        Label msg = new Label("Message: ");
        TextField message = new TextField();

        grid.add(msg, 0, 1);
        grid.add(message, 1, 1);

        Button btSend = new Button("Send");
        Button btExit = new Button("Exit");

        grid.add(btSend, 0, 3);
        grid.add(btExit, 0, 4);

        Scene scene = new Scene(grid, 230, 150);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.show();


        btSend.setOnAction(e -> {
            try {

                String usernameofperson = username.getText();
                String sendmessage = message.getText();

                toServer.writeUTF(usernameofperson);
                toServer.flush();
                toServer.writeUTF(sendmessage);
                toServer.flush();

            } catch (IOException ex) {
                System.err.println(ex);
            }
        });

        try {

            Socket socket = new Socket("localhost" , 8000);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex){

        }

        btExit.setOnAction(e -> {
            primaryStage.close();
        });
    }
}
