#! /bin/bash

#make folder
echo "이름을 입력해주세요 : \c"
read name 
echo "<${name}>을 등록합니다."
mkdir -p $name

#face detection from video
python face_detect.py $name

#copy files to docker 
sudo docker cp $name/. openface:/root/openface/input/us/$name
echo "docker에 데이터 복사 완료"

#train with openface 
echo "docker start"
docker start openface
docker exec -it openface /bin/bash run_training.sh
echo "docker exit"