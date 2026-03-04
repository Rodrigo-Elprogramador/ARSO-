package arso.usuarios.rest;

import java.io.Serializable;

/**
 * DTO para la transferencia de datos de Usuario.
 * Evita problemas de serialización con tipos complejos como LocalDate.
 */
public class UsuarioDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String apellidos;
    private String email;
    private String clave;
    private String fechaNacimiento; // Recibido como "yyyy-MM-dd"
    private String telefono;

    // Constructor vacío necesario para los serializadores JSON
    public UsuarioDTO() {
    }

    // Constructor por conveniencia
    public UsuarioDTO(String nombre, String apellidos, String email, String clave, String fechaNacimiento, String telefono) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.clave = clave;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}