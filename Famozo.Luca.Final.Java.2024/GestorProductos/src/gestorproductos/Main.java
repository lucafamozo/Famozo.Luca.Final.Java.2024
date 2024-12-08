package gestorproductos;

import java.util.*;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.function.Predicate;

public class Main extends Application {

    private GestorProductos gestor = new GestorProductos();
    private TableView<Productos> table = new TableView<>();
    private ObservableList<Productos> listaProductos = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestor de Productos");

        // Panel de formulario para agregar productos
        VBox form = new VBox(10);

        // Campos comunes
        TextField txtNombre = new TextField();
        TextField txtPrecio = new TextField();

        // ComboBox para seleccionar el tipo de producto
        ComboBox<String> cbTipoProducto = new ComboBox<>();
        cbTipoProducto.getItems().addAll("Ropa", "Alimento", "Electrodoméstico");

        // ComboBox para seleccionar el tipo de envío
        ComboBox<TipoEnvio> cbTipoEnvio = new ComboBox<>();
        cbTipoEnvio.getItems().addAll(TipoEnvio.values());  // Agregar los valores del enum TipoEnvio
        cbTipoEnvio.setPromptText("Seleccionar tipo de envío");

        // Panel para campos específicos
        VBox panelCamposEspecificos = new VBox(10);
        TextField txtMarca = new TextField();
        TextField txtTalle = new TextField();
        TextField txtCategoria = new TextField();
        TextField txtFechaCaducidad = new TextField();
        TextField txtPotencia = new TextField();

        // Ocultar campos específicos inicialmente
        panelCamposEspecificos.setVisible(false);

        // Listener para mostrar campos según el tipo seleccionado
        cbTipoProducto.setOnAction(e -> {
            panelCamposEspecificos.getChildren().clear();
            panelCamposEspecificos.setVisible(true);

            switch (cbTipoProducto.getValue()) {
                case "Ropa":
                    panelCamposEspecificos.getChildren().addAll(
                            new Label("Marca:"), txtMarca,
                            new Label("Talle:"), txtTalle
                    );
                    break;
                case "Alimento":
                    panelCamposEspecificos.getChildren().addAll(
                            new Label("Categoría:"), txtCategoria,
                            new Label("Fecha de Caducidad (dd/mm/yyyy):"), txtFechaCaducidad
                    );
                    break;
                case "Electrodoméstico":
                    panelCamposEspecificos.getChildren().addAll(
                            new Label("Marca:"), txtMarca,
                            new Label("Potencia (W):"), txtPotencia
                    );
                    break;
                default:
                    panelCamposEspecificos.setVisible(false);
                    break;
            }
        });

        // Botón para agregar productos
        Button btnAgregar = new Button("Agregar Producto");

        // Funcionalidad del botón agregar
        btnAgregar.setOnAction(e -> {
            String nombre = txtNombre.getText();
            String tipo = cbTipoProducto.getValue();
            double precio;

            // Selección del tipo de envío
            TipoEnvio tipoEnvio = cbTipoEnvio.getValue();

            try {
                precio = Double.parseDouble(txtPrecio.getText());
            } catch (NumberFormatException ex) {
                showError("Error", "El precio debe ser un número válido.");
                return;
            }

            if (nombre.isEmpty() || tipo == null || tipoEnvio == null) {
                showError("Error", "Complete todos los campos comunes, seleccione un tipo de producto y un tipo de envío.");
                return;
            }

            try {
                Productos productoNuevo = null;
                switch (tipo) {
                    case "Ropa":
                        String marcaRopa = txtMarca.getText();
                        String talle = txtTalle.getText();
                        if (marcaRopa.isEmpty() || talle.isEmpty()) {
                            showError("Error", "Complete todos los campos específicos para ropa.");
                            return;
                        }
                        productoNuevo = new Ropa(nombre, precio, marcaRopa, talle);
                        break;

                    case "Alimento":
                        String categoria = txtCategoria.getText();
                        String fechaCaducidad = txtFechaCaducidad.getText();
                        if (categoria.isEmpty() || fechaCaducidad.isEmpty()) {
                            showError("Error", "Complete todos los campos específicos para alimento.");
                            return;
                        }
                        productoNuevo = new Alimentos(nombre, precio, fechaCaducidad, categoria);
                        break;

                    case "Electrodoméstico":
                        String marcaElectro = txtMarca.getText();
                        String potencia = txtPotencia.getText();
                        if (marcaElectro.isEmpty() || potencia.isEmpty()) {
                            showError("Error", "Complete todos los campos específicos para electrodoméstico.");
                            return;
                        }
                        productoNuevo = new Electrodomesticos(nombre, precio, marcaElectro, potencia);
                        break;

                    default:
                        showError("Error", "Tipo de producto no válido.");
                        return;
                }

                // Asignar el tipo de envío al producto
                productoNuevo.setTipoEnvio(tipoEnvio);

                // Agregar el producto a la lista
                gestor.agregar(productoNuevo);
                listaProductos.add(productoNuevo);
                gestor.exportarPorTipoEnvio(listaProductos);

                // Limpiar los campos del formulario
                limpiarCampos(txtNombre, txtPrecio, cbTipoProducto, cbTipoEnvio, txtMarca, txtTalle, txtCategoria, txtFechaCaducidad, txtPotencia);
            } catch (Excepciones.ProductoNoValidoException ex) {
                showError("Error", "Producto no válido: " + ex.getMessage());
            }
        });

