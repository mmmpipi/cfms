package com.mmmpipi.cfms.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ModFolderFind {

    private final Path modFolder;

    public ModFolderFind(Path modFolder){
        this.modFolder = modFolder;
    }

    public Stream<Path> getAllChild() {
        ArrayList<Path> result = new ArrayList<>();
        this.scanFolders().forEach(folder -> {
            try {
                result.addAll(Files.list(folder.toPath()).toList());
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        });

        return result.stream();
    }

    public ArrayList<File> scanFolders() {
        ArrayList<File> result = new ArrayList<>();
        this.scanFolder(this.modFolder, result);
        return result;
    }

    public Path scanFolder(Path folder, ArrayList<File> fileList) {
        try (var files = Files.list(folder)) {
            files.map(Path::toFile)
                    .filter(f -> f.isDirectory() && !f.getName().equals("disable"))
                    //.filter(f -> f.isDirectory() && !Config.folderName.contains(f.getName()))
                    .map(file -> this.scanFolder(file.toPath(), fileList))
                    .forEach(file -> fileList.add(file.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return folder;
    }
}
