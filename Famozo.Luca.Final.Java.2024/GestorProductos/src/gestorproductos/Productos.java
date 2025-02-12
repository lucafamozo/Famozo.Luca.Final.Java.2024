/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestorproductos;

/**
 *
 * @author Luca
 */
public abstract class Productos implements Comparable<Productos>{
    private int id;
    private String nombre;
    private double precio;
    private TipoEnvio tipoEnvio;

    // Constructor para crear un producto con nombre y precio
    public Productos(String nombre, double precio) {
        this.id = GestorProductos.generarId(); // Genera un ID único automáticamente
        this.nombre = nombre;
        this.precio = precio;
    }
    
    public boolean esValido() {
        // Lógica de validación general
        return nombre != null && !nombre.isEmpty() && precio > 0 && id > 0;
    }

    // Getters y setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
    this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public TipoEnvio getTipoEnvio() {
        return tipoEnvio;
    }
    
    public void setTipoEnvio(TipoEnvio tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }
    
    // Sobrecargas

    @Override
    public String toString() {
        return "Producto{id=" + id + ", nombre='" + nombre + "', precio=" + precio + "}";
    }
    
    @Override
    public int compareTo(Productos otroProducto) {
        return this.nombre.compareToIgnoreCase(otroProducto.nombre);
    }
    
    /*
     * Métodos abstractos
     */
    
  
    public abstract double aplicarDescuento();
    
    public abstract double calcularImpuestos();
    
    public abstract double calcularCostoEnvio();
}
