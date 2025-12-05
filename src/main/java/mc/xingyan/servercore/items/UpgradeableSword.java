package mc.xingyan.servercore.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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

public class UpgradeableSword implements Listener {

    static ItemStack item = null;

    public ItemStack get(String rarity){
        ItemMeta im;
        List<Component> lore;
        switch (rarity){
            case "STONE":
                item = new ItemStack(Material.STONE_SWORD);
                im = item.getItemMeta();
                lore = new ArrayList<>();
                lore.add(MiniMessage.miniMessage().deserialize("<yellow>Next Upgrade:<white> Wooden Sword"));
                lore.add(MiniMessage.miniMessage().deserialize("<yellow>Need exp: (0/50)"));
                im.lore(lore);
                item.setItemMeta(im);
                break;
            case "GOLDEN":
                item = new ItemStack(Material.GOLDEN_SWORD);
                im = item.getItemMeta();
                lore = new ArrayList<>();
                lore.add(MiniMessage.miniMessage().deserialize("<yellow>Next Upgrade:<white> Iron Sword"));
                lore.add(MiniMessage.miniMessage().deserialize("<yellow>Need exp: (0/80)"));
                im.lore(lore);
                item.setItemMeta(im);
                break;
            case "IRON":
                item = new ItemStack(Material.IRON_SWORD);
                im = item.getItemMeta();
                lore = new ArrayList<>();
                lore.add(MiniMessage.miniMessage().deserialize("<yellow>Next Upgrade:<white> Diamond Sword"));
                lore.add(MiniMessage.miniMessage().deserialize("<yellow>Need exp: (0/5000)"));
                im.lore(lore);
                item.setItemMeta(im);
                break;
            case "DIAMOND":
                item = new ItemStack(Material.DIAMOND_SWORD);
                im = item.getItemMeta();
                lore = new ArrayList<>();
                lore.add(MiniMessage.miniMessage().deserialize("<yellow>Next Upgrade:<white> Netherite Sword"));
                lore.add(MiniMessage.miniMessage().deserialize("<yellow>Need exp: (0/50000)"));
                im.lore(lore);
                item.setItemMeta(im);
                break;
            default:
                item = new ItemStack(Material.WOODEN_SWORD);
                im = item.getItemMeta();
                lore = new ArrayList<>();
                lore.add(MiniMessage.miniMessage().deserialize("<yellow>Next Upgrade:<white> Stone Sword"));
                lore.add(MiniMessage.miniMessage().deserialize("<yellow>Need exp: (0/10)"));
                im.lore(lore);
                item.setItemMeta(im);
                break;
        }
        return item;
    }

    @EventHandler
    public void ongetexp(PlayerExpChangeEvent event){
        Player player = event.getPlayer();
        Inventory inv = player.getInventory();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta()) return;
        ItemMeta itemMeta = item.getItemMeta();
        if (!itemMeta.hasLore()) return;
        List<Component> lore = itemMeta.lore();
        if (lore == null || lore.isEmpty()) return;

        String line0 = PlainTextComponentSerializer.plainText().serialize(lore.get(0));
        String line1 = PlainTextComponentSerializer.plainText().serialize(lore.get(1));

