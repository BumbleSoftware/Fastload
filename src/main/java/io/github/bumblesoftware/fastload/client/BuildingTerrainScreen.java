package io.github.bumblesoftware.fastload.client;

import io.github.bumblesoftware.fastload.compat.bedrockify.BedrockifyCompat.Context;
import io.github.bumblesoftware.fastload.config.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.ObjectHolder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static io.github.bumblesoftware.fastload.compat.bedrockify.BedrockifyCompat.BEDROCKIFY_COMPAT_EVENT;
import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;
import static io.github.bumblesoftware.fastload.util.FLColourConstants.WHITE;

public class BuildingTerrainScreen extends Screen {
    private final Text screenName;
    private final Text screenTemplate;
    private final Text buildingChunks;
    private final Text preparingChunks;
    private Integer preparedProgressStorage = 0;
    private Integer buildingProgressStorage = 0;
    private static final int heightUpFromCentre = 50;
    public final int loadingAreaGoal;
    private Runnable runnable;

    private Integer getLoadedChunkCount() {
        return ABSTRACTED_CLIENT.getClientWorld() != null ? ABSTRACTED_CLIENT.getLoadedChunkCount() : 0;
    }
    private Integer getBuiltChunkCount() {
        return ABSTRACTED_CLIENT.getClientWorld() != null ? ABSTRACTED_CLIENT.getCompletedChunkCount() : 0;
    }

    /**
     * Texts to draw
     */
    public BuildingTerrainScreen(final int loadingAreaGoal) {
        super(NarratorManager.EMPTY);
        this.loadingAreaGoal = loadingAreaGoal;
        screenName = ABSTRACTED_CLIENT.newTranslatableText("menu.generatingTerrain");
        screenTemplate = ABSTRACTED_CLIENT.newTranslatableText("fastload.screen.buildingTerrain.template");
        buildingChunks = ABSTRACTED_CLIENT.newTranslatableText("fastload.screen.buildingTerrain.building");
        preparingChunks = ABSTRACTED_CLIENT.newTranslatableText("fastload.screen.buildingTerrain.preparing");
    }

    public BuildingTerrainScreen() {
        this(FLMath.getLocalRenderChunkArea());
    }

    /**
     * Renders screen texts
     */
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        final String loadedChunksString =
                getLoadedChunkCount() + "/"  + loadingAreaGoal;
        final String builtChunksString =
                getBuiltChunkCount() + "/"  + loadingAreaGoal;

        ABSTRACTED_CLIENT.renderScreenBackgroundTexture(this, 0, matrices);

        if (preparedProgressStorage < getLoadedChunkCount())
            Fastload.LOGGER.info("World Chunk Loading: " + loadedChunksString);
        if (buildingProgressStorage < getBuiltChunkCount())
            Fastload.LOGGER.info("World Chunk Building: " + builtChunksString);

        preparedProgressStorage = getLoadedChunkCount();
        buildingProgressStorage = getBuiltChunkCount();

        if (BEDROCKIFY_COMPAT_EVENT.isNotEmpty()) {
            final var shouldContinue = new ObjectHolder<>(true);
            BEDROCKIFY_COMPAT_EVENT.fireEvent(
                    new Context(
                            shouldContinue,
                            ABSTRACTED_CLIENT,
                            matrices,
                            screenName,
                            screenTemplate,
                            preparingChunks,
                            buildingChunks,
                            loadedChunksString,
                            builtChunksString,
                            this::getLoadedChunkCount,
                            this::getBuiltChunkCount,
                            loadingAreaGoal
                    )
            );
            if (!shouldContinue.heldObj)
                return;
        }

        if (getLoadedChunkCount() == 0 && getBuiltChunkCount() == 0) {
            ABSTRACTED_CLIENT.drawCenteredText(
                    matrices,
                    this.textRenderer,
                    ABSTRACTED_CLIENT.newTranslatableText("fastload.buildingTerrain.starting"),
                    this.width / 2,
                    this.height / 2 - heightUpFromCentre,
                    WHITE
            );
            return;
        }

        ABSTRACTED_CLIENT.drawCenteredText(
                matrices,
                this.textRenderer,
                screenName,
                this.width / 2,
                this.height / 2 - heightUpFromCentre,
                WHITE
        );

        ABSTRACTED_CLIENT.drawCenteredText(
                matrices,
                this.textRenderer,
                screenTemplate,
                this.width / 2,
                this.height / 2 - heightUpFromCentre + 30,
                WHITE
        );

        ABSTRACTED_CLIENT.drawCenteredText(
                matrices,
                this.textRenderer,
                 preparingChunks.getString() + ": " + loadedChunksString,
                width / 2,
                height / 2 - heightUpFromCentre + 45,
                WHITE);

        ABSTRACTED_CLIENT.drawCenteredText(
                matrices,
                this.textRenderer,
                buildingChunks.getString() + ": " + builtChunksString,
                width / 2,
                height / 2 - heightUpFromCentre + 60,
                WHITE);

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
