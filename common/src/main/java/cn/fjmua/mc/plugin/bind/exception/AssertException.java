package cn.fjmua.mc.plugin.bind.exception;

/**
 * Assert exception
 * */
public class AssertException extends RuntimeException {

    public AssertException() {
    }

    public AssertException(String message) {
        super(message);
    }

    public AssertException(String message, Throwable cause) {
        super(message, cause);
    }

}