
package com.xcompwiz.mystcraft.api.linking;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Functions for interfacing with the linking mechanics
 * The implementation of this is provided by MystAPI
 * Do NOT implement this yourself!
 * 
 * @author xcompwiz
 * 
 */
public interface ILinkingAPI {

    /**
     * Links (Teleports) the entity using the information provided by the link
     * info object
     * A valid link info object can be created using the other functions from
     * this interface
     */
    void linkEntity(Entity entity, ILinkInfo linkInfo);

    /**
     * Returns a tag compound which contains all of the information to link an
     * entity back to the present location
     * Defaults to Non-Intra-Age, so the link only works when changing
     * dimensions
     * Note that the Entity's yaw is maintained, but not its pitch
     * 
     * @return The Link's descriptor. You can modify the link's properties
     *         through this.
     */
    ILinkInfo getLinkInfoFromPosition(World world, Entity location);

    /**
     * Creates a new LinkInfo object from the data in a NBTTagCompound
     * This will not modify the compound and modifications made to the LinkInfo
     * do not reflect in the compound
     * If null is passed in it returns a default link description (overworld
     * spawn)
     * 
     * @return The Link's descriptor. You can modify the link's properties
     *         through this.
     */
    public ILinkInfo createLinkInfo(NBTTagCompound linkInfo);

    /**
     * Returns the unique identifier for a dimension for use with the LinkInfo
     * link descriptor object
     * Setting the dimension value of a LinkInfo object to this value will set
     * it to target the passed dimension
     */
    int getDimensionUID(World worldObj);

    /**
     * Use this to register your own rendering effects on the linkpanel
     * 
     * @param The
     *        renderer to register
     */
    @SideOnly(Side.CLIENT)
    void registerRenderEffect(LinkPanelEffect renderer);
}
