/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dec2midi;

/**
 *
 * @author Wizard
 */

import dec2midi.data.NumberStringConvertor;
import dec2midi.data.NumberStringSource;
import dec2midi.data.StringNumber;
import dec2midi.data.StringNumberDuodecadic;
import dec2midi.filewriter.MyFileWriter;
import dec2midi.midi.MidiEngine;
import dec2midi.midi.internal.NoteListGenerator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequencer;


public class FXMLController {
     
    public FXMLController() {}

    @FXML
    private ChoiceBox<NoteListGenerator.Extra> choiceBox_extraSelect;
    
    @FXML
    private ChoiceBox<Integer> choiceBox_OctaveSelect;
        
    @FXML
    private ChoiceBox<String> choiceBox_noteDurationString;
    
    @FXML
    private ChoiceBox<NoteListGenerator.Scale> choiceBox_ScaleSelect;
    
    @FXML
    private ChoiceBox<NoteListGenerator.RootKey> choiceBox_rootNote;

    @FXML
    public TextArea textArea_input;

    @FXML
    private TextArea textArea_conversion;
    
    @FXML
    private TextArea textArea_output;

    @FXML
    private TextField textField_numberCount;
    
    @FXML
    private TextField textField_decimalCount;
    
    @FXML
    private TextField textField_numberName;
    
    @FXML
    private TextField textField_iterationCount;
    
    @FXML
    private TextField textField_tempoDisplay;

    @FXML
    private Button button_setSqrtTwo;

    @FXML
    private Button button_setPi;

    @FXML
    private Button button_setEul;
    
    @FXML
    private Button button_setRandom;
    
    @FXML
    private Button button_setNew;

    @FXML
    private Button button_convertOctal;

    @FXML
    private Button button_saveAs;

    @FXML
    private Button button_convertDuodec;
    
    @FXML
    private Button button_setIterDC;
    
    @FXML
    private Button button_setIterMax;
    
    @FXML
    private Button button_setIter1024;  
    
    @FXML
    private Button button_setIter128;

    @FXML
    private Button button_generateMidiData;    
    
    @FXML
    private Button button_generateAndPlay;
    
    @FXML
    private Button button_play;
        
    @FXML
    private Button button_stop;
    
    @FXML
    private Slider slider_tempo;

   
    
    private NumberStringSource nss;
    private NumberStringConvertor nsc;
    private MidiEngine me;
    private MyFileWriter mfw; 
    private NoteListGenerator nlg;
    private Sequencer sequencer;
    

    
    private boolean tools = false;
    
    private Thread thread;
    private Stage stage;
    private Scene scene;
    private Scene sceneWait;
    
