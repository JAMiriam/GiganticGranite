from flask import Flask, Response, jsonify, request
from actorinfo import ActorsInfoPicker, Actor, MovieCredit
# from werkzeug import secure_filename
from werkzeug.utils import secure_filename
from db_connection import DBConnector
import json
import facerec
prec=facerec.reclass()
app = Flask(__name__)


@app.route('/actors/image', methods=['POST'])
def getActors():
    img = request.files['image']
    
    #prec.prd(img) returns array of vectors which contain:
    #at first position "right" actor was found or "wrong" actor wasn't
    #found at current position
    #at second place is a mega position number of actor (or best guess in case of "wrong")
    #next four are position of face four int left,top,right,bottom
    #at this moment there can be problems with using this function
    found_actors=prec.prd(img)
    
    # 		TODO
    # find actor's name for specific id
    # create JSON with array of id, name, position
    #

    # test connection
    # dicted_actors = dict(status='ok')
    dicted_actors = []
    dicted_actors.append(dict(name='Alan Rickman', imdb='nm0000614'))
    data = json.dumps(dicted_actors)

    return Response(data, mimetype="application/json")


# JSON with details contains
# 	array of Actor:
#		name: string
#		biography: string
#		birthday: string
#		deathday: string or null
#		gender: string
#		imdb_profile: string
#		images: array of string
#		movie_credits: array of MovieCredit
#			title: string
#			genres: array of string
#			vote_average: number
#			poster: string
#	
@app.route('/actordetails/<actor_id>', methods=['GET'])
def getDetails(actor_id):
    picker = ActorsInfoPicker()
    connector = DBConnector()
    actors = []

    tmdb_id = connector.find_actor("nm0000093")['tmdb_id']
    actors.append(picker.download_actor_info(tmdb_id, 'nm0000093'))
    # actors.append(picker.download_actor_info('4566', 'nm0000614'))

    # Preparing JSON with actor details to return
    dicted_actors = []
    for actor in actors:
        dicted_credits = []
        for movie_credit in actor.movie_credits:
            dicted_credits.append(dict(
                title=movie_credit.title,
                genres=movie_credit.genres,
                vote_average=movie_credit.vote_average,
                poster=movie_credit.poster
            ))

        temp = dict(
            name=actor.name,
            biography=actor.biography,
            birthday=actor.birthday,
            deathday=actor.deathday,
            gender=actor.gender,
            imdb_profile=actor.imdb_profile,
            images=actor.images,
            movie_credits=dicted_credits
        )
        dicted_actors.append(temp)
    data = json.dumps(dicted_actors)

    return Response(data, mimetype="application/json")


if __name__ == '__main__':
    app.run()
