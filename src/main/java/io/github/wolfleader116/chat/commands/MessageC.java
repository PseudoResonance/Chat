package io.github.wolfleader116.chat.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import io.github.wolfleader116.chat.ChatPlugin;
import io.github.wolfleader116.chat.Config;
import io.github.wolfleader116.utils.Utils;
import io.github.wolfleader116.wolfapi.ChatComponent;
import io.github.wolfleader116.wolfapi.ChatElement;
import io.github.wolfleader116.wolfapi.ComponentType;
import io.github.wolfleader116.wolfapi.Errors;
import io.github.wolfleader116.wolfapi.Message;
import io.github.wolfleader116.wolfapi.WolfAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageC implements CommandExecutor {

	private static final Logger log = Logger.getLogger("Minecraft");

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Config c = new Config("playerdata", ChatPlugin.plugin);
			if (cmd.getName().equalsIgnoreCase("msg")) {
				if (args.length == 0) {
					log.info("You need to add player and a message!");
				} else if (args.length == 1) {
					log.info("You need to add a message!");
				} else if (args.length >= 2) {
					if (Bukkit.getPlayer(args[0]) == null) {
						log.info("That player is offline!");
					} else {
						Player rec = Bukkit.getPlayer(args[0]);
						String message = "";
						for(int i = 1; i < args.length; i++) {
							String arg = args[i] + " ";
							message = message + arg;
						}
						message = message.replaceAll("&", "§");
						c.getConfig().set("reply.console", rec.getName());
						c.getConfig().set("reply." + rec.getUniqueId().toString(), "console");
						c.save();
						if (Bukkit.getServer().getPluginManager().getPlugin("Settings") != null) {
							Config settings = new Config("../Settings/playerdata", ChatPlugin.plugin);
							if (settings.getConfig().getBoolean("afk." + rec.getUniqueId().toString())) {
								log.info("That player is afk so they may not see your message!");
							}
						}
						String splayer = ChatColor.RESET + "Console" + ChatColor.RESET;
						String arrows = ChatColor.LIGHT_PURPLE + "> " + ChatColor.RESET;
						String format = ChatPlugin.plugin.getConfig().getString("MsgFormatFrom");
						format = format.replaceAll("%ARROW%", arrows);
						format = format.replaceAll("%PREFIX%", "");
						format = format.replaceAll("%SUFFIX%", "c");
						format = format.replaceAll("%MESSAGE%", message);
						format = format.replaceAll("&", "§");
						List<ChatElement> joinchats = new ArrayList<ChatElement>();
						for (String join : Arrays.asList(format.split("%PLAYER%"))) {
							joinchats.add(new ChatElement(join));
						}
						ChatElement elem = new ChatElement(splayer);
						for (int i = 1; i < joinchats.size(); i++) {
							joinchats.add(i, elem);
							i++;
						}
						Message.sendJSONMessage(rec, joinchats);
						if (c.getConfig().getString("nick." + rec.getUniqueId().toString()) == null) {
							String srec = ChatColor.RESET + rec.getName() + ChatColor.RESET;
							String formats = ChatPlugin.plugin.getConfig().getString("MsgFormatTo");
							format = format.replaceAll("%ARROW%", arrows);
							format = format.replaceAll("%PREFIX%", "");
							format = format.replaceAll("%SUFFIX%", "c");
							format = format.replaceAll("%MESSAGE%", message);
							formats = formats.replaceAll("&", "§");
							format = format.replaceAll("%PLAYER%", srec);
							log.info(formats);
						} else {
							String srec = ChatColor.RESET + c.getConfig().getString("nick." + rec.getUniqueId().toString()) + ChatColor.RESET;
							String formats = ChatPlugin.plugin.getConfig().getString("MsgFormatTo");
							format = format.replaceAll("%ARROW%", arrows);
							format = format.replaceAll("%PREFIX%", "");
							format = format.replaceAll("%SUFFIX%", "c");
							format = format.replaceAll("%MESSAGE%", message);
							formats = formats.replaceAll("&", "§");
							format = format.replaceAll("%PLAYER%", srec);
							log.info(formats);
						}
					}
				}
			}
		} else if (sender instanceof Player) {
			Config c = new Config("playerdata", ChatPlugin.plugin);
			Player player = (Player) sender;
			if ((Bukkit.getPluginManager().getPlugin("Utils") != null) && Utils.isMuted(player.getUniqueId().toString())) {
				WolfAPI.message("You are muted!", player, "Chat");
			} else {
				if (cmd.getName().equalsIgnoreCase("msg")) {
					if (args.length == 0) {
						if (c.getConfig().getString("reply." + player.getUniqueId().toString()) == null) {
							WolfAPI.message("You have nobody to reply to!", player, "Chat");
						} else {
							c.getConfig().set("channel." + player.getUniqueId().toString(), 2);
							c.save();
							WolfAPI.message("Now in message chat mode!", player, "Chat");
						}
					} else if (args.length == 1) {
						if (Bukkit.getPlayer(args[0]) != null) {
							Player rec = Bukkit.getPlayer(args[0]);
							c.getConfig().set("reply." + player.getUniqueId().toString(), rec.getName());
							c.getConfig().set("channel." + player.getUniqueId().toString(), 2);
							c.save();
							WolfAPI.message("Now in message chat mode!", player, "Chat");
						} else {
							Errors.sendError(Errors.NOT_ONLINE, player, "Chat");
						}
					} else if (args.length >= 2) {
						if (Bukkit.getPlayer(args[0]) == null) {
							Errors.sendError(Errors.NOT_ONLINE, player, "Chat");
						} else {
							Player rec = Bukkit.getPlayer(args[0]);
							String message = "";
							for(int i = 1; i < args.length; i++) {
								String arg = args[i] + " ";
								message = message + arg;
							}
							if (sender.hasPermission("chat.color")) {
								message = message.replaceAll("&", "§");
							}
							c.getConfig().set("reply." + player.getUniqueId().toString(), rec.getName());
							c.getConfig().set("reply." + rec.getUniqueId().toString(), player.getName());
							c.save();
							String prefix = ChatPlugin.chat.getPlayerPrefix(player) + ChatColor.RESET;
							String suffix = ChatPlugin.chat.getPlayerSuffix(player);
							String arrows = ChatColor.LIGHT_PURPLE + "> " + ChatColor.RESET;
							if (Bukkit.getServer().getPluginManager().getPlugin("Settings") != null) {
								Config settings = new Config("../Settings/playerdata", ChatPlugin.plugin);
								if (settings.getConfig().getBoolean("afk." + rec.getUniqueId().toString())) {
									WolfAPI.message("That player is afk so they may not see your message!", player, "Chat");
								}
							}
							if (c.getConfig().getString("nick." + player.getUniqueId().toString()) == null) {
								String splayer = ChatColor.RESET + player.getName() + ChatColor.RESET;
								String format = ChatPlugin.plugin.getConfig().getString("MsgFormatFrom");
								format = format.replaceAll("%ARROW%", arrows);
								format = format.replaceAll("%PREFIX%", prefix);
								format = format.replaceAll("%SUFFIX%", suffix);
								format = format.replaceAll("%MESSAGE%", message);
								format = format.replaceAll("&", "§");
								List<ChatElement> joinchats = new ArrayList<ChatElement>();
								for (String join : Arrays.asList(format.split("%PLAYER%"))) {
									joinchats.add(new ChatElement(join));
								}
								ChatElement elem = new ChatElement(splayer, new ChatComponent(ComponentType.RUN_COMMAND, "/p " + player.getName()), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
								for (int i = 1; i < joinchats.size(); i++) {
									joinchats.add(i, elem);
									i++;
								}
								Message.sendJSONMessage(rec, joinchats);
							} else {
								String splayer = ChatColor.RESET + c.getConfig().getString("nick." + player.getUniqueId().toString()) + ChatColor.RESET;
								String format = ChatPlugin.plugin.getConfig().getString("MsgFormatFrom");
								format = format.replaceAll("%ARROW%", arrows);
								format = format.replaceAll("%PREFIX%", prefix);
								format = format.replaceAll("%SUFFIX%", suffix);
								format = format.replaceAll("%MESSAGE%", message);
								format = format.replaceAll("&", "§");
								List<ChatElement> joinchats = new ArrayList<ChatElement>();
								for (String join : Arrays.asList(format.split("%PLAYER%"))) {
									joinchats.add(new ChatElement(join));
								}
								ChatElement elem = new ChatElement(splayer, new ChatComponent(ComponentType.RUN_COMMAND, "/p " + player.getName()), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
								for (int i = 1; i < joinchats.size(); i++) {
									joinchats.add(i, elem);
									i++;
								}
								Message.sendJSONMessage(rec, joinchats);
							}
							if (c.getConfig().getString("nick." + rec.getUniqueId().toString()) == null) {
								String srec = ChatColor.RESET + rec.getName() + ChatColor.RESET;
								String format = ChatPlugin.plugin.getConfig().getString("MsgFormatTo");
								format = format.replaceAll("%ARROW%", arrows);
								format = format.replaceAll("%PREFIX%", prefix);
								format = format.replaceAll("%SUFFIX%", suffix);
								format = format.replaceAll("%MESSAGE%", message);
								format = format.replaceAll("&", "§");
								List<ChatElement> joinchats = new ArrayList<ChatElement>();
								for (String join : Arrays.asList(format.split("%PLAYER%"))) {
									joinchats.add(new ChatElement(join));
								}
								ChatElement elem = new ChatElement(srec, new ChatComponent(ComponentType.RUN_COMMAND, "/p " + rec.getName()), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
								for (int i = 1; i < joinchats.size(); i++) {
									joinchats.add(i, elem);
									i++;
								}
								Message.sendJSONMessage((Player) sender, joinchats);
							} else {
								String srec = ChatColor.RESET + c.getConfig().getString("nick." + rec.getUniqueId().toString()) + ChatColor.RESET;
								String format = ChatPlugin.plugin.getConfig().getString("MsgFormatTo");
								format = format.replaceAll("%ARROW%", arrows);
								format = format.replaceAll("%PREFIX%", prefix);
								format = format.replaceAll("%SUFFIX%", suffix);
								format = format.replaceAll("%MESSAGE%", message);
								format = format.replaceAll("&", "§");
								List<ChatElement> joinchats = new ArrayList<ChatElement>();
								for (String join : Arrays.asList(format.split("%PLAYER%"))) {
									joinchats.add(new ChatElement(join));
								}
								ChatElement elem = new ChatElement(srec, new ChatComponent(ComponentType.RUN_COMMAND, "/p " + rec.getName()), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
								for (int i = 1; i < joinchats.size(); i++) {
									joinchats.add(i, elem);
									i++;
								}
								Message.sendJSONMessage((Player) sender, joinchats);
							}
						}
					}
				}
			}
		}
		return false;
	}
}
