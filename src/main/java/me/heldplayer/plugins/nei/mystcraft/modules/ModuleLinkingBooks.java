package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemInfo;
import cpw.mods.fml.relauncher.Side;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.client.Integrator;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.specialattack.forge.core.config.ConfigValue;
import net.specialattack.forge.core.crafting.CraftingHelper;
import net.specialattack.forge.core.crafting.FakeShapelessSpACoreRecipe;
import net.specialattack.forge.core.crafting.ICraftingResultHandler;
import net.specialattack.forge.core.crafting.ISpACoreRecipe;
import org.apache.logging.log4j.Level;

import java.util.List;

public class ModuleLinkingBooks implements IModule {

    public static ConfigValue<Boolean> addLinkingBooks;
    public static ConfigValue<Boolean> showRecipeForLinkbooks;
    private boolean enabled;
    private boolean recipeEnabled;

    public ModuleLinkingBooks() {
        addLinkingBooks = new ConfigValue<Boolean>("addLinkingBooks", "config.nei.mystcraft.key.addLinkingBooks", Side.CLIENT, Boolean.TRUE, "Should unlinked linking books be added to NEI?");
        showRecipeForLinkbooks = new ConfigValue<Boolean>("showRecipeForLinkbooks", "config.nei.mystcraft.key.showRecipeForLinkbooks", Side.CLIENT, Boolean.TRUE, "Should the recipe for linking books be added to NEI?");
    }

    @Override
    public void enable() {
        if (addLinkingBooks.getValue()) {
            Objects.log.log(Level.DEBUG, "Adding linking books to NEI view");

            for (ItemStack panel : Integrator.getAllLinkpanels()) {
                ItemStack book = new ItemStack(MystObjs.linkbook_unlinked.getItem());

                book.stackTagCompound = (NBTTagCompound) panel.stackTagCompound.copy();

                API.addItemListEntry(book);
            }

            enabled = true;
        }

        if (!recipeEnabled) {
            Objects.log.log(Level.DEBUG, "Adding fake linkbook recipe");

            ICraftingResultHandler handler = new ICraftingResultHandler() {

                @Override
                public ItemStack getOutput(ISpACoreRecipe recipe, List<ItemStack> input) {
                    ItemStack result = recipe.getOutput();

                    for (ItemStack stack : input) {
                        if (stack != null && stack.getItem() == MystObjs.page.getItem()) {
                            if (stack.stackTagCompound != null) {
                                result.stackTagCompound = stack.stackTagCompound;
                                break;
                            }
                        }
                    }

                    return result;
                }

                @Override
                public String getOwningModName() {
                    return "Mystcraft";
                }

                @Override
                public String getOwningModId() {
                    return "Mystcraft";
                }

                @Override
                public boolean isValidRecipeInput(ItemStack input) {
                    if (input != null && input.getItem() == MystObjs.page.getItem()) {
                        if (input.stackTagCompound == null) {
                            return false;
                        }

                        NBTTagCompound tag = input.stackTagCompound;
                        return tag.hasKey("linkpanel");

                    }
                    return true;
                }

                @Override
                public String getNEIOverlayText() {
                    return StatCollector.translateToLocal("nei.mystcraft.linkbook.activate");
                }
            };

            List<ItemStack> linkPanels = Integrator.getAllLinkpanels();

            ItemStack[] stacks = new ItemStack[linkPanels.size()];
            stacks = linkPanels.toArray(stacks);

            FakeShapelessSpACoreRecipe recipe = new FakeShapelessSpACoreRecipe(handler, new ItemStack(MystObjs.linkbook_unlinked.getItem()), stacks, new ItemStack(Items.leather)) {

                @Override
                public boolean isEnabled() {
                    return showRecipeForLinkbooks.getValue();
                }

            };

            CraftingHelper.fakeRecipes.add(recipe);

            recipeEnabled = true;
        }
    }

    @Override
    public void disable() {
        if (enabled) {
            Objects.log.log(Level.DEBUG, "Removing linking books from NEI view");

            ItemInfo.itemOverrides.removeAll(MystObjs.linkbook_unlinked.getItem());

            enabled = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public ConfigValue<?>[] getConfigEntries() {
        return new ConfigValue<?>[] { addLinkingBooks, showRecipeForLinkbooks };
    }

}
