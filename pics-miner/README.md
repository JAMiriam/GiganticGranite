## Mining images

### [TMDb](https://themoviedb.org)
To mine from TMDb, first get your API key
(by [registering an account](https://www.themoviedb.org/account/signup)
and then [creating your key](https://www.themoviedb.org/settings/api))
and put it in the `api_key` file in your working directory.

To download images and tagged images of people with ids
in range [`<min_id>`, `<max_id>`), run:

``python3 tmdb-miner.py <min_id> <max_id>``

or for single `<id>` run:
``python3 tmdb-miner.py <id>``

Images will be saved in `./images` in specific folder for each id
named as IMDb id if specified or as TMDb id otherwise. 


### [IMDb](http://www.imdb.com) (by TMDb's popularity rank)

Script downloads IMDb pictures of currently most popular actors in 
TMDb's popularity rank. 

A unit in this script is one page (i.e. 20 people). 


#### Usage
``python3 popular-imdb-miner.py <further_iteration> <min_page> <max_page>``

or
 
``python3 popular-imdb-miner.py <further_iteration> <page>``

where:
- `<futher_iteration>` specifies whether images 
of people, whose IMDb photos have already been downloaded, should be 
downloaded again (`t` if  not, i.e. this is just update for new popular actors, 
`f` otherwise, i.e. whole database will be renewed).
- `<min_page>` and `<max_page>` specifies the range of pages: `[<min_page>, <max_page>)`


Images will be saved in `./images` in specific folder for each person,
named after their IMDb id.
