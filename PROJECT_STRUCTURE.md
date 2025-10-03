# DemolitionWars Project Structure

This document provides a visual overview of the project organization.

## Repository Structure

```
DemolitionWars/
│
├── 📱 app/                                    # Original Game (Modernized)
│   ├── src/main/
│   │   ├── java/
│   │   │   ├── android/gameengine/           # Custom game engine
│   │   │   │   ├── engine/                   # Core engine (GameEngine, GameView, etc.)
│   │   │   │   ├── input/                    # Input handling
│   │   │   │   ├── objects/                  # Game objects framework
│   │   │   │   └── dashboard/                # UI components
│   │   │   │
│   │   │   └── com/diygames/                 # Game implementation
│   │   │       ├── demolitionwars/           # Main game logic
│   │   │       │   ├── DemolitionWars.java   # Main game class
│   │   │       │   ├── World.java            # Game world
│   │   │       │   ├── MovingObject.java     # Physics base class
│   │   │       │   ├── SpriteLoader.java     # Asset loading
│   │   │       │   └── GameMenu.java         # Menu system
│   │   │       │
│   │   │       ├── Blocks/                   # Terrain blocks
│   │   │       ├── Humans/                   # Characters
│   │   │       │   ├── Human.java            # Base character
│   │   │       │   ├── Player.java           # Player character
│   │   │       │   ├── Bot.java              # AI base
│   │   │       │   ├── Merchant.java         # Shop keeper
│   │   │       │   ├── Guard.java            # Enemy guard
│   │   │       │   └── King.java             # Victory target
│   │   │       │
│   │   │       └── Items/                    # Weapons/items
│   │   │
│   │   ├── res/                              # Resources
│   │   │   ├── drawable-mdpi/                # Sprites (reused in libGDX)
│   │   │   ├── layout/                       # UI layouts
│   │   │   └── values/                       # Strings, etc.
│   │   │
│   │   └── AndroidManifest.xml               # Android config
│   │
│   └── build.gradle                          # Build configuration
│
├── 🎮 libgdx-version/                        # New libGDX Port
│   │
│   ├── core/                                 # Platform-independent code
│   │   ├── src/main/java/com/demolitionwars/
│   │   │   ├── DemolitionWarsGame.java       # Main game class
│   │   │   │
│   │   │   ├── screens/                      # Game screens
│   │   │   │   └── GameScreen.java           # Main gameplay
│   │   │   │
│   │   │   └── entities/                     # Game entities
│   │   │       ├── GameEntity.java           # Base entity
│   │   │       ├── Player.java               # Player with Box2D
│   │   │       ├── Block.java                # Destructible terrain
│   │   │       └── Explosive.java            # Weapons/bombs
│   │   │
│   │   └── build.gradle                      # Core module config
│   │
│   ├── android/                              # Android platform
│   │   ├── src/main/
│   │   │   ├── java/com/demolitionwars/android/
│   │   │   │   └── AndroidLauncher.java      # Android entry point
│   │   │   │
│   │   │   ├── res/                          # Android resources
│   │   │   │   ├── drawable-mdpi/            # Icon
│   │   │   │   └── values/                   # Strings, styles
│   │   │   │
│   │   │   └── AndroidManifest.xml           # Android config
│   │   │
│   │   └── build.gradle                      # Android module config
│   │
│   ├── assets/                               # Game assets
│   │   ├── sprites/                          # (Copy from ../app/res/drawable-mdpi/)
│   │   ├── sounds/                           # Sound effects
│   │   └── README.md                         # Asset documentation
│   │
│   ├── gradle/                               # Gradle wrapper
│   ├── build.gradle                          # Root build config
│   ├── settings.gradle                       # Module configuration
│   ├── gradle.properties                     # Build properties
│   │
│   ├── copy-assets.sh                        # Utility script
│   ├── README.md                             # libGDX version docs
│   └── PORTING_GUIDE.md                      # Detailed porting guide
│
├── 🔧 .github/                               # GitHub Configuration
│   └── workflows/
│       └── android-build.yml                 # CI/CD pipeline
│
├── 📚 Documentation                          # Project Documentation
│   ├── README.md                             # Main project README
│   ├── SUMMARY.md                            # Complete overview
│   ├── IMPLEMENTATION_STATUS.md              # Current status
│   ├── QUICKSTART.md                         # Getting started guide
│   └── PROJECT_STRUCTURE.md                  # This file
│
├── ⚙️ Build Configuration                    # Root Build Config
│   ├── build.gradle                          # Root build file
│   ├── settings.gradle                       # Project settings
│   ├── gradle.properties                     # Global properties
│   └── gradle/                               # Gradle wrapper
│       └── wrapper/
│           └── gradle-wrapper.properties     # Gradle 8.0
│
└── 📄 Other Files
    ├── .gitignore                            # Git ignore rules
    ├── gradlew                               # Gradle wrapper script (Unix)
    ├── gradlew.bat                           # Gradle wrapper script (Windows)
    └── import-summary.txt                    # Legacy import info

```

