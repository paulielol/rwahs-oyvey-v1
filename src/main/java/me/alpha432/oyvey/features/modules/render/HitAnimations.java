package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.entity.*;
import net.minecraft.item.*;
import net.minecraft.client.renderer.*;

public class HitAnimations extends Module
{

    public HitAnimations() {
        super("HitAnimations", "fills holes", Category.RENDER, true, false, false);
    }

    public Setting<Boolean> sword = this.register(new Setting<Boolean>("OnlySword", false));

    public void onUpdate() {
        boolean boo = false;
        Label_0080: {
            if (this.sword.getValue()) {
                final EntityPlayerSP Sp = HitAnimations.mc.player;
                final ItemStack Stack = Sp.getHeldItemMainhand();
                if (Stack.getItem() instanceof ItemSword) {
                    final EntityRenderer Entity = HitAnimations.mc.entityRenderer;
                    final ItemRenderer Item = Entity.itemRenderer;
                    if (Item.prevEquippedProgressMainHand >= 0.9) {
                        boo = true;
                        break Label_0080;
                    }
                }
                final ItemStack var70 = Sp.getHeldItemMainhand();
                boo = false;
            }
            else {
                boo = true;
            }
        }
        if (boo) {
            EntityRenderer Entity = HitAnimations.mc.entityRenderer;
            ItemRenderer Item = Entity.itemRenderer;
            Item.equippedProgressMainHand = 1.0f;
            Entity = HitAnimations.mc.entityRenderer;
            Item = Entity.itemRenderer;
            final EntityPlayerSP var71 = HitAnimations.mc.player;
            Item.itemStackMainHand = var71.getHeldItemMainhand();
        }
    }
}
