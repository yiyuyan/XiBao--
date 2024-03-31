package cn.ksmcbrigade.XB.mixin;

import cn.ksmcbrigade.XB.XiBao;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Files;

@Mixin(RenderSystem.class)
public abstract class EVENT {

    @Inject(method = "pushMatrix",at = @At("TAIL"))
    private static void render(CallbackInfo ci) throws IOException {
        if(Minecraft.getInstance().level!=null){
            if(!XiBao.init){
                XiBao.init();
            }
            Screen s = Minecraft.getInstance().screen;
            boolean showXibao = !Files.exists(FMLPaths.GAMEDIR.get().resolve(".xibao_stop"));
            if (s!=null && showXibao && XiBao.can(s)) {
                Tessellator tesselator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tesselator.getBuilder();
                Minecraft.getInstance().getTextureManager().bind(XiBao.LOCATION);
            /*if(bufferbuilder.building()){
                bufferbuilder.end();
            }*/
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.vertex(0.0D, s.height, 0.0D).uv(0F, 1F).color(255, 255, 255, 255).endVertex();
                bufferbuilder.vertex(s.width, s.height, 0.0D).uv(1F, 1F).color(255, 255, 255, 255).endVertex();
                bufferbuilder.vertex(s.width, 0.0D, 0.0D).uv(1F, 0F).color(255, 255, 255, 255).endVertex();
                bufferbuilder.vertex(0.0D, 0.0D, 0.0D).uv(0F, 0F).color(255, 255, 255, 255).endVertex();
                tesselator.end();
            }
        }
    }
}
