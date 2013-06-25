
package me.heldplayer.plugins.NEI.mystcraft;

import java.util.ArrayList;
import java.util.Arrays;

import me.heldplayer.util.HeldCore.Vector;
import me.heldplayer.util.HeldCore.VectorPool;
import me.heldplayer.util.HeldCore.client.GuiHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.xcompwiz.mystcraft.api.MystObjects;
import com.xcompwiz.mystcraft.api.internals.Color;
import com.xcompwiz.mystcraft.api.internals.ColorGradient;

/**
 * NEI integration class for ink mixer recipes
 * 
 * @author heldplayer
 * 
 */
@SuppressWarnings("rawtypes")
public class InkMixerRecipeHandler extends TemplateRecipeHandler {

    public class CachedInkMixerRecipe extends CachedRecipe {

        public String[] modifiers;
        public Float[] percentages;
        public ColorGradient gradient;
        public long frame;
        private PositionedStack stack;
        private PositionedStack leftOver;
        private PositionedStack ingredient;
        private ArrayList<PositionedStack> ingredients;

        public CachedInkMixerRecipe(Object ingredient) {
            this.ingredients = new ArrayList<PositionedStack>();

            if (ingredient instanceof ItemStack) {
                this.ingredient = new PositionedStack(ingredient, 74, 29);
                this.ingredients.add(this.ingredient);
            }
            else if (ingredient instanceof String) {
                ArrayList<ItemStack> list = OreDictionary.getOres((String) ingredient);

                if (list.size() > 0) {
                    this.ingredient = new PositionedStack(list, 74, 29);
                    this.ingredients.add(this.ingredient);
                }
            }
            else if (ingredient instanceof Integer) {
                this.ingredient = new PositionedStack(new ItemStack((Integer) ingredient, 1, OreDictionary.WILDCARD_VALUE), 74, 29);
                this.ingredients.add(this.ingredient);
            }
            else {
                this.ingredient = null;
            }

            InkMixerRecipe result = Integrator.getInkMixerRecipe(this.ingredient != null ? this.ingredient.item : null);
            if (result == null) {
                this.modifiers = null;
                this.percentages = null;
                this.gradient = null;
            }
            else {
                this.modifiers = result.modifiers;
                this.percentages = result.percentages;
                this.gradient = result.gradient;
            }
            this.frame = 0;

            ItemStack stack = new ItemStack(MystObjects.page, 1, 0);

            NBTTagCompound compound = new NBTTagCompound("tag");
            NBTTagCompound linkPanelCompound = new NBTTagCompound("linkpanel");

            NBTTagList list = new NBTTagList("properties");

            if (this.modifiers != null) {
                for (String modifier : this.modifiers) {
                    list.appendTag(new NBTTagString("", modifier));
                }
            }

            linkPanelCompound.setTag("properties", list);

            compound.setTag("linkpanel", linkPanelCompound);

            stack.setTagCompound(compound);

            this.stack = new PositionedStack(stack, 147, 37);

            this.ingredients.add(new PositionedStack(new ItemStack(MystObjects.inkvial), 3, 16));
            this.ingredients.add(new PositionedStack(new ItemStack(Item.paper), 3, 37));
            this.leftOver = new PositionedStack(new ItemStack(Item.glassBottle), 147, 16);
        }

        @Override
        public PositionedStack getResult() {
            return this.stack;
        }

        @Override
        public ArrayList<PositionedStack> getIngredients() {
            return this.ingredients;
        }

        @Override
        public PositionedStack getIngredient() {
            return this.ingredients.get(0);
        }

        @Override
        public PositionedStack getOtherStack() {
            return this.leftOver;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof CachedInkMixerRecipe)) {
                return false;
            }
            CachedInkMixerRecipe other = (CachedInkMixerRecipe) obj;
            if (!this.getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (this.ingredient == null) {
                if (other.ingredient != null) {
                    return false;
                }
            }
            else if (this.ingredient.item == null) {
                if (other.ingredient.item != null) {
                    return false;
                }
            }
            else if (!ItemStack.areItemStacksEqual(this.ingredient.item, other.ingredient.item)) {
                return false;
            }
            if (!Arrays.equals(this.modifiers, other.modifiers)) {
                return false;
            }
            if (this.stack == null) {
                if (other.stack != null) {
                    return false;
                }
            }
            else if (this.stack.item == null) {
                if (other.stack.item != null) {
                    return false;
                }
            }
            else if (!ItemStack.areItemStacksEqual(this.stack.item, other.stack.item)) {
                return false;
            }
            return true;
        }

