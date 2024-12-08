package com.mmmpipi.cfms;

import com.mmmpipi.cfms.util.ModFolderFind;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.ModLoadingIssue;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.ModDirTransformerDiscoverer;
import net.neoforged.fml.loading.StringUtils;
import net.neoforged.fml.loading.moddiscovery.NightConfigWrapper;
import net.neoforged.neoforgespi.ILaunchContext;
import net.neoforged.neoforgespi.locating.IDiscoveryPipeline;
import net.neoforged.neoforgespi.locating.IModFileCandidateLocator;
import net.neoforged.neoforgespi.locating.IncompatibleFileReporting;
import net.neoforged.neoforgespi.locating.ModFileDiscoveryAttributes;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;


public class Locator implements IModFileCandidateLocator {

    private static final Logger LOGGER = LogUtils.getLogger();
    private final Path modFolder = FMLPaths.MODSDIR.get();
    private final Path configPath = FMLPaths.CONFIGDIR.get().resolve("CFMS.toml");


    public Locator(){
    }

    @Override
    public void findCandidates(ILaunchContext context, IDiscoveryPipeline pipeline) {
        LOGGER.info("CHMS start!");

        List<Path> excluded = ModDirTransformerDiscoverer.allExcluded();

        ModFolderFind folderHelper = new ModFolderFind(this.modFolder,this.configPath);

        List<Path> stream = folderHelper.getAllChild()
                .filter(p -> !excluded.contains(p))
                .sorted(Comparator.comparing(path -> StringUtils.toLowerCase(path.getFileName().toString())))
                .filter(p -> StringUtils.toLowerCase(p.getFileName().toString()).endsWith(".jar"))
                .toList();
        for (var path : stream){
            if (!Files.isRegularFile(path)) {
                pipeline.addIssue(ModLoadingIssue.warning("fml.modloadingissue.brokenfile.unknown").withAffectedPath(path));
                continue;
            }
            pipeline.addPath(path, ModFileDiscoveryAttributes.DEFAULT, IncompatibleFileReporting.WARN_ALWAYS);
        }
    }
}
