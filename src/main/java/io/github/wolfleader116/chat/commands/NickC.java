package io.github.wolfleader116.chat.commands;

import java.util.logging.Logger;

import io.github.wolfleader116.chat.ChatPlugin;
import io.github.wolfleader116.chat.Config;
import io.github.wolfleader116.wolfapi.Errors;
import io.github.wolfleader116.wolfapi.WolfAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickC implements CommandExecutor {
	
	private static final Logger log = Logger.getLogger("Minecraft");

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Config c = new Config("playerdata", ChatPlugin.plugin);
		if (cmd.getName().equalsIgnoreCase("nick")) {
			if (!(sender instanceof Player)) {
				if (args.length == 0) {
					log.info("You need to add a nickname or off and a player name!");
				} else if (args.length == 1) {
					log.info("You need to add a player name!");
				} else if (args.length >= 2) {
					Player player = Bukkit.getPlayer(args[1]);
					if (player == null) {
						log.info("The specified player is not online!");
					} else if (args[0].equalsIgnoreCase("off")) {
						c.getConfig().set("nick." + player.getUniqueId().toString(), null);
						c.save();
						log.info(player.getName() + "'s nickname is now reset!");
						WolfAPI.message("Your nickname is now reset!", player, "Chat");
					} else {
						String nick = args[0];
						nick = ChatColor.stripColor(nick);
						player.setDisplayName(nick);
						c.getConfig().set("nick." + player.getUniqueId().toString(), nick);
						c.save();
						log.info(player.getName() + "'s nickname is now " + nick + "!");
						WolfAPI.message("Your nickname is now " + ChatColor.RESET + nick + ChatColor.GREEN + "!", player, "Chat");
					}
				}
			} else if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length == 0) {
					WolfAPI.message("You need to add a nickname or off!", p, "Chat");
				} else if (args.length == 1) {
					String player = ((Player) sender).getName();
					if (sender.hasPermission("chat.nick")) {
						if (args[0].equalsIgnoreCase("off")) {
							c.getConfig().set("nick." + Bukkit.getPlayer(player).getUniqueId().toString(), null);
							c.save();
							WolfAPI.message("Your nickname is now reset!", p, "Chat");
						} else {
							String nick = args[0];
							nick = ChatColor.stripColor(nick);
							p.setDisplayName(nick);
							c.getConfig().set("nick." + Bukkit.getPlayer(player).getUniqueId().toString(), nick);
							c.save();
							WolfAPI.message("Your nickname is now " + ChatColor.RESET + nick + ChatColor.GREEN + "!", p, "Chat");
						}
					} else {
						Errors.sendError(Errors.NO_PERMISSION, p, "Chat");
					}
				} else if (args.length >= 2) {
					if (sender.hasPermission("chat.nick.other")) {
						Player player = Bukkit.getPlayer(args[1]);
						if (player == null) {
							Errors.sendError(Errors.NOT_ONLINE, p, "Chat");
						} else if (args[0].equalsIgnoreCase("off")) {
							c.getConfig().set("nick." + player.getUniqueId().toString(), null);
							c.save();
							WolfAPI.message(ChatColor.RESET + player.getName() + ChatColor.GREEN + "'s nickname is now reset!", p, "Chat");
							WolfAPI.message("Your nickname is now reset!", player, "Chat");
						} else {
							String nick = args[0];
							nick = ChatColor.stripColor(nick);
							player.setDisplayName(nick);
							c.getConfig().set("nick." + player.getUniqueId().toString(), nick);
							c.save();
							WolfAPI.message(player.getName() + ChatColor.GREEN + "'s nickname is now " + ChatColor.RESET + nick + ChatColor.GREEN + "!", p, "Chat");
							WolfAPI.message("Your nickname is now " + ChatColor.RESET + nick + ChatColor.GREEN + "!", player, "Chat");
						}
					} else {
						sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "You do not have permission to do this!");
					}
				}
			}
		}
		return false;
	}
}
