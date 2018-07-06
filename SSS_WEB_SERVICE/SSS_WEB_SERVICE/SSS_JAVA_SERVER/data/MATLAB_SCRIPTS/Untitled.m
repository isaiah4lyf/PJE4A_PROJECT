path = 'C:/Users/isaia/PJE4A_PROJECT/SSS_WEB_SERVICE/SSS_WEB_SERVICE/MATLAB_TRAIN_DATA/IMAGES_FEATURES.mat';
path2 = 'C:/Users/isaia/PJE4A_PROJECT/SSS_WEB_SERVICE/SSS_WEB_SERVICE/MATLAB_TRAIN_DATA/IMAGES_FEATURES_LABELS.mat';


file2 = load(path2);
file = load(path);

mat1 = transpose(double(file2.labels));
mat2 = transpose(file.new_File);
train_Matrix = [mat1;mat2];