    private StringNumber dsn;
    private StringNumber psn;
    private String trackText;  

    
        public void init(NumberStringSource nss, NumberStringConvertor nsc, MidiEngine me, MyFileWriter mfw, Sequencer sequencer) {
        this.nss = nss;
        this.nsc = nsc;
        this.me = me;
        this.mfw = mfw;
        this.nlg = me.getNoteListGenerator();
        this.sequencer = sequencer;
    
        tools = true;
        textArea_inputListenerInit();   
        textField_numberCount.setText("0");
        textField_decimalCount.setText("0");
        textField_iterationCount.setText(String.valueOf(nsc.getIterationCount()));
        textField_iterationCountListenerInit();          
        choiceBoxInit();
        sliderInit();
        textField_tempoDisplay.setText(Integer.parseInt(me.getTempo(), 16)+" uSec/crotchet");
  
    }
        public void initStageSwitch(Stage stage, Scene scene, Scene sceneWait) {
        this.stage = stage;
        this.scene = scene;
        this.sceneWait = sceneWait;
        
        }
        
        
        private void textField_iterationCountListenerInit(){
        
        textField_iterationCount.textProperty().addListener((observable, oldValue, newValue) -> {
        
            String regValue = newValue.replaceAll("[^0-9]", "");
            boolean empty = false;
            if(regValue.equals("")) {textField_iterationCount.setText(oldValue); empty=true;}
            if(!empty){
            int newIter = Integer.parseInt(regValue);
            if (newIter>=0 && newIter<= NumberStringConvertor.MAXITERATIONS) {textField_iterationCount.setText(regValue);}
            else {textField_iterationCount.setText(oldValue);}
                      } 
        });
   
        }
        
        
        private void textArea_inputListenerInit(){      
                  textArea_input.textProperty().addListener(((observable, oldValue, newValue) -> {          
              if(dsn==null) {
                  dsn = new StringNumber("New number", "");}              
                  if (textField_numberName.getText().equals("")) {                
                 textField_numberName.setText("New number");
                
              }
                  
              if(newValue !=null) {
     dsn.setAndTreatValue(newValue);
     textArea_input.setText(dsn.getValue());
     
        if(dsn.getWholePart().length()>18){textField_numberCount.setText(String.valueOf(dsn.getWholePart().length())+"!!!"); textField_numberCount.setStyle("-fx-text-fill: red;");} 
        else {textField_numberCount.setText(String.valueOf(dsn.getWholePart().length())); textField_numberCount.setStyle("-fx-text-fill: black;");}
        if(dsn.getDecimalPart()!=null){textField_decimalCount.setText(String.valueOf(dsn.getDecimalPart().length()));} else {textField_decimalCount.setText("0");}        
    decimalCountToIterationCount();
              }
        
        }));
        
        }
        
                    private void decimalCountToIterationCount() {
    
        Integer dc = Integer.valueOf(textField_decimalCount.getText());
if (dc>0 && dc<= NumberStringConvertor.MAXITERATIONS) {textField_iterationCount.setText(textField_decimalCount.getText());}
else { if(dc>NumberStringConvertor.MAXITERATIONS) {textField_iterationCount.setText(String.valueOf(NumberStringConvertor.MAXITERATIONS));} }
           if (dc<=0) {textField_iterationCount.setText("0"); }           
    }
        
