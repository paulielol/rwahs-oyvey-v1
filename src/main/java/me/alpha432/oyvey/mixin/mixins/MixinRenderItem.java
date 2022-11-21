package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.features.modules.render.EnchantColor;
import net.minecraft.client.renderer.RenderItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RenderItem.class)
public class MixinRenderItem {

    @ModifyArg(method = "renderEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"))
    public int getColor(int hexColor) {
        return EnchantColor.INSTANCE.isEnabled() ?
                ((0xFF) << 24) |
                        ((EnchantColor.INSTANCE.red.getValue() & 0xFF) << 16) |
                        ((EnchantColor.INSTANCE.green.getValue() & 0xFF) << 8)|
                        ((EnchantColor.INSTANCE.blue.getValue() & 0xFF))
                : hexColor;
    }

}
