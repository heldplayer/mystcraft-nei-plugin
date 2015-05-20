package me.heldplayer.plugins.nei.mystcraft.client;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import me.heldplayer.plugins.nei.mystcraft.AgeInfo;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.specialattack.forge.core.asm.AccessHelper;
import net.specialattack.forge.core.client.GLState;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class AgeExplorerRecipeHandler extends TemplateRecipeHandler {

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("nei.mystcraft.recipe.ages");
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("item")) {
            if (results.length > 0) {
                this.loadCraftingRecipes((ItemStack) results[0]);
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (result.getItem() == MystObjs.descriptiveBook) {
            NBTTagCompound tag = result.stackTagCompound;
            if (tag == null) {
                return;
            }

            int dim = tag.getInteger("Dimension");

            AgeInfo ageInfo = ClientProxy.clientAgesMap.get(dim);

            if (ageInfo == null || ageInfo.pages == null) {
                return;
            }

            CachedBooksRecipe recipe = new CachedBooksRecipe(ageInfo, 0);
            this.arecipes.add(recipe);
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient.getItem() == MystObjs.descriptiveBook) {
            NBTTagCompound tag = ingredient.stackTagCompound;
            if (tag == null) {
                return;
            }

            int dim = tag.getInteger("Dimension");

            AgeInfo ageInfo = ClientProxy.clientAgesMap.get(dim);

            if (ageInfo == null || ageInfo.symbols == null) {
                return;
            }

            CachedBooksRecipe recipe = new CachedBooksRecipe(ageInfo, 1);
            this.arecipes.add(recipe);
        }
    }

    @Override
    public String getGuiTexture() {
        return "neimystcraft:textures/gui/ages.png";
    }

    @Override
    public void drawExtras(int recipeId) {
        CachedBooksRecipe recipe = (CachedBooksRecipe) this.arecipes.get(recipeId);

        //recipe.currentScroll = (float) ((this.cycleticks % 42) / 2) / 20.0F;

        GuiDraw.drawTexturedModalRect(152, 17 + (int) (91 * recipe.currentScroll), recipe.canScroll ? 232 : 244, 0, 12, 15);

        GuiDraw.drawString(recipe.ageInfo.ageName, 5, 2, 0x404040, false);
        GuiDraw.drawStringR(StatCollector.translateToLocal("nei.mystcraft.recipe.ages.modes." + recipe.mode), 160, 2, 0x404040, false);
    }

    @Override
    public void drawBackground(int recipe) {
        GLState.glColor4f(1, 1, 1, 1);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 10, 5, 11, 166, 114);
    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipeId) {
        CachedBooksRecipe recipe = (CachedBooksRecipe) this.arecipes.get(recipeId);

        if (!recipe.canScroll) {
            return super.handleTooltip(gui, currenttip, recipeId);
        }

        boolean clicking = Mouse.isButtonDown(0);

        Point mousepos = GuiDraw.getMousePosition();
        Point relMouse = new Point(mousepos.x - AccessHelper.getGuiLeft(gui) - 5, mousepos.y - AccessHelper.getGuiTop(gui) - 16);

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
        }

        int i = Mouse.getDWheel();

        if (i != 0) {
            int j = recipe.stacks.size() / 8 - 6 + 1;

            if (i > 0) {
                i = 1;
            }

            if (i < 0) {
                i = -1;
            }

            recipe.currentScroll = (float) ((double) recipe.currentScroll - (double) i / (double) j);

            if (recipe.currentScroll < 0.0F) {
                recipe.currentScroll = 0.0F;
            }

            if (recipe.currentScroll > 1.0F) {
                recipe.currentScroll = 1.0F;
            }

            recipe.scroll();
        }

        return super.handleTooltip(gui, currenttip, recipeId);
    }

    public class CachedBooksRecipe extends CachedRecipe {

        protected AgeInfo ageInfo;
        protected int mode; // 0 = Pages; 1 = Symbols
        protected float currentScroll;
        protected boolean isScrolling;
        protected boolean wasClicking;
        protected boolean canScroll;
        private ArrayList<PositionedStack> visibleStacks;
        private PositionedStack[] allStacks;
        private ArrayList<ItemStack> stacks;

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
                    this.stacks.add(ageInfo.pages.get(i));
                }
            }
            if (mode == 1) {
                for (int i = 0; i < ageInfo.symbols.size(); i++) {
                    ItemStack stack = Integrator.itemFactory.buildSymbolPage(ageInfo.symbols.get(i));
                    this.stacks.add(stack);
                }
            }

            if (this.stacks.size() >= 48) {
                this.canScroll = true;
            }

            this.scroll();
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

        @Override
        public PositionedStack getResult() {
            return null;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return this.visibleStacks;
        }
    }

}
