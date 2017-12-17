package me.badbones69.crazyenchantments.enchantments;

import me.badbones69.crazyenchantments.Main;
import me.badbones69.crazyenchantments.ParticleEffect;
import me.badbones69.crazyenchantments.api.CEnchantments;
import me.badbones69.crazyenchantments.api.Version;
import me.badbones69.crazyenchantments.api.events.ArmorEquipEvent;
import me.badbones69.crazyenchantments.api.events.EnchantmentUseEvent;
import me.badbones69.crazyenchantments.multisupport.Support;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Boots implements Listener{
	
	private static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CrazyEnchantments");
	
	public static ArrayList<Player> Flying = new ArrayList<Player>();
	private int time = Integer.MAX_VALUE;
	
	@EventHandler
 	public void onEquip(ArmorEquipEvent e){
		Player player = e.getPlayer();
		ItemStack NewItem = e.getNewArmorPiece();
		ItemStack OldItem = e.getOldArmorPiece();
		if(Main.CE.hasEnchantments(NewItem)){
			if(Main.CE.hasEnchantment(NewItem, CEnchantments.ANTIGRAVITY)){
				if(CEnchantments.ANTIGRAVITY.isEnabled()){
					EnchantmentUseEvent event = new EnchantmentUseEvent(player, CEnchantments.ANTIGRAVITY, NewItem);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()){
						player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, time, 1+Main.CE.getPower(NewItem, CEnchantments.ANTIGRAVITY)));
					}
				}
			}
			if(Main.CE.hasEnchantment(NewItem, CEnchantments.GEARS)){
				if(CEnchantments.GEARS.isEnabled()){
					EnchantmentUseEvent event = new EnchantmentUseEvent(player, CEnchantments.GEARS, NewItem);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()){
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, time, Main.CE.getPower(NewItem, CEnchantments.GEARS)-1));
					}
				}
			}
			if(Main.CE.hasEnchantment(NewItem, CEnchantments.SPRINGS)){
				if(CEnchantments.SPRINGS.isEnabled()){
					EnchantmentUseEvent event = new EnchantmentUseEvent(player, CEnchantments.SPRINGS, NewItem);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()){
						player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, time, Main.CE.getPower(NewItem, CEnchantments.SPRINGS)-1));
					}
				}
			}
			if(Main.CE.hasEnchantment(NewItem, CEnchantments.WINGS)){
				if(CEnchantments.WINGS.isEnabled()){
					if(Support.inTerritory(player)||Support.inWingsRegion(player.getLocation())){
						if(player.getGameMode() != GameMode.CREATIVE){
							if(Version.getVersion().comparedTo(Version.v1_8_R1) >= 1){
								if(player.getGameMode() != GameMode.ADVENTURE){
									player.setAllowFlight(true);
								}
							}else{
								player.setAllowFlight(true);
							}
						}
					}
				}
			}
		}
		if(Main.CE.hasEnchantments(OldItem)){
			if(Main.CE.hasEnchantment(OldItem, CEnchantments.ANTIGRAVITY)){
				if(CEnchantments.ANTIGRAVITY.isEnabled()){
					player.removePotionEffect(PotionEffectType.JUMP);
				}
			}
			if(Main.CE.hasEnchantment(OldItem, CEnchantments.GEARS)){
				if(CEnchantments.GEARS.isEnabled()){
					player.removePotionEffect(PotionEffectType.SPEED);
				}
			}
			if(Main.CE.hasEnchantment(OldItem, CEnchantments.SPRINGS)){
				if(CEnchantments.SPRINGS.isEnabled()){
					player.removePotionEffect(PotionEffectType.JUMP);
				}
			}
			if(Main.CE.hasEnchantment(OldItem, CEnchantments.WINGS)){
				if(CEnchantments.WINGS.isEnabled()){
					if(player.getGameMode()!=GameMode.CREATIVE){
						if(Version.getVersion().comparedTo(Version.v1_8_R1) >= 1){
							if(player.getGameMode() != GameMode.ADVENTURE){
								player.setAllowFlight(false);
							}
						}else{
							player.setAllowFlight(false);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onFly(PlayerToggleFlightEvent e){
		Player player = e.getPlayer();
		ItemStack boots = player.getEquipment().getBoots();
		if(Main.CE.hasEnchantments(boots)){
			if(Main.CE.hasEnchantment(boots, CEnchantments.WINGS)){
				if(CEnchantments.WINGS.isEnabled()){
					if(Support.inTerritory(player) || Support.inWingsRegion(player.getLocation())){
						if(!areEnemiesNearBy(player)){
							if(e.isFlying()){
								e.setCancelled(true);
								player.setFlying(true);
								Flying.add(player);
							}else{
								Flying.remove(player);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		Player player = e.getPlayer();
		ItemStack boots = player.getEquipment().getBoots();
		if(Main.CE.hasEnchantments(boots)){
			if(Main.CE.hasEnchantment(boots, CEnchantments.WINGS)){
				if(CEnchantments.WINGS.isEnabled()){
					if(Support.inTerritory(player) || Support.inWingsRegion(player.getLocation())){
						if(!areEnemiesNearBy(player)){
							if(!player.getAllowFlight()){
								player.setAllowFlight(true);
							}
						}else{
							if(player.isFlying()){
								if(player.getGameMode() != GameMode.CREATIVE){
									if(Version.getVersion().comparedTo(Version.v1_8_R1) >= 1){
										if(player.getGameMode() != GameMode.ADVENTURE){
											player.setFlying(false);
											player.setAllowFlight(false);
											Flying.remove(player);
										}
									}else{
										player.setFlying(false);
										player.setAllowFlight(false);
										Flying.remove(player);
									}
								}
							}
						}
					}else{
						if(player.isFlying()){
							if(player.getGameMode() != GameMode.CREATIVE){
								if(Version.getVersion().comparedTo(Version.v1_8_R1) >= 1){
									if(player.getGameMode() != GameMode.ADVENTURE){
										player.setFlying(false);
										player.setAllowFlight(false);
										Flying.remove(player);
									}
								}else{
									player.setFlying(false);
									player.setAllowFlight(false);
									Flying.remove(player);
								}
							}
						}
					}
					if(player.isFlying()){
						Flying.add(player);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player player = e.getPlayer();
		ItemStack boots = player.getEquipment().getBoots();
		if(Main.CE.hasEnchantments(boots)){
			if(Main.CE.hasEnchantment(boots, CEnchantments.WINGS)){
				if(CEnchantments.WINGS.isEnabled()){
					if(Support.inTerritory(player) || Support.inWingsRegion(player.getLocation())){
						if(!areEnemiesNearBy(player)){
							player.setAllowFlight(true);
							Flying.add(player);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		Player player = e.getPlayer();
		ItemStack boots = player.getEquipment().getBoots();
		if(Main.CE.hasEnchantments(boots)){
			if(Main.CE.hasEnchantment(boots, CEnchantments.WINGS)){
				if(CEnchantments.WINGS.isEnabled()){
					player.setFlying(false);
					player.setAllowFlight(false);
					Flying.remove(player);
				}
			}
		}
	}
	
	public static void onStart(){
		if(Main.settings.getConfig().contains("Settings.EnchantmentOptions.Wings.Clouds")){
			if(Main.settings.getConfig().getBoolean("Settings.EnchantmentOptions.Wings.Clouds")){
				Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
					@Override
					public void run() {
						for(Player player : Bukkit.getServer().getOnlinePlayers()){
							if(Flying.contains(player)){
								Location l = player.getLocation().subtract(0, .25, 0);
								if(player.isFlying()){
									ItemStack boots = player.getEquipment().getBoots();
									if(boots != null){
										if(Main.CE.hasEnchantment(boots, CEnchantments.WINGS)){
											if(CEnchantments.WINGS.isEnabled()){
												ParticleEffect.CLOUD.display((float) .25, (float) 0, (float) .25, 0, 10, l, 100);
											}
										}
									}
								}
							}
						}
					}
				}, 1, 1);
			}
		}
	}
	
	private Boolean areEnemiesNearBy(Player player){
		if(Main.settings.getConfig().getBoolean("Settings.EnchantmentOptions.Wings.Enemy-Toggle")){
			for(Player p : getNearByPlayers(player, Main.settings.getConfig().getInt("Settings.EnchantmentOptions.Wings.Distance"))){
				if(!Support.isFriendly(player, p)){
					return true;
				}
			}
		}
		return false;
	}
	
	private ArrayList<Player> getNearByPlayers(Player player, int radius){
		ArrayList<Player> players = new ArrayList<Player>();
		for(Entity en : player.getNearbyEntities(radius, radius, radius)){
			if(en instanceof Player){
				if((Player)en != player){
					players.add((Player) en);
				}
			}
		}
		return players;
	}
	
}