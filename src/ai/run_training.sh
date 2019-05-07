#!/bin/bash

rm /root/openface/align/us/cache.t7
./root/openface/util/align-dlib.py /root/openface/input/. align outerEyesAndNose /root/openface/align/us --size 96

./root/openface/batch-represent/main.lua -outDir /root/openface/embedding/us/ -data /root/openface/align/us/

./root/openface/demos/classifier.py train /root/openface/embedding/us/