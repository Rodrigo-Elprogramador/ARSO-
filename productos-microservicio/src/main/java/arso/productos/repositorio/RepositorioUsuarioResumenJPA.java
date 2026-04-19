package arso.productos.repositorio;

import repositorio.RepositorioJPA;
import arso.productos.modelo.UsuarioResumen;

public class RepositorioUsuarioResumenJPA extends RepositorioJPA<UsuarioResumen> {

    @Override
    public Class<UsuarioResumen> getClase() {
        return UsuarioResumen.class;
    }
}