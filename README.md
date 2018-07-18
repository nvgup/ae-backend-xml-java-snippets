# AgileEngine backend-XML java snippets

You should use java -jar <your_bundled_app>.jar <input_origin_file_path> <input_other_sample_file_path> <target_element_id> to execute a program.

<target_element_id> is an optional parameter and could be omitted. In this case 'make-everything-ok-button' would be used

Jar file you can find in root folder of repository: ae-backend-xml-java-snippets-all-0.0.1.jar

Outputs example: html[0] -> body[1] -> div[2] -> .. 
The index in brackets means the element position in the parent element, starting from 0

Outputs for different files:

sample-1-evil-gemini.html: > html[0] > body[1] > div[0] > div[1] > div[2] > div[0] > div[0] > div[1] > a[1];

sample-2-container-and-clone.html: > html[0] > body[1] > div[0] > div[1] > div[2] > div[0] > div[0] > div[1] > div[0] > a[0];

sample-3-the-escape.html:

sample-4-the-mash.html: > html[0] > body[1] > div[0] > div[1] > div[2] > div[0] > div[0] > div[2] > a[0];

It is possible to configure the level of similarity of elements: see SimpleElementFinder.WEIGHT_SUCCESS_THRESHOLD. 
Higher value means that the wanted element mast be more similar to the target element and vice versa