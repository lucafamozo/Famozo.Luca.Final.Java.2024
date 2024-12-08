/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestorproductos;

/**
 *
 * @author Luca
 */
public class Alimentos extends Productos {
    private String fechaCaducidad;
    private String categoria;
    private TipoEnvio tipoEnvio;

    // Constructor
    public Alimentos(String nombre, double precio, String fechaCaducidad, String categoria) {
        super(nombre, precio); // Llamamos al constructor de la clase base (Productos)
        this.fechaCaducidad = fechaCaducidad;
        this.categoria = categoria;
    }
    
    // Constructor sobrecargado
    public Alimentos(int id, String nombre, double precio) {
        super(nombre, precio);
    }

    // Getters y Setters
    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public TipoEnvio getTipoEnvio() {
        return tipoEnvio;
    }
    
    public void setTipoEnvio(TipoEnvio tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }
    
    // Sobrecarga método toString
    @Override
    public String toString() {
        return super.toString() + " - Fecha de caducidad: " + fechaCaducidad + " - Categoría: " + categoria;
    }
}
