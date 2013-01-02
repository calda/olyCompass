package com.olympuspvp.compass;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class InteractionListener implements Listener{

	List<String> tracking = new ArrayList<String>();
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent e){
		if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		final Player p = e.getPlayer();
		final ItemStack hand = p.getItemInHand();
		if(hand.getType() != Material.COMPASS) return;
		ItemMeta meta = hand.getItemMeta();
		if(meta == null) return;
		if(!meta.hasLore()) return;
		List<String> lore = meta.getLore();
		if(tracking.contains(p.getName())){
			p.sendMessage(olyCompass.tag + "Your compass is already powered on.");
			return;
		}
		String parse = lore.get(1);
		String[] parsed = parse.split(":");
		if(parsed.length == 1){
			p.sendMessage(olyCompass.tag + "Your must attune your compass first.");
			p.sendMessage(olyCompass.tag + "Cause damage to a person to link it to them.");
			return;
		}String name = parsed[1].substring(1);
		final Player pto = Bukkit.getPlayerExact(name);
		final Location oldCompass = p.getCompassTarget();
		if(pto == null){
			p.sendMessage(olyCompass.tag + "That player is not online.");
			return;
		}if(pto.getWorld() != p.getWorld()){
			p.sendMessage(olyCompass.tag + "That player is not currently in the same world as you.");
		}p.setCompassTarget(pto.getLocation());
		String tracksUnparsed = lore.get(0);
		String[] tracksParsed = tracksUnparsed.split(":");
		String trackstr = tracksParsed[1].substring(1);
		int tracks = 1;
		try{ tracks = Integer.parseInt(trackstr);
		}catch(Exception exe){}
		tracks -= 1;
		final int finaltracks = tracks;
		List<String> newLore = new ArrayList<String>();
		newLore.add("Number of tracks remaining: " + tracks);
		newLore.add(lore.get(1));
		meta.setLore(newLore);
		hand.setItemMeta(meta);
		p.setItemInHand(hand);
		tracking.add(p.getName());
		p.sendMessage(olyCompass.tag + "Now tracking " + ChatColor.DARK_RED + pto.getName());
		for(long i = 1; i < 60; i++){
			Bukkit.getScheduler().scheduleSyncDelayedTask(olyCompass.compass, new Runnable(){
				
				@Override
				public void run(){
					p.setCompassTarget(pto.getLocation());
				}
				
			}, i * 5L);
		}Bukkit.getScheduler().scheduleSyncDelayedTask(olyCompass.compass, new Runnable(){
			
			@Override
			public void run(){
				p.setCompassTarget(oldCompass);
				p.sendMessage(olyCompass.tag + "Your compass has powered off.");
				tracking.remove(p.getName());
				if(finaltracks <= 0){
					hand.setAmount(hand.getAmount() - 1);
					if(hand.getAmount() <= 0) p.setItemInHand(new ItemStack(Material.COMPASS, 1));
					else p.setItemInHand(hand);
				}
			}
			
		}, 16 * 20L);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerDamageOther(EntityDamageByEntityEvent e){
		if(e.isCancelled()) return;
		if(!(e.getDamager() instanceof Player)) return;
		Player damager = (Player) e.getDamager();
		ItemStack hand = damager.getItemInHand();
		if(hand.getType() != Material.COMPASS) return;
		ItemMeta meta = hand.getItemMeta();
		if(meta == null) return;
		if(!meta.hasLore()) return;
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		List<String> lore = meta.getLore();
		List<String> newLore = new ArrayList<String>();
		newLore.add(lore.get(0));
		newLore.add("Compass attuned to: " + p.getName());
		meta.setLore(newLore);
		meta.setDisplayName("Tracking Compass (" + p.getName() + ")");
		hand.setItemMeta(meta);
		damager.setItemInHand(hand);
		damager.sendMessage(olyCompass.tag + "Your compass is now attuned to " + ChatColor.DARK_RED + p.getName());
	}
	
}
