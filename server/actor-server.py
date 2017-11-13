from flask import Flask, Response, jsonify, request
from actorinfo import ActorsInfoPicker, Actor, MovieCredit
from werkzeug import secure_filename

import json


app = Flask(__name__)


@app.route('/actors/image', methods=['POST'])
def getActors():
	img = request.files['image'];

	img.save(secure_filename(img.filename))
	# 		TODO
	# Use image to run AI
	# find actor's name for specific id
	# create JSON with array of id, name, position
	#

	# test connection
	dicted_actors = dict(status = 'ok')	
	data = json.dumps(dicted_actors)
	
	return Response(data, mimetype="application/json")



#JSON with details contains 
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
	actors = []
	
	actors.append(picker.download_by_imdb_id(actor_id))

	# Preparing JSON with actor details to return
	dicted_actors = []
	for actor in actors:
		dicted_credits = []
		for movie_credit in actor.movie_credits:
			
			dicted_credits.append(dict(
					title = movie_credit.title,
					genres = movie_credit.genres,
					vote_average = movie_credit.vote_average,
					poster = movie_credit.poster
				))

		temp = dict(
				name = actor.name,
				biography = actor.biography,
				birthday = actor.birthday,
				deathday = actor.deathday,
				gender = actor.gender,
				imdb_profile = actor.imdb_profile,
				images = actor.images,
				movie_credits = dicted_credits
			)
		dicted_actors.append(temp)
	data = json.dumps(dicted_actors)

	return Response(data, mimetype="application/json")



if __name__ == '__main__':
    app.run()


