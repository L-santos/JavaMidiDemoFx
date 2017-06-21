package midi;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by lucas on 20/06/17.
 */
public class Player {

    private static Sequencer sequencer;
    private static String fileName;

    public static void play(File file) throws IOException {
        try{
            Sequence sequence = MidiSystem.getSequence(file);
            fileName = file.getName();
            sequencer.setSequence(sequence);
            sequencer.open();
            sequencer.start();
        } catch (InvalidMidiDataException e) {
            throw new IOException("Illegal midi file");
        } catch (MidiUnavailableException e) {
            //TODO
        }


    }

    public static void stop(){
        if(sequencer.isRunning()) {
            sequencer.stop();
            sequencer.close();
        }
    }

    public static String getInfo(){
        Sequence sqn = sequencer.getSequence();

        float divisionType = sqn.getDivisionType();
        int resolution = sqn.getResolution();
        int tracks = sqn.getTracks().length;
        long tickLength = sqn.getTickLength();
        int tempoInBPM = (int)sequencer.getTempoInBPM();

        double ticksPerSecond = resolution * (tempoInBPM / 60.0);
        double tickSize = 1.0 / ticksPerSecond;

        String info = String.format("File: %s%nTracks: %d - Tempo: %d%n"+
                        "DivisionType %f - Resolution %d%n"+
                        "TicksPSec: %f - TickSize: %f",
                fileName, tracks, tempoInBPM, divisionType, resolution, ticksPerSecond, tickSize);

        return info;
    }

    public static void init() throws MidiUnavailableException {
        sequencer = MidiSystem.getSequencer();
    }
}
