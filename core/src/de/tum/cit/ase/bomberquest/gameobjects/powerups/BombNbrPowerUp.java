package de.tum.cit.ase.bomberquest.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.gameobjects.Coordinates;
import de.tum.cit.ase.bomberquest.texture.Textures;

public class BombNbrPowerUp extends PowerUp {

    public BombNbrPowerUp(World world, float x, float y) {
        super(world, x, y);
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        if (!markedForRemoval) {
            return Textures.BOMBNBR;
        } else {
            return Textures.EMPTY;
        }
    }

    /**
     * Checks if there is a bombNbrPowerUp at the specified coordinates in the parameter. Is used to determine which item is collected
     *
     * @param coordinates the x- and y-coordinate to check
     * @return true if a bombNbrPowerUp exists at the given coordinates, false otherwise
     */
    public boolean isBombNbrPowerUpAt(Coordinates coordinates) {
        if (this.getX() == coordinates.getX() && this.getY() == coordinates.getY()) {
            return true;
        }
        return false;
    }
}
