# USAGE
# python object_tracker.py --prototxt deploy.prototxt --model res10_300x300_ssd_iter_140000.caffemodel

import argparse

import cv2
import dlib
import imutils
from imutils import face_utils

from pyimagesearch.centroidtracker import CentroidTracker

# construct the argument parse and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-p", "--prototxt", default="deploy.prototxt",
                help="path to Caffe 'deploy' prototxt file")
ap.add_argument("-m", "--model", default="res10_300x300_ssd_iter_140000.caffemodel",
                help="path to Caffe pre-trained model")
ap.add_argument("-c", "--confidence", type=float, default=0.8,
                help="minimum probability to filter weak detections")
args = vars(ap.parse_args())

# initialize our centroid tracker and frame dimensions
ct = CentroidTracker()
(H, W) = (None, None)
# init age and gender parameters
MODEL_MEAN_VALUES = (78.4263377603, 87.7689143744, 114.895847746)
age_list = ['(0, 2)', '(4, 6)', '(8, 12)', '(15, 20)', '(25, 32)', '(38, 43)', '(48, 53)', '(60, 100)']
gender_list = ['Male', 'Female']

# load our serialized model from disk
print("[INFO] loading models...")
net = cv2.dnn.readNetFromCaffe(args["prototxt"], args["model"])

age_net = cv2.dnn.readNetFromCaffe(
    "age_gender_models/deploy_age.prototxt",
    "age_gender_models/age_net.caffemodel")
gender_net = cv2.dnn.readNetFromCaffe(
    "age_gender_models/deploy_gender.prototxt",
    "age_gender_models/gender_net.caffemodel")

font = cv2.FONT_HERSHEY_SIMPLEX

print("finish loading model")

while True:
    img_path = input()
    img = cv2.imread('../image-loader/src/main/resources/assets/' + img_path)

    img = imutils.resize(img, width=400)
    if W is None or H is None:
        (H, W) = img.shape[:2]

    detector = dlib.get_frontal_face_detector()
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    rects = detector(gray, 0)

    new_rects = []
    for rect in rects:
        (x, y, w, h) = face_utils.rect_to_bb(rect)
        if y < 0:
            continue
        new_rects.append((x, y, x + w, y + h))
        face_img = img[y:y + h, x:x + w].copy()
        blob2 = cv2.dnn.blobFromImage(face_img, 1, (227, 227), MODEL_MEAN_VALUES, swapRB=False)

        # Predict gender
        gender_net.setInput(blob2)
        gender_preds = gender_net.forward()
        gender = gender_list[gender_preds[0].argmax()]
        # Predict age
        age_net.setInput(blob2)
        age_preds = age_net.forward()
        age = age_list[age_preds[0].argmax()]
        overlay_text = "%s, %s" % (gender, age)
        cv2.putText(img, overlay_text, (x, y-8), font, 0.6, (255, 0, 0), 2, cv2.LINE_AA)
        cv2.rectangle(img, (x, y), (x + w, y + h), (0, 255, 0), 1)

    cv2.imwrite("../image-loader/src/main/resources/assets/1" + img_path, img)
    print("Finish :)")
