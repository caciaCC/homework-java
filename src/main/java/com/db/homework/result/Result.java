package com.db.homework.result;

public class Result {
    private int code;
    private String message;
    private Object result;

    public Result(int code, String message, Object result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    /**
     * Gets the value of code. *
     *
     * @return the value of code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets the code. *
     * <p>You can use getCode() to get the value of code</p>
     * * @param code code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets the value of message. *
     *
     * @return the value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message. *
     * <p>You can use getMessage() to get the value of message</p>
     * * @param message message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the value of result. *
     *
     * @return the value of result
     */
    public Object getResult() {
        return result;
    }

    /**
     * Sets the result. *
     * <p>You can use getResult() to get the value of result</p>
     * * @param result result
     */
    public void setResult(Object result) {
        this.result = result;
    }
}
