try

    exception = 0;
    file = load(path);
	trainedClassifier  = file.trainedClassifier;

	ads = audioDatastore(audio_path, 'IncludeSubfolders', true,...
        'FileExtensions', '.wav',...
        'LabelSource','foldernames');
    [trainDatastore, testDatastore]  = splitEachLabel(ads,0.80);
    [sampleTrain, info] = read(trainDatastore);
    reset(trainDatastore);
    lenDataTrain = length(trainDatastore.Files);
    features = cell(lenDataTrain,1);
    for i = 1:lenDataTrain
        [dataTrain, infoTrain] = read(trainDatastore);
        features{i} = HelperComputePitchAndMFCC(dataTrain,infoTrain);
    end
    features = vertcat(features{:});
    features = rmmissing(features);
    featureVectors = features{:,2:15};
    m = mean(featureVectors);
    s = std(featureVectors);
    features{:,2:15} = (featureVectors-m)./s;
    matrix = table2array(features(:,2:14));
	
	trained = trainedClassifier.predictFcn(matrix);
	
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

	predAccuracy = max_Num/m *100
	max_Num
	max_Class
catch ME
    switch ME.identifier
        case 'MATLAB:UndefinedFunction'
        otherwise
            exception = 1;
			ME
    end
end

