import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Entrega {

    private String codigo;
    private LocalDate fecha;
    private EstadoEntrega estado;

    // Relaciones del diagrama UML
    private Aportante theAportante;
    private List<Lote> listLotes;
    private Comprobante theComprobante;

    public Entrega(String codigo, LocalDate fecha, Aportante theAportante) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.theAportante = theAportante;
        this.estado = EstadoEntrega.ABIERTA;
        this.listLotes = new ArrayList<>();
        this.theComprobante = null;
    }

    public boolean agregarLote(Lote lote) {
        if (lote == null) {
            return false;
        }

        // Solo se pueden agregar lotes si la entrega está ABIERTA
        if (this.estado != EstadoEntrega.ABIERTA) {
            return false;
        }

        // Validar código, descripción y peso positivo (> 0)
        if (lote.getCodigo() == null || lote.getCodigo().trim().isEmpty() ||
            lote.getDescripcion() == null || lote.getDescripcion().trim().isEmpty() ||
            lote.getPesoKg() <= 0) {
            return false;
        }

        // Validar reglas específicas de cada subtipo de lote
        if (lote instanceof LoteResiduoElectronico electronico && electronico.getCantidadEquiposCompletos() < 0) {
            return false;
        }

        if (lote instanceof LoteMetal metal && metal.getTipoMetal() == null) {
            return false;
        }

        // Validar que el código del lote no se repita en esta entrega
        if (existeCodigoLote(lote.getCodigo())) {
            return false;
        }

        this.listLotes.add(lote);
        return true;
    }

    public boolean existeCodigoLote(String codigoLote) {
        if (codigoLote == null || codigoLote.trim().isEmpty()) {
            return false;
        }
        for (Lote l : this.listLotes) {
            if (l != null && l.getCodigo().equalsIgnoreCase(codigoLote.trim())) {
                return true;
            }
        }
        return false;
    }

    public int calcularPesoTotal() {
        int pesoTotal = 0;
        for (Lote l : this.listLotes) {
            pesoTotal += l.getPesoKg();
        }
        return pesoTotal;
    }

    public int calcularValorTotal() {
        int valorTotal = 0;
        for (Lote l : this.listLotes) {
            valorTotal += l.calcularValor();
        }
        return valorTotal;
    }

    public Comprobante aceptar() {
        if (this.estado != EstadoEntrega.ABIERTA || this.listLotes.isEmpty() || this.theAportante == null) {
            return null;
        }

        this.estado = EstadoEntrega.ACEPTADA;
        int pesoTotal = calcularPesoTotal();
        int valorTotal = calcularValorTotal();

        this.theComprobante = new Comprobante(
                this.codigo,
                this.theAportante.identificacion(),
                pesoTotal,
                valorTotal
        );

        return this.theComprobante;
    }

    public List<String> obtenerEtiquetas() {
        List<String> etiquetas = new ArrayList<>();
        for (Lote l : this.listLotes) {
            if (l instanceof IEtiquetable etiquetable) {
                etiquetas.add(etiquetable.generarEtiqueta());
            }
        }
        return etiquetas;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public EstadoEntrega getEstado() {
        return estado;
    }

    public Aportante getTheAportante() {
        return theAportante;
    }

    public List<Lote> getListLotes() {
        return new ArrayList<>(listLotes);
    }

    public Comprobante getTheComprobante() {
        return theComprobante;
    }
}