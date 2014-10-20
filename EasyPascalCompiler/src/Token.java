
public class Token {
	private String name;
	private String content;
	
	public Token(String name, String content){
		this.name = name;
		this.content = content;
	}

	public String toString(){
		return "(" + name + "," + content + ")";
	}
	
}
