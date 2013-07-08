
package com.xcompwiz.mystcraft.api.symbol;

/**
 * Provides methods for interacting with the grammar rules for the symbol
 * generation system
 * 
 * @author xcompwiz
 */
public interface IGrammarAPI {

    public static final String BIOME = "Biome";
    public static final String BIOMES = "Biomes";
    public static final String BIOMECONTROLLER = "BiomeController";
    public static final String LIGHTING = "Lighting";
    public static final String WEATHER = "Weather";
    public static final String TERRAIN = "Terrain";

    public static final String VISUAL_EFFECT = "Visual";
    public static final String TERRAINMOD = "TerrainMod";
    public static final String POPULATOR = "Populator";
    public static final String EFFECT = "Effect";

    public static final String SUN = "Sun";
    public static final String MOON = "Moon";
    public static final String STARFIELD = "Starfield";
    public static final String DOODAD = "Doodad";

    public static final String SUNSET_UNCOMMON = "SunsetUncommon";
    public static final String SUNSET = "Sunset";

    public static final String ANGLE = "Angle";
    public static final String PERIOD = "Period";
    public static final String PHASE = "Phase";
    public static final String COLOR = "Color";
    public static final String GRADIENT = "Gradient";

    public static final String BLOCK_TERRAIN = "BlockTerrain";
    public static final String BLOCK_SOLID = "BlockSolid";
    public static final String BLOCK_STRUCTURE = "BlockStructure";
    public static final String BLOCK_CRYSTAL = "BlockCrystal";
    public static final String BLOCK_SEA = "BlockSea";
    public static final String BLOCK_FLUID = "BlockFluid";
    public static final String BLOCK_ANY = "BlockAny";

    /**
     * Registers a rule for the grammar system
     * Example:
     * {@code registerGrammarRule(1.0F, IGrammarAPI.TERRAINMOD, IGrammarAPI.BLOCK_STRUCTURE, "TerModLattice")}
     * 
     * @param parent
     *        The token to expand
     * @param weight
     *        The weight value applied to the rule (higher -> more common).
     *        typically, 1 is common
     * @param args
     *        The tokens to expand to
     */
    void registerGrammarRule(String parent, float weight, String... args);

}
