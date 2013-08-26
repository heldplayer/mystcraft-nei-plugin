
package me.heldplayer.plugins.nei.mystcraft.client;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.core.gui.GuiDraw;
import codechicken.nei.forge.IContainerInputHandler;
import codechicken.nei.forge.IContainerTooltipHandler;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;

public class MystTooltipeHandler implements IContainerInputHandler, IContainerTooltipHandler {

    @Override
    public List<String> handleTooltipFirst(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
        if (ClientProxy.guiWritingDeskClass.isAssignableFrom(gui.getClass())) {
            Point mousepos = GuiDraw.getMousePosition();
            Point relMouse = new Point(mousepos.x - gui.guiLeft, mousepos.y - gui.guiTop);

            if (currenttip.isEmpty() && new Rectangle(156 + 228, 45, 18, 34).contains(relMouse)) {
                currenttip.add("Recipes");
            }
        }
        else if (gui instanceof GuiRecipe) {
            Point mousepos = GuiDraw.getMousePosition();
            Point relMouse = new Point(mousepos.x - gui.guiLeft, mousepos.y - gui.guiTop);

            GuiRecipe guiRecipe = (GuiRecipe) gui;

            if (currenttip.isEmpty() && guiRecipe.currenthandlers.get(guiRecipe.recipetype) instanceof WritingDeskRecipeHandler && new Rectangle(151, 34, 18, 34).contains(relMouse)) {
                currenttip.add("Recipes");
            }
        }
        return currenttip;
    }

    @Override
    public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {
        return currenttip;
    }

    @Override
    public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode) {
        return false;
    }

    @Override
    public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {}

    @Override
    public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyID) {
        return false;
    }

    @Override
    public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
        if (ClientProxy.guiWritingDeskClass.isAssignableFrom(gui.getClass())) {
            Point mousepos = GuiDraw.getMousePosition();
            Point relMouse = new Point(mousepos.x - gui.guiLeft, mousepos.y - gui.guiTop);

            if (new Rectangle(156 + 228, 45, 18, 34).contains(relMouse)) {
                GuiCraftingRecipe.openRecipeGui("writingdesk");
            }
        }
        else if (gui instanceof GuiRecipe) {
            Point mousepos = GuiDraw.getMousePosition();
            Point relMouse = new Point(mousepos.x - gui.guiLeft, mousepos.y - gui.guiTop);

            GuiRecipe guiRecipe = (GuiRecipe) gui;

            if (guiRecipe.currenthandlers.get(guiRecipe.recipetype) instanceof WritingDeskRecipeHandler && new Rectangle(151, 34, 18, 34).contains(relMouse)) {
                GuiCraftingRecipe.openRecipeGui("writingdesk");
            }
        }
        return false;
    }

    @Override
    public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) {}

    @Override
    public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button) {}

    @Override
    public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
        return false;
    }

    @Override
    public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {}

    @Override
    public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime) {}

}
