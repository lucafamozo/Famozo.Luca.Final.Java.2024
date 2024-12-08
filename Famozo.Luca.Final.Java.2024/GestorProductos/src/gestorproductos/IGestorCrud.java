/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestorproductos;

/**
 *
 * @author Luca
 */
public interface IGestorCrud<T> {
    // Crear (Agregar una entidad)
    void agregar(T entidad) throws Excepciones.ProductoNoValidoException;
    
    // Leer (Obtener una entidad por su ID)
    T obtenerPorId(int id) throws Excepciones.ProductoNoEncontradoException;
    
    // Actualizar (Modificar una entidad)
    void actualizar(int id, T entidad) throws Excepciones.ProductoNoEncontradoException;
    
    // Eliminar (Eliminar una entidad por su ID)
    boolean eliminar(int id) throws Excepciones.ProductoNoEncontradoException;
}
