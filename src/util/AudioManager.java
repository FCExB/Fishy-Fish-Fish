package util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import org.newdawn.slick.util.ResourceLoader;

public class AudioManager {

	public static final int NUM_BUFFERS = 1;
	public static final int NUM_SOURCES = 5;
	public static final int NUM_SPLASH_SOURCES = 5;
	public static final int SPLASH = 0;

	private static IntBuffer buffer = BufferUtils.createIntBuffer(NUM_BUFFERS);

	private static IntBuffer sources = BufferUtils.createIntBuffer(NUM_SOURCES);

	private static FloatBuffer sourcePos = BufferUtils
			.createFloatBuffer(NUM_SOURCES * 3);

	private static FloatBuffer sourceVel = BufferUtils
			.createFloatBuffer(NUM_SOURCES * 3);

	private static FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3);

	private static FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3);

	/**
	 * Orientation of the listener. (first 3 elements are "at", second 3 are
	 * "up") Also note that these should be units of '1'.
	 */
	private static FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6);

	public static void initAudioManager() {

		// if (!AL.isCreated()) {
		try {
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		// }
		AL10.alGetError();

		// Load the wav data.
		if (loadALData() == AL10.AL_FALSE) {
			System.out.println("Error loading data.");
			return;
		}

		setListenerValues();
	}

	private static void setListenerValues() {
		listenerPos.put(new float[] { 0.0f, 0.0f, 15f });
		listenerVel.put(new float[] { 0.0f, 0.0f, 0.0f });

		// First 3 = lookingAt, Second 3 = up
		listenerOri.put(new float[] { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f });

		listenerPos.flip();
		listenerVel.flip();
		listenerOri.flip();

		AL10.alListener(AL10.AL_POSITION, listenerPos);
		AL10.alListener(AL10.AL_VELOCITY, listenerVel);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
	}

	private static int loadALData() {
		// Load wav data into a buffers.
		AL10.alGenBuffers(buffer);

		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			return AL10.AL_FALSE;

		InputStream is = null;
		is = ResourceLoader.getResourceAsStream("data/SPLASH.wav");

		WaveData waveFile = WaveData.create(new BufferedInputStream(is));
		AL10.alBufferData(buffer.get(SPLASH), waveFile.format, waveFile.data,
				waveFile.samplerate);
		waveFile.dispose();

		// Bind buffers into audio sources.
		AL10.alGenSources(sources);

		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			return AL10.AL_FALSE;

		for (int i = 0; i < NUM_SPLASH_SOURCES; i++) {

			sourcePos.put(i * 3 + 0, 0f);
			sourcePos.put(i * 3 + 1, 0f);
			sourcePos.put(i * 3 + 2, 0f);

			sourceVel.put(i * 3 + 0, 0f);
			sourceVel.put(i * 3 + 1, 0f);
			sourceVel.put(i * 3 + 2, 0f);

			AL10.alSourcei(sources.get(i), AL10.AL_BUFFER, buffer.get(SPLASH));
			AL10.alSourcef(sources.get(i), AL10.AL_PITCH, 1.0f);
			AL10.alSourcef(sources.get(i), AL10.AL_GAIN, 1.0f);
			AL10.alSource(sources.get(i), AL10.AL_POSITION,
					(FloatBuffer) sourcePos.position(i * 3));
			AL10.alSource(sources.get(i), AL10.AL_VELOCITY,
					(FloatBuffer) sourceVel.position(i * 3));
			AL10.alSourcei(sources.get(i), AL10.AL_LOOPING, AL10.AL_FALSE);

		}

		// Do another error check and return.
		if (AL10.alGetError() == AL10.AL_NO_ERROR)
			return AL10.AL_TRUE;

		return AL10.AL_FALSE;

	}

	private final static double SOUND_VARIATION = 0.2;

	public static void playSound(double x, double y, double z, int sound) {

		for (int i = 0; i < NUM_SPLASH_SOURCES; i++) {
			int state = AL10.alGetSourcei(sources.get(i), AL10.AL_SOURCE_STATE);

			if (state != AL10.AL_PLAYING) {
				sourcePos.put(i * 3 + 0, (float) (x * SOUND_VARIATION));
				sourcePos.put(i * 3 + 1, (float) (y * SOUND_VARIATION));
				sourcePos.put(i * 3 + 1, (float) (z * SOUND_VARIATION));

				AL10.alSource(sources.get(i), AL10.AL_POSITION,
						(FloatBuffer) sourcePos.position(i * 3));

				AL10.alSourcePlay(sources.get(sound));

				return;
			}
		}
	}

	public static void updateListenerPosition(double x, double y, double z) {
		listenerPos.put(0, (float) (x * SOUND_VARIATION));
		listenerPos.put(1, (float) (y * SOUND_VARIATION));
		listenerPos.put(2, (float) (z * SOUND_VARIATION));

		AL10.alListener(AL10.AL_POSITION, listenerPos);
	}
}