                    private void sliderInit(){
                    slider_tempo.setValue(0.5);
                    slider_tempo.setShowTickLabels(true);
slider_tempo.setShowTickMarks(true);
slider_tempo.setMajorTickUnit(0.25);
slider_tempo.setMinorTickCount(2);
slider_inputListenerInit();
                        
                    }
                    
                    
                            private void slider_inputListenerInit(){      
                  slider_tempo.valueProperty().addListener(new ChangeListener<Number>() {
                      @Override
                      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                          try {
                          me.setTempo((double) newValue);
                              textField_tempoDisplay.setText(Integer.parseInt(me.getTempo(), 16)+" uSec/crotchet");   
                          } catch (Exception ex) {
                             
                          }                        
                      }
                  });
                
              }
                    
        private void choiceBoxInit(){
        
            
            ObservableList<NoteListGenerator.RootKey> rootKeyList = FXCollections.observableArrayList();
            rootKeyList.addAll(NoteListGenerator.RootKey.values());
            
        choiceBox_rootNote.setItems(rootKeyList);
        choiceBox_rootNote.setValue(nlg.getRootKey());
        
        ObservableList<NoteListGenerator.Scale> scaleList = FXCollections.observableArrayList();
        scaleList.addAll(NoteListGenerator.Scale.values());

        choiceBox_ScaleSelect.setItems(scaleList);
        choiceBox_ScaleSelect.setValue(nlg.getScale());
        
        ObservableList<NoteListGenerator.Extra> extraList = FXCollections.observableArrayList();
        extraList.addAll(NoteListGenerator.Extra.values());
        
        choiceBox_extraSelect.setItems(extraList);
        choiceBox_extraSelect.setValue(nlg.getExtra());
        
        ObservableList<Integer> octaveList = FXCollections.observableArrayList();
        octaveList.addAll(0,1,2,3,4,5,6,7,8);
        
        choiceBox_OctaveSelect.setItems(octaveList);
        choiceBox_OctaveSelect.setValue(nlg.getOctave());
        
        ObservableList<String> noteDurationList = FXCollections.observableArrayList();
        noteDurationList.addAll("Quarter","Half","Full","Double","Quad");
        
        choiceBox_noteDurationString.setItems(noteDurationList);
        choiceBox_noteDurationString.setValue(nlg.getNoteDurationString());
        
        }
        

        
            @FXML
    void setIterDC(ActionEvent event) {       
    decimalCountToIterationCount();
    }  
        
            @FXML
    void setIterMax(ActionEvent event) {
             
textField_iterationCount.setText(String.valueOf(nsc.getMaxIterations()));

    }

    @FXML
    void setIter128(ActionEvent event) {
textField_iterationCount.setText("128");
    }

    @FXML
    void setIter1024(ActionEvent event) {
textField_iterationCount.setText("1024");
    }

     
        
                    @FXML
    void setInputNew(ActionEvent event) {

        dsn = new StringNumber("New number", "");
    textArea_input.setText("New number");
    textField_numberCount.setText("0");
    textField_decimalCount.setText("0");
    textField_iterationCount.setText("0");
    textField_numberName.setText(dsn.getName());      
    }
    
 
    private void setInputNumber(String name){
            if(tools){    
                
            dsn = new StringNumber(nss.getStringNumberByName(name));
            textArea_input.setText(dsn.getValue());
            textField_numberName.setText(dsn.getName());              
           textField_numberCount.setText(String.valueOf(dsn.getWholePart().length()));
            textField_decimalCount.setText(String.valueOf(dsn.getDecimalPart().length()));
            
        }
    }
 
    @FXML
    void setInputPi(ActionEvent event) {
      
        setInputNumber("Pi");
    }

    @FXML
    void setInputEul(ActionEvent event) {
        
        setInputNumber("Euler");
        
    }

    @FXML
    void setInputSqrtTwo(ActionEvent event) {

        setInputNumber("Sqrt Two");    
    }

    @FXML
    void setRandomInput(ActionEvent event) {
        
        if(tools){
        int RandomIndex = (int) (Math.random() * nss.getList().size());        
        setInputNumber(nss.getList().get(RandomIndex).getName());
        }  
    }
        

    
    @FXML
    void convertInputDuodec(ActionEvent event) throws Exception {
        
 convertInput(12);
choiceBox_ScaleSelect.setDisable(true);

    }
    
    

    @FXML
    void convertInputOctal(ActionEvent event) throws Exception {
       
 convertInput(8);
choiceBox_ScaleSelect.setDisable(false);

        } 
    
    
    
    
    private void convertInput(int base) {
    
         
        
        if(dsn!=null && !dsn.getValue().equals("") && !dsn.getValue().equals(".") && (base==8 || base==12)){
            
             
            if(dsn.getWholePart().length()>18){alertError("Wrong input number", "Natural (whole) part of number is too long");}
            else {
                
                 checkIterations();
                 nsc.setIterationCount(Integer.parseInt(textField_iterationCount.getText()));
                 dsn.setName(textField_numberName.getText());
            
        Task task = new Task<Void>() {
    @Override public Void call() throws Exception {    
        switch (base) {
            case 8:
                 psn = nsc.decadic2Octal(dsn);
                break;
            case 12: psn = nsc.decadic2DuoDecadic(dsn);        
            break;
            default:
                throw new AssertionError();
        }
        
        textArea_conversion.setText(psn.getValue());      

     return null; 
    }   };
        
        stage.setScene(sceneWait);
        thread = new Thread(task);
        thread.start();
        
        task.setOnSucceeded(e -> {
            stage.setScene(scene);
        
});
        task.setOnCancelled(e -> {
            alertError("Task was cancelled","Something went wrong durring processing");
            stage.setScene(scene);
        
});
        task.setOnFailed(e -> {
            alertError("Task failed","Something went wrong durring processing");
            stage.setScene(scene);
        
});
        
      
        }}else {
         alertError("You didn't set input number", "It simply can't work without data. ");
                
        }

    }
    
    
    private void checkIterations() {
    Integer dc = Integer.valueOf(textField_decimalCount.getText());
    Integer ic = Integer.valueOf(textField_iterationCount.getText());
       
    if(ic>dc) {
        alertWarning("Loss of precision", "You didn't provided enough decimal spaces for setted amount of iterations.");
    
    }
    
    }
    
   
        
        private void alertWarning(String headerText, String contentText){ 
      Alert alert = new Alert(Alert.AlertType.WARNING);
alert.setTitle("Warning");
alert.setHeaderText(headerText);
alert.setContentText(contentText);
alert.showAndWait();
        }
        
        private void alertError(String headerText, String contentText){ Alert alert = new Alert(Alert.AlertType.ERROR);
alert.setTitle("Error");
alert.setHeaderText(headerText);
alert.setContentText(contentText);
alert.showAndWait();
        }
        
        
