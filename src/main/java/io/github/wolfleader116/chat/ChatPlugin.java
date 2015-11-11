package io.github.wolfleader116.chat;

import io.github.wolfleader116.chat.commands.AllPluginsC;
import io.github.wolfleader116.chat.commands.BroadcastC;
import io.github.wolfleader116.chat.commands.ChatC;
import io.github.wolfleader116.chat.commands.GlobalC;
import io.github.wolfleader116.chat.commands.LocalC;
import io.github.wolfleader116.chat.commands.MessageC;
import io.github.wolfleader116.chat.commands.NickC;
import io.github.wolfleader116.chat.commands.PluginsC;
import io.github.wolfleader116.chat.commands.ReplyC;
import io.github.wolfleader116.chat.commands.StaffC;
import io.github.wolfleader116.chat.tabcompleters.ChatTC;
import io.github.wolfleader116.chat.tabcompleters.MessageTC;
import io.github.wolfleader116.chat.tabcompleters.NickTC;
import io.github.wolfleader116.wolfapi.ChatComponent;
import io.github.wolfleader116.wolfapi.ChatElement;
import io.github.wolfleader116.wolfapi.ComponentType;
import io.github.wolfleader116.wolfapi.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatPlugin extends JavaPlugin implements Listener {

	public static Economy economy = null;
	public static Chat chat = null;

	private boolean setupChat()
	{
		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}

		return (chat != null);
	}

	private boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		if (this.getConfig().getInt("Version") != 3) {
			File conf = new File(this.getDataFolder(), "config.yml");
			conf.delete();
			this.saveDefaultConfig();
			this.reloadConfig();
		}
		initializeCommands();
		initializeTabcompleters();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		setupChat();
		setupEconomy();
		plugin = this;
	}

	@Override
	public void onDisable() {
		plugin = null;
	}

	public static String getNick(Player p) {
		Config c = new Config("playerdata", ChatPlugin.plugin);
		if (c.getConfig().getString("nick." + p.getUniqueId().toString()) == null) {
			String nick = p.getName();
			return nick;
		} else {
			String nick = c.getConfig().getString("nick." + p.getUniqueId().toString()) + ChatColor.RESET;
			return nick;
		}
	}

	private void initializeCommands() {
		this.getCommand("broadcast").setExecutor(new BroadcastC());
		this.getCommand("nick").setExecutor(new NickC());
		this.getCommand("chat").setExecutor(new ChatC());
		this.getCommand("pl").setExecutor(new PluginsC());
		this.getCommand("allpl").setExecutor(new AllPluginsC());
		this.getCommand("msg").setExecutor(new MessageC());
		this.getCommand("r").setExecutor(new ReplyC());
		this.getCommand("global").setExecutor(new GlobalC());
		this.getCommand("local").setExecutor(new LocalC());
		this.getCommand("staff").setExecutor(new StaffC());
	}

	private void initializeTabcompleters() {
		this.getCommand("nick").setTabCompleter(new NickTC());
		this.getCommand("chat").setTabCompleter(new ChatTC());
		this.getCommand("msg").setTabCompleter(new MessageTC());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Config co = new Config("playerdata", ChatPlugin.plugin);
		if ((co.getConfig().getInt("channel." + e.getPlayer().getUniqueId().toString()) != 0) && (co.getConfig().getInt("channel." + e.getPlayer().getUniqueId().toString()) != 1) && (co.getConfig().getInt("channel." + e.getPlayer().getUniqueId().toString()) != 2) && (co.getConfig().getInt("channel." + e.getPlayer().getUniqueId().toString()) != 3)) {
			if (this.getConfig().getBoolean("Global")) {
				co.getConfig().set("channel." + e.getPlayer().getUniqueId().toString(), 0);
			} else {
				co.getConfig().set("channel." + e.getPlayer().getUniqueId().toString(), 1);
			}
		}
		if (!(co.getConfig().getBoolean("join." + e.getPlayer().getUniqueId().toString()))) {
			co.getConfig().set("join." + e.getPlayer().getUniqueId().toString(), true);
			co.save();
			String joinmessage = this.getConfig().getString("FirstJoinMessage");
			joinmessage = joinmessage.replaceAll("&", "§");
			List<ChatElement> joinchats = new ArrayList<ChatElement>();
			for (String join : Arrays.asList(joinmessage.split("%PLAYER%"))) {
				joinchats.add(new ChatElement(join));
			}
			ChatElement elem = new ChatElement(e.getPlayer().getName(), new ChatComponent(ComponentType.RUN_COMMAND, "/p " + e.getPlayer().getName()), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
			for (int i = 1; i < joinchats.size(); i++) {
				joinchats.add(i, elem);
				i++;
			}
			Message.broadcastJSONMessage(joinchats);
		}
		Config c = new Config("playerdata", ChatPlugin.plugin);
		String message = this.getConfig().getString("JoinMessage");
		if (e.getPlayer().hasPermission("chat.silent")) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				if (online.hasPermission("chat.silent.exempt")) {
					if (c.getConfig().getString("nick." + e.getPlayer().getUniqueId().toString()) == null) {
						message = message.replaceAll("&", "§");
						List<ChatElement> joinchats = new ArrayList<ChatElement>();
						for (String join : Arrays.asList(message.split("%PLAYER%"))) {
							joinchats.add(new ChatElement(join));
						}
						ChatElement elem = new ChatElement(e.getPlayer().getName(), new ChatComponent(ComponentType.RUN_COMMAND, "/p " + e.getPlayer().getName()), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
						for (int i = 1; i < joinchats.size(); i++) {
							joinchats.add(i, elem);
							i++;
						}
						Message.sendJSONMessage(online, joinchats);
					} else {
						String displayname = c.getConfig().getString("nick." + e.getPlayer().getUniqueId().toString()) + ChatColor.RESET;
						message = message.replaceAll("&", "§");
						List<ChatElement> joinchats = new ArrayList<ChatElement>();
						for (String join : Arrays.asList(message.split("%PLAYER%"))) {
							joinchats.add(new ChatElement(join));
						}
						ChatElement elem = new ChatElement(displayname, new ChatComponent(ComponentType.RUN_COMMAND, "/p " + e.getPlayer().getName()), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
						for (int i = 1; i < joinchats.size(); i++) {
							joinchats.add(i, elem);
							i++;
						}
						Message.sendJSONMessage(online, joinchats);
					}
				}
			}
			e.setJoinMessage(null);
		} else {
			if (c.getConfig().getString("nick." + e.getPlayer().getUniqueId().toString()) == null) {
				message = message.replaceAll("&", "§");
				List<ChatElement> joinchats = new ArrayList<ChatElement>();
				for (String join : Arrays.asList(message.split("%PLAYER%"))) {
					joinchats.add(new ChatElement(join));
				}
				ChatElement elem = new ChatElement(e.getPlayer().getName(), new ChatComponent(ComponentType.RUN_COMMAND, "/p " + e.getPlayer().getName()), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
				for (int i = 1; i < joinchats.size(); i++) {
					joinchats.add(i, elem);
					i++;
				}
				Message.broadcastJSONMessage(joinchats);
			} else {
				String displayname = c.getConfig().getString("nick." + e.getPlayer().getUniqueId().toString()) + ChatColor.RESET;
				message = message.replaceAll("&", "§");
				List<ChatElement> joinchats = new ArrayList<ChatElement>();
				for (String join : Arrays.asList(message.split("%PLAYER%"))) {
					joinchats.add(new ChatElement(join));
				}
				ChatElement elem = new ChatElement(displayname, new ChatComponent(ComponentType.RUN_COMMAND, "/p " + e.getPlayer().getName()), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
				for (int i = 1; i < joinchats.size(); i++) {
					joinchats.add(i, elem);
					i++;
				}
				Message.broadcastJSONMessage(joinchats);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent e) {
		Config c = new Config("playerdata", ChatPlugin.plugin);
		String message = this.getConfig().getString("LeaveMessage");
		if (e.getPlayer().hasPermission("chat.silent")) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				if (online.hasPermission("chat.silent.exempt")) {
					if (c.getConfig().getString("nick." + e.getPlayer().getUniqueId().toString()) == null) {
						message = message.replaceAll("&", "§");
						List<ChatElement> joinchats = new ArrayList<ChatElement>();
						for (String join : Arrays.asList(message.split("%PLAYER%"))) {
							joinchats.add(new ChatElement(join));
						}
						ChatElement elem = new ChatElement(e.getPlayer().getName(), new ChatComponent(ComponentType.RUN_COMMAND, "/p " + e.getPlayer().getName()), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
						for (int i = 1; i < joinchats.size(); i++) {
							joinchats.add(i, elem);
							i++;
						}
						Message.sendJSONMessage(online, joinchats);
					} else {
						String displayname = c.getConfig().getString("nick." + e.getPlayer().getUniqueId().toString()) + ChatColor.RESET;
						message = message.replaceAll("&", "§");
						List<ChatElement> joinchats = new ArrayList<ChatElement>();
						for (String join : Arrays.asList(message.split("%PLAYER%"))) {
							joinchats.add(new ChatElement(join));
						}
						ChatElement elem = new ChatElement(displayname, new ChatComponent(ComponentType.RUN_COMMAND, "/p " + e.getPlayer().getName()), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
						for (int i = 1; i < joinchats.size(); i++) {
							joinchats.add(i, elem);
							i++;
						}
						Message.sendJSONMessage(online, joinchats);
					}
				}
			}
			e.setQuitMessage(null);
		} else {
			if (c.getConfig().getString("nick." + e.getPlayer().getUniqueId().toString()) == null) {
				message = message.replaceAll("&", "§");
				List<ChatElement> joinchats = new ArrayList<ChatElement>();
				for (String join : Arrays.asList(message.split("%PLAYER%"))) {
					joinchats.add(new ChatElement(join));
				}
				ChatElement elem = new ChatElement(e.getPlayer().getName(), new ChatComponent(ComponentType.RUN_COMMAND, "/p " + e.getPlayer().getName()), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
				for (int i = 1; i < joinchats.size(); i++) {
					joinchats.add(i, elem);
					i++;
				}
				Message.broadcastJSONMessage(joinchats);
			} else {
				String displayname = c.getConfig().getString("nick." + e.getPlayer().getUniqueId().toString()) + ChatColor.RESET;
				message = message.replaceAll("&", "§");
				List<ChatElement> joinchats = new ArrayList<ChatElement>();
				for (String join : Arrays.asList(message.split("%PLAYER%"))) {
					joinchats.add(new ChatElement(join));
				}
				ChatElement elem = new ChatElement(displayname, new ChatComponent(ComponentType.RUN_COMMAND, "/p " + e.getPlayer().getName()), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
				for (int i = 1; i < joinchats.size(); i++) {
					joinchats.add(i, elem);
					i++;
				}
				Message.broadcastJSONMessage(joinchats);
			}
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Config c = new Config("playerdata", ChatPlugin.plugin);
		String message = e.getMessage();
		String player = e.getPlayer().getName() + ChatColor.RESET;
		Player eplayer = e.getPlayer();
		String displayname = c.getConfig().getString("nick." + eplayer.getUniqueId().toString()) + ChatColor.RESET;
		String prefix = chat.getPlayerPrefix(eplayer) + ChatColor.RESET;
		String suffix = chat.getPlayerSuffix(eplayer);
		String arrow = ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "> " + ChatColor.RESET;
		String arrows = ChatColor.LIGHT_PURPLE + "> " + ChatColor.RESET;
		prefix = prefix.replaceAll("&", "§");
		int range = this.getConfig().getInt("Range");
		if (eplayer.hasPermission("chat.color")) {
			message = message.replaceAll("&", "§");
		}
		if (this.getConfig().getBoolean("Global")) {
			if (c.getConfig().getInt("channel." + eplayer.getUniqueId().toString()) == 0) {
				if (c.getConfig().getString("nick." + eplayer.getUniqueId().toString()) == null) {
					String format = this.getConfig().getString("GlobalFormat");
					format = format.replaceAll("%ARROW%", arrow);
					format = format.replaceAll("%PREFIX%", prefix);
					format = format.replaceAll("%SUFFIX%", suffix);
					format = format.replaceAll("%MESSAGE%", message);
					format = format.replaceAll("&", "§");
					format = format.replaceAll("%PLAYER%", player);
					e.setFormat(format);
				} else {
					String format = this.getConfig().getString("GlobalFormat");
					format = format.replaceAll("%ARROW%", arrow);
					format = format.replaceAll("%PREFIX%", prefix);
					format = format.replaceAll("%SUFFIX%", suffix);
					format = format.replaceAll("%MESSAGE%", message);
					format = format.replaceAll("&", "§");
					format = format.replaceAll("%PLAYER%", displayname);
					e.setFormat(format);
				}
			} else if (c.getConfig().getInt("channel." + eplayer.getUniqueId().toString()) == 1) {
				for (Player rec : Bukkit.getOnlinePlayers()) {
					if (rec.getWorld() == eplayer.getWorld()) {
						if (rec.getLocation().distance(eplayer.getLocation()) <= range) {
							if (c.getConfig().getString("nick." + eplayer.getUniqueId().toString()) == null) {
								String format = this.getConfig().getString("LocalFormat");
								format = format.replaceAll("%ARROW%", arrow);
								format = format.replaceAll("%PREFIX%", prefix);
								format = format.replaceAll("%SUFFIX%", suffix);
								format = format.replaceAll("%MESSAGE%", message);
								format = format.replaceAll("&", "§");
								format = format.replaceAll("%PLAYER%", player);
								rec.sendMessage(format);
								e.setCancelled(true);
							} else {
								String format = this.getConfig().getString("LocalFormat");
								format = format.replaceAll("%ARROW%", arrow);
								format = format.replaceAll("%PREFIX%", prefix);
								format = format.replaceAll("%SUFFIX%", suffix);
								format = format.replaceAll("%MESSAGE%", message);
								format = format.replaceAll("&", "§");
								format = format.replaceAll("%PLAYER%", displayname);
								rec.sendMessage(format);
								e.setCancelled(true);
							}
						}
					}
				}
			} else if (c.getConfig().getInt("channel." + eplayer.getUniqueId().toString()) == 2) {
				if (c.getConfig().getString("reply." + eplayer.getUniqueId().toString()) == null) {
					eplayer.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "You have nobody to reply to!");
					e.setCancelled(true);
				} else if (Bukkit.getPlayer(c.getConfig().getString("reply." + eplayer.getUniqueId().toString())) == null) {
					eplayer.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "That player is offline!");
					e.setCancelled(true);
				} else {
					Player rec = Bukkit.getPlayer(c.getConfig().getString("reply." + eplayer.getUniqueId().toString()));
					c.getConfig().set("reply." + rec.getUniqueId().toString(), eplayer.getName());
					c.save();
					if (Bukkit.getServer().getPluginManager().getPlugin("Settings") != null) {
						Config settings = new Config("../Settings/playerdata", ChatPlugin.plugin);
						if (settings.getConfig().getBoolean("afk." + rec.getUniqueId().toString())) {
							eplayer.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "That player is afk so they may not see your message!");
						}
					}
					if (c.getConfig().getString("nick." + eplayer.getUniqueId().toString()) == null) {
						String splayer = ChatColor.RESET + eplayer.getName() + ChatColor.RESET;
						String format = this.getConfig().getString("MsgFormatFrom");
						format = format.replaceAll("%ARROW%", arrows);
						format = format.replaceAll("%PREFIX%", prefix);
						format = format.replaceAll("%SUFFIX%", suffix);
						format = format.replaceAll("%MESSAGE%", message);
						format = format.replaceAll("&", "§");
						format = format.replaceAll("%PLAYER%", splayer);
						rec.sendMessage(format);
						e.setCancelled(true);
					} else {
						String splayer = ChatColor.RESET + c.getConfig().getString("nick." + eplayer.getUniqueId().toString()) + ChatColor.RESET;
						String format = this.getConfig().getString("MsgFormatFrom");
						format = format.replaceAll("%ARROW%", arrows);
						format = format.replaceAll("%PREFIX%", prefix);
						format = format.replaceAll("%SUFFIX%", suffix);
						format = format.replaceAll("%MESSAGE%", message);
						format = format.replaceAll("&", "§");
						format = format.replaceAll("%PLAYER%", splayer);
						rec.sendMessage(format);
						e.setCancelled(true);
					}
					if (c.getConfig().getString("nick." + rec.getUniqueId().toString()) == null) {
						String srec = ChatColor.RESET + rec.getName() + ChatColor.RESET;
						String format = this.getConfig().getString("MsgFormatTo");
						format = format.replaceAll("%ARROW%", arrows);
						format = format.replaceAll("%PREFIX%", prefix);
						format = format.replaceAll("%SUFFIX%", suffix);
						format = format.replaceAll("%MESSAGE%", message);
						format = format.replaceAll("&", "§");
						format = format.replaceAll("%PLAYER%", srec);
						eplayer.sendMessage(format);
						e.setCancelled(true);
					} else {
						String srec = ChatColor.RESET + c.getConfig().getString("nick." + rec.getUniqueId().toString()) + ChatColor.RESET;
						String format = this.getConfig().getString("MsgFormatTo");
						format = format.replaceAll("%ARROW%", arrows);
						format = format.replaceAll("%PREFIX%", prefix);
						format = format.replaceAll("%SUFFIX%", suffix);
						format = format.replaceAll("%MESSAGE%", message);
						format = format.replaceAll("&", "§");
						format = format.replaceAll("%PLAYER%", srec);
						eplayer.sendMessage(format);
						e.setCancelled(true);
					}
				}
			} else if (c.getConfig().getInt("channel." + eplayer.getUniqueId().toString()) == 3) {
				if (this.getConfig().getBoolean("Staff")) {
					if (eplayer.hasPermission("chat.staff")) {
						for (Player rec : Bukkit.getOnlinePlayers()) {
							if (rec.hasPermission("chat.staff")) {
								if (c.getConfig().getString("nick." + eplayer.getUniqueId().toString()) == null) {
									String format = this.getConfig().getString("StaffFormat");
									format = format.replaceAll("%ARROW%", arrow);
									format = format.replaceAll("%PREFIX%", prefix);
									format = format.replaceAll("%SUFFIX%", suffix);
									format = format.replaceAll("%MESSAGE%", message);
									format = format.replaceAll("&", "§");
									format = format.replaceAll("%PLAYER%", player);
									rec.sendMessage(format);
									e.setCancelled(true);
								} else {
									String format = this.getConfig().getString("StaffFormat");
									format = format.replaceAll("%ARROW%", arrow);
									format = format.replaceAll("%PREFIX%", prefix);
									format = format.replaceAll("%SUFFIX%", suffix);
									format = format.replaceAll("%MESSAGE%", message);
									format = format.replaceAll("&", "§");
									format = format.replaceAll("%PLAYER%", displayname);
									rec.sendMessage(format);
									e.setCancelled(true);
								}
							}
						}
					} else {
						c.getConfig().set("channel." + eplayer.getUniqueId().toString(), 0);
						c.save();
					}
				} else {
					c.getConfig().set("channel." + eplayer.getUniqueId().toString(), 0);
					c.save();
				}
			}
		} else {
			if (c.getConfig().getInt("channel." + eplayer.getUniqueId().toString()) == 0) {
				eplayer.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "Your channel cannot be global! Please choose another channel!");
				e.setCancelled(true);
			} else if (c.getConfig().getInt("channel." + eplayer.getUniqueId().toString()) == 1) {
				for (Player rec : Bukkit.getOnlinePlayers()) {
					if (rec.getWorld() == eplayer.getWorld()) {
						if (rec.getLocation().distance(eplayer.getLocation()) <= range) {
							if (c.getConfig().getString("nick." + eplayer.getUniqueId().toString()) == null) {
								String format = this.getConfig().getString("LocalFormat");
								format = format.replaceAll("%ARROW%", arrow);
								format = format.replaceAll("%PREFIX%", prefix);
								format = format.replaceAll("%SUFFIX%", suffix);
								format = format.replaceAll("%MESSAGE%", message);
								format = format.replaceAll("&", "§");
								format = format.replaceAll("%PLAYER%", player);
								rec.sendMessage(format);
								e.setCancelled(true);
							} else {
								String format = this.getConfig().getString("LocalFormat");
								format = format.replaceAll("%ARROW%", arrow);
								format = format.replaceAll("%PREFIX%", prefix);
								format = format.replaceAll("%SUFFIX%", suffix);
								format = format.replaceAll("%MESSAGE%", message);
								format = format.replaceAll("&", "§");
								format = format.replaceAll("%PLAYER%", displayname);
								rec.sendMessage(format);
								e.setCancelled(true);
							}
						}
					}
				}
			} else if (c.getConfig().getInt("channel." + eplayer.getUniqueId().toString()) == 2) {
				if (c.getConfig().getString("reply." + eplayer.getUniqueId().toString()) == null) {
					eplayer.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "You have nobody to reply to!");
					e.setCancelled(true);
				} else if (Bukkit.getPlayer(c.getConfig().getString("reply." + eplayer.getUniqueId().toString())) == null) {
					eplayer.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "That player is offline!");
					e.setCancelled(true);
				} else {
					Player rec = Bukkit.getPlayer(c.getConfig().getString("reply." + eplayer.getUniqueId().toString()));
					c.getConfig().set("reply." + rec.getUniqueId().toString(), eplayer.getName());
					c.save();
					if (Bukkit.getServer().getPluginManager().getPlugin("Settings") != null) {
						Config settings = new Config("../Settings/playerdata", ChatPlugin.plugin);
						if (settings.getConfig().getBoolean("afk." + rec.getUniqueId().toString())) {
							eplayer.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "That player is afk so they may not see your message!");
						}
					}
					if (c.getConfig().getString("nick." + eplayer.getUniqueId().toString()) == null) {
						String splayer = ChatColor.RESET + eplayer.getName() + ChatColor.RESET;
						String format = this.getConfig().getString("MsgFormatFrom");
						format = format.replaceAll("%ARROW%", arrows);
						format = format.replaceAll("%PREFIX%", prefix);
						format = format.replaceAll("%SUFFIX%", suffix);
						format = format.replaceAll("%MESSAGE%", message);
						format = format.replaceAll("&", "§");
						format = format.replaceAll("%PLAYER%", splayer);
						rec.sendMessage(format);
						e.setCancelled(true);
					} else {
						String splayer = ChatColor.RESET + c.getConfig().getString("nick." + eplayer.getUniqueId().toString()) + ChatColor.RESET;
						String format = this.getConfig().getString("MsgFormatFrom");
						format = format.replaceAll("%ARROW%", arrows);
						format = format.replaceAll("%PREFIX%", prefix);
						format = format.replaceAll("%SUFFIX%", suffix);
						format = format.replaceAll("%MESSAGE%", message);
						format = format.replaceAll("&", "§");
						format = format.replaceAll("%PLAYER%", splayer);
						rec.sendMessage(format);
						e.setCancelled(true);
					}
					if (c.getConfig().getString("nick." + rec.getUniqueId().toString()) == null) {
						String srec = ChatColor.RESET + rec.getName() + ChatColor.RESET;
						String format = this.getConfig().getString("MsgFormatTo");
						format = format.replaceAll("%ARROW%", arrows);
						format = format.replaceAll("%PREFIX%", prefix);
						format = format.replaceAll("%SUFFIX%", suffix);
						format = format.replaceAll("%MESSAGE%", message);
						format = format.replaceAll("&", "§");
						format = format.replaceAll("%PLAYER%", srec);
						eplayer.sendMessage(format);
						e.setCancelled(true);
					} else {
						String srec = ChatColor.RESET + c.getConfig().getString("nick." + rec.getUniqueId().toString()) + ChatColor.RESET;
						String format = this.getConfig().getString("MsgFormatTo");
						format = format.replaceAll("%ARROW%", arrows);
						format = format.replaceAll("%PREFIX%", prefix);
						format = format.replaceAll("%SUFFIX%", suffix);
						format = format.replaceAll("%MESSAGE%", message);
						format = format.replaceAll("&", "§");
						format = format.replaceAll("%PLAYER%", srec);
						eplayer.sendMessage(format);
						e.setCancelled(true);
					}
				}
			} else if (c.getConfig().getInt("channel." + eplayer.getUniqueId().toString()) == 3) {
				if (this.getConfig().getBoolean("Staff")) {
					if (eplayer.hasPermission("chat.staff")) {
						for (Player rec : Bukkit.getOnlinePlayers()) {
							if (rec.hasPermission("chat.staff")) {
								if (c.getConfig().getString("nick." + eplayer.getUniqueId().toString()) == null) {
									String format = this.getConfig().getString("StaffFormat");
									format = format.replaceAll("%ARROW%", arrow);
									format = format.replaceAll("%PREFIX%", prefix);
									format = format.replaceAll("%SUFFIX%", suffix);
									format = format.replaceAll("%MESSAGE%", message);
									format = format.replaceAll("&", "§");
									format = format.replaceAll("%PLAYER%", player);
									rec.sendMessage(format);
									e.setCancelled(true);
								} else {
									String format = this.getConfig().getString("StaffFormat");
									format = format.replaceAll("%ARROW%", arrow);
									format = format.replaceAll("%PREFIX%", prefix);
									format = format.replaceAll("%SUFFIX%", suffix);
									format = format.replaceAll("%MESSAGE%", message);
									format = format.replaceAll("&", "§");
									format = format.replaceAll("%PLAYER%", displayname);
									rec.sendMessage(format);
									e.setCancelled(true);
								}
							}
						}
					} else {
						c.getConfig().set("channel." + eplayer.getUniqueId().toString(), 0);
						c.save();
					}
				} else {
					c.getConfig().set("channel." + eplayer.getUniqueId().toString(), 0);
					c.save();
				}
			}
		}
	}

	public static ChatPlugin plugin;

}
