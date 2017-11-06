## Face Painter

### [Anaconda](https://www.anaconda.com/download/)

To be able to use Face_Painter you need to install Python and quite a few libraries in correct versions. The easiest way is to download Anaconda which has most of them. Chose correct version for your system [here](https://www.anaconda.com/download/). I recommend to add Anaconda's bin folder to your $PATH as default Python. 

### Dlib and OpenCV

There are two libraries, you need to add to your Anaconda Environment. Dlib and OpenCV. Open your terminal (or if you on Windows open Anaconda Navigator, go to environment, choose root, click triangle, open terminal) and type:

``conda config --add channels conda-forge ``

``conda install dlib``

``conda install opencv``

#### Usage
``python Face_Painter.py <path to folder with images> ``
