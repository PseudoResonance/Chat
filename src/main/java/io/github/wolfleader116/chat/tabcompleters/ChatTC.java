package io.github.wolfleader116.chat.tabcompleters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class ChatTC implements TabCompleter {
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("chat")) {
			ArrayList<String> possible = new ArrayList<String>();
			if (args.length == 1) {
				ArrayList<String> subs = new ArrayList<String>();
				subs.add("help");
				subs.add("reset");
				subs.add("reload");
				if (!(args[0].equals(""))) {
					for (String sub : subs) {
						if (sub.toLowerCase().startsWith(args[0].toLowerCase())) {
							possible.add(sub);
						}
					}
				} else {
					for (String sub : subs) {
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
