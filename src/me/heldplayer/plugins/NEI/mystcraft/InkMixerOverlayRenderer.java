
package me.heldplayer.plugins.NEI.mystcraft;

import java.util.ArrayList;

import net.minecraft.inventory.Slot;

import org.lwjgl.opengl.GL11;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.forge.GuiContainerManager;

/**
 * Overlay renderer for ink mixer recipes, renders when the "?" button next to a
 * recipe is pressed
 * 
 * @author heldplayer
 * 
 */
public class InkMixerOverlayRenderer implements IRecipeOverlayRenderer {

    private IStackPositioner positioner;
    private ArrayList<PositionedStack> ingreds;
    private PositionedStack ingredient;

    public InkMixerOverlayRenderer(ArrayList<PositionedStack> stacks, IStackPositioner positioner, PositionedStack ingredient) {
        this.positioner = positioner;
        ingreds = new ArrayList<PositionedStack>();
        for (PositionedStack stack : stacks) {
            ingreds.add(stack.copy());
        }
        ingreds = this.positioner.positionStacks(ingreds);

        ArrayList<PositionedStack> temp = new ArrayList<PositionedStack>();
        temp.add(ingredient);
        temp = this.positioner.positionStacks(temp);
        ingredient = temp.get(0);
        this.ingredient = ingredient;
    }

    @Override
    public void renderOverlay(GuiContainerManager gui, Slot slot) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glColor4d(0.6, 0.6, 0.6, 0.7);

        gui.setColouredItemRender(true);

        if (slot.slotNumber == 0) {
            gui.drawItem(ingredient.relx, ingredient.rely, ingredient.item);
        }

        for (PositionedStack stack : ingreds) {
            if (stack.relx == slot.xDisplayPosition && stack.rely == slot.yDisplayPosition) {
                gui.drawItem(stack.relx, stack.rely, stack.item);
            }
        }
        gui.setColouredItemRender(false);

        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

}
