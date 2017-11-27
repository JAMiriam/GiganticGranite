import os
import sys
from pymongo import MongoClient, errors


class DBConnector:
    def __init__(self):
        try:
            uri_file = open('uri', 'r')
        except FileNotFoundError:
            print("No \'uri\' file found in", os.getcwd())
            sys.exit(2)

        self.uri = uri_file.read()
        self.client = MongoClient(self.uri)
        self.db = self.client['gigantic-granite']
        self.collection = self.db['actors-test']

    ## insert single person into db
    def post(self, imdb_id, tmdb_id, name):
        p = {"_id": imdb_id,
             "tmdb_id": tmdb_id,
             "name": name}
        try:
            post_id = self.collection.insert_one(p).inserted_id
            return post_id
        except errors.DuplicateKeyError as dke:
            print('Error: %s' % dke)
            return None

    def find_actor(self, imdb_id):
        return self.collection.find_one({"_id": imdb_id})

    def find_actor_int(self, id):
        return self.collection.find_one({"internal_id": id})

# # get all documents matching query
# for post in collection.find({"_id": "nm0000093"}):
#     pprint.pprint(post)
#
# # get all documents in collection
# for post in collection.find():
#     pprint.pprint(post)


# def main():
#     connector = DBConnector()
#     print(connector.find_actor("nm0000093"))
#
# if __name__ == "__main__":
#     main()