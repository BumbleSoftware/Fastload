package io.github.bumblesoftware.fastload.client;

import io.github.bumblesoftware.fastload.api.internal.abstraction.AbstractClientCalls;
import io.github.bumblesoftware.fastload.init.Fastload;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static io.github.bumblesoftware.fastload.init.FastloadClient.MINECRAFT_ABSTRACTION_HANDLER;
import static io.github.bumblesoftware.fastload.util.FLColourConstants.WHITE;

public class BuildingTerrainScreen extends Screen {
    public static final AbstractClientCalls ABSTRACTED_CLIENT = MINECRAFT_ABSTRACTION_HANDLER.directory.getAbstractedEntries();
    private static final int HEIGHT_UP_FROM_CENTRE = 50;
    private final Text screenName;
    private final Text screenTemplate;
    private final Text buildingChunks;
    private final Text preparingChunks;
    private final Text startingSession;
    private Integer preparedProgressStorage = 0;
    private Integer buildingProgressStorage = 0;
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
        startingSession = ABSTRACTED_CLIENT.newTranslatableText("fastload.buildingTerrain.starting");
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

        if (getLoadedChunkCount() == 0 && getBuiltChunkCount() == 0) {
            ABSTRACTED_CLIENT.drawCenteredText(
                    matrices,
                    this.textRenderer,
                    startingSession,
                    this.width / 2,
                    this.height / 2 - HEIGHT_UP_FROM_CENTRE,
                    WHITE
            );
            return;
        }

        ABSTRACTED_CLIENT.drawCenteredText(
                matrices,
                this.textRenderer,
                screenName,
                this.width / 2,
                this.height / 2 - HEIGHT_UP_FROM_CENTRE,
                WHITE
        );

        ABSTRACTED_CLIENT.drawCenteredText(
                matrices,
                this.textRenderer,
                screenTemplate,
                this.width / 2,
                this.height / 2 - HEIGHT_UP_FROM_CENTRE + 30,
                WHITE
        );

        ABSTRACTED_CLIENT.drawCenteredText(
                matrices,
                this.textRenderer,
                 preparingChunks.getString() + ": " + loadedChunksString,
                width / 2,
                height / 2 - HEIGHT_UP_FROM_CENTRE + 45,
                WHITE);

        ABSTRACTED_CLIENT.drawCenteredText(
                matrices,
                this.textRenderer,
                buildingChunks.getString() + ": " + builtChunksString,
                width / 2,
                height / 2 - HEIGHT_UP_FROM_CENTRE + 60,
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
