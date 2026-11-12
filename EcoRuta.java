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
        if (codigoNuevo == null || codigoNuevo.trim().isEmpty()) {
            return false;
        }
        if (this.listEntregas != null) {
            for (Entrega entrega : this.listEntregas) {
                if (entrega != null && entrega.getCodigo().equalsIgnoreCase(codigoNuevo.trim())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean registrarAportante(String identificacion, String nombre, String correo) {
        if (identificacion == null || identificacion.trim().isEmpty() ||
            nombre == null || nombre.trim().isEmpty() ||
            correo == null || correo.trim().isEmpty()) {
            return false;
        }

        if (consultarAportanteId(identificacion.trim()) != null) {
            return false;
        }

        Aportante nuevoAportante = new Aportante(identificacion.trim(), nombre.trim(), correo.trim());
        return this.listAportantes.add(nuevoAportante);
    }

    public boolean registrarEntrega(String codigo, LocalDate fecha, Aportante aportante) {
        if (aportante == null || fecha == null || !validarCodigoEntrega(codigo)) {
            return false;
        }

        // Verifica que el aportante realmente esté registrado en EcoRuta
        Aportante aportanteRegistrado = consultarAportanteId(aportante.identificacion());
        if (aportanteRegistrado == null) {
            return false;
        }

        Entrega nuevaEntrega = new Entrega(codigo.trim(), fecha, aportanteRegistrado);
        return this.listEntregas.add(nuevaEntrega);
    }

    public boolean aceptarEntrega(Entrega entrega) {
        if (entrega != null && this.listEntregas != null && this.listEntregas.contains(entrega)) {
            Comprobante nuevoComprobante = entrega.aceptar();
            if (nuevoComprobante != null) {
                this.listComprobantes.add(nuevoComprobante);
                return true;
            }
        }
        return false;
    }

    public Aportante consultarAportanteId(String id) {
        if (id != null && !id.trim().isEmpty() && this.listAportantes != null) {
            for (Aportante aportante : this.listAportantes) {
                if (aportante != null && aportante.identificacion().equalsIgnoreCase(id.trim())) {
                    return aportante;
                }
            }
        }
        return null;
    }

    public Entrega consultarEntregaCodigo(String codigo) {
        if (codigo != null && !codigo.trim().isEmpty() && this.listEntregas != null) {
            for (Entrega entrega : this.listEntregas) {
                if (entrega != null && entrega.getCodigo().equalsIgnoreCase(codigo.trim())) {
                    return entrega;
                }
            }
        }
        return null;
    }

    public Comprobante consultarComprobanteCodigo(String codigoEntrega) {
        if (codigoEntrega != null && !codigoEntrega.trim().isEmpty() && this.listComprobantes != null) {
            for (Comprobante comprobante : this.listComprobantes) {
                if (comprobante != null && comprobante.codigoEntrega().equalsIgnoreCase(codigoEntrega.trim())) {
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

        if (idAportante != null && !idAportante.trim().isEmpty() && this.listComprobantes != null) {
            for (Comprobante comprobante : this.listComprobantes) {
                if (comprobante != null && comprobante.identificacionAportante().equalsIgnoreCase(idAportante.trim())) {
                    valorAcumulado += comprobante.valorTotalReconocido();
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