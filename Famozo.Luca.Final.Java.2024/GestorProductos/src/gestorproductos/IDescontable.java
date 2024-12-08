/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestorproductos;

/**
 * Interfaz que define el comportamiento para aplicar descuentos a los productos.
 * @author Luca
 */
public interface IDescontable {
    /**
     * Método abstracto para aplicar un descuento al precio del producto.
     * 
     * @return El precio con el descuento aplicado.
     */
    double aplicarDescuento();
}
