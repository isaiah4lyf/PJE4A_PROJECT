I1 = imread(image_Path);

%% Measure Distance from Stereo Camera to a Face
%% Load stereo parameters.

% Copyright 2015 The MathWorks, Inc.

load('webcamsSceneReconstruction.mat');
%% Read in the stereo pair of images.


%% Undistort the images.
image = undistortImage(I1,stereoParams.CameraParameters1);

%% Detect a face in both images.
faceDetector = vision.CascadeObjectDetector;
face1 = step(faceDetector,image);



I = imcrop(I1,face1);
im=rgb2gray(I);
J = imresize(im, 5);
%imshowpair(J, I2, 'montage');


%% Read image and detect interest points.

ptsOriginal = detectSURFFeatures(J);
   
%% Display locations of interest in image.
%imshow(J); hold on;
%plot(ptsOriginal.selectStrongest(100));
%imwrite(J,'C:\Users\isaia\Pictures\rihanna\new3.jpg')


[featuresOriginal,validPtsOriginal] = ...
            extractFeatures(J,ptsOriginal);

   
[m,n] = size(featuresOriginal);
A = [];

for i=1:m
    A = [A;user_ID];
end




mat = [double(A) double(featuresOriginal)];



if exist(path, 'file')
  % File exists.  Do stuff....
  file2 = load(path);
  new_File = [file2.new_File;mat];
  save(path2,'new_File');
else
  % File does not exist.
  new_File = [mat];
  save(path2,'new_File');
end


