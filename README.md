# Bomber Quest

This project is a Java application providing a Bomberman-style game made by Leonard Poppenborg and Florian Janner.

# Code structure:

scr/de/tum/cit/ase/bomberquest/
├── BomberQuestGame                 # Core of the Bomber Quest game, manages the screens and global resources.
├── audio/
│   ├── MusicTrack                  # Enum to manage all music tracks in the game.
│   └── Soundeffect                 # Enum to manage all sound effects in the game.
├── gameobjects/
│   ├── Coordinates                 # 2D coordinates in the game map, extended by all game objects.
│   ├── GameObject                  # Base class for all game objects, each GameObject has Coordinates, a texture for rendering, and possible also a hitbox for collision.
│   ├── bombs/
│   │   └── Bomb                    # Represents a bomb in the game, capable of exploding after a set delay, which destroys destructible walls and kills enemies and players.
│   ├── exits/
│   │   └── Exit                    # Static object in the game, the player can interact with to win the game, but since the hitbox is a sensor Enemies can pass thought it. 
│   ├── flowers/
│   │   └── Flowers                 # Static, non-interactive decorative object in the game, only for providing a visual appearance.
│   ├── mobs/
│   │   ├── Mob                     # Abstract class for movable entities in the game, with methods for hitbox handling, movement, and animation.
│   │   ├── Enemy                   # Character with a circular hitbox that moves in a circular pattern, with the ability to be killed.
│   │   └── Player                  # Controllable character with a circular hitbox, which the camara follows.
│   ├── powerups/
│   │   ├── PowerUp                 # Abstract class for power-ups in the game. It has a hitbox for collision detection, but since it´s a sensor Enemies can pass thought it.
│   │   ├── BlastRadiusPowerUp      # Power-up to increase the blast radius of an explosion by one in each direction.
│   │   ├── BombNbrPowerUp          # Power-up to increase the amount of bombs the player can simultaneously place on the map.
│   │   └── MovementSpeedPowerUp    # Power-up to increase the movement speed of the player. Different to the other two power-ups this one is automatically created under a random destructible wall.
│   ├── walls/
│   │   ├── Wall                    # Abstract class for static game objects with a square-shaped hitbox, preventing the player from passing through unless destroyed.
│   │   ├── DestructibleWall        # Wall that can be destroyed by a bomb's blast radius, with an animation for its destruction.
│   │   └── IndestructibleWall      # Wall that can´t be destroyed.
├── map/
│   ├── GameContactListener         # Modified contact listener defining to the game logic specific reactions if two bodies collide.
│   ├── GameMap                     # Represents the game's map, managing and updating all game objects, while handling physics and win/lose conditions.
│   └── MapLoader                   # Handles loading and parsing of map files and provides the possibility to load a randomly selected map.
├── screen/
│   ├── BaseScreen                  # Abstract class for common functionalities of the different screens.
│   ├── GameScreen                  # Rendering of the gameplay screen, managing the camera, HUD, game objects, resizing of the game, and updating the game state.
│   ├── Hud                         # Displays all for the user relevant information about the screen, e.g. time left, amount of power-ups.
│   ├── LooseScreen                 # Displays a game over screen with options it´s own music and buttons for navigation.
│   ├── MenuScreen                  # Displays a menu screen with options it´s own music and buttons for navigation. This is the first screen the user will see.
│   ├── PauseScreen                 # Displays a pause screen with options it´s own music and buttons for navigation. When this screen is active the game is paused.
│   └── WinScreen                   # Displays a win screen with options it´s own music and buttons for navigation.
└── texture/
    ├── Animations                  # Contains all animations used in the game.
    ├── Drawable                    # Interface for objects on the game map, that can be drawn on the screen.
    ├── SpriteSheet                 # Enum for all spritesheets used in the game, provides helper methods for grabbing texture regions.
    └── Textures                    # All texture constants used in the game

# How to run the game:

When running the game, the user is first greeted by the menu screen. Here the user can press three buttons. The first one will select a random map and start the game. With the second button the player will be forwarded to the map folder where he can choose a map himself. After choosing a map the game starts. With the third button the user can end the application.

Currently there are three different maps for the game, however more maps can be loaded as long as the files follow the guidelines of the project work. Since the exit needs the coordinates of a destructible wall to be created, the specification if at least one destructible wall is enough to load a map.

In the game the player can be controlled by the arrow keys as well as WASD, however the game is only designed to be played by one single player. Bombs are dropped via the space key. To pause the game the user can press Esc, which opens the pause screen.

In the pause screen the user can choose to resume the game, select start a new game with a newly selected map or exit the game.

Upon winning or losing the game – according to the logic of the project work – the user is presented with a win- or a lose-screen from which he can go back to the menu or exit the game.

# Game mechanics beyond the minimal requirements:

Besides a power-up that increases the number of bombs the player can drop simultaneously and another one that increases the blast radius of the explosions there is also a power-up that increases the speed of the player. Up to five instances (dependent on the amount of free destructible walls) of this power-up are placed randomly underneath existing destructible walls. After the player has collected four speed power-ups the maximum speed is reached, collecting further speed power-ups won´t increase the player’s speed.

Besides the required sound effects there is also one for pressing buttons in the different screens.