package org.easygson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * <p>EasyGson is a wrapper for Gson, the excellent Google Java Json library. The added value of EasyGson
 * is that it will allow you to handle the Json in a more "native" manner (native to Json, not to Java).
 * This library pays off especially if you do not intend to transform to and from a Java domain model, but
 * instead chose to operate directly on the Json model itself.</p>
 *
 * <p></p>Every node (array, object or primitive) in a JSON tree is a JsonEntity. JsonEntity offers all
 * the methods available to every node type, though it checks whether the call can be made. If not,
 * an exception is thrown, with details on the branch of the JSON tree where the failure took place.</p>
 *
 * <p>Some JSON terminology:</p>
 * <ul>
 *     <li>object; a container of JSON elements, each callable through a property name</li>
 *     <li>array; a sequence of JSON elements</li>
 *     <li>primitive; a leaf within the JSON tree, contains the actual value</li>
 * </ul>
 *
 * <p>The interface of JsonEntity is fluent, meaning that if you create an array or object, it will return
 * the newly created instance. If you create a primitive, it will return the parent (the primitive itself
 * is not interesting to return).</p>
 *
 * <p>When creating objects/arrays, it is also possible to "create or get" it. This operation is called
 * "ensure". It will create the object or array if it does not exist, or otherwise return the existing
 * version.</p>
 * @author Robert Bor
 */
@SuppressWarnings("UnusedDeclaration")
public class JsonEntity implements Iterable<JsonEntity> {

    /** the JSON element that qualifies as the parent of this element. Must be either an array or an object */
    private JsonEntity parent;

    /** the current JSON element */
    private JsonElement json;

    /** the name/index of the current element within the parent */
    private String propertyName;

    /** the index of the current element within the parent */
    private int propertyIndex = -1;

    /**
     * Constructor that takes a JSON string representation and transforms it into a JsonEntity
     * @param jsonString JSON string representation
     */
    public JsonEntity(String jsonString) {
        this(new JsonParser().parse(jsonString));
    }

    /**
     * Constructor that takes a Gson JsonElement and transforms it into a JsonEntity
     * @param json Gson JsonElement
     */
    public JsonEntity(JsonElement json) {
        this(null, null, -1, json);
    }

    private JsonEntity(JsonEntity parent, String propertyName, int propertyIndex, JsonElement json) {
        this.parent = parent;
        this.json = json == null ? JsonNull.INSTANCE : json;
        this.propertyName = propertyName;
        this.propertyIndex = propertyIndex;
    }

    /**
     * Creates an array within an object. The array will be stored under the property name. If something already
     * exists under that property, it will be overwritten with this new one.
     * @param property name of the property to create the array under
     * @return the newly created array
     */
    public JsonEntity createArray(String property) {
        return create(property, new JsonArray());
    }

    /**
     * Creates an array within an array and adds it as the last element. This method will never overwrite
     * already created arrays, since it always appends to the end of the array.
     * @return the newly created array
     */
    public JsonEntity createArray() {
        return create(arraySize(), new JsonArray());
    }

    /**
     * Creates an array within an array and adds it at the designated position. If something already exists at that
     * position, it will be overwritten with the new array.
     * @param index the position within the array where the array must be created
     * @return the newly created array
     */
    public JsonEntity createArray(int index) {
        return create(index, new JsonArray());
    }

    /**
     * Returns the array in an object with the designated name. If nothing exists under that name, a new array
     * will be created. The ensure-operation never overwrites values, and is therefore a useful method for usage
     * within loops. If an already existing element is not an array, an exception will be thrown.
     * @param property name of the property to get the array from (if it exists) or to create the array under (if
     *                 it does not exist)
     * @return the already existing or newly created array
     */
    public JsonEntity ensureArray(String property) {
        JsonEntity array = get(property);
        if (array == null) {
            array = create(property, new JsonArray());
        }
        return verifyArray(array);
    }

