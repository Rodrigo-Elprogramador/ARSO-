package arso.usuarios.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TratamientoIllegalArgumentException implements ExceptionMapper<IllegalArgumentException> {
    @Override
    public Response toResponse(IllegalArgumentException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity("{\"error\": \"" + e.getMessage() + "\"}")
                       .type("application/json")
                       .build();
    }
}