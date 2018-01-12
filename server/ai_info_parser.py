from db_connection import DBConnector


def make_dict(found_actors):
    dict_actors = []
    connector = DBConnector()
    for actor in found_actors:
        info = connector.find_actor_int(actor[1])
        dict_actors.append(dict(reliability=actor[0],
                                name=info['name'], imdb=info['_id'],
                                left=actor[2], top=actor[3],
                                right=actor[4], bottom=actor[5]))
    return dict_actors

def make_dict_logged(found_actors):
    dict_actors = []
    actors_to_history = []
    connector = DBConnector()
    for actor in found_actors:
        info = connector.find_actor_int(actor[1])
        actors_to_history.append(info['name'])
        dict_actors.append(dict(name=info['name'], imdb=info['_id'],
                                left=actor[2], top=actor[3],
                                right=actor[4], bottom=actor[5]))
    return dict_actors, ', '.join(actors_to_history)



# def main():
#     ret = []
#     for i in range(1, 5):
#         ret.append(("right", i, 0, 0, 10, 10))
#     print(make_dict(ret))
#
#
# if __name__ == "__main__":
#     main()


