package xin.tianhui.cloud.exception;

/**
 * 业务异常
 * @author junchen
 */
public class BusinessException extends RuntimeException {

    private Integer status;

    private Integer code;

    public BusinessException() {

    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer status,Integer code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public Integer getStatus() {
        return status;
    }
}
