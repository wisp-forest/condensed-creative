package io.wispforest.condensed_creative.fabriclike;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.ModOrigin;
import net.fabricmc.loader.impl.ModContainerImpl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DebugModContainer implements ModContainer {

    public List<Path> additionalRootPaths = new ArrayList<>();

    private ModContainer wrappedContainer;

    public DebugModContainer(ModContainer container){
        this.wrappedContainer = container;
    }

    @Override
    public ModMetadata getMetadata() {
        return wrappedContainer.getMetadata();
    }

    @Override
    public List<Path> getRootPaths() {
        List<Path> rootPaths = new ArrayList<>();

        rootPaths.addAll(wrappedContainer.getRootPaths());
        rootPaths.addAll(additionalRootPaths);

        return rootPaths;
    }

    public void addAdditionalRootPath(Path path){
        this.additionalRootPaths.add(path);
    }

    @Override
    public ModOrigin getOrigin() {
        return wrappedContainer.getOrigin();
    }

    @Override
    public Optional<ModContainer> getContainingMod() {
        return wrappedContainer.getContainingMod();
    }

    @Override
    public Collection<ModContainer> getContainedMods() {
        return wrappedContainer.getContainedMods();
    }

    @Override
    public Path getRootPath() {
        return wrappedContainer.getRootPath();
    }

    @Override
    public Path getPath(String file) {
        return wrappedContainer.getPath(file);
    }
}
