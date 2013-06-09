package com.xcompwiz.mystcraft.api.internals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.xcompwiz.mystcraft.api.symbol.IAgeController;
import com.xcompwiz.mystcraft.api.symbol.Modifier;

/**
 * For defining information for block modifiers
 * This handles the block modifier system.  Use this to get block modifiers for usage as well as to push block modifiers to the age.
 * If you create new types of block classes (usable categories) and you want the CFG to handle it remember to bind it to a grammar token
 * @author xcompwiz
 *
 */
public class BlockDescriptor {

	private static final HashMap<String, String> grammarBindings = new HashMap<String, String>();
	private static int blockDangling = 50;

	public final short id;
	public final byte metadata;
	private final IFormula formula;

	private final HashMap<String, Boolean> useable = new HashMap<String, Boolean>();

	public static final String TERRAIN   = "TERRAIN";
	public static final String SOLID     = "SOLID";
	public static final String STRUCTURE = "STRUCTURE";
	public static final String CRYSTAL   = "CRYSTAL";
	public static final String SEA       = "SEA";
	public static final String FLUID     = "FLUID";
	public static final String ANY       = "BLOCK";

	public BlockDescriptor(short blockId, byte metadata, IFormula instability_function) {
		this.id = blockId;
		this.metadata = metadata;
		this.formula = instability_function;
	}

	public BlockDescriptor(short blockId, byte metadata) {
		this(blockId, metadata, null);
	}

	public BlockDescriptor(short blockId) {
		this(blockId, (byte)0);
	}

	public void setUsable(String key, boolean flag) {
		this.useable.put(key, flag);
	}

	public boolean isUsable(String key) {
		if (key == ANY) return true;
		if (!this.useable.containsKey(key)) return false;
		return this.useable.get(key);
	}

	public int getInstability(int blocksPerChunk) {
		if (blocksPerChunk < 0) throw new RuntimeException("Cannot generate negative blocks per chunk!");
		if (formula == null) return 0;
		return (int)formula.calc(blocksPerChunk);
	}

	public static BlockDescriptor popBlockOfType(IAgeController controller, String type) {
		Modifier modifier = controller.popModifier("blocklist");
		List<BlockDescriptor> list = modifier.asList();
		if (list == null) return null;
		controller.setModifier("blocklist", modifier);
		for (int i = 0; i < list.size(); ++i) {
			BlockDescriptor block = list.get(i);
			if (block.isUsable(type)) {
				list.remove(i);
				modifier.dangling -= blockDangling;
				return block;
			}
		}
		return null;
	}
	public static void pushBlock(IAgeController controller, BlockDescriptor block) {
		Modifier modifier = controller.popModifier("blocklist");
		List<BlockDescriptor> list = modifier.asList();
		if (list == null) {
			list = new ArrayList<BlockDescriptor>();
			modifier = new Modifier(list, 0);
		}
		list.add(0, block);
		modifier.dangling += blockDangling;
		controller.setModifier("blocklist", modifier);
	}

	public static void bindGrammarToken(String typekey, String grammartoken) {
		grammarBindings.put(typekey, grammartoken);
	}

	public static Set<String> getTypes() {
		return grammarBindings.keySet();
	}

	public static String getGrammarBinding(String key) {
		return grammarBindings.get(key);
	}
}