    /**
     * Returns the array in an array with a designated position. If nothing exists in that position, a new array
     * will be created. The ensure-operation never overwrites values, and is therefore a useful method for usage
     * within loops. If an already existing element is not an array, an exception will be thrown.
     * @param index position of the property to get the array from (if it exists) or to create the array under
     *              (if it does not exist)
     * @return the already existing or newly created array
     */
    public JsonEntity ensureArray(int index) {
        return verifyArray(ensure(index, new JsonArray()));
    }

    private JsonEntity verifyArray(JsonEntity array) {
        if (!array.isArray()) {
            throw new JsonEntityException(array, null, "is not an array");
        }
        return array;
    }

    /**
     * Creates an object within an object. The object will be stored under the property name. If something already
     * exists under that property, it will be overwritten with this new one.
     * @param property name of the property to create the object under
     * @return the newly created object
     */
    public JsonEntity createObject(String property) {
        return create(property, new JsonObject());
    }

    /**
     * Creates an object within an array and adds it as the last element. This method will never overwrite
     * already created objects, since it always appends to the end of the array.
     * @return the newly created object
     */
    public JsonEntity createObject() {
        return createObject(arraySize());
    }

    /**
     * Creates an object within an array and adds it at the designated position. If something already exists at that
     * position, it will be overwritten with the new object.
     * @param index the position within the array where the object must be created
     * @return the newly created object
     */
    public JsonEntity createObject(int index) {
        return create(index, new JsonObject());
    }

    /**
     * Returns the object in an object with the designated name. If nothing exists under that name, a new object
     * will be created. The ensure-operation never overwrites values, and is therefore a useful method for usage
     * within loops. If an already existing element is not an object, an exception will be thrown.
     * @param property name of the property to get the object from (if it exists) or to create the object under
     *                (if it does not exist)
     * @return the already existing or newly created object
     */
    public JsonEntity ensureObject(String property) {
        JsonEntity object = get(property);
        if (object == null) {
            object = create(property, new JsonObject());
        }
        return verifyObject(object);
    }

    /**
     * Returns the object in an array with a designated position. If nothing exists in that position, a new object
     * will be created. The ensure-operation never overwrites values, and is therefore a useful method for usage
     * within loops. If an already existing element is not an object, an exception will be thrown.
     * @param index position of the property to get the object from (if it exists) or to create the object under
     *              (if it does not exist)
     * @return the already existing or newly created object
     */
    public JsonEntity ensureObject(int index) {
        return verifyObject(ensure(index, new JsonObject()));
    }

    private JsonEntity verifyObject(JsonEntity object) {
        if (!object.isObject()) {
            throw new JsonEntityException(object, null, "is not an object");
        }
        return object;
    }

    /**
     * Creates a JsonEntity under property name in the object. The JSON tree in jsonEntity will be stored here
     * @param property name of the property
     * @param jsonEntity JsonEntity to create in the object
     * @return the newly created JsonEntity (if array/object) or the object (if primitive/null)
     */
    public JsonEntity create(String property, JsonEntity jsonEntity) {
        return create(property, jsonEntity.raw());
    }

    /**
     * Appends a JsonEntity to the end of the array. The JSON tree in jsonEntity will be stored here.
     * @param jsonEntity JsonEntity to create in the array
     * @return the newly created JsonEntity (if array/object) or the object (if primitive/null)
     */
    public JsonEntity create(JsonEntity jsonEntity) {
        return create(arraySize(), jsonEntity.raw());
    }

    /**
     * Inserts a JsonEntity at the designated position in the array. The JSON tree in jsonEntity will be
     * stored here.
     * @param index position in the array to store the JsonEntity
     * @param jsonEntity JsonEntity to create in the array
     * @return the newly created JsonEntity (if array/object) or the object (if primitive/null)
     */
    public JsonEntity create(int index, JsonEntity jsonEntity) {
        return create(index, jsonEntity.raw());
    }

