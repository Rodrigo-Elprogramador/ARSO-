package arso.productos.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ProductoDTO implements Serializable{

	private String id;
	private String titulo;
	private double precio;
	private Estado estado;
	private LocalDateTime fechaPublicacion;
	private String categoria;
	private int numVisualizaciones;
	private String idVendedor;
	private String recogida;
	private boolean vendido;
	
	public ProductoDTO(String id, String titulo, double precio, Estado estado, LocalDateTime fechaPublicacion, String categoria,
			int numVisualizaciones) {
		this.id = id;
		this.titulo = titulo;
		this.precio = precio;
		this.estado = estado;
		this.fechaPublicacion = fechaPublicacion;
		this.categoria = categoria;
		this.numVisualizaciones = numVisualizaciones;
	}
	
	public String getId() {
		return id;
	}
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
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
	public LocalDateTime getFechaPublicacion() {
		return fechaPublicacion;
	}
	public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public int getNumVisualizaciones() {
		return numVisualizaciones;
	}
	public void setNumVisualizaciones(int numVisualizaciones) {
		this.numVisualizaciones = numVisualizaciones;
	}
	public String getIdVendedor() {
		return idVendedor;
	}
	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}
	public String getRecogida() {
		return recogida;
	}
	public void setRecogida(String recogida) {
		this.recogida = recogida;
	}
	public boolean isVendido() {
		return vendido;
	}
	public void setVendido(boolean vendido) {
		this.vendido = vendido;
	}
	
	@Override
	public String toString() {
		return "ProductoDTO [titulo=" + titulo + ", precio=" + precio + ", estado=" + estado + ", fechaPublicacion="
				+ fechaPublicacion + ", categoria=" + categoria + ", numVisualizaciones=" + numVisualizaciones + "]";
	}
	
	
}
