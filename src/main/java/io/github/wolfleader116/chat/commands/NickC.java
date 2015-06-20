package io.github.wolfleader116.chat.commands;

import java.util.logging.Logger;

import io.github.wolfleader116.chat.ChatPlugin;
import io.github.wolfleader116.chat.Config;

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
					} else {
						String nick = args[0];
						nick = ChatColor.stripColor(nick);
						player.setDisplayName(nick);
						c.getConfig().set("nick." + player.getUniqueId().toString(), nick);
						c.save();
						log.info(player.getName() + "'s nickname is now " + nick + "!");
					}
				}
			} else if (sender instanceof Player) {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "You need to add a nickname or off!");
				} else if (args.length == 1) {
					String player = ((Player) sender).getName();
					if (sender.hasPermission("chat.nick")) {
						if (args[0].equalsIgnoreCase("off")) {
							c.getConfig().set("nick." + Bukkit.getPlayer(player).getUniqueId().toString(), null);
							c.save();
							sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "Your nickname is now reset!");
						} else {
							String nick = args[0];
							Player p = (Player) sender;
							nick = ChatColor.stripColor(nick);
							p.setDisplayName(nick);
							c.getConfig().set("nick." + Bukkit.getPlayer(player).getUniqueId().toString(), nick);
							c.save();
							sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "Your nickname is now " + ChatColor.RESET + nick + ChatColor.GREEN + "!");
						}
					} else {
						sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "You do not have permission to do this!");
					}
				} else if (args.length >= 2) {
					if (sender.hasPermission("chat.nick.other")) {
						Player player = Bukkit.getPlayer(args[1]);
						if (player == null) {
							sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "The specified player is not online!");
						} else if (args[0].equalsIgnoreCase("off")) {
							c.getConfig().set("nick." + player.getUniqueId().toString(), null);
							c.save();
							sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.RESET + player.getName() + ChatColor.GREEN + "'s nickname is now reset!");
						} else {
							String nick = args[0];
							Player p = Bukkit.getPlayer(args[1]);
							nick = ChatColor.stripColor(nick);
							p.setDisplayName(nick);
							c.getConfig().set("nick." + player.getUniqueId().toString(), nick);
							c.save();
							sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.RESET + player.getName() + ChatColor.GREEN + "'s nickname is now " + ChatColor.RESET + nick + ChatColor.GREEN + "!");
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
