package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Environment(EnvType.CLIENT)
@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Inject(method = "load", at = @At("TAIL"))
    public void generateDebugRebinds(CallbackInfo _info) {
        RebindAllTheKeys.updateDebugKeybinds();
    }

    @Redirect(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/options/KeyBinding;setBoundKey(Lnet/minecraft/client/util/InputUtil$Key;)V"))
    public void fixKeybindsAfterAmecsIsRemoved(KeyBinding keyBinding, InputUtil.Key key) {

        if (!RebindAllTheKeys.isAmecsInstalled && keyBinding.getCategory().equals("rebind_all_the_keys.keybind_group.debug") && !keyBinding.getTranslationKey().equals("rebind_all_the_keys.keybind.debug_key") && key.getCode() > -1) {
            String prefix = key.getCategory() == InputUtil.Type.MOUSE ? "key.mouse." : "key.keyboard.";
            keyBinding.setBoundKey(InputUtil.fromTranslationKey(prefix + -key.getCode()));
        }
        else
            keyBinding.setBoundKey(key);

    }

}
