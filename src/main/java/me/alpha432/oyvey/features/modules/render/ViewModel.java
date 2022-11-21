package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class ViewModel extends Module {
    public Setting<Integer> translateX = this.register(new Setting<>("TranslateX", 0, -100, 100));
    public Setting<Integer> translateY = this.register(new Setting<>("TranslateY", 0, -100, 100));
    public Setting<Integer> translateZ = this.register(new Setting<>("TranslateZ", 0, -100, 100));

    public Setting<Integer> rotateX = this.register(new Setting<>("RotateX", 0, -100, 100));
    public Setting<Integer> rotateY = this.register(new Setting<>("RotateY", 0, -100, 100));
    public Setting<Integer> rotateZ = this.register(new Setting<>("RotateZ", 0, -100, 100));

    public Setting<Integer> scaleX = this.register(new Setting<>("ScaleX", 100, 0, 100));
    public Setting<Integer> scaleY = this.register(new Setting<>("ScaleY", 100, 0, 100));
    public Setting<Integer> scaleZ = this.register(new Setting<>("ScaleZ", 100, 0, 100));

    public ViewModel() {
        super("ViewModel", "does item changing things.", Category.RENDER, false, false, false);
    }

    public static ViewModel INSTANCE;
    {
        INSTANCE = this;
    }

}
