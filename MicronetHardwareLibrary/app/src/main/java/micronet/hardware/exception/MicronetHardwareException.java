package micronet.hardware.exception;

/**
 * Micronet Hardware Exception class.
 */
public final class MicronetHardwareException extends Exception {

    /**
     * The specific error code.
     */
    private int errorCode;

    public MicronetHardwareException(String message, int error) {
        super(message);
        this.errorCode = error;
    }

    /**
     * Get the specific error code.
     *
     * Error codes for requests:
     * -1 -> connection failure,
     * -2 -> tx msg failure,
     * -3 -> rx msg failure,
     * -4 -> invalid resp msg type,
     * -5 -> general error
     *
     * @return error code
     */
    public int getErrorCode() {
        return errorCode;
    }
}
