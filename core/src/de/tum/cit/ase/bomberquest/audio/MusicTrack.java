package de.tum.cit.ase.bomberquest.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * This enum is used to manage the music tracks in the game.
 * An enum is used as it allows for easy management of the music tracks
 * and prevents the same track from being loaded into memory multiple times.
 */
public enum MusicTrack {

    GAME_BACKGROUND("gameBackground.mp3", 0.2f),
    MENU("menu.mp3", 0.2f),
    PAUSE("pause.OGG", 0.2f),
    LOOSE("LooseScreen.OGG", 0.2f),
    WIN("win.OGG", 0.2f);


    /** The music file owned by this variant. */
    private final Music music;

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
}
