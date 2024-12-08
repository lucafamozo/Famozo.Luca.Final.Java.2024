/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestorproductos;

/**
 *
 * @author Luca
 */
public class Electrodomesticos extends Productos implements IDescontable{
    private String marca;
    private String potencia;
    private TipoEnvio tipoEnvio;

    // Constructor
    public Electrodomesticos(String nombre, double precio, String marca, String potencia) {
        super(nombre, precio); // Llamamos al constructor de la clase base (Productos)
        this.marca = marca;
        this.potencia = potencia;
    } 
    
    // Constructor sobrecargado
    public Electrodomesticos(int id, String nombre, double precio) {
        super(nombre, precio);
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
    
    // Sobrecarga método toString
    @Override
    public String toString() {
        return super.toString() + " - Marca: " + marca + " - Potencia: " + potencia + "W";
    }
}
