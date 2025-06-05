package net.reconhalcyon.reconsilkspawners.util;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class ModUtils {
    /**
     * Checks if the given ItemStack has the Silk Touch enchantment.
     *
     * @param itemStack The ItemStack to check.
     * @return true if the ItemStack has Silk Touch, false otherwise.
     */
    public static boolean hasSilkTouch(ItemStack itemStack, RegistryAccess registryAccess) {
        var enchantmentRegistry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> silkTouch = enchantmentRegistry.getHolderOrThrow(Enchantments.SILK_TOUCH);

        return itemStack.getEnchantmentLevel(silkTouch) > 0;
    }
}
