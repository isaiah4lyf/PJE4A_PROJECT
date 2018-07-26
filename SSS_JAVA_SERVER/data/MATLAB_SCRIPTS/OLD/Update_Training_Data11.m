try
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





catch ME
    switch ME.identifier
        case 'MATLAB:UndefinedFunction'
            warning('Function is undefined.  Assigning a value of NaN.');
            %a = NaN;
        case 'MATLAB:scriptNotAFunction'
            warning(['Attempting to execute script as function. '...
                'Running script and assigning output a value of 0.']);
            notaFunction;
            %a = 0;
        otherwise
            warning('Invalid Image....');
			status = 'false';
    end
end

if status == 'true'

	I = imcrop(I1,face1);
	im=rgb2gray(I);
	J = imresize(im, 5);
	%imshowpair(J, I2, 'montage');


	%% Read image and detect interest points.

	ptsOriginal = detectSURFFeatures(J);
	   
	status = 'true';
	%% Display locations of interest in image.
	%imshow(J); hold on;
	%plot(ptsOriginal.selectStrongest(100));
	imwrite(J,image_Path)

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
else
	response = 'Code doesnt Work'
end
