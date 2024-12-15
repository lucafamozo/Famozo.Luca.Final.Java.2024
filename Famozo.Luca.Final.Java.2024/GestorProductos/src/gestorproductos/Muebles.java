/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorproductos;

/**
 *
 * @author Luca
 */
public class Muebles extends Productos implements Ensamblable{
    private String tamaño;
    private String material;
    private TipoEnvio tipoEnvio;
    
    // Constructor
    public Muebles(String tamaño, String material, TipoEnvio tipoEnvio, String nombre, double precio) {
        super(nombre, precio);
        this.tamaño = tamaño;
        this.material = material;
        this.tipoEnvio = tipoEnvio;
    }
    
    // Constructores sobrecargados
    public Muebles(int id, String nombre, double precio) {
        super(nombre, precio);
    }

    public Muebles(String nombre, double precio, String tamaño, String material) {
        super(nombre, precio);
        this.tamaño = tamaño;
        this.material = material;
    }
    
    public Muebles(int id, String nombre, double precio, TipoEnvio tipoEnvio) {
        super(nombre, precio); // Llamamos al constructor de la clase base (Productos)
        this.tipoEnvio = tipoEnvio;
    }
    
    // Getters y Setters
    public String getTamaño() {
        return tamaño;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
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
        return super.toString() + " - Tamaño: " + tamaño + " - Material: " + material;
    }
    
    @Override
    public double aplicarDescuento() {
        return this.getPrecio() * 0.15; // Descuento del 15%
    }
    
    @Override
    public double calcularImpuestos() {
        return this.getPrecio() * 0.20; // 20% de IVA
    }

    @Override
    public double calcularCostoEnvio() {
        return this.getPrecio() * 0.15; // 15% del precio como costo de envío
    }
    
    // Sobrecarga de los métodos de la interfaz Ensamblable
    public boolean requiereEnsamblaje(){
        return true;
    }
    
    public String instruccionesDeEnsamblaje(){
        return "Paso 1: Colocar las piezas base del mueble. " +
           "Paso 2: Ensamblar las partes laterales. " +
           "Paso 3: Fijar la parte superior. " +
           "Paso 4: Asegurarse de que todas las conexiones estén firmes.";
    }
}
