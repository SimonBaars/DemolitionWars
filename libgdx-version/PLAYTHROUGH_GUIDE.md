# DemolitionWars libGDX - Playthrough Guide

## 🎮 How to Play

### Objective
Defeat the enemy King located in the fortress at x=25000 to win the game!

### Controls

**Movement:**
- `Arrow Keys` or `A/D` - Move left/right
- `Space` or `W` or `Up Arrow` - Jump
- Hold direction key to move faster

**Combat:**
- `F` - Place TNT explosive ($200)
  - TNT has a 4-second fuse
  - Explodes destroying nearby blocks
  - Damages enemies in radius

**Inventory:**
- `Q` - Previous item
- `Tab` - Next item  
- `E` - Use current item

**Utility:**
- `F1` - Toggle debug physics view
- `R` - Restart game (when game over)

### Game Mechanics

**Money System:**
- Start with $1000
- Earn money by destroying blocks
- Different blocks give different amounts:
  - Dirt: $2
  - Grass: $1
  - Brick: $5
  - Stone: $7
  - Steel: $10
  - Wool: $1

**Health:**
- Start with 100 health
- Take damage from falling or enemy attacks
- Game over if health reaches 0

**Physics:**
- Realistic Box2D physics
- Explosions apply force to objects
- Blocks can fall when support is destroyed

### World Layout

```
[Start] -------- [Merchants] -------- [Platforms] -------- [Fortress] -------- [King]
x=3000           x=4000-8000          x=5000-20000         x=24000-27000      x=26000
```

**Starting Area (x=3000):**
- Player spawn point
- Safe area with walls

**Merchant Zone (x=4000-8000):**
- Weapons Merchant at x=4000
- Explosives Merchant at x=8000
- Buy supplies here (future feature)

**Platform Challenges (x=5000-20000):**
- Scattered platforms for navigation
- Practice movement and jumping
- Good place to earn money from blocks

**Enemy Fortress (x=24000-27000):**
- Heavily fortified with steel and stone
- 5 Guards patrolling the area
- Multiple rooms and corridors
- King at the center (x=26000)

### Strategy Tips

1. **Start Slow**: Practice movement and jumping in the starting area

2. **Earn Money**: Destroy blocks in the platform section to build up funds

3. **Buy Explosives**: Use TNT to clear paths through the fortress
   - Each TNT costs $200
   - Strategic placement is key
   - Can destroy multiple blocks at once

4. **Avoid Guards**: Enemy guards patrol the fortress
   - They can damage you
   - Try to avoid direct confrontation
   - Use explosives from a distance

5. **Find the King**: Located deep in the fortress at x=26000
   - Heavily protected by walls
   - Requires multiple explosives to reach
   - Defeating him wins the game!

6. **Manage Resources**: 
   - Balance spending on explosives vs saving money
   - Each block destroyed gives money back
   - Plan your route carefully

### Win Condition
Destroy enough blocks to reach the King and eliminate him!

### Lose Condition
Player health reaches 0 (fall damage, enemy attacks, etc.)

---

## 🔧 Technical Details

**World Dimensions:**
- Width: 30,000 units
- Height: 4,000 units
- Each block: 64x64 units

**Entity Types:**
- Player (dynamic body)
- Blocks (static bodies) - 11 types
- Explosives (dynamic bodies) - 7 types
- Guards (dynamic bodies with AI)
- King (dynamic body)
- Merchants (static bodies)

**Physics:**
- Gravity: -30 m/s²
- Player jump force: 1200 N
- Player move force: 800 N
- Explosive radius: varies by type

---

## 🐛 Known Issues

- Shop interaction not fully implemented
- No sound effects yet
- No particle effects for explosions
- Camera can be jumpy on edges

---

## 💡 Future Enhancements

- [ ] Add sound effects and music
- [ ] Implement shop purchase UI
- [ ] Add particle effects for explosions
- [ ] Add more weapon types
- [ ] Implement save/load game
- [ ] Add difficulty levels
- [ ] Create more elaborate fortress layouts

---

Enjoy playing DemolitionWars libGDX version!
