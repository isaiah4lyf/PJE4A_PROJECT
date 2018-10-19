try
    
    exception = 0;

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
            exception = 1
            ME
    end
end

