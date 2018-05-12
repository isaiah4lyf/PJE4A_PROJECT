file = load(path);

trainedClassifier  = file.trainedClassifier;


image = imread(Image_Name_of);
if(size(image,3)==4) % resize image
    image(:,:,1)=[]; % convert to I = [MxNx3]
end

im=rgb2gray(image);
original=rgb2gray(image);
ptsOriginal  = detectSURFFeatures(original);

[featuresOriginal,validPtsOriginal] = ...
            extractFeatures(original,ptsOriginal);


trained = trainedClassifier.predictFcn(featuresOriginal);
[m,n] = size(trained);

A = [];

for i=1:m
    if trained(i) == 0
      A = [A;0];
    end
end

B = [];

for i=1:m
    if trained(i) == 1
      B = [B;1];
    end
end

[Gog_F,C] = size(A);
[Cow_F,J] = size(B);
Gog_F
Cow_F