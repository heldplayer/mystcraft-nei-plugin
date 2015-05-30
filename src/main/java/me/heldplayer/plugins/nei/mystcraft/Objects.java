package me.heldplayer.plugins.nei.mystcraft;

import java.util.Random;
import net.specialattack.forge.core.ModInfo;
import org.apache.logging.log4j.Logger;

/**
 * MystNEIPlugin mod Objects
 *
 * @author heldplayer
 */
public final class Objects {

    public static final String MOD_ID = "NEI-Mystcraft-Plugin";
    public static final String MOD_NAME = "NEI Mystcraft Plugin";
    public static final ModInfo MOD_INFO = new ModInfo(MOD_ID, MOD_NAME);
    // Dependencies: https://github.com/MinecraftForge/MinecraftForge/blob/master/fml/src/main/java/net/minecraftforge/fml/common/versioning/VersionRange.java#L100
    public static final String MOD_DEPENCIES = "after:*;" +
            "required-after:spacore@[01.05.06,);" +
            "required-after:NotEnoughItems;" +
            "required-after:Mystcraft@[0.11.6,);" +
            "after:LookingGlass@(,0.0.1.01]";
    public static final String MOD_CHANNEL = "MystNEI";
    public static final String CLIENT_PROXY = "me.heldplayer.plugins.nei.mystcraft.client.ClientProxy";
    public static final String SERVER_PROXY = "me.heldplayer.plugins.nei.mystcraft.CommonProxy";
    public static final String GUI_FACTORY = "me.heldplayer.plugins.nei.mystcraft.client.gui.GuiFactory";
    public static final Random rnd = new Random();
    public static Logger log;

}
