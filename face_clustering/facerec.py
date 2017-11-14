def lrn(path):
    import os
    import glob
    import pickle
    import numpy as np
    from sklearn.svm import LinearSVC
    X=[]
    Y=[]
    for f in glob.glob(os.path.join(path, "*.p")):
        faces=pickle.load(open(f, "rb"))
        for x in faces:
            print(np.asarray(x))
            X.append(np.asarray(x))
            Y.append(os.path.basename(f))
    X=np.asarray(X)
    Y=np.asarray(Y)
    clf = LinearSVC()
    clf.fit(X, Y)
    pickle.dump(clf, open("brain.b", "wb"), pickle.HIGHEST_PROTOCOL)
def cls(path):
    import os
    import dlib
    import numpy as np
    import glob
    import cv2
    import pickle
    predictor_path = "shape_predictor_5_face_landmarks.dat"
    face_rec_model_path = "dlib_face_recognition_resnet_model_v1.dat"
    detector = dlib.cnn_face_detection_model_v1("mmod_human_face_detector.dat")
    sp = dlib.shape_predictor(predictor_path)
    facerec = dlib.face_recognition_model_v1(face_rec_model_path)
    mainpath=path
    dics=[x[0] for x in os.walk(path)]
    dics=dics[1:]
    print(dics)
    for dic in dics:
        path=dic
        descriptors = []
        for f in glob.glob(os.path.join(path, "*.jpg")):
            img = cv2.imread(f)
            dets = detector(img, 1)
            for k, dr in enumerate(dets):
                d=dr.rect
                shape = sp(img, d)
                face_descriptor = facerec.compute_face_descriptor(img, shape)
                descriptors.append(face_descriptor)
        labels = dlib.chinese_whispers_clustering(descriptors, 0.5)
        num_classes = len(set(labels))
        biggest_class = None
        biggest_class_length = 0
        for i in range(0, num_classes):
            class_length = len([label for label in labels if label == i])
            if class_length > biggest_class_length:
                biggest_class_length = class_length
                biggest_class = i
        faces = []
        for i, label in enumerate(labels):
            if label == biggest_class:
                faces.append(descriptors[i])
        pickle.dump(faces, open(os.path.join(mainpath, os.path.basename(path)+".p"),"wb"), pickle.HIGHEST_PROTOCOL)
        print(os.path.join(mainpath, os.path.basename(path)+".p"))
def prd(path):
    import pickle
    import cv2
    import dlib
    from sklearn.svm import LinearSVC
    img = cv2.imread(path)
    predictor_path = "shape_predictor_5_face_landmarks.dat"
    face_rec_model_path = "dlib_face_recognition_resnet_model_v1.dat"
    detector = dlib.get_frontal_face_detector()
    sp = dlib.shape_predictor(predictor_path)
    facerec = dlib.face_recognition_model_v1(face_rec_model_path)
    dets = detector(img, 1)
    descriptors=[]
    for k, d in enumerate(dets):
        shape = sp(img, d)
        face_descriptor = facerec.compute_face_descriptor(img, shape)
        descriptors.append([face_descriptor,d.left,d.right,d.top,d.bottom])
    clf=LinearSVC()
    clf= pickle.load(open("brain.p", "rb"))
    for des in descriptors:
        print(clf.predict(des[0].reshape(1, -1)),des[1],des[2],des[3],des[4])







