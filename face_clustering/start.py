import facerec
import sys
if (len(sys.argv)>2):
    if(sys.argv[1]=="-l"):
        facerec.lrn(sys.argv[2])
    if(sys.argv[1]=="-c"):
        facerec.cls(sys.argv[2])
    if(sys.argv[1]=="-p"):
        facerec.prd(sys.argv[2])
else:
    facerec.cls(sys.argv[1])