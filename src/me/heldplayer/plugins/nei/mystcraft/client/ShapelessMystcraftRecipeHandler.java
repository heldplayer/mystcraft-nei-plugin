
package me.heldplayer.plugins.nei.mystcraft.client;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import codechicken.core.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.ShapedRecipeHandler;

import com.xcompwiz.mystcraft.api.MystObjects;

public class ShapelessMystcraftRecipeHandler extends ShapedRecipeHandler {

    public int[][] stackorder = new int[][] { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 }, { 0, 2 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } };

    public class CachedShapelessMystcraftRecipe extends CachedRecipe {

        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;

        public CachedShapelessMystcraftRecipe() {
            this.ingredients = new ArrayList<PositionedStack>();
        }

        public CachedShapelessMystcraftRecipe(ItemStack output) {
            this();
            this.setResult(output);
        }

        public CachedShapelessMystcraftRecipe(Object[] input, ItemStack output) {
            this(Arrays.asList(input), output);
        }

        public CachedShapelessMystcraftRecipe(List<?> input, ItemStack output) {
            this(output);
            this.setIngredients(input);
        }

        public void setIngredients(List<?> items) {
            this.ingredients.clear();
            for (int ingred = 0; ingred < items.size(); ingred++) {
                PositionedStack stack = new PositionedStack(items.get(ingred), 25 + ShapelessMystcraftRecipeHandler.this.stackorder[ingred][0] * 18, 6 + ShapelessMystcraftRecipeHandler.this.stackorder[ingred][1] * 18);
                stack.setMaxSize(1);
                this.ingredients.add(stack);
            }
        }

        public void setResult(ItemStack output) {
            this.result = new PositionedStack(output, 119, 24);
        }

        @Override
        public ArrayList<PositionedStack> getIngredients() {
            return (ArrayList<PositionedStack>) this.getCycledIngredients(ShapelessMystcraftRecipeHandler.this.cycleticks / 20, this.ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return this.result;
        }

    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("nei.mystcraft.recipe.shapeless");
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crafting") && this.getClass() == ShapelessMystcraftRecipeHandler.class) {
            this.addAllLinkbooks();
        }
        else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (MystObjects.page == null) {
            return;
        }

        // Hardcoding untill proper recipes are added
        if (result.getItem() == MystObjects.linkbook_unlinked || result.getItem() == MystObjects.linkbook) {
            ItemStack linkingbook = result.copy();
            linkingbook.stackSize = 1;

            NBTTagCompound compound = linkingbook.getTagCompound();
            if (compound == null) {
                this.addAllLinkbooks();
                return;
            }

            if (!compound.hasKey("linkpanel")) {
                this.addAllLinkbooks();
                return;
            }
            NBTTagCompound linkPanelCompound = compound.getCompoundTag("linkpanel");

            if (!linkPanelCompound.hasKey("properties")) {
                this.addAllLinkbooks();
                return;
            }
            NBTTagList list = linkPanelCompound.getTagList("properties");

            if (list.tagCount() == 0) {
                this.addAllLinkbooks();
                return;
            }

            ItemStack linkPanel = new ItemStack(MystObjects.page);
            linkPanel.stackTagCompound = (NBTTagCompound) linkingbook.stackTagCompound.copy();

            CachedShapelessMystcraftRecipe recipe = new CachedShapelessMystcraftRecipe(linkingbook);
            List<ItemStack> ingredients = new ArrayList<ItemStack>();
            ingredients.add(new ItemStack(Item.leather));
            ingredients.add(linkPanel);
            recipe.setIngredients(ingredients);

            this.arecipes.add(recipe);
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (MystObjects.page == null) {
            return;
        }

        if (ingredient.getItem() == MystObjects.page) {
            ItemStack linkPanel = ingredient.copy();
            linkPanel.stackSize = 1;
            if (linkPanel.stackTagCompound == null) {
                return;
            }

            NBTTagCompound compound = linkPanel.getTagCompound();
            if (compound == null) {
                return;
            }

            if (!compound.hasKey("linkpanel")) {
                return;
            }
            NBTTagCompound linkPanelCompound = compound.getCompoundTag("linkpanel");

            if (!linkPanelCompound.hasKey("properties")) {
                this.addAllLinkbooks();
                return;
            }
            NBTTagList list = linkPanelCompound.getTagList("properties");

            if (list.tagCount() == 0) {
                this.addAllLinkbooks();
                return;
            }

            ItemStack linkingBook = new ItemStack(MystObjects.linkbook_unlinked);
            linkingBook.stackTagCompound = (NBTTagCompound) linkPanel.stackTagCompound.copy();

            CachedShapelessMystcraftRecipe recipe = new CachedShapelessMystcraftRecipe(linkingBook);
            List<ItemStack> ingredients = new ArrayList<ItemStack>();
            ingredients.add(new ItemStack(Item.leather));
            ingredients.add(linkPanel);
            recipe.setIngredients(ingredients);

            this.arecipes.add(recipe);
        }
    }

    @Override
    public boolean isRecipe2x2(int recipe) {
        return this.getIngredientStacks(recipe).size() <= 4;
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipeId) {
        CachedShapelessMystcraftRecipe recipe = (CachedShapelessMystcraftRecipe) this.arecipes.get(recipeId);

        currenttip = super.handleItemTooltip(gui, stack, currenttip, recipeId);

        Point mousepos = GuiDraw.getMousePosition();
        Point relMouse = new Point(mousepos.x - gui.guiLeft, mousepos.y - gui.guiTop);

        Point recipepos = gui.getRecipePosition(recipeId);

        if (currenttip.isEmpty() && stack == null && new Rectangle(recipepos.x, recipepos.y, 166, 60).contains(relMouse)) {
            if (recipe != null) {
                if (recipe.result.item.getItem() == MystObjects.linkbook_unlinked) {
                    currenttip.add(StatCollector.translateToLocal("nei.mystcraft.linkbook.activate"));
                }
            }
        }
        return currenttip;
    }

    private void addAllLinkbooks() {
        for (ItemStack linkPanel : Integrator.getAllLinkpanels()) {
            ItemStack output = new ItemStack(MystObjects.linkbook_unlinked);
            output.stackTagCompound = (NBTTagCompound) linkPanel.stackTagCompound.copy();

            CachedShapelessMystcraftRecipe recipe = new CachedShapelessMystcraftRecipe(output);
            List<ItemStack> ingredients = new ArrayList<ItemStack>();
            ingredients.add(new ItemStack(Item.leather));
            ingredients.add(linkPanel);
            recipe.setIngredients(ingredients);

            this.arecipes.add(recipe);
        }
    }

}
