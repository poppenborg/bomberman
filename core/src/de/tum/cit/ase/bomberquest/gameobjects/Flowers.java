package de.tum.cit.ase.bomberquest.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.map.Coordinates;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Flowers are a static object without any special properties.
 * They do not have a hitbox, so the player does not collide with them.
 * They are purely decorative and serve as a nice floor decoration.
 */
public class Flowers extends Coordinates implements Drawable {

    public Flowers(int x, int y) {
        super(x, y);
    }
    
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.FLOWERS;
    }

}
