/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.Utilsapp;

/**
 *
 * @author dmarcobo
 */
public class ExcelReader {
    private final int ROWSTART = 13;
    private final int ROWSTARTSTATUS = 3;
    private final String FMTDATE = "MM/dd/yy";
    private final String FMTDATE2 = "dd/MM/yy";
    private final List<DesvioData> listadoDesvios;
    private final HashMap<String,DesvioData> estadoPeticiones;

    public ExcelReader() {
        listadoDesvios = new ArrayList<>();
        estadoPeticiones = new HashMap<>();
    }

    /**
     * @return the estadoPeticiones
     */
    public HashMap<String,DesvioData> getEstadoPeticiones() {
        return estadoPeticiones;
    }

    /**
     * @return the listadoDesvios
     */
    public List<DesvioData> getListadoDesvios() {
        return listadoDesvios;
    }
    

    public enum Error {
        NONE, XLSNOTFOUND, OTHER;
    }

    public enum TipoPet {
        PETICION(0,"PET"), OT(1,"OT"), OTHER(2,"Otro");
        
        private final double code;   // in kilograms
        private final String text; // in meters
        
        TipoPet(double code, String text) {
            this.code = code;
            this.text = text;
        }
        
        public double code() { return code; }
        public String text() { return text; }
    }
    
    public enum EstadoPet{
        APLAZADA(0,"Aplazada"), BORRADOR(1,"Borrador"), DESESTIMADA(2,"Desestimada"),
        EJECUCION(3,"Ejecución"), PROPUESTA(4,"Pdte. Propuesta"), VALIDACION(5,"Pdte. Validación"), ENTREGADA(6,"Entregada"), 
        OTHER(7,"Otro");
        
        private final double code;   // in kilograms
        private final String text; // in meters
        
        EstadoPet(double code, String text) {
            this.code = code;
            this.text = text;
        }
        
        public double code() { return code; }
        public String text() { return text; }
    }

    public class DesvioData {

        private int peticion;
        private int ot;
        private TipoPet tipo;
        private String nombrePet;
        private String actor;
        private Date fechaFinAcuerdo;
        private EstadoPet estado;
        private String StrPetStatus;

        public DesvioData() {
            peticion = 0;
            ot = 0;
            tipo = TipoPet.OTHER;
            nombrePet = "";
            actor = "";
            fechaFinAcuerdo = null;
            estado = EstadoPet.OTHER;
            StrPetStatus = "";
        }
        
        /**
         * @return the peticion
         */
        public int getPeticion() {
            return peticion;
        }

        /**
         * @param peticion the peticion to set
         */
        public void setPeticion(int peticion) {
            this.peticion = peticion;
        }

        /**
         * @return the tipo
         */
        public TipoPet getTipo() {
            return tipo;
        }

        /**
         * @param tipo the tipo to set
         */
        public void setTipo(TipoPet tipo) {
            this.tipo = tipo;
        }

        /**
         * @return the nombrePet
         */
        public String getNombrePet() {
            return nombrePet;
        }

        /**
         * @param nombrePet the nombrePet to set
         */
        public void setNombrePet(String nombrePet) {
            this.nombrePet = nombrePet;
        }

        /**
         * @return the actor
         */
        public String getActor() {
            return actor;
        }

        /**
         * @param actor the actor to set
         */
        public void setActor(String actor) {
            this.actor = actor;
        }

        /**
         * @return the fechaFinAcuerdo
         */
        public Date getFechaFinAcuerdo() {
            return fechaFinAcuerdo;
        }

        /**
         * @param fechaFinAcuerdo the fechaFinAcuerdo to set
         */
        public void setFechaFinAcuerdo(Date fechaFinAcuerdo) {
            this.fechaFinAcuerdo = fechaFinAcuerdo;
        }

        /**
         * @return the estado
         */
        public EstadoPet getEstado() {
            return estado;
        }

        /**
         * @param estado the estado to set
         */
        public void setEstado(EstadoPet estado) {
            this.estado = estado;
        }

        /**
         * @return the StrPetStatus
         */
        public String getStrPetStatus() {
            return StrPetStatus;
        }

        /**
         * @param StrPetStatus the StrPetStatus to set
         */
        public void setStrPetStatus(String StrPetStatus) {
            this.StrPetStatus = StrPetStatus;
        }

        /**
         * @return the ot
         */
        public int getOt() {
            return ot;
        }

        /**
         * @param ot the ot to set
         */
        public void setOt(int ot) {
            this.ot = ot;
        }
    }

    

