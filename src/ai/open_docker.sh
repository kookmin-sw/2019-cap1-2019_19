#!/bin/bash
echo "DOCKER START"

docker start openface

docker exec -it openface /bin/bash run_classifier.sh

echo "DOCKER EXIT" 