        private InkMixerRecipeHandler getOuterType() {
            return InkMixerRecipeHandler.this;
        }

    }

    private LiquidStack liquid;

    public InkMixerRecipeHandler() {
        this.liquid = LiquidDictionary.getCanonicalLiquid("Liquid Black Dye");
    }

    @Override
    public String getRecipeName() {
        return "Ink Mixer";
    }

    @Override
    public String getGuiTexture() {
        return "/mods/mystcraft/gui/inkmixer.png";
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (MystObjects.page == null) {
            return;
        }

        if (outputId.equals("item")) {
            this.loadCraftingRecipes((ItemStack) results[0]);
            return;
        }

        ArrayList recipes = Integrator.getALlInkMixerRecipes();

        for (Object recipe : recipes) {
            this.arecipes.add(new CachedInkMixerRecipe(recipe));
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (MystObjects.page == null) {
            return;
        }

        if (result.getItem() == MystObjects.page) {
            NBTTagCompound compound = result.getTagCompound();
            if (compound == null) {
                return;
            }

            NBTTagCompound linkPanelCompound = compound.getCompoundTag("linkpanel");
            if (linkPanelCompound == null) {
                return;
            }

            NBTTagList list = linkPanelCompound.getTagList("properties");
            if (list == null) {
                return;
            }

            ArrayList recipes = Integrator.getALlInkMixerRecipes();

            for (Object recipeObj : recipes) {
                CachedInkMixerRecipe recipe = new CachedInkMixerRecipe(recipeObj);

                if (recipe.modifiers == null) {
                    continue;
                }
                if (list.tagCount() == 0) {
                    recipe:
                    {
                        for (String modifier : recipe.modifiers) {
                            if (!modifier.isEmpty()) {
                                break recipe;
                            }
                        }
                        this.arecipes.add(recipe);
                    }
                    continue;
                }

                for (int i = 0; i < list.tagCount(); i++) {
                    for (String modifier : recipe.modifiers) {
                        if (!modifier.isEmpty() && ((NBTTagString) list.tagAt(i)).data.equals(modifier) && !this.arecipes.contains(recipe)) {
                            this.arecipes.add(recipe);
                            break;
                        }
                    }
                }
            }
        }
        else if (result.getItem() == Item.glassBottle) {
            ArrayList recipes = Integrator.getALlInkMixerRecipes();

            for (Object ingr : recipes) {
                CachedInkMixerRecipe recipe = new CachedInkMixerRecipe(ingr);
                if (recipe.modifiers != null) {
                    this.arecipes.add(recipe);
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (MystObjects.page == null) {
            return;
        }

        if (ingredient.getItem() == Item.paper || ingredient.getItem() == MystObjects.inkvial) {
            ArrayList recipes = Integrator.getALlInkMixerRecipes();

            for (Object ingr : recipes) {
                CachedInkMixerRecipe recipe = new CachedInkMixerRecipe(ingr);
                if (recipe.modifiers != null) {
                    this.arecipes.add(recipe);
                }
            }
        }
        else {
            CachedInkMixerRecipe recipe = new CachedInkMixerRecipe(ingredient);
            if (recipe.modifiers != null) {
                this.arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadTransferRects() {}

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public void drawBackground(GuiContainerManager gui, int recipe) {
        this.renderTank(49, 5, 66, 65, gui, (CachedInkMixerRecipe) this.arecipes.get(recipe));

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        gui.bindTexture(this.getGuiTexture());
        gui.drawTexturedModalRect(0, 0, 5, 11, 166, 76);
    }

    @Override
    public void drawExtras(GuiContainerManager gui, int recipeId) {
        CachedInkMixerRecipe recipe = (CachedInkMixerRecipe) this.arecipes.get(recipeId);

        Vector center = VectorPool.getFreeVector(82, 100, 0);
        Color color = recipe.gradient.getColor(recipe.frame);
        java.awt.Color awtColor = new java.awt.Color(color.r, color.g, color.b);
        DniColourRenderer.render(awtColor, center, 20.0D);
    }

    private void renderTank(int left, int top, int width, int height, GuiContainerManager gui, CachedInkMixerRecipe recipe) {
        GuiHelper.drawLiquid(this.liquid.itemID, this.liquid.itemMeta, left, top, width, height);

        recipe.frame++;
        if (recipe.frame > recipe.gradient.getLength()) {
            recipe.frame = 0;
        }
        Color color = recipe.gradient.getColor(recipe.frame);
        int iColor = color.asInt();
        gui.drawGradientRect(left, top, left + width, top + height, 0x40000000 + iColor, 0xB0000000 + iColor);
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return PluginNEIMystcraft.guiInkMixerClass;
    }

    @Override
    public String getOverlayIdentifier() {
        return "inkmixer";
    }

    @Override
    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe) {
        IStackPositioner positioner = RecipeInfo.getStackPositioner(gui, this.getOverlayIdentifier());
        if (positioner == null) {
            return null;
        }
        return new InkMixerOverlayRenderer(this.getIngredientStacks(recipe), positioner, this.arecipes.get(recipe).getIngredient());
    }

}
