package me.badbones69.crazyenchantments.controlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.badbones69.crazyenchantments.Main;
import me.badbones69.crazyenchantments.Methods;
import me.badbones69.crazyenchantments.api.CEnchantments;
import me.badbones69.crazyenchantments.api.Version;
import me.badbones69.crazyenchantments.api.currencyapi.Currency;
import me.badbones69.crazyenchantments.api.currencyapi.CurrencyAPI;
import me.badbones69.crazyenchantments.multisupport.EnchantGlow;

public class BlackSmith implements Listener{
	
	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CrazyEnchantments");

	public static void openBlackSmith(Player player){
		Inventory inv = Bukkit.createInventory(null, 27, Methods.color(Main.settings.getConfig().getString("Settings.BlackSmith.GUIName")));
		List<Integer> other = new ArrayList<Integer>();
		List<Integer> result = new ArrayList<Integer>();
		other.add(1);other.add(2);other.add(3);other.add(4);other.add(5);other.add(6);
		other.add(10);other.add(12);other.add(13);other.add(15);
		other.add(19);other.add(20);other.add(21);other.add(22);other.add(23);other.add(24);
		result.add(7);result.add(8);result.add(9);
		result.add(16);;result.add(18);
		result.add(25);result.add(26);result.add(27);
		for(int i:other)inv.setItem(i-1, Methods.makeItem(Material.STAINED_GLASS_PANE, 1, 7, " "));
		for(int i:result)inv.setItem(i-1, Methods.makeItem(Material.STAINED_GLASS_PANE, 1, 14, " "));
		if(Version.getVersion().getVersionInteger()<181){
			ItemStack item = Methods.makeItem(Material.STAINED_CLAY, 1, 0, Main.settings.getConfig().getString("Settings.BlackSmith.Results.None"));
			if(Main.settings.getConfig().contains("Settings.BlackSmith.Results.Not-Found-Lore")){
				for(String line : Main.settings.getConfig().getStringList("Settings.BlackSmith.Results.Not-Found-Lore")){
					item = Methods.addLore(item, line);
				}
			}
			inv.setItem(16, item);
		}else{
			ItemStack item = Methods.makeItem(Material.BARRIER, 1, 0, Main.settings.getConfig().getString("Settings.BlackSmith.Results.None"));
			if(Main.settings.getConfig().contains("Settings.BlackSmith.Results.Not-Found-Lore")){
				for(String line : Main.settings.getConfig().getStringList("Settings.BlackSmith.Results.Not-Found-Lore")){
					item = Methods.addLore(item, line);
				}
			}
			inv.setItem(16, item);
		}
		player.openInventory(inv);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e){
		List<Integer> result = new ArrayList<Integer>();
		result.add(7);result.add(8);result.add(9);
		result.add(16);;result.add(18);
		result.add(25);result.add(26);result.add(27);
		Player player=(Player)e.getWhoClicked();
		Inventory inv = e.getInventory();
		FileConfiguration config = Main.settings.getConfig();
		if(inv!=null){
			if(inv.getName().equals(Methods.color(config.getString("Settings.BlackSmith.GUIName")))){
				e.setCancelled(true);
				if(e.getCurrentItem()!=null){
					ItemStack item = e.getCurrentItem();
					if(!inBlackSmith(e.getRawSlot())){// Click In Players Inventory
						if(item.getAmount()!=1)return;
						if(Main.CE.hasEnchantments(item) || item.getType() == Main.CE.getEnchantmentBookItem().getType()){
							if(item.getType() == Main.CE.getEnchantmentBookItem().getType()){//Is a book.
								if(!item.hasItemMeta())return;
								if(!item.getItemMeta().hasDisplayName())return;
								boolean T=false;
								for(CEnchantments en : Main.CE.getEnchantments()){
									if(item.getItemMeta().getDisplayName().startsWith(en.getBookColor() + en.getCustomName())){
										T = true;
									}
								}
								for(String en : Main.CustomE.getEnchantments()){
									if(item.getItemMeta().getDisplayName().startsWith(Main.CustomE.getBookColor(en) + Main.CustomE.getCustomName(en))){
										T = true;
									}
								}
								if(!T){
									return;
								}
							}
							if(inv.getItem(10) == null){
								e.setCurrentItem(new ItemStack(Material.AIR));
								inv.setItem(10, item);
								playClick(player);
								if(inv.getItem(13) != null){
									if(getUpgradeCost(inv.getItem(10), inv.getItem(13)) > 0){
										inv.setItem(16, Methods.addLore(getUpgradedItem(inv.getItem(10), inv.getItem(13), true),
												config.getString("Settings.BlackSmith.Results.Found")
												.replaceAll("%Cost%", getUpgradeCost(inv.getItem(10), inv.getItem(13))+"").replaceAll("%cost%", getUpgradeCost(inv.getItem(10), inv.getItem(13))+"")));
										for(int i:result)inv.setItem(i-1, Methods.makeItem(Material.STAINED_GLASS_PANE, 1, 5, " "));
									}else{
										if(Version.getVersion().getVersionInteger()<181){
											ItemStack it = Methods.makeItem(Material.STAINED_CLAY, 1, 0, config.getString("Settings.BlackSmith.Results.None"));
											if(config.contains("Settings.BlackSmith.Results.Not-Found-Lore")){
												for(String line : config.getStringList("Settings.BlackSmith.Results.Not-Found-Lore")){
													it = Methods.addLore(it, line);
												}
											}
											inv.setItem(16, it);
										}else{
											ItemStack it = Methods.makeItem(Material.BARRIER, 1, 0, config.getString("Settings.BlackSmith.Results.None"));
											if(config.contains("Settings.BlackSmith.Results.Not-Found-Lore")){
												for(String line : config.getStringList("Settings.BlackSmith.Results.Not-Found-Lore")){
													it = Methods.addLore(it, line);
												}
											}
											inv.setItem(16, it);
										}
										for(int i:result)inv.setItem(i-1, Methods.makeItem(Material.STAINED_GLASS_PANE, 1, 14, " "));
									}
								}
							}else{
								e.setCurrentItem(new ItemStack(Material.AIR));
								if(inv.getItem(13) != null){
									e.setCurrentItem(inv.getItem(13));
								}
								inv.setItem(13, item);
								playClick(player);
								if(getUpgradeCost(inv.getItem(10), inv.getItem(13)) > 0){
									inv.setItem(16, Methods.addLore(getUpgradedItem(inv.getItem(10), inv.getItem(13), true),
											config.getString("Settings.BlackSmith.Results.Found")
											.replaceAll("%Cost%", getUpgradeCost(inv.getItem(10), inv.getItem(13))+"").replaceAll("%cost%", getUpgradeCost(inv.getItem(10), inv.getItem(13))+"")));
									for(int i:result)inv.setItem(i-1, Methods.makeItem(Material.STAINED_GLASS_PANE, 1, 5, " "));
								}else{
									if(Version.getVersion().getVersionInteger()<181){
										ItemStack it = Methods.makeItem(Material.STAINED_CLAY, 1, 0, config.getString("Settings.BlackSmith.Results.None"));
										if(config.contains("Settings.BlackSmith.Results.Not-Found-Lore")){
											for(String line : config.getStringList("Settings.BlackSmith.Results.Not-Found-Lore")){
												it = Methods.addLore(it, line);
											}
										}
										inv.setItem(16, it);
									}else{
										ItemStack it = Methods.makeItem(Material.BARRIER, 1, 0, config.getString("Settings.BlackSmith.Results.None"));
										if(config.contains("Settings.BlackSmith.Results.Not-Found-Lore")){
											for(String line : config.getStringList("Settings.BlackSmith.Results.Not-Found-Lore")){
												it = Methods.addLore(it, line);
											}
										}
										inv.setItem(16, it);
									}
									for(int i:result)inv.setItem(i-1, Methods.makeItem(Material.STAINED_GLASS_PANE, 1, 14, " "));
								}
							}
						}
					}else{// Click In the Black Smith
						if(e.getRawSlot()==10||e.getRawSlot()==13){
							e.setCurrentItem(new ItemStack(Material.AIR));
							player.getInventory().addItem(item);
							if(Version.getVersion().getVersionInteger()<181){
								ItemStack it = Methods.makeItem(Material.STAINED_CLAY, 1, 0, config.getString("Settings.BlackSmith.Results.None"));
								if(config.contains("Settings.BlackSmith.Results.Not-Found-Lore")){
									for(String line : config.getStringList("Settings.BlackSmith.Results.Not-Found-Lore")){
										it = Methods.addLore(it, line);
									}
								}
								inv.setItem(16, it);
							}else{
								ItemStack it = Methods.makeItem(Material.BARRIER, 1, 0, config.getString("Settings.BlackSmith.Results.None"));
								if(config.contains("Settings.BlackSmith.Results.Not-Found-Lore")){
									for(String line : config.getStringList("Settings.BlackSmith.Results.Not-Found-Lore")){
										it = Methods.addLore(it, line);
									}
								}
								inv.setItem(16, it);
							}
							for(int i:result)inv.setItem(i-1, Methods.makeItem(Material.STAINED_GLASS_PANE, 1, 14, " "));
							playClick(player);
						}
						if(e.getRawSlot()==16){
							if(inv.getItem(10)!=null&&inv.getItem(13)!=null){
								if(getUpgradeCost(inv.getItem(10), inv.getItem(13))>0){
									int cost = getUpgradeCost(inv.getItem(10), inv.getItem(13));
									if(player.getGameMode()!=GameMode.CREATIVE){
										if(Currency.isCurrency(config.getString("Settings.BlackSmith.Transaction.Currency"))){
											Currency currency = Currency.getCurrency(config.getString("Settings.BlackSmith.Transaction.Currency"));
											if(CurrencyAPI.canBuy(player, currency, cost)){
												CurrencyAPI.takeCurrency(player, currency, cost);
											}else{
												String needed = (cost - CurrencyAPI.getCurrency(player, currency)) + "";
												switch(currency){
													case VAULT:
														player.sendMessage(Methods.getPrefix() + Methods.color(Main.settings.getMsg().getString("Messages.Need-More-Money")
																.replace("%Money_Needed%", needed).replace("%money_needed%", needed)));
														break;
													case XP_LEVEL:
														player.sendMessage(Methods.getPrefix() + Methods.color(Main.settings.getMsg().getString("Messages.Need-More-XP-Lvls")
																.replace("%XP%", needed).replace("%xp%", needed)));
														break;
													case XP_TOTAL:
														player.sendMessage(Methods.getPrefix() + Methods.color(Main.settings.getMsg().getString("Messages.Need-More-Total-XP")
																.replace("%XP%", needed).replace("%xp%", needed)));
														break;
												}
												return;
											}
										}
									}
									player.getInventory().addItem(getUpgradedItem(inv.getItem(10),inv.getItem(13), false));
									inv.setItem(10, new ItemStack(Material.AIR));
									inv.setItem(13, new ItemStack(Material.AIR));
									try{
										if(Version.getVersion().getVersionInteger()>=191){
											player.playSound(player.getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 1, 1);
										}else{
											player.playSound(player.getLocation(), Sound.valueOf("LEVEL_UP"), 1, 1);
										}
									}catch(Exception ex){}
									if(Version.getVersion().getVersionInteger()<181){
										ItemStack it = Methods.makeItem(Material.STAINED_CLAY, 1, 0, config.getString("Settings.BlackSmith.Results.None"));
										if(config.contains("Settings.BlackSmith.Results.Not-Found-Lore")){
											for(String line : config.getStringList("Settings.BlackSmith.Results.Not-Found-Lore")){
												it = Methods.addLore(it, line);
											}
										}
										inv.setItem(16, it);
									}else{
										ItemStack it = Methods.makeItem(Material.BARRIER, 1, 0, config.getString("Settings.BlackSmith.Results.None"));
										if(config.contains("Settings.BlackSmith.Results.Not-Found-Lore")){
											for(String line : config.getStringList("Settings.BlackSmith.Results.Not-Found-Lore")){
												it = Methods.addLore(it, line);
											}
										}
										inv.setItem(16, it);
									}
									for(int i:result)inv.setItem(i-1, Methods.makeItem(Material.STAINED_GLASS_PANE, 1, 14, " "));
								}else{
									try{
										if(Version.getVersion().getVersionInteger() >= 191){
											player.playSound(player.getLocation(), Sound.valueOf("ENTITY_VILLAGER_NO"), 1, 1);
										}
										if(Version.getVersion().getVersionInteger() < 191){
											player.playSound(player.getLocation(), Sound.valueOf("VILLAGER_NO"), 1, 1);
										}
									}catch(Exception ex){}
								}
							}else{
								try{
									if(Version.getVersion().getVersionInteger() >= 191){
										player.playSound(player.getLocation(), Sound.valueOf("ENTITY_VILLAGER_NO"), 1, 1);
									}
									if(Version.getVersion().getVersionInteger() < 191){
										player.playSound(player.getLocation(), Sound.valueOf("VILLAGER_NO"), 1, 1);
									}
								}catch(Exception ex){}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInvClose(final InventoryCloseEvent e){
		final Inventory inv = e.getInventory();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if(inv!=null){
					if(inv.getName().equals(Methods.color(Main.settings.getConfig().getString("Settings.BlackSmith.GUIName")))){
						List<Integer> slots = new ArrayList<Integer>();
						slots.add(10);slots.add(13);
						Boolean dead = e.getPlayer().isDead();
						for(int slot : slots){
							if(inv.getItem(slot)!=null){
								if(inv.getItem(slot).getType()!=Material.AIR){
									if(dead){
										e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), inv.getItem(slot));
									}else{
										if(Methods.isInvFull(((Player)e.getPlayer()))){
											e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), inv.getItem(slot));
										}else{
											e.getPlayer().getInventory().addItem(inv.getItem(slot));
										}
									}
								}
							}
						}
						inv.clear();
					}
				}
			}
		}, 0);
	}
	
