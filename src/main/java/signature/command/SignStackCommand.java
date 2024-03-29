package signature.command;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.server.command.CommandManager.literal;

import static net.minecraft.server.command.CommandManager.argument;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class SignStackCommand {

		public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
				dispatcher.register(
								literal("signstack").executes(ctx -> signstack(ctx.getSource(), null))
												.then(argument("description", greedyString())
																.executes(ctx -> signstack(ctx.getSource(),
																				getString(ctx, "description")))));
		}

		public static int signstack(ServerCommandSource serverCommandSource, String description)
						throws CommandSyntaxException {
				ServerPlayerEntity player = serverCommandSource.getPlayer();
				ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
				if (stack.isEmpty()) {
					serverCommandSource.sendError(Text.of("Please hold an item in your main hand."));
					return -1;
				}

				NbtCompound nbt = stack.getNbt();

				if(nbt!=null && !nbt.isEmpty() && nbt.contains("isSigned") && nbt.getBoolean("isSigned"))
				{
					serverCommandSource.sendError(Text.of("This item is already signed."));
					return -1;
				}

				String nbtString;

				if (description == null) {
						nbtString = String.format(
										"{isSigned:1b,display:{Lore:['[{\"text\":\"Signed by \",\"color\":\"blue\"},{\"text\":\"%s\",\"underlined\":true}]']}}",
										player.getEntityName());
				} else {
						description = description.replaceAll("\"", "\\\\\"");
						description = description.replaceAll("\"", "\\\\\"");
						description = description.replaceAll("'", "\\\\'");

						nbtString = String.format(
										"{isSigned:1b,display:{Lore:['[{\"text\":\"%1$s\",\"italic\":false,\"color\":\"gold\"},{\"text\":\"\"}]','[{\"text\":\"\"}]','[{\"text\":\"Signed by \",\"italic\":true,\"color\":\"blue\"},{\"text\":\"%2$s\",\"underlined\":true}]']}}",
										description, player.getEntityName());

				}

				nbt = NbtHelper.fromNbtProviderString(nbtString);

				stack.setNbt(nbt);
				serverCommandSource.sendFeedback(
								Text.of("Item stack signed!"), false);
				return Command.SINGLE_SUCCESS;
		}
}