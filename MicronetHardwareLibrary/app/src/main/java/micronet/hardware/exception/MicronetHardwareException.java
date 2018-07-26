package micronet.hardware.exception;

/**
 * Micronet Hardware Exception class.
 */
public class MicronetHardwareException extends Exception {

    /**
     * The specific error code.
     *
     * Error codes for requests:
     *      -1 : connection failure,
     *      -2 : tx msg failure,
     *      -3 : rx msg failure,
     *      -4 : invalid resp msg type
     */
    private int errorCode;

    public MicronetHardwareException(String message, int error){
        super(message);
        this.errorCode = error;
    }

    /**
     * Get the specific error code.
     * @return error code
     */
    public int getErrorCode() {
        return errorCode;
    }
}
