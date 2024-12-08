/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestorproductos;

/**
 *
 * @author Luca
 */
public class Productos implements Comparable<Productos>{
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

    @Override
    public String toString() {
        return "Producto{id=" + id + ", nombre='" + nombre + "', precio=" + precio + "}";
    }
    
    public double aplicarDescuento() {
        double precioFinal = precio;

        // Aplicar descuento según la clase específica del producto (por ejemplo, en Ropa, Alimentos, etc.)
        if (this instanceof Ropa) {
            precioFinal = ((Ropa) this).aplicarDescuento();
        } else if (this instanceof Alimentos) {
            precioFinal = ((Alimentos) this).aplicarDescuento();
        } else if (this instanceof Electrodomesticos) {
            precioFinal = ((Electrodomesticos) this).aplicarDescuento();
        }

        // Luego aplicar el costo de envío
        if (tipoEnvio == TipoEnvio.ENVIO_DOMICILIO) {
            precioFinal = aplicarCostoEnvio(precioFinal);
        }

        return precioFinal;
    }

    private double aplicarCostoEnvio(double precioFinal) {
        double precioConEnvio = precioFinal;

        // Aplicar incremento por costo de envío según el tipo de producto
        if (this instanceof Alimentos) {
            precioConEnvio += precioFinal * 0.03; // +3% para alimentos
        } else if (this instanceof Ropa) {
            precioConEnvio += precioFinal * 0.05; // +5% para ropa
        } else if (this instanceof Electrodomesticos) {
            precioConEnvio += precioFinal * 0.10; // +10% para electrodomésticos
        }

        return precioConEnvio;
    }

    public void setTipoEnvio(TipoEnvio tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public TipoEnvio getTipoEnvio() {
        return tipoEnvio;
    }
    
    // Método para obtener el precio final, teniendo en cuenta el descuento y el costo de envío
    public double getPrecioFinal(TipoEnvio tipoEnvio) {
        double precioFinal = this.precio;  // Comienza con el precio original
        double descuento = 0;
        double costoEnvio = calcularEnvio(tipoEnvio);  // Calcula el costo de envío

        // Aplica el descuento dependiendo del tipo de producto
        if (this instanceof Ropa) {
            Ropa ropa = (Ropa) this;
            descuento = ropa.aplicarDescuento();  // Calcula el descuento de ropa
            precioFinal -= descuento;  // Aplica el descuento
        } else if (this instanceof Alimentos) {
            Alimentos alimento = (Alimentos) this;
            descuento = alimento.aplicarDescuento();  // Calcula el descuento de alimentos
            precioFinal -= descuento;  // Aplica el descuento
        } else if (this instanceof Electrodomesticos) {
            Electrodomesticos electro = (Electrodomesticos) this;
            descuento = electro.aplicarDescuento();  // Calcula el descuento de electrodomésticos
            precioFinal -= descuento;  // Aplica el descuento
        }

        // Agregar el costo de envío
        precioFinal += costoEnvio;
        return precioFinal;
    }

    // Método para calcular el costo de envío, dependiendo del tipo de producto y del tipo de envío
    private double calcularEnvio(TipoEnvio tipoEnvio) {
        double costoEnvio = 0;

        // Dependiendo del tipo de envío, se calcula el costo
        switch (tipoEnvio) {
            case RETIRO_LOCAL:
                // Si es "Retiro Local", no hay costo de envío
                return 0;

            case ENVIO_DOMICILIO:
                // Si es "Envío Domicilio", calculamos el costo dependiendo del tipo de producto
                if (this instanceof Alimentos) {
                    costoEnvio = this.precio * 0.03;  // 3% para alimentos
                } else if (this instanceof Ropa) {
                    costoEnvio = this.precio * 0.05;  // 5% para ropa
                } else if (this instanceof Electrodomesticos) {
                    costoEnvio = this.precio * 0.10;  // 10% para electrodomésticos
                }
                break;
        }

        return costoEnvio;
    }
    
    @Override
    public int compareTo(Productos otroProducto) {
        return this.nombre.compareToIgnoreCase(otroProducto.nombre);
    }
}
