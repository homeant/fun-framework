package xin.tianhui.cloud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import xin.tianhui.cloud.exception.BusinessException;

import java.text.MessageFormat;

@Data
@AllArgsConstructor
public class ErrorMate {
    private Integer status;

    private Integer code;

    private String message;

    public BusinessException getException(){
        return new BusinessException(status,code,message);
    }

    public BusinessException getException(Object ... arguments){
        return new BusinessException(status,code, MessageFormat.format(message,arguments));
    }
}
