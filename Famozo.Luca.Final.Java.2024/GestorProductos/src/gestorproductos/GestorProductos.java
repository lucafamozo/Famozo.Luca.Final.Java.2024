/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestorproductos;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;


/**
 * GestorProductos se encarga de gestionar la lista de productos, permitiendo agregar, eliminar,
 * @author Luca
 */
public class GestorProductos implements IGestorCrud<Productos>{
    private List<Productos> productos = new ArrayList<>();
    private static int contadorId = 0; // Contador para generar IDs únicos
    private Map<String, List<Productos>> productosPorTipo = new HashMap<>();

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
        aplicarDescuento();
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
     * Busca productos por su ID (Método auxiliar)
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

    
    
    // Ordena la lista de productos por un criterio natural por defecto
    public void ordenarPorCriterioNatural() {
        Collections.sort(productos); // Usa Comparable
    }
    
    /**
     * Ordena los productos según un comparador personalizado
     * 
     * @param criterio El comparador utilizado para ordenar los productos
     */
    public void ordenarProductosPor(Comparator<Productos> criterio) {
        productos.sort(criterio);
    }
    
    @Override
    public void actualizar(int id, Productos producto) throws Excepciones.ProductoNoEncontradoException {
        Productos productoExistente = obtenerPorId(id);
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setPrecio(producto.getPrecio());
        aplicarDescuento();
    }
    
    /**
    * Convierte la lista de productos en una lista de representaciones genéricas (mapas o listas).
    * 
    * @return Lista de datos genéricos representando los productos.
    */
   private List<List<Object>> obtenerDatosProductos() {
       List<List<Object>> datosProductos = new ArrayList<>();
       for (Productos producto : productos) {
           List<Object> datos = new ArrayList<>();
           datos.add(producto.getId());
           datos.add(producto.getNombre());
           datos.add(producto.getPrecio());
           datos.add(producto.getClass().getSimpleName());
           datos.add(producto.getTipoEnvio() != null ? producto.getTipoEnvio().name() : "LOCAL");
           datosProductos.add(datos);
       }
       return datosProductos;
   }

    /**
     * Guarda la lista de productos en un archivo JSON
     */
    public void guardarProductosEnJson(String rutaArchivo) throws Excepciones.ArchivoNoValidoException {
        try (Writer writer = new FileWriter(rutaArchivo)) {
            List<List<Object>> datosProductos = obtenerDatosProductos();
            // Convertimos cada lista a un mapa para serialización JSON
            List<Map<String, Object>> listaProductos = datosProductos.stream()
                .map(datos -> Map.of(
                    "id", datos.get(0),
                    "nombre", datos.get(1),
                    "precio", datos.get(2),
                    "tipo", datos.get(3),
                    "tipoEnvio", datos.get(4)
                ))
                .collect(Collectors.toList());
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
            // Escribir encabezado
            writer.writeNext(new String[] { "ID", "Nombre", "Precio", "Tipo", "Tipo de Envío" });
            // Escribir filas
            List<List<Object>> datosProductos = obtenerDatosProductos();
            for (List<Object> datos : datosProductos) {
                writer.writeNext(datos.stream()
                    .map(Object::toString) // Convertir todos los objetos a cadenas
                    .toArray(String[]::new));
            }
            System.out.println("Productos guardados en archivo CSV.");
        } catch (IOException e) {
            throw new Excepciones.ArchivoNoValidoException("Error al guardar productos en archivo CSV: " + e.getMessage());
        }
    }
    
