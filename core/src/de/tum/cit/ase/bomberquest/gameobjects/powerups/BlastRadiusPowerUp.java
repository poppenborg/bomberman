package de.tum.cit.ase.bomberquest.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.gameobjects.Coordinates;
import de.tum.cit.ase.bomberquest.texture.Textures;

public class BlastRadiusPowerUp extends PowerUp {

    public BlastRadiusPowerUp(World world, float x, float y) {
        super(world, x, y);

    }

    @Override
    public TextureRegion getCurrentAppearance() {
        if (!markedForRemoval) {
            return Textures.BLASTRADIUS;
        } else {
            return Textures.EMPTY;
        }
    }

    /**
     * Checks if there is a blastRadiusPowerUp at the specified coordinates in the parameter. Is used to determine which item is collected
     *
     * @param coordinates the x- and y-coordinate to check
     * @return true if a blastRadiusPowerUp exists at the given coordinates, false otherwise
     */
    public boolean isBlastRadiusPowerUpAt(Coordinates coordinates) {
        if (this.getX() == coordinates.getX() && this.getY() == coordinates.getY()) {
            return true;
        }
        return false;
    }
}
