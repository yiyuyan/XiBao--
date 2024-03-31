package cn.ksmcbrigade.XB.mixin;

import cn.ksmcbrigade.XB.XiBao;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mixin(Screen.class)
public abstract class EVENT2 {
    @Shadow
    protected abstract <T extends IGuiEventListener> T addWidget(T p_230481_1_);

    @Shadow protected abstract <T extends Widget> T addButton(T p_230480_1_);

    @Inject(method = "init()V",at = @At("TAIL"))
    public void init(CallbackInfo ci) throws IOException {
        if(!XiBao.init){
            XiBao.init();
        }
        boolean showXibao = !Files.exists(FMLPaths.GAMEDIR.get().resolve(".xibao_stop"));
        Screen s = Minecraft.getInstance().screen;
        if (s!=null && showXibao && XiBao.can(s)) {
            Button disableXibao = new Button(s.width / 2 - 75, s.height - 30, 150, 20, new TranslationTextComponent("xibao.do_not_show_again"), btn -> {
                Path gameDir = FMLPaths.GAMEDIR.get();
                try {
                    Files.write(gameDir.resolve(".xibao_stop"), "Remove this file to show Xibao again".getBytes());
                } catch (IOException e) {
                    return;
                }
                btn.active = false;
            });
            this.addButton(disableXibao);
        }
    }

    @Inject(method = "renderBackground(Lcom/mojang/blaze3d/matrix/MatrixStack;I)V",at = @At("TAIL"))
    public void background(MatrixStack p_238651_1_, int p_238651_2_, CallbackInfo ci) throws IOException {
        if(!XiBao.init){
            XiBao.init();
        }
        boolean showXibao = !Files.exists(FMLPaths.GAMEDIR.get().resolve(".xibao_stop"));
        Screen s = Minecraft.getInstance().screen;
        if (s!=null && showXibao && XiBao.can(s)) {
            Tessellator tesselator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            Minecraft.getInstance().getTextureManager().bind(XiBao.LOCATION);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.vertex(0.0D, s.height, 0.0D).uv(0F, 1F).color(255, 255, 255, 255).endVertex();
            bufferbuilder.vertex(s.width, s.height, 0.0D).uv(1F, 1F).color(255, 255, 255, 255).endVertex();
            bufferbuilder.vertex(s.width, 0.0D, 0.0D).uv(1F, 0F).color(255, 255, 255, 255).endVertex();
            bufferbuilder.vertex(0.0D, 0.0D, 0.0D).uv(0F, 0F).color(255, 255, 255, 255).endVertex();
            tesselator.end();
        }
    }
}
