package org.easygson;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.easygson.JsonEntity.emptyArray;
import static org.easygson.JsonEntity.emptyObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

import org.junit.Test;

public class JsonEntityTest {

    @Test
    public void parseObject() {
        JsonEntity json = new JsonEntity("{x:\"hello world\"}");
        assertEquals("hello world", json.asString("x"));
    }

    @Test
    public void parseArray() {
        JsonEntity json = new JsonEntity("[\"alpha\",\"beta\",\"gamma\"]");
        assertEquals("alpha", json.asString(0));
        assertEquals("beta", json.asString(1));
        assertEquals("gamma", json.asString(2));
    }

    @Test
    public void createArray() {
        JsonEntity array = emptyArray()
                .create("alpha")
                .create("beta")
                .create("gamma");
        assertEquals("alpha", array.asString(0));
        assertEquals("beta", array.asString(1));
        assertEquals("gamma", array.asString(2));
    }

    @Test
    public void nestedObject() {
        JsonEntity object = emptyObject()
                .ensureObject("coordinates")
                .create("x", 28)
                .create("y", 14)
                .parent();
        assertEquals("28", object.get("coordinates").asString("x"));
        assertEquals("14", object.get("coordinates").asString("y"));
    }
    
    @Test
    public void nullSafeAccess() {
        JsonEntity json = emptyObject();
        assertNotNull(json.getSafely("unknown"));
    }

    @Test
    public void callingEnsureArrayPropertyTwice() {
        JsonEntity json = emptyObject().ensureArray("array").parent();
        assertSame(json.get("array").raw(), json.ensureArray("array").raw());
    }

    @Test
    public void callingCreateArrayPropertyTwice() {
        JsonEntity json = emptyObject()
                .createArray("array").parent();
        assertNotSame(json.get("array").raw(), json.createArray("array").raw());
    }

    @Test
    public void callingEnsureArrayIndexTwice() {
        JsonEntity json = emptyArray()
                .ensureArray(0).parent();
        assertSame(json.get(0).raw(), json.ensureArray(0).raw());
    }

    @Test
    public void callingCreateArrayIndexTwice() {
        JsonEntity json = emptyArray()
                .createArray(0).parent();
        assertNotSame(json.get(0).raw(), json.createArray(0).raw());
    }

    @Test
    public void callingEnsureObjectPropertyTwice() {
        JsonEntity json = emptyObject()
                .ensureObject("object").parent();
        assertSame(json.get("object").raw(), json.ensureObject("object").raw());
    }

    @Test
    public void callingCreateObjectPropertyTwice() {
        JsonEntity json = emptyObject()
                .createObject("object").parent();
        assertNotSame(json.get("object").raw(), json.createObject("object").raw());
    }

    @Test
    public void callingEnsureObjectIndexTwice() {
        JsonEntity json = emptyArray()
                .ensureObject(0).parent();
        assertSame(json.get(0).raw(), json.ensureObject(0).raw());
    }

    @Test
    public void callingCreateObjectIndexTwice() {
        JsonEntity json = emptyArray()
                .createObject(0).parent();
        assertNotSame(json.get(0).raw(), json.createObject(0).raw());
    }

    @Test
    public void iterableArray() {
        JsonEntity array = emptyArray()
                .create("el1")
                .create("el2")
                .create("el3");
        int count = 1;
        for (JsonEntity arrayElement : array) {
            assertEquals("el"+count, arrayElement.asString());
            count++;
        }
    }
    
    @Test
    public void iterableObject() {
        JsonEntity json = emptyObject();
        json.create("test", "success");
        
        Iterator<JsonEntity> iterator = json.iterator();
        assertEquals("success", iterator.next().asString("test"));
        assertFalse(iterator.hasNext());
    }

    @Test(expected = JsonEntityException.class)
    public void arrayElementDoesNotExist() {
        JsonEntity array = emptyArray();
        array.get(0);
    }

    @Test
    public void asCharacterFromArray() {
        JsonEntity array = emptyArray();
        array.create('Q');
        assertEquals('Q', array.asCharacter(0));
    }

    @Test
    public void asCharacterFromObject() {
        JsonEntity array = emptyObject();
        array.create("letter", 'Q');
        assertEquals('Q', array.asCharacter("letter"));
    }

    @Test
    public void asStringFromArray() {
        JsonEntity array = emptyArray();
        array.create("alpha");
        assertEquals("alpha", array.asString(0));
    }

    @Test
    public void asStringFromObject() {
        JsonEntity array = emptyObject();
        array.create("some", "words");
        assertEquals("words", array.asString("some"));
    }

    @Test
    public void asBooleanFromArray() {
        JsonEntity array = emptyArray();
        array.create(true);
        assertTrue(array.asBoolean(0));
    }

    @Test
    public void asBooleanFromObject() {
        JsonEntity array = emptyObject();
        array.create("truth", true);
        assertTrue(array.asBoolean("truth"));
    }

