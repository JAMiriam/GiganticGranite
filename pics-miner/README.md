## Mining images

### [TMDb](https://themoviedb.org)
To mine from TMDb, first get your API key
(by [registering an account](https://www.themoviedb.org/account/signup)
and then [creating your key](https://www.themoviedb.org/settings/api))
and put it in the `api_key` file in your working directory.

To download images and tagged images of people with ids
in range [`<min_id>`, `<max_id>`), run:

``python3 tmdb-miner.py <min_id> <max_id>``

Images will be saved in `./images`.
