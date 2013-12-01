package org.easygson;

import com.google.gson.JsonNull;

public class WrappedNull extends WrappedElement<JsonNull> {

    public static final WrappedNull NULL = new WrappedNull();

    private WrappedNull() {
        super(JsonNull.INSTANCE);
    }

    @Override
    public boolean isNull() {
        return true;
    }

}
