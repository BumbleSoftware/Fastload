package com.abdelaziz.fastload.util.screen;

import com.abdelaziz.fastload.FastLoad;
import com.abdelaziz.fastload.config.FLMath;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class BuildingTerrainScreen extends Screen {
    private final Text SCREEN_NAME;
    private final Text PROPORTION;
    private final MinecraftClient client = MinecraftClient.getInstance();

    private Integer PREPARED_PROGRESS_STORAGE = 0;
    private Integer BUILDING_PROGRESS_STORAGE = 0;

    private Integer getLoadedChunkCount() {
        return client.world != null ? client.world.getChunkManager().getLoadedChunkCount() : 0;
    }

    private Integer getBuiltChunkCount() {
        return client.world != null ? client.worldRenderer.getCompletedChunkCount() : 0;
    }

    public BuildingTerrainScreen() {
        super(NarratorManager.EMPTY);
        SCREEN_NAME = Text.translatable("menu.generatingTerrain");
        PROPORTION = Text.literal("(COMPLETED)/(GOAL)");
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        final int white = 0xFFFFFF;
        final int heightUpFromCentre = 50;
        final String loadedChunksString = getLoadedChunkCount() + "/"  + FLMath.getPreRenderArea();
        final String builtChunksString = getBuiltChunkCount() + "/"  + FLMath.getPreRenderArea() * client.options.getFov().getValue()/360;
        if (PREPARED_PROGRESS_STORAGE < getLoadedChunkCount()) {
            FastLoad.LOGGER.info("World Chunk Sending: " + loadedChunksString);
        }

        if (BUILDING_PROGRESS_STORAGE < getBuiltChunkCount()) {
            FastLoad.LOGGER.info("Visible Chunk Building: " + builtChunksString);
        }

        PREPARED_PROGRESS_STORAGE = getLoadedChunkCount();
        BUILDING_PROGRESS_STORAGE = getBuiltChunkCount();
        DrawableHelper.drawCenteredText(matrices, this.textRenderer, SCREEN_NAME, this.width / 2, this.height / 2 - heightUpFromCentre, white);
        DrawableHelper.drawCenteredText(matrices, this.textRenderer, PROPORTION, this.width / 2, this.height / 2 - heightUpFromCentre + 30, white);

        DrawableHelper.drawCenteredText(
                matrices,
                this.textRenderer,
                "Preparing All Chunks: " + loadedChunksString,
                width / 2,
                height / 2 - heightUpFromCentre + 45,
                white);
        DrawableHelper.drawCenteredText(
                matrices,
                this.textRenderer,
                "Building Visible Chunks: " + builtChunksString,
                width / 2,
                height / 2 - heightUpFromCentre + 60,
                white);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}