## Key Directories Explained

### 📱 Original Game (`/app`)
- **Custom game engine** implementation
- Working game targeting Android 12+
- All original functionality preserved
- Modernized build configuration

### 🎮 libGDX Version (`/libgdx-version`)
- **New implementation** using libGDX framework
- Box2D physics integration
- Cross-platform support
- Modern game architecture
- ~15% complete (framework ready)

### 🔧 CI/CD (`/.github/workflows`)
- Automated builds for both versions
- Runs on every push/PR
- Generates Debug and Release APKs
- Artifact uploads

### 📚 Documentation
- **README.md**: Project overview
- **SUMMARY.md**: Complete implementation summary
- **IMPLEMENTATION_STATUS.md**: Detailed status tracking
- **QUICKSTART.md**: Developer getting started guide
- **PROJECT_STRUCTURE.md**: This file

## Module Dependencies

### Original Version
```
app
 └─ Dependencies
     ├─ AndroidX AppCompat 1.6.1
     └─ Custom GameEngine (internal)
```

### libGDX Version
```
android
 └─ Depends on: core
     └─ Dependencies
         ├─ libGDX 1.12.1
         └─ Box2D (via libGDX)

core
 └─ Dependencies
     ├─ libGDX Core 1.12.1
     └─ Box2D 1.12.1
```

## File Count by Type

| Type | Original | libGDX | Total |
|------|----------|--------|-------|
| Java Files | ~50 | ~8 | ~58 |
| Gradle Files | 3 | 5 | 8 |
| XML Files | ~10 | 3 | ~13 |
| Documentation | - | - | 7 |
| Sprites (PNG) | ~50 | 1 | ~51 |

## Build Outputs

### Original Version
```
app/build/outputs/apk/
├── debug/
│   └── app-debug.apk
└── release/
    └── app-release-unsigned.apk
```

### libGDX Version
```
libgdx-version/android/build/outputs/apk/
├── debug/
│   └── android-debug.apk
└── release/
    └── android-release-unsigned.apk
```

## Development Workflow

```
┌─────────────────┐
│  Make Changes   │
│  to Source      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐      ┌──────────────┐
│  Local Build    │─────▶│ Test on      │
│  ./gradlew      │      │ Emulator/    │
│  assembleDebug  │      │ Device       │
└────────┬────────┘      └──────────────┘
         │
         ▼
┌─────────────────┐
│  Commit & Push  │
│  to GitHub      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐      ┌──────────────┐
│  GitHub Actions │─────▶│ APK          │
│  Build Pipeline │      │ Artifacts    │
└─────────────────┘      └──────────────┘
```

## Technology Stack

### Original Version
- **Language**: Java 17
- **Framework**: Custom Game Engine
- **Build**: Gradle 8.0
- **Min Android**: API 31 (Android 12)
- **Target Android**: API 33

### libGDX Version
- **Language**: Java 17
- **Framework**: libGDX 1.12.1
- **Physics**: Box2D
- **Build**: Gradle 8.0
- **Min Android**: API 31 (Android 12)
- **Target Android**: API 33
- **Future Platforms**: Desktop, iOS, Web (HTML5)

## Assets Organization

### Sprites (Both Versions)
```
Sprites are stored in:
- Original: app/src/main/res/drawable-mdpi/
- libGDX: libgdx-version/assets/sprites/ (to be copied)

Categories:
- Characters: Player, Merchant, Guard, King (animated, 8 frames)
- Blocks: Brick, Dirt, Grass, Planks, Steel, Stone, Wool variants
- Weapons: TNT, Firebomb, Grenade, Miner, Napalm, Nuke, Scatterbomb, Supernova
- Projectiles: Bullet, Blade variants
- UI: Buttons (A, B, X, Y), D-Pad
- Environment: Clouds, Ladder
```

## Next Development Areas

1. **libGDX - Phase 1**: Sprite loading and rendering
2. **libGDX - Phase 2**: Player movement and input
3. **libGDX - Phase 3**: Map loading and terrain
4. **libGDX - Phase 4**: Weapons and combat
5. **libGDX - Phase 5**: AI system
6. **libGDX - Phase 6**: UI/HUD
7. **libGDX - Phase 7**: Audio and polish

See `IMPLEMENTATION_STATUS.md` for detailed breakdown.
