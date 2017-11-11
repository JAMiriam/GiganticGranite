package json;

//JSON contains
//		array of Actor:
//		name: string
//		biography: string
//		birthday: string
//		deathday: string or null
//		gender: string
//		imdb_profile: string
//		images: array of string
//		movie_credits: array of MovieCredit
//		title: string
//		genres: array of string
//		vote_average: number
//		poster: string

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
