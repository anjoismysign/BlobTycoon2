package io.github.anjoismysign.entity.plotdata;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockVector;
import io.github.anjoismysign.bloblib.api.BlobLibSoundAPI;
import io.github.anjoismysign.bloblib.entities.PlayerAddress;
import io.github.anjoismysign.bloblib.entities.inventory.BlobInventory;
import io.github.anjoismysign.bloblib.entities.inventory.BlobObjectBuilder;
import io.github.anjoismysign.bloblib.entities.inventory.ObjectBuilderButton;
import io.github.anjoismysign.bloblib.entities.inventory.ObjectBuilderButtonBuilder;
import io.github.anjoismysign.bloblib.entities.message.BlobSound;
import io.github.anjoismysign.director.manager.PlotManager;
import io.github.anjoismysign.entity.StructureDirection;
import io.github.anjoismysign.entity.selection.Selector;

import java.io.File;
import java.util.UUID;

public class PlotDataBuilder extends BlobObjectBuilder<PlotData> {

    private final Selector selector;
    private final BukkitTask checker;

    public static PlotDataBuilder build(UUID builderId, PlotManager plotManager) {
        BlobInventory inventory = BlobInventory
                .ofKeyAddressOrThrow("PlotData", PlayerAddress.builder()
                        .setUUID(builderId).build());

        return new PlotDataBuilder(
                inventory,
                builderId,
                plotManager);
    }

    private PlotDataBuilder(BlobInventory blobInventory, UUID builderId,
                            PlotManager plotManager) {
        super(blobInventory, builderId);
        selector = Selector.of(getPlayer());
        ObjectBuilderButton<String> indexButton = ObjectBuilderButtonBuilder.QUICK_STRING(
                "PlotIndex", 300, this);
        ObjectBuilderButton<StructureDirection> directionButton = ObjectBuilderButtonBuilder
                .ENUM_NAVIGATOR("PlotDirection",
                        StructureDirection.class, this);
        ObjectBuilderButton<Integer> sizeXButton = ObjectBuilderButtonBuilder
                .POSITIVE_INTEGER("PlotSizeX", 300, this);
        ObjectBuilderButton<Integer> sizeYButton = ObjectBuilderButtonBuilder
                .POSITIVE_INTEGER("PlotSizeY", 300, this);
        ObjectBuilderButton<Integer> sizeZButton = ObjectBuilderButtonBuilder
                .POSITIVE_INTEGER("PlotSizeZ", 300, this);
        ObjectBuilderButton<Block> minPointButton = ObjectBuilderButtonBuilder
                .QUICK_ACTION_BLOCK(
                        "PlotMinPoint", 300, this,
                        selector::setSelected);
        checker = Bukkit.getScheduler().runTaskTimer(plotManager.getPlugin(), () -> {
            if (selector.getSelected() == null)
                return;
            if (!sizeXButton.isValuePresentAndNotNull())
                return;
            if (!sizeYButton.isValuePresentAndNotNull())
                return;
            if (!sizeZButton.isValuePresentAndNotNull())
                return;
            int x = sizeXButton.get().get();
            int y = sizeYButton.get().get();
            int z = sizeZButton.get().get();
            selector.setSize(new BlockVector(x, y, z));
        }, 1, 3);

        addObjectBuilderButton(indexButton)
                .addObjectBuilderButton(minPointButton)
                .addObjectBuilderButton(directionButton)
                .addObjectBuilderButton(sizeXButton)
                .addObjectBuilderButton(sizeYButton)
                .addObjectBuilderButton(sizeZButton)
                .setFunction(builder -> {
                    PlotData build = builder.construct();
                    if (build == null)
                        return null;
                    Player player = getPlayer();
                    BlobSound sound = BlobLibSoundAPI.getInstance().getSound("Builder.Build-Complete");
                    sound.play(player);
                    player.closeInventory();
                    checker.cancel();
                    selector.stop();
                    File directory = plotManager.getPlotsDirectory();
                    build.saveToFile(directory);
                    PlotData read = PlotData.fromFile(new File(directory, build.getIndex() + ".yml"));
                    plotManager.load(read);
                    plotManager.getPlotDataBuilderManager().removeBuilder(player);
                    return build;
                });

    }

    @SuppressWarnings("unchecked")
    @Override
    public PlotData construct() {
        ObjectBuilderButton<String> indexButton = (ObjectBuilderButton<String>) getObjectBuilderButton("PlotIndex");
        ObjectBuilderButton<Block> minPointButton = (ObjectBuilderButton<Block>) getObjectBuilderButton("PlotMinPoint");
        ObjectBuilderButton<StructureDirection> directionButton = (ObjectBuilderButton<StructureDirection>) getObjectBuilderButton("PlotDirection");
        ObjectBuilderButton<Integer> sizeXButton = (ObjectBuilderButton<Integer>) getObjectBuilderButton("PlotSizeX");
        ObjectBuilderButton<Integer> sizeYButton = (ObjectBuilderButton<Integer>) getObjectBuilderButton("PlotSizeY");
        ObjectBuilderButton<Integer> sizeZButton = (ObjectBuilderButton<Integer>) getObjectBuilderButton("PlotSizeZ");

        if (!indexButton.isValuePresentAndNotNull() || !minPointButton.isValuePresentAndNotNull()
                || !directionButton.isValuePresentAndNotNull() || !sizeXButton.isValuePresentAndNotNull()
                || !sizeYButton.isValuePresentAndNotNull() || !sizeZButton.isValuePresentAndNotNull())
            return null;

        String index = indexButton.get().get();
        Block minPoint = minPointButton.get().get();
        StructureDirection direction = directionButton.get().get();
        int x = sizeXButton.get().get();
        int y = sizeYButton.get().get();
        int z = sizeZButton.get().get();

        return PlotData.of(index, minPoint.getLocation(),
                new BlockVector(x, y, z),
                direction, null);
    }
}