private void generateMidi() throws Exception {

            if(psn != null){
            
            nlg.setRootKey(choiceBox_rootNote.getValue());
            nlg.setOctave(choiceBox_OctaveSelect.getValue());
            nlg.setScale(choiceBox_ScaleSelect.getValue());
            nlg.setNoteDurationString(choiceBox_noteDurationString.getValue());
            nlg.setExtra(choiceBox_extraSelect.getValue());
            
            if(psn instanceof StringNumberDuodecadic){
        trackText = "Midification of " + psn.getName()+" ("+nlg.getRootKey()+nlg.getOctave()+")";} else {
             trackText = "Midification of " + psn.getName()+" in ("+nlg.getRootKey()+nlg.getOctave()+" "+nlg.getScale()+")";}
            
            
        me.generateMidiHexString(psn, trackText);
        String str = me.getHexString().toUpperCase().replaceAll("(.{4})(?!$)", "$1 - ");
        textArea_output.setText(str);
        } else {
            
            alertWarning("You didn't convert any number", "You have to convert input decadic number into duodecadic or octal form to generate midi data.");
        
        
        }

}

private void play() throws IOException, InvalidMidiDataException {

    if(me.getOutput()!=null){
        sequencer.setSequence(new ByteArrayInputStream(me.getOutput()));
        sequencer.start();}
    
}
     
    @FXML
    void generateMidiData(ActionEvent event) throws Exception {       
generateMidi();
    }
    
    
        @FXML
    void generateAndPlay(ActionEvent event) throws IOException, InvalidMidiDataException, Exception {
        generateMidi();
            play();

    }

    @FXML
    void myPlay(ActionEvent event) throws IOException, InvalidMidiDataException {
 play();
    }

    @FXML
    void myStop(ActionEvent event) {
        if(sequencer.isRunning()){
        sequencer.stop();}
    }
    
    
    @FXML
    void writeFileAs(ActionEvent event) throws IOException {
       if(me.getOutput()!=null){
                        
FileChooser fileChooser = new FileChooser();
fileChooser.setTitle("Save as");
fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Midi file (*.mid)", "*.mid"));
fileChooser.setInitialFileName(trackText+".mid");

String userDirectoryString = System.getProperty("user.home");
File userDirectory = new File(userDirectoryString);
if(!userDirectory.canRead()) {
    userDirectory = new File("c:/");
}
fileChooser.setInitialDirectory(userDirectory);

File file = fileChooser.showSaveDialog(stage);
mfw.writeMidiFile(file, me.getOutput());
        
        } else { alertError("No midi data", "You have to generate midi data to write file");}      
    }
    
    

}
