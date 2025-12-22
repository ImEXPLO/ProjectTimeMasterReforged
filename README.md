# ⏳ Project Time Master: Reforged

![License](https://img.shields.io/badge/License-MIT-green)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-green)
![Loader](https://img.shields.io/badge/NeoForge-21.1+-orange)
![Status](https://img.shields.io/badge/Status-Stable-blue)

**Master time, accelerate your machines, and control the day-night cycle in Minecraft 1.21!**

## 📖 About

**Project Time Master: Reforged** is a dedicated addon for [ProjectE](https://www.curseforge.com/minecraft/mc-mods/projecte), designed to bring back the powerful Time Watch mechanics to modern Minecraft versions.

This project is a modern rewrite and unofficial fork based on the original *Project Time* mod logic. We have completely overhauled the codebase to run natively and efficiently on **NeoForge 1.21**, fixing old bugs, optimizing performance, and ensuring full integration with ProjectE's EMC system.

## ✨ Features

* **Time Control:** Fully control the Day/Night cycle. Charge the watch to fast-forward through the night or speed up the day.
* **Tile Entity Acceleration:** Speed up your machines, furnaces, hoppers, and modded blocks! The Watch accelerates the tick rate of blocks.
* **8 Power Tiers:** Progression from the basic **MK1** to the god-tier **MK8**.
* **Dark Matter Pedestal Integration:**
    * Place any Time Watch on a ProjectE Pedestal to activate a **Time Acceleration Field**.
    * Creates an Area of Effect (AoE) that speeds up all machines in range.
    * **Range:** Scales with the Watch Tier (MK8 covers a massive area).
* **Full EMC Integration:** Balanced recipes using Klein Stars, Dark Matter, and Red Matter.
* **Configurable:** Ranges, EMC costs, and bonus ticks can be adjusted in the config file.

## 🛠️ Dependencies

To play with this mod, you need:

1.  **Minecraft** 1.21.1
2.  **NeoForge** (Latest Recommended)
3.  **ProjectE** (Required)

## 🎮 How to Use

### Handheld Mode
1.  **Craft** a Time Watch.
2.  **Shift + Right-Click (Air):** Toggle the watch mode (Charge/ON/OFF).
    * *ON:* Accelerates the block you are looking at or controls time.
    * *OFF:* Standard state.
3.  **Right-Click (Block):** Accelerate the tick rate of that specific block instantly.

### Pedestal Mode (Passive)
1.  Craft a **Dark Matter Pedestal** (from ProjectE).
2.  Place the Time Watch on the pedestal.
3.  **Right-Click** the pedestal to activate.
4.  Particles will indicate that the acceleration field is active. All machines within the radius (e.g., 9x9 for MK5) will work faster.
