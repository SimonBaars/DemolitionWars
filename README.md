# DemolitionWars

Strategical conquest game originally targeting Android 2 Froyo, updated for Android 12+.

The idea of the game is to strategically use monetary resources to, ideally, kill the opposing king.

## Game Concept

How it works:
- You start with 1000 moolah
- The king is protected behind a bunch of walls and guards
- You can buy weapons and bombs
- The game has a physics system for realistic destruction
- Destroy blocks and enemies to reach and defeat the king

## Project Versions

This repository contains TWO versions of the game:

### 1. Original Version (Modernized)
**Location**: `/app/`

The original custom game engine implementation, updated to:
- Target Android 12+ (API 31+)
- Use modern Gradle build system (8.0)
- AndroidX libraries
- GitHub Actions CI/CD for automated APK builds

**Build**: `./gradlew assembleDebug`

### 2. libGDX Version (Playable!)
**Location**: `/libgdx-version/`

A complete port to the libGDX game engine featuring:
- Box2D physics engine for realistic collisions
- Better performance and optimization
- Cross-platform support (Android, Desktop, Web)
- Complete entity system with AI
- Proper sprite batching and rendering
- Fully playable with win/lose conditions

**Status**: ~85% complete and fully playable! See [PLAYTHROUGH_GUIDE.md](libgdx-version/PLAYTHROUGH_GUIDE.md) for how to play.

**Build**: `cd libgdx-version && ./gradlew android:assembleDebug`

## Building

### Prerequisites
- JDK 11 or higher
- Android SDK with API 33
- Gradle 8.0 (included via wrapper)

### Build Commands

```bash
# Original version
./gradlew assembleDebug
./gradlew assembleRelease

# libGDX version
cd libgdx-version
./gradlew android:assembleDebug
./gradlew android:assembleRelease
```

## GitHub Actions

Automated builds are configured to:
- Build both versions on every push/PR
- Generate Debug and Release APKs
- Upload artifacts for download

## Development

### Original Version
Maintained for compatibility. Core game logic in:
- `app/src/main/java/com/diygames/demolitionwars/`
- Uses custom `GameEngine` framework

### libGDX Version
Active development target. Core game logic in:
- `libgdx-version/core/src/main/java/com/demolitionwars/`
- Uses libGDX framework with Box2D physics

See [libgdx-version/PORTING_GUIDE.md](libgdx-version/PORTING_GUIDE.md) for detailed porting instructions.

## License

MIT License (see MIT-license.txt)
