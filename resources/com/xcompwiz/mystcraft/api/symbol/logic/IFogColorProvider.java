package com.xcompwiz.mystcraft.api.symbol.logic;

import com.xcompwiz.mystcraft.api.internals.Color;

public interface IFogColorProvider {
	public abstract Color getFogColor(float celestial_angle, float partialticks);
}