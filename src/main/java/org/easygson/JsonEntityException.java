package org.easygson;

import java.util.Stack;

/**
 * Exception that shows where in a JSON tree a problem took place and what it was
 * @author Robert Bor
 */
public class JsonEntityException extends RuntimeException {

    /** JsonEntity on which the exception took place */
    private JsonEntity json;

    /**
     * Constructor for the exception always takes into account a valid JsonEntity object to report on
     * @param json the JsonEntity where the error took place
     * @param exception optional, the cause that led to this exception
     * @param message what went wrong
     */
    public JsonEntityException(JsonEntity json, Exception exception, String message) {
        super(message, exception);
        this.json = json;
    }

    /**
     * the path of the JsonEntity is determined by tracing the parent method and pushing all findings to
     * a stack. The path is reconstructed by popping the stack.
     * @return text representation of the error and where it took place
     */
    public String toString() {
        JsonEntity currentJsonEntity = json;
        Stack<String> stack = new Stack<String>();

        while (currentJsonEntity != null) {
            String entityName = currentJsonEntity.name();
            if (entityName != null) {
                stack.push(entityName);
            }
            currentJsonEntity = currentJsonEntity.parent();
            if (currentJsonEntity != null && currentJsonEntity.isObject() && currentJsonEntity.name() != null) {
                stack.push(".");
            }
        }

        StringBuilder jsonPath = new StringBuilder();
        while (stack.size() > 0) {
            jsonPath.append(stack.pop());
        }
        return jsonPath.toString() + " "+getMessage();
    }

}
