/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorproductos;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author Luca
 */
public class IteradorPorTipo implements Iterator<Productos>{
    private List<Productos> productos;
    private Class<?> tipoProducto;
    private int posicion = 0;

    public IteradorPorTipo(List<Productos> productos, Class<?> tipoProducto) {
        this.productos = productos;
        this.tipoProducto = tipoProducto;
    }

    @Override
    public boolean hasNext() {
        while (posicion < productos.size()) {
            if (tipoProducto.isInstance(productos.get(posicion))) {
                return true;
            }
            posicion++;
        }
        return false;
    }

    @Override
    public Productos next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return productos.get(posicion++);
    }
}