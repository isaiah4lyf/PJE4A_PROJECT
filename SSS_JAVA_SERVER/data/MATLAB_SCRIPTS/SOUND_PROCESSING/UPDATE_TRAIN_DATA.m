try
    
    exception = 0;
    path = "C:\Users\isaia\Desktop\DLL FILES\Model_01_1.mat";
    path2 = "C:\Users\isaia\Desktop\DLL FILES\Model_01_2.mat";
    user_ID = 2;
    wavFileName = 'C:\Users\isaia\PJE4A_PROJECT\SSS_JAVA_SERVER\data\MATLAB_TRAIN_DATA\Isaiah\AUD_20180810_193533.wav';
    ads = audioexample.Datastore(wavFileName, 'IncludeSubfolders', true,...
        'FileExtensions', '.wav', 'ReadMethod','File',...
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
	[m,n] = size(matrix);
	A = [];
	for i=1:m
		A = [A;user_ID];
	end
	mat = [double(A) double(matrix)];
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
catch ME
    switch ME.identifier
        case 'MATLAB:UndefinedFunction'
        otherwise
            exception = 1;
            ME
    end
end

