try
	load('webcamsSceneReconstruction.mat');
	image = undistortImage(I1,stereoParams.CameraParameters1);
	faceDetector = vision.CascadeObjectDetector;
	face1 = step(faceDetector,image);
	TF = isempty(face1);

	if TF == 0
		I = imcrop(I1,face1);
		im=rgb2gray(I);
		J = imresize(im, 5);

		ptsOriginal = detectSURFFeatures(J);
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
		status = 0
	else
		status = 1
	end
	exception = 0
catch ME
    switch ME.identifier
        case 'MATLAB:UndefinedFunction'
        otherwise
            exception = 1
    end
end

	
