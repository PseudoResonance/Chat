package io.github.wolfleader116.chat.commands;

import io.github.wolfleader116.wolfapi.Errors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastC implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("broadcast")) {
			if (!(sender instanceof Player)) {
				String message = "";
				for(int i = 0; i < args.length; i++) {
					String arg = args[i] + " ";
					message = message + arg;
				}
				message = message.replaceAll("&", "§");
				Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Broadcast> " + ChatColor.GREEN + message);
			} else {
				Player p = (Player) sender;
				if (sender.hasPermission("chat.broadcast")) {
					String message = "";
					for(int i = 0; i < args.length; i++) {
						String arg = args[i] + " ";
						message = message + arg;
					}
					if (sender.hasPermission("chat.color")) {
						message = message.replaceAll("&", "§");
					}
					Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Broadcast> " + ChatColor.GREEN + message);
				} else {
					Errors.sendError(Errors.NO_PERMISSION, p, "Chat");
				}
			}
		}
		return false;
	}
}