        //swords
        if(item.getType().equals(Material.WOODEN_SWORD)){
            if(line0.equals("Next Upgrade: Stone Sword")){
                String expStr = line1.replace("Need exp: (", "").replace("/10)", "");
                try {
                    int exp = Integer.parseInt(expStr);
                    exp = exp + event.getAmount();
                    if(exp>=10){
                        item.setType(Material.STONE_SWORD);
                        lore.set(0, MiniMessage.miniMessage().deserialize("<yellow>Next Upgrade:<white> Golden Sword"));
                        exp = exp-10;
                        lore.set(1, MiniMessage.miniMessage().deserialize("<yellow>Need exp: ("+exp+"/50)"));
                        itemMeta.lore(lore);
                        item.setItemMeta(itemMeta);
                        player.getInventory().setItemInMainHand(item);
                        return;
                    }
                    lore.set(1, MiniMessage.miniMessage().deserialize("<yellow>Need exp: ("+exp+"/10)"));
                    itemMeta.lore(lore);
                    item.setItemMeta(itemMeta);
                    player.getInventory().setItemInMainHand(item);
                } catch (NumberFormatException e) {}
            }
        }
        if(item.getType().equals(Material.STONE_SWORD)){
            if(line0.equals("Next Upgrade: Golden Sword")){
                String expStr = line1.replace("Need exp: (", "").replace("/50)", "");
                try {
                    int exp = Integer.parseInt(expStr);
                    exp = exp + event.getAmount();
                    if(exp>=50){
                        item.setType(Material.GOLDEN_SWORD);
                        lore.set(0, MiniMessage.miniMessage().deserialize("<yellow>Next Upgrade:<white> Iron Sword"));
                        exp = exp-50;
                        lore.set(1, MiniMessage.miniMessage().deserialize("<yellow>Need exp: ("+exp+"/80)"));
                        itemMeta.lore(lore);
                        item.setItemMeta(itemMeta);
                        player.getInventory().setItemInMainHand(item);
                        return;
                    }
                    lore.set(1, MiniMessage.miniMessage().deserialize("<yellow>Need exp: ("+exp+"/50)"));
                    itemMeta.lore(lore);
                    item.setItemMeta(itemMeta);
                    player.getInventory().setItemInMainHand(item);
                } catch (NumberFormatException e) {}
            }
        }
        if(item.getType().equals(Material.GOLDEN_SWORD)){
            if(line0.equals("Next Upgrade: Iron Sword")){
                String expStr = line1.replace("Need exp: (", "").replace("/80)", "");
                try {
                    int exp = Integer.parseInt(expStr);
                    exp = exp + event.getAmount();
                    if(exp>=80){
                        item.setType(Material.IRON_SWORD);
                        lore.set(0, MiniMessage.miniMessage().deserialize("<yellow>Next Upgrade:<white> Diamond Sword"));
                        exp = exp-80;
                        lore.set(1, MiniMessage.miniMessage().deserialize("<yellow>Need exp: ("+exp+"/5000)"));
                        itemMeta.lore(lore);
                        item.setItemMeta(itemMeta);
                        player.getInventory().setItemInMainHand(item);
                        return;
                    }
                    lore.set(1, MiniMessage.miniMessage().deserialize("<yellow>Need exp: ("+exp+"/80)"));
                    itemMeta.lore(lore);
                    item.setItemMeta(itemMeta);
                    player.getInventory().setItemInMainHand(item);
                } catch (NumberFormatException e) {}
            }
        }
        if(item.getType().equals(Material.IRON_SWORD)){
            if(line0.equals("Next Upgrade: Diamond Sword")){
                String expStr = line1.replace("Need exp: (", "").replace("/5000)", "");
                try {
                    int exp = Integer.parseInt(expStr);
                    exp = exp + event.getAmount();
                    if(exp>=5000){
                        item.setType(Material.DIAMOND_SWORD);
                        lore.set(0, MiniMessage.miniMessage().deserialize("<yellow>Next Upgrade:<white> Netherite Sword"));
                        exp = exp-5000;
                        lore.set(1, MiniMessage.miniMessage().deserialize("<yellow>Need exp: ("+exp+"/50000)"));
                        itemMeta.lore(lore);
                        item.setItemMeta(itemMeta);
                        player.getInventory().setItemInMainHand(item);
                        return;
                    }
                    lore.set(1, MiniMessage.miniMessage().deserialize("<yellow>Need exp: ("+exp+"/5000)"));
                    itemMeta.lore(lore);
                    item.setItemMeta(itemMeta);
                    player.getInventory().setItemInMainHand(item);
                } catch (NumberFormatException e) {}
            }
        }


        event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<green>Exp: "+event.getAmount()));
    }

}


