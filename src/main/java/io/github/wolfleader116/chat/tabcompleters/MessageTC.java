package io.github.wolfleader116.chat.tabcompleters;

import io.github.wolfleader116.chat.ChatPlugin;
import io.github.wolfleader116.chat.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class MessageTC implements TabCompleter {
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("msg")) {
			if (sender instanceof Player) {
				Config c = new Config("playerdata", ChatPlugin.plugin);
				Player player = (Player) sender;
				ArrayList<String> possible = new ArrayList<String>();
				if (args.length == 1) {
					if (!(args[0].equals(""))) {
						if (c.getConfig().getString("reply." + player.getUniqueId().toString()) == null) {
							for (Player p : Bukkit.getServer().getOnlinePlayers()) {
								String sub = p.getName();
								if (sub.toLowerCase().startsWith(args[0].toLowerCase())) {
									possible.add(sub);
								}
							}
						} else {
							if (c.getConfig().getString("reply." + player.getUniqueId().toString()).startsWith(args[0])) {
								possible.add(c.getConfig().getString("reply." + player.getUniqueId().toString()));
							} else {
								for (Player p : Bukkit.getServer().getOnlinePlayers()) {
									String sub = p.getName();
									if (sub.toLowerCase().startsWith(args[0].toLowerCase())) {
										possible.add(sub);
									}
								}
							}
						}
					} else {
						if (c.getConfig().getString("reply." + player.getUniqueId().toString()) == null) {
							for (Player p : Bukkit.getServer().getOnlinePlayers()) {
								String sub = p.getName();
								if (sub.toLowerCase().startsWith(args[0].toLowerCase())) {
									possible.add(sub);
								}
							}
						} else {
							possible.add(c.getConfig().getString("reply." + player.getUniqueId().toString()));
						}
					}
				}
				Collections.sort(possible);
				return possible;
			}
		}
		return null;
	}
}
