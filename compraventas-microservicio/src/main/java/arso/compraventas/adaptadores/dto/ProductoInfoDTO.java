package arso.compraventas.adaptadores.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductoInfoDTO {
    private String titulo;
    private double precio;
    private String idVendedor;
    private String recogida;
    
    //TAREA 7
    private boolean vendido;
    public boolean isVendido() { return vendido; }
    public void setVendido(boolean vendido) { this.vendido = vendido; }
    //
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getIdVendedor() { return idVendedor; }
    public void setIdVendedor(String idVendedor) { this.idVendedor = idVendedor; }
    public String getRecogida() { return recogida; }
    public void setRecogida(String recogida) { this.recogida = recogida; }
}
