package arso.productos.repositorio;

import repositorio.RepositorioJPA;
import arso.productos.modelo.Categoria;

public class RepositorioCategoriaJPA extends RepositorioJPA<Categoria> {

    @Override
    public Class<Categoria> getClase() {
        return Categoria.class;
    }
}