package de.tum.cit.ase.bomberquest.gameobjects.flowers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.gameobjects.GameObject;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Flowers are a static object without any special properties.
 * They do not have a hitbox, so the player does not collide with them.
 * They are purely decorative and serve as a nice floor decoration.
 */
public class Flowers extends GameObject {

    public Flowers(int x, int y) {
        super(x, y);
    }
    
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.FLOWERS;
    }

}
