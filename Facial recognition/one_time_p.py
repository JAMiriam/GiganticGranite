import sys
def otp(path):
    import os
    import dlib
    import numpy as np
    import glob
    import cv2
    import math
    import pickle
    predictor_path = "shape_predictor_5_face_landmarks.dat"
    face_rec_model_path = "dlib_face_recognition_resnet_model_v1.dat"
    detector = dlib.cnn_face_detection_model_v1("mmod_human_face_detector.dat")
    sp = dlib.shape_predictor(predictor_path)
    facerec = dlib.face_recognition_model_v1(face_rec_model_path)
    img = cv2.imread(path)
    (x,y,z)=(img.shape)
    descriptors = []
    if(x*y>(1920*1080)):
        sizer=math.sqrt(((1920*1080))/(x*y))
        img = cv2.resize(img,None,fx=sizer, fy=sizer, interpolation =  cv2.INTER_AREA )
    dets = detector(img, 1)
    for k, dr in enumerate(dets):
        d=dr.rect
        shape = sp(img, d)
        face_descriptor = facerec.compute_face_descriptor(img, shape)
        descriptors.append(face_descriptor)
        print(face_descriptor)
    pickle.dump(descriptors, open("z.p","wb"), pickle.HIGHEST_PROTOCOL)
otp(sys.argv[1])