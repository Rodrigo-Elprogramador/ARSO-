//TAREA 7
package arso.compraventas.puertos;

import java.util.Map;

public interface IBusEventos {
    void publicarEvento(String tipoEvento, Map<String, Object> datos) throws Exception;
}
