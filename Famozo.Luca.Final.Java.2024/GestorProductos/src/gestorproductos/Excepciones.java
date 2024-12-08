package gestorproductos;

/**
 *
 * @author Luca
 */
public class Excepciones {

    /**
     * Excepción lanzada cuando ocurre un error al intentar cargar o guardar productos.
     */
    public static class ArchivoNoValidoException extends Exception {
        public ArchivoNoValidoException(String mensaje) {
            super(mensaje);
        }

        public ArchivoNoValidoException(String mensaje, Throwable causa) {
            super(mensaje, causa);
        }
    }

    /**
     * Excepción lanzada cuando un producto no se encuentra en el gestor de productos.
     */
    public static class ProductoNoEncontradoException extends Exception {
        public ProductoNoEncontradoException(String mensaje) {
            super(mensaje);
        }

        public ProductoNoEncontradoException(String mensaje, Throwable causa) {
            super(mensaje, causa);
        }
    }

    /**
     * Excepción lanzada cuando un producto tiene un precio no válido (por ejemplo, precio negativo).
     */
    public static class PrecioInvalidoException extends Exception {
        public PrecioInvalidoException(String mensaje) {
            super(mensaje);
        }

        public PrecioInvalidoException(String mensaje, Throwable causa) {
            super(mensaje, causa);
        }
    }

    /**
     * Excepción lanzada cuando no se puede agregar un producto debido a algún error interno.
     */
    public static class ProductoNoValidoException extends Exception {
        public ProductoNoValidoException(String mensaje) {
            super(mensaje);
        }

        public ProductoNoValidoException(String mensaje, Throwable causa) {
            super(mensaje, causa);
        }
    }
}
