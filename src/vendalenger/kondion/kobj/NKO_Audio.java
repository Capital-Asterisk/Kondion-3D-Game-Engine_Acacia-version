package vendalenger.kondion.kobj;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.Window;
import vendalenger.kondion.lwjgl.resource.KAudio;
import vendalenger.kondion.objectbase.KObj_Node;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Solid;

public class NKO_Audio extends KObj_Node {
	
	private KAudio sound;
	private boolean sourced = false;
	private int src;
	public float pitch = 1.0f;
	public float volume = 1.0f;
	
	public NKO_Audio() {
		super();
	}
	
	public NKO_Audio(KAudio f) {
		super();
		sound = f;
	}
	
	public void set(KAudio snd) {
		sound = snd;
	}
	
	public void loop(boolean b) {
		AL10.alSourcei(src, AL10.AL_LOOPING, b ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	@Override
	public void update() {
		//System.out.println((AL10.alGetSourcei(src, AL10.AL_PLAYING)));
		defaultUpdate();
		if (parent instanceof KObj_Oriented) {
			AL10.alSource3f(src, AL10.AL_POSITION,
					((KObj_Oriented) parent).transform.m30,
					((KObj_Oriented) parent).transform.m31,
					((KObj_Oriented) parent).transform.m32);
			if (parent instanceof KObj_Solid)
				AL10.alSource3f(src, AL10.AL_VELOCITY,
						((KObj_Solid) parent).velocity.x,
						((KObj_Solid) parent).velocity.y,
						((KObj_Solid) parent).velocity.z);
		} else {
			AL10.alSourcei(src, AL10.AL_SOURCE_RELATIVE, AL10.AL_TRUE);
			AL10.alSourcef(src, AL10.AL_ROLLOFF_FACTOR, 0.0f);
			AL10.alSource3f(src, AL10.AL_POSITION, 0, 0, 0);
		}
		//AL10.alSourcei(src, AL10.AL_SOURCE_RELATIVE, AL10.AL_TRUE);
		//AL10.alSourcef(src, AL10.AL_ROLLOFF_FACTOR, 0.0f);
		AL10.alSourcef(src, AL10.AL_PITCH, pitch * Kondion.kjs.timescale);
		//AL10.alSourcePlay(src);
	}
	
	public float position() {
		return AL10.alGetSourcef(src, AL11.AL_SEC_OFFSET);
	}
	
	public void play() {
		play(true);
	}
	
	public void play(boolean replace) {
		if (!sourced) {
			genSource();
		}
		AL10.alSourcef(src, AL10.AL_PITCH, pitch * Kondion.kjs.timescale);
		AL10.alSourcef(src, AL10.AL_GAIN, volume);
		if (replace)
			AL10.alSourcePlay(src);
		else if (AL10.alGetSourcei(src, AL10.AL_SOURCE_STATE) != AL10.AL_PLAYING) {
			AL10.alSourcePlay(src);
		}
	}
	
	public void stop() {
		AL10.alSourceStop(src);
	}
	
	public void change(KAudio f) {
		if (f != sound) {
			sound = f;
			AL10.alSourcei(src, AL10.AL_BUFFER, sound.getId());
		}
	}
	
	@Override
	public void delete() {
		if (AL10.alGetSourcei(src, AL10.AL_LOOPING) == AL10.AL_TRUE)
			delSource();
		else {
			System.out.println("waiting...");
			long left = (long) (1000 * (
					(AL10.alGetBufferi(sound.getId(), AL10.AL_SIZE)
					/ AL10.alGetBufferi(sound.getId(), AL10.AL_CHANNELS)
					/ (AL10.alGetBufferi(sound.getId(), AL10.AL_BITS) / 8))
					/ AL10.alGetBufferi(sound.getId(), AL10.AL_FREQUENCY)
					/ pitch
					- AL10.alGetSourcef(src, AL11.AL_SEC_OFFSET) / pitch));
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						//System.out.println(
						//		"Sound Size: " + 
						//		((AL10.alGetBufferi(sound.getId(), AL10.AL_SIZE)
						//		/ AL10.alGetBufferi(sound.getId(), AL10.AL_CHANNELS)
						//		/ (AL10.alGetBufferi(sound.getId(), AL10.AL_BITS) / 8))
						//		/ AL10.alGetBufferi(sound.getId(), AL10.AL_FREQUENCY)
						//		/ pitch));
						Thread.sleep(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Deleted source");
					AL10.alDeleteSources(src);
				}
			}).run();
		}
	}
	
	private void genSource() {
		src = AL10.alGenSources();
		AL10.alSourcei(src, AL10.AL_BUFFER, sound.getId());
		AL10.alSourcef(src, AL10.AL_PITCH, pitch);
		AL10.alSourcef(src, AL10.AL_GAIN, volume);
		AL10.alSourcef(src, AL10.AL_GAIN, 1.0f);
		AL10.alSource3f(src, AL10.AL_POSITION, 0.0f, 0.0f, 0.0f);
		AL10.alSource3f(src, AL10.AL_VELOCITY, 0.0f, 0.0f, 0.0f);
		sourced = true;
		
		System.out.println("gensource: " + AL10.alGetError());
	}
	
	private void delSource() {
		AL10.alDeleteSources(src);
		
	}
}
