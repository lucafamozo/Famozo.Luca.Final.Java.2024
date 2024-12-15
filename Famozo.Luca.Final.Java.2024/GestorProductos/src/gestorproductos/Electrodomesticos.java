/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestorproductos;

/**
 *
 * @author Luca
 */
public class Electrodomesticos extends Productos implements Ensamblable{
    private String marca;
    private String potencia;
    private TipoEnvio tipoEnvio;

    // Constructor
    public Electrodomesticos(String nombre, double precio, String marca, String potencia) {
        super(nombre, precio); // Llamamos al constructor de la clase base (Productos)
        this.marca = marca;
        this.potencia = potencia;
    } 
    
    // Constructores sobrecargados
    public Electrodomesticos(int id, String nombre, double precio) {
        super(nombre, precio);
    }
    
    public Electrodomesticos(String nombre, double precio, String marca) {
        super(nombre, precio); // Llamamos al constructor de la clase base (Productos)
        this.marca = marca;
    }
    
    public Electrodomesticos(int id, String nombre, double precio, TipoEnvio tipoEnvio) {
        super(nombre, precio); // Llamamos al constructor de la clase base (Productos)
        this.tipoEnvio = tipoEnvio;
    }

    // Getters y Setters
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPotencia() {
        return potencia;
    }

    public void setPotencia(String potencia) {
        this.potencia = potencia;
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
        return super.toString() + " - Marca: " + marca + " - Potencia: " + potencia + "W";
    }
    
    @Override
    public double aplicarDescuento() {
        return this.getPrecio() * 0.15; // Descuento del 15%
    }
    
    @Override
    public double calcularImpuestos() {
        return this.getPrecio() * 0.25; // 25% de IVA
    }

    @Override
    public double calcularCostoEnvio() {
        return this.getPrecio() * 0.10; // 10% del precio como costo de envío
    }
    
    // Sobrecargas de los métodos de la interfaz Ensamblable
    // Sobrecarga de los métodos de la interfaz Ensamblable
    public boolean requiereEnsamblaje(){
        return false;
    }
    
    public String instruccionesDeEnsamblaje(){
        return "No requiere ensamblaje.";
    }
}
