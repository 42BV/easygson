package org.easygson;

import com.google.gson.JsonElement;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.easygson.WrappedNull.NULL;

/**
 *
 * @author Robert Bor
 */
public abstract class WrappedElement<T extends JsonElement> {

    /** the current JSON element */
    protected T json;

    public static WrappedElement createArray() {
        return new WrappedArray();
    }

    public static WrappedElement createObject() {
        return new WrappedObject();
    }

    public WrappedElement(T json) {
        this.json = json;
    }

    public void remove(String property) throws WrappedElementException {
        throw new WrappedElementException("is not an object, therefore no property can be removed from it");
    }

    public int arraySize() throws WrappedElementException {
        throw new WrappedElementException("is not an array, therefore does not have an array size");
    }

    public WrappedElement rebuildArray(int replaceIndex, WrappedElement replaceElement) throws WrappedElementException {
        throw new WrappedElementException("is not an array, therefore the array can not be rebuild");
    }

    public WrappedElement getAtIndex(int index) throws WrappedElementException {
        throw new WrappedElementException("is not an array, therefore the element cannot be fetched from the index position");
    }

    public WrappedElement get(String property) throws WrappedElementException {
        throw new WrappedElementException("is not an object, so the property could not be fetched");
    }

    public void linkToObject(String property, WrappedElement jsonEntity) throws WrappedElementException {
        throw new WrappedElementException("is not an array, there the element cannot be stored with the key");
    }

    public void linkToArray(int index, WrappedElement jsonEntity) throws WrappedElementException {
        throw new WrappedElementException("is not an array, there the element cannot be stored at the index location");
    }

    public List<WrappedElement> list() throws WrappedElementException {
        return Collections.<WrappedElement> emptyList();
    }

    public char asCharacter() throws WrappedElementException {
        throw new WrappedElementException("is not a primitive");
    }

    public boolean asBoolean() throws WrappedElementException {
        throw new WrappedElementException("is not a primitive");
    }

    public String asString() throws WrappedElementException {
        throw new WrappedElementException("is not a primitive");
    }

    public double asDouble() throws WrappedElementException {
        throw new WrappedElementException("is not a primitive");
    }

    public float asFloat() throws WrappedElementException {
        throw new WrappedElementException("is not a primitive");
    }

    public short asShort() throws WrappedElementException {
        throw new WrappedElementException("is not a primitive");
    }

    public int asInt() throws WrappedElementException {
        throw new WrappedElementException("is not a primitive");
    }

    public byte asByte() throws WrappedElementException {
        throw new WrappedElementException("is not a primitive");
    }

    public BigDecimal asBigDecimal() throws WrappedElementException {
        throw new WrappedElementException("is not a primitive");
    }

    public BigInteger asBigInteger() throws WrappedElementException {
        throw new WrappedElementException("is not a primitive");
    }

    public boolean isArray() {
        return false;
    }

    public boolean isPrimitive() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public boolean fluentPlayer() {
        return false;
    }

    public static WrappedElement createPrimitiveBoolean(Boolean value) {
        return value == null ? NULL : new WrappedPrimitive(value);
    }

    public static WrappedElement createPrimitiveNumber(Number value) {
        return value == null ? NULL : new WrappedPrimitive(value);
    }

    public static WrappedElement createPrimitiveString(String value) {
        return value == null ? NULL : new WrappedPrimitive(value);
    }

    public static WrappedElement createPrimitiveCharacter(Character value) {
        return value == null ? NULL : new WrappedPrimitive(value);
    }

    public JsonElement raw() {
        return this.json;
    }

}
