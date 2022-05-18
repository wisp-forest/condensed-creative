# CondensedCreativeItems

[![curseforge](https://img.shields.io/badge/-CurseForge-gray?style=for-the-badge&logo=curseforge&labelColor=orange)](https://www.curseforge.com/minecraft/mc-mods/jello)
[![modrinth](https://img.shields.io/badge/-modrinth-gray?style=for-the-badge&labelColor=green&labelWidth=15&logo=appveyor&logoColor=white)]([https://modrinth.com/mod/jello](https://modrinth.com/mod/condensed-creative))
[![release](https://img.shields.io/github/v/release/wisp-forest/CondensedCreativeItems?logo=github&style=for-the-badge)](https://github.com/wisp-forest/CondensedCreativeItems/releases)
[![discord](https://img.shields.io/discord/825828008644313089?label=wisp%20forest&logo=discord&logoColor=white&style=for-the-badge)](https://discord.gg/xrwHKktV2d)

## Overview

Condensed Creative Items is currently a small library mod that allows for Mod creators to condense items into one slot similar to the Bedrock version of the game. Upon clicking the slot, it will expand to show what is all within Condensed Entry. Such is basically a space saver for blocks or items that are very similar, with the creative tab.

**Build Setup:**

```grovvy
repositories {
    maven { url 'https://maven.wispforest.io' }
}
```

```groovy
dependencies {
    // Recommend creating a CondensedCreativeItems version within your gradle.properties 
    modImplementation "io.wispforest:condensed_creative:${project.condensed_creative_version}"
}
