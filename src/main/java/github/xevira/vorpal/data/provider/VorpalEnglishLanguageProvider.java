package github.xevira.vorpal.data.provider;

import github.xevira.vorpal.Vorpal;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class VorpalEnglishLanguageProvider extends FabricLanguageProvider {
    public VorpalEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.addEnchantment(Vorpal.VORPAL_ENCHANTMENT_KEY, "Vorpal");
    }
}
