package com.mmmpipi.cfms.util;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ModFolderFind {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Path modFolder;

    private final Path configPath;

    private List<String> nameList;

    public ModFolderFind(Path modFolder,Path configPath){
        this.modFolder = modFolder;
        this.configPath = configPath;
        getConfig();
    }

    private void getConfig(){
        try {
            File file = configPath.toFile();
            if (!file.exists()){
                FileConfig fileConfig = FileConfig.of(configPath);
                fileConfig.load();
                fileConfig.set("black_list",List.of("disable",".connector"));
                fileConfig.save();
                fileConfig.close();
            }
            FileConfig config = FileConfig.of(configPath);
            config.load();
            this.nameList = config.get("black_list");
        } catch (Exception e) {
            LOGGER.error("cant load CMFS config:",e);
        }
        if (nameList == null){
            nameList = List.of();
        }
    }

    public ArrayList<Path> getAllChild() {
        ArrayList<Path> result = new ArrayList<>();
        this.scanFolders().forEach(folder -> {
            try (Stream<Path> stream = Files.list(folder.toPath())) {
                result.addAll(stream.toList());
            } catch (IOException e) {
                LOGGER.error("",e);
            }
        });
        return result;
    }

    public ArrayList<File> scanFolders() {
        ArrayList<File> result = new ArrayList<>();
        this.scanFolder(this.modFolder, result);
        return result;
    }

    public Path scanFolder(Path folder, ArrayList<File> fileList) {
        try (var files = Files.list(folder)) {
            files.map(Path::toFile)
                    .filter(f -> f.isDirectory() && !nameList.contains(f.getName()))
                    .map(file -> this.scanFolder(file.toPath(), fileList))
                    .forEach(file -> fileList.add(file.toFile()));
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
        return folder;
    }
}
