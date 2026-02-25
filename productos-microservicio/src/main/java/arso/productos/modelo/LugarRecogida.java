package arso.productos.modelo;

import javax.persistence.Embeddable;

@Embeddable	//Depende de producto, por lo que no tendrá tabla
public class LugarRecogida {
	private String especificacion;
	private double longitud;
	private double latitud;
	
	public LugarRecogida() {
		
	}
	public LugarRecogida(String descripcion, double longitud, double latitud) {
		this.especificacion=descripcion;
		this.longitud=longitud;
		this.latitud=latitud;
	}

	public String getEspecificacion() {
		return especificacion;
	}

	public void setEspecificacion(String descripcion) {
		this.especificacion = descripcion;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	@Override
	public String toString() {
		return "LugarRecogida [especificacion=" + especificacion + ", longitud=" + longitud + ", latitud=" + latitud + "]";
	}
}
