package com.excavator.PixelEvents;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	int total = 0;
	boolean enabled = false;
	HashMap<String, Boolean> deaths = new HashMap<String, Boolean>();
	HashMap<String, Boolean> revives = new HashMap<String, Boolean>();

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		for(Player online : Bukkit.getOnlinePlayers())
			online.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
	}
	@Override
	public void onDisable() {

	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		

		if(deaths.get(e.getPlayer().getName()) == null) {
			total++;
		}
		for(Player online : Bukkit.getOnlinePlayers())
			createScoreboard(online);
		
	}
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		total = Bukkit.getOnlinePlayers().size();
		total--;
		for(Player online : Bukkit.getOnlinePlayers())
			createScoreboard(online);
		
	}
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		revives.put(e.getEntity().getName(), null);
		if(deaths.get(e.getEntity().getName()) != null) { //null is non died yet, false is revived

		}
		else { //in not in hashmap or false
		total--;
		deaths.put(e.getEntity().getName(), true);
		for(Player online : Bukkit.getOnlinePlayers())
			createScoreboard(online);
		}
	}
	
	public void createScoreboard(Player player) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective obj = board.registerNewObjective("PixelScoreboard", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "PixelQube Events");
		Score score = obj.getScore(ChatColor.DARK_RED + "" + ChatColor.AQUA + "" + ChatColor.STRIKETHROUGH +"------------------");
		score.setScore(2);
		Score score2 = obj.getScore(ChatColor.AQUA + "» " + ChatColor.WHITE + "Players Alive: " + ChatColor.DARK_AQUA + total);
		score2.setScore(1); 
		Score score3 = obj.getScore(ChatColor.AQUA + "" + ChatColor.STRIKETHROUGH + "------------------");
		score3.setScore(0);
		player.setScoreboard(board);
		
	}
	
	public boolean hasScoreboard(Player p){
		return p.getScoreboard() != null;
		}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("event")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("You cannot execute this from the console!");
				return true;
			}
			else { //player execution
			Player player = (Player) sender;

			if(cmd.getName().equalsIgnoreCase("event")) {
				if(args.length == 0) {
					player.sendMessage("Usage: /event <start | stop | help | revive> [player]");
				}
				else {
				if(args[0].equalsIgnoreCase("help")) {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.STRIKETHROUGH + "--------------------------------------");
					player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "PixelEvents 1.0");
					player.sendMessage(ChatColor.DARK_AQUA + "/event start" + ChatColor.WHITE + " - Starts the event");
					player.sendMessage(ChatColor.DARK_AQUA + "/event stop" + ChatColor.WHITE + " - Stops the event");
					player.sendMessage(ChatColor.DARK_AQUA + "/event revive <player>" + ChatColor.WHITE + " - Revives an eliminated player");
					player.sendMessage(ChatColor.DARK_AQUA + "/event help" + ChatColor.WHITE + " - Displays this help message");
					player.sendMessage(ChatColor.DARK_AQUA + "Developer: " + ChatColor.BOLD + "Excavator#9612");
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.STRIKETHROUGH + "--------------------------------------");
					return true;
				}
				if(args[0].equalsIgnoreCase("start")) {
					enabled = true;
					total = Bukkit.getOnlinePlayers().size();
					for(Player online : Bukkit.getOnlinePlayers()) {
						createScoreboard(online);
						revives.put(online.getName(), true); //initial
						online.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "The event has begun. Good luck!");
					}
					deaths.clear();
					
					
					return true;
				}
				
				if(args[0].equalsIgnoreCase("stop")) {
					enabled = false;
					total = 0;
					revives.clear();
					deaths.clear();
					for(Player online : Bukkit.getOnlinePlayers())
						online.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
					return true;
					
				}
				if(args[0].equalsIgnoreCase("revive")) {
					
					if(!enabled) { 				//no scoreboard detected
						player.sendMessage(ChatColor.RED + "The game is not running. You can run it using /event start.");
					}
					else {
					if(args.length == 2) {
						
					if(Bukkit.getServer().getPlayerExact(args[1]) != null){ //if player is online
						
						if(revives.get(args[1]) != null) {
							player.sendMessage(ChatColor.RED +  "This player has already been revived!");
						}
							
						
						else { //in not in hashmap or false
						total++;
						revives.put(args[1], true);
						deaths.put(args[1], null);
						for(Player online : Bukkit.getOnlinePlayers())
							createScoreboard(online);
						}
						//end
						for(Player online : Bukkit.getOnlinePlayers())
							createScoreboard(online);
					}
					
					else { 
						player.sendMessage(ChatColor.RED + "Player not found.");
					}
					}
					else {
						player.sendMessage("Usage: /event revive {player}");
					}
				}
					return true;
				}
				else {
					player.sendMessage("Usage: /event <start | stop | help | revive> [player]");
				}
				
			}
		
			return true;
			} //end player execution
		
		}
		
	}
	
		return false;
}
}
