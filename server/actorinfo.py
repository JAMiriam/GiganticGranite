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
# * The main method requires both tmdb id and imdb id.
# * ActorsInfoPicker requires two files to work properly:
#   genres - file with python dictionary to recognize genre of movie, api_key - file with key to TMDb.


class ActorsInfoPicker:
    tmdb_id_url = "https://api.themoviedb.org/3/person/"
    imdb_id_url = "https://api.themoviedb.org/3/find/"
    name_url = "https://api.themoviedb.org/3/search/person"
    tmdb_photo = "https://image.tmdb.org/t/p/original"
    append_link = "&append_to_response=images,combined_credits"
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
        # check if the key is valid and does not have new line
        if self.key[len(self.key) - 1] == '\n':
            self.key = self.key[:len(self.key) - 1]
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

    # Main method to download info
    def download_actor_info(self, tmdb_id, imdb_id):
        downloaded_actor = Actor(imdb_id)
        url = self.tmdb_id_url + str(tmdb_id) + self.key_url + self.append_link
        resp = requests.request("GET", url)
        if resp.status_code != 200:
            print(resp.json()['status_code'], resp.json()['status_message'])
            raise ConnectionError('GET {}'.format(resp.status_code))

        downloaded_actor.name = str(resp.json()['name']) if 'name' in resp.json() else ''
        downloaded_actor.birthday = str(resp.json()['birthday']) if 'birthday' in resp.json() else ''
        downloaded_actor.deathday = str(resp.json()['deathday']) if 'deathday' in resp.json() else ''
        downloaded_actor.biography = str(resp.json()['biography']) if 'biography' in resp.json() else ''
        if 'gender' in resp.json():
            downloaded_actor.gender = 'Male' if resp.json()['gender'] == 2 else "Female" if resp.json()['gender'] == 1 \
                else 'not set'
        else:
            ''
        downloaded_actor.imdb_profile = 'http://www.imdb.com/name/{}'.format(resp.json()['imdb_id']) \
            if 'imdb_id' in resp.json() else ''

        downloaded_actor.images = self.download_image_urls(resp)
        # downloaded_actor.movie_credits = self.download_movie_credits(downloaded_actor.id, resp)
        downloaded_actor.movie_credits = self.download_popular_movie_credits(downloaded_actor.id, resp)
        return downloaded_actor

    def download_movie_credits(self, actor_id, resp):
        movie_credits = []
        if 'combined_credits' in resp.json() and 'cast' in resp.json()['combined_credits']:
            for movie_json in resp.json()['combined_credits']['cast']:
                movie_credit = MovieCredit(actor_id)
                movie_credit.title = str(movie_json['title']) if 'title' in movie_json \
                    else str(movie_json['name']) if 'name' in movie_json else ''
                movie_credit.vote_average = str(movie_json['vote_average']) if 'vote_average' in movie_json else ''
                movie_credit.poster = self.tmdb_photo + str(movie_json['poster_path']) \
                    if 'poster_path' in movie_json else ''
                if 'genre_ids' in movie_json:
                    for genre_id in movie_json['genre_ids']:
                       if genre_id in self.genres.keys(): 
                            movie_credit.genres.append(self.genres[genre_id])
                       else:
                            print('No genre with id = '+str(genre_id))
                movie_credits.append(movie_credit)

        return movie_credits

    def download_popular_movie_credits(self, actor_id, resp):
        movie_credits = []
        if 'combined_credits' in resp.json() and 'cast' in resp.json()['combined_credits']:
            size = 5
            popular_movie = []
            for movie_json in resp.json()['combined_credits']['cast']:
                popular = float(movie_json['popularity']) if 'popularity' in movie_json else 0.0
                counter = 0

                for movie in popular_movie:
                    mpop = float(movie['popularity']) if 'popularity' in movie else 0.0
                    if popular < mpop:
                        counter = counter + 1
                if counter < len(popular_movie):
                    if len(popular_movie) > size:
                        temp = popular_movie[counter:size-1]
                    else:
                        temp = popular_movie[counter:len(popular_movie)-1]
                    popular_movie[counter] = movie_json
                    popular_movie = popular_movie[:counter+1] + temp
                elif counter < size:
                    popular_movie.append(movie_json)

            for movie_json in popular_movie:
                movie_credit = MovieCredit(actor_id)
                movie_credit.title = str(movie_json['title']) if 'title' in movie_json \
                    else str(movie_json['name']) if 'name' in movie_json else ''
                movie_credit.vote_average = str(movie_json['vote_average']) if 'vote_average' in movie_json else ''
                movie_credit.poster = self.tmdb_photo + str(movie_json['poster_path']) \
                    if 'poster_path' in movie_json else ''
                if 'genre_ids' in movie_json:
                    for genre_id in movie_json['genre_ids']:
                        if genre_id in self.genres.keys():
                            movie_credit.genres.append(self.genres[genre_id])
                        else:
                            print('No genre with id = ' + str(genre_id))
                movie_credits.append(movie_credit)

        return movie_credits

    def download_image_urls(self, resp):
        images = []
        if 'images' in resp.json() and 'profiles' in resp.json()['images']:
            for image_json in resp.json()['images']['profiles']:
                if 'file_path' in image_json:
                    images.append(self.tmdb_photo + str(image_json['file_path']))
        return images

    def download_by_name(self, name):
        url = ActorsInfoPicker.name_url + self.key_url + '&query=' + name
        resp = requests.request("GET", url)
        if resp.status_code != 200:
            raise ConnectionError('GET {}'.format(resp.status_code))
        results_count = resp.json()['total_results'] if 'total_results' in resp.json() else 0
        if results_count > 0 and 'id' in resp.json()['results'][0]:
            tmdb_id = resp.json()['results'][0]['id']
            url = self.tmdb_id_url + str(tmdb_id) + self.key_url
            resp = requests.request("GET", url)
            if resp.status_code != 200:
                # print(resp.json()['status_code'], resp.json()['status_message'])
                raise ConnectionError('GET {}'.format(resp.status_code))
            if 'imdb_id' in resp.json():
                imdb_id = str(resp.json()['imdb_id'])
                return imdb_id, tmdb_id
            else:
                return None, None
        else:
            return None, None


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
#     actor = picker.download_actor_info('4566', 'nm0000614')
#     actor.print()
#
# if __name__ == "__main__":
#     main()
