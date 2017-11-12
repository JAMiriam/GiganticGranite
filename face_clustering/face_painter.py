#Pierwszy argument ścieżka na folder z obrazami
import sys
import os
import numpy as np
import dlib
import glob
import cv2
from skimage import io
detector = dlib.get_frontal_face_detector()
try:
    os.mkdir(os.path.join(sys.argv[1],r'faces'))
except FileExistsError:
    print("Folder exists")
quality=[int(cv2.IMWRITE_JPEG_QUALITY), 100]
predictor = dlib.shape_predictor("shape_predictor_68_face_landmarks.dat")
for f in glob.glob(os.path.join(sys.argv[1], "*.jpg")):
    print("Processing file: {}".format(f))
    img = cv2.imread(f)
    dets = detector(img, 1)
    color = (0, 0, 0)
    print("Number of faces detected: {}".format(len(dets)))
    shapes=[]
    for i, d in enumerate(dets):
        shape = predictor(img, d)
        jaw = []
        lefteye = []
        righteye = []
        for x in range(0, 17):
            jaw.append((shape.part(x).x, shape.part(x).y))
        for x in range(36, 42):
            lefteye.append((shape.part(x).x, shape.part(x).y))
        for x in range(43, 48):
            righteye.append((shape.part(x).x, shape.part(x).y))
        jaw = np.asarray(jaw)
        lefteye = np.asarray(lefteye)
        righteye = np.asarray(righteye)
        shapes.append([jaw,lefteye,righteye])
    for i, d in enumerate(dets):
        new_img=img.copy()
        for j,r in enumerate(dets):
            if(i!=j):
                cv2.fillConvexPoly(new_img, shapes[j][0], (0, 0, 0))
                cv2.fillConvexPoly(new_img, shapes[j][1], (0, 0, 0))
                cv2.fillConvexPoly(new_img, shapes[j][2], (0, 0, 0))
        cv2.imwrite(os.path.join(sys.argv[1],(r'faces'+r'\face'+str(i)+os.path.basename(f))), new_img,quality)



