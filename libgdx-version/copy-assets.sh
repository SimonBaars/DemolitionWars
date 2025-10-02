#!/bin/bash

# Script to copy assets from original game to libGDX version

echo "Copying sprite assets from original game..."

SOURCE_DIR="../app/src/main/res/drawable-mdpi"
DEST_DIR="assets/sprites"

# Create destination directory if it doesn't exist
mkdir -p "$DEST_DIR"

# Copy all PNG files
cp "$SOURCE_DIR"/*.png "$DEST_DIR/" 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✓ Successfully copied sprite assets to $DEST_DIR"
    echo "  Files copied:"
    ls "$DEST_DIR"/*.png | wc -l
else
    echo "✗ Failed to copy assets. Check that source directory exists."
    exit 1
fi

echo ""
echo "Next steps:"
echo "1. Review copied sprites in $DEST_DIR"
echo "2. Consider creating a texture atlas for better performance"
echo "3. Implement sprite loading in GameScreen.java"
