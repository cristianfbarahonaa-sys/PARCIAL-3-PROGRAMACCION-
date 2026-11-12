

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.LinkedList;

public class Main {
    static EcoRuta ecoRuta = new EcoRuta("EcoRuta");

    public static void main(String[] args) {
        
        ecoRuta.registrarAportante("1094", "Juan Perez", "juan@correo.com");
        ecoRuta.registrarAportante("8900", "Comercio Quindio", "ventas@comercio.com");
        ecoRuta.registrarAportante("9001", "Universidad del Quindio", "uq@uniquindio.edu.co");

        boolean bandera = true;
        do {
            int dato = Integer.parseInt(JOptionPane.showInputDialog(null, "MENU DE EcoRuta" +
                    "\n1. Registrar aportante" +
                    "\n2. Registrar entrega" +
                    "\n3. Agregar lote a una entrega" +
                    "\n4. Aceptar entrega" +
                    "\n5. Menu Consultas" +
                    "\n0. Salir del sistema"));

            switch (dato) {
                case 1:
                    registrarAportante();
                    break;
                case 2:
                    registrarEntrega();
                    break;
                case 3:
                    agregarLoteEntrega();
                    break;
                case 4:
                    aceptarEntrega();
                    break;
                case 5:
                    menuConsultar();
                    break;
                case 0:
                    bandera = false;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "opcion no valida");
            }
        } while (bandera);
    }

    public static void agregarLoteEntrega() {
        boolean bandera = true;

        do {
            int dato = Integer.parseInt(JOptionPane.showInputDialog(null, "Escoja el tipo de lote a agregar\n" +
                    "1. Registrar lote de Papel y Carton\n" +
                    "2. Registrar lote de Metal\n" +
                    "3. Registrar lote de Residuos Electronicos\n" +
                    "0. Salir"));

            switch (dato) {
                case 1:
                    registrarLotePapel();
                    break;
                case 2:
                    registrarLoteMetal();
                    break;
                case 3:
                    registrarLoteElectronico();
                    break;
                case 0:
                    bandera = false;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "opcion no valida");
            }
        } while (bandera);
    }

    public static void menuConsultar() {
        boolean bandera = true;

        do {
            int dato = Integer.parseInt(JOptionPane.showInputDialog(null, "Menu de consulta\n" +
                    "1. Consultar comprobante por codigo de entrega\n" +
                    "2. Consultar etiquetas por codigo de entrega\n" +
                    "3. Consultar valor acumulado por aportante\n" +
                    "0. Salir"));

            switch (dato) {
                case 1:
                    consultarComprobanteCodigo();
                    break;
                case 2:
                    consultarEtiquetasCodigo();
                    break;
                case 3:
                    consultarValorAcumuladoId();
                    break;
                case 0:
                    bandera = false;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "opcion no valida");
            }
        } while (bandera);
    }

   
    public static void registrarAportante() {
        String identificacion = JOptionPane.showInputDialog("Ingrese la identificación del aportante:");
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del aportante:");
        String correo = JOptionPane.showInputDialog("Ingrese el correo del aportante:");

        boolean verificacion = ecoRuta.registrarAportante(identificacion, nombre, correo);

        if (verificacion) {
            JOptionPane.showMessageDialog(null, "¡Aportante registrado con éxito!");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo registrar el aportante.");
        }
    }

    public static void registrarEntrega() {
        String codigo = JOptionPane.showInputDialog("Ingrese el código de la entrega (Ej: E01):");

        String fechaStr = JOptionPane.showInputDialog("Ingrese la fecha (Ej: 2026-11-10):");
        LocalDate fecha = LocalDate.parse(fechaStr);

        String idAportante = JOptionPane.showInputDialog("Ingrese la identificación del aportante:");
        Aportante aportante = ecoRuta.consultarAportanteId(idAportante);

        boolean verificacion = ecoRuta.registrarEntrega(codigo, fecha, aportante);

        if (verificacion) {
            JOptionPane.showMessageDialog(null, "¡Entrega registrada en estado ABIERTA con éxito!");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo registrar la entrega.");
        }
    }

    public static void registrarLotePapel() {
        String codigoEntrega = JOptionPane.showInputDialog("Ingrese el código de la entrega:");
        Entrega theEntrega = ecoRuta.consultarEntregaCodigo(codigoEntrega);

        String codigo = JOptionPane.showInputDialog("Ingrese el código del lote:");
        String descripcion = JOptionPane.showInputDialog("Ingrese la descripción:");
        int peso = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el peso en kilogramos:"));

        String opcionClasificado = JOptionPane.showInputDialog(null, "¿El material fue clasificado antes de entregarse?\nsi\nno");
        boolean clasificado;
        if (opcionClasificado.equalsIgnoreCase("si")) {
            clasificado = true;
        } else {
            clasificado = false;
        }

        boolean verificacion = false;
        if (theEntrega != null) {
            LotePapelCarton nuevoLote = new LotePapelCarton(codigo, descripcion, peso, clasificado);
            verificacion = theEntrega.agregarLote(nuevoLote);
        }

        if (verificacion) {
            JOptionPane.showMessageDialog(null, "Lote agregado exitosamente");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo agregar el lote");
        }
    }

    public static void registrarLoteMetal() {
        String codigoEntrega = JOptionPane.showInputDialog("Ingrese el código de la entrega:");
        Entrega theEntrega = ecoRuta.consultarEntregaCodigo(codigoEntrega);

        String codigo = JOptionPane.showInputDialog("Ingrese el código del lote:");
        String descripcion = JOptionPane.showInputDialog("Ingrese la descripción:");
        int peso = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el peso en kilogramos:"));

        String metalStr = JOptionPane.showInputDialog("Ingrese el tipo de metal (ALUMINIO, ACERO):");
        TipoMetal tipoMetal = TipoMetal.valueOf(metalStr.toUpperCase());

        boolean verificacion = false;
        if (theEntrega != null) {
            LoteMetal nuevoLote = new LoteMetal(codigo, descripcion, peso, tipoMetal);
            verificacion = theEntrega.agregarLote(nuevoLote);
        }

        if (verificacion) {
            JOptionPane.showMessageDialog(null, "Lote agregado exitosamente");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo agregar el lote");
        }
    }

    public static void registrarLoteElectronico() {
        String codigoEntrega = JOptionPane.showInputDialog("Ingrese el código de la entrega:");
        Entrega theEntrega = ecoRuta.consultarEntregaCodigo(codigoEntrega);

        String codigo = JOptionPane.showInputDialog("Ingrese el código del lote:");
        String descripcion = JOptionPane.showInputDialog("Ingrese la descripción:");
        int peso = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el peso en kilogramos:"));
        int equipos = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de equipos completos:"));

        boolean verificacion = false;
        if (theEntrega != null && equipos >= 0) {
            LoteResiduoElectronico nuevoLote = new LoteResiduoElectronico(codigo, descripcion, peso, equipos);
            verificacion = theEntrega.agregarLote(nuevoLote);
        }

        if (verificacion) {
            JOptionPane.showMessageDialog(null, "Lote agregado exitosamente");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo agregar el lote");
        }
    }

    public static void aceptarEntrega() {
        String codigoEntrega = JOptionPane.showInputDialog("Ingrese el codigo de la entrega a aceptar:");
        Entrega entrega = ecoRuta.consultarEntregaCodigo(codigoEntrega);

        boolean res = ecoRuta.aceptarEntrega(entrega);

        if (res) {
            JOptionPane.showMessageDialog(null, "¡Entrega aceptada y comprobante generado con exito!");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo aceptar la entrega.");
        }
    }

    
    public static void consultarComprobanteCodigo() {
        String dato = JOptionPane.showInputDialog("Ingrese el codigo de la entrega:");
        Comprobante comprobante = ecoRuta.consultarComprobanteCodigo(dato);

        if (comprobante != null) {
            JOptionPane.showMessageDialog(null, "Comprobante Encontrado" +
                    "\n Codigo de entrega: " + comprobante.codigoEntrega() +
                    "\n Identificacion aportante: " + comprobante.identificacionAportante() +
                    "\n Peso total (kg): " + comprobante.pesoTotalKg() +
                    "\n Valor total reconocido: $" + comprobante.valorTotalReconocido());
        } else {
            JOptionPane.showMessageDialog(null, "No existe comprobante para esa entrega.");
        }
    }

    public static void consultarEtiquetasCodigo() {
        String dato = JOptionPane.showInputDialog("Ingrese el codigo de la entrega para consultar las etiquetas:");
        LinkedList<String> listaConsultada = ecoRuta.consultarEtiquetasEntrega(dato);
        String mensaje = "ETIQUETAS de la entrega: \n";

        if (listaConsultada != null) {
            for (String etiqueta : listaConsultada) {
                mensaje += etiqueta + "\n";
            }
        }

        JOptionPane.showMessageDialog(null, mensaje);
    }

    public static void consultarValorAcumuladoId() {
        String dato = JOptionPane.showInputDialog("Ingrese la identificacion del aportante:");
        int totalAcumulado = ecoRuta.calcularValorAcumuladoAportante(dato);

        JOptionPane.showMessageDialog(null, "Identificacion: " + dato +
                "\nValor total acumulado reconocido: $" + totalAcumulado);
    }
}