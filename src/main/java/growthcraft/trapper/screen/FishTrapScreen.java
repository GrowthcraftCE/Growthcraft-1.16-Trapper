package growthcraft.trapper.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import growthcraft.trapper.GrowthcraftTrapper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * FishTrapScreen is a GUI screen for displaying the Fish Trap interface.
 * It handles the rendering of the GUI elements, including the background, labels, and tooltips.
 * <p>
 * This class extends AbstractContainerScreen<FishTrapMenu> and utilizes Minecraft's GuiGraphics for rendering.
 */
public class FishTrapScreen extends AbstractContainerScreen<FishTrapMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            GrowthcraftTrapper.MODID, "textures/gui/fish_trap_screen.png");

    public FishTrapScreen(FishTrapMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    /**
     * Renders the background of the FishTrapScreen, including the texture setup and position calculations.
     *
     * @param poseStack   the graphics context used for rendering.
     * @param partialTick the partial tick time.
     * @param mouseX      the X coordinate of the mouse cursor.
     * @param mouseY      the Y coordinate of the mouse cursor.
     */
    @Override
    protected void renderBg(GuiGraphics poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        poseStack.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    /**
     * Renders the FishTrapScreen GUI, background, and tooltips.
     *
     * @param poseStack the graphics context used for rendering.
     * @param mouseX    the X coordinate of the mouse cursor.
     * @param mouseY    the Y coordinate of the mouse cursor.
     * @param delta     the frame delta time.
     */
    @Override
    public void render(GuiGraphics poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack, mouseX, mouseY, delta);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    /**
     * Renders the labels on the FishTrapScreen GUI, such as the screen title and the player's inventory title.
     *
     * @param guiGraphics the graphics context used for rendering.
     * @param offsetX     the x-coordinate offset used for rendering.
     * @param offsetY     the y-coordinate offset used for rendering.
     */
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int offsetX, int offsetY) {
        guiGraphics.drawString(this.font, this.title,
                this.titleLabelX, this.titleLabelY, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle,
                this.inventoryLabelX, this.inventoryLabelY - 32, 4210752, false);
    }
}
