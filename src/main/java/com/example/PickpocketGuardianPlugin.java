package com.example;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Pickpocket Guardian"
)
public class PickpocketGuardianPlugin extends Plugin
{
	private static final String PICKPOCKET_OPTION = "Pickpocket";

	@Inject
	private Client client;

	@Inject
	private PickpocketGuardianConfig config;

	/**
	 * If the player tries to perform the "Pickpocket" option and their health is at or below the set threshold, consume the action - thus preventing it from resolving.
	 * @param menuOptionClicked The menu option details that was clicked. <see>MenuOptionClicked</see>.
	 */
	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked)
	{
		int currentHealth = client.getBoostedSkillLevel(Skill.HITPOINTS);

		if (currentHealth <= config.getHitpointsThreshold() && menuOptionClicked.getMenuEntry().getOption().equals(this.PICKPOCKET_OPTION)) {
			menuOptionClicked.consume();
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Current health at or below the set threshold! Eat some food.", "Pickpocket Safe Guard");
		}
	}

	@Provides
	PickpocketGuardianConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PickpocketGuardianConfig.class);
	}
}
