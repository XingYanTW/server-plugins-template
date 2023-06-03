package mc.xingyan.xycore.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class UPGRADEABLE_SWORD implements Listener {

    static ItemStack item = null;

    public ItemStack get(String rarity){
        ItemMeta im;
        List<String> lore;
        switch (rarity){
            case "STONE":
                item = new ItemStack(Material.STONE_SWORD);
                im = item.getItemMeta();
                lore = new ArrayList<>();
                lore.add(ChatColor.YELLOW+"Next Upgrade:"+ChatColor.WHITE+" Wooden Sword");
                lore.add(ChatColor.YELLOW+"Need exp: (0/50)");
                im.setLore(lore);
                item.setItemMeta(im);
                break;
            case "GOLDEN":
                item = new ItemStack(Material.GOLD_SWORD);
                im = item.getItemMeta();
                lore = new ArrayList<>();
                lore.add(ChatColor.YELLOW+"Next Upgrade:"+ChatColor.WHITE+" Iron Sword");
                lore.add(ChatColor.YELLOW+"Need exp: (0/80)");
                im.setLore(lore);
                item.setItemMeta(im);
                break;
            case "IRON":
                item = new ItemStack(Material.IRON_SWORD);
                im = item.getItemMeta();
                lore = new ArrayList<>();
                lore.add(ChatColor.YELLOW+"Next Upgrade:"+ChatColor.WHITE+" Diamond Sword");
                lore.add(ChatColor.YELLOW+"Need exp: (0/5000)");
                im.setLore(lore);
                item.setItemMeta(im);
                break;
            case "DIAMOND":
                item = new ItemStack(Material.DIAMOND_SWORD);
                im = item.getItemMeta();
                lore = new ArrayList<>();
                lore.add(ChatColor.YELLOW+"Next Upgrade:"+ChatColor.WHITE+" Netherite Sword");
                lore.add(ChatColor.YELLOW+"Need exp: (0/50000)");
                im.setLore(lore);
                item.setItemMeta(im);
                break;
            default:
                item = new ItemStack(Material.WOOD_SWORD);
                im = item.getItemMeta();
                lore = new ArrayList<>();
                lore.add(ChatColor.YELLOW+"Next Upgrade:"+ChatColor.WHITE+" Stone Sword");
                lore.add(ChatColor.YELLOW+"Need exp: (0/10)");
                im.setLore(lore);
                item.setItemMeta(im);
                break;
        }
        return item;
    }

    @EventHandler
    public void ongetexp(PlayerExpChangeEvent event){
        Player player = event.getPlayer();
        Inventory inv = player.getInventory();
        ItemStack item = player.getItemInHand();

        //swords
        if(item.getType().equals(Material.WOOD_SWORD)){
            ItemMeta itemMeta = item.getItemMeta();
            List<String> lore = itemMeta.getLore();
            if(lore.get(0).equals(ChatColor.YELLOW+"Next Upgrade:"+ChatColor.WHITE+" Stone Sword")){
                String loreline2 = lore.get(1).replace(ChatColor.YELLOW+"Need exp: (", "").replace("/10)", "");
                int exp = Integer.parseInt(loreline2);
                exp = exp + event.getAmount();
                if(exp>=10){
                    item.setType(Material.STONE_SWORD);
                    lore.set(0, ChatColor.YELLOW+"Next Upgrade:"+ChatColor.WHITE+" Golden Sword");
                    exp = exp-10;
                    lore.set(1, ChatColor.YELLOW+"Need exp: ("+exp+"/50)");
                    itemMeta.setLore(lore);
                    item.setItemMeta(itemMeta);
                    player.setItemInHand(item);
                    return;
                }
                lore.set(1, ChatColor.YELLOW+"Need exp: ("+exp+"/10)");
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                player.setItemInHand(item);
            }
        }
        if(item.getType().equals(Material.STONE_SWORD)){
            ItemMeta itemMeta = item.getItemMeta();
            List<String> lore = itemMeta.getLore();
            if(lore.get(0).equals(ChatColor.YELLOW+"Next Upgrade:"+ChatColor.WHITE+" Golden Sword")){
                String loreline2 = lore.get(1).replace(ChatColor.YELLOW+"Need exp: (", "").replace("/50)", "");
                int exp = Integer.parseInt(loreline2);
                exp = exp + event.getAmount();
                if(exp>=50){
                    item.setType(Material.GOLD_SWORD);
                    lore.set(0, ChatColor.YELLOW+"Next Upgrade:"+ChatColor.WHITE+" Iron Sword");
                    exp = exp-50;
                    lore.set(1, ChatColor.YELLOW+"Need exp: ("+exp+"/80)");
                    itemMeta.setLore(lore);
                    item.setItemMeta(itemMeta);
                    player.setItemInHand(item);
                    return;
                }
                lore.set(1, ChatColor.YELLOW+"Need exp: ("+exp+"/50)");
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                player.setItemInHand(item);
            }
        }
        if(item.getType().equals(Material.GOLD_SWORD)){
            ItemMeta itemMeta = item.getItemMeta();
            List<String> lore = itemMeta.getLore();
            if(lore.get(0).equals(ChatColor.YELLOW+"Next Upgrade:"+ChatColor.WHITE+" Iron Sword")){
                String loreline2 = lore.get(1).replace(ChatColor.YELLOW+"Need exp: (", "").replace("/80)", "");
                int exp = Integer.parseInt(loreline2);
                exp = exp + event.getAmount();
                if(exp>=80){
                    item.setType(Material.IRON_SWORD);
                    lore.set(0, ChatColor.YELLOW+"Next Upgrade:"+ChatColor.WHITE+" Diamond Sword");
                    exp = exp-80;
                    lore.set(1, ChatColor.YELLOW+"Need exp: ("+exp+"/5000)");
                    itemMeta.setLore(lore);
                    item.setItemMeta(itemMeta);
                    player.setItemInHand(item);
                    return;
                }
                lore.set(1, ChatColor.YELLOW+"Need exp: ("+exp+"/80)");
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                player.setItemInHand(item);
            }
        }
        if(item.getType().equals(Material.IRON_SWORD)){
            ItemMeta itemMeta = item.getItemMeta();
            List<String> lore = itemMeta.getLore();
            if(lore.get(0).equals(ChatColor.YELLOW+"Next Upgrade:"+ChatColor.WHITE+" Diamond Sword")){
                String loreline2 = lore.get(1).replace(ChatColor.YELLOW+"Need exp: (", "").replace("/5000)", "");
                int exp = Integer.parseInt(loreline2);
                exp = exp + event.getAmount();
                if(exp>=5000){
                    item.setType(Material.DIAMOND_SWORD);
                    lore.set(0, ChatColor.YELLOW+"Next Upgrade:"+ChatColor.WHITE+" Netherite Sword");
                    exp = exp-5000;
                    lore.set(1, ChatColor.YELLOW+"Need exp: ("+exp+"/50000)");
                    itemMeta.setLore(lore);
                    item.setItemMeta(itemMeta);
                    player.setItemInHand(item);
                    return;
                }
                lore.set(1, ChatColor.YELLOW+"Need exp: ("+exp+"/5000)");
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                player.setItemInHand(item);
            }
        }


        event.getPlayer().sendMessage(ChatColor.GREEN+"Exp: "+event.getAmount());
    }

}
