
package me.heldplayer.plugins.nei.mystcraft.client;

import static codechicken.core.gui.GuiDraw.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import me.heldplayer.plugins.nei.mystcraft.AgeInfo;
import me.heldplayer.plugins.nei.mystcraft.CommonProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import codechicken.core.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.xcompwiz.mystcraft.api.MystAPI;
import com.xcompwiz.mystcraft.api.MystObjects;

public class BooksRecipeHandler extends TemplateRecipeHandler {

    public class CachedBooksRecipe extends CachedRecipe {

        protected AgeInfo ageInfo;
        protected int mode; // 0 = Pages; 1 = Symbols
        private ArrayList<PositionedStack> visibleStacks;
        private PositionedStack[] allStacks;
        private ArrayList<ItemStack> stacks;
        protected float currentScroll;
        protected boolean isScrolling;
        protected boolean wasClicking;
        protected boolean canScroll;

        public CachedBooksRecipe(AgeInfo ageInfo, int mode) {
            this.ageInfo = ageInfo;
            this.mode = mode;

            this.visibleStacks = new ArrayList<PositionedStack>();
            this.allStacks = new PositionedStack[48];
            this.stacks = new ArrayList<ItemStack>();

            for (int i = 0; i < this.allStacks.length; i++) {
                this.allStacks[i] = new PositionedStack(new ItemStack[0], (i % 8) * 18 + 4, i / 8 * 18 + 17);
            }

            if (mode == 0) {
                for (int i = 0; i < ageInfo.pages.size(); i++) {
                    stacks.add(ageInfo.pages.get(i));
                    //this.visibleStacks.add(new PositionedStack(ageInfo.pages.get(i), (i % 8) * 18 + 4, i / 8 * 18 + 17));
                }
            }
            if (mode == 1) {
                for (int i = 0; i < ageInfo.symbols.size(); i++) {
                    ItemStack stack = MystAPI.itemFact.buildSymbolPage(ageInfo.symbols.get(i));
                    stacks.add(stack);
                    //this.visibleStacks.add(new PositionedStack(stack, (i % 8) * 18 + 4, i / 8 * 18 + 17));
                }
            }

            if (this.stacks.size() >= 48) {
                this.canScroll = true;
            }

            this.scroll();
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return this.visibleStacks;
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        public void scroll() {
            int i = this.stacks.size() / 8 - 6 + 1;
            int j = (int) ((double) (this.currentScroll * (float) i) + 0.5D);

            if (j < 0) {
                j = 0;
            }

            this.visibleStacks.clear();

            for (int k = 0; k < 6; ++k) {
                for (int l = 0; l < 8; ++l) {
                    int i1 = l + (k + j) * 8;

                    if (i1 >= 0 && i1 < this.stacks.size()) {
                        this.allStacks[l + k * 8].item = this.stacks.get(i1);
                        this.visibleStacks.add(this.allStacks[l + k * 8]);
                    }
                }
            }
        }
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("nei.mystcraft.recipe.ages");
    }

    @Override
    public String getGuiTexture() {
        return "neimystcraft:textures/gui/ages.png";
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (MystObjects.page == null) {
            return;
        }

        if (outputId.equals("item")) {
            if (results.length > 0) {
                this.loadCraftingRecipes((ItemStack) results[0]);
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (MystObjects.page == null) {
            return;
        }

        if (result.getItem() == MystObjects.descriptive_book) {
            NBTTagCompound tag = result.stackTagCompound;
            if (tag == null) {
                return;
            }

            int dim = tag.getInteger("Dimension");

            AgeInfo ageInfo = CommonProxy.clientAgesMap.get(Integer.valueOf(dim));

            if (ageInfo == null || ageInfo.pages == null) {
                return;
            }

            CachedBooksRecipe recipe = new CachedBooksRecipe(ageInfo, 0);
            this.arecipes.add(recipe);
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (MystObjects.page == null) {
            return;
        }

        if (ingredient.getItem() == MystObjects.descriptive_book) {
            NBTTagCompound tag = ingredient.stackTagCompound;
            if (tag == null) {
                return;
            }

            int dim = tag.getInteger("Dimension");

            AgeInfo ageInfo = CommonProxy.clientAgesMap.get(Integer.valueOf(dim));

            if (ageInfo == null || ageInfo.symbols == null) {
                return;
            }

            CachedBooksRecipe recipe = new CachedBooksRecipe(ageInfo, 1);
            this.arecipes.add(recipe);
        }
    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        changeTexture(getGuiTexture());
        drawTexturedModalRect(0, 10, 5, 11, 166, 114);
    }

    @Override
    public void drawExtras(int recipeId) {
        CachedBooksRecipe recipe = (CachedBooksRecipe) this.arecipes.get(recipeId);

        //recipe.currentScroll = (float) ((this.cycleticks % 42) / 2) / 20.0F;

        drawTexturedModalRect(152, 17 + (int) (91 * recipe.currentScroll), recipe.canScroll ? 232 : 244, 0, 12, 15);

        drawString(recipe.ageInfo.ageName, 5, 2, 0x404040, false);
        drawStringR(StatCollector.translateToLocal("nei.mystcraft.recipe.ages.modes." + recipe.mode), 160, 2, 0x404040, false);
    }

    @Override
    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipeId) {
        CachedBooksRecipe recipe = (CachedBooksRecipe) this.arecipes.get(recipeId);

        if (!recipe.canScroll) {
            return super.handleTooltip(gui, currenttip, recipeId);
        }

        boolean clicking = Mouse.isButtonDown(0);

        Point mousepos = GuiDraw.getMousePosition();
        Point relMouse = new Point(mousepos.x - gui.guiLeft - 5, mousepos.y - gui.guiTop - 16);

        if (!recipe.wasClicking && clicking && relMouse.x >= 152 && relMouse.y >= 17 && relMouse.x < 164 && relMouse.y < 123) {
            recipe.isScrolling = true;
        }

        if (!clicking) {
            recipe.isScrolling = false;
        }

        recipe.wasClicking = clicking;

        if (recipe.isScrolling) {
            recipe.currentScroll = ((float) (relMouse.y - 17) - 7.5F) / ((float) (123 - 17) - 15.0F);

            if (recipe.currentScroll < 0.0F) {
                recipe.currentScroll = 0.0F;
            }

            if (recipe.currentScroll > 1.0F) {
                recipe.currentScroll = 1.0F;
            }

            recipe.scroll();

            currenttip.add("Scrolling...");
        }

        return super.handleTooltip(gui, currenttip, recipeId);
    }

}
