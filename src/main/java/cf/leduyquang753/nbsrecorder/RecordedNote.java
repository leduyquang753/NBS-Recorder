package cf.leduyquang753.nbsrecorder;

public class RecordedNote {
	private int tick;
	private String sound;
	private float pitch;
	
	public RecordedNote(int tick, String sound, float pitch) {
		setTick(tick);
		setSound(sound);
		setPitch(pitch);
	}

	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	
}
