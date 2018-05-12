file = load(path);
[trainedClassifier, validationAccuracy] = trainClassifier_ESS1(file.new_File);
validationAccuracy*100

save(path2,'trainedClassifier');
