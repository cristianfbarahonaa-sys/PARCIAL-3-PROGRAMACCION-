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

        
        if (this.estado != EstadoEntrega.ABIERTA) {
            return false;
        }

       
        if (lote.getCodigo() == null || lote.getCodigo().trim().isEmpty() || lote.getPesoKg() <= 0) {
            return false;
        }

        
        if (existeCodigoLote(lote.getCodigo())) {
            return false;
        }

        listLotes.add(lote);
        return true;
    }

    
    public boolean existeCodigoLote(String codigoLote) {
        if (codigoLote == null) {
            return false;
        }
        for (Lote l : listLotes) {
            if (l.getCodigo().equalsIgnoreCase(codigoLote.trim())) {
                return true;
            }
        }
        return false;
    }

   
    public int calcularPesoTotal() {
        int pesoTotal = 0;
        for (Lote l : listLotes) {
            pesoTotal += l.getPesoKg();
        }
        return pesoTotal;
    }

   
    public int calcularValorTotal() {
        int valorTotal = 0;
        for (Lote l : listLotes) {
            valorTotal += l.calcularValor();
        }
        return valorTotal;
    }

    
    public Comprobante aceptar() {
        if (this.estado != EstadoEntrega.ABIERTA || this.listLotes.isEmpty()) {
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
        for (Lote l : listLotes) {
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
        return listLotes;
    }

    public Comprobante getTheComprobante() {
        return theComprobante;
    }
}