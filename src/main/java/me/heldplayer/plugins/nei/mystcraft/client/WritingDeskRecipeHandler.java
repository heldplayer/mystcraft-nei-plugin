package me.heldplayer.plugins.nei.mystcraft.client;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.xcompwiz.mystcraft.core.InternalAPI;
import com.xcompwiz.mystcraft.symbol.IAgeSymbol;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.client.renderer.WritingDeskOverlayRenderer;
import me.heldplayer.plugins.nei.mystcraft.modules.ModuleRecipes;
import me.heldplayer.plugins.nei.mystcraft.modules.ModuleTooltips;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.specialattack.forge.core.asm.AccessHelper;
import net.specialattack.forge.core.client.GuiHelper;
import org.lwjgl.opengl.GL11;

public class WritingDeskRecipeHandler extends TemplateRecipeHandler {

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("tile.myst.writing_desk.name");
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (MystObjs.page.getItem() == null || !ModuleRecipes.writingDeskEnabled) {
            return;
        }

        if (outputId.equals("item") || outputId.equals("writingdesk")) {
            if (results.length > 0) {
                this.loadCraftingRecipes((ItemStack) results[0]);
            } else {
                CachedWritingDeskRecipe recipe = new CachedWritingDeskRecipe(InternalAPI.symbol.getSymbolForIdentifier(null), true);
                this.arecipes.add(recipe);

                List<IAgeSymbol> recipes = MystObjs.getAllRegisteredSymbols();

                for (IAgeSymbol symbol : recipes) {
                    recipe = new CachedWritingDeskRecipe(symbol, false);
                    this.arecipes.add(recipe);
                }
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (MystObjs.page.getItem() == null || !ModuleRecipes.writingDeskEnabled) {
            return;
        }

        if (result.getItem() == MystObjs.page.getItem()) {
            String symbol = InternalAPI.page.getPageSymbol(result);

            if (symbol == null || symbol.isEmpty()) {
                return;
            }

            CachedWritingDeskRecipe recipe = new CachedWritingDeskRecipe(InternalAPI.symbol.getSymbolForIdentifier(symbol), false);
            this.arecipes.add(recipe);
        } else if (result.getItem() == Items.glass_bottle) {
            CachedWritingDeskRecipe recipe = new CachedWritingDeskRecipe(InternalAPI.symbol.getSymbolForIdentifier(null), true);
            this.arecipes.add(recipe);

            List<IAgeSymbol> recipes = InternalAPI.symbol.getAllRegisteredSymbols();

            for (IAgeSymbol symbol : recipes) {
                recipe = new CachedWritingDeskRecipe(symbol, false);
                this.arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (MystObjs.page.getItem() == null || !ModuleRecipes.writingDeskEnabled) {
            return;
        }

        if (ingredient.getItem() == MystObjs.page.getItem()) {
            String symbol = InternalAPI.page.getPageSymbol(ingredient);

            if (symbol == null || symbol.isEmpty()) {
                return;
            }

            CachedWritingDeskRecipe recipe = new CachedWritingDeskRecipe(InternalAPI.symbol.getSymbolForIdentifier(symbol), true);
            this.arecipes.add(recipe);

            recipe = new CachedWritingDeskRecipe(InternalAPI.symbol.getSymbolForIdentifier(symbol), false);
            this.arecipes.add(recipe);
        } else if (ingredient.getItem() == MystObjs.notebook.getItem()) {
            CachedWritingDeskRecipe recipe = new CachedWritingDeskRecipe(InternalAPI.symbol.getSymbolForIdentifier(null), true);
            this.arecipes.add(recipe);
        } else if (ingredient.getItem() == Items.paper || ingredient.getItem() == MystObjs.inkvial.getItem()) {
            CachedWritingDeskRecipe recipe = new CachedWritingDeskRecipe(InternalAPI.symbol.getSymbolForIdentifier(null), true);
            this.arecipes.add(recipe);

            List<IAgeSymbol> recipes = InternalAPI.symbol.getAllRegisteredSymbols();

            for (IAgeSymbol symbol : recipes) {
                recipe = new CachedWritingDeskRecipe(symbol, false);
                this.arecipes.add(recipe);
            }
        }
    }

    @Override
    public String getGuiTexture() {
        return "mystcraft:gui/writingdesk.png";
    }

    @Override
    public String getOverlayIdentifier() {
        return "writingdesk";
    }

    @Override
    public void drawExtras(int recipeId) {
        CachedWritingDeskRecipe recipe = (CachedWritingDeskRecipe) this.arecipes.get(recipeId);
        this.renderTank(126, 0, 16, 70, recipe);

        if (recipe.isNotebook) {
            GuiDraw.drawRect(25, 0, 90, 50, 0xAA000000);
            GuiDraw.drawRect(25, 0, 10, 50, 0x33000000);
            GuiDraw.drawRect(25 + 80, 0, 10, 50, 0x33000000);

            Integrator.renderPage(recipe.symbol, 37.0F, 3.0F, 0.0F, 33.0F, 44.0F);
        } else if (recipe.symbol != null) {
            Integrator.renderPage(recipe.symbol, 27.0F, 0.0F, 0.0F, 36.0F, 48.0F);
        }

        recipe.textField.drawTextBox();
    }

    private void renderTank(int left, int top, int width, int height, CachedWritingDeskRecipe recipe) {
        GuiDraw.drawGradientRect(left, top, width, height, 0x99000000, 0x99000000);
        GuiDraw.drawGradientRect(left + 1, top + 1, width - 2, height - 2, 0xFFCCCCEE, 0xFF666699);

        float filled = (float) recipe.tank.getFluidAmount() / (float) recipe.tank.getCapacity();
        if (filled > 1.0F) {
            filled = 1.0F;
        }
        int ltop = top + height - 1;
        int lheight = (int) ((height - 2) * filled);
        GuiHelper.drawFluid(recipe.tank.getFluid().getFluid(), left + 1, ltop - lheight, width - 2, lheight);
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return Integrator.guiWritingDeskClass;
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 7, 166, 70);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        List<IAgeSymbol> symbols = null;

        if (!NEIClientUtils.shiftKey() && this.cycleticks % 20 == 0) {
            for (CachedRecipe cachedRecipe : this.arecipes) {
                CachedWritingDeskRecipe recipe = (CachedWritingDeskRecipe) cachedRecipe;
                Fluid fluid = recipe.tank.getFluid().getFluid();
                recipe.tank.drain(50, true);
                if (recipe.tank.getFluidAmount() < 0) {
                    recipe.tank.fill(new FluidStack(fluid, 1000), true);
                }

                if (recipe.isNotebook && recipe.nullSymbol) {
                    if (symbols == null) {
                        symbols = MystObjs.getAllRegisteredSymbols();
                    }

                    recipe.symbol = symbols.get(Objects.rnd.nextInt(symbols.size()));
                }
            }
        }
    }

    @Override
    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe) {
        IStackPositioner positioner = RecipeInfo.getStackPositioner(gui, this.getOverlayIdentifier());
        if (positioner == null) {
            return null;
        }
        return new WritingDeskOverlayRenderer(this.getIngredientStacks(recipe), positioner);
    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipeId) {
        CachedWritingDeskRecipe recipe = (CachedWritingDeskRecipe) this.arecipes.get(recipeId);

        currenttip = super.handleItemTooltip(gui, stack, currenttip, recipeId);

        Point mousepos = GuiDraw.getMousePosition();
        Point relMouse = new Point(mousepos.x - AccessHelper.getGuiLeft(gui), mousepos.y - AccessHelper.getGuiTop(gui));

        if (recipe.isNotebook) {
            if (currenttip.isEmpty() && stack == null && new Rectangle(42, 19, 33, 44).contains(relMouse)) {
                if (recipe.symbol != null) {
                    currenttip.add(recipe.symbol.displayName());
                }
            }
        } else if (recipe.symbol != null) {
            if (currenttip.isEmpty() && stack == null && new Rectangle(32, 16, 36, 48).contains(relMouse)) {
                if (recipe.symbol != null) {
                    currenttip.add(recipe.symbol.displayName());
                }
            }
        }

        if (currenttip.isEmpty() && stack == null && new Rectangle(131, 16, 16, 71).contains(relMouse)) {
            currenttip.add(MystObjs.black_ink.getLocalizedName(recipe.tank.getFluid()) + ": " + recipe.tank + "/1000");
        }

        if (ModuleTooltips.recipesTooltips && currenttip.isEmpty() && stack == null && new Rectangle(151, 34, 18, 34).contains(relMouse)) {
            currenttip.add(StatCollector.translateToLocal("nei.mystcraft.recipes"));
        }

        if (!ModuleTooltips.writingDeskTooltips) {
            return currenttip;
        }

        if (currenttip.isEmpty() && stack == null && new Rectangle(28, 70, 99, 14).contains(relMouse)) {
            if (recipe.isNotebook) {
                currenttip.add(StatCollector.translateToLocal("nei.mystcraft.writingdesk.notebook.name"));
            } else {
                if (recipe.symbol != null) {
                    currenttip.add(StatCollector.translateToLocal("nei.mystcraft.writingdesk.page.name"));
                }
            }
        }

        Point recipepos = gui.getRecipePosition(recipeId);

        if (currenttip.isEmpty() && stack == null && new Rectangle(recipepos.x, recipepos.y, 166, 80).contains(relMouse)) {
            if (recipe.isNotebook) {
                currenttip.add(StatCollector.translateToLocal("nei.mystcraft.writingdesk.notebook"));
            } else {
                if (recipe.symbol != null) {
                    currenttip.add(StatCollector.translateToLocal("nei.mystcraft.writingdesk.page"));
                }
            }
        }
        return currenttip;
    }

    public class CachedWritingDeskRecipe extends CachedRecipe {

        protected IAgeSymbol symbol;
        protected FluidTank tank;
        protected boolean isNotebook;
        protected boolean nullSymbol;
        private PositionedStack leftOver;
        private PositionedStack result;
        private ArrayList<PositionedStack> ingredients;
        private GuiTextField textField;

        public CachedWritingDeskRecipe(IAgeSymbol symbol, boolean isNotebook) {
            this.symbol = symbol;
            this.ingredients = new ArrayList<PositionedStack>();
            this.tank = new FluidTank(MystObjs.black_ink, 1000, 1000);
            this.isNotebook = isNotebook;
            this.nullSymbol = symbol == null;
            this.textField = new GuiTextField(GuiDraw.fontRenderer, 23, 54, 99, 14);

            this.ingredients.add(new PositionedStack(new ItemStack(MystObjs.inkvial.getItem()), 147, 1));
            this.ingredients.add(new PositionedStack(new ItemStack(Items.paper), 3, 1));
            this.leftOver = new PositionedStack(new ItemStack(Items.glass_bottle), 147, 53);

            if (isNotebook) {
                this.result = new PositionedStack(InternalAPI.itemFact.buildNotebook("Named Notebook", new String[0]), 3, 53);

                this.textField.setText("Named Notebook");

                if (this.nullSymbol) {
                    List<IAgeSymbol> symbols = MystObjs.getAllRegisteredSymbols();

                    this.symbol = symbols.get(Objects.rnd.nextInt(symbols.size()));
                }
            } else {
                if (symbol != null) {
                    this.result = new PositionedStack(InternalAPI.itemFact.buildSymbolPage(symbol.identifier()), 3, 53);

                    this.textField.setText(symbol.displayName());
                    this.textField.setCursorPosition(0);
                }
            }
        }

        @Override
        public PositionedStack getResult() {
            return this.result;
        }

        @Override
        public ArrayList<PositionedStack> getIngredients() {
            return this.ingredients;
        }

        @Override
        public PositionedStack getOtherStack() {
            return this.leftOver;
        }

    }

}
