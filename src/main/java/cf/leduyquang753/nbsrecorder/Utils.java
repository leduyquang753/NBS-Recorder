package cf.leduyquang753.nbsrecorder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cf.leduyquang753.nbsapi.Instrument;
import cf.leduyquang753.nbsapi.Layer;
import cf.leduyquang753.nbsapi.Note;

public class Utils {
	public static int div = 4;
	public static boolean sort = true;
	public static Comparator<RecordedNote> comp = (n1, n2) -> new Integer(getPitch(n2.getPitch())).compareTo(new Integer(getPitch(n1.getPitch())));
	
	public static Instrument getInstrument(String name) {
		switch (name) {
		case "note.bassattack": return Instrument.BASS;
		case "note.bd": return Instrument.DRUM;
		case "note.snare": return Instrument.SNARE;
		case "note.hat": return Instrument.CLICK;
		}
		return Instrument.HARP;
	}
	
	public static boolean isNote(String name) {
		if (name.equalsIgnoreCase("note.bassattack")) return true;
		if (name.equalsIgnoreCase("note.bd")) return true;
		if (name.equalsIgnoreCase("note.snare")) return true;
		if (name.equalsIgnoreCase("note.hat")) return true;
		if (name.equalsIgnoreCase("note.harp")) return true;
		return false;
	}
	
	public static int getPitch(float speed) {
		if (speed < 0.51) return 33;
		if (speed < 0.54) return 34;
		if (speed < 0.57) return 35;
		if (speed < 0.60) return 36;
		if (speed < 0.65) return 37;
		if (speed < 0.68) return 38;
		if (speed < 0.73) return 39;
		if (speed < 0.78) return 40;
		if (speed < 0.81) return 41;
		if (speed < 0.86) return 42;
		if (speed < 0.92) return 43;
		if (speed < 0.97) return 44;
		if (speed < 1.02) return 45;
		if (speed < 1.10) return 46;
		if (speed < 1.16) return 47;
		if (speed < 1.23) return 48;
		if (speed < 1.31) return 49;
		if (speed < 1.39) return 50;
		if (speed < 1.47) return 51;
		if (speed < 1.56) return 52;
		if (speed < 1.63) return 53;
		if (speed < 1.70) return 54;
		if (speed < 1.85) return 55;
		if (speed < 1.98) return 56;
		return 57;
	}
	
	public static List<Layer> convert(List<RecordedNote> list) {
		List<RecordedNote> harp = new ArrayList<RecordedNote>();
		List<RecordedNote> bass = new ArrayList<RecordedNote>();
		List<RecordedNote> drum = new ArrayList<RecordedNote>();
		List<RecordedNote> snare = new ArrayList<RecordedNote>();
		List<RecordedNote> click = new ArrayList<RecordedNote>();
		for (RecordedNote n : list) {
			switch(getInstrument(n.getSound())) {
			case HARP: harp.add(n); break;
			case BASS: bass.add(n); break;
			case DRUM: drum.add(n); break;
			case SNARE: snare.add(n); break;
			case CLICK: click.add(n); break;
			default: break;
			}
		}
		List<Layer> result = new ArrayList<Layer>();
		result.addAll(arrangeAndConvert(harp));
		result.addAll(arrangeAndConvert(bass));
		result.addAll(arrangeAndConvert(drum));
		result.addAll(arrangeAndConvert(snare));
		result.addAll(arrangeAndConvert(click));
		return result;
	}
	
	public static List<Layer> arrangeAndConvert(List<RecordedNote> list) {
		List<RecordedNote> arranged = new ArrayList<RecordedNote>();
		List<RecordedNote> thisTick = new ArrayList<RecordedNote>();
		int lastTick = -1;
		for (RecordedNote n : list) {
			if (n.getTick()/div != lastTick) {
				lastTick = n.getTick()/div;
				if (sort) thisTick.sort(comp);
				arranged.addAll(thisTick);
				thisTick.clear();
			}
			thisTick.add(n);
		}
		return convertToSongBoard(arranged);
	}
	
	public static List<Layer> convertToSongBoard(List<RecordedNote> list) {
		List<Layer> result = new ArrayList<Layer>();
		int lastTick = -1;
		int currentLayer = -1;
		for (RecordedNote note : list) {
			if (note.getTick()/div != lastTick) {
				currentLayer = -1;
			}
			currentLayer++;
			while (result.size() < currentLayer+1) result.add(new Layer("", (byte) 100));
			result.get(currentLayer).setNote(note.getTick()/div, new Note(getInstrument(note.getSound()), (byte) getPitch(note.getPitch())));
			lastTick = note.getTick()/div;
		}
		return result;
	}
}
