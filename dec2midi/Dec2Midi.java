/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dec2midi;


import dec2midi.midi.MidiEngine;
import dec2midi.data.NumberStringConvertor;
import dec2midi.data.NumberStringSource;
import dec2midi.filewriter.MyFileWriter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

/**
 *
 * @author Wizard
 * 
 * 
 * Application that allow transformation of decadic number with lots of decimal places into midi data in various note forms.
 * 
 */
public class Dec2Midi extends Application {
    
    NumberStringSource numberStringSource = new NumberStringSource();
    NumberStringConvertor numberStringConvertor = new NumberStringConvertor(1024);
    MidiEngine midiEngine = new MidiEngine();
    MyFileWriter myFileWriter = new MyFileWriter();
    
    @Override
    public void start(Stage stage) throws Exception {
       
        
         FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));   
         FXMLLoader loaderWait = new FXMLLoader(getClass().getResource("FXMLDocument_wait.fxml"));
         Parent root = (Parent) loader.load();
         Parent wait = (Parent) loaderWait.load();
         FXMLController controller = loader.getController();
         
         Sequencer sequencer = MidiSystem.getSequencer();
         sequencer.open();
         controller.init(numberStringSource, numberStringConvertor, midiEngine, myFileWriter, sequencer);
        Scene scene = new Scene(root);
        Scene sceneWait = new Scene(wait);
        controller.initStageSwitch(stage, scene, sceneWait);
        stage.setScene(scene);
        stage.setTitle("Decadic decimal MIDI conventor");
        stage.show();
       
        stage.setOnCloseRequest((WindowEvent t) -> {
            Platform.exit();
            System.exit(0);
         });
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {      
        launch(args);
    }
    
    
    
    
    
    

    
    
    
    
}
