package org.easygson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class WrappedObject extends WrappedElement<JsonObject> {

    public WrappedObject() {
        this(new JsonObject());
    }

    public WrappedObject(JsonObject json) {
        super(json);
    }

    @Override
    public void remove(String property) throws WrappedElementException {
        json.remove(property);
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public void linkToObject(String property, WrappedElement jsonEntity) throws WrappedElementException {
        json.add(property, jsonEntity.raw());
    }

    @Override
    public WrappedElement get(String property) throws WrappedElementException {
        JsonElement element = json.get(property);
        return element != null ? WrapFactory.wrap(element) : null;
    }

    @Override
    public boolean fluentPlayer() {
        return true;
    }

}
