
package me.heldplayer.plugins.nei.mystcraft;

import java.util.Random;
import java.util.logging.Logger;

import me.heldplayer.util.HeldCore.ModInfo;

/**
 * MystNEIPlugin mod Objects
 * 
 * @author heldplayer
 * 
 */
public final class Objects {

    public static final String MOD_ID = "NEI-Mystcraft-Plugin";
    public static final String MOD_NAME = "NEI Mystcraft Plugin";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String MOD_DEPENCIES = "after:*";
    public static final String MOD_CHANNEL = "MystNEI";
    public static final String CLIENT_PROXY = "me.heldplayer.plugins.nei.mystcraft.client.ClientProxy";
    public static final String SERVER_PROXY = "me.heldplayer.plugins.nei.mystcraft.CommonProxy";

    public static final ModInfo MOD_INFO = new ModInfo(MOD_ID, MOD_NAME, MOD_VERSION);

    public static Logger log;

    public static final Random rnd = new Random();

}
