import sys
def rename(path):
    import os
    import glob
    i=1
    for f in glob.glob(os.path.join(path, "*.p")):
        if(os.path.basename(f)!="z.p"):
            os.rename(f,os.path.join(os.path.dirname(f),str(i)+".p"))
            i=i+1
rename(sys.argv[1])