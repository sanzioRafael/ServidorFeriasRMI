package servidor;
import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.swing.JOptionPane;


public class ServerGUI extends Application{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnSelectServerFile;

    @FXML
    private Button btnStartServer;

    @FXML
    private TextField tfServerAddress;

    @FXML
    private TextField tfServerFile;

    @FXML
    private TextField tfServerPort;

    Stage stage;
    int port;

    @FXML
    void btnSelectServerFileOnAction(ActionEvent event) {
    	DirectoryChooser chooser = new DirectoryChooser();
    	chooser.setTitle("Servidor RMI");
    	File selectedDirectory = chooser.showDialog(null);
    	
    	if(selectedDirectory != null) tfServerFile.setText(selectedDirectory.getPath());
    }

    @FXML
    void btnStartServerOnAction(ActionEvent event) throws RemoteException{
    	port = Integer.parseInt(tfServerPort.getText());
    	String path = tfServerFile.getText();
    	
    	Server server = new Server();
    	
    	if(server.startServer("server",port, path)){
    		JOptionPane.showMessageDialog(null, "conectou");
    	}
    }

    @FXML
    void initialize() {
    }

	@Override
	public void start(Stage stage) throws Exception {
		try {
			//ABRE A INTERFACE GRÁFICA
			AnchorPane pane = (AnchorPane) FXMLLoader.load(getClass().getResource("ServerGUI.fxml"));
			Scene scene = new Scene(pane);
			stage.setScene(scene);
			stage.getIcons().add(new Image("img/Home-Server-icon.png"));
			stage.show();
			this.stage = stage;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
    
    

}
