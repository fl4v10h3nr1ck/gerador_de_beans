package principal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;



public class Gerador {

	
	
private String sql;	
	
private List<String> classes;	
private List<String> nomes_de_classes;


private String pacote_classe;
private String pacote_anotacoes;


private String path_destino;



	public Gerador(String pacote, String pacote_anotacoes){
		
	this.sql = "";	
	this.classes = new ArrayList<String>();
	this.nomes_de_classes = new ArrayList<String>();
	
	this.pacote_classe  =pacote;
	this.pacote_anotacoes = pacote_anotacoes;
	}



	
	
	
	
	public void gerar(File arquivo, String destino){
	
	this.path_destino = destino;	
		
		try{
		
		FileReader arq = new FileReader(arquivo);
		BufferedReader lerArq = new BufferedReader(arq);
	
		String linha = lerArq.readLine();
		     
			while (linha != null) {
	
			this.sql+=linha+"\n";
			linha = lerArq.readLine(); 
			}

		arq.close();
		} 
		catch (IOException e) {
	    	
		JOptionPane.showMessageDialog(null,"Um erro ocorreu ao abrir o arquivo SQL.", "ERRO", JOptionPane.ERROR_MESSAGE);	
		return;
		}
	
	this.gerar();
	
	
		if(this.classes!=null && this.classes.size()>0){
			
			try {	
				
				for(String classe: this.classes){
				
				FileWriter arq = new FileWriter(this.path_destino+"\\"+this.nomes_de_classes.get(this.classes.indexOf(classe))+".java");
				
				PrintWriter gravarArq = new PrintWriter(arq);
		
				gravarArq.printf(classe);
				
				arq.close();
				} 
			}
			catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Um erro ocorreu ao gerar uma das classes.", "ERRO", JOptionPane.ERROR_MESSAGE);	
			return;
			}
	
		}
	}
	
	
	
	
	
	
	private void gerar(){
		
		if(this.sql.length()>0){
		
		String[] linhas= this.sql.split(";");	

			for(String linha: linhas){
			
			String imports  = 
						"package "+this.pacote_classe+";\n\n"+
						"import "+this.pacote_anotacoes+".Anot_BD_Campo;\n"+
						"import "+this.pacote_anotacoes+".Anot_BD_Tabela;\n\n";	
			
			
			String classe = "";
				
			linha = linha.replace("\n", "");	
				
			if(linha==null || 
					linha.length()==0 || 
						this.ehComentario(linha) ||
							!this.ehTabela(linha))
			continue;
			
			String nome_tabela = this.getNomeDeTabela(linha);
			
			
				if(nome_tabela==null){
				
				JOptionPane.showMessageDialog(null,"Tabela sem nome.", "ERRO", JOptionPane.ERROR_MESSAGE);	
				return;
				}
			
			String nome_classe = this.getNomeDeClasse(nome_tabela);
					
			
			String prefixo_tabela = this.getPrefixoDeClasse(nome_tabela);
					
			
			classe +="@Anot_BD_Tabela(nome=\""+nome_tabela+"\", prefixo=\""+prefixo_tabela+"\")\n"+"public class "+nome_classe+" {\n\n";
			
			
			List<String[]> campos = this.getCampos(linha);
			
			boolean tem_data = false;
			
				if(campos!=null){
				
					for(String[] campo: campos){
					classe +=campo[2]+"\n"+"private "+campo[0]+" "+campo[1]+";\n\n";
					
					if(campo[0].compareTo("Date")==0)
					tem_data  =  true;
					}
				
				classe += "\n\n";
				
				for(String[] campo: campos)
				classe += this.getMetodos(campo);
				}
			
	
			classe +="\n\n\n}";
			
			
			if(tem_data)
			imports	 += "import java.text.SimpleDateFormat;\nimport java.util.Date;";
			
			this.classes.add(imports+"\n\n\n"+classe);
			this.nomes_de_classes.add(nome_classe);
			}
		}	
	}
	
	
	
	
	

	private boolean ehComentario(String linha){
		
	if(linha.length()<2)
	return false;
	
	return linha.charAt(0)=='-' && linha.charAt(1)=='-'?true:false;
	}
	
	
	

	

	private boolean ehTabela(String linha){
		
	return linha.contains("CREATE TABLE IF NOT EXISTS");
	}
	
	
	
	
	

	
	private String getNomeDeTabela(String linha){
	
	linha = linha.replace("(", "###");
		
	String aux[] = linha.replace("CREATE TABLE IF NOT EXISTS", "").split("###");	
	
		if(aux!=null && aux.length>0){
		
		aux[0] = aux[0].replace(" ", "").replace("'", "").replace(".", "###");	
		
		aux = aux[0].split("###");	

		if(aux==null || aux.length==0)
		return null;
		
		return aux.length==1?aux[0]:aux[1];
		}
	
	return null;
	}
	
	
	
	
	
	
	private String getNomeDeClasse(String nome_tabela){
		
	String aux[]  = nome_tabela.split("_");
		
	String nome ="";
		for(int i= 0; i < aux.length; i++){
					
		if(aux[i]!=null && aux[i].length()>0)
		nome += (""+aux[i].charAt(0)).toUpperCase()+(aux[i].length()>1?aux[i].substring(1):"")+"_";			
		}
			
	return nome.length()>0?nome.substring(0, nome.length()-1):"";
	}
		
		
	
	
	
	
	private String getPrefixoDeClasse(String nome_classe){
		
	String prefixo ="";
	
	String[] aux = nome_classe.split("_");
	
		for(int i= 0; i < aux.length; i++){
					
		if(aux[i]==null && aux[i].length()==0)
		continue;
				
		if(aux[i].length()>=3)
		prefixo += aux[i].substring(0, 3).toLowerCase();
		else
		prefixo += aux[i].substring(0, 1);	
		}
	
	return prefixo;
	}
		
	
	
	
	
	
	
	private List<String[]> getCampos(String linha){
	
	String aux[] = linha.split(",");	
	List<String[]> campos = new ArrayList<String[]>();
	String linha_aux = "";
	
	String aux_[] = null;	
	
		if(aux!=null){
	
			if(aux.length>0){	
		
			linha_aux = aux[0].replace("(", "###");
			
			aux_ = linha_aux.split("###");	
				
				if(aux_!=null && aux_.length>1){
				
				aux_ = this.extraiCampo(aux_[1]);
				
				if(aux_!=null)
				campos.add(aux_);	
				}
			
				if(aux.length>1){
					
					for(int i = 1; i < aux.length; i++){
				
					aux_ = this.extraiCampo(aux[i]);
					
					if(aux_!=null)
					campos.add(aux_);
					}
				}
			}
		}
	
	return  campos;		
	}
	
	
	
	
	
	
	private String[] extraiCampo(String item){
	
	if(item==null || item.length()==0 || item.contains("PRIMARY KEY"))
	return null;
			
	String aux[] = item.split("'");	
	
	String[] dados = new String[4];
	
	dados[0]= "indefinido";
	dados[1]= "indefinido";
	dados[2]= "";
	dados[3] = "Indefinido";
	
	if(aux!=null && aux.length>2)
	dados[1] = aux[1];
		else{
		
		aux = item.split(" ");	
		
		if(aux!=null && aux.length>1)
		dados[1] = aux[0];
		}
	
	
	if(item.contains(" INT ") || 
	   item.contains(" SMALLINT ") ||
	   item.contains(" TINYINT ") ||
	   item.contains(" MEDIUMINT ") ||
	   item.contains(" INTEGER "))
	dados[0] = "int";
	
	else if(item.contains(" BIGINT ") || 
			item.contains(" BIGINT "))	
	dados[0] = "long";
	
	else if(item.contains(" FLOAT(") || 
			item.contains(" FLOAT ") ||
			item.contains(" DOUBLE ") ||
			item.contains(" REAL "))	
	dados[0] = "double";
		
	else if(item.contains(" DATE ") ||
			item.contains(" DATETIME ") ||
			item.contains(" TIMESTAMP ") )	
	dados[0] = "Date";
	
	else
	dados[0] = "String";
	
	
	
	dados[3] = (dados[1].toUpperCase().charAt(0))+dados[1].substring(1);
	
	
	dados[2] = "@Anot_BD_Campo(nome = \""+dados[1]+"\", ";
	
	if(dados[0].compareTo("int")==0 || dados[0].compareTo("long")==0)
	dados[2] += "tipo=int.class, ";
	
	if(dados[0].compareTo("double")==0)
	dados[2] += "tipo=double.class, ";
	
	if(dados[0].compareTo("Date")==0)
	dados[2] += "tipo=Date.class, getBD = \"get"+dados[3]+"BD\", getTab = \"get"+dados[3]+"String\", ";
	
	dados[2] += "set = \"set"+dados[3]+"\", get = \"get"+dados[3]+"\"";
	
	
	if(item.contains("AUTO_INCREMENT"))
	dados[2] += ", ehId=true)";	
	else
	dados[2] += ")";	
	
	return dados;	
	}
	
	
	
	
	
	private String getMetodos(String[] campo){
		
	String metodos = "";	
		
	if(campo==null)
	return "";
		
	
	metodos = "public void set"+campo[3]+"( "+campo[0]+" "+campo[1]+" ){this."+campo[1]+"="+campo[1]+";}\n";
	metodos += "public "+campo[0]+" get"+campo[3]+"(){return this."+campo[1]+";}\n";
	
	if(campo[0].compareTo("Date")==0)
	metodos += "public "+campo[0]+" get"+campo[3]+"BD(){return this."+campo[1]+" == null? null: new java.sql.Date(this."+campo[1]+".getTime());}\n"
			+  "public String get"+campo[3]+"String(){return this."+campo[1]+" == null?\"\":new SimpleDateFormat(\"dd/MM/yyyy\").format(this."+campo[1]+");}";

	
	
	return metodos+"\n";
	}
	
	
	
}
