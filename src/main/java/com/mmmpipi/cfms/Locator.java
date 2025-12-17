package com.mmmpipi.cfms;

import com.mmmpipi.cfms.util.ModFolderFind;
import com.mojang.logging.LogUtils;
import cpw.mods.modlauncher.TransformationServiceDecorator;
import net.neoforged.fml.ModLoadingIssue;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.ModDirTransformerDiscoverer;
import net.neoforged.fml.loading.StringUtils;
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


    public Locator(){
    }

    @Override
    public void findCandidates(ILaunchContext context, IDiscoveryPipeline pipeline) {
        LOGGER.info("CFMS start!");

        List<Path> excluded = ModDirTransformerDiscoverer.allExcluded();
        List<Path> selfExcluded = TransformerDiscoverer.allExcluded();
        ModFolderFind folderHelper;
        if (ChildFolderModScanner.folderHelper==null){
            ChildFolderModScanner.folderHelper = new ModFolderFind(
                    FMLPaths.MODSDIR.get(),
                    FMLPaths.CONFIGDIR.get().resolve(ChildFolderModScanner.CONFIG_NAME)
            );
        }
        folderHelper = ChildFolderModScanner.folderHelper;

        List<Path> stream = folderHelper.getAllChild().stream()
                .filter(p -> !excluded.contains(p))
                .filter(p -> !selfExcluded.contains(p))
                .filter(p -> p.getFileName().toString().endsWith(".jar"))
                .sorted(Comparator.comparing(path -> StringUtils.toLowerCase(path.getFileName().toString())))
                .toList();
        for (Path path : stream){
            if (!Files.isRegularFile(path)) {
                pipeline.addIssue(ModLoadingIssue.warning("fml.modloadingissue.brokenfile.unknown").withAffectedPath(path));
                continue;
            }
            pipeline.addPath(path, ModFileDiscoveryAttributes.DEFAULT, IncompatibleFileReporting.WARN_ALWAYS);
        }
    }
}
