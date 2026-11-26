package Parcial.src.Persistencia;

import Parcial.src.MultaPorTipo;
import Parcial.src.Prestamo;
import Parcial.src.PrestamoVencido;
import Parcial.src.Modelo.Material;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAOImpl implements PrestamoDAO {

    @Override
    public void guardar(Prestamo p) throws Exception {

        String sql = "INSERT INTO prestamos "
                + "(id_material, documento, fecha_prestamo, fecha_limite, "
                + "fecha_devolucion, componentes_faltantes, politica_multa, "
                + "seguro, multa_base, multa_final) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getMaterial().getIdMaterial());
            ps.setString(2, p.getDocumento());
            ps.setDate(3, java.sql.Date.valueOf(p.getFechaPrestamo()));
            ps.setDate(4, java.sql.Date.valueOf(p.getFechaLimite()));

            if (p.getFechaDevolucion() != null) {
                ps.setDate(5, java.sql.Date.valueOf(p.getFechaDevolucion()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            if (p.getComponentesFaltantes() != null) {
                ps.setInt(6, p.getComponentesFaltantes());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            ps.setString(7, p.getPoliticaMulta());
            ps.setDouble(8, p.getSeguro());
            ps.setDouble(9, p.getMultaBase());
            ps.setDouble(10, p.getMultaFinal());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al guardar préstamo: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Prestamo> listar() throws Exception {

        List<Prestamo> prestamos = new ArrayList<>();

        String sql = "SELECT p.*, m.id_material, m.codigo, m.nombre, "
                + "m.tipo, m.valor_comercial, m.numero_componentes "
                + "FROM prestamos p "
                + "INNER JOIN materiales m ON p.id_material = m.id_material";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Material material = MaterialMapper.reconstruir(rs);

                java.sql.Date fechaDevolucionSQL =
                        rs.getDate("fecha_devolucion");

                Object valor = rs.getObject("componentes_faltantes");

                Integer componentesFaltantes = valor == null
                        ? null
                        : ((Number) valor).intValue();

                Prestamo prestamo = new Prestamo(
                        rs.getInt("id_prestamo"),
                        material,
                        rs.getString("documento"),
                        rs.getDate("fecha_prestamo").toLocalDate(),
                        rs.getDate("fecha_limite").toLocalDate(),
                        fechaDevolucionSQL == null ? null
                                : fechaDevolucionSQL.toLocalDate(),
                        componentesFaltantes,
                        rs.getString("politica_multa"),
                        rs.getDouble("seguro"),
                        rs.getDouble("multa_base"),
                        rs.getDouble("multa_final")
                );

                prestamos.add(prestamo);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar préstamos: " + e.getMessage());
            throw e;
        }

        return prestamos;
    }

    @Override
    public void actualizar(Prestamo p) throws Exception {

        String sql = "UPDATE prestamos SET documento=?, fecha_prestamo=?, "
                + "fecha_limite=?, fecha_devolucion=?, "
                + "componentes_faltantes=?, politica_multa=?, seguro=?, "
                + "multa_base=?, multa_final=? WHERE id_prestamo=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getDocumento());
            ps.setDate(2, java.sql.Date.valueOf(p.getFechaPrestamo()));
            ps.setDate(3, java.sql.Date.valueOf(p.getFechaLimite()));

            if (p.getFechaDevolucion() != null) {
                ps.setDate(4, java.sql.Date.valueOf(p.getFechaDevolucion()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            if (p.getComponentesFaltantes() != null) {
                ps.setInt(5, p.getComponentesFaltantes());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.setString(6, p.getPoliticaMulta());
            ps.setDouble(7, p.getSeguro());
            ps.setDouble(8, p.getMultaBase());
            ps.setDouble(9, p.getMultaFinal());
            ps.setInt(10, p.getIdPrestamo());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar préstamo: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void eliminar(int idPrestamo) throws Exception {

        String sql = "DELETE FROM prestamos WHERE id_prestamo=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPrestamo);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al eliminar préstamo: " + e.getMessage());
            throw e;
        }
    }
    @Override
    public List<PrestamoVencido> consultarVencidos(
        java.time.LocalDate fechaConsulta) throws Exception {

            List<PrestamoVencido> resultados = new ArrayList<>();

            String sql = "SELECT m.codigo, m.nombre, p.documento, "
                    + "p.fecha_limite, DATEDIFF(?, p.fecha_limite) AS dias_retraso "
                    + "FROM prestamos p "
                    + "INNER JOIN materiales m ON p.id_material = m.id_material "
                    + "WHERE p.fecha_devolucion IS NULL "
                    + "AND p.fecha_limite < ?";

            try (Connection con = Conexion.conectar();
                PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setDate(1, java.sql.Date.valueOf(fechaConsulta));
                ps.setDate(2, java.sql.Date.valueOf(fechaConsulta));

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        PrestamoVencido vencido = new PrestamoVencido(
                                rs.getString("codigo"),
                                rs.getString("nombre"),
                                rs.getString("documento"),
                                rs.getDate("fecha_limite").toLocalDate(),
                                rs.getInt("dias_retraso")
                        );

                        resultados.add(vencido);
                    }
                }

            } catch (SQLException e) {
                System.out.println("Error al consultar préstamos vencidos: "
                        + e.getMessage());
                throw e;
            }

            return resultados;
        }
    @Override
    public List<MultaPorTipo> consultarMultasPorTipo() throws Exception {

        List<MultaPorTipo> resultados = new ArrayList<>();

        String sql = "SELECT m.tipo, SUM(p.multa_final) AS total_multas "
                + "FROM prestamos p "
                + "INNER JOIN materiales m ON p.id_material = m.id_material "
                + "WHERE p.fecha_devolucion IS NOT NULL "
                + "GROUP BY m.tipo";

        try (Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                MultaPorTipo multa = new MultaPorTipo(
                        rs.getString("tipo"),
                        rs.getDouble("total_multas")
                );

                resultados.add(multa);
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar multas: " + e.getMessage());
            throw e;
        }

        return resultados;
    }
}