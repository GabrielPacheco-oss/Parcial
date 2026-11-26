package Parcial.src.Persistencia;

import Parcial.src.;
import java.time.LocalDate;
import java.util.List;

public interface PrestamoDAO {

    void guardar(Prestamo prestamo) throws Exception;

    List<Prestamo> listar() throws Exception;

    void actualizar(Prestamo prestamo) throws Exception;

    void eliminar(int idPrestamo) throws Exception;

    List<PrestamoVencido> consultarVencidos(LocalDate fechaConsulta)
            throws Exception;

    List<MultaPorTipo> consultarMultasPorTipo()
            throws Exception;
}