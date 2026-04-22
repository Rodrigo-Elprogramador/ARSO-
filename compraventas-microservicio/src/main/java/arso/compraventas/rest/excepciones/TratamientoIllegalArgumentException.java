package arso.compraventas.rest.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TratamientoIllegalArgumentException {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleException(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Petición incorrecta");
        error.put("mensaje", ex.getMessage());
        return error;
    }
}