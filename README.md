# Condensed Creative

[![curseforge](https://img.shields.io/badge/-CurseForge-gray?style=for-the-badge&logo=curseforge&labelColor=orange)](https://www.curseforge.com/minecraft/mc-mods/cci)
[![modrinth](https://img.shields.io/badge/-modrinth-gray?style=for-the-badge&labelColor=green&labelWidth=15&logo=appveyor&logoColor=white)](https://modrinth.com/mod/condensed-creative)
[![release](https://img.shields.io/github/v/release/wisp-forest/CondensedCreativeItems?logo=github&style=for-the-badge)](https://github.com/wisp-forest/CondensedCreativeItems/releases)
[![discord](https://img.shields.io/discord/825828008644313089?label=wisp%20forest&logo=discord&logoColor=white&style=for-the-badge)](https://discord.gg/xrwHKktV2d)

## Overview

Condensed Creative is a small library mod that allows for Mod or Datapack Developers to reduce Creative ItemGroup clutter by compacting similar Items into Condensed Entries that can be expanded or shrunk similar to Bedrock Addition of the Game. 

Information on how to use the API can be found at [Wisp Forest Docs](https://docs.wispforest.io/condensed-creative/setup/)

### Build Setup

Add the given maven below to your repositories section within your `build.gradle`:

```groovy title="build.gradle"
repositories {
    maven { url 'https://maven.wispforest.io' }
}

<...>

dependencies {
    //Loom (Fabric / Quilt / Forge Loom)
    modImplementation "io.wispforest:condensed_creative-fabric:${project.condensed_creative_version}"

    //Base Forge
    implementation fg.deobf("io.wispforest:condensed_creative-forge:${project.condensed_creative_version}")
}
```
```
condensed_creative_version=...
```

Note: If you are using Architectury, you can get the Common version using `modImplementation "io.wispforest:condensed_creative-common:${project.condensed_creative_version}"`

