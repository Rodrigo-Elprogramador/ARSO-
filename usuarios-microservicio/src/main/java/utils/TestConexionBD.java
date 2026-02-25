package utils;


import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestConexionBD {
    public static void main(String[] args) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("segundumdb");
            System.out.println(" Conexión correcta con la base de datos");
            emf.close();
        } catch (Exception e) {
            System.err.println(" Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}