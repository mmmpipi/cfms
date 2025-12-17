package com.mmmpipi.cfms;

import com.mmmpipi.cfms.util.ModFolderFind;
import com.mojang.logging.LogUtils;
import cpw.mods.modlauncher.api.LambdaExceptionUtils;
import cpw.mods.modlauncher.api.NamedPath;
import cpw.mods.modlauncher.serviceapi.ITransformerDiscoveryService;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.TransformerDiscovererConstants;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TransformerDiscoverer implements ITransformerDiscoveryService {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final static List<NamedPath> found = new ArrayList<>();

    public static List<Path> allExcluded() {
        return found.stream().map(np -> np.paths()[0]).toList();
    }

    @Override
    public List<NamedPath> candidates(Path gameDirectory) {
        LOGGER.debug("CFMS services discoverer");
//        ModFolderFind folderHelper;
//        if (ChildFolderModScanner.folderHelper==null){
//            FMLPaths.loadAbsolutePaths(gameDirectory);
//            ChildFolderModScanner.folderHelper= new ModFolderFind(
//                    FMLPaths.MODSDIR.get(),
//                    FMLPaths.CONFIGDIR.get().resolve(ChildFolderModScanner.CONFIG_NAME)
//            );
//        }
        FMLPaths.loadAbsolutePaths(gameDirectory);
        ModFolderFind folderHelper = new ModFolderFind(
                    FMLPaths.MODSDIR.get(),
                    FMLPaths.CONFIGDIR.get().resolve(ChildFolderModScanner.CONFIG_NAME)
            );
        folderHelper.getAllChild().stream()
                .filter(p -> p.getFileName().toString().endsWith(".jar"))
                .filter(TransformerDiscoverer::shouldLoadInServiceLayer)
                .forEachOrdered(p -> found.add(new NamedPath(p.getFileName().toString(), p)));
        return List.copyOf(found);
    }


    private static boolean shouldLoadInServiceLayer(Path path) {
        if (!Files.isRegularFile(path)) return false;
        if (!path.toString().endsWith(".jar")) return false;
        if (LambdaExceptionUtils.uncheck(() -> Files.size(path)) == 0) return false;

        return TransformerDiscovererConstants.shouldLoadInServiceLayer(path);
    }
}
