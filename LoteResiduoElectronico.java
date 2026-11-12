public class LoteResiduoElectronico extends Lote implements IEtiquetable {

    private int cantidadEquiposCompletos;

    public LoteResiduoElectronico(String codigo, String descripcion, int pesoKg, int cantidadEquiposCompletos) {
        super(codigo, descripcion, pesoKg);
        this.cantidadEquiposCompletos = cantidadEquiposCompletos;
    }

    @Override
    public int calcularValor() {
        return (this.pesoKg * 1400) + (this.cantidadEquiposCompletos * 800);
    }

    @Override
    public String generarEtiqueta() {
        return "Lote Electrónico [Código: " + this.codigo + " | Equipos completos: " + this.cantidadEquiposCompletos + "]";
    }

    public int getCantidadEquiposCompletos() {
        return cantidadEquiposCompletos;
    }

    public void setCantidadEquiposCompletos(int cantidadEquiposCompletos) {
        this.cantidadEquiposCompletos = cantidadEquiposCompletos;
    }
}