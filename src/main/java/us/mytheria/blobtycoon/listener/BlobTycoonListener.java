package us.mytheria.blobtycoon.listener;

import us.mytheria.bloblib.entities.BlobListener;
import us.mytheria.blobtycoon.director.manager.TycoonConfigManager;
import us.mytheria.blobtycoon.director.manager.TycoonListenerManager;

public abstract class BlobTycoonListener implements BlobListener {
    private final TycoonListenerManager listenerManager;

    public BlobTycoonListener(TycoonListenerManager listenerManager) {
        this.listenerManager = listenerManager;
    }

    public TycoonListenerManager getListenerManager() {
        return listenerManager;
    }

    public TycoonConfigManager getConfigManager() {
        return getListenerManager().getManagerDirector().getConfigManager();
    }
}
