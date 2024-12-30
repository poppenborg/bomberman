package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Placeholders are a static object without any special properties.
 * Since they are just supposed to symbolise the missing objetcs
 * they do not have a hitbox, so the player does not collide with them and
 * they are purely decorative and serve as a nice floor decoration.
 */
public class Placeholder implements Drawable {

    private final int x;
    private final int y;

    public Placeholder(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.PLACEHOLDER;
    }
    
    @Override
    public float getX() {
        return x;
    }
    
    @Override
    public float getY() {
        return y;
    }
}
