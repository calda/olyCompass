package com.olympuspvp.compass;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


public class olyCompass extends JavaPlugin{

	final protected static String tag = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "olyCompass" + ChatColor.DARK_GRAY + "] " + ChatColor.RED;
	protected static olyCompass compass;
	
	@Override
	public void onEnable(){
		loadRecipes();
		System.out.println("[olyCompass] Recipes loaded");
		InteractionListener il = new InteractionListener();
		Bukkit.getPluginManager().registerEvents(il, this);
		olyCompass.compass = this;
	}
	
	public void loadRecipes(){
		Bukkit.addRecipe(new ShapelessRecipe(getEnchantedCompass(1)).addIngredient(1, Material.DIAMOND).addIngredient(Material.COMPASS));
		Bukkit.addRecipe(new ShapelessRecipe(getEnchantedCompass(2)).addIngredient(2, Material.DIAMOND).addIngredient(Material.COMPASS));
		Bukkit.addRecipe(new ShapelessRecipe(getEnchantedCompass(3)).addIngredient(3, Material.DIAMOND).addIngredient(Material.COMPASS));
		Bukkit.addRecipe(new ShapelessRecipe(getEnchantedCompass(4)).addIngredient(4, Material.DIAMOND).addIngredient(Material.COMPASS));
		Bukkit.addRecipe(new ShapelessRecipe(getEnchantedCompass(5)).addIngredient(5, Material.DIAMOND).addIngredient(Material.COMPASS));
		Bukkit.addRecipe(new ShapelessRecipe(getEnchantedCompass(6)).addIngredient(6, Material.DIAMOND).addIngredient(Material.COMPASS));
		Bukkit.addRecipe(new ShapelessRecipe(getEnchantedCompass(7)).addIngredient(7, Material.DIAMOND).addIngredient(Material.COMPASS));
		Bukkit.addRecipe(new ShapelessRecipe(getEnchantedCompass(8)).addIngredient(8, Material.DIAMOND).addIngredient(Material.COMPASS));
	}
	
	public ItemStack getEnchantedCompass(int numberOfTracks){
		ItemStack item = new ItemStack(Material.COMPASS, 1);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Number of tracks remaining: " + numberOfTracks * 3);
		lore.add("Compass not attuned");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.ARROW_INFINITE, 3, true);
		meta.setDisplayName("Unattuned Tracking Compass");
		item.setItemMeta(meta);
		return item;
	}
	
}
