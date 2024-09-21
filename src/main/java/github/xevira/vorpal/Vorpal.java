package github.xevira.vorpal;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceWithEnchantedBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vorpal implements ModInitializer {
	public static final String MOD_ID = "vorpal";

	public static final int LOOT_CHANCE = 5;
	public static final float CHANCE_PER_LEVEL = 0.05f;		// 5% - hopefully this can be made into a config option eventually

	public static final RegistryKey<Enchantment> VORPAL_ENCHANTMENT_KEY = RegistryKey.of(RegistryKeys.ENCHANTMENT, id("vorpal"));

	public static final RegistryKey<LootTable> ANCIENT_CITY_LOOT_TABLE_KEY =
			RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/ancient_city"));

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.


		updateLootTables();
	}

	private static LootCondition.Builder VorpalConditionBuilder(RegistryWrapper.WrapperLookup registryLookup, float base, float perLevelAboveFirst) {
		RegistryWrapper.Impl<Enchantment> impl = registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
		return () -> {
			return new RandomChanceWithEnchantedBonusLootCondition(base, new EnchantmentLevelBasedValue.Linear(base + perLevelAboveFirst, perLevelAboveFirst), impl.getOrThrow(VORPAL_ENCHANTMENT_KEY));
		};
	}


	private static void addVorpalDrop(LootTable.Builder tableBuilder, RegistryWrapper.WrapperLookup lookup, Item head)
	{
		LootPool.Builder poolBuilder = LootPool.builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.conditionally(VorpalConditionBuilder(lookup, CHANCE_PER_LEVEL, CHANCE_PER_LEVEL))
				.with(ItemEntry.builder(head));

		tableBuilder.pool(poolBuilder.build());
	}

	private static void addVorpalBook(LootTable.Builder tableBuilder, RegistryWrapper.WrapperLookup lookup)
	{
		var enchantments = lookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
		var vorpal_enchant = enchantments.getOrThrow(VORPAL_ENCHANTMENT_KEY);

		LootPool.Builder poolBuilder = LootPool.builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.with(ItemEntry.builder(Items.AIR).weight(100 - LOOT_CHANCE))
				.apply(EnchantRandomlyLootFunction.create().option(vorpal_enchant).build())
				.with(ItemEntry.builder(Items.BOOK).weight(LOOT_CHANCE));

		tableBuilder.pool(poolBuilder.build());
	}

	private static void updateLootTables()
	{
		// Insert vorpal loot conditions for the mobs that have vanilla heads
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (source.isBuiltin())
			{
				if (ANCIENT_CITY_LOOT_TABLE_KEY.equals(key))
				{
					addVorpalBook(tableBuilder, registries);
				}
				else if (EntityType.SKELETON.getLootTableId().equals(key))
				{
					addVorpalDrop(tableBuilder, registries, Items.SKELETON_SKULL);
				}
				else if (EntityType.WITHER_SKELETON.getLootTableId().equals(key))
				{
					addVorpalDrop(tableBuilder, registries, Items.WITHER_SKELETON_SKULL);
				}
				else if (EntityType.ZOMBIE.getLootTableId().equals(key))
				{
					addVorpalDrop(tableBuilder, registries, Items.ZOMBIE_HEAD);
				}
				else if (EntityType.CREEPER.getLootTableId().equals(key))
				{
					addVorpalDrop(tableBuilder, registries, Items.CREEPER_HEAD);
				}
				else if (EntityType.PIGLIN.getLootTableId().equals(key))
				{
					addVorpalDrop(tableBuilder, registries, Items.PIGLIN_HEAD);
				}
			}
		});
	}

	private static RegistryKey<Enchantment> registeryKey(String name) {
		return RegistryKey.of(RegistryKeys.ENCHANTMENT, id(name));
	}

	public static Identifier id(String name)
	{
		return Identifier.of(MOD_ID, name);
	}
}