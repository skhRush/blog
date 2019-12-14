package org.skh.blog.exceptiom;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * 带有@ResponseStatus注解的异常类会被ResponseStatusExceptionResolver 解析。
 * 可以实现自定义的一些异常,同时在页面上进行显示。具体的使用方法如下:
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND) //返回一个状态码 404
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
