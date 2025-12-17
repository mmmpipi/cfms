package com.mmmpipi.cfms;

import com.mmmpipi.cfms.util.ModFolderFind;
import net.neoforged.fml.common.Mod;
@Mod(ChildFolderModScanner.MODID)
public class ChildFolderModScanner
{
    public static final String MODID = "cfms";
    public static ModFolderFind folderHelper;
    public static final String CONFIG_NAME="CFMS.toml";
}
