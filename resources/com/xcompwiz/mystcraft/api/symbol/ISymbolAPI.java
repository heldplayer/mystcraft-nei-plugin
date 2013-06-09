package com.xcompwiz.mystcraft.api.symbol;

import com.xcompwiz.mystcraft.api.symbol.words.DrawableWord;

public interface ISymbolAPI {

	/**
	 * Use this to turn off symbols from the core mod
	 * This must be done before Mystcraft's init phase to have any effect
	 * @param identifier The identifier of the symbol to prevent from being registered
	 */
	void blacklistIdentifier(String identifier);

	/**
	 * Registers a logic provider "Symbol" to the system
	 * The symbol should provide logic elements or push modifier values to the IAgeController passed to it
	 * Note: Don't forget to create grammar rules for your symbols! See {@link IGrammarAPI}
	 * @param symbol The IAgeSymbol to register
	 */
	void registerSymbol(IAgeSymbol symbol);

	/**
	 * Registers a logic provider "Symbol" to the system
	 * The symbol should provide logic elements or push modifier values to the IAgeController passed to it
	 * Note: Don't forget to create grammar rules for your symbols! See {@link IGrammarAPI}
	 * @param symbol The IAgeSymbol to register
	 * @param hasConfigOption Whether or not a config entry will be created for the symbol
	 */
	void registerSymbol(IAgeSymbol symbol, boolean hasConfigOption);

	/**
	 * Binds a DrawableWord to the provided name key if no other word is already bound to that name
	 * Use this to make your custom words render correctly on your symbols
	 */
	void registerWord(String name, DrawableWord word);

	/**
	 * Constructs a DrawableWord from the provided component array and binds it to the provided name key if no other word is already bound to that name
	 * Use this to make your custom words render correctly on your symbols
	 */
	void registerWord(String name, Integer[] components);
}
