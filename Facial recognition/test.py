import facerec
import sys
prec=facerec.reclass()
f=open(sys.argv[1],'rb')
print(prec.prd(f))