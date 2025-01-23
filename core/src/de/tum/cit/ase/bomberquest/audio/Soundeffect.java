package de.tum.cit.ase.bomberquest.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import javax.sound.midi.Soundbank;

/**
 * This enum is used to manage the sound effects in the game.
 * An enum is used as it allows for easy management of the music tracks
 * and prevents the same track from being loaded into memory multiple times.
 */
public enum Soundeffect {

    WINN_SOUND("WinSound.wav"),
    DIE_SOUND("tribe_d.wav"),
    DROP_BOMB_SOUND("FX272.mp3"),
    EXPLODS_SOUND("Explosion.wav"),
    POWERUP_SOUND("FX279.mp3"),
    ENEMYDEATH_SOUN("GoblinDeath.wav");

    // General Volume
    private static float VOLUME = 1f;




    /** The sound effect file owned by this variant. */
    private final Sound sound;

    Soundeffect(String fileName) {
        this.sound = Gdx.audio.newSound(Gdx.files.internal("audio/" + fileName));
    }

    /**
     * Play this sound effect.
     * This will not stop other sound effects from playing.
     * Sets the looping to false
     * Sets the Volume to volume
     */
    public void play(float volume) {
        this.sound.play(volume);
//        this.id = this.sound.play();
//        this.sound.setLooping(id, false);
//        this.sound.setVolume(id, volume);
    }

    public void stop() {
        this.sound.stop();
    }

    public void dispose() {
        sound.dispose();
    }

    public static float getVOLUME() {
        return VOLUME;
    }
}
