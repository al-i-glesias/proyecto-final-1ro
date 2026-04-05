package com.sucursalbancaria.Controllers.ControlVistas;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.sucursalbancaria.Controllers.Logica.SucursalBancaria;
import com.sucursalbancaria.Models.Solicitantes.Empresa;
import com.sucursalbancaria.Models.Solicitantes.Persona;
import com.sucursalbancaria.Models.Solicitantes.Solicitante;
import com.sucursalbancaria.Models.Solicitudes.SolicitudCredito;

import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

public class MainController {
    
    SucursalBancaria sucursalBancaria = new SucursalBancaria();

    @FXML
    MenuItem nuevoBtn;

    @FXML
    TableView<Empresa> tablaEmpresas;
    
    @FXML
    TableView<Persona> tablaPersonas;

    @FXML
    TableView<SolicitudCredito<Solicitante>> tablaSolicitudes;

    @FXML
    Button nuevaEMpresa;

    @FXML
    Button nuevaPersona;

    @FXML
    Button nuevaSolicitud;


    @FXML
    public void initialize(){

        tablaEmpresas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tablaPersonas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tablaSolicitudes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        tablaEmpresas.setEditable(true);
        tablaEmpresas.setItems(FXCollections.observableArrayList());
        configurarColumnas(tablaEmpresas, nombresColumnasEmpresa(), Empresa.class);

        tablaPersonas.setEditable(true);
        tablaPersonas.setItems(FXCollections.observableArrayList());
        configurarColumnas(tablaPersonas, nombresColumnasPersona(), Persona.class);

    }

    public <T> void configurarColumnas(TableView<?> tabla, Map<String, String> nombresColumnas, Class<T> clazz){

        
        tabla.getColumns().forEach(columna -> {
            String nombreCampo = nombresColumnas.get(columna.getText());

            Field campoActual = null;

            Class<T> claseActual = clazz;

            while (claseActual != null) {
                
                try{
                    campoActual = claseActual.getDeclaredField(nombreCampo);
                    campoActual.setAccessible(true);
                    break;
                }
                catch(NoSuchFieldException ex){

                    claseActual = (Class<T>)claseActual.getSuperclass();
                }
            }

            final Field campo = campoActual;

            try{

                if(campo.getType() == String.class){

                    @SuppressWarnings("unchecked")
                    TableColumn<T, String> c = (TableColumn<T, String>) columna;
                    
                    c.setCellValueFactory(new PropertyValueFactory<>(nombreCampo));
                    c.setCellFactory(TextFieldTableCell.forTableColumn());
                    c.setOnEditCommit(e -> {
                        try{
                            campo.set(e.getRowValue(), e.getNewValue());
                        }
                        catch(IllegalAccessException ill){
                            System.out.println(ill.getMessage());
                        }
                        
                    });
                }
                else if(campo.getType() == Double.class || campo.getType() == double.class){

                    @SuppressWarnings("unchecked")
                    TableColumn<T, Double> c = (TableColumn<T, Double>) columna;

                    c.setCellValueFactory(new PropertyValueFactory<>(nombreCampo));
                    c.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
                    c.setOnEditCommit(e -> {
                        try{

                            campo.set(e.getRowValue(), e.getNewValue());

                        }catch(IllegalAccessException ill){

                            System.out.println(ill.getMessage());
                        }
                    });
                }
                else if(campo.getType() == Integer.class || campo.getType() == int.class){

                    @SuppressWarnings("unchecked")
                    TableColumn<T, Integer> c = (TableColumn<T, Integer>) columna;

                    c.setCellValueFactory(new PropertyValueFactory<>(nombreCampo));
                    c.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
                    c.setOnEditCommit(e -> {
                        try{
                            campo.set(e.getRowValue(), e.getNewValue());
                        }catch(IllegalAccessException ill){

                            System.out.println(ill.getMessage());
                        }
                    });

                }
                else if(campo.getType() == Long.class || campo.getType() == long.class){

                    @SuppressWarnings("unchecked")
                    TableColumn<T, Long> c = (TableColumn<T, Long>) columna;

                    c.setCellValueFactory(new PropertyValueFactory<>(nombreCampo));
                    c.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
                    c.setOnEditCommit(e -> {

                        try{

                            campo.set(e.getRowValue(), e.getNewValue());

                        }catch(IllegalAccessException ill){

                            System.out.println(ill.getMessage());
                        }
                    });
                }

            }catch(ClassCastException e){

                System.out.println("Error de tipo en la columna: "+columna.getText());
            }
        });
    }

    @FXML
    public void agregarEmpresa() throws Exception {

        tablaEmpresas.getItems().add(new Empresa());

    }

    @FXML
    public void agregarPersona() throws Exception {

        tablaPersonas.getItems().add(new Persona());
    }

    @FXML
    public void agregarSolicitud(){

    }


    @FXML
    public void nuevoArchivo() throws IOException {

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Nuevo archivo");

        dialog.setHeaderText("Ingrese el nombre del archivo");

        Optional<String> resultado = dialog.showAndWait();

        resultado.ifPresent(nombre -> {
            File archivo;

            File carpeta = new File("Datos");

            if(!carpeta.exists()){

                carpeta.mkdir();

            }
            archivo = new File(carpeta, nombre+".bin");

            try{
                if(archivo.createNewFile()){
                    System.out.println("archivo creado correctamente");
                }
                else{
                    System.out.println("no se completo la creacion del archivo");
                }
            }catch(IOException err){

            }
        });
        
    }

    @FXML
    public void cerrarApp(){

        System.exit(1);

    }

    



    //UTILIDAD

    public Map<String, String> nombresColumnasEmpresa() {
        Map<String, String> nombresColumnas = new HashMap<>();

        nombresColumnas.put("Nombre", "nombreSolicitante");
        nombresColumnas.put("Valor del Crédito", "valorCredito");
        nombresColumnas.put("Dirección", "direccionSolicitante");
        nombresColumnas.put("Director", "nombreDirector");
        nombresColumnas.put("Dirección del Director", "direccionDirector");
        nombresColumnas.put("Ganancia Promedio Anual", "gananciaPromedioAnual");
        nombresColumnas.put("Cantidad de Trabajadores", "cantidadTrabajadores");
        nombresColumnas.put("Ministerio", "ministerio");
        nombresColumnas.put("Código", "codigo");

        return nombresColumnas;
    }

    public Map<String, String> nombresColumnasPersona() {
        Map<String, String> nombresColumnas = new HashMap<>();

        nombresColumnas.put("Nombre", "nombreSolicitante");
        nombresColumnas.put("Valor del Crédito", "valorCredito");
        nombresColumnas.put("Dirección", "direccionSolicitante");
        nombresColumnas.put("CI", "CI");
        nombresColumnas.put("Salario del Núcleo Familiar", "salarioNucleo");
        nombresColumnas.put("Personas que Sustenta", "personasQueSustenta");

        return nombresColumnas;
    }

}