    /*
     * Método para cargar desde JSON
     */ 
    public void cargarProductosDesdeJson(String rutaArchivo) throws Excepciones.ArchivoNoValidoException {
        try (Reader reader = new FileReader(rutaArchivo)) {
            
            System.out.println("Contenido del archivo JSON: ");
            BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
            br.lines().forEach(System.out::println);
            
            // Crear un objeto Gson
            Gson gson = new Gson();

            // Definir el tipo de la lista
            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();

            // Deserializar el archivo JSON a una lista de mapas
            List<Map<String, Object>> listaProductosJson = gson.fromJson(reader, listType);
            
            // Verifica el contenido de la lista de productos cargados
            System.out.println("Datos cargados del JSON: " + listaProductosJson);

            for (Map<String, Object> productoJson : listaProductosJson) {
                int id = ((Double) productoJson.get("id")).intValue();
                String nombre = (String) productoJson.get("nombre");
                double precio = ((Number) productoJson.get("precio")).doubleValue();
                String tipoProducto = (String) productoJson.get("tipo");
                String tipoEnvioStr = (String) productoJson.get("tipoEnvio");

                // Convertir tipoEnvio a enum
                TipoEnvio tipoEnvio = TipoEnvio.valueOf(tipoEnvioStr);

                // Crear el producto usando el método proporcionado
                Productos producto = crearProductoDesdeTipo(id, nombre, precio, tipoProducto, tipoEnvio);

                // Agregar el producto a la lista
                productos.add(producto);

                // Imprimir el contenido de cada producto
                System.out.println("Producto CARGADO: " + producto); // Asume que 'producto' tiene un toString() adecuado
            }

            // Mostrar la cantidad de productos cargados
            System.out.println("Productos CARGADOS: " + productos.size());
            Double precioTotalLista = calcularPrecioTotal(productos);
            System.out.println("Precio sumado entre los productos de la lista: $" + precioTotalLista);
        } catch (Exception e) {
            e.printStackTrace();
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
            
            
            Double precioTotalLista = calcularPrecioTotal(productos);            
            System.out.println("Precio sumado entre los productos de la lista: $" + precioTotalLista);
            
            
        } catch (IOException e) {
            throw new Excepciones.ProductoNoValidoException("Error al leer el archivo CSV: " + e.getMessage());
        } catch (Exception e) {
            throw new Excepciones.ProductoNoValidoException("Error al procesar CSV: " + e.getMessage());
        }
    }
    
    /*
     * Crea un objeto de tipo Productos específico basado en el parámetro tipoProducto
     */
    public Productos crearProductoDesdeTipo(int id, String nombre, double precio, String tipoProducto, TipoEnvio tipoEnvio) {
        // Dependiendo del tipo de producto, creamos una instancia específica
        Productos producto = null;

        switch (tipoProducto) {
            case "Ropa":
                producto = new Ropa(id, nombre, precio, tipoEnvio);
                break;
            case "Electrodomesticos":
                producto = new Electrodomesticos(id, nombre, precio, tipoEnvio);
                break;
            case "Alimentos":
                producto = new Alimentos(id, nombre, precio, tipoEnvio);
                break;
            case "Muebles":
                producto = new Muebles(id, nombre, precio, tipoEnvio);
                break;
            // Puedes agregar más casos según los tipos de productos que tengas
            default:
                // Manejo de un tipo de producto desconocido, si es necesario
                System.out.println("Tipo de producto no reconocido: " + tipoProducto);
                break;
        }

        // Imprimir el producto creado para depuración
        if (producto != null) {
            System.out.println("Producto CREADO: " + producto); // Aquí se imprime el producto creado
        }

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
    
    /*
     * Método con Wildcard con límites superiores
     */    
    public double calcularPrecioTotal(List<? extends Productos> productos) {
        return productos.stream()
                        .mapToDouble(Productos::getPrecio)
                        .sum();
    }
    
    /*
     * Método con Wildcard con límites inferiores
     */
    public <T> List<T> filtrarPorTipo(List<? super T> lista, Class<T> tipoClase) {
        return lista.stream()
                    .filter(tipoClase::isInstance) // Filtra elementos que sean del tipo T
                    .map(tipoClase::cast)         // Realiza un cast seguro al tipo T
                    .collect(Collectors.toList());
    }
    
    /*
     * Método que aplica un descuento del 10% a los productos e implementa la interfaz funcional 'Consumer'
     */
    public static void procesarProductos(List<Productos> lista, Consumer<Productos> accion) {
        lista.forEach(accion);
    }
   
    public void aplicarDescuento(){
        procesarProductos(productos, 
            producto -> producto.setPrecio(producto.getPrecio() * 0.9) // Aplicar un 10% de descuento
        );

        procesarProductos(productos, 
            producto -> System.out.println("Producto: " + producto.getNombre() + ", Precio con el 10% de descuento: $" + producto.getPrecio())
        );
    }
}
