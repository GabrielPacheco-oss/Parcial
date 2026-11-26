import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class FormularioEditarPrestamo extends JDialog {
    private final PrestamoDAO dao=new PrestamoDAOImpl();
    private final Prestamo original;
    private JTextField documento,fechaLimite,fechaDevolucion,faltantes;
    private JComboBox<String> politica=new JComboBox<>(new String[]{"NORMAL","DOCENTE","SEMANA_EXAMENES"});
    private JCheckBox pagada=new JCheckBox("Multa pagada");

    public FormularioEditarPrestamo(JFrame padre,Prestamo p){
        super(padre,"Editar / registrar devolución",true);original=p;
        setSize(520,390);setLocationRelativeTo(padre);
        JPanel f=new JPanel(new GridLayout(0,2,8,8));f.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        documento=new JTextField(p.getDocumento());fechaLimite=new JTextField(p.getFechaLimite().toString());
        fechaDevolucion=new JTextField(p.getFechaDevolucion()==null?"":p.getFechaDevolucion().toString());
        faltantes=new JTextField(p.getComponentesFaltantes()==null?"0":p.getComponentesFaltantes().toString());
        politica.setSelectedItem(p.getPoliticaMulta());pagada.setSelected(p.isMultaPagada());
        f.add(new JLabel("Documento:"));f.add(documento);
        f.add(new JLabel("Fecha límite:"));f.add(fechaLimite);
        f.add(new JLabel("Fecha devolución (vacío = pendiente):"));f.add(fechaDevolucion);
        f.add(new JLabel("Componentes faltantes (Kit):"));f.add(faltantes);
        f.add(new JLabel("Política de multa:"));f.add(politica);
        f.add(new JLabel("Estado de cobro:"));f.add(pagada);
        JButton guardar=new JButton("Guardar cambios"),cancelar=new JButton("Cancelar");
        JPanel b=new JPanel();b.add(guardar);b.add(cancelar);add(f,BorderLayout.CENTER);add(b,BorderLayout.SOUTH);
        boolean esKit=p.getMaterial() instanceof Kit;faltantes.setEnabled(esKit);
        guardar.addActionListener(e->guardar());cancelar.addActionListener(e->dispose());
    }
    private void guardar(){
        try{
            String doc=documento.getText().trim();if(doc.isEmpty())throw new IllegalArgumentException("Documento obligatorio.");
            LocalDate limite=LocalDate.parse(fechaLimite.getText().trim());
            String dev=fechaDevolucion.getText().trim();LocalDate devolucion=dev.isEmpty()?null:LocalDate.parse(dev);
            Integer n=null;
            if(original.getMaterial() instanceof Kit)n=Integer.parseInt(faltantes.getText().trim());
            String pol=(String)politica.getSelectedItem();
            Prestamo p=new Prestamo(original.getIdPrestamo(),original.getMaterial(),doc,
                original.getFechaPrestamo(),limite,devolucion,n,pol,original.getSeguro(),
                original.getMultaBase(),original.getMultaFinal(),pagada.isSelected());
            p.calcularMulta(PoliticaMultaFactory.obtener(pol));
            if(p.getMultaFinal()==0) p.setMultaPagada(true);
            dao.actualizar(p);
            JOptionPane.showMessageDialog(this,"Préstamo actualizado. Multa final: $"+String.format("%.0f",p.getMultaFinal()));
            dispose();
        }catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }
}
