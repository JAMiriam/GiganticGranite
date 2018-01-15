from flask import Flask, Response, jsonify, request, send_file
from actorinfo import ActorsInfoPicker, Actor, MovieCredit
#from werkzeug import secure_filename
from werkzeug.utils import secure_filename

from ai_info_parser import make_dict, make_dict_logged
from db_connection import DBConnector
import json
import facerec
import sys
from flaskext.mysql import MySQL
import bcrypt
import os
import datetime
import random
import string
import cv2
import numpy as np

prec=facerec.reclass()
app = Flask(__name__)

mysql = MySQL()
app.config['MYSQL_DATABASE_USER'] = sys.argv[1]
app.config['MYSQL_DATABASE_PASSWORD'] = sys.argv[2]
app.config['MYSQL_DATABASE_DB'] = 'giganticgraniteDB'
app.config['MYSQL_DATABASE_HOST'] = 'localhost'
app.config['UPLOAD_FOLDER'] = 'images'

mysql.init_app(app)

@app.route('/actors/image', methods=['POST'])
def getActors():
    img = request.files['image']
    token = request.form.get('token')
    if not token:
        token = request.args.get('token', '')
    #print(token)
    #prec.prd(img) returns array of vectors which contain:
    #at first position "right" actor was found or "wrong" actor wasn't
    #found at current position
    #at second place is a mega position number of actor (or best guess in case of "wrong")
    #next four are position of face four int top left right bottom 
    #at this moment there can be problems with using this function
    found_actors=prec.prd(img)
    
    if token != '':
        temp = make_dict_logged(found_actors)
        dicted_actors = temp[0]
        to_history = temp[1]

        filename = secure_filename(img.filename)
        img.save(os.path.join('temp', filename))

        insertToHistory(token, to_history, img.filename)

        data = json.dumps(dicted_actors)

        return Response(data, mimetype="application/json")
    # test connection
    # dicted_actors = dict(status='ok')

    dicted_actors = make_dict(found_actors)
    # dicted_actors.append(dict(name='Alan Rickman', imdb='nm0000614'))
    data = json.dumps(dicted_actors)

    return Response(data, mimetype="application/json")


@app.route('/actors/suggestion', methods=['POST'])
def getComplaint():
    picker = ActorsInfoPicker()
    connector = DBConnector()
    resp = "OK"
    img = request.files['image']
    complaint = request.form.get('complaint')
    suggestion = json.loads(complaint)
    name = suggestion['name']
    position_id = connector.find_by_name(name)
    if position_id is not None:
        result = prec.sug(img, (suggestion['top'], suggestion['left'],
                                suggestion['right'], suggestion['bottom']), position_id)
        if result is 0:
            resp = "NOT OK"
    else:
        imdb_id, tmdb_id = picker.download_by_name(name)
        if tmdb_id is not None:
            pos_id = connector.post(imdb_id, tmdb_id, name)
            if pos_id is not None:
                prec.sug(img, (suggestion['top'], suggestion['left'],
                            suggestion['right'], suggestion['bottom']), pos_id)
            else:
                resp = "NOT OK"
        else:
            resp = "NOT OK"

    return Response(resp, mimetype="text/xml")


# JSON with details contains
#   array of Actor:
#       name: string
#       biography: string
#       birthday: string
#       deathday: string or null
#       gender: string
#       imdb_profile: string
#       images: array of string
#       movie_credits: array of MovieCredit
#           title: string
#           genres: array of string
#           vote_average: number
#           poster: string
#   
@app.route('/actordetails/<actor_id>', methods=['GET'])
def getDetails(actor_id):
    picker = ActorsInfoPicker()
    connector = DBConnector()
    actors = []

    tmdb_id = connector.find_actor(actor_id)['tmdb_id']
    pick = picker.download_actor_info(tmdb_id, actor_id)
    if pick is not False:
        actors.append(pick)
    # actors.append(picker.download_actor_info(tmdb_id, 'nm0000093'))
    # actors.append(picker.download_actor_info('4566', 'nm0000614'))

    # Preparing JSON with actor details to return
    dicted_actors = []
    for actor in actors:
        dicted_credits = []
        for movie_credit in actor.movie_credits:
            dicted_credits.append(dict(
                title=movie_credit.title,
                genres=movie_credit.genres,
                vote_average=movie_credit.vote_average,
                poster=movie_credit.poster
            ))

        temp = dict(
            name=actor.name,
            biography=actor.biography,
            birthday=actor.birthday,
            deathday=actor.deathday,
            gender=actor.gender,
            imdb_profile=actor.imdb_profile,
            images=actor.images,
            movie_credits=dicted_credits
        )
        dicted_actors.append(temp)
    data = json.dumps(dicted_actors)

    return Response(data, mimetype="application/json")

