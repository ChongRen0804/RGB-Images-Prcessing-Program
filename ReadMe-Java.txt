To run the code from command line, first compile with:

>> javac ImageDisplay.java

and then, you can run it to take in Three parameters:

>> java ImageDisplay pathToRGBVideo mode magnification_times anti_aliasing

The first parameter is the name of the image, which will be provided in an 8 bit
per channel RGB format (Total 24 bits per pixel). You may assume that all images will be of the same size for this assignment, more information on the image format will be placed on the DEN class website

The second parameter will be a mode. It will have an input of 1 or 2. Depending on the input you are asked to process the image separately. For mode 1 you will be rescaling/resampling your image. Mode 2 represents an interactive application. Both these modes are described in more detail below

The third parameter is a floating-point value suggesting by how much the image has to be scaled, such as 0.5 or 1.2 This single number will scale both width and height, resulting in re-sampling your image.
The fourth parameter will be a boolean value (0 or 1) suggesting whether you want to deal with aliasing. A 0 signifies do nothing (aliasing will remain in your output) and a 1 signifies that anti-aliasing should be performed.
