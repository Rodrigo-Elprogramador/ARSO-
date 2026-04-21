package arso.productos.modelo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductoAltaDTO {
    
    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;
    
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;
    
    @Min(value = 0, message = "El precio no puede ser negativo")
    private double precio;
    
    @NotNull(message = "El estado es obligatorio")
    private Estado estado;
    
    @NotBlank(message = "El ID de la categoría es obligatorio")
    private String idCategoria;
    
    private boolean disponible = true;
    
    @NotBlank(message = "El ID del vendedor es obligatorio")
    private String idVendedor;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(String idCategoria) {
		this.idCategoria = idCategoria;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public String getIdVendedor() {
		return idVendedor;
	}

	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}
}