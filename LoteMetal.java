public class LoteMetal extends Lote implements IEtiquetable {

    private TipoMetal tipoMetal;

    public LoteMetal(String codigo, String descripcion, int pesoKg, TipoMetal tipoMetal) {
        super(codigo, descripcion, pesoKg);
        this.tipoMetal = tipoMetal;
    }

    @Override
    public int calcularValor() {
        if (this.tipoMetal == TipoMetal.ALUMINIO) {
            return this.pesoKg * 2200;
        } else if (this.tipoMetal == TipoMetal.ACERO) {
            return this.pesoKg * 900;
        }
        return 0;
    }

    @Override
    public String generarEtiqueta() {
        return "Lote Metal [Código: " + this.codigo + " | Tipo: " + this.tipoMetal + "]";
    }

    public TipoMetal getTipoMetal() {
        return tipoMetal;
    }

    public void setTipoMetal(TipoMetal tipoMetal) {
        this.tipoMetal = tipoMetal;
    }
}