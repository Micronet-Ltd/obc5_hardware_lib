package micronet.hardware.exception;

/**
 * Micronet Hardware Exception class.
 */
public class MicronetHardwareException extends Exception {

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
