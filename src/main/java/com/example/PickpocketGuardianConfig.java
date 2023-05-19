package com.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("PickpocketGuardianConfig")
public interface PickpocketGuardianConfig extends Config
{
	@ConfigItem(
			keyName = "hpThreshold",
			name = "HP Threshold",
			description = "The hitpoint threshold to disable the \"Pickpocket\" action at."
	)
	default int getHitpointsThreshold()
	{
		return 3;
	}
}
