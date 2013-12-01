package org.easygson;

import com.google.gson.JsonPrimitive;

import java.math.BigDecimal;
import java.math.BigInteger;

public class WrappedPrimitive extends WrappedElement<JsonPrimitive> {

    public WrappedPrimitive(Boolean value) {
        super(new JsonPrimitive(value));
    }

    public WrappedPrimitive(Number value) {
        super(new JsonPrimitive(value));
    }

    public WrappedPrimitive(String value) {
        super(new JsonPrimitive(value));
    }

    public WrappedPrimitive(Character value) {
        super(new JsonPrimitive(value));
    }

    public WrappedPrimitive(JsonPrimitive json) {
        super(json);
    }

    @Override
    public char asCharacter() throws WrappedElementException {
        return json.getAsCharacter();
    }

    @Override
    public boolean asBoolean() throws WrappedElementException {
        return json.getAsBoolean();
    }

    @Override
    public String asString() throws WrappedElementException {
        return json.getAsString();
    }

    @Override
    public double asDouble() throws WrappedElementException {
        return json.getAsDouble();
    }

    @Override
    public float asFloat() throws WrappedElementException {
        return json.getAsFloat();
    }

    @Override
    public short asShort() throws WrappedElementException {
        return json.getAsShort();
    }

    @Override
    public int asInt() throws WrappedElementException {
        return json.getAsInt();
    }

    @Override
    public byte asByte() throws WrappedElementException {
        return json.getAsByte();
    }

    @Override
    public BigDecimal asBigDecimal() throws WrappedElementException {
        return json.getAsBigDecimal();
    }

    @Override
    public BigInteger asBigInteger() throws WrappedElementException {
        return json.getAsBigInteger();
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

}
