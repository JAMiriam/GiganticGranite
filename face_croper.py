#Pierwszy argument ścieżka na folder z obrazami konwencja ściezki z '\'
import sys
import os
import dlib
import glob
import cv2
from skimage import io
detector = dlib.get_frontal_face_detector()
try:
    os.mkdir(sys.argv[1] + r'faces')
except FileExistsError:
    print("Folder exists")

for f in glob.glob(os.path.join(sys.argv[1], "*.jpg")):
    print("Processing file: {}".format(f))
    img = io.imread(f)
    dets = detector(img, 1)
    for i, d in enumerate(dets):
        crop_img = img[d.top():d.bottom(), d.left():d.right()]
        cv2.imwrite(sys.argv[1]+r'faces'+r'\face'+str(i)+f.replace(sys.argv[1],""), crop_img)



