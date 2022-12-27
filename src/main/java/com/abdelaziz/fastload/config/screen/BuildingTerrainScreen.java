package com.abdelaziz.fastload.config.screen;

import com.abdelaziz.fastload.config.init.FLMath;
import com.abdelaziz.fastload.init.Fastload;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static com.abdelaziz.fastload.config.screen.FLColourConstants.white;

public class BuildingTerrainScreen extends Screen {
    private final Text SCREEN_NAME;
    private static final int heightUpFromCentre = 50;
    private final Text SCREEN_TEMPLATE;
    private final Text BUILDING_CHUNKS;
    private Integer PREPARED_PROGRESS_STORAGE = 0;
    private Integer BUILDING_PROGRESS_STORAGE = 0;
    private final Text PREPARING_CHUNKS;
    private final MinecraftClient client = MinecraftClient.getInstance();

    private Integer getLoadedChunkCount() {
        return client.world != null ? client.world.getChunkManager().getLoadedChunkCount() : 0;
    }

    private Integer getBuiltChunkCount() {
        return client.world != null ? client.worldRenderer.getCompletedChunkCount() : 0;
    }

    /**
     * Texts to draw
     */
    public BuildingTerrainScreen() {
        super(NarratorManager.EMPTY);
        SCREEN_NAME = Text.of("menu.generatingTerrain");
        SCREEN_TEMPLATE = Text.of("fastload.screen.buildingTerrain.template");
        BUILDING_CHUNKS = Text.of("fastload.screen.buildingTerrain.building");
        PREPARING_CHUNKS = Text.of("fastload.screen.buildingTerrain.preparing");
    }

    /**
     * Renders screen texts
     */
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        final String loadedChunksString = getLoadedChunkCount() + "/" + FLMath.getPreRenderArea();
        final String builtChunksString = getBuiltChunkCount() + "/" + FLMath.getPreRenderArea() * client.options.fov / 180;
        if (PREPARED_PROGRESS_STORAGE < getLoadedChunkCount()) {
            Fastload.LOGGER.info("World Chunk Sending: " + loadedChunksString);
        }
        if (BUILDING_PROGRESS_STORAGE < getBuiltChunkCount()) {
            Fastload.LOGGER.info("Visible Chunk Building: " + builtChunksString);
        }
        PREPARED_PROGRESS_STORAGE = getLoadedChunkCount();
        BUILDING_PROGRESS_STORAGE = getBuiltChunkCount();

        DrawableHelper.drawCenteredText(
                matrices,
                this.textRenderer,
                SCREEN_NAME,
                this.width / 2,
                this.height / 2 - heightUpFromCentre,
                white
        );
        DrawableHelper.drawCenteredText(
                matrices,
                this.textRenderer,
                SCREEN_TEMPLATE,
                this.width / 2,
                this.height / 2 - heightUpFromCentre + 30,
                white);

        DrawableHelper.drawCenteredText(
                matrices,
                this.textRenderer,
                PREPARING_CHUNKS.getString() + ": " + loadedChunksString,
                width / 2,
                height / 2 - heightUpFromCentre + 45,
                white);

        DrawableHelper.drawCenteredText(
                matrices,
                this.textRenderer,
                BUILDING_CHUNKS.getString() + ": " + builtChunksString,
                width / 2,
                height / 2 - heightUpFromCentre + 60,
                white);

        super.render(matrices, mouseX, mouseY, delta);
    }

    /**
     * Fastload determines when to bail, not the user
     */
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    /**
     * Permits the server to keep ticking
     */
    @Override
    public boolean shouldPause() {
        return false;
    }
}