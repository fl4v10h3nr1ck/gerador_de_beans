package principal;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;




public class Form extends JDialog{

	
	
// atencao mudar o tipo de aspa simples para modo ANSI	
	

private static final long serialVersionUID = 1L;



private JTextField pacote;
private JTextField path_anotacoes;

private JTextField path_arq_sql;
private JTextField path_destino;





	public Form(){
		
	super();
		
	this.setTitle("Gerador de Javabeans a partir de ScriptSQL");
	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	this.setSize(500 , 340);
	this.setLocationRelativeTo(null);
	this.setLayout(new GridBagLayout());
	this.setModal(true);
	this.getContentPane().setBackground(new Color(251, 243, 179));  

	this.addComponentes();
	}
	
	
	
	
	
	
	private void addComponentes(){
		
	GridBagConstraints cons = new GridBagConstraints();  

	cons.fill = GridBagConstraints.HORIZONTAL;
	cons.weighty  = 0;
	
	cons.gridwidth  = GridBagConstraints.REMAINDER;
	cons.insets = new Insets( 10, 2, 20, 2);
	this.add(new JLabel("<html><b>Atenção: O arquivo SQL deve está no modo ANSI.</b></html>"), cons);
	
	
	
	cons.gridwidth  = 1;
	cons.weightx  = 0.1;
	cons.insets = new Insets( 2, 2, 0, 2);
	this.add(new JLabel("<html><b>package </b></html>"), cons);
	
	cons.weightx  = 0.9;
	cons.gridwidth  = GridBagConstraints.REMAINDER;
	cons.insets = new Insets( 2, 2, 2, 2);
	this.add(this.pacote = new JTextField(), cons);
	
	
	cons.gridwidth  = 1;
	cons.weightx  = 0.1;
	cons.insets = new Insets( 2, 2, 0, 2);
	this.add(new JLabel("<html><b>import </b></html>"), cons);
	
	cons.weightx  = 0.8;
	cons.insets = new Insets( 2, 2, 2, 2);
	this.add(this.path_anotacoes = new JTextField(), cons);
	
	cons.gridwidth  = GridBagConstraints.REMAINDER;
	cons.weightx  = 0.1;
	cons.insets = new Insets( 2, 2, 0, 2);
	this.add(new JLabel("<html><b>.Anot_BD_Campo</b></html>"), cons);
	
	cons.gridwidth  = 1;
	cons.weightx  = 0.1;
	cons.insets = new Insets( 2, 2, 0, 2);
	this.add(new JLabel(""), cons);
	
	cons.weightx  = 0.8;
	this.add(new JLabel(""), cons);
	
	cons.gridwidth  = GridBagConstraints.REMAINDER;
	cons.weightx  = 0.1;
	this.add(new JLabel("<html><b>.Anot_BD_Tabela</b></html>"), cons);
	

	cons.insets = new Insets(20, 2, 0, 2);
	this.add(new JLabel("<html>Selecione o Arquivo SQL:<font color=red>*</font></html>"), cons);
	
	cons.insets = new Insets(2, 2, 2, 2);
	this.add(this.path_arq_sql = new JTextField(), cons);
	this.path_arq_sql.setEditable(false);
	
	
	cons.insets = new Insets(20, 2, 0, 2);
	this.add(new JLabel("<html>Destino (onde os Javabeans serão criados):<font color=red>*</font></html>"), cons);
	
	cons.insets = new Insets(2, 2, 2, 2);
	this.add(this.path_destino = new JTextField(), cons);
	this.path_destino.setEditable(false);
	
	
	
	
	
	
	
	cons.fill = GridBagConstraints.NONE;
	cons.anchor = GridBagConstraints.CENTER;
	cons.ipadx = 25;
	cons.weightx = 0;
	cons.insets = new Insets( 30, 2, 0, 2);
	JButton bt_gerar  = new JButton("Gerar JavaBeans");
	bt_gerar.setToolTipText("Gerar JavaBeans");
	this.add(bt_gerar, cons);
				
	
	
	
	
	
		this.path_arq_sql.addFocusListener( new FocusAdapter(){	
		@Override 
		public void focusGained(FocusEvent e) {
			
		bt_gerar.requestFocus();
			
		selecionarArquivoSQL();
		}});

	

		this.path_destino.addFocusListener( new FocusAdapter(){	
		@Override 
		public void focusGained(FocusEvent e) {
				
		bt_gerar.requestFocus();
				
		selecionarDestino();
		}});
		
		
		
	
		bt_gerar.addActionListener( new ActionListener(){
		@Override
		public void actionPerformed( ActionEvent event ){
		
		if(gerar())
		JOptionPane.showMessageDialog(null, "Javabeans gerados com sucesso.", "SUCESSO", JOptionPane.INFORMATION_MESSAGE);	
		
		}});		
	}
	
	
	
	
	
	

	private void selecionarArquivoSQL(){
		
	JFileChooser fc = new JFileChooser();
	fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		fc.setFileFilter(new javax.swing.filechooser.FileFilter(){
		      
			public boolean accept(File f){
		    
			if (f.isDirectory()) {return true;}	
				
			return f.getName().toLowerCase().endsWith(".sql");
		    }

		    public String getDescription() {
		    return "Scripts SQL (.sql)";
		    }
		});
		
	int returnVal = fc.showOpenDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION)
	this.path_arq_sql.setText(fc.getSelectedFile().getAbsolutePath());
	}
	
	
	
	
	
	
	

	private void selecionarDestino(){
		
	JFileChooser fc = new JFileChooser();
	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		fc.setFileFilter(new javax.swing.filechooser.FileFilter(){
		      
			public boolean accept(File f){
		    
			if (f.isDirectory()) {return true;}	
				
			return false;
		    }

		    public String getDescription() {
		    return "Apenas Diretórios";
		    }
		});
		
	int returnVal = fc.showOpenDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION)
	this.path_destino.setText(fc.getSelectedFile().getAbsolutePath());
	}
	
	
	
	
	
	
	
	
	
	private boolean gerar(){
		
		if(this.pacote.getText().length()==0){
			
		JOptionPane.showMessageDialog(null, "Informe o pacote dos Javabeans.", "ERRO", JOptionPane.ERROR_MESSAGE);		
		return false;
		}
		
		
		if(this.path_anotacoes.getText().length()==0){
			
		JOptionPane.showMessageDialog(null, "Informe o import para os arquivos de anotações.", "ERRO", JOptionPane.ERROR_MESSAGE);		
		return false;
		}
		
		
		if(this.path_arq_sql.getText().length()==0){
			
		JOptionPane.showMessageDialog(null, "Selecione o arquivo SQL.", "ERRO", JOptionPane.ERROR_MESSAGE);		
		return false;
		}
	
		
		if(this.path_destino.getText().length()==0){
			
		JOptionPane.showMessageDialog(null, "Selecione o destino para os Javabeans.", "ERRO", JOptionPane.ERROR_MESSAGE);		
		return false;
		}
		
	Gerador gerador = new Gerador(this.pacote.getText(), this.path_anotacoes.getText());	
	
	File arquivo = new File(this.path_arq_sql.getText());
		
	gerador.gerar(arquivo, this.path_destino.getText());	
		
	return true;
	}
	
	
	
	
	
	
	
}

