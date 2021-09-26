package messenger;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import static javafx.application.Application.launch;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;



public class Messenger_Client extends Application  {

    static Socket s;
    static DataInputStream din;
    static DataOutputStream dout;
    
    static TextArea messageDisplay;
    
    @Override
    public void start(Stage stage) {
        /*
        =============CLIENT==============
        */
        // SETTINGS + DESIGN
        stage.setTitle("Messenger");
        BorderPane screen = new BorderPane();
        HBox menuView = new HBox();
        BorderPane sendmsg = new BorderPane();
        
        screen.setPrefSize(450, 700);
        menuView.setPadding(new Insets(15));
        menuView.setSpacing(15);
        sendmsg.setPadding(new Insets(15));
        menuView.setStyle("-fx-background-color: #0078ff;"); 
        
        // MENU
        screen.setTop(menuView);
        Label username = new Label("CLIENT");
        
        Font font = Font.font("Verdana", FontWeight.BOLD, 20);
        username.setFont(font);
        
        username.setTextFill(Color.color(1, 1, 1));
        username.setAlignment(Pos.CENTER);
        menuView.getChildren().addAll(username);

        // TEXT AREA
        messageDisplay = new TextArea();
        screen.setCenter(messageDisplay);              
        messageDisplay.setEditable(false);        
        messageDisplay.setPrefSize(450, 450);
        
        // SUBMIT
        Image img = new Image("messenger/Assets/send.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        
        TextField text = new TextField();
        text.setPrefWidth(350);
        text.setPrefHeight(25);
        
        text.setStyle("-fx-background-color: #ffffff;");
        Button send = new Button();
        send.setGraphic(view);
        send.setStyle("-fx-background-color: #ffffff;");
        
        screen.setBottom(sendmsg);
        sendmsg.setStyle("-fx-background-color: #ffffff;");
        sendmsg.setLeft(text);
        sendmsg.setRight(send);
        
        
        send.setOnAction((event) -> {
            try {
                String msgout = "";
                msgout = text.getText().trim();
                dout.writeUTF(msgout);
                text.setText("");
                messageDisplay.setText(messageDisplay.getText().trim() + "\nYou:\t\t" + msgout);
                
            } catch (Exception e) {
                
            }
        });
        
        // FINAL SETUP
        Scene scene = new Scene(screen);
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        // so that the code still runs after class launches
        Thread thread = new Thread(){
            public void run() {
                launch(Messenger_Client.class);
            }
        };
        thread.start();

        String msgin = "";
        try {
            s = new Socket("127.0.0.1", 1201);
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            
            while(true) {
                msgin = din.readUTF();
                messageDisplay.setText(messageDisplay.getText().trim() + "\nServer:\t" + msgin);
            }
            
        } catch (Exception e) {
        
        }
    }
    
}
