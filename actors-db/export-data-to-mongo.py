import os
import pprint

import pymongo
from pymongo import MongoClient, errors


uri_file = open("uri", "r")
uri = uri_file.read()

client = MongoClient(uri) # connect to atlas server
# client = MongoClient() # local db (for testing purposes)
db = client['gigantic-granite']

# collection = db['actors-data'] # do not use this db on server unless you know exactly what you're doing!!!
collection = db['actors-test'] # db copy for testing

# rootdir = "./info"
rootdir = "./"



for subdir, dirs, files in os.walk(rootdir):
    for filename in files:

        print("Reading file", filename)
        file = open(subdir + "/" + filename, "r")

        name = file.readline().rstrip('\r\n')
        imdb_id = file.readline().rstrip('\r\n')
        tmdb_id = int(file.readline())

        post = {"_id": imdb_id,
                "tmdb_id": tmdb_id,
                "name": name}
        try:
            post_id = collection.insert_one(post).inserted_id
            print(post_id, "inserted to mongoDB")
        except errors.DuplicateKeyError as dke:
            print('Error: %s' % dke)

collection.create_index([('tmdb_id', pymongo.ASCENDING)], unique=True)



# Prints out all posts in collection
for post in collection.find():
    pprint.pprint(post)
