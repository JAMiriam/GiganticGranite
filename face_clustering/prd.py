import sys
def prd(path):
    import pickle
    import cv2
    import dlib
    import numpy as np
    img = cv2.imread(path)
    cnn_face_detector = dlib.cnn_face_detection_model_v1("mmod_human_face_detector.dat")
    predictor_path = "shape_predictor_5_face_landmarks.dat"
    face_rec_model_path = "dlib_face_recognition_resnet_model_v1.dat"
    detector = dlib.get_frontal_face_detector()
    sp = dlib.shape_predictor(predictor_path)
    facerec = dlib.face_recognition_model_v1(face_rec_model_path)
    dets = cnn_face_detector(img, 1)
    descriptors=[]
    for k, dr in enumerate(dets):
        d=dr.rect
        shape = sp(img, d)
        face_descriptor = facerec.compute_face_descriptor(img, shape)
        descriptors.append([np.asarray(face_descriptor),d.left(),d.right(),d.top(),d.bottom()])
    clf= pickle.load(open("brain.p", "rb"))
    for des in descriptors:
        guess=clf.predict(des[0].reshape(1, -1))
        guess_score=clf.score(des[0].reshape(1, -1),guess)
        print(guess,guess_score,clf.predict_proba(des[0].reshape(1, -1)),des[1],des[2],des[3],des[4])
prd(sys.argv[1])