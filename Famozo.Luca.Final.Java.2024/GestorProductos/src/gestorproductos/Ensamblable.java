/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestorproductos;

/**
 *
 * @author Luca
 */
public interface Ensamblable {
    boolean requiereEnsamblaje(); // Indica si el producto necesita ensamblaje
    String instruccionesDeEnsamblaje(); // Proporciona las instrucciones de ensamblaje
}