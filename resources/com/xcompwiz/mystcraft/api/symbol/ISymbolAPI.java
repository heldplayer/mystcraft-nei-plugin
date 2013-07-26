
package com.xcompwiz.mystcraft.api.symbol;

import com.xcompwiz.mystcraft.api.symbol.words.DrawableWord;

/**
 * Provides functions for registering different aspects to the Symbol system
 * You can blacklist identifiers, register your own symbols, and create your own
 * Narayan words (See WordData for examples)
 * The implementation of this is provided by MystAPI
 * Do NOT implement this yourself!
 * 
 * @author xcompwiz
 */
public interface ISymbolAPI {

    /**
     * Use this to turn off symbols from the core mod
     * This must be done before Mystcraft's init phase to have any effect
     * 
     * @param identifier
     *        The identifier of the symbol to prevent from being registered
     */
    void blacklistIdentifier(String identifier);

    /**
     * Registers a logic provider "Symbol" to the system
     * The symbol should provide logic elements or push modifier values to the
     * IAgeController passed to it
     * If a symbol throws an exception during profiling then the symbol will not
     * be registered, the identifier will be blacklisted, and te registration
     * function will throw an exception
     * Note: Don't forget to create grammar rules for your symbols! See
     * {@link IGrammarAPI}
     * 
     * @param symbol
     *        The IAgeSymbol to register
     * @return Success
     */
    boolean registerSymbol(IAgeSymbol symbol);

    /**
     * Registers a logic provider "Symbol" to the system
     * The symbol should provide logic elements or push modifier values to the
     * IAgeController passed to it
     * If a symbol throws an exception during profiling then the symbol will not
     * be registered, the identifier will be blacklisted, and te registration
     * function will throw an exception
     * Note: Don't forget to create grammar rules for your symbols! See
     * {@link IGrammarAPI}
     * 
     * @param symbol
     *        The IAgeSymbol to register
     * @param hasConfigOption
     *        Whether or not a config entry will be created for the symbol
     * @return Success
     */
    boolean registerSymbol(IAgeSymbol symbol, boolean hasConfigOption);

    /**
     * Binds a DrawableWord to the provided name key if no other word is already
     * bound to that name
     * Use this to make your custom words render correctly on your symbols
     */
    void registerWord(String name, DrawableWord word);

    /**
     * Constructs a DrawableWord from the provided component array and binds it
     * to the provided name key if no other word is already bound to that name
     * Use this to make your custom words render correctly on your symbols
     */
    void registerWord(String name, Integer[] components);
}
