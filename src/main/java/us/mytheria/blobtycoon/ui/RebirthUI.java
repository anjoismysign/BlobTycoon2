package us.mytheria.blobtycoon.ui;

import net.milkbowl.vault.economy.IdentityEconomy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.api.BlobLibEconomyAPI;
import us.mytheria.bloblib.api.BlobLibInventoryAPI;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.bloblib.entities.ReloadableUI;
import us.mytheria.bloblib.vault.multieconomy.ElasticEconomy;
import us.mytheria.blobtycoon.BlobTycoonInternalAPI;
import us.mytheria.blobtycoon.entity.PlotProfile;
import us.mytheria.blobtycoon.entity.TycoonPlayer;
import us.mytheria.blobtycoon.entity.configuration.CostIncreaseConfiguration;
import us.mytheria.blobtycoon.entity.configuration.RebirthConfiguration;

public class RebirthUI implements ReloadableUI {
    protected RebirthUI() {
    }

    @Override
    public void reload(@NotNull BlobLibInventoryAPI inventoryAPI) {
        var rebirthRegistry = inventoryAPI.getInventoryDataRegistry("Rebirth");
        rebirthRegistry.onClick("Rebirth", inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();
            TycoonPlayer tycoonPlayer = BlobTycoonInternalAPI.getInstance().getTycoonPlayer(player);
            if (tycoonPlayer == null || tycoonPlayer.getProfile() == null) {
                player.closeInventory();
                BlobLibMessageAPI.getInstance()
                        .getMessage("Player.Not-Inside-Plugin-Cache", player)
                        .handle(player);
                return;
            }
            PlotProfile profile = tycoonPlayer.getProfile().getPlotProfile();
            int rebirths = profile.getRebirths();
            CostIncreaseConfiguration configuration = RebirthConfiguration.getInstance().getCostIncreaseConfiguration();
            double price = configuration.getCost(rebirths);
            String currency = configuration.getCostCurrency();
            ElasticEconomy elasticEconomy = BlobLibEconomyAPI.getInstance().getElasticEconomy();
            IdentityEconomy economy = elasticEconomy.getImplementation(currency);
            if (!economy.has(player, price)) {
                player.closeInventory();
                BlobLibMessageAPI.getInstance()
                        .getMessage("Withdraw.Insufficient-Balance", player)
                        .handle(player);
                return;
            }
            economy.withdrawPlayer(player, price);
            profile.rebirth();
        });
    }
}
