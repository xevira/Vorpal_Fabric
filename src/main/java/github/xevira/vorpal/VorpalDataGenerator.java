package github.xevira.vorpal;

import github.xevira.vorpal.data.generator.EnchantmentGenerator;
import github.xevira.vorpal.data.provider.VorpalEnglishLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;

public class VorpalDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(VorpalEnglishLanguageProvider::new);
		pack.addProvider(EnchantmentGenerator::new);

	}
}
