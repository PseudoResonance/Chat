package io.github.wolfleader116.chat.tabcompleters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class NickTC implements TabCompleter {
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("nick")) {
			ArrayList<String> possible = new ArrayList<String>();
			if (args.length == 1) {
				if (!(args[0].equals(""))) {
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						String sub = p.getName();
						if (sub.toLowerCase().startsWith(args[0])) {
							possible.add(sub);
						}
					}
				} else {
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						String sub = p.getName();
						possible.add(sub);
					}
				}
			} else if (args.length == 2) {
				if (!(args[1].equals(""))) {
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						String sub = p.getName();
						if (sub.toLowerCase().startsWith(args[1])) {
							possible.add(sub);
						}
					}
				} else {
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						String sub = p.getName();
						possible.add(sub);
					}
				}
			}
			Collections.sort(possible);
			return possible;
		}
		return null;
	}
}
