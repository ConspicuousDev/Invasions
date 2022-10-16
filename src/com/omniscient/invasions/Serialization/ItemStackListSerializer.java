package com.omniscient.invasions.Serialization;

import com.google.gson.*;
import com.omniscient.omnicore.Utils.Serialization.ItemStackSerialization;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ItemStackListSerializer implements JsonSerializer<List<ItemStack>>, JsonDeserializer<List<ItemStack>> {
    @Override
    public JsonElement serialize(List<ItemStack> itemStacks, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(ItemStackSerialization.itemStackArrayToBase64(itemStacks.toArray(new ItemStack[]{})));
    }

    @Override
    public List<ItemStack> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return new ArrayList<>(Arrays.asList(ItemStackSerialization.itemStackArrayFromBase64(jsonElement.getAsString())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
