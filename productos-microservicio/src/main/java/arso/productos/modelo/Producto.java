package arso.productos.modelo; 

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import repositorio.Identificable;

@Entity
//@Table (name = "producto")
public class Producto implements Identificable{
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE) //anotación JPA
	private String id;
	private String titulo;
	private String descripcion;
	private double precio;
	@Enumerated ( EnumType.STRING ) //Para evitar problemas de cambios en el enumerado
	private Estado estado;
	@Column(name = "fecha_publicacion")
	private LocalDateTime fechaPublicacion;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORIA_ID")
	private Categoria categoria;
	private int visualizaciones;
	@Column(name = "envio_disponible")
	private boolean envioDisponible;
	@Embedded
	private LugarRecogida recogida;
	/**
     * Relación Muchos-a-Uno con Usuario (vendedor).
     * Muchos productos pueden pertenecer a un mismo vendedor.
     */
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "vendedor_id")
	private UsuarioResumen vendedor;
	
	public Producto() {
		
	}
	
	public Producto(String titulo, String descripcion, double precio, Estado estado, 
            LocalDateTime fechaPublicacion, Categoria categoria, int visualizaciones, 
            boolean envioDisponible, LugarRecogida recogida, UsuarioResumen vendedor)  {
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.precio = precio;
		this.estado = estado;
		this.fechaPublicacion = fechaPublicacion;
		this.categoria = categoria;
		this.visualizaciones = visualizaciones;
		this.envioDisponible = envioDisponible;
		this.recogida = recogida;
		this.vendedor = vendedor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public LocalDateTime getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public int getVisualizaciones() {
		return visualizaciones;
	}

	public void setVisualizaciones(int visualizaciones) {
		this.visualizaciones = visualizaciones;
	}

	public boolean isEnvioDisponible() {
		return envioDisponible;
	}

	public void setEnvioDisponible(boolean envioDisponible) {
		this.envioDisponible = envioDisponible;
	}

	public LugarRecogida getRecogida() {
		return recogida;
	}

	public void setRecogida(LugarRecogida recogida) {
		this.recogida = recogida;
	}

	public UsuarioResumen getVendedor() {
	    return vendedor;
	}

	public void setVendedor(UsuarioResumen vendedor) {
	    this.vendedor = vendedor;
	}
	
	public void setIncremento() {
		this.visualizaciones++;
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + ", precio=" + precio
				+ ", estado=" + estado + ", fechaPublicacion=" + fechaPublicacion + ", categoria=" + categoria
				+ ", visualizaciones=" + visualizaciones + ", envioDisponible=" + envioDisponible + ", recogida="
				+ recogida + ", vendedor=" + vendedor + "]";
	}
    
}
