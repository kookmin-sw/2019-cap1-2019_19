#!/bin/bash

cd ..
rm /root/openface/users/id/align/cache.t7
./root/openface/util/align-dlib.py /root/openface/users/id/input/. align outerEyesAndNose /root/openface/users/id/align/ --size 96

./root/openface/batch-represent/main.lua -outDir /root/openface/users/id/embedding/ -data /root/openface/users/id/align/

./root/openface/demos/classifier.py train /root/openface/users/id/embedding/