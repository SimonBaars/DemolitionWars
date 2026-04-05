#!/usr/bin/env python3
"""Generate minimal placeholder PNGs for LibGDX asset loading (CI / dev without art repo)."""
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "assets" / "sprites"


def solid(w, h, rgb, label=None):
    img = Image.new("RGBA", (w, h), (*rgb, 255))
    if label:
        draw = ImageDraw.Draw(img)
        try:
            font = ImageFont.load_default()
        except Exception:
            font = None
        draw.text((4, 4), label[:12], fill=(255, 255, 255, 255), font=font)
    return img


def sheet_human(label, base_rgb):
    """512x128 sheet: 8 frames x 64x128 (split uses 64,128 in code)."""
    w, fh = 512, 128
    img = Image.new("RGBA", (w, fh), (30, 30, 40, 255))
    draw = ImageDraw.Draw(img)
    for i in range(8):
        x0 = i * 64
        shade = 40 + i * 8
        c = (
            min(255, base_rgb[0] + shade // 4),
            min(255, base_rgb[1] + shade // 5),
            min(255, base_rgb[2] + shade // 6),
        )
        draw.rectangle([x0 + 4, 8, x0 + 60, fh - 8], fill=(*c, 255))
        draw.text((x0 + 8, 12), f"{label[0]}{i}", fill=(255, 255, 255, 255))
    return img


def main():
    OUT.mkdir(parents=True, exist_ok=True)

    human_sheets = [
        ("speler_zwart", (80, 80, 90)),
        ("merchant_zwart", (120, 90, 60)),
        ("merchant_blauw", (60, 90, 140)),
        ("guard_zwart", (90, 70, 70)),
        ("guard_blauw", (70, 90, 140)),
        ("king_zwart", (140, 120, 60)),
        ("king_blauw", (60, 100, 200)),
    ]
    for name, rgb in human_sheets:
        sheet_human(name, rgb).save(OUT / f"{name}.png")

    tiles = [
        ("brick", (160, 70, 60)),
        ("dirt", (120, 80, 50)),
        ("grass", (70, 140, 60)),
        ("planks", (180, 130, 80)),
        ("steel", (140, 150, 160)),
        ("stone", (110, 110, 115)),
        ("unbreakable_rock", (40, 40, 45)),
        ("wooden_door", (120, 80, 50)),
        ("steel_door", (130, 135, 140)),
        ("woodenpole", (160, 120, 80)),
        ("wool_red", (200, 60, 60)),
        ("wool_white", (230, 230, 230)),
        ("wool_black", (45, 45, 50)),
        ("wool_blue", (70, 90, 200)),
        ("ladder1", (180, 140, 80)),
        ("ladder2", (170, 130, 70)),
    ]
    for name, rgb in tiles:
        solid(64, 64, rgb, name).save(OUT / f"{name}.png")

    bombs = [
        ("tnt", (200, 50, 50)),
        ("firebomb", (255, 120, 40)),
        ("grenade", (80, 120, 60)),
        ("miner", (100, 100, 80)),
        ("napalm", (255, 200, 40)),
        ("nuke", (200, 255, 100)),
        ("scatterbomb", (200, 100, 200)),
        ("supernova", (255, 220, 255)),
    ]
    for name, rgb in bombs:
        solid(32, 32, rgb, name).save(OUT / f"{name}.png")

    weapons = [
        ("bullet", (255, 240, 120)),
        ("bullet2", (255, 200, 80)),
        ("blade", (200, 200, 220)),
        ("blade2", (180, 180, 200)),
    ]
    for name, rgb in weapons:
        solid(16, 8, rgb, name).save(OUT / f"{name}.png")

    print("Wrote sprites to", OUT)


if __name__ == "__main__":
    main()
