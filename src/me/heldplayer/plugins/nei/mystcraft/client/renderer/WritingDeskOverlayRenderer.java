
package me.heldplayer.plugins.nei.mystcraft.client.renderer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.Slot;

import org.lwjgl.opengl.GL11;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.forge.GuiContainerManager;

public class WritingDeskOverlayRenderer implements IRecipeOverlayRenderer {

    @SuppressWarnings("unused")
    private IStackPositioner positioner;
    private ArrayList<PositionedStack> ingreds;

    public WritingDeskOverlayRenderer(List<PositionedStack> stacks, IStackPositioner positioner) {
        positioner = this.positioner = positioner;
        this.ingreds = new ArrayList<PositionedStack>();
        for (PositionedStack stack : stacks) {
            this.ingreds.add(stack.copy());
        }
        this.ingreds = positioner.positionStacks(this.ingreds);
    }

    @Override
    public void renderOverlay(GuiContainerManager gui, Slot slot) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

        GuiContainerManager.setColouredItemRender(true);

        for (PositionedStack stack : this.ingreds) {
            if (stack.relx + 233 == slot.xDisplayPosition && stack.rely + 16 == slot.yDisplayPosition) {
                GL11.glColor4d(0.6F, 0.6F, 0.6F, 0.7F);
                GuiContainerManager.drawItem(stack.relx + 233, stack.rely + 16, stack.item);
            }
        }

        GuiContainerManager.setColouredItemRender(false);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

}
