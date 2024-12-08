/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestorproductos;

/**
 *
 * @author Luca
 */
public class Ropa extends Productos implements IDescontable {
    private String talla;
    private String marca;
    private TipoEnvio tipoEnvio; // Atributo de tipo TipoEnvio

    // Constructor
    public Ropa( String nombre, double precio, String talla, String marca) {
        super(nombre, precio); // Llamamos al constructor de la clase base (Productos)
        this.talla = talla;
        this.marca = marca;
    }
    
    // Constructor sobrecargado
    public Ropa(int id, String nombre, double precio) {
        super(nombre, precio);
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
        double precio = getPrecio(); // Obtener el precio original
        double nuevoPrecio = precio; // Inicializamos el nuevo precio como el original

        // Lógica de descuento para Ropa
        if (precio > 100) {
            nuevoPrecio = precio * 0.8; // 20% de descuento
        } else if (precio > 69) {
            nuevoPrecio = precio * 0.85; // 15% de descuento
        } else if (precio > 39) {
            nuevoPrecio = precio * 0.9;  // 10% de descuento
        }

        setPrecio(nuevoPrecio); // Actualiza el precio con el descuento
        return nuevoPrecio;     // Retorna el nuevo precio con el descuento aplicado
    }
    
    public void aplicarCostoEnvio() {
        if (tipoEnvio == TipoEnvio.ENVIO_DOMICILIO) {
            double porcentaje = 0.05; // 5% para ropa
            setPrecio(getPrecio() * (1 + porcentaje)); // Incrementar precio
        }
    }

    

    
}
