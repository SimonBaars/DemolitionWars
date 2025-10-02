# Assets Directory

This directory contains game assets for the libGDX version.

## Sprites

To reuse sprites from the original game:

```bash
cp ../app/src/main/res/drawable-mdpi/*.png sprites/
```

Original game sprites include:
- speler_zwart (black player)
- brick, dirt, grass, planks, steel, stone (block types)
- steel_door, wooden_door (doors)
- woodenpole, ladder (climbable objects)
- wool_red, wool_white, wool_black, wool_blue (colored wool)
- merchant sprites
- guard sprites  
- king sprites
- weapons: tnt, firebomb, grenade, miner, napalm, nuke, scatterbomb, supernova
- projectiles: bullet, bullet2, blade, blade2

## Atlas Creation

For better performance, combine sprites into a texture atlas using libGDX's TexturePacker:

```java
TexturePacker.process("sprites/", "atlas/", "game");
```

## Sounds

Add sound effects for:
- Explosions
- Block destruction
- Player movement
- Menu interactions
- Victory/defeat
