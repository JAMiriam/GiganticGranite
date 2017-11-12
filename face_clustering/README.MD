## Face Clustering
### [Anaconda](https://www.anaconda.com/download/)

To be able to use face_clustering you need to install Python and quite a few libraries in correct versions. The easiest way is to download Anaconda which has most of them. Chose correct version for your system [here](https://www.anaconda.com/download/). I recommend to add Anaconda's bin folder to your $PATH as default Python. 

### Dlib and OpenCV
If you working on Windows 10, have gpu compatible with cuda and processor with avx instruction you can use dlib.pyd in face_clustering's directory. In that case don't download dlib.

If you don't have cuda delete file dlib.pyd

There are two libraries, you need to add to your Anaconda Environment. Dlib and OpenCV. Open your terminal (or if you on Windows open Anaconda Navigator, go to environment, choose root, click triangle, open terminal) and type:

``pip install dlib``

``pip install opencv-python``

### CUDA
To be able to use cuda with dlib you need to download and install CUDA 9 and cudnn 7 from nvidia website. After instalation of CUDA 9 (in default location) copy-past the content of cudnn archive into cuda's directory. Reboot.

### Compilation
Befor use you need to compile pyx code via Cython
(If you on Windows 10, and have processor with avx instruction you can skip compilation)

``python setup.py build_ext --inplace``

#### Usage
``python start.py <path to folder with images> ``
