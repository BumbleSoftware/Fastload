package io.github.bumblesoftware.fastload.abstraction.client;

import com.mojang.serialization.Codec;
import io.github.bumblesoftware.fastload.abstraction.tool.RetrieveValueFunction;
import io.github.bumblesoftware.fastload.abstraction.tool.ScreenProvider;
import io.github.bumblesoftware.fastload.abstraction.tool.StoreValueFunction;
import io.github.bumblesoftware.fastload.client.BuildingTerrainScreen;
import io.github.bumblesoftware.fastload.compat.modmenu.FLConfigScreenButtons;
import io.github.bumblesoftware.fastload.config.DefaultConfig;
import io.github.bumblesoftware.fastload.config.FLConfig;
import io.github.bumblesoftware.fastload.mixin.client.ScreenAccess;
import io.github.bumblesoftware.fastload.util.Action;
import io.github.bumblesoftware.fastload.util.Bound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.function.Function;

import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

public class Client1192 implements AbstractClientCalls {

    @Override
    public MinecraftClient getClientInstance() {
        return MinecraftClient.getInstance();
    }

    @Override
    public ClientWorld getClientWorld() {
        return getClientInstance().world;
    }

    @Override
    public <T> Screen newConfigScreen(
            final Screen parent,
            GameOptions gameOptions,
            final Text title,
            Function<Object[], T[]> options,
            Action config
    ) {
        final var main = this;
        return new SimpleOptionsScreen(
                parent,
                gameOptions,
                title,
                (SimpleOption<?>[]) options.apply(new SimpleOption[]{})
        ) {
            @Override
            protected void initFooter() {
                main.initFooter(this, parent, config);
            }
        };
    }

    protected void initFooter(final Screen current, final Screen parent, Action config) {
        addDrawableChild(current,
                getNewButton(
                        current.width / 2 - 100,
                        current.height - 27,
                        200, 20,
                        ScreenTexts.DONE,
                        (button) -> {
                            config.commit();
                            getClientInstance().setScreen(parent);
                        })
        );
    }

    @Override
    public Screen newFastloadConfigScreen(final Screen parent) {
        return newConfigScreen(
                parent,
                getClientInstance().options,
                ABSTRACTED_CLIENT.newTranslatableText("fastload.screen.config"),
                objects -> ABSTRACTED_CLIENT.newFLConfigScreenButtons().getAllOptions(objects),
                FLConfig::writeToDisk
        );
    }

    @Override
    public Screen newBuildingTerrainScreen(final int loadingAreaGoal) {
        return new BuildingTerrainScreen(loadingAreaGoal);
    }

    @Override
    public Screen getCurrentScreen() {
        return getClientInstance().currentScreen;
    }

    @Override
    public Text newTranslatableText(final String content) {
        return Text.translatable(content);
    }

    @Override
    public Text newLiteralText(final String content) {
        return Text.literal(content);
    }

    @Override
    public <T1 extends Element & Drawable & Selectable> T1 addDrawableChild(final Screen screen, final T1 drawableElement) {
        return ((ScreenAccess) screen).addDrawableChildProxy(drawableElement);
    }

    @Override
    public <T> FLConfigScreenButtons<T> newFLConfigScreenButtons() {
        return (FLConfigScreenButtons<T>) new FLConfigScreenButtons<SimpleOption>();
    }

    @Override
    public ButtonWidget getNewButton(
            final int x,
            final int y,
            final int width,
            final int height,
            final Text message,
            final ButtonWidget.PressAction onPress
    ) {
        return new ButtonWidget(x, y, width, height, message, onPress);
    }

    @SuppressWarnings("unchecked")
    @Override
    public SimpleOption<Boolean> newCyclingButton(
            final String namespace,
            final String identifier,
            final RetrieveValueFunction retrieveValueFunction,
            final StoreValueFunction storeValueFunction
    ) {
        return SimpleOption.ofBoolean(
                namespace + identifier,
                SimpleOption.constantTooltip(newTranslatableText(namespace + identifier + ".tooltip")),
                Boolean.parseBoolean(retrieveValueFunction.getValue(identifier)),
                aBoolean -> storeValueFunction.setValue(identifier, Boolean.toString(aBoolean))
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public SimpleOption<Integer> newSlider(
            final String namespace,
            final String identifier,
            final RetrieveValueFunction retrieveValueFunction,
            final StoreValueFunction storeValueFunction,
            final Bound minMaxValues,
            final int width
    ) {
        int max = minMaxValues.max();
        int min = minMaxValues.min();
        return new SimpleOption<>(
                namespace + identifier,
                SimpleOption.constantTooltip(newTranslatableText(namespace + identifier + ".tooltip")),
                (optionText, value) -> {
                    if (value.equals(min)) {
                        return GameOptions.getGenericValueText(optionText, Text.translatable(namespace + identifier + ".min"));
                    } else if (value.equals(max)) {
                        return GameOptions.getGenericValueText(optionText,
                                Text.translatable(namespace + identifier + ".max"));
                    } else {
                        return GameOptions.getGenericValueText(optionText, value);
                    }
                },
                new SimpleOption.ValidatingIntSliderCallbacks(min, max),
                Codec.DOUBLE.xmap(value -> max, value -> (double) value - max),
                Integer.parseInt(retrieveValueFunction.getValue(identifier)),
                value -> storeValueFunction.setValue(identifier, Integer.toString(value))
        );
    }


    @Override
    public void setScreen(final Screen screen) {
        getClientInstance().setScreen(screen);
    }

    @Override
    public void renderScreenBackgroundTexture(
            final Screen screen,
            final int offset,
            final MatrixStack matrices
    ) {
        screen.renderBackgroundTexture(0);
    }

    @Override
    public void drawCenteredText(
            final MatrixStack matrices,
            final TextRenderer textRenderer,
            final Text text,
            final int centerX,
            final int y,
            final int color
    ) {
        DrawableHelper.drawCenteredText(matrices, textRenderer, text, centerX, y, color);
    }

    @Override
    public void drawCenteredText(
            final MatrixStack matrices,
            final TextRenderer textRenderer,
            final String text,
            final int centerX,
            final int y,
            final int color
    ) {
        DrawableHelper.drawCenteredText(matrices, textRenderer, text, centerX, y, color);
    }

    @Override
    public int getLoadedChunkCount() {
        return getClientWorld().getChunkManager().getLoadedChunkCount();
    }

    @Override
    public int getCompletedChunkCount() {
        return getClientInstance().worldRenderer.getCompletedChunkCount();
    }

    @Override
    public int getViewDistance() {
        if (getClientInstance().options == null) {
            return DefaultConfig.LOCAL_CHUNK_RADIUS_BOUND.max();
        } else return getClientInstance().options.getClampedViewDistance();
    }


    @Override
    public boolean isWindowFocused() {
        return getClientInstance().isWindowFocused();
    }

    @Override
    public boolean isSingleplayer() {
        return ABSTRACTED_CLIENT.getClientInstance().isInSingleplayer();
    }

    @Override
    public boolean forCurrentScreen(final ScreenProvider screenProvider) {
        return screenProvider.getCurrent(getCurrentScreen());
    }

    @Override
    public boolean isBuildingTerrainScreen(final Screen screen) {
        return screen instanceof BuildingTerrainScreen;
    }

    @Override
    public boolean isGameMenuScreen(final Screen screen) {
        return screen instanceof GameMenuScreen;
    }

    @Override
    public boolean isDownloadingTerrainScreen(final Screen screen) {
        return screen instanceof DownloadingTerrainScreen;
    }
}