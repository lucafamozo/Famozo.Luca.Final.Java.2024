/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestorproductos;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;
import javafx.scene.control.TableView;


/**
 * GestorProductos se encarga de gestionar la lista de productos, permitiendo agregar, eliminar,
 * @author Luca
 */
public class GestorProductos implements IGestorCrud<Productos>{
    private List<Productos> productos = new ArrayList<>();
    private static int contadorId = 0; // Contador para generar IDs únicos
    private Map<String, List<Productos>> productosPorTipo = new HashMap<>();
    private TableView<Productos> table = new TableView<>();

    // Constructor
    public GestorProductos() {
        this.productos = new ArrayList<>();
    }

    /**
     * Obtiene la lista de productos
     * 
     * @return Lista de productos
     */
    public List<Productos> obtenerProductos() {
        return new ArrayList<>(productos); // Devolvemos una copia para evitar modificaciones externas
    }

    /**
     * Genera un ID único para cada nuevo producto
     * 
     * @return ID único generado
     */
    public static int generarId() {
        return ++contadorId; // Incrementa y retorna un ID único
    }

    /**
     * Agrega un producto a la lista de productos.
     * 
     * @param producto El producto a agregar.
     * @throws ProductoNoValidoException Si el producto no es válido
     */
    @Override
    public void agregar(Productos producto) throws Excepciones.ProductoNoValidoException {
        if (producto == null || producto.getNombre().isEmpty() || producto.getPrecio() <= 0) {
            throw new Excepciones.ProductoNoValidoException("Producto no válido.");
        }
        productos.add(producto);
        String tipo = producto.getClass().getSimpleName();
        productosPorTipo.putIfAbsent(tipo, new ArrayList<>());
        productosPorTipo.get(tipo).add(producto);
    }
     
    /**
     * Itera la lista de acuerdo al tipo de producto indicado
     */ 
    public Iterator<Productos> obtenerIteradorPorTipo(String tipo) {
        List<Productos> productos = productosPorTipo.get(tipo);
        if (productos != null) {
            return productos.iterator();
        } else {
            throw new IllegalArgumentException("Tipo de producto no encontrado.");
        }
    }
    
    /*
     * Busca productos por su ID
     */
    
    @Override
    public Productos obtenerPorId(int id) throws Excepciones.ProductoNoEncontradoException {
        return productos.stream()
                        .filter(p -> p.getId() == id)
                        .findFirst()
                        .orElseThrow(() -> new Excepciones.ProductoNoEncontradoException("Producto no encontrado."));
    }

    // Elimina un producto por su ID
    @Override
    public boolean eliminar(int id) throws Excepciones.ProductoNoEncontradoException {
        Productos productoAEliminar = obtenerPorId(id);
        return productos.remove(productoAEliminar);
    }

    /**
     * Lista todos los productos almacenados
     * 
     * @return Lista de todos los productos
     */
    public List<Productos> listarProductos() {
        return obtenerProductos(); // Reutilizamos el método para obtener copia de la lista
    }

    /**
     * Ordena los productos por precio ascendente
     */
    public void ordenarPorPrecio() {
        productos.sort(Comparator.comparingDouble(Productos::getPrecio));
    }

    /**
     * Filtra los productos según un criterio dado
     * 
     * @param criterio Un predicado que define el criterio de filtrado
     */
    public void filtrarProductos(Predicate<Productos> criterio) {
        productos = productos.stream()
            .filter(criterio)
            .collect(Collectors.toList());
    }

    /**
     * Muestra todos los productos utilizando un Consumer
     * 
     * @param consumer Un consumidor para procesar cada producto
     */
    public void mostrarConConsumer(Consumer<Productos> consumer) {
        productos.forEach(consumer);
    }

