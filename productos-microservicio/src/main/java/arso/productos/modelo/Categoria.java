package arso.productos.modelo;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import arso.productos.repositorio.Identificable;

@Entity	//anotación JPA
@Table(name = "categoria")
@XmlRootElement // anotación JAXB
@XmlAccessorType(XmlAccessType.FIELD)
public class Categoria implements Identificable{
	@Id //anotaciónJPA
	@XmlAttribute
	@Column(nullable = false)
	private String id;
	@XmlElement
	@Column(nullable = false)
	private String nombre;
	@XmlTransient				//Ignoramos elemento en la conversión a XML
	private String descripcion;
	@XmlAttribute
	private String ruta;

	

    @ManyToOne(fetch = FetchType.LAZY) // LAZY para no cargar al padre innecesariamente
    @JoinColumn(name = "categoria_padre_id" ) // Así se llamará la columna FK en la BD
    private Categoria categoriaPadre;

    @OneToMany(mappedBy = "categoriaPadre", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@XmlElement(name="categoria")
    private List<Categoria> subcategorias;
    
    public Categoria() {}
    
	public Categoria(String id,String nombre,String ruta) {
		this.id=id;
		this.nombre=nombre;
		this.ruta=ruta;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public void setCategoriaPadre(Categoria padre) {
		if(padre != null) {
			this.categoriaPadre = padre;
		}
	}
	
	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public List<Categoria> getSubcategorias() {
		return subcategorias;
	}

	public void setSubcategorias(List<Categoria> subcategorias) {
		this.subcategorias = subcategorias;
	}

	@Override
	public String toString() {
		return "Categoria [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", ruta=" + ruta
				+ ", categoriaPadre=" + categoriaPadre + ", subcategorias=" + subcategorias.size() + "]";
	}

	
	
}
