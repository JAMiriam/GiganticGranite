import sys
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
            Y.append(str(i)+os.path.basename(f))
        i=i+1
    X=np.asarray(X)
    Y=np.asarray(Y)
    clf = OneVsRestClassifier(LinearSVC())
    clf.fit(X, Y)
    pickle.dump(clf, open("brain.p", "wb"), pickle.HIGHEST_PROTOCOL)
lrn(sys.argv[1])