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


#		To Do!
# Prepare all required info using
# available APIs
#

class MovieCredit:
	def __init__(self, actor_id):
		self.title = ''
		self.genres = []
		self.vote_average = 0
		self.poster = ''		

class Actor:
	def __init__(self, id):
		self.id = id
		self.name = ''
		self.birthday = ''
		self.deathday = ''
		self.biography = ''
		self.gender = ''
		self.imdb_profile = ''
		self.images = []
		self.movie_credits = []
		self.movie_credits.append(MovieCredit(id))




