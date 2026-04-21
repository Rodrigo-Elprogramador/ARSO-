package arso.usuarios.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import repositorio.RepositorioException;

@Provider
public class TratamientoRepositorioException implements ExceptionMapper<RepositorioException> {
    @Override
    public Response toResponse(RepositorioException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("{\"error\": \"" + e.getMessage() + "\"}")
                       .type("application/json")
                       .build();
    }
}