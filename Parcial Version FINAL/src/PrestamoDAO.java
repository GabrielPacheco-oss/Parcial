import java.time.LocalDate;
import java.util.List;
public interface PrestamoDAO {
    void guardar(Prestamo p) throws Exception;
    List<Prestamo> listar() throws Exception;
    void actualizar(Prestamo p) throws Exception;
    void eliminar(int id) throws Exception;
    boolean materialDisponible(int idMaterial) throws Exception;
    List<PrestamoVencido> consultarVencidos(LocalDate fecha) throws Exception;
    List<MultaPorTipo> consultarMultasPorTipo() throws Exception;
}
