
package me.heldplayer.plugins.nei.mystcraft.client;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import codechicken.core.gui.GuiDraw;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.forge.IContainerInputHandler;
import codechicken.nei.forge.IContainerTooltipHandler;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;

public class MystTooltipHandler implements IContainerInputHandler, IContainerTooltipHandler {

    @Override
    public List<String> handleTooltipFirst(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
        if (NEIConfig.guiWritingDeskClass.isAssignableFrom(gui.getClass())) {
            Point mousepos = GuiDraw.getMousePosition();
            Point relMouse = new Point(mousepos.x - gui.guiLeft, mousepos.y - gui.guiTop);

            Rectangle rect = new Rectangle(156 + 228, 45, 18, 34);

            if (currenttip.isEmpty() && gui.manager.shouldShowTooltip() && rect.contains(relMouse)) {
                currenttip.add(StatCollector.translateToLocal("nei.mystcraft.recipes"));
            }
        }
        else if (NEIConfig.guiInkMixerClass.isAssignableFrom(gui.getClass())) {
            Point mousepos = GuiDraw.getMousePosition();
            Point relMouse = new Point(mousepos.x - gui.guiLeft, mousepos.y - gui.guiTop);

            Point center = new Point(87, 49);

            if (currenttip.isEmpty() && gui.manager.shouldShowTooltip() && center.distance(relMouse) < 34.0D) {
                currenttip.add(StatCollector.translateToLocal("nei.mystcraft.recipes"));
            }
        }
        return currenttip;
    }

    @Override
    public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {
        if (gui instanceof GuiRecipe) {
            Point mousepos = GuiDraw.getMousePosition();
            Point relMouse = new Point(mousepos.x - gui.guiLeft, mousepos.y - gui.guiTop);

            GuiRecipe guiRecipe = (GuiRecipe) gui;

            if (currenttip.isEmpty() && gui.manager.shouldShowTooltip()) {
                if (guiRecipe.currenthandlers.get(guiRecipe.recipetype) instanceof WritingDeskRecipeHandler) {
                    Rectangle rect = new Rectangle(151, 34, 18, 34);
                    if (rect.contains(relMouse)) {
                        currenttip.add(StatCollector.translateToLocal("nei.mystcraft.recipes"));
                    }
                }
                else if (guiRecipe.currenthandlers.get(guiRecipe.recipetype) instanceof InkMixerRecipeHandler) {
                    Point center = new Point(87, 49);
                    if (center.distance(relMouse) < 34.0D) {
                        currenttip.add(StatCollector.translateToLocal("nei.mystcraft.recipes"));
                    }
                }
            }
        }
        return currenttip;
    }

    @Override
    public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode) {
        if (NEIConfig.guiWritingDeskClass.isAssignableFrom(gui.getClass())) {
            Point mousepos = GuiDraw.getMousePosition();
            Point relMouse = new Point(mousepos.x - gui.guiLeft, mousepos.y - gui.guiTop);

            Rectangle rect = new Rectangle(156 + 228, 45, 18, 34);

            if (rect.contains(relMouse)) {
                if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe")) {
                    GuiCraftingRecipe.openRecipeGui("writingdesk");
                    return true;
                }
                else if (keyCode == NEIClientConfig.getKeyBinding("gui.usage")) {
                    GuiUsageRecipe.openRecipeGui("writingdesk");
                    return true;
                }
            }
        }
        else if (NEIConfig.guiInkMixerClass.isAssignableFrom(gui.getClass())) {
            Point mousepos = GuiDraw.getMousePosition();
            Point relMouse = new Point(mousepos.x - gui.guiLeft, mousepos.y - gui.guiTop);

            Point center = new Point(87, 49);

            if (center.distance(relMouse) < 34.0D) {
                if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe")) {
                    GuiCraftingRecipe.openRecipeGui("inkmixer");
                    return true;
                }
                else if (keyCode == NEIClientConfig.getKeyBinding("gui.usage")) {
                    GuiUsageRecipe.openRecipeGui("inkmixer");
                    return true;
                }
            }
        }

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
        if (NEIConfig.guiWritingDeskClass.isAssignableFrom(gui.getClass())) {
            Point mousepos = GuiDraw.getMousePosition();
            Point relMouse = new Point(mousepos.x - gui.guiLeft, mousepos.y - gui.guiTop);

            Rectangle rect = new Rectangle(156 + 228, 45, 18, 34);

            if (rect.contains(relMouse)) {
                if (button == 0) {
                    GuiCraftingRecipe.openRecipeGui("writingdesk");
                    return true;
                }
                else if (button == 1) {
                    GuiUsageRecipe.openRecipeGui("writingdesk");
                    return true;
                }
            }
        }
        else if (NEIConfig.guiInkMixerClass.isAssignableFrom(gui.getClass())) {
            Point mousepos = GuiDraw.getMousePosition();
            Point relMouse = new Point(mousepos.x - gui.guiLeft, mousepos.y - gui.guiTop);

            Point center = new Point(87, 49);

            if (center.distance(relMouse) < 34.0D) {
                if (button == 0) {
                    GuiCraftingRecipe.openRecipeGui("inkmixer");
                    return true;
                }
                else if (button == 1) {
                    GuiUsageRecipe.openRecipeGui("inkmixer");
                    return true;
                }
            }
        }
        else if (gui instanceof GuiRecipe) {
            Point mousepos = GuiDraw.getMousePosition();
            Point relMouse = new Point(mousepos.x - gui.guiLeft, mousepos.y - gui.guiTop);

            GuiRecipe guiRecipe = (GuiRecipe) gui;

            if (guiRecipe.currenthandlers.get(guiRecipe.recipetype) instanceof WritingDeskRecipeHandler) {
                Rectangle rect = new Rectangle(156, 45, 18, 34);

                if (rect.contains(relMouse)) {
                    if (button == 0) {
                        GuiCraftingRecipe.openRecipeGui("writingdesk");
                        return true;
                    }
                    else if (button == 1) {
                        GuiUsageRecipe.openRecipeGui("writingdesk");
                        return true;
                    }
                }
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
