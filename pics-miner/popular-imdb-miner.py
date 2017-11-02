import os
import re
from urllib.request import urlretrieve
import requests
import sys


class IMDbPicsMiner:
    tmdb_api_url = "https://api.themoviedb.org/3/person/"
    download_url = "https://image.tmdb.org/t/p/original"
    imdb_api_url = "http://www.theimdbapi.org/api/person?person_id="
    dir = "./images"
    pattern = re.compile('([^/]+$)')

    def __init__(self, min_page, max_page, further_iteration):
        try:
            key_file = open('api_key', 'r')
        except FileNotFoundError:
            print("No \'api_key\' file found in", os.getcwd())
            sys.exit(2)


        self.further_iteration = further_iteration
        self.key = key_file.read()
        self.key_url = "?api_key=" + self.key
        if not os.path.exists(IMDbPicsMiner.dir):
            os.makedirs(IMDbPicsMiner.dir)

        self.pages = range(min_page, max_page)

    def download_pics(self, person_dir, pics_dict):
        if not pics_dict:
            return False

        if not os.path.exists(person_dir):
            os.makedirs(person_dir)

        for link in pics_dict:
            if link is None:
                continue
            urlretrieve(link, person_dir + re.findall(IMDbPicsMiner.pattern, link)[0])
        return True


    def get_imdb_pics(self, person_imdb_id):
        url = IMDbPicsMiner.imdb_api_url + str(person_imdb_id)
        response = requests.request("GET", url)
        if response.status_code != 200:
            print("Problem occured while getting id", person_imdb_id, "from IMDb API")
            return
        if 'photos' in response.json():
            print("Downloading images for id", person_imdb_id)
            photos = response.json()['photos']
            person_dir = IMDbPicsMiner.dir + "/" + person_imdb_id +"/"
            self.download_pics(person_dir, photos)


    def mine(self):
        for page in self.pages:
            url = IMDbPicsMiner.tmdb_api_url + "popular" + self.key_url + "&page=" + str(page)
            rank_response = requests.request("GET", url)
            if rank_response.status_code != 200:
                print("Problem occured while getting rank page no", page)
                continue

            people = rank_response.json()['results'] if 'results' in rank_response.json() else None
            if not people:
                print("Page", page, "seems to be empty")
                continue

            for person in people:
                if 'id' in person:
                    person_id = person['id']
                else:
                    continue

                url = IMDbPicsMiner.tmdb_api_url + str(person_id) + self.key_url
                person_info_response = requests.request("GET", url)
                if person_info_response.status_code != 200:
                    print("Id", person, "does not exist.")
                    continue

                if 'imdb_id' in person_info_response.json():
                    person_imdb_id = person_info_response.json()['imdb_id']
                    person_dir = IMDbPicsMiner.dir + "/" + person_imdb_id

                    if self.further_iteration and os.path.exists(person_dir+"/imdb"):
                        continue

                    if not os.path.exists(person_dir):
                        os.makedirs(person_dir)
                    file = open(person_dir + "/info.txt", 'w')
                    # print(person_info_response.json()['name'] + "\n" if 'name' in person_info_response.json() else "\n")
                    # print(person_imdb_id + "\n")
                    # print(str(person_id)+ "\n")

                    file.write(person_info_response.json()['name'] + "\n" if 'name' in person_info_response.json() else "\n")
                    file.write(person_imdb_id + "\n")
                    file.write(str(person_id)+ "\n")

                    file.close()

                    self.get_imdb_pics(person_imdb_id)

                    file = open(person_dir + "/imdb", 'w')
                    file.close()



def main(argv):
    if len(argv) > 3:
        further = True if argv[1] == "t" else False
        min_id = int(argv[2])
        max_id = int(argv[3])
    elif len(argv) > 2:
        further = True if argv[1] == "t" else False
        min_id = int(argv[2])
        max_id = int(argv[2]) + 1
    else:
        print("usage: \n popular-imdb-miner.py <further_iteration (t or f)> <min_page> <max_page> \n or \n popular-imdb-miner.py <further_iteration> <page>")
        sys.exit(2)

    miner = IMDbPicsMiner(min_id, max_id, further)
    miner.mine()

if __name__ == "__main__":
    main(sys.argv)
