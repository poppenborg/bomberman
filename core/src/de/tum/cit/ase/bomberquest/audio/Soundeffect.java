package de.tum.cit.ase.bomberquest.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import javax.sound.midi.Soundbank;

/**
 * This enum is used to manage the sound effects in the game.
 * An enum is used as it allows for easy management of the music tracks
 * and prevents the same track from being loaded into memory multiple times.
 */
public enum Soundeffect {

    /** Various sound effects for various occasions */
    WINN_SOUND("winSound.wav"),
    DIE_SOUND("dieSound.wav"),
    DROP_BOMB_SOUND("bombSound.mp3"),
    EXPLODS_SOUND("explosionSound.wav"),
    POWERUP_SOUND("powerupSound.ogg"),
    ENEMYDEATH_SOUN("deathSound.wav"),
    BUTTON_SOUN("clickSound.wav");

    /** general Volume for the sound effects */
    private static float VOLUME = 1f;

    /** The sound effect file owned by this variant. */
    private final Sound sound;

    /**
     * Constructor for the sound effects
     *
     * @param fileName Name of the sound file to be used
     */
    Soundeffect(String fileName) {
        this.sound = Gdx.audio.newSound(Gdx.files.internal("audio/" + fileName));
    }

    /**
     * Play this sound effect.
     * This will not stop other sound effects from playing.
     * The Volume can be chosen idividually
     */
    public void play(float volume) {
        this.sound.play(volume);
    }

    // Setters and Getters
    public static float getVOLUME() {
        return VOLUME;
    }
}