	private ItemStack getUpgradedItem(ItemStack master, ItemStack sub, boolean glowing){
		ItemStack item = master.clone();
		if(master.getType() == Main.CE.getEnchantmentBookItem().getType() && sub.getType() == Main.CE.getEnchantmentBookItem().getType()){
			if(Methods.removeColor(master.getItemMeta().getDisplayName()).equalsIgnoreCase(Methods.removeColor(sub.getItemMeta().getDisplayName()))){
				for(CEnchantments en : Main.CE.getEnchantments()){
					if(master.getItemMeta().getDisplayName().startsWith(en.getBookColor() + en.getCustomName())){
						int power = Main.CE.getBookPower(master, en);
						int max = Main.settings.getEnchs().getInt("Enchantments." + en.getName() + ".MaxPower");
						if(power + 1 <= max){
							item = addGlow(Methods.makeItem(Main.settings.getConfig().getString("Settings.Enchantment-Book-Item"), 1, en.getBookColor() + en.getCustomName() + " " + Methods.getPower(power+1), master.getItemMeta().getLore()), glowing);
						}
					}
				}
				for(String en : Main.CustomE.getEnchantments()){
					if(master.getItemMeta().getDisplayName().startsWith(Main.CustomE.getBookColor(en) + Main.CustomE.getCustomName(en))){
						int power = Main.CustomE.getBookPower(master, en);
						int max = Main.settings.getCustomEnchs().getInt("Enchantments." + en + ".MaxPower");
						if(power + 1 <= max){
							item = addGlow(Methods.makeItem(Main.settings.getConfig().getString("Settings.Enchantment-Book-Item"), 1, Main.CustomE.getBookColor(en) + Main.CustomE.getCustomName(en) + " " + Methods.getPower(power+1), master.getItemMeta().getLore()), glowing);
						}
					}
				}
			}
		}
		if(master.getType() != Main.CE.getEnchantmentBookItem().getType() && sub.getType() != Main.CE.getEnchantmentBookItem().getType()){
			List<String> d = new ArrayList<String>();//Dup Enchantments
			List<String> n = new ArrayList<String>();//New Enchantments
			List<String> m = new ArrayList<String>();//Master's Enchantments
			List<String> s = new ArrayList<String>();//Sub's Enchantments
			for(CEnchantments en : Main.CE.getEnchantments()){
				for(String lore : master.getItemMeta().getLore()){
					if(lore.startsWith(en.getEnchantmentColor() + en.getCustomName())){
						m.add(lore);
					}
				}
				for(String lore : sub.getItemMeta().getLore()){
					if(lore.startsWith(en.getEnchantmentColor() + en.getCustomName())){
						s.add(lore);
					}
				}
			}
			for(String en : Main.CustomE.getEnchantments()){
				for(String lore : master.getItemMeta().getLore()){
					if(lore.startsWith(Main.CustomE.getEnchantmentColor(en) + Main.CustomE.getCustomName(en))){
						m.add(lore);
					}
				}
				for(String lore : sub.getItemMeta().getLore()){
					if(lore.startsWith(Main.CustomE.getEnchantmentColor(en) + Main.CustomE.getCustomName(en))){
						s.add(lore);
					}
				}
			}
			for(String l : s){
				boolean T = false;
				for(String lore : m){
					if(l.contains(Methods.removeColor(lore))){
						T = true;
						d.add(l);
					}
				}
				if(!T){
					n.add(l);
				}
			}
			for(String l : d){
				for(CEnchantments en : Main.CE.getEnchantments()){
					if(l.startsWith(en.getEnchantmentColor() + en.getCustomName())){
						int power = Main.CE.getPower(item, en);
						int max = Main.settings.getEnchs().getInt("Enchantments." + en.getName() + ".MaxPower");
						if(power + 1 <= max){
							item = Methods.replaceLore(item, l,en.getEnchantmentColor() + en.getCustomName() + " " + Methods.getPower(power+1));
						}
					}
				}
			}
			for(String l : d){
				for(String en : Main.CustomE.getEnchantments()){
					if(l.startsWith(Main.CustomE.getEnchantmentColor(en) + Main.CustomE.getCustomName(en))){
						int power = Main.CustomE.getPower(item, en);
						int max = Main.settings.getCustomEnchs().getInt("Enchantments." + en + ".MaxPower");
						if(power + 1 <= max){
							item = Methods.replaceLore(item, l, Main.CustomE.getEnchantmentColor(en) + Main.CustomE.getCustomName(en) + " " + Methods.getPower(power+1));
						}
					}
				}
			}
			for(String lore : n){
				boolean T = false;
				for(CEnchantments en : Main.CE.getEnchantments()){
					if(lore.startsWith(en.getEnchantmentColor() + en.getCustomName())){
						for(String l : item.getItemMeta().getLore()){
							if(l.startsWith(en.getEnchantmentColor() + en.getCustomName())){
								T = true;
							}
						}
					}
				}
				for(String en : Main.CustomE.getEnchantments()){
					if(lore.startsWith(Main.CustomE.getEnchantmentColor(en) + Main.CustomE.getCustomName(en))){
						for(String l : item.getItemMeta().getLore()){
							if(l.startsWith(Main.CustomE.getEnchantmentColor(en) + Main.CustomE.getCustomName(en))){
								T = true;
							}
						}
					}
				}
				if(!T){
					int maxEnchants = Main.settings.getConfig().getInt("Settings.EnchantmentOptions.MaxAmountOfEnchantments");
					if(Main.settings.getConfig().getBoolean("Settings.EnchantmentOptions.MaxAmountOfEnchantmentsToggle")){
						if(Methods.getEnchAmount(item)<maxEnchants){
							item = Methods.addLore(item, lore);
						}
					}
				}
			}
		}
		return item;
	}
	
