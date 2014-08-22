package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.api.API;
import codechicken.nei.api.ItemFilter;
import cpw.mods.fml.relauncher.Side;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.item.ItemStack;
import net.specialattack.forge.core.config.ConfigValue;
import org.apache.logging.log4j.Level;

import java.util.HashSet;

public class ModuleItemSubsets implements IModule {

    public static ConfigValue<Boolean> addItemRanges;
    private boolean enabled;

    public ModuleItemSubsets() {
        addItemRanges = new ConfigValue<Boolean>("addItemRanges", "config.nei.mystcraft.key.addItemRanges", Side.CLIENT, Boolean.TRUE, "Should item ranges be added to NEI?");
        addItemRanges.setRequiresMcRestart(true);
    }

    @Override
    public void enable() {
        if (!enabled) {
            Objects.log.log(Level.DEBUG, "Adding item ranges to NEI");

            HashSet<ItemStack> blocks = new HashSet<ItemStack>();
            blocks.add(new ItemStack(MystObjs.portal));
            blocks.add(new ItemStack(MystObjs.crystal));
            blocks.add(new ItemStack(MystObjs.crystal_receptacle));
            blocks.add(new ItemStack(MystObjs.decay, 1, 0));
            blocks.add(new ItemStack(MystObjs.decay, 1, 1));
            blocks.add(new ItemStack(MystObjs.decay, 1, 3));
            blocks.add(new ItemStack(MystObjs.decay, 1, 4));
            blocks.add(new ItemStack(MystObjs.decay, 1, 6));
            blocks.add(new ItemStack(MystObjs.bookstand));
            blocks.add(new ItemStack(MystObjs.book_lectern));
            blocks.add(new ItemStack(MystObjs.writing_desk_block));
            blocks.add(new ItemStack(MystObjs.bookbinder));
            blocks.add(new ItemStack(MystObjs.inkmixer));
            blocks.add(new ItemStack(MystObjs.star_fissure));
            blocks.add(new ItemStack(MystObjs.link_modifer));
            blocks.add(new ItemStack(MystObjs.black_ink.getBlock()));

            API.addSubset("Mystcraft.Blocks", blocks);

            HashSet<ItemStack> items = new HashSet<ItemStack>();

            items.add(new ItemStack(MystObjs.writing_desk, 1, 0));
            items.add(new ItemStack(MystObjs.writing_desk, 1, 1));
            items.add(new ItemStack(MystObjs.inkvial));

            if (ModulePages.addSymbolPages.getValue() || ModulePages.addLinkPanels.getValue()) {
                if (ModulePages.addSymbolPages.getValue()) {
                    API.addSubset("Mystcraft.Items.Symbols", new ItemFilter() {

                        @Override
                        public boolean matches(ItemStack itemStack) {
                            if (itemStack.getItem() != MystObjs.page || !itemStack.hasTagCompound()) {
                                return false;
                            }
                            return itemStack.getTagCompound().hasKey("symbol");
                        }
                    });
                }
                if (ModulePages.addLinkPanels.getValue()) {
                    API.addSubset("Mystcraft.Items.Link Panels", new ItemFilter() {

                        @Override
                        public boolean matches(ItemStack itemStack) {
                            if (itemStack.getItem() != MystObjs.page || !itemStack.hasTagCompound()) {
                                return false;
                            }
                            return itemStack.getTagCompound().hasKey("linkpanel");
                        }
                    });
                }
            } else {
                items.add(new ItemStack(MystObjs.page));
            }

            if (ModuleCreativeNotebooks.addCreativeNotebooks.getValue()) {
                API.addSubset("Mystcraft.Items.Notebooks", new ItemFilter() {

                    @Override
                    public boolean matches(ItemStack itemStack) {
                        return itemStack.getItem() == MystObjs.notebook;
                    }
                });
            } else {
                items.add(new ItemStack(MystObjs.notebook));
            }

            if (ModuleLinkingBooks.addLinkingBooks.getValue()) {
                API.addSubset("Mystcraft.Items.Linking Books", new ItemFilter() {

                    @Override
                    public boolean matches(ItemStack itemStack) {
                        return itemStack.getItem() == MystObjs.linkbook || itemStack.getItem() == MystObjs.linkbook_unlinked;
                    }
                });
            } else {
                items.add(new ItemStack(MystObjs.linkbook));
                items.add(new ItemStack(MystObjs.linkbook_unlinked));
            }

            if (ModuleDescriptiveBooks.addAgeList.getValue()) {
                API.addSubset("Mystcraft.Items.Descriptive Books", new ItemFilter() {

                    @Override
                    public boolean matches(ItemStack itemStack) {
                        return itemStack.getItem() == MystObjs.descriptive_book;
                    }
                });
            } else {
                items.add(new ItemStack(MystObjs.descriptive_book));
            }

            API.addSubset("Mystcraft.Items", items);

            enabled = true;
        }
    }

    @Override
    public void disable() {
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public ConfigValue<?>[] getConfigEntries() {
        return new ConfigValue<?>[] { addItemRanges };
    }

}
