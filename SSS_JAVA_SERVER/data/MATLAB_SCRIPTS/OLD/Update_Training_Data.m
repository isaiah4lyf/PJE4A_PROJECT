image = imread(image_Path);

if(size(image,3)==4) % resize image
    image(:,:,1)=[]; % convert to I = [MxNx3]
end
im=rgb2gray(image);

original=rgb2gray(image);


ptsOriginal  = detectSURFFeatures(original);
[featuresOriginal,validPtsOriginal] = ...
            extractFeatures(original,ptsOriginal);

   
[m,n] = size(featuresOriginal);
A = [];

for i=1:m
    A = [A;user_ID];
end


mat = [double(A) featuresOriginal]

file2 = load(path);
new_File = [file2.new_File;mat];
save(path,'new_File');

