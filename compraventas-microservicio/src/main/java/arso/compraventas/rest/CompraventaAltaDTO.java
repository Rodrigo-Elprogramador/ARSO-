package arso.compraventas.rest;

import javax.validation.constraints.NotBlank;

public class CompraventaAltaDTO {
    @NotBlank(message = "El ID del producto es obligatorio")
    private String idProducto;
    
    @NotBlank(message = "El ID del comprador es obligatorio")
    private String idComprador;

    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }
    public String getIdComprador() { return idComprador; }
    public void setIdComprador(String idComprador) { this.idComprador = idComprador; }
}