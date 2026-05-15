// TAREA 7
package arso.usuarios.puertos;

public interface IEventosCompraventas {
    void onCompraventaCreada(String idVendedor, String idComprador) throws Exception;
}
