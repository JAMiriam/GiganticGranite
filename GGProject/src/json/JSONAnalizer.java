package json;

public class JSONAnalizer {
	JSONAnalizer() {

	}

	public static void analyze(String line) {
		for(int i = 0; i < line.length(); i++) {
			if(line.charAt(i) == '[')
				System.out.println();
			System.out.print(line.charAt(i));
		}
	}
}
