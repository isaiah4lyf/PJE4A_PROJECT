try
	status = 0;
	exception = 0;
    file = load(path);
	
	trainedClassifier  = file.trainedClassifier;
	image_un = imread(Image_Name_of);
	load('webcamsSceneReconstruction.mat');
	
	image1 = imrotate(image_un,90);
	if(size(image1,3)==4) % resize image
		image1(:,:,1)=[]; % convert to I = [MxNx3]
	end
	
	image = undistortImage(image1,stereoParams.CameraParameters1);
	faceDetector = vision.CascadeObjectDetector;
	face1 = step(faceDetector,image);
	TF = isempty(face1);
	
	if TF == 0

		I = imcrop(image1,face1);
		im=rgb2gray(I);
		J = imresize(im, 5);
	

		ptsOriginal = detectSURFFeatures(J);
		%imwrite(J,Image_Name_of)
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
		max_Is = 'A';
		if ROWS_B > ROWS_A
			max_Class = class_2;
			max_Num = ROWS_B;
			max_Is = 'B';
		end
		if ROWS_C > ROWS_B
			max_Class = class_3;
			max_Num = ROWS_C;
			max_Is = 'C';
		end
		if ROWS_D > ROWS_C
			max_Class = class_2;
			max_Num = ROWS_D;
			max_Is = 'D';
		end

		predAccuracy = max_Num/m *100
		max_Num
		max_Class
	else
		status = 1;
	end	
catch ME
    switch ME.identifier
        case 'MATLAB:UndefinedFunction'
        otherwise
            exception = 1;
			ME
    end
end
