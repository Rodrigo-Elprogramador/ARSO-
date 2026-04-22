package arso.compraventas.rest;

import java.time.LocalDateTime;

public class CompraventaDTO {
    private String id;
    private String idProducto;
    private String titulo;
    private double precio;
    private String nombreVendedor;
    private String nombreComprador;
    private LocalDateTime fecha;

    public CompraventaDTO() {
    	
    }

	public CompraventaDTO(String id, String idProducto, String titulo, double precio, String nombreVendedor,
			String nombreComprador, LocalDateTime fecha) {
		super();
		this.id = id;
		this.idProducto = idProducto;
		this.titulo = titulo;
		this.precio = precio;
		this.nombreVendedor = nombreVendedor;
		this.nombreComprador = nombreComprador;
		this.fecha = fecha;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
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

	public String getNombreVendedor() {
		return nombreVendedor;
	}

	public void setNombreVendedor(String nombreVendedor) {
		this.nombreVendedor = nombreVendedor;
	}

	public String getNombreComprador() {
		return nombreComprador;
	}

	public void setNombreComprador(String nombreComprador) {
		this.nombreComprador = nombreComprador;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
    
    
}