package com.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("PickpocketGuardianConfig")
public interface PickpocketGuardianConfig extends Config
{
	@ConfigSection(
			name = "Pickpocketing",
			description = "Configuration settings related to pickpocketing.",
			position = 0
	)
	String pickpocketingSection = "pickpocketing";

	@ConfigItem(
			keyName = "hpThreshold",
			name = "HP Threshold",
			description = "The hitpoint threshold to disable the \"Pickpocket\" action at.",
			section = "pickpocketing",
			position = 0
	)
	default int getHitpointsThreshold()
	{
		return 3;
	}

	@ConfigSection(
			name = "Pyramid Plunder",
			description = "Configuration settings related to the Pyramid Plunder minigame.",
			position = 1
	)
	String pyramidPlunderSection = "pyramidPlunder";

	@ConfigItem(
			keyName = "safeUrnOpenings",
			name = "Safe Urn Openings & Speartraps",
			description = "Prevents opening urns and passing speartraps in Pyramid Plunder when at or below 4 hitpoints.",
			section = "pyramidPlunder",
			position = 0
	)
	default boolean getSafeUrnOpenings()
	{
		return true;
	}

	@ConfigItem(
			keyName = "preventSarcophagus",
			name = "Prevent Sarcophagus Opening",
			description = "Prevents opening a sarcophagus in Pyramid Plunder.",
			section = "pyramidPlunder",
			position = 1
	)
	default boolean getPreventSarcophagus()
	{
		return false;
	}
}
