package cn.zbx1425.mtrsteamloco.mixin;

import cn.zbx1425.mtrsteamloco.ClientConfig;
import cn.zbx1425.mtrsteamloco.MainClient;
import cn.zbx1425.mtrsteamloco.render.RenderUtil;
import cn.zbx1425.sowcer.util.GLStateCapture;
import com.mojang.blaze3d.vertex.PoseStack;
import cn.zbx1425.sowcer.math.Matrix4f;
import mtr.screen.ResourcePackCreatorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourcePackCreatorScreen.class)
public class ResourcePackCreatorScreenMixin {

    @Shadow(remap = false)
    private static int guiCounter;

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At("TAIL"))
    private static void render(PoseStack matrices, CallbackInfo ci) {
        if (guiCounter == 0) return;

        final MultiBufferSource.BufferSource immediate = Minecraft.getInstance().renderBuffers().bufferSource();
        MainClient.drawScheduler.commit(immediate, ClientConfig.useRenderOptimization(), MainClient.profiler);
    }
}
