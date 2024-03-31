package cn.ksmcbrigade.XB;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

@Mod("xb")
public class XiBao {

    public static boolean init = false;
    public static ArrayList<String> arrayList = new ArrayList<>();
    public static final ResourceLocation LOCATION = new ResourceLocation("xb", "textures/xibao.png");

    public XiBao() throws IOException {
        MinecraftForge.EVENT_BUS.register(this);
        init();
    }

    public static void init() throws IOException{
        File file = FMLPaths.CONFIGDIR.get().resolve("xb-config.json").toFile();

        if(!file.exists()){
            JsonArray array = new JsonArray();
            array.add(DisconnectedScreen.class.getName());
            array.add(DeathScreen.class.getName());
            Files.write(file.toPath(),array.toString().getBytes());
        }
        JsonArray classes = new JsonParser().parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonArray();
        classes.forEach(e -> arrayList.add(e.getAsString()));
        init = true;
    }

    public static boolean can(Screen screen){
        boolean ret = false;
        for (String e:arrayList){
            if(screen.getClass().getName().equals(e)){
                ret = true;
                break;
            }
        }
        return ret;
    }
}
