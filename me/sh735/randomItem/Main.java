package me.sh735.randomItem;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin{

	// Create Timer used for infinite loops
	Timer t = new Timer();
	String pluginPrefix = ChatColor.AQUA + "[randomItem]";
	
	// Check if user has a free space in his inventory function
	public boolean hasAvaliableSlot(Player player){
	    Inventory inv = player.getInventory();
	    for (ItemStack item: inv.getContents()) {
	         if(item == null) {
	                 return true;
	         }
	     }
	return false;
	}
	
	// On plugin enable
	@Override
	public void onEnable() {
		
		System.out.println("randomItem enabled.");
		t = new Timer();
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			p.sendMessage(pluginPrefix + ChatColor.GREEN + " Plugin enabled.");
		}
	}
	
	// On plugin disable
	@Override
	public void onDisable() {
		// Shutdown, reloads, plugin reloads
		System.out.println("randomItem disabled.");
		t.cancel();
		t.purge();
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			p.sendMessage(pluginPrefix + ChatColor.RED + " Plugin disabled. Turned off all timers.");
		}
	}
	
	// On command execution
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Turn on random item giving for all players
		if(label.equalsIgnoreCase("randon")) {
			// Check if player, create player variable
			Player player = null;
			if (sender instanceof Player) {
				player = (Player) sender;
			}
				// Check perms or if console
				if (sender instanceof Player == false || player.hasPermission("randomitem.admin")) {
					// If no argument it autos to 10 seconds
					int milliseconds = 10000;
					if (args.length > 0) {
						// Convert seconds to milliseconds
						milliseconds = Integer.parseInt(args[0]) * 1000;
					}

					// Loop task
					TimerTask task = new TimerTask() {
					    @Override
					    public void run() {
					    	// Get all players and give them all random item.
					    	for(Player p : Bukkit.getServer().getOnlinePlayers()) {
					    		// Check if player has full inventory
					    		if (p.getInventory().firstEmpty() == -1) {
					    			return;
					    		}
					    			
					    		// Get random item from Material.values()
					    		Random generator = new Random();
					    		int randomIndex = generator.nextInt(Material.values().length);
					    		ItemStack Item = new ItemStack(Material.values()[randomIndex]);
					    		// Give player a random item
					    		p.getInventory().addItem(Item);
					    	}
					    	
					    }
					};
					// Create infinite loop
					t.schedule(task, 0, milliseconds);
					String msg = pluginPrefix + ChatColor.GREEN + " Turned on for every " + milliseconds/1000 + " seconds.";
					if(sender instanceof Player) {
						player.sendMessage(msg);
						return true;
					}else {
						sender.sendMessage(msg);
						return true;
					}

				}
				player.sendMessage( pluginPrefix + ChatColor.RED + " Missing permission randomitem.admin.");
				return true;
			

		}
		
		// Turn off random item giving for all players
		if(label.equalsIgnoreCase("randoff")) {
			Player player = null;
			if(sender instanceof Player) {
				player = (Player) sender;
			}
			if (sender instanceof Player == false || player.hasPermission("randomitem.admin")) {
				
					t.cancel();
					t.purge();
					t = new Timer();
					player.sendMessage(pluginPrefix + ChatColor.RED + " Turned off all timers.");
					return true;

			}
			player.sendMessage(pluginPrefix + ChatColor.RED + " Missing permission randomitem.admin.");
			return true;
		}
		return false;
	}
	

}
