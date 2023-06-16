package io.github.bumblesoftware.fastload.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;

public class BuildingTerrainScreen extends Screen {
    public final int loadingAreaGoal;
    private Runnable runnable;

    /**
     * Texts to draw
     */
    public BuildingTerrainScreen(final int loadingAreaGoal) {
        super(NarratorManager.EMPTY);
        this.loadingAreaGoal = loadingAreaGoal;
    }

    /**
     * Renders screen texts
     */
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        if (runnable != null) runnable.run();
        else super.close();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public void setClose(Runnable runnable) {
        this.runnable = runnable;
    }
}
