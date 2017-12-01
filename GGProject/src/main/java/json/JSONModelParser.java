package json;

import models.Actor;
import models.MovieCredit;
import models.SimpleActor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONModelParser {
    public static ArrayList<SimpleActor> parseToSimpleActor(JSONArray json) {
        ArrayList<SimpleActor> actors = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonobject = json.getJSONObject(i);
            SimpleActor actor = new SimpleActor();
            actor.setName(jsonobject.getString("name"));
            actor.setImdb_id(jsonobject.getString("imdb"));
            actor.setPos(jsonobject.getInt("left"), jsonobject.getInt("top"),
                    jsonobject.getInt("right"), jsonobject.getInt("bottom"));
            actors.add(actor);
        }
        return actors;
    }

    public static ArrayList<Actor> parseToActor(JSONArray json) {
        ArrayList<Actor> actors = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonobject = json.getJSONObject(i);
            Actor actor = new Actor();
            actor.setName(jsonobject.getString("name"));
            actor.setBirthday(jsonobject.getString("birthday"));
            actor.setDeathday(jsonobject.getString("deathday"));
            actor.setBiography(jsonobject.getString("biography"));
            actor.setGender(jsonobject.getString("gender"));

            JSONArray imagesjson = (JSONArray) jsonobject.get("images");
            ArrayList<String> images = new ArrayList<>();
            for(int j = 0; j < imagesjson.length(); j++) {
                images.add(imagesjson.getString(j));
            }
            actor.setImages(images);
            JSONArray moviesjson = (JSONArray) jsonobject.get("movie_credits");
            actor.setMovie_credits(parseToMovieCredit(moviesjson));
            actors.add(actor);
        }
        return actors;
    }

    private static ArrayList<MovieCredit> parseToMovieCredit(JSONArray json) {
        ArrayList<MovieCredit> movies = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonobject = json.getJSONObject(i);
            MovieCredit movie = new MovieCredit();
            movie.setTitle(jsonobject.getString("title"));
            movie.setVote_average(jsonobject.getFloat("vote_average"));
            movie.setPoster(jsonobject.getString("poster"));

            JSONArray genresjson = (JSONArray) jsonobject.get("genres");
            ArrayList<String> genres = new ArrayList<>();
            for(int j = 0; j < genresjson.length(); j++) {
                genres.add(genresjson.getString(j));
            }
            movie.setGenres(genres);
            movies.add(movie);
        }
        return movies;
    }
}
