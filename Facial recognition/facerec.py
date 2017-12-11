def lrn(path):
    import os
    import glob
    import pickle
    import numpy as np
    from sklearn.multiclass import OneVsRestClassifier
    from sklearn.svm import LinearSVC
    X=[]
    Y=[]
    i=1
    for f in glob.glob(os.path.join(path, "*.p")):
        faces=pickle.load(open(f, "rb"))
        for x in faces:
            X.append(np.asarray(x))
            Y.append(i)
        i=i+1
    X=np.asarray(X)
    Y=np.asarray(Y)
    clf = OneVsRestClassifier(LinearSVC())
    clf.fit(X, Y)
    pickle.dump(clf, open("brain.p", "wb"), pickle.HIGHEST_PROTOCOL)
def cls(path):
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
    mainpath=path
    dics=[x[0] for x in os.walk(path)]
    dics=dics[1:]
    print(dics)
    for dic in dics:
        path=dic
        descriptors = []
        for f in glob.glob(os.path.join(path, "*.jpg")):
            img = cv2.imread(f)
            (x,y,z)=(img.shape)
            if(x*y>(1920*1080)):
                sizer=math.sqrt(((1920*1080))/(x*y))
                img = cv2.resize(img,None,fx=sizer, fy=sizer, interpolation =  cv2.INTER_AREA )
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

def sugcls(path):
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
    for f in glob.glob(os.path.join(path, "*.pb")):
        descriptors=pickle.load(open(f, "rb"))
        if len(descriptors)>=100:
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
            pickle.dump(faces, open(os.path.join(path, os.path.basename(f)[:-3]+".p"),"wb"), pickle.HIGHEST_PROTOCOL)
            os.remove(f)



def prd(path):
    import pickle
    import cv2
    import math
    import dlib
    import numpy as np
    img = cv2.imread(path)
    (x,y,z)=(img.shape)
    if(x*y>(1920*1080)):
        sizer=math.sqrt(((1920*1080))/(x*y))
        img = cv2.resize(img,None,fx=sizer, fy=sizer, interpolation =  cv2.INTER_AREA )
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
        print(guess,guess_score,clf.decision_function(des[0].reshape(1, -1)),des[1],des[2],des[3],des[4])

def rename(path):
    import os
    import glob
    i=1
    for f in glob.glob(os.path.join(path, "*.p")):
        os.rename(f,os.path.join(os.path.dirname(f),i+".p"))
        i=i+1


class reclass:
    def __init__(self):
        import pickle
        import cv2
        import math
        import dlib
        import numpy as np
        self.cnn_face_detector = dlib.cnn_face_detection_model_v1("mmod_human_face_detector.dat")
        self.predictor_path = "shape_predictor_5_face_landmarks.dat"
        self.face_rec_model_path = "dlib_face_recognition_resnet_model_v1.dat"
        self.detector = dlib.get_frontal_face_detector()
        self.sp = dlib.shape_predictor(self.predictor_path)
        self.facerec = dlib.face_recognition_model_v1(self.face_rec_model_path)
        self.clf= pickle.load(open("brain.p", "rb"))
    def prd(self,img_str):
        import numpy as np
        import cv2
        import math
        nparr = np.fromstring(img_str.read(), np.uint8)
        img = cv2.imdecode(nparr,cv2.IMREAD_COLOR)
        (x,y,z)=(img.shape)
        if(x*y>(1920*1080)):
            sizer=math.sqrt(((1920*1080))/(x*y))
            img = cv2.resize(img,None,fx=sizer, fy=sizer, interpolation =  cv2.INTER_AREA )
        dets = self.cnn_face_detector(img, 1)
        descriptors=[]
        ret=[]
        for k, dr in enumerate(dets):
            d=dr.rect
            shape = self.sp(img, d)
            face_descriptor = self.facerec.compute_face_descriptor(img, shape)
            descriptors.append([np.asarray(face_descriptor),d.left(),d.right(),d.top(),d.bottom()])
        for des in descriptors:
            guess=self.clf.predict(des[0].reshape(1, -1))
            guess_score=self.clf.score(des[0].reshape(1, -1),guess)
            fut=self.clf.decision_function(des[0].reshape(1, -1))
            print(guess,guess_score,fut,des[1],des[2],des[3],des[4])
            try:
                if(fut[0][guess[0]-1]>0):
                    ret.append(("right",guess[0],des[3],des[1],des[2],des[4]))
                else:
                    ret.append(("wrong",guess[0],des[3],des[1],des[2],des[4]))
            except:
                ret.append(("wrong",1,des[3],des[1],des[2],des[4]))
        return ret
    def sug(self,img_str,position,sugid):
        import numpy as np
        import cv2
        import os
        import pickle
        import math
        ret=1
        nparr = np.fromstring(img_str.read(), np.uint8)
        img = cv2.imdecode(nparr,cv2.IMREAD_COLOR)
        (x,y,z)=(img.shape)
        (sugtop,sugleft,sugright,sugbottom)=position
        if(x*y>(1920*1080)):
            sizer=math.sqrt(((1920*1080))/(x*y))
            img = cv2.resize(img,None,fx=sizer, fy=sizer, interpolation =  cv2.INTER_AREA )
        dets = self.cnn_face_detector(img, 1)
        theface=0
        sugdif=(x,y,y,x)
        x = np.array([x,y,y,x])
        closestnorm=np.linalg.norm(x)
        ret=[]
        for k, dr in enumerate(dets):
            d=dr.rect
            shape = self.sp(img, d)
            face_descriptor = self.facerec.compute_face_descriptor(img, shape)
            x = np.array([abs(sugtop-d.top()),abs(sugright-d.right()),abs(sugbottom-d.bottom()),abs(sugleft-d.left())])
            thisnorm=np.linalg.norm(x)
            if(thisnorm<closestnorm):
                closestnorm=thisnorm
                theface=face_descriptor
        try:
            faces=pickle.load(open(os.path.join("vector_base",str(sugid)+".p"), "rb"))
            guess=self.clf.predict(np.asarray(theface).reshape(1, -1))
            fut=self.clf.decision_function(np.asarray(theface).reshape(1, -1))
            if(fut[0][sugid]>=-0.5):
                faces.append(theface)
                pickle.dump(faces, open(os.path.join("vector_base",str(sugid)+".pb"),"wb"), pickle.HIGHEST_PROTOCOL)
            else:
                ret=0
        except FileNotFoundError as e:
            try:
                faces=pickle.load(open(os.path.join("vector_base",str(sugid)+".pb"), "rb"))
                faces.append(theface)
                pickle.dump(faces, open(os.path.join("vector_base",str(sugid)+".pb"),"wb"), pickle.HIGHEST_PROTOCOL)
            except FileNotFoundError as e:
                faces=[]
                faces.append(theface)
                pickle.dump(faces, open(os.path.join("vector_base",str(sugid)+".pb"),"wb"), pickle.HIGHEST_PROTOCOL)
        except:
            ret=0         
        return ret




