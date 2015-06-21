package io.github.wolfleader116.chat.commands;

import java.io.File;
import java.util.logging.Logger;

import io.github.wolfleader116.chat.ChatPlugin;
import io.github.wolfleader116.wolfapi.WolfAPI;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatC implements CommandExecutor {

	private static final Logger log = Logger.getLogger("Minecraft");

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		File configFile = new File(ChatPlugin.plugin.getDataFolder(), "config.yml");
		if (cmd.getName().equalsIgnoreCase("chat")) {
			if (!(sender instanceof Player)) {
				if (args.length == 0) {
					log.info("Use /chat help for a list of Chat commands.");
					log.info("Chat plugin created by WolfLeader116");
					log.info("===---Chat Info---===");
				} else if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("help")) {
						log.info("/allpl Shows all plugins.");
						log.info("/pl Shows all custom plugins.");
						log.info("/nick <nickname> (player) Changes your nickname or the specified player's nickname.");
						log.info("/broadcast <message> Broadcasts to the entire server.");
						log.info("/chat reload Reloads the config.");
						log.info("/chat reset Resets the config.");
						log.info("/chat help Shows this message.");
						log.info("/chat Shows the info page.");
						log.info("===---Chat Help---===");
					} else if (args[0].equalsIgnoreCase("reset")) {
						configFile.delete();
						ChatPlugin.plugin.saveDefaultConfig();
						ChatPlugin.plugin.reloadConfig();
						log.info("Reset the config!");
					} else if (args[0].equalsIgnoreCase("reload")) {
						ChatPlugin.plugin.reloadConfig();
						log.info("Reloaded the config!");
					} else {
						log.info("Unknown subcommand! Use /chat help for subcommands.");
					}
				}
			} else {
				Player p = (Player) sender;
				if (args.length == 0) {
					sender.sendMessage(ChatColor.DARK_AQUA + "===---" + ChatColor.GOLD + "Chat Info" + ChatColor.DARK_AQUA + "---===");
					sender.sendMessage(ChatColor.AQUA + "Chat plugin created by WolfLeader116");
					sender.sendMessage(ChatColor.AQUA + "Use " + ChatColor.RED + "/chat help " + ChatColor.AQUA + "for a list of Chat commands.");
				} else if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("help")) {
						sender.sendMessage(ChatColor.DARK_AQUA + "===---" + ChatColor.GOLD + "Chat Help" + ChatColor.DARK_AQUA + "---===");
						sender.sendMessage(ChatColor.RED + "/chat " + ChatColor.AQUA + "Shows the info page.");
						sender.sendMessage(ChatColor.RED + "/chat help " + ChatColor.AQUA + "Shows this message.");
						if (sender.hasPermission("chat.reset")) {
							sender.sendMessage(ChatColor.RED + "/chat reset " + ChatColor.AQUA + "Resets the config.");
						}
						if (sender.hasPermission("chat.reload")) {
							sender.sendMessage(ChatColor.RED + "/chat reload " + ChatColor.AQUA + "Reloads the config.");
						}
						if (sender.hasPermission("chat.broadcast")) {
							sender.sendMessage(ChatColor.RED + "/broadcast <message> " + ChatColor.AQUA + "Broadcasts to the entire server.");
						}
						if (sender.hasPermission("chat.nick.other")) {
							sender.sendMessage(ChatColor.RED + "/nick <nickname> (player) " + ChatColor.AQUA + "Changes your nickname or the specified player's nickname.");
						} else if (sender.hasPermission("chat.nick")) {
							sender.sendMessage(ChatColor.RED + "/nick <nickname> " + ChatColor.AQUA + "Changes your nickname.");
						}
						sender.sendMessage(ChatColor.RED + "/pl " + ChatColor.AQUA + "Shows all custom plugins.");
						if (sender.hasPermission("chat.allplugins")) {
							sender.sendMessage(ChatColor.RED + "/allpl " + ChatColor.AQUA + "Shows all plugins.");
						}
						if (sender.hasPermission("chat.global")) {
							sender.sendMessage(ChatColor.RED + "/global " + ChatColor.AQUA + "Broadcasts a message to the entire server or switches to global mode.");
						}
						if (sender.hasPermission("chat.local")) {
							sender.sendMessage(ChatColor.RED + "/local " + ChatColor.AQUA + "Broadcasts a message to the surrounding area or switches to local mode.");
						}
						if (sender.hasPermission("chat.staff")) {
							sender.sendMessage(ChatColor.RED + "/staff " + ChatColor.AQUA + "Broadcasts a message to the staff or switches to staff mode.");
						}
					} else if (args[0].equalsIgnoreCase("reset")) {
						configFile.delete();
						ChatPlugin.plugin.saveDefaultConfig();
						ChatPlugin.plugin.reloadConfig();
						WolfAPI.message("Reset the config!", p, "Chat");
					} else if (args[0].equalsIgnoreCase("reload")) {
						ChatPlugin.plugin.reloadConfig();
						WolfAPI.message("Reloaded the config!", p, "Chat");
					} else {
						WolfAPI.message("Unknown subcommand! Use /chat help for subcommands.", p, "Chat");
					}
				}
			}
		}
		return false;
	}
}
