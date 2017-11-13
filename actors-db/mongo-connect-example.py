import pymongo
from pymongo import MongoClient, errors
import pprint


uri_file = open("uri", "r")
uri = uri_file.read()

# client = MongoClient(uri) # connect to atlas server
client = MongoClient() # local db (for testing purposes)
db = client['gigantic-granite'] # our db name
collection = db['actors-test'] # collection name (sth a bit like table in sql)


## insert single person into db
def post(imdb_id, tmdb_id, name):
    p = {"_id": imdb_id,
         "tmdb_id": tmdb_id,
         "name": name}
    try:
        post_id = collection.insert_one(p).inserted_id
        return post_id
    except errors.DuplicateKeyError as dke:
        print('Error: %s' % dke)
        return None


print(post("nm0000093", "287", "Brad Pitt"), "inserted to db")

# get single document matching query
pprint.pprint(collection.find_one({"_id": "nm0000093"}))

# get all documents matching query
for post in collection.find({"_id": "nm0000093"}):
    pprint.pprint(post)

# get all documents in collection
for post in collection.find():
    pprint.pprint(post)