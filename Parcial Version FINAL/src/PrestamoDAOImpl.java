import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class PrestamoDAOImpl implements PrestamoDAO {
    @Override public void guardar(Prestamo p) throws Exception {
        String sql="INSERT INTO prestamos(id_material,documento,fecha_prestamo,fecha_limite,fecha_devolucion,componentes_faltantes,politica_multa,seguro,multa_base,multa_final,multa_pagada) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try(Connection c=Conexion.conectar();PreparedStatement s=c.prepareStatement(sql)){
            s.setInt(1,p.getMaterial().getIdMaterial());s.setString(2,p.getDocumento());
            s.setDate(3,java.sql.Date.valueOf(p.getFechaPrestamo()));s.setDate(4,java.sql.Date.valueOf(p.getFechaLimite()));
            if(p.getFechaDevolucion()==null)s.setNull(5,Types.DATE);else s.setDate(5,java.sql.Date.valueOf(p.getFechaDevolucion()));
            if(p.getComponentesFaltantes()==null)s.setNull(6,Types.INTEGER);else s.setInt(6,p.getComponentesFaltantes());
            s.setString(7,p.getPoliticaMulta());s.setDouble(8,p.getSeguro());s.setDouble(9,p.getMultaBase());s.setDouble(10,p.getMultaFinal());s.setBoolean(11,p.isMultaPagada());
            s.executeUpdate();
        }catch(SQLException e){throw new Exception("No se pudo guardar el préstamo: "+e.getMessage(),e);}
    }
    @Override public List<Prestamo> listar() throws Exception {
        List<Prestamo> lista=new ArrayList<>();
        String sql="SELECT p.*,m.codigo,m.nombre,m.tipo,m.valor_comercial,m.numero_componentes FROM prestamos p JOIN materiales m ON p.id_material=m.id_material ORDER BY p.id_prestamo DESC";
        try(Connection c=Conexion.conectar();PreparedStatement s=c.prepareStatement(sql);ResultSet r=s.executeQuery()){
            while(r.next()){
                Material m=MaterialMapper.reconstruir(r);
                java.sql.Date dev=r.getDate("fecha_devolucion");
                Object falt=r.getObject("componentes_faltantes");
                Integer n=falt==null?null:((Number)falt).intValue();
                lista.add(new Prestamo(r.getInt("id_prestamo"),m,r.getString("documento"),
                    r.getDate("fecha_prestamo").toLocalDate(),r.getDate("fecha_limite").toLocalDate(),
                    dev==null?null:dev.toLocalDate(),n,r.getString("politica_multa"),
                    r.getDouble("seguro"),r.getDouble("multa_base"),r.getDouble("multa_final"),r.getBoolean("multa_pagada")));
            }
        }catch(SQLException e){throw new Exception("No se pudieron listar los préstamos: "+e.getMessage(),e);}
        return lista;
    }
    @Override public void actualizar(Prestamo p) throws Exception {
        String sql="UPDATE prestamos SET documento=?,fecha_limite=?,fecha_devolucion=?,componentes_faltantes=?,politica_multa=?,seguro=?,multa_base=?,multa_final=?,multa_pagada=? WHERE id_prestamo=?";
        try(Connection c=Conexion.conectar();PreparedStatement s=c.prepareStatement(sql)){
            s.setString(1,p.getDocumento());s.setDate(2,java.sql.Date.valueOf(p.getFechaLimite()));
            if(p.getFechaDevolucion()==null)s.setNull(3,Types.DATE);else s.setDate(3,java.sql.Date.valueOf(p.getFechaDevolucion()));
            if(p.getComponentesFaltantes()==null)s.setNull(4,Types.INTEGER);else s.setInt(4,p.getComponentesFaltantes());
            s.setString(5,p.getPoliticaMulta());s.setDouble(6,p.getSeguro());s.setDouble(7,p.getMultaBase());s.setDouble(8,p.getMultaFinal());s.setBoolean(9,p.isMultaPagada());s.setInt(10,p.getIdPrestamo());s.executeUpdate();
        }catch(SQLException e){throw new Exception("No se pudo actualizar el préstamo: "+e.getMessage(),e);}
    }
    @Override public void eliminar(int id) throws Exception {
        try(Connection c=Conexion.conectar();PreparedStatement s=c.prepareStatement("DELETE FROM prestamos WHERE id_prestamo=?")){s.setInt(1,id);s.executeUpdate();}
        catch(SQLException e){throw new Exception("No se pudo eliminar el préstamo: "+e.getMessage(),e);}
    }
    @Override public boolean materialDisponible(int id) throws Exception {
        String sql="SELECT COUNT(*) FROM prestamos WHERE id_material=? AND fecha_devolucion IS NULL";
        try(Connection c=Conexion.conectar();PreparedStatement s=c.prepareStatement(sql)){s.setInt(1,id);try(ResultSet r=s.executeQuery()){r.next();return r.getInt(1)==0;}}
        catch(SQLException e){throw new Exception("No se pudo verificar la disponibilidad: "+e.getMessage(),e);}
    }
    @Override public List<PrestamoVencido> consultarVencidos(LocalDate fecha) throws Exception {
        List<PrestamoVencido> l=new ArrayList<>();
        String sql="SELECT m.codigo,m.nombre,p.documento,p.fecha_limite,DATEDIFF(?,p.fecha_limite) dias_retraso FROM prestamos p JOIN materiales m ON p.id_material=m.id_material WHERE p.fecha_devolucion IS NULL AND p.fecha_limite<? ORDER BY p.fecha_limite";
        try(Connection c=Conexion.conectar();PreparedStatement s=c.prepareStatement(sql)){
            s.setDate(1,java.sql.Date.valueOf(fecha));s.setDate(2,java.sql.Date.valueOf(fecha));
            try(ResultSet r=s.executeQuery()){while(r.next())l.add(new PrestamoVencido(r.getString("codigo"),r.getString("nombre"),r.getString("documento"),r.getDate("fecha_limite").toLocalDate(),r.getInt("dias_retraso")));}
        }catch(SQLException e){throw new Exception("Error al consultar vencidos: "+e.getMessage(),e);}
        return l;
    }
    @Override public List<MultaPorTipo> consultarMultasPorTipo() throws Exception {
        List<MultaPorTipo> l=new ArrayList<>();
        String sql="SELECT m.tipo,COALESCE(SUM(p.multa_final),0) total_multas FROM prestamos p JOIN materiales m ON p.id_material=m.id_material WHERE p.multa_pagada=TRUE GROUP BY m.tipo ORDER BY m.tipo";
        try(Connection c=Conexion.conectar();PreparedStatement s=c.prepareStatement(sql);ResultSet r=s.executeQuery()){while(r.next())l.add(new MultaPorTipo(r.getString("tipo"),r.getDouble("total_multas")));}
        catch(SQLException e){throw new Exception("Error al consultar multas cobradas: "+e.getMessage(),e);}
        return l;
    }
}
