package me.john000708.barrels;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.john000708.barrels.block.Barrel;
import me.john000708.barrels.items.BarrelModule;
import me.john000708.barrels.items.IDCard;
import me.john000708.barrels.listeners.DisplayListener;
import me.john000708.barrels.listeners.WorldListener;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.BukkitUpdater;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.Updater;

/**
 * Created by John on 06.05.2016.
 */
public class Barrels extends JavaPlugin implements SlimefunAddon {
	
	private static Barrels instance;
    
    private boolean requirePlastic;
    private boolean displayItem;
    private String itemFormat;

    @Override
    public void onEnable() {
    	instance = this;
        Config config = new Config(this);

		// Setting up the Auto-Updater
		Updater updater;

		if (!getDescription().getVersion().startsWith("DEV - ")) {
			// We are using an official build, use the BukkitDev Updater
			updater = new BukkitUpdater(this, getFile(), 99947);
		}
		else {
			// If we are using a development build, we want to switch to our custom 
			updater = new GitHubBuildsUpdater(this, getFile(), "John000708/Barrels/master");
		}

		if (config.getBoolean("options.auto-update")) {
			updater.start();
		}

        new DisplayListener(this);
        new WorldListener(this);

        displayItem = config.getBoolean("options.displayItem");
        requirePlastic = config.getBoolean("options.plastic-recipe");
        itemFormat = config.getString("options.item-format");
        
        setup();
        getLogger().info("Barrels v" + getDescription().getVersion() + " has been enabled!");
    }
    
    @Override
    public void onDisable() {
    	instance = null;
    }
    
