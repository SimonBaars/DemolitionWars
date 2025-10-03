# DemolitionWars Quick Start Guide

Quick guide to get started developing on DemolitionWars.

## Prerequisites

- **JDK 17+**
- **Android SDK** with API 33 installed
- **Android Studio** (recommended) or IntelliJ IDEA
- **Git**

## Setup

### 1. Clone Repository

```bash
git clone https://github.com/SimonBaars/DemolitionWars.git
cd DemolitionWars
```

### 2. Configure Android SDK

Create `local.properties` in project root:

```properties
sdk.dir=/path/to/your/android-sdk
```

## Building

### Original Version

```bash
# Debug build
./gradlew assembleDebug

# Release build  
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug
```

APK location: `app/build/outputs/apk/`

### libGDX Version

```bash
cd libgdx-version

# Copy sprite assets (first time only)
./copy-assets.sh

# Build Android APK
./gradlew android:assembleDebug

# Install on device
./gradlew android:installDebug
```

APK location: `libgdx-version/android/build/outputs/apk/`

## Development Workflow

### Working on Original Version

1. Open project in Android Studio
2. Select `app` module
3. Edit files in `app/src/main/java/`
4. Run on emulator or device: Select "app" configuration and click Run

### Working on libGDX Version

1. Open `libgdx-version` directory in Android Studio as separate project
2. Core game logic: `libgdx-version/core/src/main/java/`
3. Android-specific: `libgdx-version/android/src/main/java/`
4. Run on emulator or device: Select "android" configuration and click Run

**Tip**: For faster iteration on libGDX, create a desktop module to test without deploying to Android.

## Project Structure

```
DemolitionWars/
├── app/                    # Original game (modernized)
│   ├── src/main/java/
│   │   ├── android/gameengine/  # Custom game engine
│   │   └── com/diygames/        # Game logic
│   └── build.gradle
│
├── libgdx-version/         # New libGDX port
│   ├── core/               # Platform-independent code
│   │   └── src/main/java/com/demolitionwars/
│   ├── android/            # Android launcher
│   ├── assets/             # Game assets (sprites, sounds)
│   └── build.gradle
│
├── .github/workflows/      # CI/CD pipelines
└── README.md
```

## Common Tasks

### Add New Sprite (libGDX)

1. Place PNG in `libgdx-version/assets/sprites/`
2. Load in code:
   ```java
   Texture texture = new Texture("sprites/mysprite.png");
   Sprite sprite = new Sprite(texture);
   ```

### Add New Entity (libGDX)

1. Create class extending `GameEntity`
2. Implement `update()` and `render()` methods
3. Create Box2D body in constructor
4. Add to GameScreen's entity list

### Modify Original Game

1. Locate relevant class in `app/src/main/java/com/diygames/`
2. Make changes
3. Test on device
4. Update documentation if API changes

## Testing

### Running on Emulator

1. Create AVD with API 33+
2. Start emulator
3. Run: `./gradlew installDebug`

### Running on Physical Device

1. Enable USB debugging on device
2. Connect via USB
3. Run: `./gradlew installDebug`

### Debugging

In Android Studio:
1. Set breakpoints in code
2. Run in Debug mode (Debug icon or Shift+F9)
3. Use Logcat to view logs

## Troubleshooting

### Gradle Sync Failed

**Problem**: "Failed to download dependencies"

**Solution**: Check internet connection and ensure you're not behind a restrictive firewall.

### SDK Not Found

**Problem**: "SDK location not found"

**Solution**: Create `local.properties` with correct `sdk.dir` path.

### Build Version Mismatch

**Problem**: "The project is configured to use an incompatible version"

**Solution**: Update Android Studio or change Gradle version in `gradle/wrapper/gradle-wrapper.properties`.

### libGDX Build Fails

**Problem**: "Could not resolve com.badlogicgames.gdx:gdx"

**Solution**: Check internet connection. libGDX dependencies are downloaded from Maven Central.

### App Crashes on Launch

**Problem**: App crashes immediately after install

**Solution**: 
1. Check Logcat for stack trace
2. Verify target device is API 31+
3. Check AndroidManifest for missing permissions

## Learning Resources

### Original Version
- Custom game engine code: `app/src/main/java/android/gameengine/`
- Game logic examples: `app/src/main/java/com/diygames/`

### libGDX Version
- **Official Wiki**: https://libgdx.com/wiki/
- **Box2D Tutorial**: https://www.iforce2d.net/b2dtut/
- **Porting Guide**: `libgdx-version/PORTING_GUIDE.md`
- **libGDX Examples**: https://github.com/libgdx/libgdx/wiki/External-tutorials

## Contributing

1. Create feature branch: `git checkout -b feature/my-feature`
2. Make changes
3. Test thoroughly
4. Commit: `git commit -am "Add my feature"`
5. Push: `git push origin feature/my-feature`
6. Create Pull Request

## Getting Help

- **Issues**: Check existing issues on GitHub
- **Documentation**: See `IMPLEMENTATION_STATUS.md` for current status
- **Porting Guide**: See `libgdx-version/PORTING_GUIDE.md` for detailed instructions

## Quick Commands Cheat Sheet

```bash
# Original version
./gradlew clean                    # Clean build
./gradlew assembleDebug           # Build debug APK
./gradlew installDebug            # Install on device
./gradlew uninstallAll            # Uninstall from device

# libGDX version
cd libgdx-version
./gradlew android:clean
./gradlew android:assembleDebug
./gradlew android:installDebug
./gradlew android:uninstallAll

# Git
git status                         # Check status
git diff                          # See changes
git log --oneline                 # View history
git branch                        # List branches
```

## Next Steps

1. **Try Building**: Start with `./gradlew assembleDebug`
2. **Run Original**: Install and play the original version to understand gameplay
3. **Explore Code**: Browse `app/src/main/java/` to understand implementation
4. **Read Porting Guide**: If working on libGDX version, read `libgdx-version/PORTING_GUIDE.md`
5. **Pick a Task**: See `IMPLEMENTATION_STATUS.md` for TODO items

Happy coding! 🚀
