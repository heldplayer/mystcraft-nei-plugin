package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemInfo;
import java.util.List;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import me.heldplayer.plugins.nei.mystcraft.client.Integrator;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.specialattack.forge.core.crafting.CraftingHelper;
import net.specialattack.forge.core.crafting.FakeShapelessSpACoreRecipe;
import net.specialattack.forge.core.crafting.ICraftingResultHandler;
import net.specialattack.forge.core.crafting.ISpACoreRecipe;
import org.apache.logging.log4j.Level;

public class ModuleLinkingBooks implements IModule {

    private boolean enabled;
    private boolean recipeEnabled;

    @Override
    public void enable() {
        if (PluginNEIMystcraft.config.addLinkingBooks) {
            Objects.log.log(Level.DEBUG, "Adding linking books to NEI view");

            for (ItemStack panel : Integrator.getAllLinkpanels()) {
                ItemStack book = new ItemStack(MystObjs.linkingBookUnlinked);

                book.stackTagCompound = (NBTTagCompound) panel.stackTagCompound.copy();

                API.addItemListEntry(book);
            }

            this.enabled = true;
        }

        if (!this.recipeEnabled) {
            Objects.log.log(Level.DEBUG, "Adding fake linkbook recipe");

            ICraftingResultHandler handler = new ICraftingResultHandler() {

                @Override
                public ItemStack getOutput(ISpACoreRecipe recipe, List<ItemStack> input) {
                    ItemStack result = recipe.getOutput();

                    for (ItemStack stack : input) {
                        if (stack != null && stack.getItem() == MystObjs.page) {
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
                    if (input != null && input.getItem() == MystObjs.page) {
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

            FakeShapelessSpACoreRecipe recipe = new FakeShapelessSpACoreRecipe(handler, new ItemStack(MystObjs.linkingBookUnlinked), stacks, new ItemStack(Items.leather)) {

                @Override
                public boolean isEnabled() {
                    return PluginNEIMystcraft.config.showRecipeForLinkbooks;
                }

            };

            CraftingHelper.fakeRecipes.add(recipe);

            this.recipeEnabled = true;
        }
    }

    @Override
    public void disable() {
        if (this.enabled) {
            Objects.log.log(Level.DEBUG, "Removing linking books from NEI view");

            ItemInfo.itemOverrides.removeAll(MystObjs.linkingBookUnlinked);

            this.enabled = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
