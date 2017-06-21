package ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import midi.Player;
import midi.Synth;

import javax.sound.midi.MidiUnavailableException;
import java.io.File;
import java.io.IOException;

public class Controller {

    @FXML private ComboBox instrumentBox;
    @FXML private ComboBox deviceBox;
    @FXML private GridPane keyboardPane;
    @FXML private ComboBox octaveSelector;
    @FXML private Slider volumeSlider;
    @FXML private MenuItem menuOpen;
    @FXML private Pane rootPane;
    @FXML private Text txtTrackInfo;
    @FXML private MenuItem menuClose;

    private ObservableList instrumentList;
    private ObservableList deviceList;

    @FXML
    public void initialize(){

        try {
            Synth.init();
            Player.init();
        }catch (MidiUnavailableException e){
            //TODO
        }

        instrumentList = FXCollections.observableList(Synth.getInstruments());
        instrumentBox.setItems(instrumentList);
        instrumentBox.setValue(instrumentBox.getItems().get(0));

        deviceBox.setValue(Synth.getSynthInfo());


        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(70);
        volumeSlider.setShowTickMarks(true);

        configureKeyboard();

        ObservableList<String> octaves = FXCollections.observableArrayList();
        for(int i = 0; i < 11; i++){
            octaves.add(i+"");
        }

        octaveSelector.setItems(octaves);
        octaveSelector.setValue(octaves.get(4));


        instrumentBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Synth.setInstrument(instrumentBox.getItems().indexOf(instrumentBox.getValue()));
            }
        });


        menuOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser chooser = new FileChooser();
                chooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("MIDI", "*mid")
                );
                File file = chooser.showOpenDialog((Stage) rootPane.getScene().getWindow());
                if(file != null)
                try {
                    Player.play(file);
                    txtTrackInfo.setText(Player.getInfo());
                }catch (IOException e) {
                    //TODO
                }
            }
        });

        menuClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Player.stop();
                txtTrackInfo.setText("");
            }
        });
    }


    private void configureKeyboard(){

        ObservableList<Node> buttons = keyboardPane.getChildren();

        for (int i = 0; i < buttons.size(); i++){
            Button button = (Button) buttons.get(i);
            button.setId(""+i);
            button.pressedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    int octave = Integer.parseInt(octaveSelector.getValue().toString());
                    int volume = (int) volumeSlider.getValue() * 2;
                    if (newValue){
                        Synth.NoteOn(button.getId(), octave, volume);
                    }else {
                        Synth.NoteOn(button.getId(), octave, 0);
                    }
                }
            });
        }
    }
}
