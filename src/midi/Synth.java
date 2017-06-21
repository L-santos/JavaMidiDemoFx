package midi;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

/**
 * Created by lucas on 20/06/17.
 */

public class Synth {
    private static Synthesizer defaultSynth;
    private static Soundbank currentSoundBank;
    private static MidiChannel[] midiChannels;
    private static List<Instrument> instruments;

    public static void init() throws MidiUnavailableException {
        defaultSynth = MidiSystem.getSynthesizer();
        defaultSynth.open();
        midiChannels = defaultSynth.getChannels();
        currentSoundBank = defaultSynth.getDefaultSoundbank();
        instruments = reloadInstruments();
    }

    private static List<Instrument> reloadInstruments() {
        return Arrays.asList(currentSoundBank.getInstruments());
    }

    public static void setInstrument(int program){
        midiChannels[0].programChange(program);
        NoteOn();
    }

    public static void NoteOn() {
        midiChannels[0].noteOn(24, 100);
        midiChannels[0].noteOff(24, 0);
    }

    public static void NoteOn(String note, int octave, int velocity){
        int noteNumber = octave * 12 + Integer.parseInt(note);
        midiChannels[0].setSolo(true);
        midiChannels[0].setChannelPressure(10);

        midiChannels[0].noteOn(noteNumber, velocity);

    }

    public static void NoteOff(String id, int i){

    }

    public static List<String> getInstruments() {
        List<String> instrumentList = new ArrayList<String>();
        for (Instrument i: instruments) {
            instrumentList.add(String.format("%d - %s", i.getPatch().getProgram() ,i.getName()));
        }

        return instrumentList;
    }

    public static String getSynthInfo(){
        return String.format("%s [%s]", defaultSynth.getDeviceInfo().getName(), defaultSynth.getDeviceInfo().getDescription());
    }

}