    public Error readExcel(String path) {
        try {
            InputStream inp = new FileInputStream(path);
            XSSFWorkbook wb = new XSSFWorkbook(inp);
            DataFormatter formatter = new DataFormatter();
            XSSFSheet sheet1 = wb.getSheetAt(0);

            String refs[];
            getListadoDesvios().clear();
            String dato;
            int rowCount = 1;

            for (Row row : sheet1) {
                if (rowCount > ROWSTART) {
                    DesvioData desvio = new DesvioData();
                    for (Cell cell : row) {
                        CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                        refs = cellRef.getCellRefParts();
                        if (refs.length >= 3) {
                            if ("AD".equals(refs[2])) {
                                desvio.setActor(formatter.formatCellValue(cell));
                            } else if ("V".equals(refs[2])) {
                                dato = formatter.formatCellValue(cell);
                                if (dato != null && !dato.isEmpty()) {
                                    try {
                                        Date fecha = Utilsapp.strToDate(dato, FMTDATE2);
                                        desvio.setFechaFinAcuerdo(fecha);
                                        //System.out.println(dato + " - " + Utilsapp.dateToStr(fecha, "dd/MM/yyyy hh:mm:ss"));
                                    } catch (ParseException e) {
                                        desvio.setFechaFinAcuerdo(null);
                                    }
                                }
                            } else if ("A".equals(refs[2])) {
                                dato = formatter.formatCellValue(cell);
                                if (dato != null && !dato.isEmpty()) {

                                    try {
                                        desvio.setPeticion(Integer.parseInt(dato));
                                    } catch (NumberFormatException e) {
                                        desvio.setPeticion(0);
                                    }
                                }
                            } else if ("G".equals(refs[2])) {
                                dato = formatter.formatCellValue(cell);
                                if (dato != null && !dato.isEmpty()) {
                                    try {
                                        desvio.setOt(Integer.parseInt(dato));
                                    } catch (NumberFormatException e) {
                                        desvio.setOt(0);
                                    }
                                }
                            } else if ("I".equals(refs[2])) {
                                desvio.nombrePet = formatter.formatCellValue(cell);
                            } else if ("H".equals(refs[2])) {
                                dato = formatter.formatCellValue(cell);
                                desvio.tipo = "PET".equals(dato) ? TipoPet.PETICION : "OT".equals(dato) ? TipoPet.OT : TipoPet.OTHER;
                            } else if ("F".equals(refs[2])) {
                                if ("APLAZADA".equals(formatter.formatCellValue(cell))){
                                    desvio.estado = EstadoPet.APLAZADA;
                                } else if ("BORRADOR".equals(formatter.formatCellValue(cell))){
                                    desvio.estado = EstadoPet.BORRADOR;
                                } else if ("DESESTIMADA".equals(formatter.formatCellValue(cell))){
                                    desvio.estado = EstadoPet.DESESTIMADA;
                                }  else if ("EN_EJECUCION".equals(formatter.formatCellValue(cell))){
                                    desvio.estado = EstadoPet.EJECUCION;
                                } else if ("PTE_PROPUESTA".equals(formatter.formatCellValue(cell))){
                                    desvio.estado = EstadoPet.PROPUESTA;
                                } else if ("PTE_VALIDACION".equals(formatter.formatCellValue(cell))){
                                    desvio.estado = EstadoPet.VALIDACION;
                                } else if ("ENTREGADA".equals(formatter.formatCellValue(cell))){
                                    desvio.estado = EstadoPet.ENTREGADA;
                                } else {
                                    desvio.estado = EstadoPet.OTHER;
                                }
                            }
                        }
                    }
                    getListadoDesvios().add(desvio);
                }
                rowCount++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Error.XLSNOTFOUND;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Error.OTHER;
        }
        return Error.NONE;
    }
    
    
    /**
     * 
     * @param path
     * @return 
     */
    public Error readActualPetStatus(String path) {
        try {
            InputStream inp = new FileInputStream(path);
            HSSFWorkbook wb = new HSSFWorkbook(inp);
            DataFormatter formatter = new DataFormatter();
            HSSFSheet sheet1 = wb.getSheetAt(0);

            String refs[];
            estadoPeticiones.clear();
            String dato;
            int rowCount = 1;

            for (Row row : sheet1) {
                if (rowCount >= ROWSTARTSTATUS) {
                    DesvioData desvio = new DesvioData();
                    for (Cell cell : row) {
                        CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                        refs = cellRef.getCellRefParts();
                        if (refs.length >= 3) {
                            if ("A".equals(refs[2])) {
                                dato = formatter.formatCellValue(cell);
                                if (dato != null && !dato.isEmpty()) {

                                    try {
                                        desvio.setPeticion(Integer.parseInt(dato));
                                    } catch (NumberFormatException e) {
                                        desvio.setPeticion(0);
                                    }
                                }
                            } else if ("C".equals(refs[2])) {
                                desvio.nombrePet = formatter.formatCellValue(cell);
                            } else if ("E".equals(refs[2])) {
                                desvio.StrPetStatus = formatter.formatCellValue(cell);
                            }
                        }
                    }
                    estadoPeticiones.put(String.valueOf(desvio.getPeticion()), desvio);
                }
                rowCount++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Error.XLSNOTFOUND;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Error.OTHER;
        }
        return Error.NONE;
    }
    
    
    
}