    @Test
    public void asDoubleFromArray() {
        JsonEntity array = emptyArray();
        array.create(11.38);
        assertEquals(11.38, array.asDouble(0));
    }

    @Test
    public void asDoubleFromObject() {
        JsonEntity array = emptyObject();
        array.create("double", 11.38);
        assertEquals(11.38, array.asDouble("double"));
    }

    @Test
    public void asFloatFromArray() {
        JsonEntity array = emptyArray();
        array.create(11.38);
        assertEquals(new BigDecimal("11.38"), new BigDecimal(String.valueOf(array.asFloat(0))));
    }

    @Test
    public void asFloatFromObject() {
        JsonEntity array = emptyObject();
        array.create("float", 11.38);
        assertEquals(new BigDecimal("11.38"), new BigDecimal(String.valueOf(array.asFloat("float"))));
    }

    @Test
    public void asBigDecimalFromArray() {
        JsonEntity array = emptyArray();
        array.create(new BigDecimal(11.38));
        assertEquals(new BigDecimal(11.38), array.asBigDecimal(0));
    }

    @Test
    public void asBigDecimalFromObject() {
        JsonEntity array = emptyObject();
        array.create("BigDecimal", new BigDecimal(11.38));
        assertEquals(new BigDecimal(11.38), array.asBigDecimal("BigDecimal"));
    }

    @Test
    public void asBigIntegerFromArray() {
        JsonEntity array = emptyArray();
        array.create(new BigInteger("11235813213455"));
        assertEquals(new BigInteger("11235813213455"), array.asBigInteger(0));
    }

    @Test
    public void asBigIntegerFromObject() {
        JsonEntity array = emptyObject();
        array.create("BigInteger", new BigInteger("11235813213455"));
        assertEquals(new BigInteger("11235813213455"), array.asBigInteger("BigInteger"));
    }

    @Test
    public void asShortFromArray() {
        JsonEntity array = emptyArray();
        array.create(42);
        assertEquals(42, array.asShort(0));
    }

    @Test
    public void asShortFromObject() {
        JsonEntity array = emptyObject();
        array.create("short", 42);
        assertEquals(42, array.asShort("short"));
    }

    @Test
    public void asIntFromArray() {
        JsonEntity array = emptyArray();
        array.create(42);
        assertEquals(42, array.asInt(0));
    }

    @Test
    public void asIntFromObject() {
        JsonEntity array = emptyObject();
        array.create("int", 42);
        assertEquals(42, array.asInt("int"));
    }

    @Test
    public void asByteFromArray() {
        JsonEntity array = emptyArray();
        array.create(42);
        assertEquals(42, array.asByte(0));
    }

    @Test
    public void asByteFromObject() {
        JsonEntity array = emptyObject();
        array.create("byte", 42);
        assertEquals(42, array.asByte("byte"));
    }

    @Test
    public void overwritePrimitive() {
        JsonEntity object = emptyObject();
        object.create("prop", "alpha");
        object.create("prop", "beta");
        assertEquals("beta", object.asString("prop"));
    }

    @Test
    public void overwritingIndexElement() {
        JsonEntity array = emptyArray();
        array.create(0, "alpha").create(1, "gamma");
        array.create(0, "beta");
        assertEquals("beta", array.asString(0));
        assertEquals("gamma", array.asString(1));
    }

    @Test
    public void nullifyInArray() {
        JsonEntity array = emptyArray();
        array.create(0, "alpha").create(1, "gamma");
        array.nullify(0);
        assertTrue(array.get(0).isNull());
        assertEquals("gamma", array.asString(1));
    }

    @Test
    public void nullifyInObject() {
        JsonEntity object = emptyObject();
        object.create("prop", "alpha");
        object.nullify("prop");
        assertTrue(object.get("prop").isNull());
    }

    @Test
    public void removeFromArray() {
        JsonEntity array = emptyArray();
        array = array.create("alpha").create("beta").create("gamma");
        array.remove(1);
        assertEquals("alpha", array.asString(0));
        assertEquals("gamma", array.asString(1));
    }

    @Test
    public void changeObject() {
        JsonEntity root = emptyObject()
            .createArray("array")
                .createObject()
                    .create("x", 13)
                    .parent()
                .parent();
        JsonEntity object = emptyObject()
            .create("x", 29);
        root.get("array").create(0, object);
        assertEquals(29, root.get("array").get(0).asInt("x"));
    }

    @Test
    public void removeFromObject() {
        JsonEntity object = emptyObject();
        object.create("prop", "alpha");
        object.remove("prop");
        assertNull(object.get("prop"));
    }

    @Test
    public void addCustomJsonAsProperty() {
        JsonEntity child = emptyObject()
                .create("x", 14)
                .create("y", 28);
        JsonEntity parent = emptyArray()
                .create(child).parent();
        assertEquals(14, parent.get(0).asInt("x"));
        assertEquals(28, parent.get(0).asInt("y"));
    }

}