    /**
     * Creates a JsonElement under property name in the object. The JSON tree in the JsonElement will
     * be stored here
     * @param property name of the property
     * @param jsonEntity JsonElement to create in the object
     * @return the newly created JsonEntity (if array/object) or the object (if primitive/null)
     */
    public JsonEntity create(String property, JsonElement jsonEntity) {
        if (!isObject()) {
            throw new JsonEntityException(this, null, "is not an object, therefore no property can be set on it");
        }
        JsonEntity result = linkToObject(property, jsonEntity);
        return result.fluentPlayer() ? result : this;
    }

    /**
     * Appends a JsonEntity to the end of the array. The JSON tree in the JsonElement will be stored here.
     * @param jsonEntity JsonElement to create in the array
     * @return the newly created JsonEntity (if array/object) or the object (if primitive/null)
     */
    public JsonEntity create(JsonElement jsonEntity) {
        return create(arraySize(), jsonEntity);
    }

    private JsonEntity ensure(int index, JsonElement jsonEntity) {
        return createOrEnsure(index, jsonEntity, false);
    }

    /**
     * Inserts a JsonElement at the designated position in the array. The JSON tree in the JsonElement
     * will be stored here.
     * @param index position in the array to store the JsonEntity
     * @param jsonEntity JsonEntity to create in the array
     * @return the newly created JsonEntity (if array/object) or the object (if primitive/null)
     */
    public JsonEntity create(int index, JsonElement jsonEntity) {
        return createOrEnsure(index, jsonEntity, true);
    }

    private JsonEntity createOrEnsure(int index, JsonElement jsonEntity, boolean overwrite) {
        JsonEntity arrayElement = getOrCreateAtIndex(index, jsonEntity, overwrite);
        return arrayElement.fluentPlayer() ? arrayElement : this;
    }

    /**
     * Creates a String value under property name in the object.
     * @param property name of the property
     * @param value value to store
     * @return the object in which the value was stored
     */
    public JsonEntity create(String property, String value) {
        return create(property, createPrimitiveString(value));
    }

    /**
     * Appends a String value to the array.
     * @param value value to store
     * @return the array in which the value was stored
     */
    public JsonEntity create(String value) {
        return create(arraySize(), value);
    }

    /**
     * Inserts a String value at the position in the array.
     * @param value value to store
     * @return the array in which the value was stored
     */
    public JsonEntity create(int index, String value) {
        return create(index, createPrimitiveString(value));
    }

    /**
     * Creates a Number value under property name in the object.
     * @param property name of the property
     * @param value value to store
     * @return the object in which the value was stored
     */
    public JsonEntity create(String property, Number value) {
        return create(property, createPrimitiveNumber(value));
    }

    /**
     * Appends a Number value to the array.
     * @param value value to store
     * @return the array in which the value was stored
     */
    public JsonEntity create(Number value) {
        return create(arraySize(), value);
    }

    /**
     * Inserts a Number value at the position in the array.
     * @param value value to store
     * @return the array in which the value was stored
     */
    public JsonEntity create(int index, Number value) {
        return create(index, createPrimitiveNumber(value));
    }

    /**
     * Creates a Boolean value under property name in the object.
     * @param property name of the property
     * @param value value to store
     * @return the object in which the value was stored
     */
    public JsonEntity create(String property, Boolean value) {
        return create(property, createPrimitiveBoolean(value));
    }

    /**
     * Appends a Boolean value to the array.
     * @param value value to store
     * @return the array in which the value was stored
     */
    public JsonEntity create(Boolean value) {
        return create(arraySize(), value);
    }

    /**
     * Inserts a Boolean value at the position in the array.
     * @param value value to store
     * @return the array in which the value was stored
     */
    public JsonEntity create(int index, Boolean value) {
        return create(index, createPrimitiveBoolean(value));
    }

    /**
     * Creates a Character value under property name in the object.
     * @param property name of the property
     * @param value value to store
     * @return the object in which the value was stored
     */
    public JsonEntity create(String property, Character value) {
        return create(property, createPrimitiveCharacter(value));
    }

    /**
     * Appends a Character value to the array.
     * @param value value to store
     * @return the array in which the value was stored
     */
    public JsonEntity create(Character value) {
        return create(arraySize(), value);
    }

