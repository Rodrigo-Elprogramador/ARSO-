package arso.usuarios.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import repositorio.EntidadNoEncontrada;

@Provider
public class TratamientoEntidadNoEncontradaException implements ExceptionMapper<EntidadNoEncontrada> {
    @Override
    public Response toResponse(EntidadNoEncontrada e) {
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("{\"error\": \"" + e.getMessage() + "\"}")
                       .type("application/json")
                       .build();
    }
}