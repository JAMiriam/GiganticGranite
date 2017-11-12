import os
import shutil

# This should be run from the parent directory of the "images" dir!

# Copies all 'info.txt' file from ./images subdirectories to ./info directory
# and renames them to their directory name (which should be imdb_id)


imgdir = "./images"
infodir = "./info"
if not os.path.exists(infodir):
    os.makedirs(infodir)

filename ="info.txt"

for subdir, dirs, files in os.walk(imgdir):
    print(subdir)
    print(files)
    print(dirs)
    if filename in files:
        _, new_filename = os.path.split(subdir)
        new_filename += ".txt"
        path = subdir + "/" + filename
        try:
            shutil.copy2(path, infodir + "/" + new_filename)
        except shutil.Error as e:
            print('Error: %s' % e)
        except IOError as e:
            print('Error: %s' % e.strerror)
    else:
        print("No file named", filename, "in", subdir)