    /**
     * Inserts a Character value at the position in the array.
     * @param value value to store
     * @return the array in which the value was stored
     */
    public JsonEntity create(int index, Character value) {
        return create(index, createPrimitiveCharacter(value));
    }

    private JsonElement createPrimitiveBoolean(Boolean value) {
        return value == null ? JsonNull.INSTANCE : new JsonPrimitive(value);
    }

    private JsonElement createPrimitiveNumber(Number value) {
        return value == null ? JsonNull.INSTANCE : new JsonPrimitive(value);
    }

    private JsonElement createPrimitiveString(String value) {
        return value == null ? JsonNull.INSTANCE : new JsonPrimitive(value);
    }

    private JsonElement createPrimitiveCharacter(Character value) {
        return value == null ? JsonNull.INSTANCE : new JsonPrimitive(value);
    }

    /**
     * Sets the element with name property to a null value.
     * @param property name of the element to nullify within the object
     * @return the object element in which the element has been nullified
     */
    public JsonEntity nullify(String property) {
        return create(property, JsonNull.INSTANCE);
    }

    /**
     * Sets the element at index in the array to a null value.
     * @param index position of the element within the array to nullify
     * @return the array element in which the element has been nullified
     */
    public JsonEntity nullify(int index) {
        return create(index, JsonNull.INSTANCE);
    }

    /**
     * Removes the element with name property from the object.
     * @param property name of the element to remove from the object
     * @return the object element from which the element has been removed
     */
    public JsonEntity remove(String property) {
        if (!isObject()) {
            throw new JsonEntityException(this, null, "is not an object, therefore no property can be removed from it");
        }
        ((JsonObject)json).remove(property);
        return this;
    }

    /**
     * Removes the element at index from the array. The array will be collapse, ie elements following the removed
     * element will get an index at 1 lower. Note that array collapsing is not supported by Gson and therefore an
     * expensive array rebuild will be executed.
     * @param index position of the element within the array to remove
     * @return the array element from which the element has been removed
     */
    public JsonEntity remove(int index) {
        create(index, (JsonElement)null);
        return this;
    }

    private boolean fluentPlayer() {
        return (!(isPrimitive() || isNull()));
    }

    /**
     * Returns the size of the array
     * @return size of the array
     */
    public int arraySize() {
        if (!isArray()) {
            throw new JsonEntityException(this, null, "is not an array, therefore does not have an array size");
        }
        return ((JsonArray)json).size();
    }

    private JsonEntity getOrCreateAtIndex(int index, JsonElement jsonElement, boolean overwrite) {
        if (!isArray()) {
            throw new JsonEntityException(this, null, "is not an array, therefore no index can be called upon it");
        }
        if (index > arraySize()) {
            throw new JsonEntityException(this, null, "index > array size");
        } else if (index < arraySize()) {
            if (overwrite) {
                // This is an expensive operation, not supported by Gson. It constructs the array from scratch in order
                // to overwrite a single value.
                return rebuildArray(index, jsonElement);
            } else {
                return getAtIndex(index);
            }
        } else {
            return linkToArray(index, jsonElement);
        }
    }

    private JsonEntity rebuildArray(int replaceIndex, JsonElement replaceElement) {
        JsonArray sourceArray = (JsonArray)json;
        JsonArray targetArray = new JsonArray();
        int currentIndex = 0;
        for (JsonElement sourceArrayElement : sourceArray) {
            if (replaceIndex == currentIndex) {
                if (replaceElement != null) {
                    targetArray.add(replaceElement);
                }
            } else {
                targetArray.add(sourceArrayElement);
            }
            currentIndex++;
        }
        json = targetArray;
        // Gson does not support the modification of arrays (ie, deletes, changes, only adds) and therefore the
        // entire JsonArray will have to be replaced. Consequence is that the parent nodes will have to be
        // notified of the new instance.
        if (parent != null) {
            if (propertyIndex > -1) { // Update the parent array, possibly recursively
                parent.rebuildArray(propertyIndex, json);
            } else { // Update the parent object
                parent.create(propertyName, json);
            }
        }
        return wrap(replaceIndex, replaceElement);
    }

