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

public class GlobalC implements CommandExecutor {

	private static final Logger log = Logger.getLogger("Minecraft");

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("global")) {
			if (!(sender instanceof Player)) {
				log.info("Only a player can run this command!");
			} else {
				Player player = (Player) sender;
				if (ChatPlugin.plugin.getConfig().getBoolean("Global")) {
					Config c = new Config("playerdata", ChatPlugin.plugin);
					String displayname = c.getConfig().getString("nick." + player.getUniqueId().toString()) + ChatColor.RESET;
					String prefix = ChatPlugin.chat.getPlayerPrefix(player) + ChatColor.RESET;
					String suffix = ChatPlugin.chat.getPlayerSuffix(player);
					String arrow = ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "> " + ChatColor.RESET;
					String splayer = player.getName() + ChatColor.RESET;
					prefix = prefix.replaceAll("&", "§");
					if (args.length == 0) {
						c.getConfig().set("channel." + player.getUniqueId().toString(), 0);
						c.save();
						sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "Now in global chat mode!");
					} else {
						String message = "";
						for(int i = 0; i < args.length; i++) {
							String arg = args[i] + " ";
							message = message + arg;
						}
						if (sender.hasPermission("chat.color")) {
							message = message.replaceAll("&", "§");
						}
						if (c.getConfig().getString("nick." + player.getUniqueId().toString()) == null) {
							String format = ChatPlugin.plugin.getConfig().getString("GlobalFormat");
							format = format.replaceAll("%ARROW%", arrow);
							format = format.replaceAll("%PREFIX%", prefix);
							format = format.replaceAll("%SUFFIX%", suffix);
							format = format.replaceAll("%MESSAGE%", message);
							format = format.replaceAll("&", "§");
							format = format.replaceAll("%PLAYER%", splayer);
							Bukkit.getServer().broadcastMessage(format);
						} else {
							String format = ChatPlugin.plugin.getConfig().getString("GlobalFormat");
							format = format.replaceAll("%ARROW%", arrow);
							format = format.replaceAll("%PREFIX%", prefix);
							format = format.replaceAll("%SUFFIX%", suffix);
							format = format.replaceAll("%MESSAGE%", message);
							format = format.replaceAll("&", "§");
							format = format.replaceAll("%PLAYER%", displayname);
							Bukkit.getServer().broadcastMessage(format);
						}
					}
				} else {
					if (sender.hasPermission("chat.global")) {
						String splayer = player.getName() + ChatColor.RESET;
						Config c = new Config("playerdata", ChatPlugin.plugin);
						String displayname = c.getConfig().getString("nick." + player.getUniqueId().toString()) + ChatColor.RESET;
						String prefix = ChatPlugin.chat.getPlayerPrefix(player) + ChatColor.RESET;
						String suffix = ChatPlugin.chat.getPlayerSuffix(player);
						String arrow = ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "> " + ChatColor.RESET;
						prefix = prefix.replaceAll("&", "§");
						if (!(ChatPlugin.chat.playerInGroup("world", player, "helper") || ChatPlugin.chat.playerInGroup("world", player, "moderator") || ChatPlugin.chat.playerInGroup("world", player, "admin") || ChatPlugin.chat.playerInGroup("world", player, "headadmin") || ChatPlugin.chat.playerInGroup("world", player, "coowner") || ChatPlugin.chat.playerInGroup("world", player, "owner"))) {
							if (ChatPlugin.economy.getBalance(player) >= ChatPlugin.plugin.getConfig().getInt("Price")) {
								if (args.length == 0) {
									sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "You need to add a message to send!");
								} else {
									String message = "";
									for(int i = 0; i < args.length; i++) {
										String arg = args[i] + " ";
										message = message + arg;
									}
									if (sender.hasPermission("chat.color")) {
										message = message.replaceAll("&", "§");
									}
									if (c.getConfig().getString("nick." + player.getUniqueId().toString()) == null) {
										String format = ChatPlugin.plugin.getConfig().getString("GlobalFormat");
										format = format.replaceAll("%ARROW%", arrow);
										format = format.replaceAll("%PREFIX%", prefix);
										format = format.replaceAll("%SUFFIX%", suffix);
										format = format.replaceAll("%MESSAGE%", message);
										format = format.replaceAll("&", "§");
										format = format.replaceAll("%PLAYER%", splayer);
										Bukkit.getServer().broadcastMessage(format);
									} else {
										String format = ChatPlugin.plugin.getConfig().getString("GlobalFormat");
										format = format.replaceAll("%ARROW%", arrow);
										format = format.replaceAll("%PREFIX%", prefix);
										format = format.replaceAll("%SUFFIX%", suffix);
										format = format.replaceAll("%MESSAGE%", message);
										format = format.replaceAll("&", "§");
										format = format.replaceAll("%PLAYER%", displayname);
										Bukkit.getServer().broadcastMessage(format);
									}
									ChatPlugin.economy.withdrawPlayer(player, ChatPlugin.plugin.getConfig().getInt("Price"));
								}
							} else {
								sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "You need $" + Integer.toString(ChatPlugin.plugin.getConfig().getInt("Price")) + " to send a global message!");
							}
						} else {
							if (args.length == 0) {
								sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "You need to add a message to send!");
							} else {
								String message = "";
								for(int i = 0; i < args.length; i++) {
									String arg = args[i] + " ";
									message = message + arg;
								}
								if (sender.hasPermission("chat.color")) {
									message = message.replaceAll("&", "§");
								}
								if (c.getConfig().getString("nick." + player.getUniqueId().toString()) == null) {
									String format = ChatPlugin.plugin.getConfig().getString("GlobalFormat");
									format = format.replaceAll("%ARROW%", arrow);
									format = format.replaceAll("%PREFIX%", prefix);
									format = format.replaceAll("%SUFFIX%", suffix);
									format = format.replaceAll("%MESSAGE%", message);
									format = format.replaceAll("&", "§");
									format = format.replaceAll("%PLAYER%", splayer);
									Bukkit.getServer().broadcastMessage(format);
								} else {
									String format = ChatPlugin.plugin.getConfig().getString("GlobalFormat");
									format = format.replaceAll("%ARROW%", arrow);
									format = format.replaceAll("%PREFIX%", prefix);
									format = format.replaceAll("%SUFFIX%", suffix);
									format = format.replaceAll("%MESSAGE%", message);
									format = format.replaceAll("&", "§");
									format = format.replaceAll("%PLAYER%", displayname);
									Bukkit.getServer().broadcastMessage(format);
								}
							}
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
