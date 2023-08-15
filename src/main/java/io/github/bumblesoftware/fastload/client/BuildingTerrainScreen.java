package io.github.bumblesoftware.fastload.client;

import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public class BuildingTerrainScreen extends Screen {
    public final int loadingAreaGoal;
    private Runnable runnable;

    /**
     * Texts to draw
     */
    public BuildingTerrainScreen(final int loadingAreaGoal) {
        super(GameNarrator.NO_TITLE);
        this.loadingAreaGoal = loadingAreaGoal;
    }

    /**
     * Renders screen texts
     */
    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (runnable != null) runnable.run();
        else super.onClose();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void setClose(Runnable runnable) {
        this.runnable = runnable;
    }
}