    private JsonEntity getAtIndex(int index) {
        if (index >= arraySize()) {
            throw new JsonEntityException(this, null, "index out of bounds: index "+index+" >= "+arraySize()+" length");
        }
        JsonElement arrayElement = ((JsonArray)json).get(index);
        if (arrayElement == null) {
            throw new JsonEntityException(this, null, "array element does not exist");
        }
        return wrap(index, arrayElement);
    }

    private void primitiveCheck() {
        if (!isPrimitive()) {
            throw new JsonEntityException(this, null, "is not a primitive");
        }
    }

    /**
     * Returns the current primitive element as a Character
     * @return Character value of the current primitive
     */
    public char asCharacter() {
        primitiveCheck();
        return json.getAsCharacter();
    }

    /**
     * Convenience method for returning the primitive at the position in the array as a Character
     * @return Character value of the primitive at the position in the array
     */
    public char asCharacter(int index) {
        return get(index).asCharacter();
    }

    /**
     * Convenience method for returning the primitive with property name from the object as a Character
     * @return Character value of the primitive at the position in the array
     */
    public char asCharacter(String property) {
        return get(property).asCharacter();
    }

    /**
     * Returns the current primitive element as a boolean
     * @return boolean value of the current primitive
     */
    public boolean asBoolean() {
        primitiveCheck();
        return json.getAsBoolean();
    }

    /**
     * Convenience method for returning the primitive at the position in the array as a boolean
     * @return boolean value of the primitive at the position in the array
     */
    public boolean asBoolean(int index) {
        return get(index).asBoolean();
    }

    /**
     * Convenience method for returning the primitive with property name from the object as a boolean
     * @return boolean value of the primitive at the position in the array
     */
    public boolean asBoolean(String property) {
        return get(property).asBoolean();
    }

    /**
     * Returns the current primitive element as a String
     * @return String value of the current primitive
     */
    public String asString() {
        primitiveCheck();
        return json.getAsString();
    }

    /**
     * Convenience method for returning the primitive at the position in the array as a String
     * @return String value of the primitive at the position in the array
     */
    public String asString(int property) {
        return get(property).asString();
    }

    /**
     * Convenience method for returning the primitive with property name from the object as a String
     * @return String value of the primitive at the position in the array
     */
    public String asString(String property) {
        return get(property).asString();
    }

    /**
     * Returns the current primitive element as a double
     * @return double value of the current primitive
     */
    public double asDouble() {
        primitiveCheck();
        return json.getAsDouble();
    }

    /**
     * Convenience method for returning the primitive at the position in the array as a double
     * @return double value of the primitive at the position in the array
     */
    public double asDouble(int property) {
        return get(property).asDouble();
    }

    /**
     * Convenience method for returning the primitive with property name from the object as a double
     * @return double value of the primitive at the position in the array
     */
    public double asDouble(String property) {
        return get(property).asDouble();
    }

    /**
     * Returns the current primitive element as a float
     * @return float value of the current primitive
     */
    public float asFloat() {
        primitiveCheck();
        return json.getAsFloat();
    }

    /**
     * Convenience method for returning the primitive at the position in the array as a float
     * @return float value of the primitive at the position in the array
     */
    public float asFloat(int property) {
        return get(property).asFloat();
    }

    /**
     * Convenience method for returning the primitive with property name from the object as a float
     * @return float value of the primitive at the position in the array
     */
    public float asFloat(String property) {
        return get(property).asFloat();
    }

    /**
     * Returns the current primitive element as a short
     * @return short value of the current primitive
     */
    public short asShort() {
        primitiveCheck();
        return json.getAsShort();
    }

    /**
     * Convenience method for returning the primitive at the position in the array as a short
     * @return short value of the primitive at the position in the array
     */
    public short asShort(int property) {
        return get(property).asShort();
    }

    /**
     * Convenience method for returning the primitive with property name from the object as a short
     * @return short value of the primitive at the position in the array
     */
    public short asShort(String property) {
        return get(property).asShort();
    }

