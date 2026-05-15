// TAREA 7
package arso.usuarios.adaptadores;

import arso.usuarios.servicio.IServicioUsuario;
import servicio.FactoriaServicios;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InicializadorBus implements ServletContextListener {

    private ConsumidorEventosUsuarios consumidor;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            IServicioUsuario servicio = FactoriaServicios.getServicio(IServicioUsuario.class);
            consumidor = new ConsumidorEventosUsuarios(servicio);
            consumidor.iniciar();
            System.out.println("[Bus] Consumidor RabbitMQ de Usuarios arrancado.");
        } catch (Exception e) {
            System.err.println("[Bus] Error arrancando consumidor: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (consumidor != null) consumidor.detener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
