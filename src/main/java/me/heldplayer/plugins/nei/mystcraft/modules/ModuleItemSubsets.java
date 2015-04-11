package me.heldplayer.plugins.nei.mystcraft.modules;

import codechicken.nei.SubsetWidget;
import codechicken.nei.api.API;
import codechicken.nei.api.ItemFilter;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import java.util.HashSet;
import java.util.LinkedList;
import me.heldplayer.plugins.nei.mystcraft.Objects;
import me.heldplayer.plugins.nei.mystcraft.wrap.MystObjs;
import net.minecraft.item.ItemStack;
import net.specialattack.forge.core.config.ConfigValue;
import org.apache.logging.log4j.Level;

public class ModuleItemSubsets implements IModule {

    public static ConfigValue<Boolean> addItemRanges;
    private boolean enabled;

    public ModuleItemSubsets() {
        addItemRanges = new ConfigValue<Boolean>("addItemRanges", "myst-nei:config.general.addItemRanges", Side.CLIENT, Boolean.TRUE);
    }

    @Override
    public void enable() {
        if (addItemRanges.getValue()) {
            Objects.log.log(Level.DEBUG, "Adding item ranges to NEI");

            HashSet<ItemStack> blocks = new HashSet<ItemStack>();
            blocks.add(new ItemStack(MystObjs.portal.getBlock()));
            blocks.add(new ItemStack(MystObjs.crystal.getBlock()));
            blocks.add(new ItemStack(MystObjs.crystal_receptacle.getBlock()));
            blocks.add(new ItemStack(MystObjs.decay.getBlock(), 1, 0));
            blocks.add(new ItemStack(MystObjs.decay.getBlock(), 1, 1));
            blocks.add(new ItemStack(MystObjs.decay.getBlock(), 1, 3));
            blocks.add(new ItemStack(MystObjs.decay.getBlock(), 1, 4));
            blocks.add(new ItemStack(MystObjs.decay.getBlock(), 1, 6));
            blocks.add(new ItemStack(MystObjs.bookstand.getBlock()));
            blocks.add(new ItemStack(MystObjs.book_lectern.getBlock()));
            blocks.add(new ItemStack(MystObjs.writing_desk_block.getBlock()));
            blocks.add(new ItemStack(MystObjs.bookbinder.getBlock()));
            blocks.add(new ItemStack(MystObjs.inkmixer.getBlock()));
            blocks.add(new ItemStack(MystObjs.star_fissure.getBlock()));
            blocks.add(new ItemStack(MystObjs.link_modifer.getBlock()));
            blocks.add(new ItemStack(MystObjs.black_ink.getBlock()));

            API.addSubset("Mystcraft.Blocks", blocks);

            LinkedList<Runnable> delayedTasks = new LinkedList<Runnable>();

            final HashSet<ItemStack> items = new HashSet<ItemStack>();

            items.add(new ItemStack(MystObjs.writing_desk.getItem(), 1, 0));
            items.add(new ItemStack(MystObjs.writing_desk.getItem(), 1, 1));
            items.add(new ItemStack(MystObjs.inkvial.getItem()));

            API.addSubset("Mystcraft.Items", items);

            if (ModulePages.addSymbolPages.getValue() || ModulePages.addLinkPanels.getValue()) {
                if (ModulePages.addSymbolPages.getValue()) {
                    delayedTasks.add(new Runnable() {
                        @Override
                        public void run() {
                            API.addSubset("Mystcraft.Items.Symbols", new ItemFilter() {

                                @Override
                                public boolean matches(ItemStack itemStack) {
                                    return !(itemStack.getItem() != MystObjs.page.getItem() || !itemStack.hasTagCompound()) && itemStack.getTagCompound().hasKey("symbol");
                                }
                            });
                        }
                    });
                }
                if (ModulePages.addLinkPanels.getValue()) {
                    delayedTasks.add(new Runnable() {
                        @Override
                        public void run() {
                            API.addSubset("Mystcraft.Items.Link Panels", new ItemFilter() {

                                @Override
                                public boolean matches(ItemStack itemStack) {
                                    return !(itemStack.getItem() != MystObjs.page.getItem() || !itemStack.hasTagCompound()) && itemStack.getTagCompound().hasKey("linkpanel");
                                }
                            });
                        }
                    });
                }
            } else {
                items.add(new ItemStack(MystObjs.page.getItem()));
            }

            if (ModuleCreativeNotebooks.addCreativeNotebooks.getValue()) {
                delayedTasks.add(new Runnable() {
                    @Override
                    public void run() {
                        API.addSubset("Mystcraft.Items.Notebooks", new ItemFilter() {

                            @Override
                            public boolean matches(ItemStack itemStack) {
                                return itemStack.getItem() == MystObjs.notebook.getItem();
                            }
                        });
                    }
                });
            } else {
                items.add(new ItemStack(MystObjs.notebook.getItem()));
            }

            if (ModuleLinkingBooks.addLinkingBooks.getValue()) {
                delayedTasks.add(new Runnable() {
                    @Override
                    public void run() {
                        API.addSubset("Mystcraft.Items.Linking Books", new ItemFilter() {

                            @Override
                            public boolean matches(ItemStack itemStack) {
                                return itemStack.getItem() == MystObjs.linkbook.getItem() || itemStack.getItem() == MystObjs.linkbook_unlinked.getItem();
                            }
                        });
                    }
                });
            } else {
                items.add(new ItemStack(MystObjs.linkbook.getItem()));
                items.add(new ItemStack(MystObjs.linkbook_unlinked.getItem()));
            }

            if (ModuleDescriptiveBooks.addAgeList.getValue()) {
                delayedTasks.add(new Runnable() {
                    @Override
                    public void run() {
                        API.addSubset("Mystcraft.Items.Descriptive Books", new ItemFilter() {

                            @Override
                            public boolean matches(ItemStack itemStack) {
                                return itemStack.getItem() == MystObjs.descriptive_book.getItem();
                            }
                        });
                    }
                });
            } else {
                items.add(new ItemStack(MystObjs.descriptive_book.getItem()));
            }

            delayedTasks.addFirst(new Runnable() {
                @Override
                public void run() {
                    API.addSubset("Mystcraft.Items", items);
                }
            });

            for (Runnable task : delayedTasks) {
                task.run();
            }

            delayedTasks.clear();

            enabled = true;
        }
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    @Override
    public void disable() {
        if (enabled) {
            SubsetWidget.SubsetTag root = ReflectionHelper.getPrivateValue(SubsetWidget.class, null, "root");
            synchronized (root) {
                root.children.remove("mystcraft");
            }

            enabled = false;
        }
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
