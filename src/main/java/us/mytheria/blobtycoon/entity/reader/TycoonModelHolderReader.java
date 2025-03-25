package us.mytheria.blobtycoon.entity.reader;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.exception.ConfigurationFieldException;
import us.mytheria.blobtycoon.director.TycoonManagerDirector;
import us.mytheria.blobtycoon.entity.Sellable;
import us.mytheria.blobtycoon.entity.structure.ObjectModel;
import us.mytheria.blobtycoon.entity.structure.StorageModel;
import us.mytheria.blobtycoon.entity.structure.StructureModel;
import us.mytheria.blobtycoon.entity.structure.TycoonModelHolderData;

import java.io.File;
import java.util.function.Consumer;

public class TycoonModelHolderReader {
    public static TycoonModelHolderData<ObjectModel> OBJECT_MODEL(@NotNull File file,
                                                                  @NotNull TycoonManagerDirector director,
                                                                  @NotNull String type,
                                                                  @NotNull String key,
                                                                  @NotNull Consumer<Player> whenRemoved,
                                                                  @NotNull Consumer<Player> whenPlaced) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.isConfigurationSection("Structure-Model"))
            throw new ConfigurationFieldException("'Structure-Model' is not valid or set");
        ObjectModel structureModel = TycoonModelReader.OBJECT_MODEL(config
                        .getConfigurationSection("Structure-Model"),
                director.getPlugin(), type, key,
                whenRemoved, whenPlaced);
        if (!config.isConfigurationSection("Sellable")) {
            if (director.getConfigManager().tinyDebug())
                director.getPlugin().getAnjoLogger().singleError("Structure '" + file.getPath() + "' is not getSellable");
            return new TycoonModelHolderData<>(structureModel, null);
        }
        Sellable sellable = SellableReader.READ(config
                .getConfigurationSection("Sellable"));
        return new TycoonModelHolderData<>(structureModel, sellable);
    }

    public static TycoonModelHolderData<StorageModel> STORAGE_HOLDER_MODEL(@NotNull File file,
                                                                           @NotNull TycoonManagerDirector director,
                                                                           @NotNull String type,
                                                                           @NotNull String key,
                                                                           @NotNull Consumer<Player> whenRemoved,
                                                                           @NotNull Consumer<Player> whenPlaced) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.isConfigurationSection("Structure-Model"))
            throw new ConfigurationFieldException("'Structure-Model' is not valid or set");
        int maxStorage = StorageModel.READ(config);
        StorageModel structureModel = TycoonModelReader.STORAGE_MODEL(config
                        .getConfigurationSection("Structure-Model"),
                director.getPlugin(), type, key,
                whenRemoved, whenPlaced, maxStorage);
        if (!config.isConfigurationSection("Sellable")) {
            if (director.getConfigManager().tinyDebug())
                director.getPlugin().getAnjoLogger().singleError("Structure '" + file.getPath() + "' is not getSellable");
            return new TycoonModelHolderData<>(structureModel, null);
        }
        Sellable sellable = SellableReader.READ(config
                .getConfigurationSection("Sellable"));
        return new TycoonModelHolderData<>(structureModel, sellable);
    }

    public static TycoonModelHolderData<StructureModel> STRUCTURE_MODEL(@NotNull File file,
                                                                        @NotNull TycoonManagerDirector director,
                                                                        @NotNull String type,
                                                                        @NotNull String key,
                                                                        @NotNull Consumer<Player> whenRemoved,
                                                                        @NotNull Consumer<Player> whenPlaced) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.isConfigurationSection("Structure-Model"))
            throw new ConfigurationFieldException("'Structure-Model' is not valid or set");
        StructureModel structureModel = TycoonModelReader.STRUCTURE_MODEL(config
                        .getConfigurationSection("Structure-Model"),
                director.getPlugin(), type, key,
                whenRemoved, whenPlaced);
        if (!config.isConfigurationSection("Sellable")) {
            if (director.getConfigManager().tinyDebug())
                director.getPlugin().getAnjoLogger().singleError("Structure '" + file.getPath() + "' is not getSellable");
            return new TycoonModelHolderData<>(structureModel, null);
        }
        Sellable sellable = SellableReader.READ(config
                .getConfigurationSection("Sellable"));
        return new TycoonModelHolderData<>(structureModel, sellable);
    }
}
