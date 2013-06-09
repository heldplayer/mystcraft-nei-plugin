package com.xcompwiz.mystcraft.api;

import com.xcompwiz.mystcraft.api.instability.IInstabilityAPI;
import com.xcompwiz.mystcraft.api.instability.IInstabilityFactory;
import com.xcompwiz.mystcraft.api.linking.IAdditiveAPI;
import com.xcompwiz.mystcraft.api.linking.ILinkingAPI;
import com.xcompwiz.mystcraft.api.symbol.IGrammarAPI;
import com.xcompwiz.mystcraft.api.symbol.ISymbolAPI;
import com.xcompwiz.mystcraft.api.symbol.ISymbolFactory;

/**
 * Provides interface implementations for Mystcraft functionality for external use
 * These are not guaranteed to be set.  Be sure to check for nulls.
 * These are set during Mystcraft's pre-init phase
 * @author XCompWiz
 *
 */
public class MystAPI {
	public static ILinkingAPI				linking				= null;
	public static IInstabilityAPI			instability			= null;
	public static ISymbolAPI				symbol				= null;
	public static IGrammarAPI				grammar				= null;
	public static IAdditiveAPI				additive			= null;

	public static ISymbolFactory			symbolFact			= null;
	public static IInstabilityFactory		instabilityFact		= null;
}
