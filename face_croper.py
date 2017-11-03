#Pierwszy argument ścieżka na folder z obrazami
import sys
import os
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
for f in glob.glob(os.path.join(sys.argv[1], "*.jpg")):
    print("Processing file: {}".format(f))
    img = cv2.imread(f)
    dets = detector(img, 1)
    for i, d in enumerate(dets):
        crop_img = img[d.top():d.bottom(), d.left():d.right()]
        cv2.imwrite(os.path.join(sys.argv[1],(r'faces'+r'\face'+str(i)+os.path.basename(f))), crop_img,quality)



