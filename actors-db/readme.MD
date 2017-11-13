#Gigantic Granite

## Actors' names and ids database

This database is built using `mongoDB` engine
and hosted on [mongo DB Atlas](https://cloud.mongodb.com).

### Instalation

To connect to the db hosted on Atlas you will only need the Python driver.

##### Local DB
To install `mongo DB` engine locally,
see the [official tutorial](https://docs.mongodb.com/manual/administration/install-on-linux/).

##### Python driver

To install Python driver for `mongo DB`, run:

``pip install pymongo``

Tutorials and docs for `pymongo` can be found [here](https://docs.mongodb.com/ecosystem/drivers/python/).

##### GUI

Graphical interface for `mongoDB` is called _MongoDB Compass_
and can be downloaded from it's [official website](https://www.mongodb.com/download-center?jmp=docs#compass)


### DB Structure

`mongoDB` is a noSQL database stored in a JSON-like format.

Our database is called `gigantic-granite` and it currently
contains one collection called `actors-data`
(and it's copy `actors-test` just for testing purposes).

Each document in the `actors-data` collection is structured as follows:
```
{
    '_id': 'nm0000093',
    'name': 'Brad Pitt',
    'tmdb_id': 287
}
```
where:
- `_id` (`string`) is the primary key and person's ID from IMDB
- `name` (`string`) is person's name
- `tmdb_id` (`int`)  person's ID from TMDB, a secondary key, should be unique.


### Usage

#### Getting started
To see how to:
- connect to the DB,
- search for entries,
- add a new document,

see the `mongo-connect-example.py` file.

**Warning**: to connect to the external DB on Atlas, first you have to put a file named `uri` containing the URI† connection string in your cwd.


† can be found as text snippet pinned to the `#baza-nazwisk` channel on slack.

#### Building DB
To build DB from info files formated like:
```
Brad Pitt
nm0000093
287
```
stored in cwd, run `export-data-to-mongo.py` in that directory.
