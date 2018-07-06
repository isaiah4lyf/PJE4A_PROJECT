file = load(path);

trainedClassifier  = file.trainedClassifier;


image1 = imread(Image_Name_of);
%% Measure Distance from Stereo Camera to a Face
%% Load stereo parameters.

% Copyright 2015 The MathWorks, Inc.

load('webcamsSceneReconstruction.mat');
%% Read in the stereo pair of images.


%% Undistort the images.

if(size(image1,3)==4) % resize image
    image1(:,:,1)=[]; % convert to I = [MxNx3]
end


image = undistortImage(image1,stereoParams.CameraParameters1);

%% Detect a face in both images.
faceDetector = vision.CascadeObjectDetector;
face1 = step(faceDetector,image);



I = imcrop(image1,face1);
im=rgb2gray(I);
J = imresize(im, 5);
%imshowpair(J, I2, 'montage');


%% Read image and detect interest points.

ptsOriginal = detectSURFFeatures(J);
   
%% Display locations of interest in image.
imshow(J); hold on;
plot(ptsOriginal.selectStrongest(100));
%imwrite(J,'C:\Users\isaia\Pictures\rihanna\new3.jpg')


[featuresOriginal,validPtsOriginal] = ...
            extractFeatures(J,ptsOriginal);

trained = trainedClassifier.predictFcn(featuresOriginal);
[m,n] = size(trained);


A = [];


for i=1:m
    if trained(i) == class_1
      A = [A;0];
    end
end
[ROWS_A,COLUMS_A] = size(A);


B = [];

for i=1:m
    if trained(i) == class_2
      B = [B;1];
    end
end
[ROWS_B,COLUMS_B] = size(B);


C = [];

for i=1:m
    if trained(i) == class_3
      C = [C;1];
    end
end
[ROWS_C,COLUMS_C] = size(C);

D = [];

for i=1:m
    if trained(i) == class_4
      D = [D;1];
    end
end
[ROWS_D,COLUMS_D] = size(D);


max_Class = class_1;
max_Num = ROWS_A;

if ROWS_B > ROWS_A
 	max_Class = class_2;
	max_Num = ROWS_B;
end

if ROWS_C > ROWS_B
 	max_Class = class_3;
	max_Num = ROWS_C;
end

if ROWS_D > ROWS_C
 	max_Class = class_2;
	max_Num = ROWS_D;
end

max_Num
max_Class