    /**
     * Returns the current primitive element as an int
     * @return int value of the current primitive
     */
    public int asInt() {
        primitiveCheck();
        return json.getAsInt();
    }

    /**
     * Convenience method for returning the primitive at the position in the array as an int
     * @return int value of the primitive at the position in the array
     */
    public int asInt(int property) {
        return get(property).asInt();
    }

    /**
     * Convenience method for returning the primitive with property name from the object as an int
     * @return int value of the primitive at the position in the array
     */
    public int asInt(String property) {
        return get(property).asInt();
    }

    /**
     * Returns the current primitive element as a byte
     * @return byte value of the current primitive
     */
    public byte asByte() {
        primitiveCheck();
        return json.getAsByte();
    }

    /**
     * Convenience method for returning the primitive at the position in the array as a byte
     * @return byte value of the primitive at the position in the array
     */
    public byte asByte(int property) {
        return get(property).asByte();
    }

    /**
     * Convenience method for returning the primitive with property name from the object as a byte
     * @return byte value of the primitive at the position in the array
     */
    public byte asByte(String property) {
        return get(property).asByte();
    }

    /**
     * Returns the current primitive element as a BigDecimal
     * @return BigDecimal value of the current primitive
     */
    public BigDecimal asBigDecimal() {
        primitiveCheck();
        return json.getAsBigDecimal();
    }

    /**
     * Convenience method for returning the primitive at the position in the array as a BigDecimal
     * @return BigDecimal value of the primitive at the position in the array
     */
    public BigDecimal asBigDecimal(int property) {
        return get(property).asBigDecimal();
    }

    /**
     * Convenience method for returning the primitive with property name from the object as a BigDecimal
     * @return BigDecimal value of the primitive at the position in the array
     */
    public BigDecimal asBigDecimal(String property) {
        return get(property).asBigDecimal();
    }

    /**
     * Returns the current primitive element as a BigInteger
     * @return BigInteger value of the current primitive
     */
    public BigInteger asBigInteger() {
        primitiveCheck();
        return json.getAsBigInteger();
    }

    /**
     * Convenience method for returning the primitive at the position in the array as a BigDecimal
     * @return BigDecimal value of the primitive at the position in the array
     */
    public BigInteger asBigInteger(int property) {
        return get(property).asBigInteger();
    }

    /**
     * Convenience method for returning the primitive with property name from the object as a BigInteger
     * @return BigInteger value of the primitive at the position in the array
     */
    public BigInteger asBigInteger(String property) {
        return get(property).asBigInteger();
    }

    /**
     * Returns an element on the basis of an index. The current element must be an array for this to work
     * @param index index of the element within an array
     * @return element with index
     */
    public JsonEntity get(int index) {
        if (!isArray()) {
            throw new JsonEntityException(this, null, "is not an array, therefore no index can be called upon it");
        }
        return getAtIndex(index);
    }

    /**
     * Returns an element on the basis of a property name. The current element must be an object for this to work.
     * @param property property name of the element to return
     * @return element with property name
     */
    public JsonEntity get(String property) {
        if (!isObject()) {
            throw new JsonEntityException(this, null, "is not an object, so the property could not be fetched");
        }
        JsonElement element = ((JsonObject) json).get(property);
        return element != null ? wrap(property, element) : null;
    }
    
    /**
     * Returns an element on the basis of a property name. Whenever the property cannot be found, we return
     * an empty object. The current element must be an object for this to work.
     * @param property property name of the element to return
     * @return element with property name
     */
    public JsonEntity getSafely(String property) {
        JsonEntity entity = get(property);
        return entity != null ? entity : new JsonEntity(JsonNull.INSTANCE);
    }

    /**
     * If the current element is an array, this method will return true
     * @return true if the current element is an array
     */
    public boolean isArray() {
        return json.isJsonArray();
    }

    /**
     * If the current element is a primitive, this method will return true
     * @return true if the current element is a primitive
     */
    public boolean isPrimitive() {
        return json.isJsonPrimitive();
    }

