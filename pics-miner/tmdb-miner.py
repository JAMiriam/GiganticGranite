import os
from urllib.request import urlretrieve
import requests
import sys


class TMDbPicsMiner:
    api_url = "https://api.themoviedb.org/3/person/"
    download_url = "https://image.tmdb.org/t/p/original"
    img_url = "/images"
    tag_img_url = "/tagged_images"
    dir = "./images"

    def __init__(self, min_id, max_id):
        try:
            key_file = open('api_key', 'r')
        except FileNotFoundError:
            print("No \'api_key\' file found in", os.getcwd())
            sys.exit(2)

        self.key = key_file.read()
        self.key_url = "?api_key=" + self.key
        if not os.path.exists(TMDbPicsMiner.dir):
            os.makedirs(TMDbPicsMiner.dir)

        self.people = range(min_id, max_id)

    def download_pics(self, person_dir, pics_dict):
        if len(pics_dict) == 0:
            return False
        for pic in pics_dict:
            path = pic['file_path']
            urlretrieve(TMDbPicsMiner.download_url + path, person_dir + path)
            return True

    def __person_exists(self, id):
        url = TMDbPicsMiner.api_url + str(id) + self.key_url
        response = requests.request("GET", url)
        return True if response.status_code == 200 else False

    def mine(self):
        for person in self.people:
            if self.__person_exists(person):
                person_dir = TMDbPicsMiner.dir + "/" + str(person)
                if not os.path.exists(person_dir):
                    os.makedirs(person_dir)

                url = TMDbPicsMiner.api_url + str(person) + TMDbPicsMiner.img_url + self.key_url
                response = requests.request("GET", url)

                print("Downloading images for id:", person, end=" ... ")
                if self.download_pics(person_dir, response.json()['profiles']):
                    print("done.")
                else:
                    print("Id", person, "does not have any images.")

                url = TMDbPicsMiner.api_url + str(person) + TMDbPicsMiner.tag_img_url + self.key_url
                response = requests.request("GET", url)

                print("Downloading tagged images for id:", person, end=" ... ")
                if self.download_pics(person_dir, response.json()['results']):
                    print("done.")
                else:
                    print("Id", person, "does not have any images.")
            else:
                print("Id", person, "does not exist.")


def main(argv):
    if len(argv) > 2:
        min_id = int(argv[1])
        max_id = int(argv[2])
    elif len(argv) > 1:
        min_id = int(argv[1])
        max_id = 19129
    else:
        min_id = 1
        max_id = 19129
    miner = TMDbPicsMiner(min_id, max_id)
    miner.mine()

if __name__ == "__main__":
    main(sys.argv)
