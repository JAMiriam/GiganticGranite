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


import os
import requests
import sys

# * ActorsInfoPicker downloads info about actor and puts it to class Actor object.
# * There are three methods for downloading info, by TMDb id, IMDb id or actor name,
#   user should provide one of these values.
# * ActorsInfoPicker requires two files to work properly:
#   genres - file with python dictionary to recognize genre of movie, api_key - file with key to TMDb.


class ActorsInfoPicker:
    tmdb_id_url = "https://api.themoviedb.org/3/person/"
    imdb_id_url = "https://api.themoviedb.org/3/find/"
    name_url = "https://api.themoviedb.org/3/search/person"
    tmdb_photo = "https://image.tmdb.org/t/p/original"
    # tmdb_genres = "https://api.themoviedb.org/3/genre/movie/list"
    # tmdb_genres_tv = "https://api.themoviedb.org/3/genre/tv/list"

    def __init__(self):
        try:
            key_file = open('api_key', 'r')
        except FileNotFoundError:
            print("No \'api_key\' file found in", os.getcwd())
            sys.exit(2)

        try:
            genres = eval(open('genres', 'r').read())
        except FileNotFoundError:
            print("No \'genres\' file found in", os.getcwd())
            sys.exit(2)

        self.key = key_file.read()
        self.key_url = "?api_key=" + self.key
        self.genres = genres
        # self.set_movie_genres()

    # * use it to update genres
    # def set_movie_genres(self):
    #     url = ActorsInfoPicker.tmdb_genres + self.key_url
    #     resp = requests.request("GET", url)
    #     if resp.status_code != 200:
    #         raise ConnectionError('GET {}'.format(resp.status_code))
    #     for genre in resp.json()['genres']:
    #         self.genres[genre['id']] = genre['name']
    #     url = ActorsInfoPicker.tmdb_genres_tv + self.key_url
    #     resp = requests.request("GET", url)
    #     if resp.status_code != 200:
    #         raise ConnectionError('GET {}'.format(resp.status_code))
    #     for genre in resp.json()['genres']:
    #         if genre['id'] not in self.genres.keys():
    #             self.genres[genre['id']] = genre['name']
    #     print(self.genres)
    #     return True

    def download_movie_credits(self, actor_id):
        movie_credits = []
        url = ActorsInfoPicker.tmdb_id_url + str(actor_id) + '/combined_credits' + self.key_url
        resp = requests.request("GET", url)
        if resp.status_code != 200:
            raise ConnectionError('GET {}'.format(resp.status_code))

        if 'cast' in resp.json():
            for movie_json in resp.json()['cast']:
                movie_credit = MovieCredit(actor_id)
                movie_credit.title = movie_json['title'] if 'title' in movie_json \
                    else movie_json['name'] if 'name' in movie_json else ''
                movie_credit.vote_average = movie_json['vote_average'] if 'vote_average' in movie_json else ''
                movie_credit.poster = self.tmdb_photo + str(movie_json['poster_path']) \
                    if 'poster_path' in movie_json else ''
                if 'genre_ids' in movie_json:
                    for genre_id in movie_json['genre_ids']:
                        movie_credit.genres.append(self.genres[genre_id])
                movie_credits.append(movie_credit)

        return movie_credits

    def download_image_urls(self, actor_id):
        images = []
        url = ActorsInfoPicker.tmdb_id_url + str(actor_id) + '/images' + self.key_url
        resp = requests.request("GET", url)
        if resp.status_code != 200:
            raise ConnectionError('GET {}'.format(resp.status_code))
        if 'profiles' in resp.json():
            for image_json in resp.json()['profiles']:
                images.append(self.tmdb_photo + image_json['file_path'])
        return images

    def download_by_tmdb_id(self, id):
        downloaded_actor = Actor(id)
        url = ActorsInfoPicker.tmdb_id_url + str(id) + self.key_url
        resp = requests.request("GET", url)
        if resp.status_code != 200:
            raise ConnectionError('GET {}'.format(resp.status_code))

        downloaded_actor.name = resp.json()['name'] if 'name' in resp.json() else ''
        downloaded_actor.birthday = resp.json()['birthday'] if 'birthday' in resp.json() else ''
        downloaded_actor.deathday = resp.json()['deathday'] if 'deathday' in resp.json() else ''
        downloaded_actor.biography = resp.json()['biography'] if 'biography' in resp.json() else ''
        if 'gender' in resp.json():
            downloaded_actor.gender = 'Male' if resp.json()['gender'] == 2 else "Female" if resp.json()['gender'] == 1 \
                else 'not set'
        else:
            ''
        downloaded_actor.imdb_profile = 'http://www.imdb.com/name/{}'.format(resp.json()['imdb_id']) \
            if 'imdb_id' in resp.json() else ''

        downloaded_actor.images = self.download_image_urls(id)
        downloaded_actor.movie_credits = self.download_movie_credits(id)
        return downloaded_actor

    def download_by_imdb_id(self, id):
        url = ActorsInfoPicker.imdb_id_url + str(id) + self.key_url + '&external_source=imdb_id'
        resp = requests.request("GET", url)
        if resp.status_code != 200:
            raise ConnectionError('GET {}'.format(resp.status_code))
        if 'person_results' in resp.json() and 'id' in resp.json()['person_results'][0]:
            tmdb_id = resp.json()['person_results'][0]['id']
            return self.download_by_tmdb_id(tmdb_id)
        else:
            return False

    def download_by_name(self, name):
        url = ActorsInfoPicker.name_url + self.key_url + '&query=' + name
        resp = requests.request("GET", url)
        if resp.status_code != 200:
            raise ConnectionError('GET {}'.format(resp.status_code))
        if 'results' in resp.json() and 'id' in resp.json()['results'][0]:
            tmdb_id = resp.json()['results'][0]['id']
            return self.download_by_tmdb_id(tmdb_id)
        else:
            return False


class MovieCredit:
    def __init__(self, actor_id):
        self.title = ''
        self.genres = []
        self.vote_average = 0
        self.poster = ''

    # for testing
    def print(self):
        print('title = ' + str(self.title))
        print('genres: ')
        print(self.genres)
        print('vote average = ' + str(self.vote_average))
        print('poster url = ' + str(self.poster))


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
        # self.movie_credits.append(MovieCredit(id))

    #for testing
    def print(self):
        print('id = ' + str(self.id))
        print('name = ' + str(self.name))
        print('birthday = ' + str(self.birthday))
        print('deathday = ' + str(self.deathday))
        print('biography = ' + str(self.biography))
        print('gender = ' + str(self.gender))
        print('imdb profile = ' + str(self.imdb_profile))
        print('image urls: ')
        print(self.images)
        print('movie credits: ')
        for mc in self.movie_credits:
            mc.print()


# * ActorInfoPicker's usage example
# def main():
#     picker = ActorsInfoPicker()
#     actor = picker.download_by_tmdb_id('206')
#     actor = picker.download_by_imdb_id('nm0000614')
#     actor = picker.download_by_name('Alan Rickman')
#     actor.print()
#
# if __name__ == "__main__":
#     main()
