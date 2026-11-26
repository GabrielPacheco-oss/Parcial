import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FormularioPrestamo extends JDialog {
    private final MaterialDAO materialDAO=new MaterialDAOImpl();
    private final PrestamoDAO prestamoDAO=new PrestamoDAOImpl();
    private JComboBox<String> tipo=new JComboBox<>(new String[]{"LIBRO","PORTATIL","KIT"});
    private JTextField codigo=new JTextField(),nombre=new JTextField(),documento=new JTextField(),
        inicio=new JTextField(LocalDate.now().toString()),limite=new JTextField(LocalDate.now().plusDays(7).toString()),
        valor=new JTextField(),componentes=new JTextField();
    private JLabel valorL=new JLabel("Valor comercial:"), componentesL=new JLabel("Número de componentes:");

    public FormularioPrestamo(JFrame padre) {
        super(padre,"Registrar préstamo",true);
        setSize(500,430);setLocationRelativeTo(padre);
        JPanel f=new JPanel(new GridLayout(0,2,8,8));f.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        f.add(new JLabel("Tipo:"));f.add(tipo);
        f.add(new JLabel("Código:"));f.add(codigo);
        f.add(new JLabel("Nombre:"));f.add(nombre);
        f.add(new JLabel("Documento:"));f.add(documento);
        f.add(new JLabel("Fecha préstamo (AAAA-MM-DD):"));f.add(inicio);
        f.add(new JLabel("Fecha límite (AAAA-MM-DD):"));f.add(limite);
        f.add(valorL);f.add(valor);f.add(componentesL);f.add(componentes);
        JButton guardar=new JButton("Guardar"), cancelar=new JButton("Cancelar");
        JPanel botones=new JPanel();botones.add(guardar);botones.add(cancelar);
        add(f,BorderLayout.CENTER);add(botones,BorderLayout.SOUTH);
        tipo.addActionListener(e->camposTipo());guardar.addActionListener(e->guardar());cancelar.addActionListener(e->dispose());
        camposTipo();
    }
    private void camposTipo(){
        boolean p="PORTATIL".equals(tipo.getSelectedItem()), k="KIT".equals(tipo.getSelectedItem());
        valorL.setVisible(p);valor.setVisible(p);componentesL.setVisible(k);componentes.setVisible(k);revalidate();repaint();
    }
    private void guardar(){
        try{
            String cod=codigo.getText().trim(),nom=nombre.getText().trim(),doc=documento.getText().trim();
            if(cod.isEmpty()||nom.isEmpty()||doc.isEmpty())throw new IllegalArgumentException("Completa código, nombre y documento.");
            LocalDate fi=LocalDate.parse(inicio.getText().trim()),fl=LocalDate.parse(limite.getText().trim());
            if(!fl.isAfter(fi))throw new DevolucionInvalidaException("La fecha límite debe ser posterior al préstamo.");
            long dias=ChronoUnit.DAYS.between(fi,fl);
            String t=(String)tipo.getSelectedItem();
            int max="LIBRO".equals(t)?15:"PORTATIL".equals(t)?3:7;
            if(dias>max)throw new PlazoExcedidoException("El plazo máximo para "+t+" es de "+max+" días.");
            Material m=materialDAO.buscarPorCodigo(cod);
            if(m==null){
                if("LIBRO".equals(t))m=new Libro(0,cod,nom);
                else if("PORTATIL".equals(t))m=new Portatil(0,cod,nom,Double.parseDouble(valor.getText().trim()));
                else m=new Kit(0,cod,nom,Integer.parseInt(componentes.getText().trim()));
                materialDAO.guardar(m);
            } else {
                if(!m.getTipo().equals(t))throw new IllegalArgumentException("El código ya existe con otro tipo de material.");
                if(!m.getNombre().equals(nom))throw new IllegalArgumentException("El código ya existe con otro nombre.");
            }
            if(!prestamoDAO.materialDisponible(m.getIdMaterial()))throw new MaterialNoDisponibleException("Este material tiene un préstamo pendiente.");
            Prestamo p=new Prestamo(0,m,doc,fi,fl,null,null,"NORMAL",m.calcularSeguro(),0,0,false);
            prestamoDAO.guardar(p);
            JOptionPane.showMessageDialog(this,"Préstamo registrado. Seguro: $"+String.format("%.0f",p.getSeguro()));
            dispose();
        }catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"No se pudo registrar",JOptionPane.ERROR_MESSAGE);}
    }
}
