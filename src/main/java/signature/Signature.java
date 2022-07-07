package signature;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import signature.command.SignStackCommand;

import com.mojang.brigadier.CommandDispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Signature implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("signature");
	CommandDispatcher<ServerCommandSource> dispatcher;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			SignStackCommand.register(dispatcher);
		});
		LOGGER.info("Signature Loaded!");
	}
}