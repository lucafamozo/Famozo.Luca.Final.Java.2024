/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorproductos;

import java.util.Comparator;

/**
 *
 * @author Luca
 */
public class ComparatorPorPrecio implements Comparator<Productos> {
    @Override
    public int compare(Productos p1, Productos p2) {
        return Double.compare(p1.getPrecio(), p2.getPrecio());
    }
}