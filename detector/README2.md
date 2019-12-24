# People-tracking-with-Age-and-Gender-detection
A combination between people tracking and age and gender detection

![alt text](https://github.com/habom2310/People-tracking-with-Age-and-Gender-detection/blob/master/result.PNG)
# Abstract
- Combining people tracking with age and gender detection is a good idea for many and many applications in real life scenarios such as caffe store management to gather the information of customers for further analysis, or in/out people control for security purposes in buildings ...
- This is just a small step of putting the state-of-the-art image processing techniques together.

# Method
- Firstly, faces are detected in the frame using the famous caffe model _res10_300x300_ssd_iter_140000.caffemodel_.
- Secondly, age and gender of every person is predicted also using caffe models _age_net_ and _gender_net_.
- Each person is then assigned an ID and tracked over time, even when they are out of the frame for not so long (5 seconds), whenever they come back in the frame, their ID will remain the same. 
- A picture of the person is then saved with the information of him/her.

# Requirements
```
pip install requirements.txt
```
# Implementation
```
python detect_and_tracker.py
```

# Result
- I can get 16fps in my Core i5 desktop with the solution of 640x480.
- If you want to train your own models for age and gender detection, have a look at https://github.com/dpressel/rude-carnie.

# References
A great thank to those who have done fantastic work
- https://github.com/eveningglow/age-and-gender-classification
- https://www.pyimagesearch.com/2018/07/23/simple-object-tracking-with-opencv/

# Notes
- Inform me in case you have any problem with running the code.
- Any related idea is welcome at khanhhanguyen2310@gmail.com