    /**
     * If the current element is an object, this method will return true
     * @return true if the current element is an object
     */
    public boolean isObject() {
        return json.isJsonObject();
    }

    /**
     * If the current element is empty (ie, 'null'), this method will return true
     * @return true if the current element is empty
     */
    public boolean isNull() {
        return json.isJsonNull();
    }

    /**
     * Checks whether the array has rows
     * @return true if the underlying Gson element is a JsonArray and has rows
     */
    public boolean isNotEmpty() {
        return isArray() && arraySize() > 0;
    }

//    /**
//     * ...
//     * @param index
//     * @return
//     */
//    public boolean isNotBlank(JsonEntity value) {
//        boolean result = false;
//        if (isNotNull()) {
//            result = StringUtils.isNotBlank(value.asString());
//        }
//        return result;
//    }

    private String property(int index) {
        return "["+index+"]";
    }

    /**
     * Returns the parent of the current element. This operation can be used in a fluent manner to navigate
     * the JSON tree without having to break the method chain.
     * @return the parent of the current element
     */
    public JsonEntity parent() {
        return parent;
    }

    /**
     * Returns the wrapped JsonElement of Gson.
     * @return wrapped Gson JsonElement
     */
    public JsonElement raw() {
        return json;
    }

    /**
     * Returns the property name the current element has within the parent. This can either be a name
     * or an index. If it is an index, the property is enclosed in square brackets. The name is used
     * together with the parent for exception reporting.
     * @return property of the element within the parent
     */
    public String name() {
        return propertyName == null ? property(propertyIndex) : propertyName;
    }

    private JsonEntity linkToObject(String property, JsonElement jsonEntity) {
        ((JsonObject)json).add(property, jsonEntity);
        return wrap(property, jsonEntity);
    }

    private JsonEntity linkToArray(int index, JsonElement jsonEntity) {
        ((JsonArray)json).add(jsonEntity);
        return wrap(index, jsonEntity);
    }

    private JsonEntity wrap(String property, JsonElement jsonElement) {
        return wrap(property, -1, jsonElement);
    }

    private JsonEntity wrap(int index, JsonElement jsonElement) {
        return wrap(null, index, jsonElement);
    }

    private JsonEntity wrap(String propertyName, int propertyIndex, JsonElement jsonElement) {
        return new JsonEntity(this, propertyName, propertyIndex, jsonElement);
    }

    /**
     * Provides a starting point, in this case an empty object
     * @return an empty object
     */
    public static JsonEntity emptyObject() {
        return new JsonEntity(new JsonObject());
    }

    /**
     * Provides a starting point, in this case an empty array
     * @return an empty array
     */
    public static JsonEntity emptyArray() {
        return new JsonEntity(new JsonArray());
    }

    /**
     * Returns the children in the current array as an iterator. Note that
     * all children will be wrapped in a JsonEntity instance.
     * @return iterator with the children of the array
     */
    @Override
    public Iterator<JsonEntity> iterator() {
    	if (json == JsonNull.INSTANCE) {
    		return Collections.<JsonEntity> emptySet().iterator();
    	}
        if (!isArray()) {
            throw new JsonEntityException(this, null, "is not an array, therefore cannot be iterated over");
        }
        JsonArray array = ((JsonArray)json);
        List<JsonEntity> wrappedElements = new ArrayList<JsonEntity>(array.size());
        int index = 0;
        for (Object anArray : array) {
            wrappedElements.add(wrap(index, (JsonElement) anArray));
            index++;
        }
        return wrappedElements.iterator();
    }

    /**
     * Copies the current node without the reference to the parent and property.
     * @return copy of the current node, un-coupled from its parent
     */
    public JsonEntity detachedCopy() {
        return new JsonEntity(json.toString());
    }

    /**
     * Uses the JsonElement.toString() to show the JSON string representation
     * @return JSON string representation
     */
    @Override
    public String toString() {
        return raw().toString();
    }

    @Override
    public int hashCode() {
        return raw().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JsonEntity)) {
            return false;
        }
        JsonEntity element = (JsonEntity)obj;
        return raw().equals(element.raw());
    }

}