    /**
     * Transforma los productos usando una función
     * 
     * @param mapper La función de transformación
     * @return Una lista con los resultados de la transformación
     */
    public List<String> transformarProductos(Function<Productos, String> mapper) {
        return productos.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Override
    public void actualizar(int id, Productos producto) throws Excepciones.ProductoNoEncontradoException {
        Productos productoExistente = obtenerPorId(id);
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setPrecio(producto.getPrecio());
    }

    /**
     * Ordena los productos según un comparador personalizado
     * 
     * @param criterio El comparador utilizado para ordenar los productos
     */
    public void ordenarProductosPor(Comparator<Productos> criterio) {
        productos.sort(criterio);
    }

    /**
     * Guarda la lista de productos en un archivo JSON
     */
    public void guardarProductosEnJson(String rutaArchivo) throws Excepciones.ArchivoNoValidoException {
        try (Writer writer = new FileWriter(rutaArchivo)) {
            List<Map<String, Object>> listaProductos = new ArrayList<>();

            // Construcción de la lista de mapas para serialización
            for (Productos producto : productos) {
                Map<String, Object> datos = new HashMap<>();
                datos.put("id", producto.getId());
                datos.put("nombre", producto.getNombre());
                datos.put("precio", producto.getPrecio());
                datos.put("tipo", producto.getClass().getSimpleName());
                datos.put("tipoEnvio", producto.getTipoEnvio().toString()); // Incluye el tipo de envío
                listaProductos.add(datos);
            }

            // Serialización con Gson
            new Gson().toJson(listaProductos, writer);
            System.out.println("Productos guardados en archivo JSON.");
        } catch (IOException e) {
            throw new Excepciones.ArchivoNoValidoException("Error al guardar productos en archivo JSON: " + e.getMessage());
        }
    }

    /**
     * Exporta todos los productos a un archivo CSV con toda la información, similar al formato JSON
     *
     * @param rutaArchivo Ruta del archivo CSV donde exportar los productos
     */
    public void guardarProductosEnCsv(String rutaArchivo) throws Excepciones.ArchivoNoValidoException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(rutaArchivo))) {
            // Escribir el encabezado del CSV
            writer.writeNext(new String[] { "ID", "Nombre", "Precio", "Tipo", "Tipo de Envío" });

            // Escribir los productos en formato CSV
            for (Productos producto : productos) {
                writer.writeNext(new String[] { 
                    String.valueOf(producto.getId()), 
                    producto.getNombre(), 
                    String.valueOf(producto.getPrecio()), 
                    producto.getClass().getSimpleName(),
                    producto.getTipoEnvio() != null ? producto.getTipoEnvio().name() : "LOCAL"
                });
            }

            System.out.println("Productos guardados en archivo CSV.");
        } catch (IOException e) {
            throw new Excepciones.ArchivoNoValidoException("Error al guardar productos en archivo CSV: " + e.getMessage());
        }
    }
    
    /*
     * Método para cargar desde JSON
     */ 
    public void cargarProductosDesdeJson(String rutaArchivo) throws Excepciones.ProductoNoValidoException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(rutaArchivo)) {
            Type tipoLista = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> listaMapas = gson.fromJson(reader, tipoLista);

            productos.clear(); // Limpiar la lista actual antes de cargar nuevos productos

            for (Map<String, Object> datos : listaMapas) {
                int id = ((Double) datos.get("id")).intValue();
                String nombre = (String) datos.get("nombre");
                double precio = (Double) datos.get("precio");
                String tipo = (String) datos.get("tipo");
                String tipoEnvioStr = (String) datos.get("tipoEnvio");
                TipoEnvio tipoEnvio = TipoEnvio.valueOf(tipoEnvioStr);

                // Crear producto desde el tipo
                Productos producto = crearProductoDesdeTipo(id, nombre, precio, tipo, tipoEnvio);
                productos.add(producto);
            }

            System.out.println("Productos cargados desde JSON exitosamente.");
        } catch (IOException e) {
            throw new Excepciones.ProductoNoValidoException("Error al leer el archivo JSON: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new Excepciones.ProductoNoValidoException("Error al deserializar JSON: " + e.getMessage());
        }
    }
    
    /*
     * Método para cargar desde CSV
     */
    public void cargarProductosDesdeCsv(String rutaArchivo) throws Excepciones.ProductoNoValidoException {
        try (CSVReader reader = new CSVReader(new FileReader(rutaArchivo))) {
            String[] nextLine;

            productos.clear(); // Limpiar la lista actual antes de cargar nuevos productos

            reader.readNext(); // Salta el encabezado (si lo tiene)
            while ((nextLine = reader.readNext()) != null) {
                int id = Integer.parseInt(nextLine[0]);
                String nombre = nextLine[1];
                double precio = Double.parseDouble(nextLine[2]);
                String tipoProducto = nextLine[3];
                String tipoEnvioStr = nextLine[4];
                TipoEnvio tipoEnvio = TipoEnvio.valueOf(tipoEnvioStr);

                Productos producto = crearProductoDesdeTipo(id, nombre, precio, tipoProducto, tipoEnvio);
                productos.add(producto);
            }

            System.out.println("Productos cargados exitosamente desde CSV.");
        } catch (IOException e) {
            throw new Excepciones.ProductoNoValidoException("Error al leer el archivo CSV: " + e.getMessage());
        } catch (Exception e) {
            throw new Excepciones.ProductoNoValidoException("Error al procesar CSV: " + e.getMessage());
        }
    }
    
    /*
     * Crea un objeto de tipo Productos específico basado en el parámetro tipoProducto
     */
    private Productos crearProductoDesdeTipo(int id, String nombre, double precio, String tipoProducto, TipoEnvio tipoEnvio) {
        Productos producto;
        switch (tipoProducto) {
            case "Ropa":
                producto = new Ropa(id, nombre, precio);
                break;
            case "Electrodomesticos":
                producto = new Electrodomesticos(id, nombre, precio);
                break;
            case "Alimentos":
                producto = new Alimentos(id, nombre, precio);
                break;
            default:
                throw new IllegalArgumentException("Tipo de producto no reconocido: " + tipoProducto);
        }
        producto.setTipoEnvio(tipoEnvio);
        return producto;
    }
    
    /*
     * Exporta a un archivo de texto los productos que cumplen con un criterio especificado
     */
    public static void exportarListado(List<Productos> productos, String tipo, Predicate<Productos> criterio) {
        // Filtrar los productos utilizando el criterio
        List<Productos> productosFiltrados = productos.stream()
                                                     .filter(criterio)
                                                     .toList();
        
        // Crear el nombre del archivo
        String fileName = "listado_" + tipo + ".txt";
        File file = new File(fileName);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Escribir el encabezado
            writer.write("Listado de Productos Filtrados por Tipo: " + tipo);
            writer.newLine();
            writer.write("-------------------------------------------------------");
            writer.newLine();
            
            // Escribir los productos en el archivo
            for (Productos producto : productosFiltrados) {
                writer.write(producto.toString());
                writer.newLine();
            }
            
            // Confirmación de exportación
            System.out.println("Exportación completada a " + fileName);
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al exportar el archivo.");
        }
    }

    /*
     * Método para exportar productos según el tipo de envío
     */ 
    public void exportarPorTipoEnvio(List<? extends Productos> productos) {
        // Filtrar productos de tipo RETIRO_LOCAL
        List<? extends Productos> productosRetiroLocal = productos.stream()
                .filter(producto -> producto.getTipoEnvio()== TipoEnvio.RETIRO_LOCAL)
                .toList();
        
        // Filtrar productos de tipo ENVIO_DOMICILIO
        List<? extends Productos> productosEnvioDomicilio = productos.stream()
                .filter(producto -> producto.getTipoEnvio()== TipoEnvio.ENVIO_DOMICILIO)
                .toList();

        // Exportar productos de RETIRO_LOCAL a "retiro_local.txt"
        exportarAArchivo(productosRetiroLocal, "retiro_local.txt", "Productos para Retiro en Local");

        // Exportar productos de ENVIO_DOMICILIO a "envio_domicilio.txt"
        exportarAArchivo(productosEnvioDomicilio, "envio_domicilio.txt", "Productos para Envío a Domicilio");
    }

    /*
     * Método privado para exportar una lista de productos a un archivo
     */ 
    private static void exportarAArchivo(List<? extends Productos> productos, String fileName, String encabezado) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Escribir el encabezado en el archivo
            writer.write(encabezado);
            writer.newLine();
            writer.write("-------------------------------------------------------");
            writer.newLine();

            // Escribir los productos en el archivo
            for (Productos producto : productos) {
                writer.write(producto.toString());  // Asumimos que toString() proporciona la información legible
                writer.newLine();
            }

            // Confirmación de exportación
            System.out.println("Exportación completada a " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al exportar el archivo: " + fileName);
        }
    }  
}
