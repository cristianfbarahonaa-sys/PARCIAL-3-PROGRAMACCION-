import java.time.LocalDate;
import java.util.LinkedList;

public class EcoRuta {

    private String nombre;

    private LinkedList<Aportante> listAportantes;
    private LinkedList<Entrega> listEntregas;
    private LinkedList<Comprobante> listComprobantes;

   
    public EcoRuta(String nombre) {
        this.nombre = nombre;

        this.listAportantes = new LinkedList<>();
        this.listEntregas = new LinkedList<>();
        this.listComprobantes = new LinkedList<>();
    }

   
    public boolean validarCodigoEntrega(String codigoNuevo) {
        boolean validado = true;

        if (this.listEntregas != null) {
            for (Entrega entrega : listEntregas) {
                if (entrega != null) {
                    if (codigoNuevo.equals(entrega.getCodigo())) {
                        validado = false;
                    }
                }
            }
        }
        return validado;
    }

    
    public boolean registrarAportante(String identificacion, String nombre, String correo) {
        for (Aportante aportante : this.listAportantes) {
            if (aportante.identificacion().equals(identificacion)) {
                return false;
            }
        }
        Aportante nuevoAportante = new Aportante(identificacion, nombre, correo);
        return this.listAportantes.add(nuevoAportante);
    }

    public boolean registrarEntrega(String codigo, LocalDate fecha, Aportante aportante) {
        
        if (aportante != null && validarCodigoEntrega(codigo)) {
            for (Entrega entrega : this.listEntregas) {
                if (entrega.getCodigo().equals(codigo)) {
                    return false;
                }
            }

            Entrega nuevaEntrega = new Entrega(codigo, fecha, aportante);
            return this.listEntregas.add(nuevaEntrega);
        } else {
            return false;
        }
    }

    
    public boolean aceptarEntrega(Entrega entrega) {
        if (entrega != null) {
            Comprobante nuevoComprobante = entrega.aceptar();
            if (nuevoComprobante != null) {
                this.listComprobantes.add(nuevoComprobante);
                return true;
            }
        }
        return false;
    }

   
    public Aportante consultarAportanteId(String id) {
        if (listAportantes != null) {
            for (Aportante aportante : listAportantes) {
                if (aportante != null) {
                    if (aportante.identificacion().equals(id)) {
                        return aportante;
                    }
                }
            }
        }
        return null;
    }

    public Entrega consultarEntregaCodigo(String codigo) {
        if (!codigo.equals("") && listEntregas != null) {
            for (Entrega entrega : listEntregas) {
                if (entrega != null && entrega.getCodigo().equals(codigo)) {
                    return entrega;
                }
            }
        }
        return null;
    }

    public Comprobante consultarComprobanteCodigo(String codigoEntrega) {
        if (!codigoEntrega.equals("") && listComprobantes != null) {
            for (Comprobante comprobante : listComprobantes) {
                if (comprobante != null && comprobante.codigoEntrega().equals(codigoEntrega)) {
                    return comprobante;
                }
            }
        }
        return null;
    }

    public LinkedList<String> consultarEtiquetasEntrega(String codigoEntrega) {
        LinkedList<String> etiquetasEntrega = new LinkedList<>();

        Entrega entrega = consultarEntregaCodigo(codigoEntrega);
        if (entrega != null && entrega.getEstado() == EstadoEntrega.ACEPTADA) {
            etiquetasEntrega.addAll(entrega.obtenerEtiquetas());
        }

        return etiquetasEntrega;
    }

    public int calcularValorAcumuladoAportante(String idAportante) {
        int valorAcumulado = 0;

        if (idAportante != null && !idAportante.equals("") && this.listComprobantes != null) {
            for (Comprobante comprobante : this.listComprobantes) {
                if (comprobante != null) {
                    if (idAportante.equals(comprobante.identificacionAportante())) {
                        valorAcumulado += comprobante.valorTotalReconocido();
                    }
                }
            }
        }
        return valorAcumulado;
    }

    
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LinkedList<Aportante> getListAportantes() {
        return this.listAportantes;
    }

    public void setListAportantes(LinkedList<Aportante> listAportantes) {
        this.listAportantes = listAportantes;
    }

    public LinkedList<Entrega> getListEntregas() {
        return this.listEntregas;
    }

    public void setListEntregas(LinkedList<Entrega> listEntregas) {
        this.listEntregas = listEntregas;
    }

    public LinkedList<Comprobante> getListComprobantes() {
        return this.listComprobantes;
    }

    public void setListComprobantes(LinkedList<Comprobante> listComprobantes) {
        this.listComprobantes = listComprobantes;
    }
}