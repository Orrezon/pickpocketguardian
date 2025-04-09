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

import java.util.Locale;

@Slf4j
@PluginDescriptor(
	name = "Pickpocket Guardian"
)
public class PickpocketGuardianPlugin extends Plugin
{
	private static final String OPTION_CHECK_FOR_SNAKES = "Check for Snakes";
	private static final String OPTION_OPEN = "Open";
	private static final String OPTION_PASS = "Pass";
	private static final String OPTION_PICKPOCKET = "Pickpocket";
	private static final String OPTION_SEARCH = "Search";

	private static final String OBJECT_URN = "Urn";
	private static final String OBJECT_SARCOPHAGUS = "Sarcophagus";
	private static final int OBJECT_SARCOPHAGUS_ID = 26626;
	private static final String OBJECT_SPEARTRAP = "Speartrap";

	private static final int MAX_DAMAGE_URN = 4;
	private static final int MAX_DAMAGE_SPEARTRAP = 4;

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
		String optionName = menuOptionClicked.getMenuEntry().getOption();
		String targetName = menuOptionClicked.getMenuTarget().toString();
		int targetId = menuOptionClicked.getId();

		// Remove the text color of the object name.
		targetName = targetName.substring(targetName.lastIndexOf(">") + 1);

		// Prevent pickpocket action when at or below specified hitpoint threshold.
		if (currentHealth <= config.getHitpointsThreshold() && optionName.equals(this.OPTION_PICKPOCKET)) {
			menuOptionClicked.consume();
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Current health at or below the set threshold! Eat some food.", "Pickpocket Safe Guard");
		}

		// Prevent opening urns or passing speartraps in Pyramid Plunder if it will kill player.
		if (config.getSafeUrnOpenings() && (
				(currentHealth <= this.MAX_DAMAGE_URN && targetName.equals(this.OBJECT_URN) && (optionName.equals(this.OPTION_SEARCH) || optionName.equals(this.OPTION_CHECK_FOR_SNAKES))) ||
				(currentHealth <= this.MAX_DAMAGE_SPEARTRAP && targetName.equals(this.OBJECT_SPEARTRAP) && optionName.equals(this.OPTION_PASS)))) {
			menuOptionClicked.consume();
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Interacting with the " + targetName.toLowerCase() + " may kill you! Eat some food.", "Pickpocket Safe Guard");
		}

		// Prevent opening sarcophagus if setting is on.
		if (config.getPreventSarcophagus() && targetName.contains(this.OBJECT_SARCOPHAGUS) &&
				optionName.equals(this.OPTION_OPEN) &&
				targetId == OBJECT_SARCOPHAGUS_ID) {
			menuOptionClicked.consume();
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Pickpocket Guardian prevented opening the sarcophagus.", "Pickpocket Safe Guard");
		}
	}

	@Provides
	PickpocketGuardianConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PickpocketGuardianConfig.class);
	}
}
