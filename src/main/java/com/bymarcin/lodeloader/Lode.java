package com.bymarcin.lodeloader;

import java.util.HashMap;

public class Lode {
	String unlocalized_name;
	int mineralChance;
	float failChance;
	String[] ores;
	float[] chances;
	HashMap<String, String> localized_names;

	public Lode() {
	}

	public Lode(String unlocalized_name, int mineralChance, float failChance, String[] ores, float[] chances, HashMap<String, String> localized_names) {
		this.unlocalized_name = unlocalized_name;
		this.failChance = failChance;
		this.ores = ores;
		this.chances = chances;
		this.localized_names = localized_names;
		this.mineralChance = mineralChance;
	}

	public boolean isValid() {
		return (unlocalized_name != null && !unlocalized_name.isEmpty()) &&
				mineralChance > 0 && failChance >= 0 &&
				(ores != null && chances != null && ores.length == chances.length) &&
				localized_names != null;
	}
}
