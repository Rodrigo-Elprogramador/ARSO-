package arso.productos.rest.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import repositorio.EntidadNoEncontrada;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TratamientoEntidadNoEncontradaException {

    @ExceptionHandler(EntidadNoEncontrada.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleException(EntidadNoEncontrada ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "No encontrado");
        error.put("mensaje", ex.getMessage());
        return error;
    }
}