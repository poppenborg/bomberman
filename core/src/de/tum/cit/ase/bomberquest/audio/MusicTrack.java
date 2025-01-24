package de.tum.cit.ase.bomberquest.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * This enum is used to manage the music tracks in the game.
 * An enum is used as it allows for easy management of the music tracks
 * and prevents the same track from being loaded into memory multiple times.
 */
public enum MusicTrack {

    /** Various music tracks for various screens */
    GAME_BACKGROUND("gameScreen.mp3", getVOLUME()),
    MENU("menuScreen.mp3", getVOLUME()),
    PAUSE("pauseScreen.OGG", getVOLUME()),
    LOOSE("looseScreen.OGG", getVOLUME()),
    WIN("winScreen.OGG", getVOLUME());

    /** General volume for the music tracks */
    private static final float VOLUME = 0.05f;

    /** The music file owned by this variant. */
    private final Music music;

    /**
     * Constructor for the music tracks
     * Sets the track to looping
     * Predefines a volume
     *
     * @param fileName Name of the music track file to be used
     */
    MusicTrack(String fileName, float volume) {
        this.music = Gdx.audio.newMusic(Gdx.files.internal("audio/" + fileName));
        this.music.setLooping(true);
        this.music.setVolume(volume);
    }

    /**
     * Play this music track.
     * This will not stop other music from playing - if you add more tracks, you will have to handle that yourself.
     */
    public void play() {
        this.music.play();
    }

    public void stop() {
        this.music.stop();
    }

    public void dispose() {
        music.dispose();
    }

    public static float getVOLUME() {
        return VOLUME;
    }
}
