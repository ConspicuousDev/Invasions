package com.omniscient.invasions.Serialization;

import com.google.gson.*;
import com.omniscient.omnicore.Utils.Serialization.ItemStackSerialization;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.lang.reflect.Type;


public class ItemStackArraySerializer implements JsonSerializer<ItemStack[]>, JsonDeserializer<ItemStack[]> {
    @Override
    public JsonElement serialize(ItemStack[] itemStacks, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(ItemStackSerialization.itemStackArrayToBase64(itemStacks));
    }

    @Override
    public ItemStack[] deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return ItemStackSerialization.itemStackArrayFromBase64(jsonElement.getAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ItemStack[0];
    }
}
