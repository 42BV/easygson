package org.easygson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class WrappedArray extends WrappedElement<JsonArray> {

    public WrappedArray() {
        this(new JsonArray());
    }

    public WrappedArray(JsonArray json) {
        super(json);
    }

    @Override
    public int arraySize() throws WrappedElementException {
        return json.size();
    }

    @Override
    public WrappedArray rebuildArray(int replaceIndex, WrappedElement replaceElement) {
        JsonArray sourceArray = json;
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
        return new WrappedArray(targetArray);
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public void linkToArray(int index, WrappedElement jsonEntity) throws WrappedElementException {
        json.add(jsonEntity.raw());
    }

    @Override
    public WrappedElement getAtIndex(int index) throws WrappedElementException {
        JsonElement arrayElement = json.get(index);
        if (arrayElement == null) {
            throw new WrappedElementException("array element does not exist");
        }
        return WrapFactory.wrap(arrayElement);
    }

    @Override
    public List<WrappedElement> list() throws WrappedElementException {
//        if (json == JsonNull.INSTANCE) {
//            return Collections.emptyList();
//        }
        JsonArray array = ((JsonArray)json);
        List<WrappedElement> wrappedElements = new ArrayList<WrappedElement>(array.size());
        for (JsonElement anArray : array) {
            wrappedElements.add(WrapFactory.wrap(anArray));
        }
        return wrappedElements;
    }

    @Override
    public boolean fluentPlayer() {
        return true;
    }

}
