from flask import Flask, Response, jsonify
from actorinfo import Actor, MovieCredit, ActorsInfoPicker

import json

app = Flask(__name__)

#JSON contains 
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


@app.route('/actors/<image>')
def getActors(image):
	actors = []
	# actors.append(Actor(1))
	picker = ActorsInfoPicker()
	actor = picker.download_actor_info('4566', 'nm0000614')
	actors.append(actor)
	
	#	ToDo
	#
	# use image to run AI
	#



	# Preparing 'actors on image' JSON to return
	dicted_actors = []
	for actor in actors:
		dicted_credits = []
		for movie_credit in actor.movie_credits:
			print movie_credit
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

