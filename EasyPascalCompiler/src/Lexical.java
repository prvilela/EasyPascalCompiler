import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Lexical {
	int state = 0;
	Set<String> setOfReservedSymbols;
	Set<String> setOfDoubleSymbols;
	Map<String,Token> mapOfReservedWords;
	List<Token> listOfTokens;
	String word="";

	public Lexical(){
		// Set of special symbols
		setOfReservedSymbols = new TreeSet<String>();
		setOfReservedSymbols.addAll(Arrays.asList(";",":","=","<",">","+","-","*","/","(",")","."));
		setOfDoubleSymbols = new TreeSet<String>();
		setOfDoubleSymbols.addAll(Arrays.asList(":=","<>","<=",">="));

		// Map of reserved words
		mapOfReservedWords = new HashMap<String,Token>();
		mapOfReservedWords.put("program", new Token("PROGRAM","program"));
		mapOfReservedWords.put("var", new Token("VAR","var"));
		mapOfReservedWords.put("begin", new Token("BEGIN","begin"));
		mapOfReservedWords.put("end", new Token("END","end"));
		mapOfReservedWords.put("while", new Token("WHILE","while"));
		mapOfReservedWords.put("do", new Token("DO","do"));
		mapOfReservedWords.put("if", new Token("IF","if"));
		mapOfReservedWords.put("then", new Token("THEN","then"));
		mapOfReservedWords.put("else", new Token("ELSE","else"));
		mapOfReservedWords.put("or", new Token("OR","or"));
		mapOfReservedWords.put("and", new Token("AND","and"));
		mapOfReservedWords.put("not", new Token("NOT","not"));
		mapOfReservedWords.put("integer", new Token("INTEGER","integer"));

		listOfTokens = new ArrayList<Token>();
	}

	private void processChar(char c){
		switch (state){
		case 0:
			if(Character.isLetter(c)){
				state = 1;
				word = word + String.valueOf(c);
			}else{
				if(Character.isDigit(c)){
					state = 2;
					word = word + String.valueOf(c);					
				}else{
					if(setOfReservedSymbols.contains(String.valueOf(c))){
						state = 3;
						word = word + String.valueOf(c);											
					}
				}
			}
			break;
		case 1:			
			if(Character.isLetter(c)||Character.isDigit(c)){
				word = word + String.valueOf(c);				
			}else{
				state = 0;
				Token token = mapOfReservedWords.get(word.toLowerCase());				
				if(token!=null){
					listOfTokens.add(token);
				}else{
					listOfTokens.add(new Token("NAME",word));
				}
				word = "";
				if(setOfReservedSymbols.contains(String.valueOf(c))){
					state = 3;
					word = word + String.valueOf(c);											
				}
			}
			break;
		case 2:
			if(Character.isDigit(c)){
				word = word + String.valueOf(c);				
			}else{
				state = 0;
				listOfTokens.add(new Token("NUMBER",word));
				word = "";
			}
			if(setOfReservedSymbols.contains(String.valueOf(c))){
				state = 3;
				word = word + String.valueOf(c);											
			}
			break;
		case 3:
			if(setOfReservedSymbols.contains(String.valueOf(c))){
				word = word + String.valueOf(c);
				if(setOfDoubleSymbols.contains(String.valueOf(word))){
					listOfTokens.add(new Token(word,word));
				}else{
					// TODO: Erro, token n‹o identificado.
				}
			}else{
				listOfTokens.add(new Token(word,word));
			}
			word = "";
			state = 0;
			break;
		}
	}

	public List<Token> processFile(File file){
		try {
			InputStream in = new FileInputStream(file);
			Reader reader = new InputStreamReader(in);

			int c;
			while ((c = reader.read()) != -1) {
				processChar((char) c);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listOfTokens;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(Token token:(new Lexical()).processFile(new File("res/exemplo.pas"))){
			System.out.println(token);
		}
	}

}
