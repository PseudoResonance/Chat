package io.github.wolfleader116.chat.commands;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AllPluginsC implements CommandExecutor {

	private static final Logger log = Logger.getLogger("Minecraft");

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("allpl")) {
			if (!(sender instanceof Player)) {
				String pluginlist = "";
				int pluginsfound = 1;
				Plugin[] plugins = Bukkit.getServer().getPluginManager().getPlugins();
				for(int i = 0; i < plugins.length; i++) {
					pluginsfound = pluginsfound + 1;
					if (Bukkit.getServer().getPluginManager().isPluginEnabled(plugins[i])) {
						String add = "";
						if (pluginlist == "") {
							add = "[E] " + plugins[i].getName();
						} else {
							add = ", [E] " + plugins[i].getName();
						}
						pluginlist = pluginlist + add;
					} else {
						String add = "";
						if (pluginlist == "") {
							add = "[D] " + plugins[i].getName();
						} else {
							add = ", [D] " + plugins[i].getName();
						}
						pluginlist = pluginlist + add;
					}
				}
				log.info("Plugins (" + pluginsfound + "): " + pluginlist);
			} else {
				if (sender.hasPermission("chat.allpl")) {
					String pluginlist = "";
					int pluginsfound = 1;
					Plugin[] plugins = Bukkit.getServer().getPluginManager().getPlugins();
					for(int i = 0; i < plugins.length; i++) {
						pluginsfound = pluginsfound + 1;
						if (Bukkit.getServer().getPluginManager().isPluginEnabled(plugins[i])) {
							String add = "";
							if (pluginlist == "") {
								add = ChatColor.GREEN + plugins[i].getName();
							} else {
								add = ChatColor.RESET + ", " + plugins[i].getName();
							}
							pluginlist = pluginlist + add;
						} else {
							String add = "";
							if (pluginlist == "") {
								add = ChatColor.RED + plugins[i].getName();
							} else {
								add = ChatColor.RESET + ", " + ChatColor.RED + plugins[i].getName();
							}
							pluginlist = pluginlist + add;
						}
					}
					sender.sendMessage("Plugins (" + pluginsfound + "): " + pluginlist);
				}
			}
		}
		return false;
	}
}