    private void setup() {
        Category barrelCat = new Category(new NamespacedKey(this, "barrels"), new CustomItem(Material.OAK_LOG, "&a桶", "", "&a> 点击打开"), 2);

        SlimefunItemStack smallBarrel = new SlimefunItemStack("BARREL_SMALL", Material.OAK_LOG, "&9桶 &7- &e小", "", "&8\u21E8 &7容量: 64 组");
        SlimefunItemStack mediumBarrel = new SlimefunItemStack("BARREL_MEDIUM", Material.SPRUCE_LOG, "&9桶 &7- &e中", "", "&8\u21E8 &7容量: 128 组");
        SlimefunItemStack bigBarrel = new SlimefunItemStack("BARREL_BIG", Material.DARK_OAK_LOG, "&9桶 &7- &e大", "", "&8\u21E8 &7容量: 256 组");
        SlimefunItemStack largeBarrel = new SlimefunItemStack("BARREL_LARGE", Material.ACACIA_LOG, "&9桶 &7- &e超大", "", "&8\u21E8 &7容量: 512 组");
        SlimefunItemStack deepStorageUnit = new SlimefunItemStack("BARREL_GIGANTIC", Material.DIAMOND_BLOCK, "&3深度存储单元", "", "&4终极存储解决方案", "", "&8\u21E8 &7容量: 1048576 组");

        //Upgrades
        SlimefunItemStack explosionModule = new SlimefunItemStack("BARREL_EXPLOSION_MODULE", Material.ITEM_FRAME, "&9爆炸保护", "", "&f阻止桶被破坏");
        SlimefunItemStack biometricProtectionModule = new SlimefunItemStack("BARREL_BIO_PROTECTION", Material.ITEM_FRAME, "&9生物保护", "", "&f阻止其他人打开你的桶.");
        SlimefunItemStack idCard = new SlimefunItemStack("BARREL_ID_CARD", Material.PAPER, "&f身份证", "", "&f右键绑定身份.");
        SlimefunItemStack structUpgrade1 = new SlimefunItemStack("STRUCT_UPGRADE_1", Material.ITEM_FRAME, "&9结构升级 &7- &eI", "&b小 &8\u21E8 &b中");
        SlimefunItemStack structUpgrade2 = new SlimefunItemStack("STRUCT_UPGRADE_2", Material.ITEM_FRAME, "&9结构升级 &7- &eII", "&b中 &8\u21E8 &b大");
        SlimefunItemStack structUpgrade3 = new SlimefunItemStack("STRUCT_UPGRADE_3", Material.ITEM_FRAME, "&9结构升级 &7- &eIII", "&b大 &8\u21E8 &b超大");

        new Barrel(barrelCat, smallBarrel, RecipeType.ENHANCED_CRAFTING_TABLE, 
        new ItemStack[] {new ItemStack(Material.OAK_SLAB), requirePlastic ? SlimefunItems.PLASTIC_SHEET : new ItemStack(Material.CAULDRON), new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_SLAB), new ItemStack(Material.CHEST), new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_SLAB), SlimefunItems.GILDED_IRON, new ItemStack(Material.OAK_SLAB)}, 4096) {

            @Override
            public String getInventoryTitle() {
                return "&9桶 &7- &e小";
            }

        }.register(this);

        new Barrel(barrelCat, mediumBarrel, RecipeType.ENHANCED_CRAFTING_TABLE, 
        new ItemStack[] {new ItemStack(Material.OAK_SLAB), requirePlastic ? SlimefunItems.PLASTIC_SHEET : new ItemStack(Material.CAULDRON), new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_SLAB), smallBarrel, new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_SLAB), SlimefunItems.GILDED_IRON, new ItemStack(Material.OAK_SLAB)}, 8192) {

            @Override
            public String getInventoryTitle() {
                return "&9桶 &7- &e中";
            }

        }.register(this);

        new Barrel(barrelCat, bigBarrel, RecipeType.ENHANCED_CRAFTING_TABLE, 
        new ItemStack[] {new ItemStack(Material.OAK_SLAB), requirePlastic ? SlimefunItems.PLASTIC_SHEET : new ItemStack(Material.CAULDRON), new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_SLAB), mediumBarrel, new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_SLAB), SlimefunItems.GILDED_IRON, new ItemStack(Material.OAK_SLAB)}, 16384) {

            @Override
            public String getInventoryTitle() {
                return "&9桶 &7- &e大";
            }

        }.register(this);

        new Barrel(barrelCat, largeBarrel, RecipeType.ENHANCED_CRAFTING_TABLE, 
        new ItemStack[] {new ItemStack(Material.OAK_SLAB), requirePlastic ? SlimefunItems.PLASTIC_SHEET : new ItemStack(Material.CAULDRON), new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_SLAB), bigBarrel, new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_SLAB), SlimefunItems.GILDED_IRON, new ItemStack(Material.OAK_SLAB)}, 32768) {

            @Override
            public String getInventoryTitle() {
                return "&9桶 &7- &e超大";
            }

        }.register(this);

        new Barrel(barrelCat, deepStorageUnit, RecipeType.ENHANCED_CRAFTING_TABLE, 
        new ItemStack[] {SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.ENDER_CHEST), SlimefunItems.REINFORCED_PLATE, SlimefunItems.PLASTIC_SHEET, largeBarrel, SlimefunItems.PLASTIC_SHEET, SlimefunItems.REINFORCED_PLATE, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.REINFORCED_PLATE}, 1048576) {

            @Override
            public String getInventoryTitle() {
                return "&3深度存储单元";
            }

        }.register(this);
        
        new BarrelModule(barrelCat, explosionModule, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {new ItemStack(Material.TNT), new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.TNT), new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.REDSTONE), new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.TNT), new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.TNT)}) {
        	
        	@Override
        	public boolean applyUpgrade(Block b) {
        		if (BlockStorage.getLocationInfo(b.getLocation(), "explosion") != null) {
        			return false;
        		}
        		
        		BlockStorage.addBlockInfo(b, "explosion", "true");
        		return true;
        	}
        	
        }.register(this);

        new BarrelModule(barrelCat, structUpgrade1, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.LEAD_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, mediumBarrel, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.LEAD_INGOT}) {
        	
        	@Override
        	public boolean applyUpgrade(Block b) {
        		if (BlockStorage.getLocationInfo(b.getLocation(), "STRUCT_1") != null) {
        			return false;
        		}
        		
        		int capacity = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "capacity"));
        		BlockStorage.addBlockInfo(b, "STRUCT_1", "true");
                BlockStorage.addBlockInfo(b, "capacity", String.valueOf(capacity + 8192));
                return true;
        	}
        	
        }.register(this);

        new BarrelModule(barrelCat, structUpgrade2, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.LEAD_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, bigBarrel, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.LEAD_INGOT}) {
        	
        	@Override
        	public boolean applyUpgrade(Block b) {
        		if (BlockStorage.getLocationInfo(b.getLocation(), "STRUCT_2") != null) {
        			return false;
        		}
        		
        		int capacity = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "capacity"));
        		BlockStorage.addBlockInfo(b, "STRUCT_2", "true");
                BlockStorage.addBlockInfo(b, "capacity", String.valueOf(capacity + 16384));
                return true;
        	}
        	
        }.register(this);

        new BarrelModule(barrelCat, structUpgrade3, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {SlimefunItems.LEAD_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, largeBarrel, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.LEAD_INGOT}) {
        	
        	@Override
        	public boolean applyUpgrade(Block b) {
        		if (BlockStorage.getLocationInfo(b.getLocation(), "STRUCT_3") != null) {
        			return false;
        		}
        		
        		int capacity = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "capacity"));
        		BlockStorage.addBlockInfo(b, "STRUCT_3", "true");
                BlockStorage.addBlockInfo(b, "capacity", String.valueOf(capacity + 32768));
                return true;
        	}
        	
        }.register(this);

        new BarrelModule(barrelCat, biometricProtectionModule, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {new ItemStack(Material.REDSTONE), new ItemStack(Material.DIAMOND), new ItemStack(Material.REDSTONE), new ItemStack(Material.DIAMOND), new ItemStack(Material.PAPER), new ItemStack(Material.DIAMOND), new ItemStack(Material.REDSTONE), new ItemStack(Material.DIAMOND), new ItemStack(Material.REDSTONE)}) {
        	
        	@Override
        	public boolean applyUpgrade(Block b) {
        		if (BlockStorage.getLocationInfo(b.getLocation(), "protected") != null) {
        			return false;
        		}
        		
        		BlockStorage.addBlockInfo(b, "protected", "true");
                return true;
        	}
        	
        }.register(this);

        new IDCard(barrelCat, idCard, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {new ItemStack(Material.REDSTONE), new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.REDSTONE), new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.PAPER), new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.REDSTONE), new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.REDSTONE)})
        .register(this);
    }

	@Override
	public JavaPlugin getJavaPlugin() {
		return this;
	}

	@Override
	public String getBugTrackerURL() {
		return "https://github.com/John000708/Barrels/issues";
	}

	public static Barrels getInstance() {
		return instance;
	}

	public static String getItemFormat() {
		return instance.itemFormat;
	}

	public static boolean displayItem() {
		return instance.displayItem;
	}
}
