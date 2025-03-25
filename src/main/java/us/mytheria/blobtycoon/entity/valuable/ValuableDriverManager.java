package us.mytheria.blobtycoon.entity.valuable;

import org.jetbrains.annotations.Nullable;
import us.mytheria.blobtycoon.director.TycoonManager;
import us.mytheria.blobtycoon.director.TycoonManagerDirector;
import us.mytheria.blobtycoon.entity.valuable.drivers.BlobLibTranslatableItem;
import us.mytheria.blobtycoon.entity.valuable.drivers.VaultMultiEconomy;

import java.util.HashMap;
import java.util.Map;

public class ValuableDriverManager extends TycoonManager {
    private final Map<String, ValuableDriver> drivers;

    public ValuableDriverManager(TycoonManagerDirector managerDirector) {
        super(managerDirector);
        drivers = new HashMap<>();
        drivers.put("Vault-MultiEconomy", new VaultMultiEconomy());
        drivers.put("BlobLib-TranslatableItem", new BlobLibTranslatableItem());
    }

    /**
     * Will get a driver from the manager
     *
     * @param key the key to identify the driver
     * @return the driver
     */
    @Nullable
    public ValuableDriver getValuableDriver(String key) {
        return drivers.get(key);
    }

    /**
     * Will add a driver to the manager
     *
     * @param key    the key to identify the driver
     * @param driver the driver to add
     * @return the previous driver if it existed
     */
    @Nullable
    public ValuableDriver addDriver(String key, ValuableDriver driver) {
        return drivers.put(key, driver);
    }
}
