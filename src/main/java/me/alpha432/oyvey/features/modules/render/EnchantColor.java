package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class EnchantColor extends Module {
    public Setting<Integer> red = this.register(new Setting<>("Red", 255, 1, 255));
    public Setting<Integer> green = this.register(new Setting<>("Green", 255, 1, 255));
    public Setting<Integer> blue = this.register(new Setting<>("Blue", 255, 1, 255));

    public EnchantColor() {
        super("EnchantColor", "does item changing things.", Category.RENDER, false, false, false);

    }
    public static EnchantColor INSTANCE;
    {
        INSTANCE = this;
    }
}