        // Configuración de la tabla
        TableColumn<Productos, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colNombre.setPrefWidth(150);
        TableColumn<Productos, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecio()).asObject());
        colPrecio.setPrefWidth(150);
        TableColumn<Productos, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colId.setPrefWidth(150);
        table.getColumns().addAll(colId, colNombre, colPrecio);
        table.setItems(listaProductos);

        // Panel de control
        HBox controlPanel = new HBox(10);
        Button btnEliminar = new Button("Eliminar Producto");
        Button btnActualizar = new Button("Actualizar Producto");
        Button btnBuscar = new Button("Buscar Producto");
        Button btnOrdenar = new Button("Ordenar");
        Button btnFiltrarPorTipo = new Button("Filtrar por Tipo");
        Button guardarButton = new Button("Guardar a .txt");
        Button btnExportar = new Button("Guardar Datos");
        Button btnCargar = new Button("Cargar Datos");
        
        controlPanel.getChildren().addAll(btnEliminar, btnActualizar, btnBuscar, btnOrdenar, btnFiltrarPorTipo, guardarButton, btnExportar, btnCargar);

        // Funcionalidad del botón eliminar
        btnEliminar.setOnAction(e -> {
            Productos selectedProducto = table.getSelectionModel().getSelectedItem();
            if (selectedProducto != null) {
                try {
                    gestor.eliminar(selectedProducto.getId());
                    listaProductos.remove(selectedProducto);
                } catch (Excepciones.ProductoNoEncontradoException ex) {
                    showError("Error", "Producto no encontrado para eliminar.");
                }
            } else {
                showError("Error", "Seleccione un producto para eliminar.");
            }
        });
        
        // Funcionalidad del botón actualizar
        btnActualizar.setOnAction(e -> {
            Productos selectedProducto = table.getSelectionModel().getSelectedItem();
            if (selectedProducto != null) {
                // Dialogo de actualización
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Actualizar Producto");
                dialog.setHeaderText("Actualizar precio y nombre del producto.");
                dialog.setContentText("Nuevo Nombre y Precio:");

                dialog.showAndWait().ifPresent(input -> {
                    String[] parts = input.split(",");
                    if (parts.length == 2) {
                        try {
                            String nuevoNombre = parts[0].trim();
                            double nuevoPrecio = Double.parseDouble(parts[1].trim());
                            selectedProducto.setNombre(nuevoNombre);
                            selectedProducto.setPrecio(nuevoPrecio);
                            table.refresh();
                            showError("Éxito", "Producto actualizado correctamente.");
                        } catch (NumberFormatException ex) {
                            showError("Error", "El precio debe ser un número válido.");
                        }
                    } else {
                        showError("Error", "Formato inválido. Use 'nombre,precio'.");
                    }
                });
            } else {
                showError("Error", "Seleccione un producto para actualizar.");
            }
        });

        // Funcionalidad del botón buscar
        btnBuscar.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Buscar Producto");
            dialog.setHeaderText("Ingrese el nombre del producto");
            dialog.setContentText("Nombre:");

            dialog.showAndWait().ifPresent(nombre -> {
                Productos producto = buscarProductoPorNombre(nombre);
                if (producto != null) {
                    mostrarInformacionProducto(producto);
                } else {
                    showError("Error", "Producto no encontrado.");
                }
            });
        });
        
        // Funcionalidad del botón para "Ordenar"
        btnOrdenar.setOnAction(e -> {
            // Crear un diálogo de tipo 'ChoiceDialog' para elegir el criterio de ordenación
            ChoiceDialog<String> dialog = new ChoiceDialog<>("Ordenar alfabéticamente", "Ordenar alfabéticamente", "Ordenar por precio");
            dialog.setTitle("Ordenar productos");
            dialog.setHeaderText("Seleccione el criterio para ordenar los productos:");
            dialog.setContentText("Criterio de ordenación:");

            // Mostrar el diálogo y esperar la respuesta
            dialog.showAndWait().ifPresent(criterioSeleccionado -> {
                try {
                    if ("Ordenar alfabéticamente".equals(criterioSeleccionado)) {
                        // Ordenar alfabéticamente
                        gestor.ordenarProductosPor(Comparator.comparing(Productos::getNombre));
                        table.setItems(FXCollections.observableList(gestor.listarProductos()));
                        showError("Éxito", "Productos ordenados alfabéticamente.");
                    } else if ("Ordenar por precio".equals(criterioSeleccionado)) {
                        // Ordenar por precio
                        gestor.ordenarProductosPor(Comparator.comparingDouble(Productos::getPrecio));
                        table.setItems(FXCollections.observableList(gestor.listarProductos()));
                        showError("Éxito", "Productos ordenados por precio.");
                    }
                } catch (Exception ex) {
                    showError("Error", "Hubo un problema al ordenar los productos: " + ex.getMessage());
                }
            });
        });
        
        // Funcionalidad del botón filtrar
        btnFiltrarPorTipo.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Filtrar Productos");
            dialog.setHeaderText("Ingrese el tipo de producto (Ropa, Alimento, Electrodoméstico)");
            dialog.setContentText("Tipo:");

            dialog.showAndWait().ifPresent(tipoSeleccionado -> {
                try {
                    Iterator<Productos> iteradorFiltrado = gestor.obtenerIteradorPorTipo(tipoSeleccionado);
                    ObservableList<Productos> productosFiltrados = FXCollections.observableArrayList();

                    while (iteradorFiltrado.hasNext()) {
                        productosFiltrados.add(iteradorFiltrado.next());
                    }

                    table.setItems(productosFiltrados); // Mostrar los productos filtrados en la tabla
                } catch (Exception ex) {
                    showError("Error", "No se pudo filtrar los productos: " + ex.getMessage());
                }
            });
        });

        // Funcionalidad del botón Guardar a .txt
        guardarButton.setOnAction(e -> {
            // Crear la nueva ventana para elegir el filtro
            Stage filtroStage = new Stage();
            filtroStage.setTitle("Selecciona el Filtro");

            // Crear el ComboBox con las opciones de filtro
            ComboBox<String> comboTipoProducto = new ComboBox<>();
            comboTipoProducto.getItems().addAll("Ropa", "Alimentos", "Electrodomesticos");
            comboTipoProducto.setPromptText("Selecciona el tipo de producto");

            // Crear el botón de aceptar
            Button aceptarButton = new Button("Aceptar");

            // Crear un contenedor de diseño para la ventana
            VBox layout = new VBox(10);
            layout.getChildren().addAll(comboTipoProducto, aceptarButton);

            // Configurar la escena y mostrar la ventana
            Scene scene = new Scene(layout, 300, 150);
            filtroStage.setScene(scene);
            filtroStage.show();

            // Manejador del evento del botón "Aceptar"
            aceptarButton.setOnAction(event -> {
                // Obtener el tipo de producto seleccionado
                String tipoSeleccionado = comboTipoProducto.getValue();

                if (tipoSeleccionado != null) {
                    // Filtrar los productos según el tipo seleccionado
                    Predicate<Productos> criterio = producto -> {
                        switch (tipoSeleccionado) {
                            case "Ropa":
                                return producto instanceof Ropa;
                            case "Alimentos":
                                return producto instanceof Alimentos;
                            case "Electrodomesticos":
                                return producto instanceof Electrodomesticos;
                            default:
                                return false;
                        }
                    };

                    // Llamar al método exportarListado para exportar el archivo .txt
                    gestor.exportarListado(listaProductos, tipoSeleccionado, criterio);
                    
                    // Cerrar la ventana de filtro
                    filtroStage.close();
                } else {
                    // Si no se selecciona un tipo, mostrar mensaje
                    System.out.println("Por favor, selecciona un tipo de producto.");
                }
            });
        });

        // Funcionalidad del botón para "Guardar"
        btnExportar.setOnAction(e -> {
            // Crear un diálogo de tipo 'ChoiceDialog' para elegir el formato
            ChoiceDialog<String> dialog = new ChoiceDialog<>("JSON", "JSON", "CSV");
            dialog.setTitle("Guardar archivo");
            dialog.setHeaderText("Seleccione el formato para guardar:");
            dialog.setContentText("Formato:");

            // Mostrar el diálogo y esperar la respuesta
            dialog.showAndWait().ifPresent(formatoSeleccionado -> {
                try {
                    if ("JSON".equals(formatoSeleccionado)) {
                        // Llamar al método para guardar en JSON
                        gestor.guardarProductosEnJson("listaProductos.json");                       
                    } else if ("CSV".equals(formatoSeleccionado)) {
                        // Llamar al método para guardar en CSV
                        gestor.guardarProductosEnCsv("listaProductos.csv");                       
                    }
                } catch (Exception ex) {
                    showError("Error", "Hubo un problema al guardar el archivo: " + ex.getMessage());
                }
            });
        });

        // Funcionalidad del botón cargar
        btnCargar.setOnAction(e -> {
            // Crear un diálogo de tipo 'ChoiceDialog' para elegir el formato
            ChoiceDialog<String> dialog = new ChoiceDialog<>("JSON", "JSON", "CSV");
            dialog.setTitle("Cargar archivo");
            dialog.setHeaderText("Seleccione el formato para cargar:");
            dialog.setContentText("Formato:");

            // Mostrar el diálogo y esperar la respuesta
            dialog.showAndWait().ifPresent(formatoSeleccionado -> {
                try {
                    if ("JSON".equals(formatoSeleccionado)) {
                        // Llamar al método para cargar desde JSON
                        gestor.cargarProductosDesdeJson("listaProductos.json");
                    } else if ("CSV".equals(formatoSeleccionado)) {
                        // Llamar al método para cargar desde CSV
                        gestor.cargarProductosDesdeCsv("listaProductos.csv");
                    }

                    // Después de cargar los productos, actualizamos la tabla
                    ObservableList<Productos> productosCargados = FXCollections.observableArrayList(gestor.obtenerProductos());
                    table.setItems(productosCargados);  // Establecer los productos en la tabla

                } catch (Exception ex) {
                    showError("Error", "Hubo un problema al cargar el archivo: " + ex.getMessage());
                }
            });
        });


        // Agregar elementos al formulario
        form.getChildren().addAll(
                new Label("Nombre:"), txtNombre,
                new Label("Precio:"), txtPrecio,
                new Label("Tipo de Producto:"), cbTipoProducto,
                new Label("Tipo de Envío:"), cbTipoEnvio, // Agregar este campo al formulario
                panelCamposEspecificos,
                btnAgregar
        );

        // Layout principal
        VBox root = new VBox(20);
        root.setPadding(new javafx.geometry.Insets(20));
        root.getChildren().addAll(form, controlPanel, table);

        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Productos buscarProductoPorNombre(String nombre) {
        for (Productos producto : listaProductos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }
        return null;
    }

    private void mostrarInformacionProducto(Productos producto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información del Producto");
        alert.setHeaderText("Detalles del Producto: " + producto.getNombre());

        StringBuilder detalles = new StringBuilder();
        detalles.append("ID: ").append(producto.getId()).append("\n");
        detalles.append("Nombre: ").append(producto.getNombre()).append("\n");
        detalles.append("Precio: ").append(producto.getPrecio()).append("\n");
        detalles.append("Tipo de Producto: ").append(producto.getClass().getSimpleName()).append("\n");
        detalles.append("Tipo de Envío: ").append(producto.getTipoEnvio()).append("\n");

        alert.setContentText(detalles.toString());
        alert.showAndWait();
    }

    private void limpiarCampos(TextField txtNombre, TextField txtPrecio, ComboBox<String> cbTipoProducto, ComboBox<TipoEnvio> cbTipoEnvio, 
                            TextField txtMarca, TextField txtTalle, TextField txtCategoria, TextField txtFechaCaducidad, TextField txtPotencia) {
        txtNombre.clear();
        txtPrecio.clear();
        cbTipoProducto.setValue(null);  // Limpia la selección del ComboBox
        cbTipoEnvio.setValue(null);     // Limpia la selección del ComboBox
        txtMarca.clear();
        txtTalle.clear();
        txtCategoria.clear();
        txtFechaCaducidad.clear();
        txtPotencia.clear();
    }


    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
