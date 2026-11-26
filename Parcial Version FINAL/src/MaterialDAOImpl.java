import java.sql.*;
import java.util.*;

public class MaterialDAOImpl implements MaterialDAO {
    @Override public void guardar(Material m) throws Exception {
        String sql="INSERT INTO materiales(codigo,nombre,tipo,valor_comercial,numero_componentes) VALUES(?,?,?,?,?)";
        try(Connection c=Conexion.conectar(); PreparedStatement p=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            p.setString(1,m.getCodigo()); p.setString(2,m.getNombre()); p.setString(3,m.getTipo());
            if(m.getValorComercialBD()==null)p.setNull(4,Types.DECIMAL); else p.setDouble(4,m.getValorComercialBD());
            if(m.getNumeroComponentesBD()==null)p.setNull(5,Types.INTEGER); else p.setInt(5,m.getNumeroComponentesBD());
            p.executeUpdate();
            try(ResultSet r=p.getGeneratedKeys()){if(r.next())m.setIdMaterial(r.getInt(1));}
        } catch(SQLException e){throw new Exception("No se pudo guardar el material: "+e.getMessage(),e);}
    }
    @Override public List<Material> listar() throws Exception {
        List<Material> l=new ArrayList<>();
        String sql="SELECT * FROM materiales ORDER BY codigo";
        try(Connection c=Conexion.conectar();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            while(r.next())l.add(MaterialMapper.reconstruir(r));
        }catch(SQLException e){throw new Exception("No se pudieron listar los materiales: "+e.getMessage(),e);}
        return l;
    }
    @Override public Material buscarPorCodigo(String codigo) throws Exception {
        String sql="SELECT * FROM materiales WHERE codigo=?";
        try(Connection c=Conexion.conectar();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,codigo);
            try(ResultSet r=p.executeQuery()){if(r.next())return MaterialMapper.reconstruir(r);}
        }catch(SQLException e){throw new Exception("No se pudo buscar el material: "+e.getMessage(),e);}
        return null;
    }
    @Override public void actualizar(Material m) throws Exception {
        String sql="UPDATE materiales SET codigo=?,nombre=?,valor_comercial=?,numero_componentes=? WHERE id_material=?";
        try(Connection c=Conexion.conectar();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,m.getCodigo());p.setString(2,m.getNombre());
            if(m.getValorComercialBD()==null)p.setNull(3,Types.DECIMAL);else p.setDouble(3,m.getValorComercialBD());
            if(m.getNumeroComponentesBD()==null)p.setNull(4,Types.INTEGER);else p.setInt(4,m.getNumeroComponentesBD());
            p.setInt(5,m.getIdMaterial());p.executeUpdate();
        }catch(SQLException e){throw new Exception("No se pudo actualizar el material: "+e.getMessage(),e);}
    }
    @Override public void eliminar(int id) throws Exception {
        try(Connection c=Conexion.conectar();PreparedStatement p=c.prepareStatement("DELETE FROM materiales WHERE id_material=?")){
            p.setInt(1,id);p.executeUpdate();
        }catch(SQLException e){throw new Exception("No se pudo eliminar el material: "+e.getMessage(),e);}
    }
}