#####################################################
#   Login
#####################################################

@app.route('/register', methods=['POST'])
def register():
    username = request.form['username']
    password = request.form['password']

    conn = mysql.connect()
    cursor = conn.cursor()

    query = ('SELECT * from user where username=%s')
    cursor.execute(query, (username))
    data = cursor.fetchone()
    if data:
        return jsonify({'data':'username is unavailable'})

    hashed_password = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt())
    token = ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(30))
    
    query = ('INSERT INTO user (username, password, token) VALUES (%s, %s, %s)')
        
    cursor.execute(query, (username, hashed_password, token))
    conn.commit()


    return jsonify({'data':'ok'})


@app.route('/login', methods=['POST'])
def login():
    username = request.form['username']
    password = request.form['password']

    conn = mysql.connect()
    cursor = conn.cursor()

    query = ('SELECT * from user where username=%s')
    cursor.execute(query, (username))
    data = cursor.fetchone()

    if data:
        hash_password = data[2]

        if bcrypt.hashpw(password.encode('utf-8'), hash_password.encode('utf-8')) == hash_password.encode('utf-8'):
            return jsonify({'data':data[3]})
        else:
            return jsonify({'data':'Wrong password'})
    else:
        return jsonify({'data':'Wrong username'})

def insertToHistory(token, actors_to_history, filename):
    conn = mysql.connect()
    cursor = conn.cursor()

    query = ('SELECT userId, token from user where token like %s')
    cursor.execute(query, (token))
    data = cursor.fetchone()
    print(data)
    if data:
        query = ('SELECT MAX(historyId) from search_history ')
        cursor.execute(query)
        hist_data = cursor.fetchone()
        
        if not hist_data[0]:
            hist_id = 1
        else:
            hist_id = hist_data[0] + 1

        user_id = data[0]
        now = datetime.datetime.now()
        search_date = now.strftime("%Y-%m-%d %H:%M")
        print("dfgdfgdrf")
        query = ('INSERT INTO search_history (historyId, userId, foundActors, searchDate) VALUES (%s, %s, %s, %s)')
        
        cursor.execute(query, (hist_id, user_id, actors_to_history, search_date))
        conn.commit()

        #temp_path = os.path.join('temp', filename)
        temp_path = 'D:\\GiganticGranite\\server\\temp\\'+filename
        img = cv2.imread(temp_path)
        if img is None:
            temp_path = os.path.join('D:\GiganticGranite\server\temp', filename)
        #img = cv2.LoadImage(temp_path,CV_LOAD_IMAGE_COLOR)
        if img is not None:

            height, width = img.shape[:2]
            max_height = 100
            max_width = 160

            scaling_factor = max_height / float(height)
            if max_width/float(width) < scaling_factor:
                scaling_factor = max_width / float(width)
                # resize image
            img = cv2.resize(img, None, fx=scaling_factor, fy=scaling_factor, interpolation=cv2.INTER_AREA)

            new_name = 'img_'+str(hist_id)+'.png'
            #new_path = os.path.join('images', new_name)
            #print(new_path)
            new_path = os.path.join('D:\GiganticGranite\server\images', new_name)
            cv2.imwrite(new_path,img)
        else:
            print("cant load temp image")
        os.remove(temp_path)
    else:
        print("something went wrong")
    return None

@app.route('/get/history')
def getHistory():
    token = request.args.get('token', '')
    if token != '':
        conn = mysql.connect()
        cursor = conn.cursor()

        query = ('SELECT userId from user where token=%s')
        cursor.execute(query, (token))
        data = cursor.fetchone()

        if data:
            user_id = data[0]
            query = ('SELECT foundActors, searchDate, historyId from search_history where userId=%s')
            cursor.execute(query, (user_id))

            history = cursor.fetchall()

            dicted_history = []
            for search in history:
                temp = dict(foundActors=json.dumps(search[0].split(', ')), 
                    date=search[1],
                    image='images/img_'+str(search[2])+'.png')
                dicted_history.append(temp)

            data = json.dumps(dicted_history)

            return Response(data, mimetype="application/json")

    return jsonify({'data':'Bad token'})

@app.route('/images/<filename>')
def getImage(filename):
    filename = os.path.join('images', filename)
    if os.path.isfile(filename):
        return send_file(filename, mimetype='image/png')
    return "no image"

if __name__ == '__main__':
    app.run(debug = False, host = '156.17.227.136', port = 5000, ssl_context='adhoc')
    #app.run(debug = False, host = '79.110.197.182', port = 5000)
