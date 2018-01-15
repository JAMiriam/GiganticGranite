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
    # def post(self, imdb_id, tmdb_id, name):
    #     p = {"_id": imdb_id,
    #          "tmdb_id": tmdb_id,
    #          "name": name}
    #     try:
    #         post_id = self.collection.insert_one(p).inserted_id
    #         return post_id
    #     except errors.DuplicateKeyError as dke:
    #         print('Error: %s' % dke)
    #         return None

    def find_by_name(self, name):
        actor = self.collection.find_one({"name": name}, {"internal_id": 1, "_id": 0})
        return actor["internal_id"] if actor is not None and "internal_id" in actor else None

    def post(self, imdb_id, tmdb_id, name):
        l = list(self.collection.find({}, {"internal_id": 1, "_id": 0}).sort("internal_id", -1).limit(1))
        internal_id = 1 + (0 if len(l) == 0 else l[0]["internal_id"])
        p = {"_id": imdb_id,
             "tmdb_id": tmdb_id,
             "name": name,
             "internal_id": internal_id}
        try:
            self.collection.insert_one(p)
            return internal_id
        except errors.DuplicateKeyError as dke:
            print('Error: %s' % dke)
            return None

    def find_actor(self, imdb_id):
        return self.collection.find_one({"_id": imdb_id})

    def find_actor_int(self, id):
        id = int(id)
        return self.collection.find_one({"internal_id": id})
    
    def swap_internal_id(self, id_from, id_to):
        if id_from == id_to:
            return
        if self.collection.find_one({"internal_id": id_from}) is not None:
            if self.collection.find_one({"internal_id": id_to}) is None:
                self.collection.update_one({'internal_id': id_from}, {'$set': {'internal_id': id_to}}, upsert=False)
            else:
                self.collection.update_one({'internal_id': id_from}, {'$set': {'internal_id': 100000000000}}, upsert=False)
                self.collection.update_one({'internal_id': id_to}, {'$set': {'internal_id': id_from}}, upsert=False)
                self.collection.update_one({'internal_id': 100000000000}, {'$set': {'internal_id': id_to}}, upsert=False)
        else:
            raise errors.PyMongoError('Actor with id %d not found' % id_from)



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
