package org.easygson;

import com.google.gson.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Robert Bor
 */
public class WrappedElement {

    public static final WrappedElement NULL = new WrappedElement(JsonNull.INSTANCE);

    /** the current JSON element */
    private JsonElement json;

    public static WrappedElement createArray() {
        return new WrappedElement(new JsonArray());
    }

    public static WrappedElement createObject() {
        return new WrappedElement(new JsonObject());
    }

    public WrappedElement(JsonElement json) {
        this.json = json;
    }

    public WrappedElement(String jsonString) {
        this.json = new JsonParser().parse(jsonString);
    }

    /**
     * Removes the element with name property from the object.
     * @param property name of the element to remove from the object
     */
    public void remove(String property) {
//        if (!isObject()) {
//            throw new JsonEntityException(this, null, "is not an object, therefore no property can be removed from it");
//        }
        ((JsonObject)json).remove(property);
    }

    public int arraySize() throws WrappedElementException {
        if (!isArray()) {
            throw new WrappedElementException("is not an array, therefore does not have an array size");
        }
        return ((JsonArray)json).size();
    }

    public WrappedElement rebuildArray(int replaceIndex, WrappedElement replaceElement) {
        JsonArray sourceArray = (JsonArray)json;
        JsonArray targetArray = new JsonArray();
        int currentIndex = 0;
        for (JsonElement sourceArrayElement : sourceArray) {
            if (replaceIndex == currentIndex) {
                if (replaceElement != null) {
                    targetArray.add(replaceElement.raw());
                }
            } else {
                targetArray.add(sourceArrayElement);
            }
            currentIndex++;
        }
        return new WrappedElement(targetArray);
    }

    public WrappedElement getAtIndex(int index) throws WrappedElementException {
        JsonElement arrayElement = ((JsonArray)json).get(index);
        if (arrayElement == null) {
            throw new WrappedElementException("array element does not exist");
        }
        return new WrappedElement(arrayElement);
    }

    public WrappedElement get(String property) throws WrappedElementException {
        if (!isObject()) {
            throw new WrappedElementException("is not an object, so the property could not be fetched");
        }
        JsonElement element = ((JsonObject) json).get(property);
        return element != null ? new WrappedElement(element) : null;
    }

    public void linkToObject(String property, WrappedElement jsonEntity) {
        ((JsonObject)json).add(property, jsonEntity.raw());
    }

    public void linkToArray(int index, WrappedElement jsonEntity) {
        ((JsonArray)json).add(jsonEntity.raw());
    }

    public JsonElement raw() {
        return this.json;
    }

    public char asCharacter() throws WrappedElementException {
        primitiveCheck();
        return json.getAsCharacter();
    }

    public boolean asBoolean() throws WrappedElementException {
        primitiveCheck();
        return json.getAsBoolean();
    }

    public String asString() throws WrappedElementException {
        primitiveCheck();
        return json.getAsString();
    }

    public double asDouble() throws WrappedElementException {
        primitiveCheck();
        return json.getAsDouble();
    }

    public float asFloat() throws WrappedElementException {
        primitiveCheck();
        return json.getAsFloat();
    }

    public short asShort() throws WrappedElementException {
        primitiveCheck();
        return json.getAsShort();
    }

    public int asInt() throws WrappedElementException {
        primitiveCheck();
        return json.getAsInt();
    }

    public byte asByte() throws WrappedElementException {
        primitiveCheck();
        return json.getAsByte();
    }

    public BigDecimal asBigDecimal() throws WrappedElementException {
        primitiveCheck();
        return json.getAsBigDecimal();
    }

    public BigInteger asBigInteger() throws WrappedElementException {
        primitiveCheck();
        return json.getAsBigInteger();
    }

    private void primitiveCheck() throws WrappedElementException {
        if (!(json instanceof JsonPrimitive)) {
            throw new WrappedElementException("is not a primitive");
        }
    }

    public boolean isArray() {
        return json.isJsonArray();
    }

    public boolean isPrimitive() {
        return json.isJsonPrimitive();
    }

    public boolean isObject() {
        return json.isJsonObject();
    }

    public boolean isNull() {
        return json.isJsonNull();
    }

    public List<WrappedElement> list() throws WrappedElementException {
        if (json == JsonNull.INSTANCE) {
            return Collections.emptyList();
        }
        if (!isArray()) {
            throw new WrappedElementException("is not an array, therefore cannot be iterated over");
        }
        JsonArray array = ((JsonArray)json);
        List<WrappedElement> wrappedElements = new ArrayList<WrappedElement>(array.size());
        for (JsonElement anArray : array) {
            wrappedElements.add(new WrappedElement(anArray));
        }
        return wrappedElements;
    }

    public static WrappedElement createPrimitiveBoolean(Boolean value) {
        return value == null ? NULL : new WrappedElement(new JsonPrimitive(value));
    }

    public static WrappedElement createPrimitiveNumber(Number value) {
        return value == null ? NULL : new WrappedElement(new JsonPrimitive(value));
    }

    public static WrappedElement createPrimitiveString(String value) {
        return value == null ? NULL : new WrappedElement(new JsonPrimitive(value));
    }

    public static WrappedElement createPrimitiveCharacter(Character value) {
        return value == null ? NULL : new WrappedElement(new JsonPrimitive(value));
    }

}
