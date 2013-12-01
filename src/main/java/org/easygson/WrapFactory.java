package org.easygson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import static org.easygson.WrappedNull.NULL;

public class WrapFactory {

    public static WrappedElement wrap(JsonElement json) {
        if (json == null || json.isJsonNull()) {
            return createNull();
        }
        if (json.isJsonArray()) {
            return createArray(json);
        }
        if (json.isJsonPrimitive()) {
            return createPrimitive(json);
        }
        return createObject(json);

    }

    public static WrappedElement createNull() {
        return NULL;
    }

    public static WrappedElement createArray(JsonElement json) {
        return new WrappedArray((JsonArray)json);
    }

    public static WrappedElement createPrimitive(JsonElement json) {
        return new WrappedPrimitive((JsonPrimitive)json);
    }

    public static WrappedElement createObject(JsonElement json) {
        return new WrappedObject((JsonObject)json);
    }

}