	private int getUpgradeCost(ItemStack master, ItemStack sub){
		int total = 0;
		if(master.getType() == Main.CE.getEnchantmentBookItem().getType() && sub.getType()==Main.CE.getEnchantmentBookItem().getType()){
			if(Methods.removeColor(master.getItemMeta().getDisplayName()).equalsIgnoreCase(Methods.removeColor(sub.getItemMeta().getDisplayName()))){
				for(CEnchantments en : Main.CE.getEnchantments()){
					if(master.getItemMeta().getDisplayName().startsWith(en.getBookColor() + en.getCustomName())){
						int power = Main.CE.getBookPower(master, en);
						int max = Main.settings.getEnchs().getInt("Enchantments."+en.getName()+".MaxPower");
						if(power + 1 <= max){
							total += Main.settings.getConfig().getInt("Settings.BlackSmith.Transaction.Costs.Book-Upgrade");
						}
					}
				}
				for(String en : Main.CustomE.getEnchantments()){
					if(master.getItemMeta().getDisplayName().startsWith(Main.CustomE.getBookColor(en) + Main.CustomE.getCustomName(en))){
						int power = Main.CustomE.getBookPower(master, en);
						int max = Main.settings.getCustomEnchs().getInt("Enchantments." + en + ".MaxPower");
						if(power + 1 <= max){
							total += Main.settings.getConfig().getInt("Settings.BlackSmith.Transaction.Costs.Book-Upgrade");
						}
					}
				}
			}
		}
		if(master.getType() != Main.CE.getEnchantmentBookItem().getType() || sub.getType() != Main.CE.getEnchantmentBookItem().getType()){
			ItemStack item = master.clone();
			List<String> d = new ArrayList<String>();//Dup Enchantments
			List<String> n = new ArrayList<String>();//New Enchantments
			List<String> m = new ArrayList<String>();//Master's Enchantments
			List<String> s = new ArrayList<String>();//Sub's Enchantments
			for(CEnchantments en : Main.CE.getEnchantments()){
				for(String lore : master.getItemMeta().getLore()){
					if(lore.startsWith(en.getBookColor() + en.getCustomName())){
						m.add(lore);
					}
				}
				for(String lore : sub.getItemMeta().getLore()){
					if(lore.startsWith(en.getBookColor() + en.getCustomName())){
						s.add(lore);
					}
				}
			}
			for(String en : Main.CustomE.getEnchantments()){
				for(String lore : master.getItemMeta().getLore()){
					if(lore.startsWith(Main.CustomE.getEnchantmentColor(en) + Main.CustomE.getCustomName(en))){
						m.add(lore);
					}
				}
				for(String lore : sub.getItemMeta().getLore()){
					if(lore.startsWith(Main.CustomE.getEnchantmentColor(en) + Main.CustomE.getCustomName(en))){
						s.add(lore);
					}
				}
			}
			for(String l : s){
				boolean T=false;
				for(String lore : m){
					if(l.contains(Methods.removeColor(lore))){
						T = true;
						d.add(l);
					}
				}
				if(!T){
					n.add(l);
				}
			}
			for(String l : d){
				for(CEnchantments en : Main.CE.getEnchantments()){
					if(l.startsWith(en.getEnchantmentColor() + en.getCustomName())){
						int power = Main.CE.getPower(item, en);
						int max = Main.settings.getEnchs().getInt("Enchantments." + en.getName() + ".MaxPower");
						if(power + 1 <= max){
							item = Methods.replaceLore(item, l, en.getEnchantmentColor() + en.getCustomName() + " " + Methods.getPower(power+1));
							total += Main.settings.getConfig().getInt("Settings.BlackSmith.Transaction.Costs.Power-Up");
						}
					}
				}
				for(String en : Main.CustomE.getEnchantments()){
					if(l.startsWith(Main.CustomE.getEnchantmentColor(en) + Main.CustomE.getCustomName(en))){
						int power = Main.CustomE.getPower(item, en);
						int max = Main.settings.getCustomEnchs().getInt("Enchantments." + en + ".MaxPower");
						if(power + 1 <= max){
							item = Methods.replaceLore(item, l, Main.CustomE.getEnchantmentColor(en) + Main.CustomE.getCustomName(en) + " " + Methods.getPower(power+1));
							total += Main.settings.getConfig().getInt("Settings.BlackSmith.Transaction.Costs.Power-Up");
						}
					}
				}
			}
			for(String lore : n){
				boolean T = false;
				for(CEnchantments en : Main.CE.getEnchantments()){
					if(lore.startsWith(en.getEnchantmentColor() + en.getCustomName())){
						for(String l : item.getItemMeta().getLore()){
							if(l.startsWith(en.getEnchantmentColor() + en.getCustomName())){
								T = true;
							}
						}
					}
				}
				for(String en : Main.CustomE.getEnchantments()){
					if(lore.startsWith(Main.CustomE.getEnchantmentColor(en) + Main.CustomE.getCustomName(en))){
						for(String l : item.getItemMeta().getLore()){
							if(l.startsWith(Main.CustomE.getEnchantmentColor(en) + Main.CustomE.getCustomName(en))){
								T = true;
							}
						}
					}
				}
				if(!T){
					int maxEnchants = Main.settings.getConfig().getInt("Settings.EnchantmentOptions.MaxAmountOfEnchantments");
					if(Main.settings.getConfig().getBoolean("Settings.EnchantmentOptions.MaxAmountOfEnchantmentsToggle")){
						if(Methods.getEnchAmount(item) < maxEnchants){
							total += Main.settings.getConfig().getInt("Settings.BlackSmith.Transaction.Costs.Add-Enchantment");
						}
					}
				}
			}
		}
		return total;
	}
	
	private static ItemStack addGlow(ItemStack item, boolean toggle) {
		if(toggle){
			return EnchantGlow.addGlow(item);
		}else{
			return Methods.addGlow(item);
		}
    }
	
	private boolean inBlackSmith(int slot){
		//The last slot in the tinker is 54
		if(slot<27)return true;
		return false;
	}
	
	private void playClick(Player player){
		try{
			if(Version.getVersion().getVersionInteger() >= 191)player.playSound(player.getLocation(), Sound.valueOf("UI_BUTTON_CLICK"), 1, 1);
			if(Version.getVersion().getVersionInteger() < 191)player.playSound(player.getLocation(), Sound.valueOf("CLICK"), 1, 1);
		}catch(Exception ex){}
	}
	
}