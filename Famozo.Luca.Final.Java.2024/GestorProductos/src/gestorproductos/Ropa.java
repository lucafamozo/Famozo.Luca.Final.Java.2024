/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestorproductos;

/**
 *
 * @author Luca
 */
public class Ropa extends Productos{
    private String talla;
    private String marca;
    private TipoEnvio tipoEnvio; // Atributo de tipo TipoEnvio

    // Constructor
    public Ropa( String nombre, double precio, String talla, String marca) {
        super(nombre, precio); // Llamamos al constructor de la clase base (Productos)
        this.talla = talla;
        this.marca = marca;
    }
    
    // Constructores sobrecargados
    public Ropa(int id, String nombre, double precio) {
        super(nombre, precio);
    }
    
    public Ropa( String nombre, double precio, String talla) {
        super(nombre, precio); // Llamamos al constructor de la clase base (Productos)
        this.talla = talla;
    }
    
    public Ropa(int id, String nombre, double precio, TipoEnvio tipoEnvio) {
        super(nombre, precio); // Llamamos al constructor de la clase base (Productos)
        this.tipoEnvio = tipoEnvio;
    }

    // Getters y Setters
    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getMarca() {
        return marca;
    }

    public void setMaterial(String material) {
        this.marca = material;
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
        return super.toString() + " - Talla: " + talla + " - Marca: " + marca;
    }
    
    @Override
    public double aplicarDescuento() {
        return this.getPrecio() * 0.10; // Descuento del 10%
    }
    
    @Override
    public double calcularImpuestos() {
        return this.getPrecio() * 0.21; // 21% de IVA
    }
    
    @Override
    public double calcularCostoEnvio() {
        return this.getPrecio() * 0.05; // 5% del precio como costo de envío
    }
}
