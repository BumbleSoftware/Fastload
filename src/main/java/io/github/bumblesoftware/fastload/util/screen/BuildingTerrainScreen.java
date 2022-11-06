package io.github.bumblesoftware.fastload.util.screen;

import io.github.bumblesoftware.fastload.FastLoad;
import io.github.bumblesoftware.fastload.config.FLMath;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class BuildingTerrainScreen extends Screen {
    private final Text text;
    private final MinecraftClient client = MinecraftClient.getInstance();

    private Integer RENDERING_PROGRESS_STORAGE = 0;
    private Integer BUILDING_PROGRESS_STORAGE = 0;
    private Integer getLoadedChunkCount() {
        return client.world != null ? client.world.getChunkManager().getLoadedChunkCount() : 0;
    }
    private Double getLoadedPercentage() {
        return getLoadedChunkCount().doubleValue()/FLMath.getPreRenderArea().doubleValue();
    }
    private Integer getBuiltChunkCount() {
        return client.world != null ? client.worldRenderer.getCompletedChunkCount() : 0;
    }
    private Double getBuiltChunkPercentage() {
        return getBuiltChunkCount().doubleValue() /(FLMath.getPreRenderArea() * client.options.getFov().getValue().doubleValue() / 360);
    }
    private Integer getPercentageInt(double percentage) {
        double d = percentage * 100;
        if (d > 100) d = 100;
        return (int) d;
    }
    public BuildingTerrainScreen() {
        super(NarratorManager.EMPTY);
        text = Text.translatable("menu.generatingTerrain");
    }
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        final int white = 0xFFFFFF;
        final int heightUpFromCentre = 50;
        final int loadedPercentageInt = getPercentageInt(getLoadedPercentage());
        final int builtPercentageInt = getPercentageInt(getBuiltChunkPercentage());
        final String loadedPercentageOfString = loadedPercentageInt + "%";
        final String builtPercentageOfString = builtPercentageInt + "%";
        if (RENDERING_PROGRESS_STORAGE < loadedPercentageInt) {
            FastLoad.LOGGER.info("World Chunk Sending: " + loadedPercentageOfString);
        }
        if (BUILDING_PROGRESS_STORAGE < builtPercentageInt) {
            FastLoad.LOGGER.info("Visible Chunk Building: " + builtPercentageOfString);
        }
        RENDERING_PROGRESS_STORAGE = loadedPercentageInt;
        BUILDING_PROGRESS_STORAGE = builtPercentageInt;
        DrawableHelper.drawCenteredText(matrices, this.textRenderer, text, this.width / 2, this.height / 2 - heightUpFromCentre, white);
        DrawableHelper.drawCenteredText(
                matrices,
                this.textRenderer,
                "Preparing All Chunks: " + loadedPercentageOfString,
                width / 2,
                height / 2 - heightUpFromCentre + 15,
                white);
        DrawableHelper.drawCenteredText(
                matrices,
                this.textRenderer,
                "Building Visible Chunks: " + builtPercentageOfString,
                width / 2,
                height / 2 - heightUpFromCentre + 30,
                white);
        super.render(matrices, mouseX, mouseY, delta);
    }
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
